package com.qzd.mobilesafe.db.dao;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import com.qzd.mobilesafe.db.BlackNumberDBOpenHelper;
import com.qzd.mobilesafe.domain.BlackNumberInfo;
/**
 * ���������ݿ���ɾ�Ĳ�ҵ����
 */
public class BlackNumberDao {
	private BlackNumberDBOpenHelper helper;
	public BlackNumberDao(Context context){
		helper = new BlackNumberDBOpenHelper(context);
	}
	//��ѯ�����������Ƿ����
	public boolean find(String number){
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber where number = ?",new String[]{number});
		if(cursor.moveToNext()){
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	//��ѯ���������������ģʽ
	public String findMode(String number){
		String result = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select mode from blacknumber where number = ?",new String[]{number});
		if(cursor.moveToNext()){
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * ��Ӻ���������
	 * @param number ����������
	 * @param mode ����ģʽ  1.�绰���� 2.�������� 3.ȫ������
	 */
	public void add(String number,String mode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		db.insert("blacknumber",null, values);
		db.close();
	}
	//�޸ĺ�����
	public void update(String number,String newMode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newMode);
		db.update("blacknumber", values,"number = ?", new String[]{number});
		db.close();
	}
	//ɾ��������
	public void delete(String number){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacknumber","number = ?",new String[]{number});
		db.close();
	}
	//��ѯ���к���������
	public List<BlackNumberInfo> findAll(){
		SystemClock.sleep(5000);
		List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc",null);
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setNumber(number);
			info.setMode(mode);
			result.add(info);
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * ��ѯ���ֺ���������
	 * @param offset ���ĸ�λ�û�ȡ����
	 * @param maxnumber һ������ȡ��������¼
	 * @return ���ز�ѯ�����ĺ�����Ϣ
	 */
	public List<BlackNumberInfo> findPart(int offset,int maxnumber){
		SystemClock.sleep(500);
		List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase(); 
		//limit 10 offset 10ֻ��д��ĩβ
		Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc limit ? offset ?",
				new String[]{String.valueOf(maxnumber),String.valueOf(offset)});
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setNumber(number);
			info.setMode(mode);
			result.add(info);
		}
		cursor.close();
		db.close();
		return result;
	}
}










