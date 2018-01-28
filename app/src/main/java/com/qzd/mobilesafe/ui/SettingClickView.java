package com.qzd.mobilesafe.ui;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qzd.mobilesafe.R;
/**
 * �����Զ������Ͽؼ�,��������2��TextView,����һ��CheckBox��View
 * @author qiuzhidi
 *
 */
public class SettingClickView extends RelativeLayout {
	private TextView tv_desc;
	private TextView tv_title;
	/**
	 * ��ʼ�������ļ�
	 * @param context
	 */
	private void iniView(Context context) {
		//��һ�������ļ�---ת����һ��View,���Ҽ�����SettingItemView
		View.inflate(context,R.layout.setting_click_view,this);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}
	/**
	 * ����2�������Ĺ��췽��,�����ļ���ʱ�����
	 * @param context
	 * @param attrs
	 */
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.qzd.mobilesafe","title");
	}
	public SettingClickView(Context context) {
		super(context);
		iniView(context);
	}
	/**
	 * ������Ͽؼ���������Ϣ
	 */
	public void setDesc(String text){
		tv_desc.setText(text);
	}
	/**
	 * ������Ͽؼ��ı���
	 */
	public void setTitle(String text){
		tv_desc.setText(text);
	}
	
}
