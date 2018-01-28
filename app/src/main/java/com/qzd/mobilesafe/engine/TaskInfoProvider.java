package com.qzd.mobilesafe.engine;
import java.util.ArrayList;
import java.util.List;

import com.qzd.mobilesafe.R;
import com.qzd.mobilesafe.domain.TaskInfo;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
public class TaskInfoProvider {
	/**
	 * 获取正在运行的进程信息
	 */
	public static List<TaskInfo> getTaskInfos(Context context){
		// 这个是包管理器
		PackageManager pm = context.getPackageManager();
		//用户进程管理器
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		//所有正在使用的用户的进程的集合 
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		//遍历这个集合  然后从这个进程中获取数据
		for (RunningAppProcessInfo processInfo : processInfos) {
			TaskInfo taskInfo = new TaskInfo();
			//得到进程的包名
			String packname = processInfo.processName;
			taskInfo.setPackname(packname);
			//进程占用的内存  可以同时获得多个进程的占用内存的信息
			// import android.os.Debug.MemoryInfo; 导包注意
			MemoryInfo[] momoryInfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
			//本地 虚拟机 等 多个内存 单位 kb *1024转化long类型
			long memsize = momoryInfos[0].getTotalPrivateDirty()*1024l;//得到某个进程总的内存大小
			taskInfo.setMemsize(memsize);
			try {//应用的清单文件
				ApplicationInfo applicationInfo = pm.getApplicationInfo(packname,0);
				Drawable icon = applicationInfo.loadIcon(pm);//图标
				taskInfo.setIcon(icon);
				String name = applicationInfo.loadLabel(pm).toString();//标签
				taskInfo.setName(name);
				// 与运算
				if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){//用户进程
					taskInfo.setUserTask(true);
				}else{//系统进程
					taskInfo.setUserTask(false);
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				taskInfo.setName(packname);
			}
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
}




