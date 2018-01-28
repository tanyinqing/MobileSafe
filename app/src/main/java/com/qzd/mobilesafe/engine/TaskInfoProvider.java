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
	 * ��ȡ�������еĽ�����Ϣ
	 */
	public static List<TaskInfo> getTaskInfos(Context context){
		// ����ǰ�������
		PackageManager pm = context.getPackageManager();
		//�û����̹�����
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		//��������ʹ�õ��û��Ľ��̵ļ��� 
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		//�����������  Ȼ�����������л�ȡ����
		for (RunningAppProcessInfo processInfo : processInfos) {
			TaskInfo taskInfo = new TaskInfo();
			//�õ����̵İ���
			String packname = processInfo.processName;
			taskInfo.setPackname(packname);
			//����ռ�õ��ڴ�  ����ͬʱ��ö�����̵�ռ���ڴ����Ϣ
			// import android.os.Debug.MemoryInfo; ����ע��
			MemoryInfo[] momoryInfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
			//���� ����� �� ����ڴ� ��λ kb *1024ת��long����
			long memsize = momoryInfos[0].getTotalPrivateDirty()*1024l;//�õ�ĳ�������ܵ��ڴ��С
			taskInfo.setMemsize(memsize);
			try {//Ӧ�õ��嵥�ļ�
				ApplicationInfo applicationInfo = pm.getApplicationInfo(packname,0);
				Drawable icon = applicationInfo.loadIcon(pm);//ͼ��
				taskInfo.setIcon(icon);
				String name = applicationInfo.loadLabel(pm).toString();//��ǩ
				taskInfo.setName(name);
				// ������
				if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){//�û�����
					taskInfo.setUserTask(true);
				}else{//ϵͳ����
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




