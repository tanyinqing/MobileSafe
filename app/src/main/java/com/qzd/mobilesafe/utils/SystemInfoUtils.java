package com.qzd.mobilesafe.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
/**
 * ϵͳ��Ϣ������
 *
 */
public class SystemInfoUtils {
	/**
	 * ��ȡ�������еĽ��̵ĸ���
	 */
	public static int getRunningProcessCount(Context context){
//		���̹����� ����������� �������Ļ��
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}
	/**
	 * ��ȡ�����ڴ�
	 * @return ��λ���ֽ�
	 */
	public static long getAvailRam(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);//�޷���ֵ ���������þ��Ǵ淵��ֵ
		return outInfo.availMem;//byteΪ��λ��long���͵Ŀ����ڴ��С
	}
	/**
	 * ��ȡ���ڴ�
	 * @return
	 *  �����api  totalmemֻ����16���ϰ汾��ʹ�á�
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
 		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
 		am.getMemoryInfo(outInfo);
  		return outInfo.totalMem;
	 */
	public static long getTotalRam(Context context) {
		try{
//			����һ���ļ�����
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//			��ȡ�ļ��ĵ�һ�е����� MemTotal:  513000 KB
			String line = br.readLine();
			StringBuffer sb = new StringBuffer();
//			�����ַ��� �������ҳ���
			for (char c : line.toCharArray()) {
				if(c >= '0' && c <= '9'){
					sb.append(c);
				}
			}
//			ת����λ��byte
			return Integer.parseInt(sb.toString())*1024l;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
}











