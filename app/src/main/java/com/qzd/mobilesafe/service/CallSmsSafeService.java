package com.qzd.mobilesafe.service;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.qzd.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {
	private InnerSmsReceiver receiver;
	private BlackNumberDao dao;
	//监听当前呼叫的状态
	private TelephonyManager tm;
	private MyPhoneListener listener;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	private class InnerSmsReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("内部广播接收者短信到来啦。。。");
			//检查发件人是否是黑名单号码,设置短信拦截全部拦截
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String sender = smsMessage.getOriginatingAddress();//得到短信发件人
				String result = dao.findMode(sender);
				if("2".equals(result) || "3".equals(result)){
					abortBroadcast();//拦截短信
				}
				String body = smsMessage.getDisplayMessageBody();
				if(body.contains("fapiao")){
					abortBroadcast();
				}
			}
		}
	}
	@Override
	public void onCreate() {
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);//监听电话的状态

		dao = new BlackNumberDao(this);
		receiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver,filter);
		super.onCreate();
	}
	private class MyPhoneListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
				case TelephonyManager.CALL_STATE_RINGING: //电话响铃的状态
					String mode = dao.findMode(incomingNumber);
					if("1".equals(mode) || "3".equals(mode)){
						System.out.println("这是黑名单的电话 "+incomingNumber);
						//删除呼叫记录
						//另外一个应用程序联系人的应用的私有数据库
//						deleteCallLog(incomingNumber);
						//观察呼叫记录数据库内容的变化
						Uri uri = Uri.parse("content://call_log/calls");
						getContentResolver().registerContentObserver(uri,true,new CallLogObserver(incomingNumber,new Handler()));
						endcall();//挂断电话 另外一个进程里面运行的 远程服务的方法。方法调用后,呼叫记录可能还没有生产。
					}
					break;
			}
		}
	}
	/**
	 * 监听通话记录
	 */
	private class CallLogObserver extends ContentObserver{
		private String incomingNumber;
		public CallLogObserver(String incomingNumber,Handler handler) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}
		@Override
		public void onChange(boolean selfChange) {
			System.out.println("数据库产生了呼叫记录");
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(incomingNumber);
			super.onChange(selfChange);
		}
	}
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		tm.listen(listener,PhoneStateListener.LISTEN_NONE);
		listener = null;
		super.onDestroy();
	}
	/**
	 * 利用内容提供者删除呼叫记录
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		//呼叫记录uri的路径
		Uri uri = Uri.parse("content://call_log/calls");
		System.out.println("删除 "+incomingNumber);
//		CallLog.CONTENT_URI;
		resolver.delete(uri,"number=?",new String[]{incomingNumber});
	}
	/**
	 * 挂断电话
	 */
	public void endcall() {
		try {
			Class<?> clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService",String.class);
			IBinder b = (IBinder) method.invoke(null,TELEPHONY_SERVICE);
			//获取到了原生未经修改包装的系统电话的管理服务
			ITelephony iTelephony = ITelephony.Stub.asInterface(b);
			iTelephony.endCall();//挂断电话
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}















