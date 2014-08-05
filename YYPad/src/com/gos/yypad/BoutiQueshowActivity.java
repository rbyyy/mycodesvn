package com.gos.yypad;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.yypad.database.ProductListDao;
import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ProductList;
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.exception.ParseException;
import com.gos.yypad.helper.GOSHelper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class BoutiQueshowActivity extends BaseActivity {
	private final static String TAG = "BoutiQueshowActivity";
	private MyAdapter mMyAdapter;//数据源
	private ArrayList<HashMap<String, Object>> kArrayList;
	private ProgressDialog mSaveDialog = null;
	private ProductListDao						productListDao;//产品列表
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boutiqueshow);
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		topTitle.setText("精品展示");
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		productListDao = new ProductListDao(this);
		kArrayList = new ArrayList<HashMap<String,Object>>();
		requestAllProduct();
		// 更新UI，显示图片  
        updateBoutiqueshowMachGridView();
//		//进行数据获取
//		mSaveDialog = ProgressDialog.show(BoutiQueshowActivity.this, "更新数据", "数据正在更新中，请稍等...", true); 
//		new Thread(){  
//            public void run(){  
//            	getProductInfoByNetWork();  
//            }  
//        }.start();
	}
	//获取所有产品
	protected void requestAllProduct() {
		if (kArrayList.size() > 0) {
			kArrayList.removeAll(kArrayList);
		}
		String pathIdString = ",2,91,92";
		Group<ProductList> productLists = productListDao.readProductListByPathId("yy", "10",pathIdString);
		if (productLists.size() > 0) {
			for (int i = 0; i < productLists.size(); i++) {
				ProductList productList = productLists.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("code", productList.getProductId());
				map.put("type", "精品");
				map.put("name", productList.getProductName());
				map.put("picUrl", productList.getProductPicurl());
				float priceFloat = Float.valueOf(productList.getProductPrice()).floatValue();
				int priceInt = (int)priceFloat;
				//int priceInt = Integer.valueOf((String)oneObject.getString("price").trim());
				String priceString = String.valueOf(priceInt);
				map.put("price", priceString);
				map.put("premark", productList.getProductPremark());
				map.put("operatorName", productList.getProductPremark());
				map.put("isOn", productList.getProductIson());
				kArrayList.add(map);
			}
		}
	}
	
	
	//根据配件和品牌进行网络数据拉取
	protected void getProductInfoByNetWork() {
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			HttpResponse aString = gosHttpOperation.invokerProductShowList(dateString, 
					"1","10", "0", "2", "91", "92");
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					JSONArray responeArray = dataObject.getJSONArray("responseList");
					if (kArrayList.size() > 0) {
						kArrayList.removeAll(kArrayList);
					}
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("code", oneObject.getString("code").trim());
							map.put("type", "精品");
							map.put("name", oneObject.getString("name").trim());
							map.put("picUrl", oneObject.getString("picUrl").trim());
							float priceFloat = Float.valueOf((String)oneObject.getString("price").trim()).floatValue();
							int priceInt = (int)priceFloat;
							String priceString = String.valueOf(priceInt);
							map.put("price", priceString);
							map.put("premark", oneObject.getString("premark").trim());
							map.put("operatorName", oneObject.getString("operatorName").trim());
							map.put("isOn", oneObject.getString("isOn").trim());
							kArrayList.add(map);
						}
					}
					new Thread()
					{
						public void run() {
							// 发送消息，通知handler在主线程中更新UI 
				            connectHanlder.sendEmptyMessage(0);
						}
					}.start();
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	 //主线程
    private Handler connectHanlder = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display view");  
            // 更新UI，显示图片  
            updateBoutiqueshowMachGridView();
        	mSaveDialog.dismiss(); 
        }  
    };
    //更新界面显示
    protected void updateBoutiqueshowMachGridView() {
		//精品展示列表
		mMyAdapter = new MyAdapter();
		GridView completeGridView = (GridView)findViewById(R.id.boutiqueshowMachGridView);
		completeGridView.setAdapter(mMyAdapter);
	}
	//
	private class MyAdapter extends BaseAdapter{
		public int getCount()
		{
			return kArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BQViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				 convertView = LayoutInflater.from(getApplication()).inflate(R.layout.boutiqueshow_item, null);
				 holder = new BQViewHolder();
				 //取到各个控件的对象
				 holder.imageView = (ImageView)convertView.findViewById(R.id.boutiqueshowImageViewOne);//背景色设置
				 holder.premarkText = (TextView)convertView.findViewById(R.id.boutiqueshowDescTextViewOne);//文字描述
				 holder.truePriceText = (TextView)convertView.findViewById(R.id.boutiqueshowTruePriceTextViewOne);//优惠价格
				 holder.falsePriceText = (TextView)convertView.findViewById(R.id.boutiqueshowFlasePriceTextViewOne);//未优惠的价格
				 holder.itemTitle = (TextView)convertView.findViewById(R.id.BqItemId);//itemID
				 convertView.setTag(holder);
			}
			else {
				holder = (BQViewHolder)convertView.getTag();
			}
			//设置整机图片
			//holder.imageView.setImageResource(R.drawable.boutiqueshowmachimage);//(Integer) kArrayList.get(position).get("ItemImage")
			//imageLoader.displayImage((String)kArrayList.get(position).get("picUrl"), holder.imageView, options);
			String pictureUrlString = (String)kArrayList.get(position).get("picUrl");
//			String[] urlStrings = pictureUrlString.split("\\/");
//			String filePathString = urlStrings[urlStrings.length-1];
//			String extPath = GOSHelper.getExternDir()+"/"+filePathString;
			
			String[] urlStrings = pictureUrlString.split("\\//");
			String[] oneUrlStrings = urlStrings[urlStrings.length-1].split("\\/");
			String twoUrlString = urlStrings[urlStrings.length-1].replace(oneUrlStrings[0], "");

			String extPath = GOSHelper.getExternDir()+twoUrlString;
			
			try {
				Bitmap bitmap = GOSHelper.getBitmapByFileName(extPath);
				holder.imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
			}
			//设置整机说明
			//holder.premarkText.setText((String)kArrayList.get(position).get("premark"));
			//设置优惠价格
			holder.truePriceText.setText((String)kArrayList.get(position).get("price"));
			//设置最高价格
			holder.falsePriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
			holder.falsePriceText.setText((String)kArrayList.get(position).get("price"));
			//设置itemid
			holder.itemTitle.setTag(position);
			holder.itemTitle.setText((String)kArrayList.get(position).get("code"));
			
			//为每个item添加click事件
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					accessProductMainSceneView(v);
				}
			});
			
			return convertView;
		}
	}
	//精品专区类
	 /**存放控件*/
	  public final class BQViewHolder{
	  		public ImageView    imageView;
	  		public TextView 	premarkText;
	  		public TextView		truePriceText;
	  		public TextView     falsePriceText;
	  		public TextView		itemTitle;
	  }
   //进入整机专区产品主图界面
   	private void accessProductMainSceneView(View v)
   	{
   		int position = (Integer)(((TextView)v.findViewById(R.id.BqItemId)).getTag());
		Intent productIntent = new Intent(BoutiQueshowActivity.this, ProductMainSceneActivity.class);
		productIntent.putExtra("accessProductMainType", "3");
		productIntent.putExtra("productCode", (String)kArrayList.get(position).get("code"));
		productIntent.putExtra("productType", (String)kArrayList.get(position).get("type"));
		productIntent.putExtra("productName", (String)kArrayList.get(position).get("name"));
		productIntent.putExtra("productImageUrl", (String)kArrayList.get(position).get("picUrl"));
		productIntent.putExtra("productPrice", (String)kArrayList.get(position).get("price"));
		productIntent.putExtra("productPremark", (String)kArrayList.get(position).get("premark"));
		productIntent.putExtra("productOperatorNme", (String)kArrayList.get(position).get("operatorName"));
		productIntent.putExtra("productIsOn", (String)kArrayList.get(position).get("isOn"));
		BoutiQueshowActivity.this.startActivity(productIntent);
   	}
  //进入精品展示详情界面
 	private void accessProductDetailView (View v) {
 		int position = (Integer)(((TextView)v.findViewById(R.id.BqItemId)).getTag());
 		String pictureUrlString = (String)kArrayList.get(position).get("picUrl");
 		String productRemarkString = (String)kArrayList.get(position).get("premark");
 		Intent productIntent = new Intent(BoutiQueshowActivity.this, ProductDetailActivity.class);
 		
 		productIntent.putExtra("productImageUrl", pictureUrlString);
 		productIntent.putExtra("productPremark", productRemarkString);
 		startActivity(productIntent);
 	}
}
