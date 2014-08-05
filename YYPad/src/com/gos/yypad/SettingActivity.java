package com.gos.yypad;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.xml.sax.Parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.yypad.database.ChooseOrderDao;
import com.gos.yypad.database.PictureShowDao;
import com.gos.yypad.database.ProductListDao;
import com.gos.yypad.database.ProductNameDao;
import com.gos.yypad.database.ServerAddressDao;
import com.gos.yypad.database.ShopListDao;
import com.gos.yypad.entity.ChooseOrder;
import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ProductList;
import com.gos.yypad.entity.ProductName;
import com.gos.yypad.entity.ServerAddress;
import com.gos.yypad.entity.ShopList;
import com.gos.yypad.entity.ShowPicture;
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.exception.ParseException;
import com.gos.yypad.helper.GOSHelper;
import com.gos.yypad.helper.ImageHelper;
import com.gos.yypad.httpoperation.GosHttpApplication;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class SettingActivity extends BaseActivity {
	private final static String TAG = "SettingActivity"; 
	private MyAdapter								mMyAdapter;//数据源
	private ArrayList<HashMap<String, Object>>		shopDomainArrayList;//店面分区
	private ProgressDialog 							mSaveDialog = null;	
	private EditText								serverAddressEditText;//服务器地址输入框
	private Button									saveButton;//保存按钮
	private Button									updateButton;//更新数据按钮
	private Button									updateImageButton;//更新图片按钮
	private	Button									cleanCacheButton;//清除缓存按钮
	private int										selectedRowInt;//当前选择的行
	
	private PictureShowDao							pictureShowDao;//图片展示
	private ShopListDao								shopListDao;//店面列表数据表
	private ServerAddressDao						serverAddressDao;//服务器地址
	private ProductNameDao							productNameDao;//产品名称
	private ProductListDao							productListDao;//产品列表
	
	private ArrayList<String>						moduleCodeArrayList;
	private String									accessString;//进入的类型
	private ArrayList<String> 						picUrlArrayList;//图片地址列表
	private ArrayList<HashMap<String, String>>		productListNumberArrayList;//产品列表
	

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		//标题
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		accessString = getIntent().getStringExtra("accessType");
		topTitle.setText("设置");
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		if (accessString.equals("0")) {
			leftNaviImageButton.setVisibility(View.INVISIBLE) ;
		}
		else {
			leftNaviImageButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SettingActivity.this.finish();
				}
			});
		}
//		
//		String extPath = GOSHelper.getExternDir()+"/"+"20140422215652.jpg";
//		ImageView imView = (ImageView)findViewById(R.id.testImageview);
//		File file = new File(extPath);
//		if (file.exists()) {
//			Bitmap bitmap = BitmapFactory.decodeFile(extPath);
//			//bitmapArrayList.add(bitmap);
//			imView.setImageBitmap(bitmap);
//		}
		
		moduleCodeArrayList = new ArrayList<String>();
		productListNumberArrayList = new ArrayList<HashMap<String,String>>();
		
		selectedRowInt = 0;//当前选择第一行
		shopListDao = new ShopListDao(this);//店面列表
		serverAddressDao = new ServerAddressDao(this);//服务器地址
		pictureShowDao = new PictureShowDao(this);//图片展示表
		productNameDao = new ProductNameDao(this);//产品名称
		productListDao = new ProductListDao(this);//产品列表
		
		serverAddressEditText = (EditText)findViewById(R.id.serverAddressEditText);
		String serverAddressString = GOSHelper.getSharePreStr(SettingActivity.this, GosHttpApplication.SERVER_ADDRESS_STRING);
		if (serverAddressString == null || serverAddressString.equals("")) {
			
		}
		else {
			serverAddressEditText.setText(serverAddressString);
		}
		
		saveButton = (Button)findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new MySetOnClickListener());
		updateButton = (Button)findViewById(R.id.updateDataButton);
		updateButton.setOnClickListener(new MySetOnClickListener());
		updateImageButton = (Button)findViewById(R.id.updateImagesButton);
		updateImageButton.setOnClickListener(new MySetOnClickListener());
		cleanCacheButton = (Button)findViewById(R.id.clearCacheButton);
		cleanCacheButton.setOnClickListener(new MySetOnClickListener());
		
		shopDomainArrayList = new ArrayList<HashMap<String,Object>>();
		mSaveDialog = ProgressDialog.show(SettingActivity.this, "更新数据", "数据正在更新中，请稍等...", true); 
		new Thread(){
			public void run() {
				getShopDomainUrl();
			}
		}.start();
	}
	//点击事件
	class MySetOnClickListener implements OnClickListener{
		public void onClick(View v)
		{
			switch(v.getId()) {
            
            case R.id.saveButton: 
            	saveServerAddress();
                break;
            case R.id.updateDataButton:  
            	updateDataAction();
                break;
            case R.id.updateImagesButton:
            	updateImageDataAction();
            	break;
            case R.id.clearCacheButton:   
            	
                break;
            default: break;
            }    
		}
	}
	//服务器地址数据保存
	protected void saveServerAddress() {
		String serverAddressString = serverAddressEditText.getText().toString().trim();
		if (serverAddressString != null && !serverAddressString.equals("")) {
//			ServerAddress serverAddress = new ServerAddress();
//			serverAddress.setServerAddressString(serverAddressString);
//			if (serverAddressDao.insert("yy", serverAddress)) {
//				Log.i(TAG, "插入shoplistdao数据库成功");
//			}
			GOSHelper.putSharePre(SettingActivity.this, GosHttpApplication.SERVER_ADDRESS_STRING, serverAddressString);
			Log.i(TAG,serverAddressString);
			Toast.makeText(SettingActivity.this, "服务器地址保存成功", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(SettingActivity.this, "服务器地址不能为空", Toast.LENGTH_SHORT).show();
		}
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
    private Handler connectHanlder = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
//        	ShopList shopList = new ShopList();
//			shopList.setShopcode((String)shopDomainArrayList.get(0).get("code"));
//			shopList.setShopname((String)shopDomainArrayList.get(0).get("name"));
//			shopList.setShopmanager((String)shopDomainArrayList.get(0).get("manager"));
//			shopList.setShopaddress((String)shopDomainArrayList.get(0).get("address"));
//			shopList.setShopmobile((String)shopDomainArrayList.get(0).get("mobile"));
//			shopList.setShopopen((String)shopDomainArrayList.get(0).get("open"));
//			shopList.setShopselected("1");
////			if (i == 0) {
////				shopList.setShopselected("1");
////			}
////			else {
////				shopList.setShopselected("0");
////			}
//		   if (shopListDao.insert("yy", shopList)) {
//			   Log.i(TAG, "插入shoplistdao数据库成功");
//		   }
            Log.d(TAG, "display image");  
            // 更新UI，显示图片  
            updateSettingUI();
            mSaveDialog.dismiss();
        }  
    };
    //更新界面
    protected void updateSettingUI() {
    	String shopCodeString = (String)shopDomainArrayList.get(0).get("code");
 	    GOSHelper.putSharePre(SettingActivity.this, GosHttpApplication.SHOP_CODE_STRING, shopCodeString);//存储shopcode
    	//门店展示列表
		mMyAdapter = new MyAdapter();
		ListView shopListView = (ListView)findViewById(R.id.firmListView);
		shopListView.setAdapter(mMyAdapter);
	}
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			SLViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.shoplist_item, null);
				 holder = new SLViewHolder();
				 //取到各个控件的对象
				 holder.imageView = (ImageView)convertView.findViewById(R.id.shopListBackgroundImageview);//背景色设置
				 holder.premarkText = (TextView)convertView.findViewById(R.id.shoplistName);//文字描述
				 //holder.truePriceText = (TextView)convertView.findViewById(R.id.completeTruePriceTextViewOne);//优惠价格
				 //holder.falsePriceText = (TextView)convertView.findViewById(R.id.completeFlasePriceTextViewOne);//未优惠的价格
				 //holder.itemTitle = (TextView)convertView.findViewById(R.id.CmItemId);//itemID
				 convertView.setTag(holder);
			}
			else {
				holder = (SLViewHolder)convertView.getTag();
			}
			//设置整机图片
			if (position == selectedRowInt) {
				holder.imageView.setBackgroundColor(Color.RED);
			}
			//设置整机说明
			holder.premarkText.setText((String)shopDomainArrayList.get(position).get("name"));
//			//设置优惠价格
//			holder.truePriceText.setText((String)completeMachineArrayList.get(position).get("price"));
//			//设置最高价格
//			holder.falsePriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
//			holder.falsePriceText.setText((String)completeMachineArrayList.get(position).get("price"));
//			//设置itemid
//			holder.itemTitle.setText((String)completeMachineArrayList.get(position).get("code"));
			//为每个item添加click事件
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clickListAction(position);
				}
			});
			return convertView;
		}
		
	}
	//整机专区类
	 /**存放控件*/
   public final class SLViewHolder{
   		public ImageView    imageView;
   		public TextView 	premarkText;
//   		public TextView		truePriceText;
//   		public TextView     falsePriceText;
//   		public TextView		itemTitle;
   }
   //点击事件实现
   protected void clickListAction(int positionInt) {
	   String shopCodeString = (String)shopDomainArrayList.get(positionInt).get("code");
	   GOSHelper.putSharePre(SettingActivity.this, GosHttpApplication.SHOP_CODE_STRING, shopCodeString);//存储shopcode
	   selectedRowInt = positionInt;
	   new Thread(){  
           public void run(){  
           	connectHanlder.sendEmptyMessage(0);  
           }  
       }.start();
   }
   //更新数据实现
   protected void updateDataAction() {
	   for (int i = 0; i < 11; i++) {
			int j = 1000;
			j += i;
			moduleCodeArrayList.add(String.valueOf(j));
		}
	   InitSplash();
   }
   //更新图片
   protected void updateImageDataAction() {
	   mSaveDialog = ProgressDialog.show(SettingActivity.this, "更新图片", "图片正在更新中，请稍等...", true);
	   new Thread(){
		   public void run() {
			   	 requestPicture();
		      }
	   }.start();
   }
   //进入主界面
   protected void accessToSplashView() {
	   if (accessString.equals("0")) {
		   Intent splashIntent = new Intent(SettingActivity.this, Splash.class);
		   SettingActivity.this.startActivity(splashIntent);
		   SettingActivity.this.finish();
	   }
   }
   
	private void InitSplash(){
		mSaveDialog = ProgressDialog.show(SettingActivity.this, "更新数据", "数据正在更新中，请稍等...", true); 
		getPictureFromUrl(moduleCodeArrayList.get(0).toString().trim());
//		cachePictureOne("http://59.188.87.54:8012/ClientBin/Images/vga/7378.jpg");
//		requestProductPicture();
	}
	//获取图片的异步网络请求
	private void getPictureFromUrl(final String moduleCode) {
		new Thread(){  
            public void run(){  
            	String shopcodeString = GOSHelper.getSharePreStr(SettingActivity.this, GosHttpApplication.SHOP_CODE_STRING);
            	getSplashImageUrl(shopcodeString, moduleCode);  
            }  
        }.start();
	}
	
	//从网络上请求splash的图片链接
	protected void getSplashImageUrl(String areaCodeString, String moduleCodeString) {
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			HttpResponse aString = gosHttpOperation.invokerShowPictureList(dateString, areaCodeString, moduleCodeString);
			//int statusCode = aString.getStatusLine().getStatusCode();
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
					moduleCodeArrayList.remove(0);
					JSONArray responeArray = dataObject.getJSONArray("responseList");
					if (pictureShowDao.deleteByModuleCode("yy", moduleCodeString)) {
						Log.v(TAG, moduleCodeString+"数据删除成功");
					}
					for (int i = 0; i < responeArray.size(); i++) {
						ShowPicture showPicture = new ShowPicture();
						JSONObject oneObject = responeArray.getJSONObject(i);
						showPicture.setAreaCode(oneObject.getString("areaCode").trim());
						showPicture.setPictureId(oneObject.getString("code").trim());
						showPicture.setModuleCode(moduleCodeString);
						showPicture.setModuleName(oneObject.getString("moduleName").trim());
						showPicture.setPicUrl(oneObject.getString("picUrl").trim());
						showPicture.setDate(oneObject.getString("date").trim());
						showPicture.setPicDescribe(oneObject.getString("describe").trim());
						if (pictureShowDao.insert("yy", showPicture)) {
							String moduleNameString = oneObject.getString("moduleName").trim();
							Log.v(TAG, moduleNameString+"数据插入成功"+i);
						}
					}
					//new Thread(connectNet).start();
					if (moduleCodeArrayList.size() == 0) {
//						mSaveDialog.dismiss();
//						requestPicture();
//						accessToSplashView();
						getProductTypeDic(areaCodeString);
					}
					else {
						getPictureFromUrl(moduleCodeArrayList.get(0).toString().trim());
					}
				}
				else {
					 new AlertDialog.Builder(this).setTitle("数据更新").setMessage("更新数据失败").setPositiveButton("确定", null).show();
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
	//从网络上获得产品的类型字典
	protected void getProductTypeDic(String areaCodeString) {
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			HttpResponse aString = gosHttpOperation.invokerProductTypeList(dateString, areaCodeString);
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
					if (responeArray.size() > 0) {
						if (productNameDao.delete("yy")) {
							Log.v(TAG, "产品字典数据删除成功");
						}
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							ProductName productNames = new ProductName();
							productNames.setProductId(oneObject.getString("code").trim());
							productNames.setProductName(oneObject.getString("name").trim());
							productNames.setProductPathid(oneObject.getString("pathID").trim());
							if (productNameDao.insert("yy", productNames)) {
								Log.v(TAG, "产品字典数据插入成功"+i);
							}
						}
						
						if(getProductCodeType()){
							//requestPicture();//请求图片下载从此处开始
							//mSaveDialog.dismiss();
							String extPathOne = GOSHelper.getExternDir();
							if (!extPathOne.equals("")) {
								dialogDismissHandler.sendEmptyMessage(1);
							}
							else {
								dialogDismissHandler.sendEmptyMessage(0);
							}
						}
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
	//隐藏Dialog
	Handler dialogDismissHandler = new Handler(){
		@Override  
        public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mSaveDialog.dismiss();
				Toast.makeText(SettingActivity.this, "创建文件夹失败", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				GOSHelper.putSharePre(SettingActivity.this, GosHttpApplication.FIRST_ACCESS_STRING, true);
				mSaveDialog.dismiss();
				accessToSplashView();
				break;	
			default:
				break;
			} 
		}
	};
	//得到各种类型
	protected Boolean getProductCodeType() {
		Group<ProductName> productNames = productNameDao.readProductNameList("yy");
		for (int i = 0; i < productNames.size(); i++) {
			String pathIdString = productNames.get(i).getProductPathid();
			String[] pathIdStringArray = pathIdString.split(",");
			if (pathIdStringArray.length == 2) {
				String allCountString = getAllCount(pathIdStringArray[pathIdStringArray.length - 1]);
				Log.v(TAG, "得到各种类型的数量"+allCountString);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("productCode", pathIdStringArray[pathIdStringArray.length - 1]);
				map.put("productAllCount", allCountString);
				productListNumberArrayList.add(map);
			}
		}
		int allCount = Integer.parseInt(productListNumberArrayList.get(0).get("productAllCount"));
		int pageNumber = 0;
		if (allCount%10 == 0) {
			pageNumber = allCount / 10;
		}
		else {
			pageNumber = allCount / 10;
		}
		String moduleCodeString = productListNumberArrayList.get(0).get("productCode");
		if (productListDao.delete("yy")) {
			Log.v(TAG, "productlistdap delete success");
		}
		for (int i = 0; i <= pageNumber; i++) {
			String pageNumberString = String.valueOf(i);
			if (getProductInfoByNetWork("10", pageNumberString)) {
				Log.v(TAG, moduleCodeString+"类型,第" + pageNumberString + "页下载完成");
			}
			else {
				Log.v(TAG, moduleCodeString+"类型,第" + pageNumberString + "页下载失败");
				if (i >= pageNumber) {
					return true;
				}
			}
		}
		return true;
	}
	//根据配件和品牌进行网络数据拉取
	protected Boolean getProductInfoByNetWork(String pageLarge, String pageNumber) {
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			String areaCodeString = GOSHelper.getSharePreStr(SettingActivity.this, GosHttpApplication.SHOP_CODE_STRING);
			HttpResponse aString = gosHttpOperation.invokerAllProductDataList(dateString, 
					areaCodeString,pageLarge, pageNumber);
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
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							ProductList	productLists = new ProductList();
							productLists.setProductId(oneObject.getString("code").trim());
							productLists.setProductName(oneObject.getString("name").trim());
							productLists.setProductPicurl(oneObject.getString("picUrl").trim());
							String priceString = (String)oneObject.getString("price").trim();
							if (!priceString.equals("")) {
								float priceFloat = Float.valueOf((String)oneObject.getString("price").trim()).floatValue();
								int priceInt = (int)priceFloat;
								String priceStringOne = String.valueOf(priceInt);
								productLists.setProductPrice(priceStringOne);
							}
							else {
								productLists.setProductPrice("0");
							}
							productLists.setProductOperatorname(oneObject.getString("operatorName").trim());
							productLists.setProductPremark(oneObject.getString("premark").trim());
							productLists.setProductIson(oneObject.getString("isOn").trim());
							productLists.setProductClassPath(oneObject.getString("classPath").trim());
							if (productListDao.insert("yy", productLists)) {
								Log.v(TAG, "productListDao insert into");
							}
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
	//得到各种diy，整机，精品的条目总数
	protected String getAllCount(String moduleCodeString) {
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			String areaCodeString = GOSHelper.getSharePreStr(SettingActivity.this, GosHttpApplication.SHOP_CODE_STRING);
			HttpResponse aString = gosHttpOperation.invokerAllProductDataList(dateString, 
					areaCodeString,"0", "0");
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
					String allCountString = dataObject.getString("allCount");
					return allCountString;
				}
				else {
					return "0";
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "0";
	}
	
	//请求展示图片
	protected void requestPicture() {
		//picUrlArrayList = new ArrayList<String>();
		Group<ShowPicture> pictureShowGroup = pictureShowDao.readPictureShowList("yy");
		for (int i = 0; i < pictureShowGroup.size(); i++) {
			//picUrlArrayList.add(pictureShowGroup.get(i).getPicUrl().toString().trim());
//			cachePicture(pictureShowGroup.get(i).getPicUrl().toString().trim());
//			Log.v(TAG, "下载图片成功");
			if (cachePicture(pictureShowGroup.get(i).getPicUrl().toString().trim())) {
				Log.v(TAG, "下载图片成功"+i);
				Log.v(TAG, "下载图片成功链接" + pictureShowGroup.get(i).getPicUrl().toString().trim());
			}
			else {
				Log.v(TAG, "下载图片失败"+i);
				Log.v(TAG, "下载图片失败链接" + pictureShowGroup.get(i).getPicUrl().toString().trim());
			}
		}
		requestProductPicture();
		//imageLoader.loadImageSync(pictureUrlString);
//		cachePicture(picUrlArrayList.get(0));
	}
	//缓存图片
	protected Boolean cachePicture(String pictureUrlString) {
		try {     
            //以下是取得图片的两种方法  
            //取得的是byte数组, 从byte数组生成bitmap 
			String[] urlStrings = pictureUrlString.split("\\//");
			String[] oneUrlStrings = urlStrings[urlStrings.length-1].split("\\/");
			String fileString = oneUrlStrings[oneUrlStrings.length - 1];
			String twoUrlString = urlStrings[urlStrings.length-1].replace(oneUrlStrings[0], "").replace("/" + fileString, "");
			
//			String oneFilePathString = urlStrings[urlStrings.length-2];
//			String filePathString = urlStrings[urlStrings.length-1];
			ImageHelper imageHelper = new ImageHelper();
            byte[] data = imageHelper.getImage(pictureUrlString);  
            if(data!=null){  
            	//String fileString = new String(data);
            	Bitmap newBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            	if (newBitmap == null) {
					Log.v(TAG, "newBitmap is null");
					Log.v(TAG, "下载图片失败");
	            	return false;
				}
            	else {
            		String[] filePathStrings = twoUrlString.split("\\/");
            		String extPathOne = GOSHelper.getExternDir();
            		String extPathString = extPathOne;
            		for (int i = 1; i < filePathStrings.length; i++) {
            			extPathString = extPathString + "/" + filePathStrings[i];
            			if (GOSHelper.getExternDir(extPathString).equals("")) {
							break;
						}
					}
            		String extPath = extPathString + "/" + fileString;
                	GOSHelper.storeImageInSD(extPath, newBitmap);
                	Log.v(TAG, "下载图片成功");
                	return true;
				}
            	
//            	picUrlArrayList.remove(0);
//            	if (picUrlArrayList.size() > 0) {
//            		cachePicture(picUrlArrayList.get(0));
//            		Log.v(TAG, "下载图片成功");
//				}
//            	else {
//            		Log.v(TAG, "下载图片完成");
//				}
//                //Bitmap newBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap  
//            	requestProductPicture();
            }else{  
                //Toast.makeText(SettingActivity.this, "Image error!", Toast.LENGTH_SHORT).show();  
                return false;
            }   
        } catch (Exception e) {  
            //Toast.makeText(SettingActivity.this,"无法链接网络！", Toast.LENGTH_SHORT).show();  
            e.printStackTrace();  
            return false;
        }
	}
	//请求产品图片
	//请求展示图片
	protected void requestProductPicture() {
		//picUrlArrayList = new ArrayList<String>();
		Group<ProductList> productListsGroup = productListDao.readAllProductList("yy");
		for (int i = 0; i < productListsGroup.size(); i++) {
			//picUrlArrayList.add(productListsGroup.get(i).getProductPicurl().toString().trim());
			String[] picUrlStringArray = productListsGroup.get(i).getProductPicurl().toString().trim().split(",");
			for (int j = 0; j < picUrlStringArray.length; j++) {
				if (cachePictureOne(picUrlStringArray[j])) {
					Log.v(TAG, "下载图片成功"+i);
					Log.v(TAG, "下载图片成功链接" + picUrlStringArray[j]);
				}
				else {
					Log.v(TAG, "下载图片失败"+i);
					Log.v(TAG, "下载图片失败链接" + picUrlStringArray[j]);
				}
			}
		}
		//imageLoader.loadImageSync(pictureUrlString);
		//GOSHelper.putSharePre(SettingActivity.this, GosHttpApplication.FIRST_ACCESS_STRING, true);
		mSaveDialog.dismiss();
		accessToSplashView();
	}
	//缓存图片
	protected Boolean cachePictureOne(String pictureUrlString) {
		try {     
            //以下是取得图片的两种方法  
            //取得的是byte数组, 从byte数组生成bitmap 
			String[] urlStrings = pictureUrlString.split("\\//");
			String[] oneUrlStrings = urlStrings[urlStrings.length-1].split("\\/");
			String fileString = oneUrlStrings[oneUrlStrings.length - 1];
			String twoUrlString = urlStrings[urlStrings.length-1].replace(oneUrlStrings[0], "").replace("/" + fileString, "");
			
//			String oneFilePathString = urlStrings[urlStrings.length-2];
//			String filePathString = urlStrings[urlStrings.length-1];
			ImageHelper imageHelper = new ImageHelper();
            byte[] data = imageHelper.getImage(pictureUrlString);  
            if(data!=null){  
            	//String fileString = new String(data);
            	Bitmap newBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            	if (newBitmap == null) {
					Log.v(TAG, "newBitmap is null");
					Log.v(TAG, "下载图片失败");
	            	return false;
				}
            	else {
            		String[] filePathStrings = twoUrlString.split("\\/");
            		String extPathOne = GOSHelper.getExternDir();
            		String extPathString = extPathOne;
            		for (int i = 1; i < filePathStrings.length; i++) {
            			extPathString = extPathString + "/" + filePathStrings[i];
            			if (GOSHelper.getExternDir(extPathString).equals("")) {
							break;
						}
					}
            		String extPath = extPathString + "/" + fileString;
                	GOSHelper.storeImageInSD(extPath, newBitmap);
                	Log.v(TAG, "下载图片成功");
                	return true;
				}
            	
            	//picUrlArrayList.remove(0);
//	            	if (picUrlArrayList.size() > 0) {
//	            		//cachePicture(picUrlArrayList.get(0));
//	            		Log.v(TAG, "下载图片成功");
//					}
//	            	else {
//	            		Log.v(TAG, "下载图片完成");
////	            		mSaveDialog.dismiss();
////	            		accessToSplashView();
//					}
                //Bitmap newBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap  
            }else{  
                //Toast.makeText(SettingActivity.this, "Image error!", Toast.LENGTH_SHORT).show();  
                return false;
            }   
        } catch (Exception e) {  
            //Toast.makeText(SettingActivity.this,"无法链接网络！", Toast.LENGTH_SHORT).show();  
            e.printStackTrace();  
            return false;
        }
//			imageLoader.loadImage(pictureUrlString, new SimpleImageLoadingListener(){
//				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//					Log.v(TAG, "图片缓存成功");
//					picUrlArrayList.remove(0);
//					if (picUrlArrayList.size()>0) {
//						cachePicture(picUrlArrayList.get(0));
//					}
//					else {
//						mSaveDialog.dismiss();
//						accessToSplashView();
//					}	
//				}
//			});
	}
	
}

