package com.qzd.mobilesafe;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {
	private EditText et_setup3_phoe;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvtity_setup3);
		et_setup3_phoe = (EditText) findViewById(R.id.et_setup3_phoe);
		et_setup3_phoe.setText(sp.getString("safenumber",""));
	}
	//下一步
	@Override
	public void showNext() {
		String safenumber = et_setup3_phoe.getText().toString().trim();
		if(TextUtils.isEmpty(safenumber)){
			Toast.makeText(this,"请先设置安全号码",0).show();
			return;
		}
		Editor editor = sp.edit();
		editor.putString("safenumber",safenumber);
		editor.commit();
		Intent intent = new Intent(this,Setup4Activity.class);;
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String phone = data.getStringExtra("phone").replace("-","").trim().replace(" ","").replace("(","").replace(")","");
		et_setup3_phoe.setText(phone);
		super.onActivityResult(requestCode, resultCode, data);
	}
	//上一步
	@Override
	public void showPre() {
		Intent intent = new Intent(this,Setup2Activity.class);;
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in,R.anim.tran_pre_out);
	}
	public void selectContact(View view){
		Intent intent = new Intent(this,SelectContactActivity.class);
		startActivityForResult(intent,0);
	}
}
