package com.qzd.mobilesafe;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.qzd.mobilesafe.utils.SmsUtils;
import com.qzd.mobilesafe.utils.SmsUtils.BackUpCallBack;

public class AtoolsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvtity_atools);
	}
	//点击事件 :号码归属地查询
	public void numberAddressQuery(View view){
		Intent intent = new Intent(this,NumberAddressQueryActvity.class);
		startActivity(intent);
	}
	//点击事件 :短信的备份
	public void smsBackup(View view){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("正在备份。。。。");
			pd.show();
			new Thread(){
				public void run() {
					try {
						SmsUtils.backupSms(getApplicationContext(),new BackUpCallBack() {
							@Override
							public void onSmsBackup(int progress) {
								pd.setProgress(progress);
							}
							@Override
							public void beforeSmsBackup(int total) {
								pd.setMax(total);
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
					pd.dismiss();
				};
			}.start();
		}else{
			Toast.makeText(this,"SD卡不可用！",0).show();
		}
	}
	//点击事件 :短信的还原
	public void smsRestore(View view){
		try {
			SmsUtils.restoreSms(this,true);
			Toast.makeText(this,"还原成功！",0).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}




