package com.qzd.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;

public class Ceshi extends Activity{
	private LocationManager lm;//�õ���λ�÷���
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ʵ����
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		
	}
}
