package com.qzd.mobilesafe;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.qzd.mobilesafe.ui.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {
	private SettingItemView siv_setup2_bindsim;
	private TelephonyManager tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		setContentView(R.layout.actvtity_setup2);
		siv_setup2_bindsim = (SettingItemView) findViewById(R.id.siv_setup2_bindsim);
		String sim = sp.getString("sim",null);
		if(TextUtils.isEmpty(sim)){
			siv_setup2_bindsim.setChecked(false);
		}else{
			siv_setup2_bindsim.setChecked(true);
		}
		siv_setup2_bindsim.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(siv_setup2_bindsim.isChecked()){//如果没有开启则清空
					Editor editor = sp.edit();
					editor.putString("sim",null);
					editor.commit();
					siv_setup2_bindsim.setChecked(false);
				}else{//绑定sim卡就把sim卡的串号给存起来
					String sim = tm.getLine1Number();//获取sim卡的串号
					Editor editor = sp.edit();
					editor.putString("sim",sim);
					editor.commit();
					siv_setup2_bindsim.setChecked(true);
				}
			}
		});
	}
	//下一步
	@Override
	public void showNext() {
		//判断是否绑定了sim卡
		String sim = sp.getString("sim",null);
		if(TextUtils.isEmpty(sim)){
			Toast.makeText(this,"请先绑定sim卡",0).show();
			return;
		}
		Intent intent = new Intent(this,Setup3Activity.class);;
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
	}
	//上一步
	@Override
	public void showPre() {
		Intent intent = new Intent(this,Setup1Activity.class);;
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in,R.anim.tran_pre_out);
	}
}
