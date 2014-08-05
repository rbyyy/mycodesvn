package com.gos.yypad;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.xml.sax.Parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.yypad.database.PictureShowDao;
import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ShowPicture;
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.exception.ParseException;
import com.gos.yypad.helper.GOSHelper;
import com.gos.yypad.helper.ImageHelper;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class FirmMienActivity extends BaseActivity {
	
	private final static String TAG = "FirmMienActivity";
	private ArrayList<String>	imageModuleCodeArrayList;
	private	ArrayList<String>   imageUrlArrayList;//图片列表
	private ArrayList<Object>   imageUrlArrArrayList;//
	private Bitmap 				mBitmap[];
	private ProgressDialog 		mSaveDialog = null;
	private ArrayList<Object> 	firmShowObjectItemList;//店面展示
	private ArrayList<Object> 	termObjectItemList;//团队风采
	private ShopShowAdapter 	mMyShopShowListAdapter;//店面展示图片列表数据适配器
	private TermShowAdapter 	mMyTermShowListAdapter;//团队展示图片列表数据适配器
	
	private PictureShowDao			pictureShowDao;//图片
	private ArrayList<Bitmap>		shopShowBitmapArrayList;//店面展示图片存储数组
	private ArrayList<Bitmap>		teamMienBitmapArrayList;//团队风采存储数组
	
	private	TextView				firmIntroTextView;//公司简介
	
	private ArrayList<String>		shopShowDescriptionArrayList;//商店展示
	private ArrayList<String>		teamMienDescriptionArrayList;//团队风采展示
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firmmien);
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		topTitle.setText("公司风采");
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		shopShowDescriptionArrayList = new ArrayList<String>();
		teamMienDescriptionArrayList = new ArrayList<String>();
		
		firmShowObjectItemList = new ArrayList<Object>();
		termObjectItemList = new ArrayList<Object>();
		imageUrlArrayList = new ArrayList<String>();
		imageModuleCodeArrayList = new ArrayList<String>();
//		imageModuleCodeArrayList.add("1007");
		pictureShowDao = new PictureShowDao(this);
		
		firmIntroTextView = (TextView)findViewById(R.id.firmIntroduce);
		firmIntroTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog firmIntroAlertDialog = new AlertDialog.Builder(FirmMienActivity.this).setTitle("公司简介") .setMessage("   飞度科技是专业从事互联网相关业务开发的公司，依托网页设计、网站制作、网站建设、网站策划为主体，为企业提供网络整合营销服务为一体的综合性网络公司。飞度科技致力于网站建设的全方位发展，立足于以WEB为核心，以WEB应用开发及服务为发展战略的经营理念，在网站建设、网上购物商城、直销会员管理系统、电子政务手机客户端开发等方面积累了自己独特的经验并取得了很好的成绩。郑州网站建设-飞度科技拥有一支集市场商务、创意策划和售后技术支持为一体的专业队伍。公司核心成员由原知名的一些网络公司技术人员组成，对于整合相关企业互联网营销业务有独到见解，更能替企业着想，我们以真诚的态度、求实的服务赢得了各级汽车、医药、化工、服装、旅游、上市集团公司等行业内的一大批具有雄厚实力的客户群体。").setPositiveButton("确定", null)
				 	.show();
			}
		});
		
		shopShowBitmapArrayList = new ArrayList<Bitmap>();
		Group<ShowPicture> pictureShowGroup = pictureShowDao.readPictureShowListByModuleCode("yy","1007");
		for (int i = 0; i < pictureShowGroup.size(); i++) {
			String pictureUrlString = pictureShowGroup.get(i).getPicUrl().toString().trim();
			shopShowDescriptionArrayList.add(pictureShowGroup.get(i).getPicDescribe().toString().trim());
			
//			String[] urlStrings = pictureUrlString.split("\\/");
//			String filePathString = urlStrings[urlStrings.length-1];
//			String extPath = GOSHelper.getExternDir()+"/"+filePathString;
			
			String[] urlStrings = pictureUrlString.split("\\//");
			String[] oneUrlStrings = urlStrings[urlStrings.length-1].split("\\/");
			String twoUrlString = urlStrings[urlStrings.length-1].replace(oneUrlStrings[0], "");

			String extPath = GOSHelper.getExternDir()+twoUrlString;
			
			try {
				Bitmap bitmap = GOSHelper.getBitmapByFileName(extPath);
				shopShowBitmapArrayList.add(bitmap);
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
			}
		}
		
//		imageModuleCodeArrayList.add("1008");
//		imageModuleCodeArrayList.add("1009");
//		imageModuleCodeArrayList.add("1010");
		teamMienBitmapArrayList = new ArrayList<Bitmap>();
		for (int j = 0; j < 3; j++) {
			int moduleCodeInt = 1008 + j;
			String moduleCodeString = String.valueOf(moduleCodeInt);
			Group<ShowPicture> pictureShowsGroup = pictureShowDao.readPictureShowListByModuleCode("yy", moduleCodeString);
			for (int i = 0; i < pictureShowsGroup.size(); i++) {
				String pictureUrlString = pictureShowsGroup.get(i).getPicUrl().toString().trim();
				teamMienDescriptionArrayList.add(pictureShowsGroup.get(i).getPicDescribe().toString().trim());
//				String[] urlStrings = pictureUrlString.split("\\/");
//				String filePathString = urlStrings[urlStrings.length-1];
//				String extPath = GOSHelper.getExternDir()+"/"+filePathString;
				
				String[] urlStrings = pictureUrlString.split("\\//");
				String[] oneUrlStrings = urlStrings[urlStrings.length-1].split("\\/");
				String twoUrlString = urlStrings[urlStrings.length-1].replace(oneUrlStrings[0], "");

				String extPath = GOSHelper.getExternDir()+twoUrlString;
				
				try {
					Bitmap bitmap = GOSHelper.getBitmapByFileName(extPath);
					teamMienBitmapArrayList.add(bitmap);
				} catch (Exception e) {
					// TODO: handle exception
					 e.printStackTrace();
				}
			}
		}
		
    	// 更新UI，显示图片
    	setShopShowList();
 		setTermmineListView();
		
//		imageUrlArrArrayList = new ArrayList<Object>();
//
//		mSaveDialog = ProgressDialog.show(FirmMienActivity.this, "更新数据", "公司风采数据正在更新中，请稍等...", true);
//		new Thread(){  
//            public void run(){  
//            	if (imageModuleCodeArrayList.size() > 0) {
//        			getMainPageImageUrl(imageModuleCodeArrayList.get(0));
//        		} 
//            }  
//        }.start();
	}
//	//弹出窗口
//	private void inputTitleDialog(String titleString) {
//	       final TextView firmIntroTextView = new TextView(this);
//
//	       AlertDialog.Builder builder = new AlertDialog.Builder(this);
//	       builder.setTitle("修改购买数量").setIcon(R.drawable.ic_launcher).setView(inputServer).setNegativeButton("确定", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					String inputName = inputServer.getText().toString();
//					if (inputName.equals("") || inputName == null || inputName.equals("0")) {
//						
//					}
//					else {
//						int chooseNumInt = Integer.parseInt(inputName);
//						String chooseID = (String)chooseListArrayList.get(position).get("code");
//						   if (chooseOrderDao.updateNumberById("yy", chooseID, String.valueOf(chooseNumInt))) {
//							    orderData();
//								//Toast.makeText(DiyDivsionActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
//								Log.v(TAG, "更新成功");
//						   } 
//						   else {
//							   //Toast.makeText(DiyDivsionActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
//							   Log.v(TAG, "更新失败");  
//						   }
//					}	
//				}
//	       });
//	       builder.setPositiveButton("取消", null);
//	       builder.show();
//	   }
	//从网络上请求数据
	protected void getMainPageImageUrl(String moduleCodeString){
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			HttpResponse aString = gosHttpOperation.invokerShowPictureList(dateString, "1", moduleCodeString);
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
//					if (imageUrlArrayList.size() > 0) {
//						imageUrlArrayList.removeAll(imageUrlArrayList);
//					}
					imageUrlArrayList = new ArrayList<String>();
					if (responeArray.size() > 0) {
						//ArrayList<String> imageUrlOneArrayList = new ArrayList<String>();
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							String imageUrlString = oneObject.getString("picUrl").trim();
							imageUrlArrayList.add(imageUrlString);
						}
						imageUrlArrArrayList.add(imageUrlArrayList);
						imageModuleCodeArrayList.remove(0);
						if (imageModuleCodeArrayList.size()>0) {
							getMainPageImageUrl(imageModuleCodeArrayList.get(0));
						}
						else {
							new Thread(connectNet).start();
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
	/* 
     * 连接网络 
     * 由于在4.0中不允许在主线程中访问网络，所以需要在子线程中访问 
     */  
    private Runnable connectNet = new Runnable(){  
        @Override  
        public void run() {  
        	int arrSizeInt = imageUrlArrArrayList.size();
        	for (int i = 0; i < arrSizeInt; i++) {
				ArrayList<String> imageArrayListOne = (ArrayList<String>)imageUrlArrArrayList.get(i);
				int sizeInt = imageArrayListOne.size();
	        	mBitmap = new Bitmap[sizeInt];
	        	for (int j = 0; j < sizeInt; j++) {
	        		String fileUrlString = imageArrayListOne.get(j);
	        		try {     
	                    //以下是取得图片的两种方法  
	                    //////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap 
	        			ImageHelper imageHelper = new ImageHelper();
	                    byte[] data = imageHelper.getImage(fileUrlString);  
	                    if(data!=null){  
	                        Bitmap newBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap  
	                        mBitmap[j] = newBitmap;
	                    }else{  
	                        Toast.makeText(FirmMienActivity.this, "Image error!", Toast.LENGTH_SHORT).show();  
	                    }   
	                } catch (Exception e) {  
	                    Toast.makeText(FirmMienActivity.this,"无法链接网络！", Toast.LENGTH_SHORT).show();  
	                    e.printStackTrace();  
	                }
	        		if (i==0) {
						 firmShowObjectItemList.add(mBitmap[j]);
					}
	        		if (i==1) {
	        			termObjectItemList.add(mBitmap[j]);
					}
	        		if (i==2) {
	        			termObjectItemList.add(mBitmap[j]);
					}
	        		if (i==3) {
	        			termObjectItemList.add(mBitmap[j]);
					}
				}
	        	
	        	 
	        }
        	// 发送消息，通知handler在主线程中更新UI  
            connectHanlder.sendEmptyMessage(0);  
            Log.d(TAG, "set image ..."); 
		}
    }; 
    
  //主线程
    private Handler connectHanlder = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
        	 Log.d(TAG, "display image");
        	// 更新UI，显示图片
        	setShopShowList();
     		setTermmineListView();
     		mSaveDialog.dismiss();
//        	switch (msg.what) {
//			case 0:
////				// 更新UI，显示图片  
////	            if (mBitmap != null) {  
////	            	if (mBitmap.length == 5) {
////	            		setShopShowList();
////	            		mSaveDialog.dismiss();
////					}
////	            }  
//				break;
//			case 1:
//				// 更新UI，显示图片  
//	            if (mBitmap != null) { 
//	            	setShopShowList();
//            		setTermmineListView();
//            		mSaveDialog.dismiss();
//	            }  
//				break;
//			default:
//				break;
//			}
        }  
    };
	//设置店面展示界面
    private void setShopShowList() {
		//对店面展示listview进行填充
    	mMyShopShowListAdapter = new ShopShowAdapter();
		ListView storeListView = (ListView)findViewById(R.id.storeShowList);
//		SimpleAdapter storeShowAdapter = new SimpleAdapter(this, 
//				InitData(),
//				R.layout.list_item,
//				new String[]{"ItemImageOne","ItemImageTwo","ItemImageThree","ItemImageFour"},
//				new int[]{R.id.showOne,R.id.showTwo,R.id.showThree,R.id.showFour});
		storeListView.setAdapter(mMyShopShowListAdapter);
	}
    //设置团队风采展示界面
    private void setTermmineListView() {
    	//对团队风采listview进行填充
    	mMyTermShowListAdapter = new TermShowAdapter();
		ListView termmineListView = (ListView)findViewById(R.id.termMienList);
//		SimpleAdapter termmineAdapter = new SimpleAdapter(this,
//				termInitData(),
//				R.layout.termmine_item,
//				new String[]{"ItemImage"},
//				new int[]{R.id.termImageview}
//				);
		termmineListView.setAdapter(mMyTermShowListAdapter);
	}
    //店面展示
    private class ShopShowAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			int pageCount = 0;
//			if (firmShowObjectItemList.size() > 0) {
//				if (firmShowObjectItemList.size() % 4 > 0) {
//					pageCount = firmShowObjectItemList.size()/4 + 1;
//				}
//				else {
//					pageCount = firmShowObjectItemList.size()/4;
//				}
//			}
			if (shopShowBitmapArrayList.size() > 0) {
				if (shopShowBitmapArrayList.size() % 4 > 0) {
					pageCount = shopShowBitmapArrayList.size()/4 + 1;
				}
				else {
					pageCount = shopShowBitmapArrayList.size()/4;
				}
			}
			
			return pageCount;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(getApplication()).inflate(R.layout.list_item, null);

			ImageView oneImageView = (ImageView)convertView.findViewById(R.id.showOne);
			BitmapDrawable bdOne = new BitmapDrawable((Bitmap)shopShowBitmapArrayList.get(position*4));//0,4,8,12
			oneImageView.setBackgroundDrawable(bdOne);
			oneImageView.setTag(position*4);
			oneImageView.setOnClickListener(new MyOnClickListener());
			
			ImageView twoImageView = (ImageView)convertView.findViewById(R.id.showTwo);
			BitmapDrawable bdTwo = new BitmapDrawable((Bitmap)shopShowBitmapArrayList.get(position*4 + 1));//1,5,9
			twoImageView.setBackgroundDrawable(bdTwo);
			twoImageView.setTag(position*4+1);
			twoImageView.setOnClickListener(new MyOnClickListener());
			
			ImageView threeImageView = (ImageView)convertView.findViewById(R.id.showThree);
			BitmapDrawable bdThree = new BitmapDrawable((Bitmap)shopShowBitmapArrayList.get(position*4 + 2));//2,6,10
			threeImageView.setBackgroundDrawable(bdThree);
			threeImageView.setTag(position*4+2);
			threeImageView.setOnClickListener(new MyOnClickListener());
			
			ImageView fourImageView = (ImageView)convertView.findViewById(R.id.showFour);
			BitmapDrawable bdFour = new BitmapDrawable((Bitmap)shopShowBitmapArrayList.get(position*4 + 3));//3,7,11
			fourImageView.setBackgroundDrawable(bdFour);
			fourImageView.setTag(position*4+3);
			fourImageView.setOnClickListener(new MyOnClickListener());
			
//			ImageView twoImageView = (ImageView)convertView.findViewById(R.id.showTwo);
//			ImageView threeImageView = (ImageView)convertView.findViewById(R.id.showThree);
			return convertView;
		}
		
	}
    //点击事件
  	class MyOnClickListener implements OnClickListener{
  		public void onClick(View v)
  		{
  			int position = Integer.parseInt(v.getTag().toString());
  			Intent termMienIntent = new Intent(FirmMienActivity.this,TermMienActivity.class);
			termMienIntent.putExtra("titles", "店面展示");
			termMienIntent.putExtra("description", shopShowDescriptionArrayList.get(position).toString());
			FirmMienActivity.this.startActivity(termMienIntent);
  		}
    }
    
    //团队风采
    private class TermShowAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return teamMienBitmapArrayList.size();
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
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(getApplication()).inflate(R.layout.termmine_item, null);
			ImageView oneImageView = (ImageView)convertView.findViewById(R.id.termImageview);
			BitmapDrawable bdOne = new BitmapDrawable((Bitmap)teamMienBitmapArrayList.get(position));
			oneImageView.setBackgroundDrawable(bdOne);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent termMienIntent = new Intent(FirmMienActivity.this,TermMienActivity.class);
					termMienIntent.putExtra("titles", "团队风采");
					termMienIntent.putExtra("description", teamMienDescriptionArrayList.get(position).toString());
					FirmMienActivity.this.startActivity(termMienIntent);
				}
			});
			return convertView;
		}
		
	}
}
