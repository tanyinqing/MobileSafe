package com.qzd.mobilesafe.test;

import java.util.List;

import com.qzd.mobilesafe.domain.TaskInfo;
import com.qzd.mobilesafe.engine.TaskInfoProvider;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestTaskInfoProvider extends AndroidTestCase{
  public void testGetTaskInfos()throws Exception{
	  List<TaskInfo> infos=TaskInfoProvider.getTaskInfos(getContext());
	  for (TaskInfo taskInfo : infos) {
		System.out.println(taskInfo.toString());
		  Log.d("tanyinqing",taskInfo.toString());
	}
  }
}
