package com.qzd.mobilesafe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
/**
 * 自己敲的，没完成相关的功能
 * @author Administrator
 *
 */
public class CleanCacheActivity extends Activity{

	private PackageManager pm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		deleteApplicationCacheFiles 主要是调用这个隐藏的方法
//		两个参数  一个是方法名 一个是数据类型 
		try {
			Method method=PackageManager.class.getMethod("deleteApplicationCacheFiles",String.class,IPackageDataObserver.class);
			try {
				//执行这个方法 接口的实现类做为参数 执行回调的方法
				method.invoke(pm, "对应应用的包名",new MypackDataObserver());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class MypackDataObserver extends IPackageDataObserver.Stub{

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			// TODO Auto-generated method stub
			//这个包名的缓存是否被清理成功了
			System.out.println(packageName+succeeded);
		}
		
	}
	
	/**
	 * 清理全部缓存 freeSstorageAndNotify 利用系统里的这个方法
	 * LRU 最近使用过的排序列表
	 */
	public void clearAll(){
		Method[] method=PackageManager.class.getMethods();
		for (Method method2 : method) {
			//获得所有方法的方法名
			//System.out.println(method2.getName());
			if ("freeSstorageAndNotify".equals(method2.getName())) {
	//第一个参数 执行者 第二个 空间大小 第三个是观察者
				try {
					method2.invoke(pm, Integer.MAX_VALUE,new MypackDataObserver());
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			} 
		}
	}
	/**
	 * 扫描手机程序的所有应用的缓存信息
	 * 加入权限 获取包大小的权限
	 */
	private void scanCache(){
		pm=getPackageManager();
		new Thread(){
			public void run(){
				Method getSizeInfoMethod;
				Method[] method=PackageManager.class.getMethods();
				for (Method method2 : method) {
					//获得所有方法的方法名
					//System.out.println(method2.getName());
					if ("getPackkageSizeInfo".equals(method2.getName())) {
						getSizeInfoMethod=method2;						
					} 
				}
				//获得全部的包名
			List<PackageInfo> packInfos=pm.getInstalledPackages(0);
			for (PackageInfo packageInfo : packInfos) {
				//用到的方法android.jar中没有 但Android系统中存在
				//就要从字节码文件中获取	
				//invoke 利用反射去执行方法 args是aidl文件 aidl文件包名必须一致
				//远程服务的接口的实例
				//在这里更新进度条 总的是100，扫描后加1
			/*	try {
					getSizeInfoMethod.invoke(packageInfo.packageName, new MyDataObsserver());
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			
			};
		}.start();
	}
	
	//这个文件需要导入aidl文件才能使用 是一个回调的方法
	/*private class MyDataObsserver extends IPackageStatsObserver.stub{
		@Override
		public void onGetStatscompleted(PackageStats pStats,boolean succeeded)
		throws RemoteException{
			long cache=pStats.cacheSize;//内存大小 long 类型
			long code=pStats.codeSize; //应用的大小
			long data=pStats.dataSize;//数据的大小
			System.out.println(Formatter.formatFileSize(getApplicationContext(), cache));
				//定义一个方法在主线程中去更新UI
				runOnUiThread(new Runnable(){
					@Override
					public void run(){
//						更新文本的内容
//						如果内存大于0，容器中加入一个子控件
					}
				});
			
		}
	}*/
}
