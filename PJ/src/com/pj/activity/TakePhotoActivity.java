package com.pj.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.pj.tools.BitMapTools;
import com.pj.utils.MulitPointTouchListener;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
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

public class TakePhotoActivity extends Activity implements SurfaceHolder.Callback {
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
	private Button stop_sufaceview_btn;
	private long mExitTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_pj);
		/* SurfaceHolder���� */
		mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
		holder = mSurfaceView.getHolder();
		holder.addCallback(TakePhotoActivity.this);
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
				i.setClass(TakePhotoActivity.this,RecordActivity.class);
				startActivity(i);
				finish();
				
			}
		});
		

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
				Toast.makeText(TakePhotoActivity.this, "SD�������ڻ�д����",
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		deleteFile();
		super.onDestroy();
	}
}