package com.qzd.mobilesafe.engine;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import com.qzd.mobilesafe.domain.AppInfo;
/**
 * ҵ�񷽷����ṩ�ֻ����氲װ�����е�Ӧ�ó�����Ϣ
 */
public class AppInfoProvider {
	/**
	 * ��ȡ���еİ�װ��Ӧ�ó�����Ϣ��
	 * @param context ������
	 * @return
	 */
	public static List<AppInfo> getAppInfos(Context context){
		PackageManager pm = context.getPackageManager();//��ȡ���Ĺ�����
		List<AppInfo> appInfos = new ArrayList<AppInfo>();//�������Ӧ�ó�����Ϣ����
		AppInfo appInfo = null;
		List<PackageInfo> PackInfos = pm.getInstalledPackages(0);//���еİ�װ��ϵͳ�ϵ�Ӧ�ó������Ϣ 0Ϊ����������ı��
		for (PackageInfo packageInfo : PackInfos) {
			String packName = packageInfo.packageName;//��ȡ����
			Drawable icon = packageInfo.applicationInfo.loadIcon(pm);//��ȡӦ�ó���ͼ��
			String name = packageInfo.applicationInfo.loadLabel(pm).toString();//��ȡӦ�ó�������
			int flag = packageInfo.applicationInfo.flags;
			appInfo = new AppInfo();
			if((flag&ApplicationInfo.FLAG_SYSTEM) == 0){//�û�����
				appInfo.setUserApp(true);
			}else{ //ϵͳ����
				appInfo.setUserApp(false);
			}
			if((flag&ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0){//�ֻ��ڴ�
				appInfo.setInRom(true);
			}else{
				appInfo.setInRom(false);
			}
			appInfo.setPackname(packName);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			appInfos.add(appInfo);
		}
		return appInfos;
	}
}






