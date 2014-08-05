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
import com.gos.yypad.entity.ChooseOrder;
import com.gos.yypad.entity.ProductList;
import com.gos.yypad.entity.ProductName;
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.exception.ParseException;
import com.gos.yypad.helper.GOSHelper;
import com.gos.yypad.database.ChooseOrderDao;
import com.gos.yypad.database.ProductListDao;
import com.gos.yypad.database.ProductNameDao;
import com.gos.yypad.entity.Group;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;



@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class DiyDivsionActivity extends BaseActivity {
	
	private final static String TAG = "SplashActivity";
	private String								productTypeString;//产品类型数据 	
	private MyAdapter						 	mMyAdapter;//配件数据适配器
	private ArrayList<HashMap<String, Object>> 	accessoryArrayList;
	private MyBrandAdapter						mMyBrandAdapter;//配件类型数据适配器
	private ArrayList<HashMap<String, Object>> 	brandArrayList;
	private MyAccessoryListAdapter 				mMyAccessoryListAdapter;//配件列表数据适配器
	private ArrayList<HashMap<String, Object>> 	accessoryListArrayList;
	private PullToRefreshListView 				accessoryListPullRefreshListView;
	private MyChooseListAdapter 				mMyChooseListAdapter;//选购单列表数据适配器
	private ArrayList<HashMap<String, Object>> 	chooseListArrayList;
	private ProgressDialog 						mSaveDialog = null;
	private Toast 								toast;
	private ChooseOrderDao						chooseOrderDao;//订单数据库
	private ProductNameDao						productNameDao;//产品目录
	private ProductListDao						productListDao;//产品列表
	
	private Group<ProductName> 					productNames;
	private String								allPathIdString;//
	private String								allCountString;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diydivsion);
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		topTitle.setText("DIY专区");
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		accessoryArrayList = new ArrayList<HashMap<String,Object>>();
		brandArrayList = new ArrayList<HashMap<String,Object>>();
		accessoryListArrayList = new ArrayList<HashMap<String,Object>>();
		chooseListArrayList = new ArrayList<HashMap<String,Object>>();
		
		chooseOrderDao = new ChooseOrderDao(this);//订单
		productNameDao = new ProductNameDao(this);//
		productListDao = new ProductListDao(this);
		productNames = productNameDao.readProductNameList("yy");
		if (productNames.size()>0) {
			if (accessoryArrayList.size() > 0) {
				accessoryArrayList.removeAll(accessoryArrayList);
			}
			for (int i = 0; i < productNames.size(); i++) {
				ProductName productName = productNames.get(i);
				String codeString = productName.getProductId();
				//String codeString = productName.get("code").trim();
				if (codeString.equals("5")) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("code", productName.getProductId());
					map.put("name", productName.getProductName());
					map.put("pathID", productName.getProductPathid());
					accessoryArrayList.add(map);
				}
			}
		}
		
		getBrandType(0);	
			
		String pathIdString = (String)accessoryArrayList.get(0).get("pathID")+",";
		allPathIdString = pathIdString;
		allCountString = "10";
		requestAllProduct(allCountString, pathIdString);
		
		updateViewData();
//		new Thread(){  
//            public void run(){  
//            	getProductTypeDic();  
//            }  
//        }.start();
//        mSaveDialog = ProgressDialog.show(DiyDivsionActivity.this, "更新数据", "DIY专区数据正在更新中，请稍等...", true);
	}
	//activity恢复
	protected void onResume() {
		super.onResume();
		orderData();
	}
	//获取品牌类型
	protected void getBrandType(int accessoryNumber) {
		if (brandArrayList.size() > 0) {
			brandArrayList.removeAll(brandArrayList);
		}
		if (accessoryArrayList.size() > 0) {
			HashMap<String, Object> accessHashMap = accessoryArrayList.get(accessoryNumber);
			String pathIDString = (String)accessHashMap.get("pathID");
			String[] codeArrayStrings = pathIDString.split(",");
			String beforeCodeString = codeArrayStrings[codeArrayStrings.length-1];
			for (int i = 0; i < productNames.size(); i++) {
				ProductName productName = productNames.get(i);
				String codeString = productName.getProductId();
				if (codeString.equals(beforeCodeString)) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("code", productName.getProductId());
					map.put("name", productName.getProductName());
					map.put("pathID", productName.getProductPathid());
					brandArrayList.add(map);
				}
			}
		}
	}
	//获取所有产品
	protected void requestAllProduct(String countString,String pathIdString) {
		allPathIdString = pathIdString;
		if (accessoryListArrayList.size() > 0) {
			accessoryListArrayList.removeAll(accessoryListArrayList);
		}
		String nameString = "";
		for (int i = 0; i < accessoryArrayList.size(); i++) {
			String pathString = (String)accessoryArrayList.get(i).get("pathID")+",";
			if (pathIdString.equals(pathString)) {
				nameString = (String)accessoryArrayList.get(i).get("name");
			}
		}
		Group<ProductList> productLists = productListDao.readProductListByPathId("yy", countString, pathIdString);
		for (int j = 0; j < productLists.size(); j++) {
			if (accessoryListArrayList.size() > 0) {
				accessoryListArrayList.removeAll(accessoryListArrayList);
			}
			if (productLists.size() > 0) {
				for (int i = 0; i < productLists.size(); i++) {
					ProductList productList = productLists.get(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("code", productList.getProductId());
					map.put("type", nameString);
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
					accessoryListArrayList.add(map);
				}
			}
		}
	}
	//刷新数据
	protected void updateViewData() {
		setAccessoryGridView();
    	setAccessoryListView();
    	setAccessoryTypeView();
    	//setChooseListView();
    	orderData();
	}
	
	
	
	//从网络上获得产品的类型字典
	protected void getProductTypeDic() {
			try {
				String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
				HttpResponse aString = gosHttpOperation.invokerProductTypeList(dateString, "23132");
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
						productTypeString = reString;
						JSONArray responeArray = dataObject.getJSONArray("responseList");
						if (responeArray.size() > 0) {
							if (accessoryArrayList.size() > 0) {
								accessoryArrayList.removeAll(accessoryArrayList);
							}
							for (int i = 0; i < responeArray.size(); i++) {
								JSONObject oneObject = responeArray.getJSONObject(i);
								String codeString = oneObject.getString("code").trim();
								if (codeString.equals("5")) {
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("code", oneObject.getString("code").trim());
									map.put("name", oneObject.getString("name").trim());
									map.put("pathID", oneObject.getString("pathID").trim());
									accessoryArrayList.add(map);
								}
							}
							
							if (brandArrayList.size() > 0) {
								brandArrayList.removeAll(brandArrayList);
							}
							if (accessoryArrayList.size() > 0) {
								HashMap<String, Object> accessHashMap = accessoryArrayList.get(0);
								String pathIDString = (String)accessHashMap.get("pathID");
								String[] codeArrayStrings = pathIDString.split(",");
								String beforeCodeString = codeArrayStrings[codeArrayStrings.length-1];
								for (int i = 0; i < responeArray.size(); i++) {
									JSONObject oneObject = responeArray.getJSONObject(i);
									String codeString = oneObject.getString("code").trim();
									if (codeString.equals(beforeCodeString)) {
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("code", oneObject.getString("code").trim());
										map.put("name", oneObject.getString("name").trim());
										map.put("pathID", oneObject.getString("pathID").trim());
										brandArrayList.add(map);
									}
								}
							}
							if (brandArrayList.size() > 0) {
								HashMap<String, Object> accessHashMap = brandArrayList.get(0);
								String pathIDString = (String)accessHashMap.get("pathID");
								final String[] codeArrayStrings = pathIDString.split(",");
								new Thread()
								{
									public void run() {
										if (getProductInfoByNetWork("", codeArrayStrings[codeArrayStrings.length-2], "")) {
											// 发送消息，通知handler在主线程中更新UI 
								            connectHanlder.sendEmptyMessage(0);//codeArrayStrings[codeArrayStrings.length-1]
										}
									}
								}.start();
							}
							else {
								// 发送消息，通知handler在主线程中更新UI 
					            connectHanlder.sendEmptyMessage(0);
							}
						}
						else {
							// 发送消息，通知handler在主线程中更新UI 
				            connectHanlder.sendEmptyMessage(0);
						}
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
	//根据配件和品牌进行网络数据拉取
	protected Boolean getProductInfoByNetWork(String moduleCodeString, String inModuleClassCodeString, String lastClassCodeString) {
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			HttpResponse aString = gosHttpOperation.invokerProductShowList(dateString, 
					"1","10", "0", "5", inModuleClassCodeString, lastClassCodeString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				int inModuleInt = Integer.parseInt(inModuleClassCodeString);
				String typeString = "";
				for (int i = 0; i < accessoryArrayList.size(); i++) {
					String pathIDString = accessoryArrayList.get(i).get("pathID").toString().trim();
					String[] codeArrayStrings = pathIDString.split(",");
					String codeString = codeArrayStrings[codeArrayStrings.length - 1];
					int accessInt = Integer.parseInt(codeString);
					if (accessInt == inModuleInt) {
						Log.i(TAG, "equalspathid");
						typeString = accessoryArrayList.get(i).get("name").toString().trim();
					}
				}
				if (stateString.equals("1")) {
					JSONArray responeArray = dataObject.getJSONArray("responseList");
					if (accessoryListArrayList.size() > 0) {
						accessoryListArrayList.removeAll(accessoryListArrayList);
					}
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("code", oneObject.getString("code").trim());
							map.put("type", typeString);
							map.put("name", oneObject.getString("name").trim());
							map.put("picUrl", oneObject.getString("picUrl").trim());
							float priceFloat = Float.valueOf((String)oneObject.getString("price").trim()).floatValue();
							int priceInt = (int)priceFloat;
							//int priceInt = Integer.valueOf((String)oneObject.getString("price").trim());
							String priceString = String.valueOf(priceInt);
							map.put("price", priceString);
							map.put("premark", oneObject.getString("premark").trim());
							map.put("operatorName", oneObject.getString("operatorName").trim());
							map.put("isOn", oneObject.getString("isOn").trim());
							accessoryListArrayList.add(map);
						}
					}
					return true;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	 //主线程
    private Handler connectHanlder = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display view");  
            // 更新UI，显示图片  
            setAccessoryGridView();
        	setAccessoryListView();
        	setAccessoryTypeView();
        	//setChooseListView();
        	orderData();
        	mSaveDialog.dismiss();
//            if ((accessoryArrayList.size() > 0) && (brandArrayList.size() > 0) && (accessoryListArrayList.size() > 0)) {  
//            	setAccessoryGridView();
//            	setAccessoryListView();
//            	setAccessoryTypeView();
//            	setChooseListView();
//            }  
        }  
    };
	//设置配件界面显示
    private void setAccessoryGridView() {
    	mMyAdapter = new MyAdapter();
//		accessoryArrayList = accessoryInitData();
		GridView accessroyGridView = (GridView)findViewById(R.id.accessoryGridview);
		accessroyGridView.setAdapter(mMyAdapter);
		accessroyGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				accessoryItemClick(arg1);
			}
		});
	}
    //设置品牌类型界面显示
    private void setAccessoryTypeView() {
    	mMyBrandAdapter = new MyBrandAdapter();
//		brandArrayList = brandInitData();
		GridView brandGridView = (GridView)findViewById(R.id.brandGridview);
		brandGridView.setAdapter(mMyBrandAdapter);
		brandGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				brandItemClick(arg1);
			}
		});
	}
    //设置产品列表界面显示
    private void setAccessoryListView() {
    	mMyAccessoryListAdapter = new MyAccessoryListAdapter();
		//accessoryListArrayList = checkAccessoryListInitData();
    	accessoryListPullRefreshListView = (PullToRefreshListView)findViewById(R.id.accessoryListview);
    	accessoryListPullRefreshListView.setMode(Mode.PULL_FROM_END);//设置刷新模式
    	// Set a listener to be invoked when the list should be refreshed.
    	accessoryListPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});
    	// Add an end-of-list listener
    	accessoryListPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//Toast.makeText(DiyDivsionActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
    	
    	accessoryListPullRefreshListView.setAdapter(mMyAccessoryListAdapter);
	}
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
//			mListItems.addFirst("Added after refresh...");
//			mAdapter.notifyDataSetChanged();
			int allCount = Integer.parseInt(allCountString)+10;
			allCountString = String.valueOf(allCount);
			requestAllProduct( allCountString, allPathIdString);
			
			updateViewData();
			// Call onRefreshComplete when the list has been refreshed.
			accessoryListPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
    
    
    //设置选购单列表界面显示
	private void setChooseListView() {
		mMyChooseListAdapter = new MyChooseListAdapter();
		//chooseListArrayList = chooseInitData();
		ListView chooseListView = (ListView)findViewById(R.id.chooseListview);
		chooseListView.setAdapter(mMyChooseListAdapter);
		ImageButton okButton = (ImageButton)findViewById(R.id.chooseOrderOKButton);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				mSaveDialog = ProgressDialog.show(DiyDivsionActivity.this, "上传数据", "订单数据上传中，请稍等...", true);
//				new Thread()
//				{
//					public void run() {
//						uploadOrderInfo();
//					}
//				}.start();
				if (chooseListArrayList.size() > 0) {
					Intent chooseOrderIntent = new Intent(DiyDivsionActivity.this, ChooseOrderActivity.class);
					startActivity(chooseOrderIntent);
				}
				else {
					Toast.makeText(DiyDivsionActivity.this, "请先选购商品", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	//订单上传
	protected void uploadOrderInfo() {
//		try {
//			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
//			HttpResponse aString = gosHttpOperation.invokerReportOrderInfo(dateString, "1", "23132", "导购员小李", "销售员", "顾客赵先生", "13688888888", 
//					"3000", "订单注释", "2014-03-07 15:14:53.993", "2014-03-07 15:14:53.993", "1", "23132", "2332", "cpu AMD3322", "322", "1");
//			//int statusCode = aString.getStatusLine().getStatusCode();
//			HttpEntity entity = aString.getEntity();
//			InputStream is = entity.getContent();
//			String reString = GOSHelper.convertStreamToString(is);
//			if (reString == null || reString.equals("")) {
//				
//			}
//			else {
//				JSONObject jsonObject = JSON.parseObject(reString);
//				//String oneString = jsonObject.getString("data");
//				JSONObject dataObject = jsonObject.getJSONObject("data");
//				String stateString = dataObject.getString("state");
//				if (stateString.equals("1")) {
//					mSaveDialog.dismiss();
//					//Toast.makeText(getApplicationContext(), "上传订单成功", Toast.LENGTH_SHORT).show();
//					new Thread()
//					{
//						public void run() {
//							// 发送消息，通知handler在主线程中更新UI 
//							connectHanlderFive.sendEmptyMessage(0);
//						}
//					}.start();
//				}
//			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (BaseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
    //主线程
    private Handler connectHanlderFive = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display image");  
            // 更新UI，显示图片  
            if (toast == null || !toast.getView().isShown())
            {
                    toast = Toast.makeText(DiyDivsionActivity.this, "订单上传成功", Toast.LENGTH_SHORT);
                    toast.show();
            }  
        }  
    };
	//配件列表
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return accessoryArrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AccessoryViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.accessory_item, null);
				 holder = new AccessoryViewHolder();
				 //取到各个控件的对象
				 holder.imageView = (ImageView)convertView.findViewById(R.id.accessoryBgView);//背景色设置
				 holder.title = (TextView)convertView.findViewById(R.id.accessoryTextview);//文字描述
				 holder.itemTitle = (TextView)convertView.findViewById(R.id.ItemId);//itemID
				 convertView.setTag(holder);
			}
			else {
				holder = (AccessoryViewHolder)convertView.getTag();
			}
			//背景色设置
			holder.imageView.setBackgroundColor(Color.rgb(219, 83, 51));
			//文字描述
			holder.title.setText((String)accessoryArrayList.get(position).get("name"));
			//itemID
			HashMap<String, Object> accessHashMap = accessoryArrayList.get(position);
			String pathIDString = (String)accessHashMap.get("pathID");
			String[] codeArrayStrings = pathIDString.split(",");
			String beforeCodeString = codeArrayStrings[codeArrayStrings.length-1];
			holder.itemTitle.setText(beforeCodeString);
			return convertView;
		}
	}
	//配件控件类
	 /**存放控件*/
    public final class AccessoryViewHolder{
    	public ImageView    imageView;
        public TextView 	title;
        public TextView		itemTitle;
    }

	//配件列表的点击事件响应
	protected void accessoryItemClick(View view) {
		final String itemString = ((TextView)view.findViewById(R.id.ItemId)).getText().toString().trim();
		
		if (brandArrayList.size() > 0) {
			brandArrayList.removeAll(brandArrayList);
		}
		for (int i = 0; i < productNames.size(); i++) {
			ProductName productName = productNames.get(i);
			String codeString = productName.getProductId();
			
			if (codeString.equals(itemString)) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("code", productName.getProductId());
				map.put("name", productName.getProductName());
				map.put("pathID", productName.getProductPathid());
				brandArrayList.add(map);
			}
		}
		String pathIdString = ",5," + itemString +",";
		requestAllProduct("10",pathIdString);
		updateViewData();
		
		
//		if (productTypeString != null && !productTypeString.equals("")) {
//			JSONObject jsonObject = JSON.parseObject(productTypeString);
//			//String oneString = jsonObject.getString("data");
//			JSONObject dataObject = jsonObject.getJSONObject("data");
//			String stateString = dataObject.getString("state");
//			if (stateString.equals("1")) {
//				JSONArray responeArray = dataObject.getJSONArray("responseList");
//					if (brandArrayList.size() > 0) {
//						brandArrayList.removeAll(brandArrayList);
//					}
//					for (int i = 0; i < responeArray.size(); i++) {
//						JSONObject oneObject = responeArray.getJSONObject(i);
//						String codeString = oneObject.getString("code").trim();
//						if (codeString.equals(itemString)) {
//							HashMap<String, Object> map = new HashMap<String, Object>();
//							map.put("code", oneObject.getString("code").trim());
//							map.put("name", oneObject.getString("name").trim());
//							map.put("pathID", oneObject.getString("pathID").trim());
//							brandArrayList.add(map);
//						}
//					}
//				}
//				if (brandArrayList.size() > 0) {
//					HashMap<String, Object> mapOne = brandArrayList.get(0);
//					String pathIDString = (String)mapOne.get("pathID");
//					final String[] pathIDArrayStrings = pathIDString.split(",");
//					new Thread()
//					{
//						public void run() {
//							if (getProductInfoByNetWork("", pathIDArrayStrings[pathIDArrayStrings.length-2], "")) {
//								// 发送消息，通知handler在主线程中更新UI 
//								connectHanlderOne.sendEmptyMessage(0);//pathIDArrayStrings[pathIDArrayStrings.length-1]
//							}
//						}
//					}.start();
//				}
//				else {
//					new Thread()
//					{
//						public void run() {
//							if (getProductInfoByNetWork("5", itemString, "")) {
//								// 发送消息，通知handler在主线程中更新UI 
//								connectHanlderOne.sendEmptyMessage(0);
//							}
//						}
//					}.start();
//				}
//			}
	}
	//品牌列表
	private class MyBrandAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return brandArrayList.size();
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
			BrandViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.accessory_item, null);
				holder = new BrandViewHolder();
				//取到各个控件的对象
				 holder.imageView = (ImageView)convertView.findViewById(R.id.accessoryBgView);//背景色设置
				 holder.title = (TextView)convertView.findViewById(R.id.accessoryTextview);//文字描述
				 holder.itemTitle = (TextView)convertView.findViewById(R.id.ItemId);//itemID
				 convertView.setTag(holder);
			}
			else {
				holder = (BrandViewHolder)convertView.getTag();
			}
			//设置背景色
			holder.imageView.setBackgroundColor(Color.rgb(216, 39, 28));
			//设置说明
			holder.title.setText((String)brandArrayList.get(position).get("name"));
			//itemid设置为pathID
			holder.itemTitle.setText((String)brandArrayList.get(position).get("pathID"));
			return convertView;
		}
	}
	//品牌控件类
	/**存放控件*/
   public final class BrandViewHolder{
   	   public ImageView    	imageView;
       public TextView 		title;
       public TextView		itemTitle;
   }
	//品牌类型点击事件
	protected void brandItemClick(View view) {
		String itemString = ((TextView)view.findViewById(R.id.ItemId)).getText().toString().trim();
		requestAllProduct("10",itemString);
		updateViewData();
//		if (itemString != null && !itemString.equals("")) {
//			final String[] codeArrayStrings = itemString.split(",");
//			new Thread()
//			{
//				public void run() {
//					if (getProductInfoByNetWork("", codeArrayStrings[codeArrayStrings.length-2], codeArrayStrings[codeArrayStrings.length-1])) {
//						// 发送消息，通知handler在主线程中更新UI 
//						connectHanlderTwo.sendEmptyMessage(0);
//					}
//				}
//			}.start();
//		}
	}
	 //配件列表刷新主线程
    private Handler connectHanlderOne = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display view");  
            // 更新UI，显示图片  
            setAccessoryTypeView();
        	setAccessoryListView();
//            if ( brandArrayList.size() > 0 && accessoryListArrayList.size() > 0) {  
//            	setAccessoryTypeView();
//            	setAccessoryListView();
//            }  
        }  
    };
  //配件列表刷新主线程
    private Handler connectHanlderTwo = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display view");  
            // 更新UI，显示图片  
            setAccessoryListView();
//            if (accessoryListArrayList.size() > 0) {  
//            	setAccessoryListView();
//            }  
        }  
    };
	//配件列表Adapter
	private class MyAccessoryListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return accessoryListArrayList.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			AccessoryListViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.accessorylist_item, null);
				holder = new AccessoryListViewHolder();
				//取到各个控件的对象
				 holder.imageView = (ImageView)convertView.findViewById(R.id.accessoryListImageview);//配件列表项图片
				 holder.nameTitle = (TextView)convertView.findViewById(R.id.accessoryListNameTextview);//配件名称
				 holder.isOnTitle = (TextView)convertView.findViewById(R.id.accessoryListIsOnTextview);//配件状态
				 holder.priceTitle = (TextView)convertView.findViewById(R.id.accessoryListPriceTextview);//配件价格
				 holder.operatorNameTitle = (TextView)convertView.findViewById(R.id.accessoryListOperatorNameTextview);//配件操作系统
				 //holder.premarkTitle = (TextView)convertView.findViewById(R.id.accessoryListPremarkTextview);//配件备注
				 holder.accessoryCheckRelativeLayout = (RelativeLayout)convertView.findViewById(R.id.accessoryCheckRelativeLayout);
				 holder.accessoryImageButton = (ImageButton)convertView.findViewById(R.id.accessoryCheckButton);//选用按钮
				 convertView.setTag(holder);
			}
			else {
				holder = (AccessoryListViewHolder)convertView.getTag();
			}
			//配件列表项图片
			//holder.imageView.setImageResource(R.drawable.accessorylistitem);
			//imageLoader.displayImage((String)accessoryListArrayList.get(position).get("picUrl"), holder.imageView, options);
			
			String pictureUrlString = (String)accessoryListArrayList.get(position).get("picUrl");
			String[] pictureStringArray = pictureUrlString.split(",");
			String picUrlString = pictureStringArray[0];
//			String[] urlStrings = picUrlString.split("\\/");
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
			//配件名称
			holder.nameTitle.setText("名称:"+(String)accessoryListArrayList.get(position).get("name"));
			//配件状态
			String productStates = (String)accessoryListArrayList.get(position).get("isOn");
			if (productStates.equals("1")) {
				holder.isOnTitle.setText("状态:在售");
			}
			else {
				holder.isOnTitle.setText("状态:已下架");
			}
			//配件价格
			holder.priceTitle.setText("价格:"+(String)accessoryListArrayList.get(position).get("price")+"元");
			//配件操作系统
			holder.operatorNameTitle.setText("操作系统:"+(String)accessoryListArrayList.get(position).get("operatorName"));
			holder.operatorNameTitle.setVisibility(View.INVISIBLE);
			//配件备注
			//holder.premarkTitle.setText("备注:"+(String)accessoryListArrayList.get(position).get("premark"));
			//选用背景
			holder.accessoryCheckRelativeLayout.setTag(position);
			holder.accessoryCheckRelativeLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					checkbuttonClickAction(v);
				}
			});
			//选用按钮
			holder.accessoryImageButton.setTag(position);//为Button添加tag标示
			//选用Button的事件响应
			holder.accessoryImageButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					checkbuttonClickAction(v);
				}
			});
			//为每个item添加click事件
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					accessProductMainScene(position);
				}
			});
			return convertView;
		}
	}
	//配件列表控件类
	/**存放控件*/
   public final class AccessoryListViewHolder{
   	   public ImageView    		imageView;
       public TextView 			nameTitle;
       public TextView			isOnTitle;
       public TextView 			priceTitle;
       public TextView			operatorNameTitle;
       public TextView 			premarkTitle;
   	   public RelativeLayout	accessoryCheckRelativeLayout;//选择背景
       public ImageButton		accessoryImageButton;
   }
   //选用button事件响应
   private void checkbuttonClickAction (View v){
	   
	   int position =  (Integer)v.getTag();
	   if (chooseListArrayList.size()>0) {
		   int forFlag = 0;
		   for (int i = 0; i < chooseListArrayList.size(); i++) {
			   if ((chooseListArrayList.get(i).get("code").toString().trim()).equals(accessoryListArrayList.get(position).get("code").toString().trim())) {
				   int chooseNumber = Integer.parseInt((String)chooseListArrayList.get(i).get("number"));
				   chooseNumber++;
				   if (chooseNumber>1) {
					   v.setEnabled(true);
				   }
				   String chooseID = (String)chooseListArrayList.get(i).get("code");
				   if (chooseOrderDao.updateNumberById("yy", chooseID, String.valueOf(chooseNumber))) {
					    orderData();
						//Toast.makeText(DiyDivsionActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
						Log.v(TAG, "更新成功");
				   } 
				   else {
					   //Toast.makeText(DiyDivsionActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
					   Log.v(TAG, "更新失败");  
				   }
				   forFlag = 1;
				   break;
			   }
		   }
		   if (forFlag != 1) {
			   String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			   ChooseOrder chooseOrder = new ChooseOrder();
			   chooseOrder.setOrder_id((String)accessoryListArrayList.get(position).get("code"));
			   chooseOrder.setOrder_type((String)accessoryListArrayList.get(position).get("type"));
			   chooseOrder.setBusiness_name((String)accessoryListArrayList.get(position).get("name"));
			   chooseOrder.setBusiness_price((String)accessoryListArrayList.get(position).get("price"));
			   chooseOrder.setBusiness_number("1");
			   chooseOrder.setBusiness_date(dateString);
			   if (chooseOrderDao.insert("yy", chooseOrder)) {
				   Log.v(TAG, "插入chooseorderdao数据库成功");
				   orderData();
			   }
		   }
	   }
	   else {
		   String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
		   ChooseOrder chooseOrder = new ChooseOrder();
		   chooseOrder.setOrder_id((String)accessoryListArrayList.get(position).get("code"));
		   chooseOrder.setOrder_type((String)accessoryListArrayList.get(position).get("type"));
		   chooseOrder.setBusiness_name((String)accessoryListArrayList.get(position).get("name"));
		   chooseOrder.setBusiness_price((String)accessoryListArrayList.get(position).get("price"));
		   chooseOrder.setBusiness_number("1");
		   chooseOrder.setBusiness_date(dateString);
		   if (chooseOrderDao.insert("yy", chooseOrder)) {
			   Log.v(TAG, "插入chooseorderdao数据库成功");
			   orderData();
		   }
	   }
   }
	//进入diy专区产品主图界面
	private void accessProductMainScene (int positionInt) {
		Intent productIntent = new Intent(DiyDivsionActivity.this, ProductMainSceneActivity.class);
		productIntent.putExtra("accessProductMainType", "1");
		productIntent.putExtra("productCode", (String)accessoryListArrayList.get(positionInt).get("code"));
		productIntent.putExtra("productType", (String)accessoryListArrayList.get(positionInt).get("type"));
		productIntent.putExtra("productName", (String)accessoryListArrayList.get(positionInt).get("name"));
		productIntent.putExtra("productImageUrl", (String)accessoryListArrayList.get(positionInt).get("picUrl"));
		productIntent.putExtra("productPrice", (String)accessoryListArrayList.get(positionInt).get("price"));
		productIntent.putExtra("productPremark", (String)accessoryListArrayList.get(positionInt).get("premark"));
		productIntent.putExtra("productOperatorNme", (String)accessoryListArrayList.get(positionInt).get("operatorName"));
		productIntent.putExtra("productIsOn", (String)accessoryListArrayList.get(positionInt).get("isOn"));
		startActivity(productIntent);
	}
	//通过读取数据库得到订单数据
	private void orderData() {
		if (chooseListArrayList.size() > 0) {
			chooseListArrayList.removeAll(chooseListArrayList);
		}
		Group<ChooseOrder> orderGroup = chooseOrderDao.readChooseOrderList("yy");
		float totalPriceFloat = 0;
		for (int i = 0; i < orderGroup.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("code", orderGroup.get(i).getOrder_id());
			map.put("classify", orderGroup.get(i).getOrder_type());
			map.put("name", orderGroup.get(i).getBusiness_name());
			map.put("price", orderGroup.get(i).getBusiness_price());
			map.put("number", orderGroup.get(i).getBusiness_number());
			chooseListArrayList.add(map);
			totalPriceFloat = Float.parseFloat(orderGroup.get(i).getBusiness_price()) *
					Integer.parseInt(orderGroup.get(i).getBusiness_number()) + totalPriceFloat;
		}
		
		if (totalPriceFloat >= 0 && chooseListArrayList.size() > 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("code", "");
			map.put("classify", "");
			map.put("name", "总价");
			map.put("price", String.valueOf(totalPriceFloat));
			map.put("number", "");
			chooseListArrayList.add(map);
		}
		
		
		if (chooseListArrayList.size() >= 0) {
			new Thread()
			{
				public void run() {
					// 发送消息，通知handler在主线程中更新UI 
		            connectHanlderThree.sendEmptyMessage(0);
				}
			}.start();
		}
	}
	//选购订单刷新主线程
    private Handler connectHanlderThree = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display view");  
            // 更新UI，显示图片  
            setChooseListView();
        }  
    };
	
	//选购单列表Adapter
	private class MyChooseListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return chooseListArrayList.size();
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
			ChooseListViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.choose_item, null);
				holder = new ChooseListViewHolder();
				//取到各个控件的对象
				 holder.classifyTextView = (TextView)convertView.findViewById(R.id.chooseClassTextView);//类别
				 holder.nameTextView = (TextView)convertView.findViewById(R.id.chooseNameTextView);//商品名称
				 holder.priceButton = (Button)convertView.findViewById(R.id.choosePriceButton);//选择价格
//				 holder.priceTextView = (TextView)convertView.findViewById(R.id.choosePriceTextView);//价格
//				 holder.minusButton = (Button)convertView.findViewById(R.id.minusButton);//数量减号
				 //holder.numberTextView = (TextView)convertView.findViewById(R.id.chooseNumberTextView);//数量
//				 holder.numberEditText = (EditText)convertView.findViewById(R.id.chooseNumberEditText);//数量可编辑
				 holder.chooseNumberButton = (Button)convertView.findViewById(R.id.chooseNumberEditButton);
				 //holder.plusButton = (Button)convertView.findViewById(R.id.plusButton);//数量加号
				 holder.operImageButton = (ImageButton)convertView.findViewById(R.id.chooseDeleteImageButton);//删除订单中产品按钮
				 convertView.setTag(holder);
			}
			else {
				holder = (ChooseListViewHolder)convertView.getTag();
			}
			String nameString = (String)chooseListArrayList.get(position).get("name");
			if (nameString.equals("总价")) {
				holder.priceButton.setEnabled(false);
				holder.chooseNumberButton.setVisibility(View.INVISIBLE);
				holder.operImageButton.setVisibility(View.INVISIBLE);
			}
			
			//类别
			holder.classifyTextView.setText((String)chooseListArrayList.get(position).get("classify"));
			//商品名称
			holder.nameTextView.setText((String)chooseListArrayList.get(position).get("name"));
			if (!nameString.equals("总价")) {
				holder.nameTextView.setTag((String)chooseListArrayList.get(position).get("code"));
				holder.nameTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//Toast.makeText(DiyDivsionActivity.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
						accessProductMainView(v.getTag().toString());
					}
				});
			}
			//价格
//			holder.priceTextView.setText((String)chooseListArrayList.get(position).get("price"));
			holder.priceButton.setTag(position);
			holder.priceButton.setText((String)chooseListArrayList.get(position).get("price"));
			holder.priceButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					priceInputTitleDialog(v);
				}
			});
			
//			//减
//			holder.minusButton.setTag(position);
//			holder.minusButton.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					//minusButtonAction(v);
//					inputTitleDialog();
//				}
//			});
//			//数量
//			holder.numberTextView.setText((String)chooseListArrayList.get(position).get("number"));
//			//加号
//			holder.plusButton.setTag(position);
//			holder.plusButton.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					plusButtonAction(v);
//				}
//			});
			//holder.numberEditText.setText((String)chooseListArrayList.get(position).get("number"));
			holder.chooseNumberButton.setTag(position);
			holder.chooseNumberButton.setText((String)chooseListArrayList.get(position).get("number"));
			holder.chooseNumberButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					inputTitleDialog(v);
				}
			});
//			//按钮作用
//			int chooseNumber = Integer.parseInt((String)chooseListArrayList.get(position).get("number"));
//			   if (chooseNumber<=1) {
//				holder.minusButton.setEnabled(false);
//			   }
			//删除按钮
			holder.operImageButton.setTag(position);
			holder.operImageButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					deleteChooseBusiness(v);
				}
			});
			
			return convertView;
		}
	}
	//订单列表控件类
	/**存放控件*/
   public final class ChooseListViewHolder{
       public TextView 			classifyTextView;
       public TextView			nameTextView;
       public Button			priceButton;
//       public TextView 			priceTextView;
//       public Button			minusButton;
//       public TextView			numberTextView;
//       public Button			plusButton;
//       public EditText			numberEditText;
       public Button			chooseNumberButton;
       public ImageButton		operImageButton;
   }
   /**产品主界面*/
 //进入diy专区产品主图界面
 	private void accessProductMainView (String codeString) {
 		Group<ProductList> productLists = productListDao.readProductById("yy", codeString);
 		if (productLists.size() > 0) {
 			String nameString = "";
 			String classPathIdString = (String)productLists.get(0).getProductClassPath().trim();
 			for (int i = 0; i < accessoryArrayList.size(); i++) {
 				String pathString = (String)accessoryArrayList.get(i).get("pathID")+",";
 				if (classPathIdString.equals(pathString)) {
 					nameString = (String)accessoryArrayList.get(i).get("name");
 				}
 			}
 			Intent productIntent = new Intent(DiyDivsionActivity.this, ProductMainSceneActivity.class);
 	 		productIntent.putExtra("accessProductMainType", "1");
 	 		productIntent.putExtra("productCode", (String)productLists.get(0).getProductId());
 	 		productIntent.putExtra("productType", nameString);
 	 		productIntent.putExtra("productName", (String)productLists.get(0).getProductName());
 	 		productIntent.putExtra("productImageUrl", (String)productLists.get(0).getProductPicurl());
 	 		productIntent.putExtra("productPrice", (String)productLists.get(0).getProductPrice());
 	 		productIntent.putExtra("productPremark", (String)productLists.get(0).getProductPremark());
 	 		productIntent.putExtra("productOperatorNme", (String)productLists.get(0).getProductOperatorname());
 	 		productIntent.putExtra("productIsOn", (String)productLists.get(0).getProductIson());
 	 		startActivity(productIntent);
		}
 	}
   
   
   private void inputTitleDialog(View v) {
	   final int position = (Integer)v.getTag();
       final EditText inputServer = new EditText(this);
       inputServer.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
       inputServer.setFocusable(true);

       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle("修改购买数量").setIcon(R.drawable.ic_launcher).setView(inputServer).setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String inputName = inputServer.getText().toString();
				if (inputName.equals("") || inputName == null || inputName.equals("0")) {
					
				}
				else {
					int chooseNumInt = Integer.parseInt(inputName);
					String chooseID = (String)chooseListArrayList.get(position).get("code");
					   if (chooseOrderDao.updateNumberById("yy", chooseID, String.valueOf(chooseNumInt))) {
						    orderData();
							//Toast.makeText(DiyDivsionActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
							Log.v(TAG, "更新成功");
					   } 
					   else {
						   //Toast.makeText(DiyDivsionActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
						   Log.v(TAG, "更新失败");  
					   }
				}	
			}
       });
       builder.setPositiveButton("取消", null);
       builder.show();
   }
   //修改价格
   private void priceInputTitleDialog(View v) {
	   final int position = (Integer)v.getTag();
       final EditText inputServer = new EditText(this);
       inputServer.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
//       inputServer.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
//       inputServer.setInputType(EditorInfo.TYPE_NUMBER_FLAG_SIGNED);
       inputServer.setFocusable(true);

       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle("修改商品价格").setIcon(R.drawable.ic_launcher).setView(inputServer).setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String inputName = inputServer.getText().toString();
				if (inputName.equals("") || inputName == null) {
					
				}
				else {
					float chooseNumInt = Float.parseFloat(inputName);
					String chooseID = (String)chooseListArrayList.get(position).get("code");
					   if (chooseOrderDao.updatePriceById("yy", chooseID, String.valueOf(chooseNumInt))) {
						    orderData();
							//Toast.makeText(DiyDivsionActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
							Log.v(TAG, "更新成功");
					   } 
					   else {
						   //Toast.makeText(DiyDivsionActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
						   Log.v(TAG, "更新失败");  
					   }
				}
			}
       });
       builder.setPositiveButton("取消", null);
       builder.show();
   }
   
   //数量减号的实现
   private void minusButtonAction(View v) {
	   int position = (Integer)v.getTag();
	   int chooseNumber = Integer.parseInt((String)chooseListArrayList.get(position).get("number"));
	   if (chooseNumber<=1) {
		   v.setEnabled(false);
	   }
	   chooseNumber--;
	   String chooseID = (String)chooseListArrayList.get(position).get("code");
	   if (chooseOrderDao.updateNumberById("yy", chooseID, String.valueOf(chooseNumber))) {
		    orderData();
			//Toast.makeText(DiyDivsionActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
		    Log.v(TAG, "更新成功");
	   } 
	   else {
		   //Toast.makeText(DiyDivsionActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
		   Log.v(TAG, "更新失败");
	   }
   }
   //数量加号的实现
   private void plusButtonAction(View v) {
	   int position = (Integer)v.getTag();
	   int chooseNumber = Integer.parseInt((String)chooseListArrayList.get(position).get("number"));
	   chooseNumber++;
	   if (chooseNumber>1) {
		   v.setEnabled(true);
	   }
	   String chooseID = (String)chooseListArrayList.get(position).get("code");
	   if (chooseOrderDao.updateNumberById("yy", chooseID, String.valueOf(chooseNumber))) {
		    orderData();
			//Toast.makeText(DiyDivsionActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
			Log.v(TAG, "更新成功");
	   } 
	   else {
		   //Toast.makeText(DiyDivsionActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
		   Log.v(TAG, "更新失败");  
	   }
   }
   //删除订单中得产品事件
   private void deleteChooseBusiness(View v) {
	   int position = (Integer)v.getTag();
	    String chooseID = (String)chooseListArrayList.get(position).get("code");
		if (chooseOrderDao.deleteById("yy", chooseID)) {
			orderData();
			//Toast.makeText(DiyDivsionActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
			Log.v(TAG, "删除成功");
		}
		else {
			//Toast.makeText(DiyDivsionActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
			Log.v(TAG, "删除失败");
		}
   }
  
}
