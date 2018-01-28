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
 * �Լ��õģ�û�����صĹ���
 * @author Administrator
 *
 */
public class CleanCacheActivity extends Activity{

	private PackageManager pm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		deleteApplicationCacheFiles ��Ҫ�ǵ���������صķ���
//		��������  һ���Ƿ����� һ������������ 
		try {
			Method method=PackageManager.class.getMethod("deleteApplicationCacheFiles",String.class,IPackageDataObserver.class);
			try {
				//ִ��������� �ӿڵ�ʵ������Ϊ���� ִ�лص��ķ���
				method.invoke(pm, "��ӦӦ�õİ���",new MypackDataObserver());
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
			//��������Ļ����Ƿ�����ɹ���
			System.out.println(packageName+succeeded);
		}
		
	}
	
	/**
	 * ����ȫ������ freeSstorageAndNotify ����ϵͳ����������
	 * LRU ���ʹ�ù��������б�
	 */
	public void clearAll(){
		Method[] method=PackageManager.class.getMethods();
		for (Method method2 : method) {
			//������з����ķ�����
			//System.out.println(method2.getName());
			if ("freeSstorageAndNotify".equals(method2.getName())) {
	//��һ������ ִ���� �ڶ��� �ռ��С �������ǹ۲���
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
	 * ɨ���ֻ����������Ӧ�õĻ�����Ϣ
	 * ����Ȩ�� ��ȡ����С��Ȩ��
	 */
	private void scanCache(){
		pm=getPackageManager();
		new Thread(){
			public void run(){
				Method getSizeInfoMethod;
				Method[] method=PackageManager.class.getMethods();
				for (Method method2 : method) {
					//������з����ķ�����
					//System.out.println(method2.getName());
					if ("getPackkageSizeInfo".equals(method2.getName())) {
						getSizeInfoMethod=method2;						
					} 
				}
				//���ȫ���İ���
			List<PackageInfo> packInfos=pm.getInstalledPackages(0);
			for (PackageInfo packageInfo : packInfos) {
				//�õ��ķ���android.jar��û�� ��Androidϵͳ�д���
				//��Ҫ���ֽ����ļ��л�ȡ	
				//invoke ���÷���ȥִ�з��� args��aidl�ļ� aidl�ļ���������һ��
				//Զ�̷���Ľӿڵ�ʵ��
				//��������½����� �ܵ���100��ɨ����1
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
	
	//����ļ���Ҫ����aidl�ļ�����ʹ�� ��һ���ص��ķ���
	/*private class MyDataObsserver extends IPackageStatsObserver.stub{
		@Override
		public void onGetStatscompleted(PackageStats pStats,boolean succeeded)
		throws RemoteException{
			long cache=pStats.cacheSize;//�ڴ��С long ����
			long code=pStats.codeSize; //Ӧ�õĴ�С
			long data=pStats.dataSize;//���ݵĴ�С
			System.out.println(Formatter.formatFileSize(getApplicationContext(), cache));
				//����һ�����������߳���ȥ����UI
				runOnUiThread(new Runnable(){
					@Override
					public void run(){
//						�����ı�������
//						����ڴ����0�������м���һ���ӿؼ�
					}
				});
			
		}
	}*/
}
