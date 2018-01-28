package com.qzd.mobilesafe;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;
import com.qzd.mobilesafe.utils.StreamTools;
public class SplashActivity extends Activity {
	protected static final String TAG = "SplashActivity";
	protected static final int ENTER_HOME = 0;
	protected static final int SHOW_UPDATE_DIALOG = 1;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;
	private TextView tv_splash_version;
	private String version,description,apkurl;
	private TextView tv_update_info;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本号: "+getVersionName());
		tv_update_info = (TextView) findViewById(R.id.tv_update_info);
		boolean update = sp.getBoolean("update",false);
		installShortCut();
		//初始化数据库文件
		//把asset下的数据库 拷贝到系统的目录里面
		copyDB();
		if(update){
			//检查升级
			checkUpdate();
		}else{
			//自动升级已经关闭
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					//进入主页面
					enterHome();
				}
			},2000);
		}
		AlphaAnimation aa = new AlphaAnimation(0.2f,1.0f);
		aa.setDuration(500);//设置动画时间
		findViewById(R.id.rl_root_splash).startAnimation(aa);
	}
	/**
	 * 创建快捷图标
	 */
	public void installShortCut(){
		boolean shortcut = sp.getBoolean("shortcut", false);
		if(shortcut)
			return;
		Editor editor = sp.edit();
		//发送广播的意图，要创建快捷图标了
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		//快捷方式  要包含3个重要的信息 1，名称 2.图标 3.干什么事情
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机卫士");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		//桌面点击图标对应的意图。
		Intent shortcutIntent = new Intent();
			shortcutIntent.setAction("android.intent.action.MAIN");
			shortcutIntent.addCategory("android.intent.category.LAUNCHER");
			shortcutIntent.setClassName(getPackageName(), "com.qzd.mobilesafe.SplashActivity");
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
			sendBroadcast(intent);
		editor.putBoolean("shortcut", true);
		editor.commit();
	}
	/**
	 * 拷贝资产目录下的数据库文件
	 */
	private void copyDB() {
		//创建一个文件 /data/data/包名/files/address.db
		File file = new File(getFilesDir(),"address.db");
		if(file.exists()&&file.length()>0){
			Log.i(TAG, "数据库文件已经拷贝过了,无需再拷贝！！！");
		}else{
			try {
				AssetManager am = getAssets();
				InputStream is = am.open("address.db");
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int len = 0;
				while((len=is.read(buf))!=-1){
					fos.write(buf,0,len);
				}
				is.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
	}
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case SHOW_UPDATE_DIALOG: //显示升级的对话框
					Log.i(TAG,"显示升级的对话框");
					showUpdateDialog();
					break;
				case ENTER_HOME: //进入主页面
					enterHome();
					break;
				case URL_ERROR: //URL错误
					enterHome();
					Toast.makeText(getApplicationContext(),"URL错误",0).show();
					break;
				case NETWORK_ERROR: //网络异常
					Toast.makeText(getApplicationContext(),"网络错误",0).show();
					enterHome();
					break;
				case JSON_ERROR: //JSON解析出错
					Toast.makeText(SplashActivity.this,"JSON错误",0).show();
					enterHome();
					break;
			}
		}
	};
	//弹出升级对话框
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示升级");
		builder.setCancelable(false);//除了对话框的选项,其他都不能
		//监听取消的事件
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				//进入主页面
				enterHome();
				dialog.dismiss();
			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("立刻升级",new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载APK，并且替换安装
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//如果sdcard存在
					//afnal
					FinalHttp finalhttp = new FinalHttp();
					finalhttp.download(apkurl,
							Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk", 
							new AjaxCallBack<File>() {
								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									t.printStackTrace();
									Toast.makeText(getApplicationContext(),"下载失败",1).show();
									super.onFailure(t, errorNo, strMsg);
								}
								@Override
								public void onLoading(long count, long current) {
									super.onLoading(count, current);
									//当前下载百分比
									tv_update_info.setVisibility(View.VISIBLE);
									int progress = (int) (current * 100/count);
									tv_update_info.setText("下载进度: "+progress+"%");
								}
								@Override
								public void onSuccess(File t) {
									super.onSuccess(t);
									installAPK(t);
								}
								//安装APK
								private void installAPK(File t) {
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(
											Uri.fromFile(t),
											"application/vnd.android.package-archive");
									startActivity(intent);
								}
								
							});
				}else{
					Toast.makeText(getApplicationContext(),"没有sdcard,请安装上在试",0).show();
				}
			}
		});
		builder.setNegativeButton("下次再说",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();//销毁对话框
				enterHome();//进入主页面
			}
		});
		builder.show();
	}
	private void enterHome() {
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		//关闭当前Activity页面
		finish();
	};
	//检查是否有新版本,如果有就升级
	 private void checkUpdate() {
		new Thread(){
			@Override
			public void run() {
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();//开始时间
				try {
					URL url = new URL(getString(R.string.serverurl));
					//联网
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					//设置请求方法
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();
					if(code == 200){
						//联网成功
						InputStream is = conn.getInputStream();
						//把流转换成字符串String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG,"联网成功 "+result);
						//JSON解析
						JSONObject obj = new JSONObject(result);
						version = (String) obj.get("version");
						description = (String) obj.get("description");
						apkurl = (String) obj.get("path");
						//校验是否有新版本
						if(getVersionName().equals(version)){
							//版本一致.没有新版本,进入主页面
							msg.what = ENTER_HOME;
						}else{
							//有新版本,弹出一升级对话框
							msg.what = SHOW_UPDATE_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					msg.what = URL_ERROR;
					e.printStackTrace();
				}catch (IOException e) {
					msg.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = JSON_ERROR;
					e.printStackTrace();
				}finally{
					long endTime = System.currentTimeMillis();//结束的时间
					long dTime = endTime - startTime;//算出花费的时间
					if(dTime < 2000){
						try {
							Thread.sleep(2000-dTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
				}
			}
		}.start();
	}
	// 得到应用程序的版本名称
	private String getVersionName(){
		//用来管理手机的APK
		PackageManager pm = getPackageManager();
		//得到指定APK的功能清单文件
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(),0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

}





