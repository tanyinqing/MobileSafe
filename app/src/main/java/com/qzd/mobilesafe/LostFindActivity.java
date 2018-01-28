package com.qzd.mobilesafe;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_lostfind_number;
	private ImageView iv_lostfind_status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		//判断是否做过设置向导,如果没有做过,就跳转到设置向导页面去设置,否则就留在当前的页面
		boolean configed = sp.getBoolean("configed",false);
		if(configed){
			setContentView(R.layout.actvtity_lost_find);
			iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
			tv_lostfind_number = (TextView) findViewById(R.id.tv_lostfind_number);
			boolean protecting = sp.getBoolean("protecting",false);
			if(protecting){
				iv_lostfind_status.setImageResource(R.drawable.lock);
			}else{
				iv_lostfind_status.setImageResource(R.drawable.unlock);
			}
			tv_lostfind_number.setText(sp.getString("safenumber",""));
		}else{
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			finish();
		}
	}
	/**
	 * 重新进入手机防盗设置向导页面
	 */
	public void reEntrySetup(View view){
		Intent intent = new Intent(LostFindActivity.this,Setup1Activity.class);
		startActivity(intent);
		finish();
	}
}




