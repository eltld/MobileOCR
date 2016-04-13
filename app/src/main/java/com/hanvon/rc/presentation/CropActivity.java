package com.hanvon.rc.presentation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import com.hanvon.HWCloudManager;
//import com.hanvon.common.HWLangDict;

import com.hanvon.rc.R;
import com.hanvon.rc.md.camera.UploadImage;
import com.hanvon.rc.md.camera.activity.RecResultActivity;
import com.hanvon.rc.utils.BitmapUtil;
import com.hanvon.rc.utils.ConnectionDetector;
import com.hanvon.rc.utils.DisplayUtil;
import com.hanvon.rc.utils.FileUtil;
import com.hanvon.rc.utils.HttpUtilsFiles;
import com.hanvon.rc.utils.InfoMsg;
import com.hanvon.rc.utils.LogUtil;
import com.hanvon.rc.wboard.bean.PhotoAlbum;
import com.hanwang.preprocessjava.preprocessdll;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropActivity extends Activity
{
	private static final String TAG = "CropActivity";
	private Crop_Canvas canvas = null;  
	private ImageView backImage;
    private Bitmap backBitmap;
    private Bitmap cropBitmap;
    private Button cancel,ensure,rotate;
    private int frameWidth,frameHeight;
    private int screen_width,screen_height,canvas_width,canvas_height,btn_width,btn_height;

    private static int MSG_PROCESS_IMAGE= 1;
    //private HWCloudManager hwCloudManagerText; //文本识别
    private ProgressDialog mProgress = null;
    private volatile boolean flag = false; //识别线程中断标识
    int w,h;
    private float[]  points;
    private static String EXTENDPATH = Environment.getExternalStorageDirectory() +"/";
    private Matrix matrix = new Matrix();
    private String pathBefore,pathMid,pathAfter,path;
    private float scale,density;
    private int padding = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pt_crop);

		Point p = DisplayUtil.getScreenMetrics(this);
		screen_height = p.y;
		screen_width = p.x;
		density = this.getResources().getDisplayMetrics().density;
		System.out.println("dens==========="+density);
		//
		PhotoAlbum album = (PhotoAlbum) this.getIntent().getSerializableExtra("data");
		int picturePos = this.getIntent().getExtras().getInt("pos");
		path = album.getBitList().get(picturePos).getPath();
		//path = this.getIntent().getStringExtra("path");
		Log.d(TAG, "!!!!! path is " + path);
		//path = EXTENDPATH+"universcan/MyGallery/未分类/未命名4.jpg";
		BitmapFactory.Options opt =  new  BitmapFactory.Options();
		opt.inSampleSize = BitmapUtil.getImageScale(path);
		backBitmap = BitmapFactory.decodeFile(path,opt);
		canvas = (Crop_Canvas) findViewById(R.id.myCanvas);
		canvas.setImageBitmap(backBitmap);
		cancel = (Button) findViewById(R.id.pt_crop_cancel);
		rotate = (Button) findViewById(R.id.pt_crop_rotate);
		ensure = (Button) findViewById(R.id.pt_crop_ok);
		backImage = (ImageView) findViewById(R.id.iv_back);
        init();
		/*
		new Thread(new Runnable()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg =new Message();
				msg.what = MSG_PROCESS_IMAGE;
				msg.obj = "process";
				handler.sendMessage(msg);
			}
		}).start();
		*/

		/*points = preprocessdll.Preprocess_FindSide(path,  EXTENDPATH+"MyTemp/pfind1.jpg");
		if (Build.VERSION.SDK_INT < 19) {//API 19 以前的{
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File( EXTENDPATH+"MyTemp/pfind1.jpg"))));
		}else{
			MediaScannerConnection.scanFile(CropActivity.this,
					new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() +"MyTemp/pfind1.jpg"}, null, null);
		}
		System.out.println(points.length);*/

		/* //fjm add commet
		hwCloudManagerText = new HWCloudManager(this, "b8ad3ae4-1393-4494-81ea-2851b481ac9a",
	   			"74e51a88-41ec-413e-b162-bd031fe0407e");
	   	*/
        cancel.setOnClickListener(new MyListener());
        ensure.setOnClickListener(new MyListener());
        rotate.setOnClickListener(new MyListener());
		backImage.setOnClickListener(new MyListener());

	}

	public class MyListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.pt_crop_cancel:
				CropActivity.this.finish();
				break;
			case R.id.pt_crop_rotate:
				/*
				preprocessdll.Preprocess_Rotate(pathAfter, pathAfter, 90, false);//旋转后保存到原路径
				backBitmap = BitmapFactory.decodeFile(pathAfter);
				canvas.setRotateFlag(true);
				init();
				*/
				break;
			case R.id.pt_crop_ok:
				File tmp = new File(Environment.getExternalStorageDirectory() + "/MyTemp");
                tmp.mkdirs();
                File f = new File(Environment.getExternalStorageDirectory() + "/MyTemp/" + "cropAfter" + ".png");
                try
				{
                    f.createNewFile();
                }
				catch (IOException e1)
				{
                    e1.printStackTrace();
                }

                FileOutputStream fOut = null;
                try
				{
                        fOut = new FileOutputStream(f);
                }
				catch (FileNotFoundException e)
				{
                        e.printStackTrace();
                }

                canvas.getSubsetBitmap(screen_width,screen_height).compress(Bitmap.CompressFormat.PNG, 60, fOut);
                try
				{
                        fOut.flush();
                }
				catch (IOException e)
				{
                        e.printStackTrace();
                }

                try
				{
                        fOut.close();
                }
				catch (IOException e)
				{
                        e.printStackTrace();
                }

                BitmapFactory.Options opts1 = new BitmapFactory.Options();
                opts1.inSampleSize = BitmapUtil.getImageScale(f.getAbsolutePath());
                System.out.println(f.getAbsolutePath() +" file path");
                cropBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), opts1);
				Log.d(TAG, "!!!!! saved file path is "  + f.getAbsolutePath());
				FileUtil.saveBitmap(cropBitmap);

                if(connInNet()) //如果连网
				{
	                mProgress = ProgressDialog.show(CropActivity.this, "", "正在识别......");
	                //new Thread(textThread).start();
					Log.d(TAG, "!!!!!!recothred begin");
					RecoThread recoThread = new RecoThread(f.getName(), f.getAbsolutePath());
					new Thread(recoThread).start();
                }

				break;

				case R.id.iv_back:

					break;

			default:
				break;
			}

		}

	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == MSG_PROCESS_IMAGE){
				pathMid = FileUtil.saveBitmap(backBitmap);	//将图片保存到临时文件夹
				pathAfter = imageProcessSeq(pathMid);	//对图片处理
//				pathAfter = imageProcessSeq(path);
				System.out.println(pathAfter + "----------thread");
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inSampleSize = BitmapUtil.getImageScale(pathAfter);
				//backBitmap = BitmapFactory.decodeFile(pathAfter,opts);
//				canvas.setBitmap(backBitmap);
				canvas.setImageBitmap(backBitmap);
//				FileUtil.deleteSDFile("/storage/sdcard0/MyTemp");//处理完后删除临时文件中的图片
			}
		}
		
	};

	private void init()
	{
		float scaleX = (float) (((screen_width- padding*2*density) * 1.0)/(backBitmap.getWidth() * 1.0) );
//	 	float scaleY = (float) (((screen_height-90*density-10*density)* 1.0)/(backBitmap.getHeight() * 1.0) ); 
//	 	scale = scaleX < scaleY ? scaleX:scaleY;
		scale = scaleX;
	 	System.out.println(scale+"----------->");
        canvas.setHeightAndWidth(screen_width, screen_height,scale,density);
        canvas.setBitmap(backBitmap);
	 } 
	
	protected String imageProcessSeq(String path) //找点，裁边
	{
		// TODO Auto-generated method stub
		String str;
		preprocessdll.Preprocess_Binary2(path,  EXTENDPATH+"MyTemp/pbinary1.jpg");
		str = EXTENDPATH+"MyTemp/pbinary1.jpg";
		if (Build.VERSION.SDK_INT < 19) {//API 19 以前的
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(EXTENDPATH+"MyTemp/pbinary1.jpg"))));
		}else{
			MediaScannerConnection.scanFile(CropActivity.this,
					new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() +"MyTemp/pbinary1.jpg"}, null, null);
		}
		
		return str; 
	
	}

	public class RecoThread implements Runnable
	{
		private String mFileName;
		private String mPath;

		public RecoThread(String filename, String path)
		{
			mFileName = filename;
			mPath = path;
		}

		@Override
		public void run()
		{
			Log.d(TAG, "!!!!!!!! RecoThread running !!!!!!!");
			String fid = null;
			fid = UploadImage.UploadFiletoHvn(InfoMsg.FILE_UPLOAD_TYPE, mPath, mFileName);

			if (null == fid)
			{
				Log.d(TAG, "upload file failed !!!");
				Message msg = new Message();
				msg.what = InfoMsg.FILE_UPLOAD_FAIL;
				//Bundle bundle = new Bundle();
				//bundle.putString("response", "");
				//msg.setData(bundle);
				CropActivity.this.textHandler.sendMessage(msg);
				return;
			}


			new UploadImage(textHandler).GetRapidRecogRet("", fid, "1", "4");

			/*
			Log.d(TAG, "!!!!!!!! response is " + response);
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("response", response);
			msg.setData(bundle);
			CropActivity.this.textHandler.sendMessage(msg);
			*/

		}
	}

	Runnable textThread = new Runnable()
	{
		
		@Override
		public void run()
		{
			/* //fjm add commet
			String content = null;
			if (null != cropBitmap)
			{
				content = hwCloudManagerText.textLanguage(HWLangDict.CHNS, cropBitmap);
				if (!cropBitmap.isRecycled())
				{
					cropBitmap.recycle();
				}
			}
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("content", content);
			message.setData(bundle);
			if(flag){
				return;
			}
			CropActivity.this.textHandler.sendMessage(message);
			*/
		}
	};
	
	public Handler textHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			Log.d(TAG, "!!!!!!! textHandler handle msg");
			mProgress.dismiss();
			switch (msg.what)
			{
				case InfoMsg.FILE_UPLOAD_FAIL:

				break;

				case InfoMsg.FILE_RECOGINE_TYPE:
				{
					Object obj = msg.obj;
					Log.i(TAG, obj.toString());
					processResult(obj.toString());
					/*
					JSONObject json = null;
					try
					{
						json = new JSONObject(obj.toString());
						if (json.get("code").equals("0"))
						{

						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}*/
				}
			}


		};
	};

	protected void processResult(String content)
	{
		Log.d(TAG, "!!!!!!! processResult !!!!!!");
		JSONObject obj = null;
		try
		{
			if (content != null)
			{
				obj = new JSONObject(content);
				if ("0".equals(obj.getString("code")))
				{
					Log.d(TAG, "!!!!!!! get success result");
					String result = obj.getString("result");
					Log.d(TAG, " !!!! result is " + result);
					String offset = obj.getString("offset");
					Log.d(TAG, "!!!! offset is " + offset);

					Intent backIntent = new Intent(CropActivity.this,RecResultActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("textResult", result);
					backIntent.putExtras(bundle);
					CropActivity.this.startActivity(backIntent);
					CropActivity.this.finish();
					/* //fjm add
					String result = obj.getString("textResult");
					System.out.println("textResult:" + result);
					System.out.println("content+---------"+content);
					Intent backIntent = new Intent(CropActivity.this,OcrRecognizeResultActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("textResult", result);
					backIntent.putExtras(bundle);
					if(flag){
						return;
					}
	                CropActivity.this.startActivity(backIntent);
		            CropActivity.this.finish();
		            */
				}
				else if (obj.getString("code").equals("520"))
				{
					Log.d(TAG, "!!!!!! server error 520 !!!!!!");
				}
				else if (obj.getString("code").equals("524"))
				{
					Log.d(TAG, "!!!!!! checksum error 524 !!!!!!");
				}
				else
				{
					String result = obj.getString("result");
					Toast.makeText(getApplicationContext(), "请重试！", Toast.LENGTH_SHORT).show();
					System.out.println(result);
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "请重试！", Toast.LENGTH_SHORT).show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public  boolean connInNet() //检查是否连网
	{
		ConnectionDetector connectionDetector = new ConnectionDetector(getApplication());
		if(connectionDetector.isConnectingTOInternet())
		{
			return true;
		}
		else
		{
			Toast.makeText(getApplication(), "网络连接失败，请检查网络后重试！", Toast.LENGTH_LONG).show();
			return false;
		}		
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (cropBitmap!= null &&!cropBitmap.isRecycled())
		{
			cropBitmap.recycle();
		}

		if(backBitmap != null && !backBitmap.isRecycled())
		{
			backBitmap.recycle();
			backBitmap = null;
		}

		if (null != mProgress) {
			mProgress.dismiss();
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK )
        {
			flag = true;
			CropActivity.this.finish();	
        }
		return true;
	}
 

}
