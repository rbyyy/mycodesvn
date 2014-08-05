package com.gos.yypad.database;

import java.util.ArrayList;

import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ShowPicture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class PictureShowDao {
	private static final String TABLE_PICTURE_SHOW = "t_pictureshowdao";

	private static final String COLUMN_PICTURE_ID = "picture_id"; //图片id
	private static final String COLUMN_AREA_CODE = "area_code";//区域code
	private static final String COLUMN_MODULE_CODE = "module_code";//模块code
	private static final String COLUMN_MODULE_NAME = "module_name";//模块
	private static final String COLUMN_PICTURE_URL = "picture_url";//图片链接
	private static final String COLUMN_DATE = "picture_date";//图片日期
	private static final String COLUMN_PICTURE_DESCRIBE = "picture_describe";//图片描述
	
	public static final String TABLE_PICTURESHOW_CREATE= "create table " + TABLE_PICTURE_SHOW 
			+ " (_id integer primary key autoincrement, "
			+ COLUMN_PICTURE_ID + " text not null, " 
			+ COLUMN_AREA_CODE + " text not null, " 
			+ COLUMN_MODULE_CODE + " text not null, " 
			+ COLUMN_MODULE_NAME + " text, " 
			+ COLUMN_PICTURE_URL + " text, " 
			+ COLUMN_DATE + " text, " 
			+ COLUMN_PICTURE_DESCRIBE + " text);";

	private DBService service = null;
	
	public PictureShowDao(Context ctx) {
		super();
		if(service == null){
			service = new DBService(ctx);
		}
	}
	//整组数据插入
	public void insertArrayList(String u_id, ArrayList<?> showPictureArrayList)
	{
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		mDb.beginTransaction();//开始事务
		try {
		    for(int i = 0; i < showPictureArrayList.size(); i++)
		    {
		    	ShowPicture showPictures = (ShowPicture) showPictureArrayList.get(i);
		    	insert(u_id, showPictures);
		    }
			mDb.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
		} finally {
			mDb.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
		}
		mDb.close();
	}
	//插入数据
	public boolean insert(String u_id, ShowPicture showpictures) {
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(COLUMN_PICTURE_ID, showpictures.getPictureId());
		initialValues.put(COLUMN_AREA_CODE, showpictures.getAreaCode());
		initialValues.put(COLUMN_MODULE_CODE, showpictures.getModuleCode());
		initialValues.put(COLUMN_MODULE_NAME, showpictures.getModuleName());
		initialValues.put(COLUMN_PICTURE_URL, showpictures.getPicUrl());
		initialValues.put(COLUMN_DATE, showpictures.getDate());
		initialValues.put(COLUMN_PICTURE_DESCRIBE, showpictures.getPicDescribe());
		
		boolean bool = mDb.insert(TABLE_PICTURE_SHOW, null, initialValues) != -1;
		System.out.println("list insert state:" + bool);
		mDb.close();
		service.close();
		return bool;
	}
	//删除整表
	public boolean delete(String u_id){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_PICTURE_SHOW, null, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//根据pictureID从表中删除数据
	public boolean deleteById(String u_id, String pictureID){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_PICTURE_SHOW, COLUMN_PICTURE_ID + "=" + pictureID, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//根据moduleCode从表中删除数据
	public boolean deleteByModuleCode(String u_id, String moduleCode) {
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_PICTURE_SHOW, COLUMN_MODULE_CODE + "=" + moduleCode, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//根据moduleCode从表中读取数据
	public Group<ShowPicture> readPictureShowListByModuleCode(String u_id, String moduleCode) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_PICTURE_SHOW, null, COLUMN_MODULE_CODE + "=" + moduleCode, null, null, null, null);
		Group<ShowPicture> showPictures = readPicturesFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return showPictures;
	}
	//读取保存图片数据库中的数据
	public Group<ShowPicture> readPictureShowList(String u_id) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_PICTURE_SHOW, null, null, null, null, null, null);
		Group<ShowPicture> showPictures = readPicturesFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return showPictures;
	}
	//通过游标读取每一条数据
	private Group<ShowPicture> readPicturesFromCursor(Cursor cursor) {
		Group<ShowPicture> showPictures = new Group<ShowPicture>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ShowPicture showPictureOne = new ShowPicture();
			
			showPictureOne.setPictureId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_ID)));
			showPictureOne.setAreaCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AREA_CODE)));
			showPictureOne.setModuleCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODULE_CODE)));
			showPictureOne.setModuleName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODULE_NAME)));
			showPictureOne.setPicUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_URL)));
			showPictureOne.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
			showPictureOne.setPicDescribe(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_DESCRIBE)));
		
			showPictures.add(showPictureOne);
		}
		return showPictures;
	}
}
