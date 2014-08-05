package com.gos.yypad.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ProductList;

public class ProductListDao {
	private static final String TABLE_PRODUCT_LIST = "t_productlistdao";

	private static final String COLUMN_PRODUCT_ID = "product_id"; //产品id
	private static final String COLUMN_PRODUCT_NAME = "product_name";//产品名称
	private static final String COLUMN_PRODUCT_PICURL = "product_picurl";//产品图片链接
	private static final String COLUMN_PRODUCT_PRICE = "product_price"; //产品价格
	private static final String COLUMN_PRODUCT_PREMARK = "product_premark";//产品备注
	private static final String COLUMN_PRODUCT_OPERATORNAME = "product_operatorname";//产品操作系统
	private static final String COLUMN_PRODUCT_ISON = "product_ison"; //产品是否在售
	private static final String COLUMN_PRODUCT_CLASSPATH = "product_classpath"; //产品所属类别
	
	public static final String TABLE_PRODUCTLIST_CREATE= "create table " + TABLE_PRODUCT_LIST 
			+ " (_id integer primary key autoincrement, "
			+ COLUMN_PRODUCT_ID + " text not null, " 
			+ COLUMN_PRODUCT_NAME + " text , " 
			+ COLUMN_PRODUCT_PICURL + " text , " 
			+ COLUMN_PRODUCT_PRICE + " text , "
			+ COLUMN_PRODUCT_PREMARK + " text , " 
			+ COLUMN_PRODUCT_OPERATORNAME + " text , "
			+ COLUMN_PRODUCT_ISON + " text , "
			+ COLUMN_PRODUCT_CLASSPATH + " text not null);";

	private DBService service = null;
	
	public ProductListDao(Context ctx) {
		super();
		if(service == null){
			service = new DBService(ctx);
		}
	}
	//插入数据
	public boolean insert(String u_id, ProductList productList) {
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(COLUMN_PRODUCT_ID, productList.getProductId());
		initialValues.put(COLUMN_PRODUCT_NAME, productList.getProductName());
		initialValues.put(COLUMN_PRODUCT_PICURL, productList.getProductPicurl());
		initialValues.put(COLUMN_PRODUCT_PRICE, productList.getProductPrice());
		initialValues.put(COLUMN_PRODUCT_PREMARK, productList.getProductPremark());
		initialValues.put(COLUMN_PRODUCT_OPERATORNAME, productList.getProductOperatorname());
		initialValues.put(COLUMN_PRODUCT_ISON, productList.getProductIson());
		initialValues.put(COLUMN_PRODUCT_CLASSPATH, productList.getProductClassPath());
		
		boolean bool = mDb.insert(TABLE_PRODUCT_LIST, null, initialValues) != -1;
		System.out.println("list insert state:" + bool);
		mDb.close();
		service.close();
		return bool;
	}
	//删除整表
	public boolean delete(String u_id){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_PRODUCT_LIST, null, null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//根据productID从表中删除数据
	public boolean deleteById(String u_id, String productID){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		int row = mDb.delete(TABLE_PRODUCT_LIST, COLUMN_PRODUCT_ID + "=" + productID , null);
		System.out.println("row:" + row);
		service.close();
		return row != 0;
	}
	//根据productID从表中查询数据
	public Group<ProductList> readProductById(String u_id, String productID){
		SQLiteDatabase mDb = service.openWritableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_PRODUCT_LIST, null, COLUMN_PRODUCT_ID + "=" + productID , null, null, null, null);
		Group<ProductList> productLists = readProductListFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return productLists;
	}
	//通过pathid来读取数据
	public Group<ProductList> readProductListByPathId(String u_id, String pageLongString, String pathIdString) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_PRODUCT_LIST, null, COLUMN_PRODUCT_CLASSPATH + " like " + "\'"+pathIdString + "%\'", null, null, null, null, pageLongString);
		Group<ProductList> productLists = readProductListFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return productLists;
	}
	//读取保存图片数据表中的10条数据
	public Group<ProductList> readTenProductList(String u_id, String limitString) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_PRODUCT_LIST, null, null, null, null, null, null,limitString);
		Group<ProductList> productLists = readProductListFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return productLists;
	}
	//读取保存图片数据库中的数据
	public Group<ProductList> readAllProductList(String u_id) {
		SQLiteDatabase mDb = service.openReadableDatabase(u_id);
		Cursor cursor = mDb.query(TABLE_PRODUCT_LIST, null, null, null, null, null, null);
		Group<ProductList> productLists = readProductListFromCursor(cursor);
		cursor.close();
		mDb.close();
		service.close();
		return productLists;
	}
	//通过游标读取每一条数据
	private Group<ProductList> readProductListFromCursor(Cursor cursor) {
		Group<ProductList> productLists = new Group<ProductList>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			ProductList productListOne = new ProductList();
			
			productListOne.setProductId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
			productListOne.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)));
			productListOne.setProductPicurl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PICURL)));
			productListOne.setProductPrice(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE)));
			productListOne.setProductPremark(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PREMARK)));
			productListOne.setProductOperatorname(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_OPERATORNAME)));
			productListOne.setProductIson(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ISON)));
			productListOne.setProductClassPath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_CLASSPATH)));
		
			productLists.add(productListOne);
		}
		return productLists;
	}
}
