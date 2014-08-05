package com.gos.yypad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ServerAddress;

public class ServerAddressDao {
	private static final String TABLE_SERVER_ADDRESS = "t_serveraddressdao";

	private static final String COLUMN_SERVER_ADDRESS = "server_address"; //服务器地址
	
	public static final String TABLE_SERVERADDRESS_CREATE= "create table " + TABLE_SERVER_ADDRESS 
			+ " (_id integer primary key autoincrement, "
			+ COLUMN_SERVER_ADDRESS + " text not null);";

	private DBService service = null;
	
	public ServerAddressDao(Context ctx) {
		super();
		if(service == null){
			service = new DBService(ctx);
		}
	}
	//插入数据
	public boolean insert(String u_id, ServerAddress serverAddress) {
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(COLUMN_SERVER_ADDRESS, serverAddress.getServerAddressString());
		
		boolean bool = mDb.insert(TABLE_SERVER_ADDRESS, null, initialValues) != -1;
		System.out.println("list insert state:" + bool);
		mDb.close();
		service.close();
		return bool;
	}
	//删除整表
	public boolean delete(String u_id){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_SERVER_ADDRESS, null, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
//	//根据pictureID从表中删除数据
//	public boolean deleteById(String u_id, String pictureID){
//		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
//		int row = mDb.delete(TABLE_SERVER_ADDRESS, COLUMN_PICTURE_ID + "=" + pictureID, null);
//		System.out.println("row:" + row);
//		service.close();
//		return row != 0;
//	}
	//读取保存服务器地址数据库中的数据
	public Group<ServerAddress> readServerAddressesList(String u_id) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_SERVER_ADDRESS, null, null, null, null, null, null);
		Group<ServerAddress> serverAddresses = readServerAddressesFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return serverAddresses;
	}
	//通过游标读取每一条数据
	private Group<ServerAddress> readServerAddressesFromCursor(Cursor cursor) {
		Group<ServerAddress> serverAddresses = new Group<ServerAddress>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ServerAddress serverAddressOne = new ServerAddress();
			serverAddressOne.setServerAddressString(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVER_ADDRESS)));
			serverAddresses.add(serverAddressOne);
		}
		return serverAddresses;
	}
}
