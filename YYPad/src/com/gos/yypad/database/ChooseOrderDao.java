package com.gos.yypad.database;
import com.gos.yypad.entity.ChooseOrder;
import com.gos.yypad.entity.Group;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChooseOrderDao {
	private static final String TABLE_CHOOSE_ORDER = "t_chooseorderdao";

	private static final String COLUMN_ORDER_ID = "order_id"; //图片id
	private static final String COLUMN_ORDER_TYPE = "order_type";//订单类型
	private static final String COLUMN_BUSINESS_NAME = "business_name";//商品名称
	private static final String COLUMN_BUSINESS_PRICE = "business_price";//商品价格
	private static final String COLUMN_BUSINESS_NUMBER = "business_number";//商品数量
	private static final String COLUMN_DATE = "business_date";//商品选购日期
	
	public static final String TABLE_CHOOSEORDER_CREATE= "create table " + TABLE_CHOOSE_ORDER 
			+ " (_id integer primary key autoincrement, "
			+ COLUMN_ORDER_ID + " text not null, " 
			+ COLUMN_ORDER_TYPE + " text not null, " 
			+ COLUMN_BUSINESS_NAME + " text, " 
			+ COLUMN_BUSINESS_PRICE + " text, " 
			+ COLUMN_BUSINESS_NUMBER + " text, "
			+ COLUMN_DATE + " text);";

	private DBService service = null;
	
	public ChooseOrderDao(Context ctx) {
		super();
		if(service == null){
			service = new DBService(ctx);
		}
	}
	
	public boolean insert(String u_id, ChooseOrder chooseOrder) {
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(COLUMN_ORDER_ID, chooseOrder.getOrder_id());
		initialValues.put(COLUMN_ORDER_TYPE, chooseOrder.getOrder_type());
		initialValues.put(COLUMN_BUSINESS_NAME, chooseOrder.getBusiness_name());
		initialValues.put(COLUMN_BUSINESS_PRICE, chooseOrder.getBusiness_price());
		initialValues.put(COLUMN_BUSINESS_NUMBER, chooseOrder.getBusiness_number());
		initialValues.put(COLUMN_DATE, chooseOrder.getBusiness_date());
		
		boolean bool = mDb.insert(TABLE_CHOOSE_ORDER, null, initialValues) != -1;
		System.out.println("list insert state:" + bool);
		mDb.close();
		service.close();
		return bool;
	}
	//删除整表
	public boolean delete(String u_id){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_CHOOSE_ORDER, null, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//根据orderId从表中删除数据
	public boolean deleteById(String u_id, String orderID){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_CHOOSE_ORDER, COLUMN_ORDER_ID + "=" + orderID, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//通过orderid修改表中的订单数量数据
	public boolean updateNumberById(String u_id, String orderID, String chooseNumberString) {
		int row = 0;
		if(service.tableIsExist(u_id, TABLE_CHOOSE_ORDER)){
			SQLiteDatabase mDb = service.openWritableDatabase(u_id);
			ContentValues updateValues = new ContentValues();
			updateValues.put(COLUMN_BUSINESS_NUMBER, chooseNumberString);
			row = mDb.update(TABLE_CHOOSE_ORDER, updateValues, COLUMN_ORDER_ID + "=" + orderID, null);
			System.out.println("row update:" + row);
			mDb.close();
		}
		service.close();
		return row != 0;
	}
	//通过orderid修改表中的订单数量数据
	public boolean updatePriceById(String u_id, String orderID, String priceString) {
		int row = 0;
		if(service.tableIsExist(u_id, TABLE_CHOOSE_ORDER)){
			SQLiteDatabase mDb = service.openWritableDatabase(u_id);
			ContentValues updateValues = new ContentValues();
			updateValues.put(COLUMN_BUSINESS_PRICE, priceString);
			row = mDb.update(TABLE_CHOOSE_ORDER, updateValues, COLUMN_ORDER_ID + "=" + orderID, null);
			System.out.println("row update:" + row);
			mDb.close();
		}
		service.close();
		return row != 0;
	}
	//查询某个orderid是否存在
	public String queryOrderIdIsExist(String u_id, String orderID) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_CHOOSE_ORDER, null, COLUMN_ORDER_ID + "=" + orderID, null, null, null, null);
		Group<ChooseOrder> chooseOrders = readChooseOrdersFromCursor(cursor);
		cursor.close();
		service.close();
		String numberString = "0";
		if(chooseOrders.size() == 1){
			numberString = chooseOrders.get(0).getBusiness_number().toString();
		}
		return numberString;
	}
	//读取订单中的数据
	public Group<ChooseOrder> readChooseOrderList(String u_id) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_CHOOSE_ORDER, null, null, null, null, null, null);
		Group<ChooseOrder> chooseOrder = readChooseOrdersFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return chooseOrder;
	}
	//通过游标读取每一条数据
	private Group<ChooseOrder> readChooseOrdersFromCursor(Cursor cursor) {
		Group<ChooseOrder> chooseOrder = new Group<ChooseOrder>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ChooseOrder cosOrder = new ChooseOrder();
			
			cosOrder.setOrder_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)));
			cosOrder.setOrder_type(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TYPE)));
			cosOrder.setBusiness_name(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUSINESS_NAME)));
			cosOrder.setBusiness_price(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUSINESS_PRICE)));
			cosOrder.setBusiness_number(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUSINESS_NUMBER)));
			cosOrder.setBusiness_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
		
			chooseOrder.add(cosOrder);
		}
		return chooseOrder;
	}
}
