package ceshi;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ceshi extends Activity{

	private PackageManager packageManager;
	/**
	 * ɨ�財��
	 */
	private void scanVirus(){
		packageManager=getPackageManager();
		List<PackageInfo> infos=packageManager.getInstalledPackages(0);//�õ����еİ�װ�İ���Ϣ
		for(PackageInfo info:infos){
			//String datadir=info.applicationInfo.dataDir;//�����������ĸ�Ŀ¼
			String sourcedir=info.applicationInfo.sourceDir;//����Դ�������ĸ�Ŀ¼ Ҳ����apk��·��
			String md5=getFileMd5(sourcedir);//�õ�����MD5��Ϣ
			System.out.println(info.applicationInfo.loadLabel(packageManager)+":"+md5);
			// ��ѯmd5��Ϣ���Ƿ��ڲ������ݿ��������
			//���Դӽ�ɽӦ���п������Ĳ������ݿ���Ϣ antvirus.db
		}
	}
	/**
	 * �õ��ļ���MD5ֵ
	 */
	private String getFileMd5(String path) {
		//��ȡһ���ļ���������Ϣ ǩ����Ϣ
		File file=new File(path);
		//md5
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("sha1");
		
		FileInputStream fis=new FileInputStream(file);
		byte[]buffer=new byte[1024];
		int len=-1;
		while((len=fis.read(buffer))!=-1){
			digest.update(buffer, 0, len);
		}
		byte[] result=digest.digest();
		StringBuffer sb=new StringBuffer();
		for(byte b:result){
			//������
			int number=b&0xff;//����
			String str=Integer.toHexString(number);
//			System.out.println(str);
			if(str.length()==1){
				sb.append("0");
			}
			sb.append(str);
		}
		return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}	
	}
	
	
}
