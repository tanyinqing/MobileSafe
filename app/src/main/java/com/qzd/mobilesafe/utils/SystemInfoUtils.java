package com.qzd.mobilesafe.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
/**
 * 系统信息工具类
 *
 */
public class SystemInfoUtils {
	/**
	 * 获取正在运行的进程的个数
	 */
	public static int getRunningProcessCount(Context context){
//		进程管理器 即任务管理器 从上下文获得
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}
	/**
	 * 获取可用内存
	 * @return 单位是字节
	 */
	public static long getAvailRam(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);//无返回值 参数的作用就是存返回值
		return outInfo.availMem;//byte为单位的long类型的可用内存大小
	}
	/**
	 * 获取总内存
	 * @return
	 *  下面的api  totalmem只能在16以上版本下使用。
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
 		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
 		am.getMemoryInfo(outInfo);
  		return outInfo.totalMem;
	 */
	public static long getTotalRam(Context context) {
		try{
//			定义一个文件对象
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//			读取文件的第一行的内容 MemTotal:  513000 KB
			String line = br.readLine();
			StringBuffer sb = new StringBuffer();
//			遍历字符串 把数字找出来
			for (char c : line.toCharArray()) {
				if(c >= '0' && c <= '9'){
					sb.append(c);
				}
			}
//			转化单位是byte
			return Integer.parseInt(sb.toString())*1024l;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
}











