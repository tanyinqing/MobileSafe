package com.qzd.mobilesafe;
import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvtity_setup1);
	}
	/**
	 * ÏÂÒ»²½
	 */
	@Override
	public void showNext() {
		Intent intent = new Intent(Setup1Activity.this,Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
	}
	@Override
	public void showPre() {
	}
}
