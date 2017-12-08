package com.notepad.namespace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.*;
import android.view.*;
import android.view.View.*;
import android.os.*;
import java.io.*;
import android.media.*;
import android.graphics.*;

public class NotepadCheckActivity extends Activity {

	private TextView titleText = null;
	private TextView contentText = null;
	private TextView timeText = null;
	
	
	ImageView imgview;
	Button playsound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_notepad);
		
		titleText = (TextView)findViewById(R.id.checkTitle);
		contentText = (TextView)findViewById(R.id.checkContent);
		timeText = (TextView)findViewById(R.id.checkTime);
		
		imgview=(ImageView)findViewById(R.id.checknotepadImageViewimg);
		playsound=(Button)findViewById(R.id.checknotepadButtonplay);
		
		
		imgview.setVisibility(View.GONE);
		playsound.setVisibility(View.GONE);
		
		Intent intent = getIntent();//获取启动该Activity的intent对象
		
		String id = intent.getStringExtra("_id");
		String title= intent.getStringExtra("title");
		String time= intent.getStringExtra("time");
		String content = intent.getStringExtra("content");
		String res=intent.getStringExtra("imgpath");
		
		final String soundp=intent.getStringExtra("soundp");
		long t = Long.parseLong(time);
		
		String datetime = DateFormat.format("yyyy-MM-dd kk:mm:ss", t).toString();
		
		this.titleText.setText(title);
		this.timeText.setText(datetime);
		this.contentText.setText(content);
		
		if(!res.equals(""))
		{
			Bitmap bit=BitmapFactory.decodeFile(res);
			imgview.setImageBitmap(bit);
			imgview.setVisibility(View.VISIBLE);
			
		}
		
		
		if(!soundp.equals(""))
		{
			playsound.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					playRecoder(soundp);
				}
			});
			
			playsound.setVisibility(View.VISIBLE);
			
		}
	}
	
	
	public  void playRecoder(String path)
	{

		File dir = new File(Environment.getExternalStorageDirectory(),"sounds");
		if(!dir.exists()){
			dir.mkdirs();
		}

		File soundFile = new File(path);
		if(!soundFile.exists()){

		    return;
		}



		MediaPlayer mediaPlayer=new MediaPlayer();
		try {
			mediaPlayer.setDataSource(path);
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
	
	
}
