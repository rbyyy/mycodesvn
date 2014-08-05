package com.ssm.songshangmen.database;

import java.util.ArrayList;

import android.R.interpolator;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ssm.songshangmen.database.DBService;
import com.ssm.songshangmen.entity.Order;
import com.ssm.songshangmen.entity.OrderGoods;

public class OrderDao {
	
	private static final String TABLE_ORDER = "t_order";

	private static final String COLUMN_SHOP_ID = "shop_id";
	private static final String COLUMN_GOODS_ID = "goods_id";
	private static final String COLUMN_GOODS_TYPE = "goods_type"; 
	private static final String COLUMN_GOODS_NAME = "goods_name";
	private static final String COLUMN_GOODS_PRICE = "goods_price";
	private static final String COLUMN_GOODS_SALE_PRICE = "goods_sale_price";
	private static final String COLUMN_GOODS_NUMBER = "goods_number";
	
	
	private DBService service = null;
	
	public OrderDao(Context ctx) {
		super();
		if(service == null){
			service = new DBService(ctx);
		}
	}
	
	public static final String TABLE_ORDER_CREATE = "create table " + TABLE_ORDER
			+ " (_id integer primary key autoincrement, "
			+ COLUMN_SHOP_ID +" TEXT NOT NULL, " 
			+ COLUMN_GOODS_ID +" INTEGER NOT NULL, " 
			+ COLUMN_GOODS_TYPE +" INTEGER NOT NULL, "
			+ COLUMN_GOODS_NAME + " TEXT, " 
			+ COLUMN_GOODS_PRICE + " REAL, " 
			+ COLUMN_GOODS_SALE_PRICE + " REAL, " 
			+ COLUMN_GOODS_NUMBER + " INTEGER );";
	
	public boolean insert(String u_id, OrderGoods orderGoods){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_SHOP_ID, orderGoods.getShopId());
		initialValues.put(COLUMN_GOODS_ID, orderGoods.getGoodsId());
		initialValues.put(COLUMN_GOODS_TYPE, orderGoods.getType());
		initialValues.put(COLUMN_GOODS_NAME, orderGoods.getName());
		initialValues.put(COLUMN_GOODS_PRICE, orderGoods.getPrice());
		initialValues.put(COLUMN_GOODS_SALE_PRICE, orderGoods.getSalePrice());
		initialValues.put(COLUMN_GOODS_NUMBER, orderGoods.getGoodsNumber());
		
		boolean bool = mDb.insert(TABLE_ORDER, null, initialValues) != -1;
		System.out.println("insert state:" + bool);
		mDb.close();
		service.close();
		return bool;
	}
	
	//通过orderid修改表中的订单数量数据
	public boolean updateNumberById(String u_id, int goodsId, String shopId, int goodsType, int chooseNumberString) {
		int row = 0;
		if(service.tableIsExist(u_id, TABLE_ORDER)){
			SQLiteDatabase mDb = service.openWritableDatabase(u_id);
			ContentValues updateValues = new ContentValues();
			updateValues.put(COLUMN_GOODS_NUMBER, chooseNumberString);
			String sqlString = COLUMN_GOODS_ID+"="+goodsId+" and "+COLUMN_SHOP_ID+"=\'"+shopId + "\' and "+COLUMN_GOODS_TYPE+"="+goodsType;
			row = mDb.update(TABLE_ORDER, updateValues, sqlString, null);
			System.out.println("row update:" + row);
			mDb.close();
		}
		service.close();
		return row != 0;
	}
	
	//通过类型来查找
	public ArrayList<OrderGoods> readOrderGoodsByType(String u_id, String shopId, int typeInt) {
		if(service.tableIsExist(u_id, TABLE_ORDER)){
			SQLiteDatabase mDb = service.openReadableDatabase(u_id);
			String sqlString = COLUMN_SHOP_ID+"=\'"+shopId + "\' and "+COLUMN_GOODS_TYPE+"="+typeInt;
			Cursor cursor = mDb.query(TABLE_ORDER, null, sqlString, null, null, null, null);
			ArrayList<OrderGoods> msgs = readOrderGoodsFromCursor(cursor);
			cursor.close();
			mDb.close();
			service.close();
			return msgs;
		}
		service.close();
		return null;
	}
	
	public ArrayList<OrderGoods> readOrderGoods(String u_id, int goodsId, String shopId, int typeInt) {
		if(service.tableIsExist(u_id, TABLE_ORDER)){
			SQLiteDatabase mDb = service.openReadableDatabase(u_id);
			String sqlString = COLUMN_GOODS_ID+"="+goodsId+" and "+COLUMN_SHOP_ID+"=\'"+shopId + "\' and "+COLUMN_GOODS_TYPE+"="+typeInt;
			Cursor cursor = mDb.query(TABLE_ORDER, null, sqlString, null, null, null, null);
			ArrayList<OrderGoods> msgs = readOrderGoodsFromCursor(cursor);
			cursor.close();
			mDb.close();
			service.close();
			return msgs;
		}
		service.close();
		return null;
	}

	public ArrayList<OrderGoods> readOrderGoodsList(String u_id){
		if(service.tableIsExist(u_id, TABLE_ORDER)){
			SQLiteDatabase mDb = service.openReadableDatabase(u_id);
			Cursor cursor = mDb.query(TABLE_ORDER, null, null, null, null, null, null);
			ArrayList<OrderGoods> msgs = readOrderGoodsFromCursor(cursor);
			cursor.close();
			mDb.close();
			service.close();
			return msgs;
		}
		service.close();
		return null;
	}

	private ArrayList<OrderGoods> readOrderGoodsFromCursor(Cursor cursor){
		ArrayList<OrderGoods> orderGoodsArrayList = new ArrayList<OrderGoods>();
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setShopId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOP_ID)));
			orderGoods.setGoodsId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GOODS_ID)));
			orderGoods.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GOODS_NAME)));
			orderGoods.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GOODS_PRICE)));
			orderGoods.setSalePrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GOODS_SALE_PRICE)));
			orderGoods.setGoodsNumber(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GOODS_NUMBER)));
			orderGoods.setType(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GOODS_TYPE)));
			orderGoodsArrayList.add(orderGoods);
		}
		return orderGoodsArrayList;
	}

	public boolean deleteOrderGoodsByGoodsId(String user_id, OrderGoods orderGoods){
		int row = 0;
		if(service.tableIsExist(user_id, TABLE_ORDER)){
			SQLiteDatabase mDb = service.openWritableDatabase(user_id);
			String sqlString = COLUMN_GOODS_ID+"="+orderGoods.getGoodsId()+" and "+COLUMN_SHOP_ID+"=\'"+orderGoods.getShopId() + "\'";
			row = mDb.delete(TABLE_ORDER, sqlString, null);
			System.out.println("delete row:" + row);
			mDb.close();
		}
		service.close();
		return row != 0;
	}

	public boolean deleteAllOrderGoods(String u_id){
		int row = 0;
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		row = mDb.delete(TABLE_ORDER, null, null);
		mDb.close();
		service.close();
		return row != 0;
	}

}
