package com.qzd.mobilesafe.service;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.drm.DrmStore.Action;
import android.os.IBinder;

import com.qzd.mobilesafe.EnterPwdActivity;
import com.qzd.mobilesafe.db.dao.ApplockDao;
/**
 * 看门狗服务 监视系统程序的运行状态
 */
public class WatchDogService extends Service {
	private ActivityManager am;
	private boolean flag;
	private ApplockDao dao;
	private InnerReceiver innerReceiver;
	private String tempStopProtectPackname;
	private ScreenOffReceiver offreceiver;
	private DataChangeReceiver dataChangeReceiver;
	private List<String> protectPacknames;
	private Intent intent;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	private class ScreenOffReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			tempStopProtectPackname = null;
		}
	}
	private class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("接收了临时停止保护的广播事件");
			tempStopProtectPackname = intent.getStringExtra("packname");
		}
	}
	private class DataChangeReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("数据库的内容变化了。。。。");
			protectPacknames = dao.findAll();
		}
	}
	@Override
	public void onCreate() {
		offreceiver = new ScreenOffReceiver();
		registerReceiver(offreceiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
		innerReceiver = new InnerReceiver();
		registerReceiver(innerReceiver,new IntentFilter("com.qzd.mobilesafe.tempstop"));
		dataChangeReceiver = new DataChangeReceiver();
		registerReceiver(dataChangeReceiver,new IntentFilter("com.qzd.mobilesafe.applockchange"));
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		dao = new ApplockDao(this);
		protectPacknames = dao.findAll();
		flag = true;
		intent = new Intent(getApplicationContext(),EnterPwdActivity.class);
		//服务是没有任务栈信息的.在服务开启activity,要指定这个activity运行的任务栈
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		new Thread(){
			public void run() {
				while(flag){
					List<RunningTaskInfo> infos = am.getRunningTasks(1);
					String packname = infos.get(0).topActivity.getPackageName();
					if(!protectPacknames.contains(packname)){
						//当前应用需要保护,蹦出来,弹出来一个输入密码的界面
						//设置要保护程序的包名
						intent.putExtra("packname",packname);
						startActivity(intent);
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		flag = false;
		unregisterReceiver(innerReceiver);
		innerReceiver = null;
		unregisterReceiver(offreceiver);
		offreceiver = null;
		unregisterReceiver(dataChangeReceiver); 
		dataChangeReceiver = null;
		super.onDestroy();
	}
}








