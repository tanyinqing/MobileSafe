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
		Object[] objs = (Object[]) intent.getExtras().get("pdus"); //��ȡ���ŵ���Ϣ
		for (Object obj : objs) {
			//��ȡ�������ŵĶ���
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			//��ȡ���ŵķ�����
			String sender = smsMessage.getOriginatingAddress();
			//��ȡ��������
			String body = smsMessage.getMessageBody();
			dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			if("#*location*#".equals(body)){ //GPS��λ
				Intent i = new Intent(context,GPSService.class);
				context.startService(i);
				SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
				String loastlocation = sp.getString("lastlocation",null);
				if(TextUtils.isEmpty(loastlocation)){
					SmsManager.getDefault().sendTextMessage(sender,null,"getting location...��",null, null);
				}else{
					SmsManager.getDefault().sendTextMessage(sender,null,loastlocation,null, null);
				}
				abortBroadcast();//���ض���
			}else if("#*alarm*#".equals(body)){ //��������
//				MediaPlayer player = MediaPlayer.create(context,R.raw.xxx);
//				player.setLooping(false);
//				player.setVolume(1.0f,1.0f);
//				player.start();
				abortBroadcast();//���ض���
			}else if("#*lockscreen*#".equals(body)){ //Զ������
				dpm.resetPassword("123",0);
				dpm.lockNow();
				abortBroadcast();//���ض���
			}else if("#*wipedate*#".equals(body)){ //�������
				dpm.wipeData(0);
				abortBroadcast();//���ض���
			}
		}
	}
}







