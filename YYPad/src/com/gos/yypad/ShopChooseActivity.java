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
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.exception.ParseException;
import com.gos.yypad.helper.GOSHelper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ShopChooseActivity extends BaseActivity {
	private final static String TAG = "ShopChooseActivity"; 
	private MyAdapter								mMyAdapter;//数据源
	private ArrayList<HashMap<String, Object>>		shopDomainArrayList;//店面分区
	private ProgressDialog 							mSaveDialog = null;			
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopchoose);
		//标题
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		
		topTitle.setText("店面选择");
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setVisibility(View.INVISIBLE);
					
		shopDomainArrayList = new ArrayList<HashMap<String,Object>>();
		mSaveDialog = ProgressDialog.show(ShopChooseActivity.this, "更新数据", "数据正在更新中，请稍等...", true); 
		new Thread(){
			public void run() {
				getShopDomainUrl();
			}
		}.start();
	}
	protected void getShopDomainUrl(){
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			HttpResponse aString = gosHttpOperation.invokerShopsList(dateString);
			//int statusCode = aString.getStatusLine().getStatusCode();
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				//String oneString = jsonObject.getString("data");
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					JSONArray responeArray = dataObject.getJSONArray("responseList");
					if (shopDomainArrayList.size() > 0) {
						shopDomainArrayList.removeAll(shopDomainArrayList);
					}
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("code", oneObject.getString("code").trim());
							map.put("name", oneObject.getString("name").trim());
							map.put("manager", oneObject.getString("manager").trim());
							map.put("address", oneObject.getString("address").trim());
							map.put("mobile", oneObject.getString("mobile").trim());
							map.put("open", oneObject.getString("open").trim());
							shopDomainArrayList.add(map);
						}
					}
					new Thread(){  
			            public void run(){  
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
    @SuppressLint("HandlerLeak")
	private Handler connectHanlder = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display image");  
            // 更新UI，显示图片  
            updateSettingUI();
            mSaveDialog.dismiss();
        }  
    };
    //更新界面
    protected void updateSettingUI() {
    	//门店展示列表
		mMyAdapter = new MyAdapter();
		ListView shopListView = (ListView)findViewById(R.id.shopChooseListView);
		shopListView.setAdapter(mMyAdapter);
	}
	//myadapter
	private class MyAdapter extends BaseAdapter{
		public int getCount()
		{
			return shopDomainArrayList.size();
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
			ShopChooseViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.shopchoose_item, null);
				 holder = new ShopChooseViewHolder();
				 //取到各个控件的对象
				 holder.shopNameTextView = (TextView)convertView.findViewById(R.id.shopNameTextView);//设置店面名称
				 holder.shopManagerTextView = (TextView)convertView.findViewById(R.id.shopManagerTextView);//设置店面管理员
				 holder.shopAddressTextView = (TextView)convertView.findViewById(R.id.shopAddressTextView);//设置店面地址
				 holder.shopTelephoneTextView = (TextView)convertView.findViewById(R.id.shopTelephoneTextView);//设置店面电话
				 holder.shopOpenTextView = (TextView)convertView.findViewById(R.id.shopOpenTextView);//设置是否开启店面
				 holder.shopRadioButton = (RadioButton)convertView.findViewById(R.id.shopChooseRadioButton);//店面选择
				 convertView.setTag(holder);
			}
			else {
				holder = (ShopChooseViewHolder)convertView.getTag();
			}
			
			//设置店面名称
			holder.shopNameTextView.setText((String)shopDomainArrayList.get(position).get("name"));
			//设置店面管理员
			holder.shopManagerTextView.setText((String)shopDomainArrayList.get(position).get("manager"));
			//设置店面地址
			holder.shopAddressTextView.setText((String)shopDomainArrayList.get(position).get("address"));
			//设置店面电话
			holder.shopTelephoneTextView.setText((String)shopDomainArrayList.get(position).get("mobile"));
			//设置是否开启店面
			if (((String)shopDomainArrayList.get(position).get("open")).equals("1")) {
				holder.shopOpenTextView.setText("正常");
			}
			else {
				holder.shopOpenTextView.setText("禁止");
			}
			
			return convertView;
		}
	}
	//整机专区类
	 /**存放控件*/
   public final class ShopChooseViewHolder{
   		public TextView     shopNameTextView; //店面名称
   		public TextView 	shopManagerTextView; //店面管理
   		public TextView		shopAddressTextView; //店面地址
   		public TextView     shopTelephoneTextView; //店面电话
   		public TextView		shopOpenTextView; //店面是否启用
   		public RadioButton	shopRadioButton; //选择店面
   }
}
