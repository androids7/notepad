package com.notepad.namespace;


import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DatabaseManage {

	private Context mContext = null;
	
	private SQLiteDatabase mSQLiteDatabase = null;//用于操作数据库的对象
	private DatabaseHelper dh = null;//用于创建数据库的对象
	
	private String dbName = "notepad.db";
	private int dbVersion = 1;

	String stitle,data;
	
	long datetimes;
	
	public DatabaseManage(Context context){
		mContext = context;
	}
	
	/**
	 * 打开数据库
	 */
	public void open(){
		
		try{
			dh = new DatabaseHelper(mContext, dbName, null, dbVersion);
			if(dh == null){
				Log.v("msg", "is null");
				return ;
			}
			mSQLiteDatabase = dh.getWritableDatabase();
			//dh.onOpen(mSQLiteDatabase);
			
		}catch(SQLiteException se){
			se.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库
	 */
	public void close(){
		
		mSQLiteDatabase.close();
		dh.close();
		
	}
	
	//获取列表
	public Cursor selectAll(){
		Cursor cursor = null;
		try{
			String sql = "select * from record";
			cursor = mSQLiteDatabase.rawQuery(sql, null);
		}catch(Exception ex){
			ex.printStackTrace();
			cursor = null;
		}
		return cursor;
	}
	
	public Cursor selectById(int id){
		
		//String result[] = {};
		Cursor cursor = null;
		try{
			String sql = "select * from record where _id='" + id +"'";
			cursor = mSQLiteDatabase.rawQuery(sql, null);
		}catch(Exception ex){
			ex.printStackTrace();
			cursor = null;
		}
		
		return cursor;
	}
	
	//插入数据
	public long insert(String title, String content,String imgpath,String soundpath){
		
		long datetime = System.currentTimeMillis();
		long l = -1;
		/*
		this.data=content;
		this.stitle=title;
		this.datetimes=datetime;
		*/
		try{
			ContentValues cv = new ContentValues();
			cv.put("title", title);
			cv.put("content", content);
			cv.put("time", datetime);
			cv.put("imgpath",imgpath);
			cv.put("soundp",soundpath);
			
			
			l = mSQLiteDatabase.insert("record", null, cv);
		//	Log.v("datetime", datetime+""+l);
		}catch(Exception ex){
			ex.printStackTrace();
			l = -1;
		}
		return l;
		
	}
	
	
	/*

	//插入录音文件地址数据
	public long insertSound(String path){

		long l = -1;
		try{
			ContentValues cv = new ContentValues();
			
			
			cv.put("title", stitle);
			cv.put("content", data);
			cv.put("time", datetimes);
			cv.put("soundp", path);
			l = mSQLiteDatabase.insert("record", null, cv);
			//	Log.v("datetime", datetime+""+l);
		}catch(Exception ex){
			ex.printStackTrace();
			l = -1;
		}
		return l;

	}
	
	*/
/*
	//插入图片数据
	public long insertImg(int contet){
		
		long l = -1;
		try{
			ContentValues cv = new ContentValues();
			
			cv.put("title", stitle);
			cv.put("content", data);
			cv.put("time", datetimes);
			
			cv.put("imgpath", contet);
		
			l = mSQLiteDatabase.insert("record", null, cv);
			//	Log.v("datetime", datetime+""+l);
		}catch(Exception ex){
			ex.printStackTrace();
			l = -1;
		}
		return l;

	}
	*/
	
	
	//删除数据
	public int delete(long id){
		int affect = 0;
		try{
			affect = mSQLiteDatabase.delete("record", "_id=?", new String[]{id+""});
		}catch(Exception ex){
			ex.printStackTrace();
			affect = -1;
		}
		
		return affect;
	}
	
	//修改数据
	public int update(int id, String title, String content){
		int affect = 0;
		try{
			ContentValues cv = new ContentValues();
			
			cv.put("title", title);
			cv.put("content", content);
			String w[] = {id+""};
			affect = mSQLiteDatabase.update("record", cv, "_id=?", w);
		}catch(Exception ex){
			ex.printStackTrace();
			affect = -1;
		}
		return affect;
	}

}
