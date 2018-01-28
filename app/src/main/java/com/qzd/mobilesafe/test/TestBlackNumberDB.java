package com.qzd.mobilesafe.test;

import java.util.List;
import java.util.Random;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.qzd.mobilesafe.db.BlackNumberDBOpenHelper;
import com.qzd.mobilesafe.db.dao.BlackNumberDao;
import com.qzd.mobilesafe.domain.BlackNumberInfo;

public class TestBlackNumberDB extends AndroidTestCase {
	
	public void testCreateDB() throws Exception {
		BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
	}
	public void testAdd() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		long number = 13500000000l;
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			dao.add(String.valueOf(number+i),String.valueOf(r.nextInt(3)+1));
		}
	}
	public void testDelete() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.delete("110");
	}
	public void testUpdate() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.update("110","2");
	}
	public void testFind() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.find("110");
		System.out.println(result);
	}
	public void testFildAll() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		List<BlackNumberInfo> infos = dao.findAll();
		for (BlackNumberInfo info : infos) {
			info.toString();
		}
	}
}
