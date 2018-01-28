package com.qzd.mobilesafe.service;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.qzd.mobilesafe.R;
import com.qzd.mobilesafe.db.dao.AddressDao;
/**
 * 电话服务
 */
public class AddressService extends Service {
	private TelephonyManager tm;
	private OutCallReceiver receiver;
	private	MyListenerPhone listenerPhone;
	private WindowManager wm;
	private View view; //归属地显示view对象
	private SharedPreferences sp;
	WindowManager.LayoutParams params;
	private long[] mHits = new long[2];
	//在服务代码的内部 创建一个内部类
	//利用代码的方式 注册一个广播接受者
	//让广播接受者
	private class OutCallReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String phone = getResultData();
			String result =  AddressDao.getAddress(phone);
			showMyToast(result);
		}
	}
	@Override
	public void onCreate() {
		sp = getSharedPreferences("config",MODE_PRIVATE);
		//采用代码的方式注册广播接受者
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		
		listenerPhone = new MyListenerPhone();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//监听来电
		tm.listen(listenerPhone,PhoneStateListener.LISTEN_CALL_STATE);
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		
		super.onCreate();
	}
	/**
	 *显示一个自定义的土司
	 * @param result
	 */
	public void showMyToast(String address) {
		int which = sp.getInt("which",0);
		//{"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
		int[] bgs = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		//直接利用窗体管理器 添加一个View对象到整个手机系统的窗体上
		view = View.inflate(this,R.layout.toast_address,null);
		view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: //手指按下屏幕
						//开始位置
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE: //手指在屏幕上移动
						//获取新的位置
						int newX = (int) event.getRawX();
						int newY = (int) event.getRawY();
						//计算偏移量
						int dx = newX - startX;
						int dy = newY - startY;
						//更新view在窗体的位置
						params.x += dx;
						params.y += dy;
						//考虑边界问题
						if(params.x < 0){
							params.x = 0;
						}
						if(params.y < 0){
							params.y = 0;
						}
						if(params.x > wm.getDefaultDisplay().getWidth() - view.getWidth()){
							params.x = (wm.getDefaultDisplay().getWidth()-view.getWidth());
						}
						if(params.y > wm.getDefaultDisplay().getHeight() - view.getHeight()){
							params.y = (wm.getDefaultDisplay().getHeight() - view.getHeight());
						}
						wm.updateViewLayout(view, params);
						//重新初始化手指的位置
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_UP://手指离开屏幕的一瞬间
						Editor editor = sp.edit();
						editor.putInt("lastx",params.x);
						editor.putInt("lasty",params.y);
						editor.commit();
						break;
				}
				return true;
			}
		});
		view.setBackgroundResource(bgs[which]);
		TextView tv = (TextView) view.findViewById(R.id.tv_location);
		tv.setText(address);
		params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP + Gravity.LEFT;
		params.x = sp.getInt("lastx",0);
		params.y = sp.getInt("lasty",0);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                 | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(view, params);
	}
	private class MyListenerPhone extends PhoneStateListener{
		//电话改变的事件
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			//state 状态 incomingNumber来电号码
			super.onCallStateChanged(state, incomingNumber);
				switch (state) {
					case TelephonyManager.CALL_STATE_RINGING: //来电铃声响起
						//查询数据库的操作
						String address = AddressDao.getAddress(incomingNumber);
						showMyToast(address);
						break;
					case TelephonyManager.CALL_STATE_IDLE://空闲状态
						if(view != null){
							wm.removeView(view);
							view = null;
						}
						break;
			}
		}
	}
	@Override
	public void onDestroy() {
		//服务停止的时候 取消注册广播接受者
		unregisterReceiver(receiver);
		receiver = null;
		tm.listen(listenerPhone,PhoneStateListener.LISTEN_NONE);
		listenerPhone = null;
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}




