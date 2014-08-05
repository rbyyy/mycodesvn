package com.gos.bluetoothtemp.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TempAlarmDao {
	private static final String TABLE_TEMP_ALARM 	= "t_tempalarminfos";

	private static final String COLUMN_TEMP_MSG_ID		= "temp_id";//消息id
	private static final String COLUMN_TEMP_TAG 		= "temp_tag";//温度报警标志
	private static final String COLUMN_TEMP_CONTENT 	= "temp_content";//温度报警内容
	private static final String COLUMN_DATE		 		= "temp_date";//温度报警日期
	private static final String COLUMN_TEMP_TYPE 		= "temp_type";//温度类型
	private static final String COlUMN_TEMP_DEAL		= "temp_deal";//是否已处理
	
	public static final String TABLE_TEMPALARM_CREATE= "create table " + TABLE_TEMP_ALARM 
			+ " (_id integer primary key autoincrement, "
			+ COLUMN_TEMP_MSG_ID  + " text, "
			+ COLUMN_TEMP_TAG + " text, " 
			+ COLUMN_TEMP_CONTENT + " text, " 
			+ COLUMN_DATE + " text, "
			+ COlUMN_TEMP_DEAL +" text, "
			+ COLUMN_TEMP_TYPE + " text );";

	private DBService service = null;
	
	public TempAlarmDao(Context ctx) {
		super();
		if(service == null){
			service = new DBService(ctx);
		}
	}
	/**数据插入*/
	public boolean insert(String u_id, TempAlarm tempAlarm) {
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(COLUMN_TEMP_MSG_ID, tempAlarm.getTemp_msg_id());
		initialValues.put(COLUMN_TEMP_TAG, tempAlarm.getTemp_tag());
		initialValues.put(COLUMN_TEMP_CONTENT, tempAlarm.getTemp_content());
		initialValues.put(COLUMN_DATE, tempAlarm.getTemp_date());
		initialValues.put(COlUMN_TEMP_DEAL, "0");
		initialValues.put(COLUMN_TEMP_TYPE, tempAlarm.getTemp_type());
		
		boolean bool = mDb.insert(TABLE_TEMP_ALARM, null, initialValues) != -1;
		System.out.println("list insert state:" + bool);
		mDb.close();
		service.close();
		return bool;
	}
	//删除整表
	public boolean delete(String u_id){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_TEMP_ALARM, null, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
//	//根据orderId从表中删除数据
//	public boolean deleteById(String u_id, String orderID){
//		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
//		int row = mDb.delete(TABLE_TEMP_ALARM, COLUMN_ORDER_ID + "=" + orderID, null);
//		System.out.println("row:" + row);
//		service.close();
//		return row != 0;
//	}
	//通过msgid修改表中的数据
	public boolean updateDataByMsgId(String u_id, String msgID) {
		int row = 0;
		if(service.tableIsExist(u_id, TABLE_TEMP_ALARM)){
			SQLiteDatabase mDb = service.openWritableDatabase(u_id);
			ContentValues updateValues = new ContentValues();
			updateValues.put(COlUMN_TEMP_DEAL, "1");
			row = mDb.update(TABLE_TEMP_ALARM, updateValues, COLUMN_TEMP_MSG_ID + "=" + msgID, null);
			System.out.println("row update:" + row);
			mDb.close();
		}
		service.close();
		return row != 0;
	}
//	//通过orderid修改表中的订单数量数据
//	public boolean updatePriceById(String u_id, String orderID, String priceString) {
//		int row = 0;
//		if(service.tableIsExist(u_id, TABLE_CHOOSE_ORDER)){
//			SQLiteDatabase mDb = service.openWritableDatabase(u_id);
//			ContentValues updateValues = new ContentValues();
//			updateValues.put(COLUMN_BUSINESS_PRICE, priceString);
//			row = mDb.update(TABLE_CHOOSE_ORDER, updateValues, COLUMN_ORDER_ID + "=" + orderID, null);
//			System.out.println("row update:" + row);
//			mDb.close();
//		}
//		service.close();
//		return row != 0;
//	}
//	//查询某个orderid是否存在
//	public String queryOrderIdIsExist(String u_id, String orderID) {
//		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
//		Cursor cursor = mDb.query(TABLE_CHOOSE_ORDER, null, COLUMN_ORDER_ID + "=" + orderID, null, null, null, null);
//		Group<ChooseOrder> chooseOrders = readChooseOrdersFromCursor(cursor);
//		cursor.close();
//		service.close();
//		String numberString = "0";
//		if(chooseOrders.size() == 1){
//			numberString = chooseOrders.get(0).getBusiness_number().toString();
//		}
//		return numberString;
//	}
	//根据类型和是否已读来读取数据
	public ArrayList<TempAlarm> readTempAlarmListByAlarmTypeAndDeal(String u_id, String alarmTypeString) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id); 
		Cursor cursor = mDb.query(TABLE_TEMP_ALARM, null, COLUMN_TEMP_TYPE+"="+alarmTypeString + " and "+COlUMN_TEMP_DEAL+"=0", null, null, null, COLUMN_DATE+" desc");
		ArrayList<TempAlarm> tempAlarmArrayList = readChooseOrdersFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return tempAlarmArrayList;
	}
	//根据温度类型来读取报警信息
	public ArrayList<TempAlarm> readTempAlarmListByAlarmType(String u_id,String alarmTypeString) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id); 
		Cursor cursor = mDb.query(TABLE_TEMP_ALARM, null, COLUMN_TEMP_TYPE+"="+alarmTypeString, null, null, null, COLUMN_DATE+" desc");
		ArrayList<TempAlarm> tempAlarmArrayList = readChooseOrdersFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return tempAlarmArrayList;
	}
	//读取订单中的数据
	public ArrayList<TempAlarm> readTempAlarmList(String u_id) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id); 
		Cursor cursor = mDb.query(TABLE_TEMP_ALARM, null, null, null, null, null, COLUMN_DATE+" desc");
		ArrayList<TempAlarm> tempAlarmArrayList = readChooseOrdersFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return tempAlarmArrayList;
	}
	//通过游标读取每一条数据
	private ArrayList<TempAlarm> readChooseOrdersFromCursor(Cursor cursor) {
		ArrayList<TempAlarm> tempAlarmArrayList = new ArrayList<TempAlarm>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			TempAlarm tempAlarm = new TempAlarm();
			
			tempAlarm.setTemp_msg_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEMP_MSG_ID)));
			tempAlarm.setTemp_tag(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEMP_TAG)));
			tempAlarm.setTemp_content(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEMP_CONTENT)));
			tempAlarm.setTemp_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
			tempAlarm.setTemp_deal(cursor.getString(cursor.getColumnIndexOrThrow(COlUMN_TEMP_DEAL)));
			tempAlarm.setTemp_type(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEMP_TYPE)));

			tempAlarmArrayList.add(tempAlarm);
		}
		return tempAlarmArrayList;
	}
	
}
