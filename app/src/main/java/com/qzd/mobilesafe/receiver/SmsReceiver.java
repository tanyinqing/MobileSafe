package com.qzd.mobilesafe.receiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import com.qzd.mobilesafe.R;
import com.qzd.mobilesafe.service.GPSService;

public class SmsReceiver extends BroadcastReceiver {
	private static final String TAG = "SmsReceiver";
	private DevicePolicyManager dpm;
	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objs = (Object[]) intent.getExtras().get("pdus"); //获取短信的信息
		for (Object obj : objs) {
			//获取这条短信的对象
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			//获取短信的发件人
			String sender = smsMessage.getOriginatingAddress();
			//获取短信内容
			String body = smsMessage.getMessageBody();
			dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			if("#*location*#".equals(body)){ //GPS定位
				Intent i = new Intent(context,GPSService.class);
				context.startService(i);
				SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
				String loastlocation = sp.getString("lastlocation",null);
				if(TextUtils.isEmpty(loastlocation)){
					SmsManager.getDefault().sendTextMessage(sender,null,"getting location...！",null, null);
				}else{
					SmsManager.getDefault().sendTextMessage(sender,null,loastlocation,null, null);
				}
				abortBroadcast();//拦截短信
			}else if("#*alarm*#".equals(body)){ //报警音乐
//				MediaPlayer player = MediaPlayer.create(context,R.raw.xxx);
//				player.setLooping(false);
//				player.setVolume(1.0f,1.0f);
//				player.start();
				abortBroadcast();//拦截短信
			}else if("#*lockscreen*#".equals(body)){ //远程锁屏
				dpm.resetPassword("123",0);
				dpm.lockNow();
				abortBroadcast();//拦截短信
			}else if("#*wipedate*#".equals(body)){ //清除数据
				dpm.wipeData(0);
				abortBroadcast();//拦截短信
			}
		}
	}
}







