package com.qzd.mobilesafe;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	//����һ������ʶ����
	private GestureDetector detector;
	public SharedPreferences sp;
	public abstract void showNext();
	public abstract void showPre();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		detector = new GestureDetector(this,new SimpleOnGestureListener(){
			/**
			 * �����ǵ���ָ�����滬����ʱ��ص�
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//������X��������������
				if(Math.abs(velocityX)<200){
					Toast.makeText(getApplicationContext(), "������̫����", Toast.LENGTH_SHORT).show();
					return true;
				}
				//����б���������
				if(Math.abs((e2.getRawY() - e1.getRawY())) > 100){
					Toast.makeText(getApplicationContext(), "����������",  Toast.LENGTH_SHORT).show();
					return true;
				}
				if((e2.getRawX() - e1.getRawX())> 200 ){
					//��ʾ��һ��ҳ�棺�������һ���
					System.out.println("��ʾ��һ��ҳ�棺�������һ���");
					showPre();
					return true;
					
				}
				if((e1.getRawX()-e2.getRawX()) > 200 ){
					//��ʾ��һ��ҳ�棺�������󻬶�
					System.out.println("��ʾ��һ��ҳ�棺�������󻬶�");
					showNext();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	/**
	 * ��һ���ĵ���¼�
	 */
	public void next(View view){
		showNext();
	}
	/**
	 * ��һ���ĵ���¼�
	 */
	public void pre(View view){
		showPre();
	}
	//ʹ������ʶ����
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
}
