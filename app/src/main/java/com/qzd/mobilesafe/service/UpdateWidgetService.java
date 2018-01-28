package com.qzd.mobilesafe.service;
import java.util.Timer;
import java.util.TimerTask;
import com.qzd.mobilesafe.R;
import com.qzd.mobilesafe.receiver.MyWidget;
import com.qzd.mobilesafe.utils.SystemInfoUtils;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;
/**
 * ��������
 *
 */
public class UpdateWidgetService extends Service {
	private ScreenOffReceiver offreceiver;
	private ScreenOnReceiver onreceiver;
	private Timer timer;
	private TimerTask task;
	//wedget�Ĺ�����
	private AppWidgetManager awm;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	//����
	private class ScreenOffReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			stopTimer();
		}
	}
	//����
	private class ScreenOnReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			startTimer();
		}
	}
	@Override
	public void onCreate() {
		onreceiver = new ScreenOnReceiver();
		offreceiver = new ScreenOffReceiver();
		registerReceiver(onreceiver,new IntentFilter(Intent.ACTION_SCREEN_ON));
		registerReceiver(offreceiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
		awm = AppWidgetManager.getInstance(this);
		startTimer();
		super.onCreate();
	}
	//����
	private void startTimer(){
		if(timer == null && task == null){
			timer = new Timer();
			task = new TimerTask() {
				@Override
				public void run() {
					//���ø��µ����
					ComponentName provider = new ComponentName(UpdateWidgetService.this,MyWidget.class);
					RemoteViews views = new RemoteViews(getPackageName(),R.layout.process_widget);
					views.setTextViewText(R.id.process_count,"�������еĽ���:"+SystemInfoUtils.getRunningProcessCount(getApplicationContext())+"��");
					views.setTextViewText(R.id.process_memory,"�����ڴ�:"+(SystemInfoUtils.getAvailRam(getApplicationContext())/1024/1024)+"MB");
					// ����һ������,����������������һ��Ӧ�ó���ִ�е�.
					// �Զ���һ���㲥�¼�,ɱ����̨���ȵ��¼�
					Intent intent = new Intent();
					intent.setAction("com.qzd.mobilesafe.killall");
					PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
					views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
					awm.updateAppWidget(provider, views);
				}
			};
			timer.schedule(task,0,3000);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(offreceiver);
		unregisterReceiver(onreceiver);
		offreceiver = null;
		onreceiver = null;
		stopTimer();
	}
	
	private void stopTimer() {
		if(timer != null && task != null){
			timer.cancel();
			task.cancel();
			timer = null;
			task = null;
		}
	}
}



















