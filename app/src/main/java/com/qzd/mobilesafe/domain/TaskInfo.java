package com.qzd.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 进程信息的业务实体
 */
public class TaskInfo {
	//标识当前item是否被选中
	private boolean checked;//是否选中
	private Drawable icon;//图标
	private String name;//命名
	private long memsize;//内存占用
	private boolean userTask;//用户进程 true是用户进程
	private String packname;//包名
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMemsize() {
		return memsize;
	}
	public void setMemsize(long memsize) {
		this.memsize = memsize;
	}
	public boolean isUserTask() {
		return userTask;
	}
	public void setUserTask(boolean userTask) {
		this.userTask = userTask;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	@Override
	public String toString() {
		return "TaskInfo [checked=" + checked + ", name=" + name + ", memsize="
				+ memsize + ", userTask=" + userTask + ", packname=" + packname
				+ "]";
	}
	
}
