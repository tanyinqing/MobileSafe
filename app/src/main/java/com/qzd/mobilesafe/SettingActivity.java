package com.qzd.mobilesafe;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.qzd.mobilesafe.service.AddressService;
import com.qzd.mobilesafe.service.CallSmsSafeService;
import com.qzd.mobilesafe.ui.SettingClickView;
import com.qzd.mobilesafe.ui.SettingItemView;
import com.qzd.mobilesafe.utils.ServiceStatusUtils;
public class SettingActivity extends Activity {
	//自动更新设置
	private SettingItemView siv_update;
	//归属地显示设置
	private SettingItemView siv_showaddress;
	private Intent showAddressIntent;
	//黑名单拦截设置
	private SettingItemView siv_callsms_safe;
	private Intent callSmsSafeIntent;
	
	private SharedPreferences sp;
	//更改归属地的背景
	private SettingClickView scv_changebg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvtity_setting);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		//自动更新的设置
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		//黑名单设置
		siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
		callSmsSafeIntent = new Intent(this,CallSmsSafeService.class);
		boolean update = sp.getBoolean("update",false);
		if(update){//自动升级已经开启
			siv_update.setChecked(true);
		}else{//自动升级已经关闭
			siv_update.setChecked(false);
		}
		siv_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				//判断是否有选中
				//已经打开自动升级
				if(siv_update.isChecked()){
					siv_update.setChecked(false);
					editor.putBoolean("update",false);
				
				}else{//没有打开自动升级
					siv_update.setChecked(true);
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
		//归属地显示设置
		siv_showaddress = (SettingItemView) findViewById(R.id.siv_showaddress);
		showAddressIntent = new Intent(this,AddressService.class);
		//判断服务是否开启
		if(ServiceStatusUtils.isServiceRunning(this,"com.qzd.mobilesafe.service.AddressService")){
			siv_showaddress.setChecked(true);
		}else{
			siv_showaddress.setChecked(false);
		}
		//判断短信到来服务是否开启
		boolean isCallsmsSafeRunning = ServiceStatusUtils.isServiceRunning(this,"com.qzd.mobilesafe.service.CallSmsSafeService");
		siv_callsms_safe.setChecked(isCallsmsSafeRunning);
		
		siv_showaddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(siv_showaddress.isChecked()){//关闭归属地显示
					siv_showaddress.setChecked(false);
					stopService(showAddressIntent);
					//存储状态到sp里面
				}else{//开启归属地显示
					siv_showaddress.setChecked(true);
					startService(showAddressIntent);
				}
			}
		});
		//黑名单拦截设置
		siv_callsms_safe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(siv_callsms_safe.isChecked()){//关闭归属地显示
					siv_callsms_safe.setChecked(false);
					stopService(callSmsSafeIntent);
					//存储状态到sp里面
				}else{//开启归属地显示
					siv_callsms_safe.setChecked(true);
					startService(callSmsSafeIntent);
				}
			}
		});
		//更改背景
		scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
		scv_changebg.setTitle("归属地提示框风格");
		int which = sp.getInt("which",0);
		final String[] items = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
		scv_changebg.setDesc(items[which]);
		scv_changebg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("归属地提示框风格");
				int tt = sp.getInt("which",0);
				builder.setSingleChoiceItems(items,tt,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						scv_changebg.setDesc(items[which]);
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("取消",null);
				builder.show();
			}
		});
	}
}








