package com.pj.activity;

import java.io.File;

import com.pj.handler.RegulationHandler;
import com.pj.handler.RegulationHandler.RegulationType;
import com.pj.regulation.TZRegulation;
import com.pj.source.TzStatusDatas;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class TzFourRecordActivity extends Activity implements SurfaceHolder.Callback {

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
	
	private static final String TAG = "TzFourRecordActivity";

	private MediaRecorder record = null;
	private static final String OUTPUT_FILE = "/sdcard/videooutput.mp4";
	private VideoView vv;
	private Button startBtn = null;
	private Thread thread;
	private SurfaceHolder holder;
	private SurfaceView surfaceView;
	Camera mCameraDevice;
	private AudioManager mAudioManager;
	private long mExitTime;
	private Button change_photo_record_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_tz_four);
		initWiget();
		eventCliskListeners();
		// =========================================================================
		startBtn = (Button) findViewById(R.id.begin_photo_record_btn);
		Button endBtn = (Button) findViewById(R.id.show_image_stoprecord_btn);
		Button playRecordingBtn = (Button) findViewById(R.id.play_record_btn);
		Button stpPlayingrecordingBtn = (Button) findViewById(R.id.stop_record_goback_btn);

		vv = (VideoView) findViewById(R.id.videoview);
		surfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
		//=======================================================================
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);		
		//========================================================================
		
		holder = surfaceView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// ==================================================================

		startBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					beginRecording();
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					e.printStackTrace();
				}
			}
		});

		endBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					stopRecording();
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					e.printStackTrace();
				}
			}
		});

		playRecordingBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					playRecording();
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					e.printStackTrace();
				}
			}
		});

		stpPlayingrecordingBtn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					stopPlayingRecording();
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					e.printStackTrace();
				}
			}
		});
		
		change_photo_record_btn = (Button)findViewById(R.id.change_photo_record_btn);
		change_photo_record_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(TzFourRecordActivity.this, TzFourPhotoActivity.class);
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
				i.setClass(TzFourRecordActivity.this, PJPhotoActivity.class);
				startActivity(i);
				finish();
			}

		}

	}

	/**
	 * ������������֮���ת��
	 *           
	 * @return
	 */
	private class ChangeFourOrFiveListener implements OnClickListener {
		public void onClick(View v) {
			if (v.getId() == R.id.pj_or_tongzi) {
				Intent i = new Intent();
				i.setClass(TzFourRecordActivity.this, TzFivePhotoActivity.class);
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
	public void surfaceCreated(SurfaceHolder sholder) {
		// TODO Auto-generated method stub
		startBtn.setEnabled(true);
	}

	public void surfaceChanged(SurfaceHolder sholder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Width x Height = " + width + "x" + height);
	}

	public void surfaceDestroyed(SurfaceHolder sholder) {
		// TODO Auto-generated method stub

	}

	protected void beginRecording() throws Exception {
		// TODO Auto-generated method stub
		if (record != null) {
			record.stop();
			record.release();
			record = null;
			Log.i(TAG, "�Ѿ�ɾ��" + 1);
		}
		// ========================================================
		deleteFile();
		changePreview();
		// ========================================================
		vv.setVisibility(View.GONE);
		surfaceView.setVisibility(View.VISIBLE);
		// ========================================================
		thread = new Thread() {
			@Override
			public void run() {
				try {
					record = new MediaRecorder();
					record.setCamera(mCameraDevice);
					record.setVideoSource(MediaRecorder.VideoSource.CAMERA);
					record.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					record.setVideoSize(320, 240);
					record.setVideoFrameRate(20);
					record.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
					record.setPreviewDisplay(holder.getSurface());
					record.setOrientationHint(90);
					record.setOutputFile(OUTPUT_FILE);
					try {
						record.prepare();
					} catch (Exception e) {
						Log.i(TAG, e.getMessage());
					}
					try {
						record.start();
					} catch (Exception e) {
						Log.e(TAG, e.getMessage() + e.getCause());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			};
		};
		thread.start();
		thread = null;
	}

	protected void stopRecording() {
		// TODO Auto-generated method stub
		vv.setVisibility(View.GONE);
		if (record != null) {
			try {
				record.stop();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.getMessage() + 1);
			}
			try {
				record.release();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.getMessage() + 2);
			}
			record = null;
		}
		try {
			mCameraDevice.release();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage() + 4);
		}
	}

	protected void playRecording() {
		// TODO Auto-generated method stub
		MediaController mc = new MediaController(this);
		surfaceView.setVisibility(View.GONE);
		vv = (VideoView) findViewById(R.id.videoview);
		vv.setVisibility(View.VISIBLE);
		vv.setMediaController(mc);
		vv.setVideoPath(OUTPUT_FILE);
		vv.start();
	}

	protected void stopPlayingRecording() throws Exception {
		// TODO Auto-generated method stub
		vv.stopPlayback();
	}

	private void deleteFile() {
		File outFile = new File(OUTPUT_FILE);
		if (outFile.exists()) {
			outFile.delete();
		}
	}

	private void changePreview() {
		if (mCameraDevice.CAMERA_ERROR_UNKNOWN == 1) {
			try {				
				mCameraDevice.release();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage() + 6);
			}
		}
		try {
			mCameraDevice = Camera.open();
			mCameraDevice.setDisplayOrientation(90);
			mCameraDevice.unlock();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage() + e.getCause());
		}
	}
	/** �˳���ť */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					 Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
					 mExitTime = System.currentTimeMillis();
					 vv.setVisibility(View.GONE);	
					 surfaceView.setVisibility(View.GONE);
				}else {
                    finish();
            }									
			return true;
			}
		}
		return super.dispatchKeyEvent(event);
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
		try {
			mCameraDevice.lock();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage() + 3);
		}
		try {
			mCameraDevice.release();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage() + 4);
		}
	}
}
