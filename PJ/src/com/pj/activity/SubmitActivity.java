package com.pj.activity;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.telephony.TelephonyManager;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TableLayout;


public class SubmitActivity extends Activity {

	/**
	 * @params first_code�ǻ�ȡ��IMEI�ĳ�ʼֵ
	 */
	private static int first_code;
	/**
	 * @params login_code��ע����
	 */
	private static int login_code;
	/**
	 * ע����������
	 */
	private EditText edit_imei;
	/**
	 * ����һ���ֻ������������������ֻ�IMEI
	 */
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit);
		//
		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		first_code = Integer.parseInt(tm.getDeviceId().substring(9, 15));
		
		// �����ж��Ƿ��ǵ�һ������
		if (fileIsExists()) {// �ļ��������ȡ
			// ������ļ���ȡ
			SharedPreferences sharedata1 = getSharedPreferences("data", 0);
			String data = sharedata1.getString("code", null);
			//���ע����
			login_code = getLoginCode(first_code);		
			if (data.equals(login_code + "")) {// �ж�ע�����Ƿ���ȷ
				runNext();
			} else {
				iniDialog();
			}
		} else {
			runNext();
			//iniDialog();
		}
	}

	/**
	 * ��һ����ó�ʼ��Ĵ���
	 */
	private void iniDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("�뽫��֤�룺" + "��" + first_code + "��" + "�ύ��ע������������ϵõ�ע����")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						getIMEIDialog();
					}
				})
				.setNegativeButton("�˳�", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						exit();
					}
				}).create().show();
	}

	/**
	 * ��������ע����Ĵ���
	 */
	private void getIMEIDialog() {
		TableLayout loginForm = (TableLayout) getLayoutInflater().inflate(
				R.layout.edit_imei, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("������ע���룺").setView(loginForm);
		edit_imei = (EditText) loginForm.findViewById(R.id.code);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (edit_imei.getText()
						.toString().equals("")) 
					reGetIMEIDialog();
				else {
					Integer edit_code = Integer.parseInt(edit_imei.getText()
							.toString());
					login_code = getLoginCode(first_code);					
					if (edit_code == login_code) {
						saveCode();
						runNext();
					} else {
						reGetIMEIDialog();
					}
					
				}
				
				
			}
		}).setNegativeButton("�˳�", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				exit();
			}
		}).create().show();
	}

	/**
	 * �������������ʾ����
	 */
	private void reGetIMEIDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("ע��������").setMessage("����������ע����")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						getIMEIDialog();
					}
				})
				.setNegativeButton("�˳�", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						exit();
					}
				}).create().show();
	}

	/**
	 * ����ע���룬�������ɵ�һ���ļ���
	 */
	private void saveCode() {
		// �����ļ�
		SharedPreferences.Editor sharedata = getSharedPreferences("data", 0)
				.edit();
		sharedata.putString("code", login_code + "");
		sharedata.commit();

	}

	/**
	 * �ж��Ƿ��ǵ�һ��ʹ��
	 */
	public boolean fileIsExists() {
		File file = new File("/data/data/com.pj.activity/shared_prefs/data.xml");// ��������ȫ·��
		if (!file.exists()) {
			return false;
		}
		return true;
	}
	/**
	 * ע����ļ��㷽��
	 */
	private int getLoginCode(int firstcode){
		int i = 1;
		int h = firstcode;
		int k;
		for (int j = 0; j < 6; j++) {
			i = i*10;
			k = h%i;
			h = (h-k)/i;
			firstcode = firstcode+(k+j)*i/10%i;
		}	
		return firstcode;
	}

	/**
	 * ��ת����һ������
	 */
	private void runNext() {

		new Handler().postDelayed(new Runnable() {

			public void run() {

				Intent i = new Intent();

				i.setClass(SubmitActivity.this, PJPhotoActivity.class);

				startActivity(i);

				finish();

			}

		}, 2000);

	}
	// �˳���ť
		public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getRepeatCount() == 0) {
					exit();
					return true;
				}
			}
			return super.dispatchKeyEvent(event);
		}

	/**
	 * �˳�����
	 */
	private void exit() {
		finish();
	}
}
