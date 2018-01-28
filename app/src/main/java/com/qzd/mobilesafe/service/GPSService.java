package com.qzd.mobilesafe.service;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {
	private LocationManager lm;
	private MyListener listener;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyListener();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//��ȡ��ǰλ���ṩ�ߵ�����
		String provider = lm.getBestProvider(criteria,true);
		//����λ�õĸ��� (��ʽ,���ʱ��,����/��,��ͨ�ļ���)
		lm.requestLocationUpdates(provider,0,0,listener);
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		super.onDestroy();
		lm.removeUpdates(listener);
		listener = null;
	}
	private class MyListener implements LocationListener{
		//��λ�ñ仯��ʱ����õķ���
		@Override
		public void onLocationChanged(Location location) {
			String latitude = "J : "+location.getLatitude()+"\n";
			String longitude = "W : "+location.getLongitude()+"\n";
			String accuracy = "A : "+location.getAccuracy()+"\n";
			//��λ�÷�����ȫ����
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("lastlocation", latitude+longitude+accuracy);
			editor.commit();
		}
		//��ĳ��λ���ṩ�ߵ�״̬�����˱仯 ����-�ر�   �ر�-����
		@Override
		public void onProviderDisabled(String provider) {
		}
		@Override
		public void onProviderEnabled(String provider) {
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
