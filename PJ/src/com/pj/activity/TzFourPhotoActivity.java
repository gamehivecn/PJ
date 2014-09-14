package com.pj.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.pj.activity.TakePhotoActivity.AutoFocusCallback;
import com.pj.handler.RegulationHandler;
import com.pj.handler.RegulationHandler.RegulationType;
import com.pj.regulation.TZRegulation;
import com.pj.source.TzStatusDatas;
import com.pj.tools.BitMapTools;
import com.pj.utils.MulitPointTouchListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class TzFourPhotoActivity extends Activity implements SurfaceHolder.Callback{

	/**
	 * ��Ļ����������
	 */
	public int input_num;
	// ��Ļ���������ְ�ť
	private ImageButton input_num_bt_0;
	private Button input_num_bt_1;
	private Button input_num_bt_2;
	private Button input_num_bt_3;
	private Button input_num_bt_4;
	private Button input_num_bt_5;
	private Button input_num_bt_6;
	private Button input_num_bt_7;
	private Button input_num_bt_8;
	private Button input_num_bt_9;

	// �������
	public EditText out_num_et;
	public EditText[] out_et = new EditText[4];
	// ��������Ӧ�±�
	public int arr_out = 0;
	/*
	 * �ж��Ƿ��һ�����Ƿ�Ϊ��1" �� p="1"��������� ��֮��ֱ�����
	 */
	public int p = 0;
	// ɾ����ť
	private Button clearButton;
	// ��ׯ���еİ�ť
	private Button judge_btn;
	// �������������Ƶ�ת����ť
	private Button fourTofive;
	// ����ť
	private Button regulationButton;
	// �հ׼���ת��
	private Button space_change;
	// �����ж�
	private int regulation = 1;
	private int regulation_judge = 0;
	// ��ׯ�������ж�
	private int judge = 1;
	// ������Ϣ�����
	private EditText editText_1;
	// ������Ϣ�����ť
	private Button enter_text_btn;
	// ������Ϣ
	private String regulation_text = "00���ͬ��ִ�С��0�㲻�ִ�С";

	private int edit_judge = 0;
	private static final String IMAGE_FILE = "/sdcard/image.jpg";
	private Camera mCamera;
	private Button begin_photo_record_btn;
	private ImageView imageView;
	private SurfaceView mSurfaceView;
	private SurfaceHolder holder;
	private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback();
	private Button show_image_stoprecord_btn;
	private Button beginCamBtn;
	private String strCaptureFilePath = Environment
			.getExternalStorageDirectory().toString();
	private AudioManager mAudioManager;
	private int panduan = 0;
	private Button change_photo_record_btn;
	private long mExitTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_tz_four);
		initWiget();
		eventCliskListeners();
		
		/* SurfaceHolder���� */
		mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
		holder = mSurfaceView.getHolder();
		holder.addCallback(TzFourPhotoActivity.this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);	
		mSurfaceView.setVisibility(View.GONE);
		/* ��������Button��OnClick�¼����� */
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		begin_photo_record_btn = (Button) findViewById(R.id.begin_photo_record_btn);
		begin_photo_record_btn.setEnabled(false);
		begin_photo_record_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {	
				/* �涯�Խ������� */
				takePicture();
				mCamera.autoFocus(mAutoFocusCallback);
			}
		});
		imageView = (ImageView) findViewById(R.id.image);	
		imageView.setOnTouchListener(new MulitPointTouchListener());		
		
		show_image_stoprecord_btn = (Button) findViewById(R.id.show_image_stoprecord_btn);
		
		show_image_stoprecord_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				File outFile = new File(IMAGE_FILE);
				if (outFile.exists()) {
					Bitmap bm = BitMapTools.getBitMap(IMAGE_FILE);
					imageView.setVisibility(View.VISIBLE);
					mSurfaceView.setVisibility(View.GONE);
					if (panduan <= 1) {
						Matrix matrix = new Matrix();
						matrix.set(imageView.getImageMatrix()); 
		                matrix.postScale((float)0.15, (float)0.15);
		                imageView.setImageMatrix(matrix); 
					}
					imageView.setImageBitmap(bm);				
					stopCamera();
					panduan = panduan+1;	
				}
				}
		});
		beginCamBtn = (Button)findViewById(R.id.stop_record_goback_btn);
		beginCamBtn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSurfaceView.setVisibility(View.VISIBLE);
				initCamera();
				panduan = panduan+1;	
			}
		});
		
		change_photo_record_btn = (Button)findViewById(R.id.change_photo_record_btn);
		change_photo_record_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(TzFourPhotoActivity.this, TzFourRecordActivity.class);
				startActivity(i);
				finish();
				
			}
		});
		
	}

	// ��ʼ���ؼ�
	private void initWiget() {
		// ���밴ťʵ����
		input_num_bt_0 = (ImageButton) findViewById(R.id.input_num_btn_0);
		input_num_bt_1 = (Button) findViewById(R.id.input_num_btn_1);
		input_num_bt_2 = (Button) findViewById(R.id.input_num_btn_2);
		input_num_bt_3 = (Button) findViewById(R.id.input_num_btn_3);
		input_num_bt_4 = (Button) findViewById(R.id.input_num_btn_4);
		input_num_bt_5 = (Button) findViewById(R.id.input_num_btn_5);
		input_num_bt_6 = (Button) findViewById(R.id.input_num_btn_6);
		input_num_bt_7 = (Button) findViewById(R.id.input_num_btn_7);
		input_num_bt_8 = (Button) findViewById(R.id.input_num_btn_8);
		input_num_bt_9 = (Button) findViewById(R.id.input_num_btn_9);
		// �������ʵ����
		out_num_et = (EditText) findViewById(R.id.output_et_0);
		int i = 0;
		out_et[i++] = (EditText) findViewById(R.id.output_et_1);
		out_et[i++] = (EditText) findViewById(R.id.output_et_2);
		out_et[i++] = (EditText) findViewById(R.id.output_et_3);
		out_et[i++] = (EditText) findViewById(R.id.output_et_4);
		// ɾ����ť
		clearButton = (Button) findViewById(R.id.delete_btn);
		// ����ť
		regulationButton = (Button) findViewById(R.id.regulationButton);
		// ��ׯ�а�ť
		judge_btn = (Button) findViewById(R.id.judge_btn);
		// �հװ���
		space_change = (Button) findViewById(R.id.space_change);
		// ������Ϣ�����
		editText_1 = (EditText) findViewById(R.id.ed_text_1);
		// ������Ϣ�����ť
		enter_text_btn = (Button) findViewById(R.id.enter_text_btn);
		// �������������Ƶ�ת����ť
		fourTofive = (Button) findViewById(R.id.pj_or_tongzi);
	}

	private void eventCliskListeners() {
		// ���밴ť�����¼�
		ButtonInputListener biLisenter = new ButtonInputListener();
		input_num_bt_0.setOnClickListener(biLisenter);
		input_num_bt_1.setOnClickListener(biLisenter);
		input_num_bt_2.setOnClickListener(biLisenter);
		input_num_bt_3.setOnClickListener(biLisenter);
		input_num_bt_4.setOnClickListener(biLisenter);
		input_num_bt_5.setOnClickListener(biLisenter);
		input_num_bt_6.setOnClickListener(biLisenter);
		input_num_bt_7.setOnClickListener(biLisenter);
		input_num_bt_8.setOnClickListener(biLisenter);
		input_num_bt_9.setOnClickListener(biLisenter);

		clearButton.setOnClickListener(new ButtonDeleteListener());
		clearButton.setOnLongClickListener(new BtnDelteAllListener());

		regulationButton
				.setOnClickListener(new RegulationButtonActionListener());

		judge_btn.setOnClickListener(new JudgeButtonListener());

		enter_text_btn.setOnClickListener(new RegulationTextListenser());

		space_change.setOnClickListener(new ChangeTzToPjListenser());

		fourTofive.setOnClickListener(new ChangeFourOrFiveListener());
	}

	// ----------------------------------------------------------------------------

	/**
	 * �Զ����к���
	 * 
	 * */
	private void switchLine() {
		if (arr_out == 4) {
			int index = out_num_et.getSelectionStart();
			Editable editable = out_num_et.getText();
			editable.insert(index, "\n");
		}
	}

	/**
	 * 
	 * ������������
	 * 
	 * */

	private void outPut() {
		int index = out_num_et.getSelectionStart();
		Editable editable = out_num_et.getText();
		editable.insert(index, input_num + "-");

	}

	/**
	 * �������������ֵİ�ť ��Ӧ��Ļ��ť����ö�Ӧ������
	 */
	public class ButtonInputListener implements OnClickListener {
		public void onClick(View v) {
			if (arr_out < 8 && arr_out >= 0) {
				switch (v.getId()) {
				case R.id.input_num_btn_0:
					input_num = 0;
					outPut();
					arr_out++;
					switchLine();
					break;
				case R.id.input_num_btn_1:
					input_num = 1;
					outPut();
					arr_out++;
					switchLine();
					break;
				case R.id.input_num_btn_2:
					input_num = 2;
					outPut();
					arr_out++;
					switchLine();
					break;
				case R.id.input_num_btn_3:
					input_num = 3;
					outPut();
					arr_out++;
					switchLine();
					break;
				case R.id.input_num_btn_4:
					input_num = 4;
					outPut();
					arr_out++;
					switchLine();
					break;
				case R.id.input_num_btn_5:
					input_num = 5;
					outPut();
					arr_out++;					
					switchLine();
					break;
				case R.id.input_num_btn_6:
					input_num = 6;
					outPut();
					arr_out++;					
					switchLine();
					break;
				case R.id.input_num_btn_7:
					input_num = 7;
					outPut();
					arr_out++;					
					switchLine();
					break;
				case R.id.input_num_btn_8:
					input_num = 8;
					outPut();
					arr_out++;				
					switchLine();
					break;
				case R.id.input_num_btn_9:
					input_num = 9;
					outPut();
					arr_out++;					
					switchLine();
					break;
				default:
					break;
				}

				if (arr_out == 8) {
					defaultEvent();
				}

			}
		}
	}

	/**
	 * �̰�ɾ���¼�
	 * */
	public class ButtonDeleteListener implements OnClickListener {
		public void onClick(View v) {
			if (v.getId() == R.id.delete_btn && arr_out > 0) {
				int index = out_num_et.getSelectionStart();
				Editable editable = out_num_et.getText();
				if (arr_out == 5) {
					editable.delete(index - 3, index);
					int index1 = out_num_et.getSelectionStart();
					Editable editable1 = out_num_et.getText();
					editable1.insert(index1, "\n");
				} else if (arr_out != 4) {
					editable.delete(index - 2, index);
				} else {
					out_num_et.setText("");
					arr_out = 1;
					p = 0;
				}
				arr_out--;
			}
			if (arr_out <= 0) {
				for (int i = 0; i < out_et.length; i++) {
					out_et[i].setText("");
				}
			}
			p = 0;
		}

	}

	/**
	 * 
	 * ����ɾ���¼�
	 * */
	public class BtnDelteAllListener implements OnLongClickListener {

		public boolean onLongClick(View v) {
			if (v.getId() == R.id.delete_btn && arr_out > 0) {
				out_num_et.setText("");
				for (int i = 0; i < out_et.length; i++) {
					out_et[i].setText("");
				}
				arr_out = 0;
			}
			return false;
		}

	}

	/**
	 * ����С���������ѡ��ť�¼�������
	 * 
	 * @author Administrator
	 * 
	 */
	private class RegulationButtonActionListener implements OnClickListener {

		public void onClick(View v) {
			if (regulation == 0) {
				regulation_judge = 0;
				if (arr_out >= 8) {
					TzStatusDatas.getInstance().setRegulationIndex(0);
					disPlayOrder();
				}
				regulationButton
						.setBackgroundResource(R.drawable.selector_pj_a);
				regulation_text = "00���ͬ��ִ�С��0�㲻�ִ�С";
				regulation = 1;
			} else if (regulation == 1) {
				regulation_judge = 1;
				if (arr_out >= 8) {
					TzStatusDatas.getInstance().setRegulationIndex(1);
					disPlayOrder();
				}
				regulationButton
						.setBackgroundResource(R.drawable.selector_pj_b);
				regulation_text = "28����9�㣬ͬ��ִ�С��0�㲻�ִ�С";
				regulation = 2;
			} else if (regulation == 2) {
				regulation_judge = 2;
				if (arr_out >= 8) {
					TzStatusDatas.getInstance().setRegulationIndex(2);
					disPlayOrder();
				}
				regulationButton
						.setBackgroundResource(R.drawable.selector_pj_c);
				regulation_text = "28���ͬ��ִ�С��0�㲻�ִ�С";
				regulation = 3;
			} else if (regulation == 3) {
				regulation_judge = 3;
				if (arr_out >= 8) {
					TzStatusDatas.getInstance().setRegulationIndex(3);
					disPlayOrder();
				}
				regulationButton
						.setBackgroundResource(R.drawable.selector_pj_d);
				regulation_text = "19>28>00,ͬ��ִ�С��0�㲻�ִ�С";
				regulation = 0;
			}
		}
	}

	/**
	 * PJת��ΪTZ�¼�
	 * 
	 * @return
	 */
	private class ChangeTzToPjListenser implements OnClickListener {

		public void onClick(View v) {
			if (v.getId() == R.id.space_change) {
				Intent i = new Intent();
				i.setClass(TzFourPhotoActivity.this, PJPhotoActivity.class);
				startActivity(i);
				finish();
			}

		}

	}

	/**
	 * ������������֮���ת��
	 *           
	 */
	private class ChangeFourOrFiveListener implements OnClickListener {
		public void onClick(View v) {
			if (v.getId() == R.id.pj_or_tongzi) {
				Intent i = new Intent();
				i.setClass(TzFourPhotoActivity.this, TzFivePhotoActivity.class);
				startActivity(i);
				finish();
			}
		}

	}

	/**
	 * �ж�8�����Ƿ��Ѿ�����������
	 * 
	 * @return
	 */
	private boolean isComplete() {
		if (out_num_et.getText() == null || out_num_et.getText().equals(""))
			return false;
		return true;
	}


	/**
	 * ��ʾ��Ϲ���˳��ķ���
	 * 
	 * @author Administrator
	 * 
	 */
	public void disPlayOrder() {
		if (!isComplete() && arr_out < 8)
			return;
		String[] target = { "��", "ƽ", "һ", "��" };
		for (int i = 0; i < 4; i++) {
			int[] datas = getInputNums();
			TZRegulation r = TzStatusDatas.getInstance()
					.getCurrentTZRegulation();
			if (i == 0) {
				datas = RegulationHandler.getInstance().getTzOrder(datas,
						RegulationType.REGULATION_RIGHT, r);
			} else if (i == 1) {
				datas = RegulationHandler.getInstance().getTzOrder(datas,
						RegulationType.REGULATION_FLAT, r);
			} else if (i == 2) {
				datas = RegulationHandler.getInstance().getTzOrder(datas,
						RegulationType.REGULATION_ONE, r);
			} else if (i == 3) {
				datas = RegulationHandler.getInstance().getTzOrder(datas,
						RegulationType.REGULATION_CLIMB, r);
			}
			if (judge == 0) {// ����˳����ʾ˳���ת
				int t = 0;
				t = datas[0];
				datas[0] = datas[3];
				datas[3] = t;
				t = datas[1];
				datas[1] = datas[2];
				datas[2] = t;
				judge_btn.setBackgroundResource(R.drawable.selector_pj_x);
				out_et[i].setText(target[i] + "\n" + datas[0] + datas[1]
						+ datas[2] + datas[3] + judgeX(comparator(datas), i));
			} else if (judge == 1) {
				judge_btn.setBackgroundResource(R.drawable.selector_pj_z);
				out_et[i].setText(target[i] + "\n" + datas[0] + datas[1]
						+ datas[2] + datas[3] + judgeZ(comparator(datas), i));

			}
		}

	}

	/**
	 * ��ׯ���ж��¼�
	 * 
	 * @author LZB
	 * 
	 */
	public class JudgeButtonListener implements OnClickListener {
		public void onClick(View v) {
			if (!isComplete() && arr_out <= 0)
				return;
			if (judge == 0) {
				judge_btn.setBackgroundResource(R.drawable.selector_pj_z);
				judge = 1;
				if (arr_out >= 8) {
					disPlayOrder();
				}

			} else if (judge == 1) {
				judge = 0;
				judge_btn.setBackgroundResource(R.drawable.selector_pj_x);
				if (arr_out >= 8) {
					disPlayOrder();
				}
			}

		}
	}

	/**
	 * ��ׯ���жϺ���
	 * 
	 * @param a
	 *            [] ѭ���ȽϺ�������
	 * @param comparator_num
	 *            ��Ӧ������
	 * @author LZB
	 * 
	 */
	private String judgeZ(int[] a, int comparator_num) {
		int[] t = new int[3];
		int j = 0, q = 0;
		String z = null;
		for (int i = 0, x = 0; i < a.length; i++) {
			if (a[i] == 3) {
				t[x++] = i + 1;
				j++;
			} else if (a[i] == 4) {
				q++;
			}
		}
		if (q == 3) {
			z = "TX";
		} else if (j > 0 && j < 4) {
			if (t[2] != 0) {
				z = "X" + t[0] + t[1] + t[2];
			} else if (t[1] != 0) {
				z = "X" + t[0] + t[1];
			} else {
				z = "X" + t[0];
			}
		} else if (j == 0) {
			z = "X0";
		}

		return z;
	}

	/**
	 * ���е��жϺ���
	 * 
	 * @param a
	 *            [] ѭ���ȽϺ�������
	 * @param comparator_num
	 *            ��Ӧ������
	 * @author LZB
	 * 
	 */
	private String judgeX(int[] a, int comparator_num) {
		int t = 0, j = 0;
		String x = null;
		for (int i = 0; i < a.length; i++)
			if (a[i] == 1) {
				t = i;
				j++;
			}
		if (j == 1) {
			t = t + 1;
			x = "Y" + t;

		} else if (j == 0) {
			x = "Y0";
		}
		return x;
	}

	/**
	 * �жϱȽϺ�������������ת�������һ��ת������һ������һ�����αȽϺ�������
	 * 
	 * @param :a[] �Ƚ�˳�������
	 * @return:�Ƚ��������һ���������������С������һ�������Ӧ�����Ǹ��±������µ������Լ�1.
	 * @author LZB
	 * 
	 */
	private int[] comparator(int[] a) {
		int t;
		int[] b = new int[] { 0, 0, 0, 0 };
		for (int i = 0; i < a.length; i++) {
			t = a[0];
			a[0] = a[1];
			a[1] = a[2];
			a[2] = a[3];
			a[3] = t;
			for (int j = 1; j < a.length; j++) {
				if (a[0] >= a[j])
					b[j]++;
			}
		}
		return b;
	}


	/**
	 * ��ʼʱĬ��ʹ��ZZ�͹���A��ť�¼�
	 * 
	 */
	private void defaultEvent() {
		if (arr_out >= 8) {
			TzStatusDatas.getInstance().setRegulationIndex(regulation_judge);
			disPlayOrder();
		}
	}

	/**
	 * ������Ϣ����¼���
	 * 
	 */
	private class RegulationTextListenser implements OnClickListener {

		public void onClick(View v) {
			if (0 == edit_judge) {
				editText_1.setText(regulation_text);
				edit_judge = 1;
			} else if (1 == edit_judge) {
				editText_1.setText("���£���������æ��������ϵ��");
				edit_judge = 0;
			}
		}

	}

	/**
	 * ��������Ƶĵ�����Ӧ��PorkNum��
	 * 
	 * @return
	 */
	private int[] getInputNums() {
		int[] result = new int[8];
		String[] o = { "\n0", "\n1", "\n2", "\n3", "\n4", "\n5", "\n6", "\n7",
				"\n8", "\n9" };
		String[] p = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String[] inputNum = out_num_et.getText().toString().split("-");
		if (arr_out > 4) {
			for (int j = 0; j < o.length; j++) {
				if (inputNum[4].equals(o[j])) {
					inputNum[4] = p[j];
				}
			}
		}
		for (int i = 0; i < inputNum.length; i++) {
			result[i] = Integer.parseInt(inputNum[i]);
		}
		return result;
	}
	public void surfaceCreated(SurfaceHolder surfaceholder) {
		begin_photo_record_btn.setEnabled(true);
		try {
			/* ������� */
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w,
			int h) {
		/* �����ʼ�� */
		initCamera();
	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		stopCamera();
		mCamera.release();	
		mCamera = null;
	}

	/* ���յ�method */
	private void takePicture() {
		if (mCamera != null) {
			mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
		}
	}

	private ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			/* ���¿���˲����������ĳ��� */
		}
	};

	private PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] _data, Camera _camera) {
			/* Ҫ����raw data?д?�� */
		}
	};

	private PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] _data, Camera _camera) {
			deleteFile();
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) // �ж�SD���Ƿ���ڣ����ҿ��Կ��Զ�д
			{

			} else {
				Toast.makeText(TzFourPhotoActivity.this, "SD�������ڻ�д����",
						Toast.LENGTH_LONG).show();
			}
			try {
				/* ȡ����Ƭ */
				Bitmap bm = BitmapFactory.decodeByteArray(_data, 0,
						_data.length);
				Bitmap bMapRotate;
				Matrix matrix = new Matrix();
				matrix.reset();
				matrix.postRotate(90);
				bMapRotate = Bitmap.createBitmap(bm, 0, 0,
						bm.getWidth(), bm.getHeight(), matrix, true);
				bm = bMapRotate;
				/* �����ļ� */
				File myCaptureFile = new File(strCaptureFilePath, "image.jpg");
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(myCaptureFile));
				/* ����ѹ��ת������ */
				bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				/* ����flush()����������BufferStream */
				bos.flush();
				/* ����OutputStream */
				bos.close();
				/* ����Ƭ��ʾ3������������ */
				// Thread.sleep(2000);
				/* �����趨Camera */
				stopCamera();
				initCamera();
				if(!bm.isRecycled()){
					bm.recycle();
					bm = null;
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/* �涨��class AutoFocusCallback */
	public final class AutoFocusCallback implements
			android.hardware.Camera.AutoFocusCallback {
		public void onAutoFocus(boolean focused, Camera camera) {
			/* �Ե��������� */
			if (focused) {
				takePicture();
			}
		}
	};

	/* �����ʼ����method */
	private void initCamera() {
		if (mCamera != null) {
			try {
				Camera.Parameters parameters = mCamera.getParameters();
				/*
				 * �趨��Ƭ��СΪ1024*768�� ��ʽΪJPG
				 * 2592, 1944
				 */
				parameters.setPictureFormat(PixelFormat.JPEG);
				parameters.setPictureSize(2592, 1944);
				mCamera.setParameters(parameters);
				mCamera.setDisplayOrientation(90);
				/* ��Ԥ������ */
				mCamera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* ֹͣ�����method */
	private void stopCamera() {
		if (mCamera != null) {
			try {
				/* ֹͣԤ�� */
				mCamera.stopPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/** �˳���ť */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					 Object mHelperUtils;
					 Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
					 mExitTime = System.currentTimeMillis();
					 imageView.setVisibility(View.GONE);	
					 mSurfaceView.setVisibility(View.GONE);
					begin_photo_record_btn.setEnabled(false);
				}else {
                    finish();
            }									
			return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	private void deleteFile() {
		File outFile = new File(IMAGE_FILE);
		if (outFile.exists()) {
			outFile.delete();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);	
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		deleteFile();
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

	}
	
}
