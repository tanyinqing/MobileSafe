package com.qzd.mobilesafe.ui;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qzd.mobilesafe.R;
/**
 * 我们自定义的组合控件,它里面有2个TextView,还有一个CheckBox和View
 * @author qiuzhidi
 *
 */
public class SettingClickView extends RelativeLayout {
	private TextView tv_desc;
	private TextView tv_title;
	/**
	 * 初始化布局文件
	 * @param context
	 */
	private void iniView(Context context) {
		//把一个布局文件---转换成一个View,并且加载在SettingItemView
		View.inflate(context,R.layout.setting_click_view,this);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}
	/**
	 * 带有2个参数的构造方法,布局文件的时候调用
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
	 * 设置组合控件的描述信息
	 */
	public void setDesc(String text){
		tv_desc.setText(text);
	}
	/**
	 * 设置组合控件的标题
	 */
	public void setTitle(String text){
		tv_desc.setText(text);
	}
	
}
