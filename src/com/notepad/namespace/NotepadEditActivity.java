package com.notepad.namespace;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.*;
import android.view.*;
import android.transition.*;
import android.os.*;
import java.io.*;
import android.media.*;
import android.app.*;
import android.content.*;
import android.provider.*;
import android.graphics.*;
import android.net.*;

public class NotepadEditActivity extends Activity {
	
	public static final int CHECK_STATE = 0;
	public static final int EDIT_STATE = 1;
	public static final int ALERT_STATE = 2;
	
	private int state = -1;
	
	private Button addRecord;//增加
	private Button complete;//完成
	private EditText title;
	private EditText content;
	private DatabaseManage dm = null;
	
	private String id = "";
	private String titleText = "";
	private String contentText = "";
	private String timeText = "";
	
	final int CODE=0x717;
	
	View pview;
	ImageView imgview;
	Button playsound;
	
	String soundpath="";
	
	//int resid=0;
	
	//录音
	MediaRecorder mr;
	
	String picPath;
	//boolean isHaveImg=false,isHaveSod=false;
	
	
	public static final int PHOTOZOOM = 2; // 缩放 
	public static final int PHOTORESOULT = 3;// 结果 
	public static final String IMAGE_UNSPECIFIED = "image/*"; 
	
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_notepad);
		
		Intent intent = getIntent();
		state = intent.getIntExtra("state", EDIT_STATE);
		
		//赋值控件对象
		addRecord = (Button)findViewById(R.id.addRecordButton);
		complete = (Button)findViewById(R.id.editComplete);
		title = (EditText)findViewById(R.id.editTitle);
		content = (EditText)findViewById(R.id.editContent);
		
		
		playsound=(Button)findViewById(R.id.editnotepadButtonplay);
		imgview=(ImageView)findViewById(R.id.editnotepadImageView);
		
		//设置默认不可见
		imgview.setVisibility(View.GONE);
		playsound.setVisibility(View.GONE);
		
		
		playsound.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				playRecoder();
				
			}
		});
		
		//设置监听
		addRecord.setOnClickListener(new AddRecordListener());
		complete.setOnClickListener(new EditCompleteListener());
		content.setOnTouchListener(new OnTouchListener(){

			
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				content.setSelection(content.getText().toString().length());
//				Editable ea = content.getText();
//				Selection.setSelection(ea,ea.length());
				
				return false;
			}
			
		});
		
		if(state == ALERT_STATE){//修改状态,赋值控件
			id = intent.getStringExtra("_id");
			titleText = intent.getStringExtra("title");
			contentText = intent.getStringExtra("content");
			timeText = intent.getStringExtra("time");
			
			title.setText(titleText);
			content.setText(contentText);
		}
		
		dm = new DatabaseManage(this);
		
		
		
		
	}
	
	public  void playRecoder()
	{
		
		File dir = new File(Environment.getExternalStorageDirectory(),"sounds");
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File soundFile = new File(soundpath);
		if(!soundFile.exists()){
			
		    return;
		}
		
		
		
		MediaPlayer mediaPlayer=new MediaPlayer();
		try {
			mediaPlayer.setDataSource(soundpath);
			//mediaPlayer.prepareAsync();
			mediaPlayer.prepare();
			mediaPlayer.start();
			
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
				{
					public void onCompletion(MediaPlayer mp)
					{
						mp.release();
						
					}
				});
			
			
			
		}
		catch(Exception e)
		{
			Toast.makeText(getBaseContext(),e.toString(),0).show();
		}
	}
	
	/**
	 * 监听增加按钮
	 * @author mao
	 *
	 */
	public class AddRecordListener implements OnClickListener {

		public void onClick(View v) {
			pview=getLayoutInflater().inflate(R.layout.add_menu,null);
			Button addImg,addRec;
			addImg=(Button)pview.findViewById(R.id.addmenuButton1);
			addRec=(Button)pview.findViewById(R.id.addmenuButton2);
			
			PopupWindow pop=new PopupWindow(pview,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			pop.setOutsideTouchable(true);
			
			pop.showAtLocation(v,Gravity.CENTER,0,0);
			
			
			addImg.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					//选择一张图片
					/*
					Intent intent=new Intent(NotepadEditActivity.this,SelectImage.class);
					
					startActivityForResult(intent,CODE);
					*/
					
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, 
											   null); 
					intent.setDataAndType( 
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
						IMAGE_UNSPECIFIED); 
					startActivityForResult(intent, PHOTOZOOM); 
					
					
					
					
					//设置可视
					//imgview.setVisibility(View.VISIBLE);
				}
			});
			
			
			addRec.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						//开始录音并保存
						
						//向数据库写录音文件路径
						AlertDialog.Builder builder=new AlertDialog.Builder(NotepadEditActivity.this);
						
						builder.setTitle("录音提示").setMessage("当前正在录音!").setPositiveButton("完成",new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface in,int it)
							{
								stopRecord();
							}
						});
						
						builder.create().show();
						
						//开始录音
						startRecord();
						
						//设置可见
						playsound.setVisibility(View.VISIBLE);
					}
				});
				
				
			
		}
		
		
		
		
		//开始录制
		private void startRecord(){
			if(mr == null){
				File dir = new File(Environment.getExternalStorageDirectory(),"sounds");
				if(!dir.exists()){
					dir.mkdirs();
				}
				String datetime=System.currentTimeMillis()+"";
				File soundFile = new File(dir,"abner"+datetime+".amr");//存储到SD卡当然也可上传到服务器
				if(!soundFile.exists()){
					try {
						soundFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}


				}
				soundpath=soundFile.getAbsolutePath();
				mr = new MediaRecorder();
				mr.setAudioSource(MediaRecorder.AudioSource.MIC);  //音频输入源
				mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);  //设置输出格式
				mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);  //设置编码格式
				mr.setOutputFile(soundFile.getAbsolutePath());
				try {
					mr.prepare();
					mr.start();  //开始录制
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		private void stopRecord(){
			if(mr != null){
				mr.stop();
				mr.release();
				mr = null;
			}
		}
	}
	
	

	
	
	
	

	/**
	 * 监听完成按钮
	 * @author mao
	 *
	 */
	public class EditCompleteListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			titleText = title.getText().toString();
			contentText = content.getText().toString();
//			
//			Log.v(t, t);
//			Log.v(c, c);
			try{
				dm.open();
				
				if(state == EDIT_STATE)//新增状态
					dm.insert(titleText, contentText,picPath,soundpath);
				if(state == ALERT_STATE)//修改状态
					dm.update(Integer.parseInt(id), titleText, contentText);
				
				dm.close();
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			Intent intent = new Intent();
			intent.setClass(NotepadEditActivity.this, NotepadActivity.class);
			NotepadEditActivity.this.startActivity(intent);
			
			
			//保存完毕
		}
		
	
	}

	/*
	public void startPhotoZoom(Uri uri) {
		
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }
	*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		super.onActivityResult(requestCode, resultCode, data);
		/*
		if(requestCode==CODE&&resultCode==CODE)
		{
			
		    resid=data.getIntExtra("imgpath",R.drawable.blue_sky);
			
			
			
			imgview.setImageResource(Integer.valueOf(resid));
			imgview.setVisibility(View.VISIBLE);
			
		}
		*/
		
		
		if (requestCode == PHOTOZOOM) {
           // startPhotoZoom(data.getData());
            Uri uri = data.getData();

            String[] pojo = { MediaStore.Images.Media.DATA };
            @SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(uri, pojo, null, null, null);
            if (cursor != null) {
                int index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(index);
                if (path != null) {
                    picPath = path;
                }
            }
        }
		/*
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap photo = bundle.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
//                icon.setImageBitmap(photo);
            }
            if (picPath == null) {

                Toast.makeText(getBaseContext(), "未修改图片！", Toast.LENGTH_SHORT)
					.show();
            } else {
                final File file = new File(picPath);
                Log.i("picPath", "******" + picPath);
                if (file != null) {
                    //在这的file就是你获取到的图片文件
                }
            }

        }
		
		*/
		
		Bitmap bit=BitmapFactory.decodeFile(picPath);
		imgview.setImageBitmap(bit);
		imgview.setVisibility(View.VISIBLE);
		
	}
	
	
	
	
	
	
}
