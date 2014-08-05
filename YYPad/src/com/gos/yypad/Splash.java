package com.gos.yypad;

import java.io.File;
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
import com.gos.yypad.database.PictureShowDao;
import com.gos.yypad.entity.Group;
import com.gos.yypad.entity.ShowPicture;
import com.gos.yypad.exception.BaseException;
import com.gos.yypad.exception.ParseException;
import com.gos.yypad.helper.GOSHelper;
import com.gos.yypad.helper.ImageHelper;
import com.gos.yypad.constants.Constants.Extra;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class Splash extends BaseActivity {
	private final static String TAG = "SplashActivity"; 
	private ViewPager mViewPager;	
//	private ImageView mPage0;
//	private ImageView mPage1;
//	private ImageView mPage2;
//	private ImageView mPage3;
//	private ImageView mPage4;
	//private int currIndex = 0;
	private int pos = 0;
    private int maxPos = 0;
    private int currentPageScrollStatus;
    //private Toast toast;
	private String	imageUrlArray[];//存储splash的图片链接
	private Bitmap 	mBitmap[]; 
	private ProgressDialog mSaveDialog = null;
	
	private PictureShowDao			pictureShowDao;//图片
	private ArrayList<String>		imageUrlArrayList;//图片链接存储数组
	private ArrayList<Bitmap>		bitmapArrayList;//图片存储数组
	
	private int						currentItemInt;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		imageUrlArrayList = new ArrayList<String>();
		bitmapArrayList = new ArrayList<Bitmap>();
		pictureShowDao = new PictureShowDao(this);
		String currentItemString = getIntent().getStringExtra("itemCount");
		
		if (currentItemString == null || currentItemString.equals("0")) {
			currentItemInt = 0;
		}
		else {
			currentItemInt = Integer.parseInt(getIntent().getStringExtra("itemCount"));
			currentItemInt--;
		}
		
		Group<ShowPicture> pictureShowGroup = pictureShowDao.readPictureShowListByModuleCode("yy","1000");
		for (int i = 0; i < pictureShowGroup.size(); i++) {
			String pictureUrlString = pictureShowGroup.get(i).getPicUrl().toString().trim();
			String[] urlStrings = pictureUrlString.split("\\//");
			String[] oneUrlStrings = urlStrings[urlStrings.length-1].split("\\/");
			String twoUrlString = urlStrings[urlStrings.length-1].replace(oneUrlStrings[0], "");

			String extPath = GOSHelper.getExternDir()+twoUrlString;//+"/"+filePathString
//			String[] urlStrings = pictureUrlString.split("\\/");
//			String filePathString = urlStrings[urlStrings.length-1];
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
		
		if (mViewPager != null) {
			mViewPager.setCurrentItem(currentItemInt, true);
		}
		
	}
	
	private void InitSplash(){
		mSaveDialog = ProgressDialog.show(Splash.this, "更新数据", "数据正在更新中，请稍等...", true); 
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
			HttpResponse aString = gosHttpOperation.invokerShowPictureList(dateString, "1", "1000");
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
					imageUrlArray = new String[5];
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
                        Toast.makeText(Splash.this, "Image error!", Toast.LENGTH_SHORT).show();  
                    }   
                } catch (Exception e) {  
                    Toast.makeText(Splash.this,"无法链接网络！", Toast.LENGTH_SHORT).show();  
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
            	if (mBitmap.length == 5) {
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
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
//		String[] imageUrls = bundle.getStringArray(Extra.IMAGES);
//		int pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);
		
		mViewPager = (ViewPager)findViewById(R.id.present_viewpager);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
//        mViewPager.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(Splash.this, "test", Toast.LENGTH_SHORT).show();
//			}
//		});
        
//    	String fileFolder = GOSHelper.getExternDir();
//    	String filepathimg = fileFolder + "/" + "one.jpg";
//    	File f = new File(filepathimg);
//    	if (f.exists()) {
//    	     Bitmap bm = BitmapFactory.decodeFile(filepathimg);
//    	     View.setImageBitmap(bm);
//    	    } else {
//    	     
//    	    }
       
//        mPage0 = (ImageView)findViewById(R.id.page0);
//        mPage1 = (ImageView)findViewById(R.id.page1);
//        mPage2 = (ImageView)findViewById(R.id.page2);
//        mPage3 = (ImageView)findViewById(R.id.page3);
//        mPage4 = (ImageView)findViewById(R.id.page4);

//        LayoutInflater mLi = LayoutInflater.from(this);
//        View view1 = mLi.inflate(R.layout.splashone, null);
//        View view2 = mLi.inflate(R.layout.splashtwo, null);
//        View view3 = mLi.inflate(R.layout.splashthree, null);
//        View view4 = mLi.inflate(R.layout.splashfour, null);
//        View view5 = mLi.inflate(R.layout.splashfive, null);
        
//        String fileFolder = GOSHelper.getExternDir();
//    	String filepathimg = fileFolder + "/" + "one.jpg";
//    	File f = new File(filepathimg);
//    	if (f.exists()) {
//    	     Bitmap bm = BitmapFactory.decodeFile(filepathimg);
//    	     BitmapDrawable bdOne = new BitmapDrawable(bm);
//    	     view1.setBackgroundDrawable(bdOne);
//    	    } else {
//    	     
//    	    }
//        for (int i = 0; i < bitmapArrayList.size(); i++) {
//			
//		}
//        
//        int sdk = android.os.Build.VERSION.SDK_INT;
//        BitmapDrawable bdOne = new BitmapDrawable(bitmapArrayList.get(0));
//        BitmapDrawable bdTwo = new BitmapDrawable(bitmapArrayList.get(1));
//        BitmapDrawable bdThree = new BitmapDrawable(bitmapArrayList.get(2));
//        BitmapDrawable bdFour = new BitmapDrawable(bitmapArrayList.get(3));
//        BitmapDrawable bdFive = new BitmapDrawable(bitmapArrayList.get(4));
//        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//        	view1.setBackgroundDrawable(bdOne);
//            view2.setBackgroundDrawable(bdTwo);
//            view3.setBackgroundDrawable(bdThree);
//            view4.setBackgroundDrawable(bdFour);
//            view5.setBackgroundDrawable(bdFive);
//        } else {
//            view1.setBackground(bdOne);
//            view2.setBackground(bdTwo);
//            view3.setBackground(bdThree);
//            view4.setBackground(bdFour);
//            view5.setBackground(bdFive);
//        }
//        //view1.setBackgroundResource(R.drawable.cuxiaoone);
//        view1.setOnClickListener(new MyViewPageClickListener());
//        //view2.setBackgroundResource(R.drawable.cuxiaotwo);
//        view2.setOnClickListener(new MyViewPageClickListener());
//        //view3.setBackgroundResource(R.drawable.cuxiaothree);
//        view3.setOnClickListener(new MyViewPageClickListener());
//        //view4.setBackgroundResource(R.drawable.cuxiaofour);
//        view4.setOnClickListener(new MyViewPageClickListener());
//        //view5.setBackgroundResource(R.drawable.cuxiaofive);
//        view5.setOnClickListener(new MyViewPageClickListener());
        

        final ArrayList<View> views = new ArrayList<View>();
//        views.add(view1);
//        views.add(view2);
//        views.add(view3);
//        views.add(view4);
//        views.add(view5);
        for (int i = 0; i < bitmapArrayList.size(); i++) {
			View oneView = new View(this);
			oneView.setOnClickListener(new MyViewPageClickListener());
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
	
//	public Object instantiateItem(View container, int position) {
//	  View view = imageViews.get(position);
//	  view.setOnClickListener(new OnClickListener() {
//	   
//	   @Override
//	   public void onClick(View v) {
//	    // TODO Auto-generated method stub
//	    Log.e("xl", "xl:arrive here.");
//	   }
//	  });
//	  ViewPager viewPager = (ViewPager) container;
//	  viewPager.addView(view);
//	  return imageViews.get(position);
//	  
//	}
	
	
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
//					mPage3.setVisibility(View.VISIBLE);
//					mPage4.setVisibility(View.VISIBLE);
//					break;
//				case 1:
//					mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//					mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage0.setVisibility(View.VISIBLE);
//					mPage1.setVisibility(View.VISIBLE);
//					mPage2.setVisibility(View.VISIBLE);
//					mPage3.setVisibility(View.VISIBLE);
//					mPage4.setVisibility(View.VISIBLE);
//					break;
//				case 2:
//					mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//					mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage0.setVisibility(View.VISIBLE);
//					mPage1.setVisibility(View.VISIBLE);
//					mPage2.setVisibility(View.VISIBLE);
//					mPage3.setVisibility(View.VISIBLE);
//					mPage4.setVisibility(View.VISIBLE);
//					break;
//				case 3:
//					mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//					mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage4.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage0.setVisibility(View.VISIBLE);
//					mPage1.setVisibility(View.VISIBLE);
//					mPage2.setVisibility(View.VISIBLE);
//					mPage3.setVisibility(View.VISIBLE);
//					mPage4.setVisibility(View.VISIBLE);
//					break;
//				case 4:
//					mPage4.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
//					mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
//					mPage0.setVisibility(View.VISIBLE);
//					mPage1.setVisibility(View.VISIBLE);
//					mPage2.setVisibility(View.VISIBLE);
//					mPage3.setVisibility(View.VISIBLE);
//					mPage4.setVisibility(View.VISIBLE);
//					break;
//				}

                setCurrentPos(arg0);//设置当前页
                setMaxPage(4);//设置最大页
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
//	                        if (positionOffsetPixels == 0 && currentPageScrollStatus == 1)
//	                        {
//	                                if (toast == null || !toast.getView().isShown())
//	                                {
//	                                        toast = Toast.makeText(Splash.this, "Already the first page", Toast.LENGTH_SHORT);
//	                                        toast.show();
//	                                }
//	                        }
	                }
	                else if (pos == maxPos)
	                {
	                        //已经在最后一页还想往右划
	                        if (positionOffsetPixels == 0 && currentPageScrollStatus == 1)
	                        {
//	                                if (toast == null || !toast.getView().isShown())
//	                                {
//	                                        toast = Toast.makeText(Splash.this, "Already the last page", Toast.LENGTH_SHORT);
//	                                        toast.show();
//	                                }
	                        	startMainActivity();//进入首页 
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
	 
	    public void startMainActivity() {  
	      	Intent intent = new Intent();
			intent.setClass(Splash.this,MainActivity.class);
			intent.putExtra("totalPage", String.valueOf(bitmapArrayList.size()));
			Splash.this.startActivity(intent);
			Splash.this.finish();
	    }    
	    //点击事件
	    class MyViewPageClickListener implements OnClickListener{
	    	public void onClick(View v)
			{
				Intent mainIntent = new Intent(Splash.this, MainActivity.class);
				mainIntent.putExtra("totalPage", String.valueOf(bitmapArrayList.size()));
				Splash.this.startActivity(mainIntent);
				Splash.this.finish();
			}
	    }
	    
		//屏蔽back键
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}

}
