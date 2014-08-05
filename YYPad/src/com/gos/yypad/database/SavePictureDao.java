package com.gos.yypad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ShowPicture;

public class SavePictureDao {
	private static final String TABLE_SAVE_PICTURE = "t_savepicturedao";

	private static final String COLUMN_PICTURE_NAME = "picture_name"; //图片名称
	private static final String COLUMN_PICTURE_STREAM = "picture_stream";//图片数据流
	
	public static final String TABLE_SAVEPICTURE_CREATE= "create table " + TABLE_SAVE_PICTURE 
			+ " (_id integer primary key autoincrement, "
			+ COLUMN_PICTURE_NAME + " text not null, " 
			+ COLUMN_PICTURE_STREAM + " blob not null);";

	private DBService service = null;
	
	public SavePictureDao(Context ctx) {
		super();
		if(service == null){
			service = new DBService(ctx);
		}
	}
	//插入数据
	public boolean insert(String u_id, ShowPicture showpictures) {
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(COLUMN_PICTURE_NAME, showpictures.getPictureId());
		initialValues.put(COLUMN_PICTURE_STREAM, showpictures.getAreaCode());
		
		boolean bool = mDb.insert(TABLE_SAVE_PICTURE, null, initialValues) != -1;
		System.out.println("list insert state:" + bool);
		mDb.close();
		service.close();
		return bool;
	}
	//删除整表
	public boolean delete(String u_id){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_SAVE_PICTURE, null, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//根据pictureID从表中删除数据
	public boolean deleteById(String u_id, String pictureName){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_SAVE_PICTURE, COLUMN_PICTURE_NAME + "=" + pictureName, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//读取保存图片数据库中的数据
	public Group<ShowPicture> readPictureShowList(String u_id) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_SAVE_PICTURE, null, null, null, null, null, null);
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
			
			showPictureOne.setPictureId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_NAME)));
			showPictureOne.setAreaCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE_STREAM)));
		
			showPictures.add(showPictureOne);
		}
		return showPictures;
	}
}
