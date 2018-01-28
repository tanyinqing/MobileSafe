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
	 * 扫描病毒
	 */
	private void scanVirus(){
		packageManager=getPackageManager();
		List<PackageInfo> infos=packageManager.getInstalledPackages(0);//拿到所有的安装的包信息
		for(PackageInfo info:infos){
			//String datadir=info.applicationInfo.dataDir;//它的数据在哪个目录
			String sourcedir=info.applicationInfo.sourceDir;//它的源代码在哪个目录 也就是apk的路径
			String md5=getFileMd5(sourcedir);//得到它的MD5信息
			System.out.println(info.applicationInfo.loadLabel(packageManager)+":"+md5);
			// 查询md5信息，是否在病毒数据库白名单中
			//可以从金山应用中拷贝它的病毒数据库信息 antvirus.db
		}
	}
	/**
	 * 得到文件的MD5值
	 */
	private String getFileMd5(String path) {
		//获取一个文件的特征信息 签名信息
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
			//与运算
			int number=b&0xff;//加盐
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
