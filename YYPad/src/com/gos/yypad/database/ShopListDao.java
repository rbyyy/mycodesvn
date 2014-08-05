package com.gos.yypad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ShopList;

public class ShopListDao {
	private static final String TABLE_SHOP_LIST = "t_shoplistdao";

	private static final String COLUMN_SHOP_CODE = "shop_code"; //门店code
	private static final String COLUMN_SHOP_NAME = "shop_name"; //门店名称
	private static final String COLUMN_SHOP_MANAGER = "shop_manager";//门店管理者
	private static final String COLUMN_SHOP_ADDRESS = "shop_address";//门店地址
	private static final String COLUMN_SHOP_MOBILE = "shop_mobile";//门店联系电话
	private static final String COLUMN_SHOP_OPEN = "shop_open";//门店是否启用
	private static final String COLUMN_SHOP_SELECTED = "shop_selected";//当前选择的门店
	
	public static final String TABLE_SHOPLIST_CREATE= "create table " + TABLE_SHOP_LIST 
			+ " (_id integer primary key autoincrement, "
			+ COLUMN_SHOP_CODE + " text not null, " 
			+ COLUMN_SHOP_NAME + " text , " 
			+ COLUMN_SHOP_MANAGER + " text, " 
			+ COLUMN_SHOP_ADDRESS + " text, " 
			+ COLUMN_SHOP_MOBILE + " text, "
			+ COLUMN_SHOP_OPEN + " text, "
			+ COLUMN_SHOP_SELECTED + " text);";

	private DBService service = null;
	
	public ShopListDao(Context ctx) {
		super();
		if(service == null){
			service = new DBService(ctx);
		}
	}
	//插入数据
	public boolean insert(String u_id, ShopList shopList) {
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(COLUMN_SHOP_CODE, shopList.getShopcode());
		initialValues.put(COLUMN_SHOP_NAME, shopList.getShopname());
		initialValues.put(COLUMN_SHOP_MANAGER, shopList.getShopmanager());
		initialValues.put(COLUMN_SHOP_ADDRESS, shopList.getShopaddress());
		initialValues.put(COLUMN_SHOP_MOBILE, shopList.getShopmobile());
		initialValues.put(COLUMN_SHOP_OPEN, shopList.getShopopen());
		initialValues.put(COLUMN_SHOP_SELECTED, shopList.getShopselected());
		
		boolean bool = mDb.insert(TABLE_SHOP_LIST, null, initialValues) != -1;
		System.out.println("list insert state:" + bool);
		mDb.close();
		service.close();
		return bool;
	}
	//删除整表
	public boolean delete(String u_id){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_SHOP_LIST, null, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//根据shopID从表中删除数据
	public boolean deleteById(String u_id, String shopID){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_SHOP_LIST, COLUMN_SHOP_CODE + "=" + shopID, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//读取店面的数据
	public Group<ShopList> readShopLists(String u_id) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_SHOP_LIST, null, null, null, null, null, null);
		Group<ShopList> shopLists = readShopListsFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return shopLists;
	}
	//通过游标读取每一条数据
	private Group<ShopList> readShopListsFromCursor(Cursor cursor) {
		Group<ShopList> shopLists = new Group<ShopList>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ShopList cosShopList = new ShopList();
			
			cosShopList.setShopcode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOP_CODE)));
			cosShopList.setShopname(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOP_NAME)));
			cosShopList.setShopmanager(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOP_MANAGER)));
			cosShopList.setShopaddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOP_ADDRESS)));
			cosShopList.setShopmobile(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOP_MOBILE)));
			cosShopList.setShopopen(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOP_OPEN)));
			cosShopList.setShopselected(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOP_SELECTED)));
		
			shopLists.add(cosShopList);
		}
		return shopLists;
	}
	//当前选择的店面的改变
	public boolean updateShopSelectedById(String shopCodeString, String shopSeletedString, String user_id){
		int row = 0;
		if(service.tableIsExist(user_id, TABLE_SHOP_LIST)){
			SQLiteDatabase mDb = service.openWritableDatabase(user_id);
			ContentValues updateValues = new ContentValues();
			updateValues.put(COLUMN_SHOP_SELECTED, shopSeletedString);
			row = mDb.update(TABLE_SHOP_LIST, updateValues, COLUMN_SHOP_CODE+ "=" + shopCodeString, null);
			System.out.println("row update:" + row);
			mDb.close();
		}
		service.close();
		return row != 0;
	}
}
