package com.gos.yypad;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.yypad.Splash.MyViewPageClickListener;
import com.gos.yypad.database.PictureShowDao;
import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ShowPicture;
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.exception.ParseException;
import com.gos.yypad.helper.GOSHelper;
import com.gos.yypad.helper.ImageHelper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class SalesPromotionActivity extends BaseActivity {  
    private final static String TAG = "SalesPromotionActivity"; 
	private ViewPager mViewPager;	
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;

	private int pos = 0;
    private int maxPos = 0;
    private int currentPageScrollStatus;
    private Toast toast;
	private String	imageUrlArray[];//存储splash的图片链接
	private Bitmap 	mBitmap[]; 
	private ProgressDialog mSaveDialog = null;
	
	private PictureShowDao			pictureShowDao;//图片
	private ArrayList<Bitmap>		bitmapArrayList;//店面展示图片存储数组
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salespromotion);
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		topTitle.setText("促销活动");
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		pictureShowDao = new PictureShowDao(this);
		bitmapArrayList = new ArrayList<Bitmap>();
		Group<ShowPicture> pictureShowGroup = pictureShowDao.readPictureShowListByModuleCode("yy","1006");
		for (int i = 0; i < pictureShowGroup.size(); i++) {
			String pictureUrlString = pictureShowGroup.get(i).getPicUrl().toString().trim();
//			String[] urlStrings = pictureUrlString.split("\\/");
//			String filePathString = urlStrings[urlStrings.length-1];
//			String extPath = GOSHelper.getExternDir()+"/"+filePathString;
			
			String[] urlStrings = pictureUrlString.split("\\//");
			String[] oneUrlStrings = urlStrings[urlStrings.length-1].split("\\/");
			String twoUrlString = urlStrings[urlStrings.length-1].replace(oneUrlStrings[0], "");

			String extPath = GOSHelper.getExternDir()+twoUrlString;
			
			try {
				Bitmap bitmap = GOSHelper.getBitmapByFileName(extPath);
				bitmapArrayList.add(bitmap);
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
			}
		}
		//InitSplash();
		setOnPageImage();
	}
	
	private void InitSplash(){
		mSaveDialog = ProgressDialog.show(SalesPromotionActivity.this, "更新数据", "数据正在更新中，请稍等...", true); 
		new Thread(){  
            public void run(){  
            	getSplashImageUrl();  
            }  
        }.start();
	}
	
	//从网络上请求splash的图片链接
	protected void getSplashImageUrl() {
		try {
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:sss").format(new Date());
			HttpResponse aString = gosHttpOperation.invokerShowPictureList(dateString, "1", "1006");
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
					imageUrlArray = new String[3];
					JSONArray responeArray = dataObject.getJSONArray("responseList");
					for (int i = 0; i < responeArray.size(); i++) {
						JSONObject oneObject = responeArray.getJSONObject(i);
						imageUrlArray[i] = oneObject.getString("picUrl").trim();
					}
					new Thread(connectNet).start();
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
        	int sizeInt = imageUrlArray.length;
        	mBitmap = new Bitmap[sizeInt];
        	for (int i = 0; i < imageUrlArray.length; i++) {
        		String fileUrlString = imageUrlArray[i];
        		try {     
                    //以下是取得图片的两种方法  
                    //////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap 
        			ImageHelper imageHelper = new ImageHelper();
                    byte[] data = imageHelper.getImage(fileUrlString);  
                    if(data!=null){  
                        Bitmap newBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap  
                        mBitmap[i] = newBitmap;
                    }else{  
                        Toast.makeText(SalesPromotionActivity.this, "Image error!", Toast.LENGTH_SHORT).show();  
                    }   
                } catch (Exception e) {  
                    Toast.makeText(SalesPromotionActivity.this,"无法链接网络！", Toast.LENGTH_SHORT).show();  
                    e.printStackTrace();  
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
            if (mBitmap != null) {  
            	if (mBitmap.length == 3) {
            		mSaveDialog.dismiss();
            		setOnPageImage();
				}
            }  
        }  
    };
	
	//设置splash图片
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	protected void setOnPageImage() {
		mViewPager = (ViewPager)findViewById(R.id.salesViewpager);        
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
       
//        mPage0 = (ImageView)findViewById(R.id.salesPage0);
//        mPage1 = (ImageView)findViewById(R.id.salesPage1);
//        mPage2 = (ImageView)findViewById(R.id.salesPage2);

//        LayoutInflater mLi = LayoutInflater.from(this);
//        View view1 = mLi.inflate(R.layout.layout1, null);
//        View view2 = mLi.inflate(R.layout.layout2, null);
//        View view3 = mLi.inflate(R.layout.layout3, null);
        
//        int sdk = android.os.Build.VERSION.SDK_INT;
//        BitmapDrawable bdOne = new BitmapDrawable(mBitmap[0]);
//        BitmapDrawable bdTwo = new BitmapDrawable(mBitmap[1]);
//        BitmapDrawable bdThree = new BitmapDrawable(mBitmap[2]);
//        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//        	view1.setBackgroundDrawable(bdOne);
//            view2.setBackgroundDrawable(bdTwo);
//            view3.setBackgroundDrawable(bdThree);
//        } else {
//            view1.setBackground(bdOne);
//            view2.setBackground(bdTwo);
//            view3.setBackground(bdThree);
//        }
//        view1.setBackgroundResource(R.drawable.cuxiaoone);
//        view2.setBackgroundResource(R.drawable.cuxiaotwo);
//        view3.setBackgroundResource(R.drawable.cuxiaothree);

        final ArrayList<View> views = new ArrayList<View>();
//        views.add(view1);
//        views.add(view2);
//        views.add(view3);
        for (int i = 0; i < bitmapArrayList.size(); i++) {
			View oneView = new View(this);
			//oneView.setOnClickListener(new MyViewPageClickListener());
			//oneView.setBackground(new BitmapDrawable(bitmapArrayList.get(i)));
			int sdk = android.os.Build.VERSION.SDK_INT;
			 if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				 oneView.setBackgroundDrawable(new BitmapDrawable(bitmapArrayList.get(i)));
			 }
			 else {
				 oneView.setBackground(new BitmapDrawable(bitmapArrayList.get(i)));
			}
			views.add(oneView);
		}

        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mViewPager.setAdapter(mPagerAdapter);
	}
	
	 public class MyOnPageChangeListener implements OnPageChangeListener {
			@Override
			public void onPageSelected(int arg0) {
//				switch (arg0) {
//				case 0:				
//					mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//					mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage0.setVisibility(View.VISIBLE);
//					mPage1.setVisibility(View.VISIBLE);
//					mPage2.setVisibility(View.VISIBLE);
//					break;
//				case 1:
//					mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//					mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage0.setVisibility(View.VISIBLE);
//					mPage1.setVisibility(View.VISIBLE);
//					mPage2.setVisibility(View.VISIBLE);
//					break;
//				case 2:
//					mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//					mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage0.setVisibility(View.VISIBLE);
//					mPage1.setVisibility(View.VISIBLE);
//					mPage2.setVisibility(View.VISIBLE);
//					break;
//				}

                setCurrentPos(arg0);//设置当前页
                setMaxPage(2);//设置最大页
			}

			@Override
	        public void onPageScrollStateChanged(int state)
	        {
	                //记录page滑动状态，如果滑动了state就是1
	                currentPageScrollStatus = state;
	        }

	        @Override
	        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	        {
	                if (pos == 0)
	                {
	                        //如果offsetPixels是0页面也被滑动了，代表在第一页还要往左划
	                        if (positionOffsetPixels == 0 && currentPageScrollStatus == 1)
	                        {
	                                if (toast == null || !toast.getView().isShown())
	                                {
	                                        toast = Toast.makeText(SalesPromotionActivity.this, "已经是第一页了", Toast.LENGTH_SHORT);
	                                        toast.show();
	                                }
	                        }
	                }
	                else if (pos == maxPos)
	                {
	                        //已经在最后一页还想往右划
	                        if (positionOffsetPixels == 0 && currentPageScrollStatus == 1)
	                        {
	                                if (toast == null || !toast.getView().isShown())
	                                {
	                                        toast = Toast.makeText(SalesPromotionActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT);
	                                        toast.show();
	                                }
	                        }
	                }
	        }

	        public void setMaxPage(int position)
	        {
	                //设置最后一页的position值
	                maxPos = position;
	        }

	        public void setCurrentPos(int position)
	        {
	                //设置当前页的position值
	                pos = position;
	        }
	}
}
