package com.gos.bluetoothtemp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBService {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "bluetoothtemp.sqlite";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;

	/**
	 * 这个类主要生成一个数据库，并对数据库的版本进行管理
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context, String id) {
			super(context, id + "_" + DATABASE_NAME, null, DATABASE_VERSION);
		}

		// 在数据库第一次生成的时候会调用，这个方法一般用来生成数据库表
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TempAlarmDao.TABLE_TEMPALARM_CREATE);

		}

		// 当数据库需要升级的时候Android会主动调用这个方法，一般在这个方法里删除旧数据表建立新数据表
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TempAlarmDao.TABLE_TEMPALARM_CREATE);
			if (true)
				System.out.println("d...");
			onCreate(db);
			if (true)
				System.out.println("c2...");
		}

	}

	public DBService(Context ctx) {
		this.mCtx = ctx;
	}

	/*
	 * 打开读写数据库
	 */
	public SQLiteDatabase openWritableDatabase(String id) throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx, id);
		mDb = mDbHelper.getWritableDatabase();
		return mDb;
	}

	/*
	 * 打开读取数据库
	 */
	public SQLiteDatabase openReadableDatabase(String id) throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx, id);
		mDb = mDbHelper.getReadableDatabase();
		return mDb;
	}

	/*
	 * 关闭数据库
	 */
	public void close() {
		mDb.close();
	}

	/**
	 * 判断某张表是否存在
	 * @param tableName 表名
	 * @return
	 */
	public boolean tableIsExist(String id, String tableName) {
		boolean result = false;
		SQLiteDatabase db = this.openReadableDatabase(id);
		String sql = "select count(*) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0 && cursor.moveToNext()) {
			int count = cursor.getInt(0);
			if (count > 0) {
				result = true;
			}
		}
		cursor.close();
		db.close();
		return result;
	}
}
