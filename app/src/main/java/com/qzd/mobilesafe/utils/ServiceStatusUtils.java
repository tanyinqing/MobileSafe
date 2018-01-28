package com.qzd.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {
	//检测一个服务是否是运行状态
	public static boolean isServiceRunning(Context context,String serviceName){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(100);
		for (RunningServiceInfo info : infos) {
			String className = info.service.getClassName();
			if(serviceName.equals(className)){
				return true;
			}
		}
		return false;
	}
}
