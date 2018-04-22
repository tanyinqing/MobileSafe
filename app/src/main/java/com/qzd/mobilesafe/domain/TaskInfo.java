package com.qzd.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * ������Ϣ��ҵ��ʵ��
 */
public class TaskInfo {
	//��ʶ��ǰitem�Ƿ�ѡ��
	private boolean checked;//�Ƿ�ѡ��
	private Drawable icon;//ͼ��
	private String name;//����
	private long memsize;//�ڴ�ռ��
	private boolean userTask;//�û����� true���û�����
	private String packname;//����
	
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
