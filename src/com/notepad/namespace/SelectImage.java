package com.notepad.namespace;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.content.*;

public class SelectImage extends Activity
{

	ImageButton img1,img2,img3;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.select_img);
		
		img1=(ImageButton)findViewById(R.id.selectimgImageButton1);
		
		img2=(ImageButton)findViewById(R.id.selectimgImageButton2);
		
		img3=(ImageButton)findViewById(R.id.selectimgImageButton3);
		
		
		
		img1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
		
				intent =new Intent();
				intent.putExtra("imgpath",R.drawable.preset2);
				setResult(0x717,intent);
				finish();
			}
		});
		
		
		img2.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{

					intent =new Intent();
					intent.putExtra("imgpath",R.drawable.drop);
					setResult(0x717,intent);
					finish();
				}
			});
		
			
			
		img3.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{

					intent =new Intent();
					intent.putExtra("imgpath",R.drawable.screen);
					setResult(0x717,intent);
					finish();
				}
			});
		
			
		
		
	}
	
	
}
