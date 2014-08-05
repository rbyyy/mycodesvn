package com.gos.yypad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ProductName;

public class ProductNameDao {
	private static final String TABLE_PRODUCT_NAME = "t_productnamedao";

	private static final String COLUMN_PRODUCT_ID = "product_id"; //产品id
	private static final String COLUMN_PRODUCT_NAME = "product_name";//产品名称
	private static final String COLUMN_PRODUCT_PATHID = "product_pathid";//产品关联
	
	public static final String TABLE_PRODUCTNAME_CREATE= "create table " + TABLE_PRODUCT_NAME 
			+ " (_id integer primary key autoincrement, "
			+ COLUMN_PRODUCT_ID + " text not null, " 
			+ COLUMN_PRODUCT_NAME + " text not null, " 
			+ COLUMN_PRODUCT_PATHID + " text not null);";

	private DBService service = null;
	
	public ProductNameDao(Context ctx) {
		super();
		if(service == null){
			service = new DBService(ctx);
		}
	}
	//插入数据
	public boolean insert(String u_id, ProductName productName) {
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(COLUMN_PRODUCT_ID, productName.getProductId());
		initialValues.put(COLUMN_PRODUCT_NAME, productName.getProductName());
		initialValues.put(COLUMN_PRODUCT_PATHID, productName.getProductPathid());
		
		boolean bool = mDb.insert(TABLE_PRODUCT_NAME, null, initialValues) != -1;
		System.out.println("list insert state:" + bool);
		mDb.close();
		service.close();
		return bool;
	}
	//删除整表
	public boolean delete(String u_id){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_PRODUCT_NAME, null, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//根据productID从表中删除数据
	public boolean deleteById(String u_id, String productID){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_PRODUCT_NAME, COLUMN_PRODUCT_ID + "=" + productID , null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//读取保存图片数据库中的数据
	public Group<ProductName> readProductNameList(String u_id) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_PRODUCT_NAME, null, null, null, null, null, null);
		Group<ProductName> productNames = readProductNameFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return productNames;
	}
	//通过游标读取每一条数据
	private Group<ProductName> readProductNameFromCursor(Cursor cursor) {
		Group<ProductName> productNames = new Group<ProductName>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ProductName productNameOne = new ProductName();
			
			productNameOne.setProductId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
			productNameOne.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
			productNameOne.setProductPathid(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PATHID)));
		
			productNames.add(productNameOne);
		}
		return productNames;
	}
}
