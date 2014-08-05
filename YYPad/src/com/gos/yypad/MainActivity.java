package com.gos.yypad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class MainActivity extends BaseActivity implements OnTouchListener,OnGestureListener {
	private final static String TAG = "MainActivity";
	private ArrayList<String>	pageImageModuleCodeArrayList;
	private	ArrayList<String>   imageUrlArrayList;//图片列表
	private Bitmap 				mBitmap[];
	private ProgressDialog 		mSaveDialog = null;
	private static final int 	FLING_MIN_DISTANCE = 461;//移动最小距离
	private static final int 	FLING_MIN_VELOCITY = 200;//移动最大速度
	
	private PictureShowDao			pictureShowDao;//图片
	//private ArrayList<String>		imageUrlArrayList;//图片链接存储数组
	private ArrayList<Bitmap>		bitmapArrayList;//图片存储数组
	private String					totalPageString;//总页数
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//隐藏返回按钮
		ImageButton leftnavImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftnavImageButton.setVisibility(View.INVISIBLE) ;
		//标题
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		topTitle.setText("永源科技欢迎您");
		
		totalPageString = getIntent().getStringExtra("totalPage");
//		imageUrlArrayList = new ArrayList<String>();
//		pageImageModuleCodeArrayList = new ArrayList<String>();
//		pageImageModuleCodeArrayList.add("1001");
//		pageImageModuleCodeArrayList.add("1002");
//		pageImageModuleCodeArrayList.add("1003");
//		pageImageModuleCodeArrayList.add("1004");
//		pageImageModuleCodeArrayList.add("1005");
//
//		mSaveDialog = ProgressDialog.show(MainActivity.this, "更新数据", "首页数据正在更新中，请稍等...", true);
//		new Thread(){  
//            public void run(){  
//            	if (pageImageModuleCodeArrayList.size() > 0) {
//        			getMainPageImageUrl(pageImageModuleCodeArrayList.get(0));
//        		} 
//            }  
//        }.start();
		imageUrlArrayList = new ArrayList<String>();
		bitmapArrayList = new ArrayList<Bitmap>();
		pictureShowDao = new PictureShowDao(this);
		for (int j = 1; j < 6; j++) {
			int moduleCodeInt = 1000 + j;
			String moduleCodeString = String.valueOf(moduleCodeInt);
			Group<ShowPicture> pictureShowGroup = pictureShowDao.readPictureShowListByModuleCode("yy", moduleCodeString);
			for (int i = 0; i < pictureShowGroup.size(); i++) {
				String pictureUrlString = pictureShowGroup.get(i).getPicUrl().toString().trim();
//				String[] urlStrings = pictureUrlString.split("\\/");
//				String filePathString = urlStrings[urlStrings.length-1];
//				String extPath = GOSHelper.getExternDir()+"/"+filePathString;
				
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
		}
		
		
		
		TableLayout tableLayout = (TableLayout)findViewById(R.id.oneTable);
		//设置Touch监听  
		tableLayout.setOnTouchListener(this);  
        //允许长按  
		tableLayout.setLongClickable(true);
		
		
		setMainPageView();
	}
	//构建手势探测器  
    @SuppressWarnings("deprecation")
	GestureDetector mygesture = new GestureDetector(this); 
    @Override  
    public boolean onTouch(View v, MotionEvent event) {  
        return mygesture.onTouchEvent(event);  
    }
    
    /*用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN,  
     * 多个ACTION_MOVE, 1个ACTION_UP触发*/  
    //主要方法  
    @Override  
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
            float velocityY) {  
        // e1：第1个ACTION_DOWN MotionEvent   
        // e2：最后一个ACTION_MOVE MotionEvent   
        // velocityX：X轴上的移动速度（像素/秒）   
        // velocityY：Y轴上的移动速度（像素/秒）   
          
        // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒   
        //向右翻图片  
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE      
                     && Math.abs(velocityX) > FLING_MIN_VELOCITY) {      
	        	Intent splashIntent = new Intent(MainActivity.this, Splash.class);
	        	splashIntent.putExtra("itemCount", "0");
	        	MainActivity.this.startActivity(splashIntent);
	        	MainActivity.this.finish();
             }   
        //向左翻图片  
        if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE      
                     && Math.abs(velocityX) > FLING_MIN_VELOCITY) {      
        	Intent splashIntent = new Intent(MainActivity.this, Splash.class);
        	splashIntent.putExtra("itemCount", totalPageString);
        	MainActivity.this.startActivity(splashIntent);     	
        	MainActivity.this.finish();
        	MainActivity.this.overridePendingTransition(R.anim.close_enter, R.anim.close_exit);
        }      
           return false;      
    }  
    //////////////////////////////////////////////////////////////下面方法没用，但是这里必须实现  
    /* 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发*/  
    @Override  
    public void onLongPress(MotionEvent e) {}  
      
    /* 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发*/  
    @Override  
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {return false;}  
      
    /* 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发     
      注意和onDown()的区别，强调的是没有松开或者拖动的状态    */  
    @Override  
    public void onShowPress(MotionEvent e) {}  
      
    /*用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发*/  
    @Override  
    public boolean onSingleTapUp(MotionEvent e) {return false;}  
      
    @Override  
    public boolean onDown(MotionEvent e) {return false;} 

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
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							String imageUrlString = oneObject.getString("picUrl").trim();
							imageUrlArrayList.add(imageUrlString);
						}
						pageImageModuleCodeArrayList.remove(0);
						if (pageImageModuleCodeArrayList.size()>0) {
							getMainPageImageUrl(pageImageModuleCodeArrayList.get(0));
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
        	int sizeInt = imageUrlArrayList.size();
        	mBitmap = new Bitmap[sizeInt];
        	for (int i = 0; i < sizeInt; i++) {
        		String fileUrlString = imageUrlArrayList.get(i);
        		try {     
                    //以下是取得图片的两种方法  
                    //////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap 
        			ImageHelper imageHelper = new ImageHelper();
                    byte[] data = imageHelper.getImage(fileUrlString);  
                    if(data!=null){  
                        Bitmap newBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap  
                        mBitmap[i] = newBitmap;
                    }else{  
                        Toast.makeText(MainActivity.this, "Image error!", Toast.LENGTH_SHORT).show();  
                    }   
                } catch (Exception e) {  
                    Toast.makeText(MainActivity.this,"无法链接网络！", Toast.LENGTH_SHORT).show();  
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
            		setMainPageView();
            		mSaveDialog.dismiss();
				}
            }  
        }  
    };
	
	
	//设置主界面上得视图
	protected void setMainPageView() {
//		int sdk = android.os.Build.VERSION.SDK_INT;
//        BitmapDrawable bdOne = new BitmapDrawable(mBitmap[0]);
//        BitmapDrawable bdTwo = new BitmapDrawable(mBitmap[1]);
//        BitmapDrawable bdThree = new BitmapDrawable(mBitmap[2]);
//        BitmapDrawable bdFour = new BitmapDrawable(mBitmap[3]);
//        BitmapDrawable bdFive = new BitmapDrawable(mBitmap[4]);
        
      //公司风采
		ImageButton firmButton = (ImageButton)findViewById(R.id.firmButton);
		firmButton.setBackgroundDrawable(new BitmapDrawable(bitmapArrayList.get(0)));
		firmButton.setOnClickListener(new MyOnClickListener());
		//设置Touch监听  
		firmButton.setOnTouchListener(this);  
        //允许长按  
		firmButton.setLongClickable(true);
		//促销活动
		ImageButton salesButton = (ImageButton)findViewById(R.id.salesButton);
		salesButton.setBackgroundDrawable(new BitmapDrawable(bitmapArrayList.get(1)));
		salesButton.setOnClickListener(new MyOnClickListener());
		//设置Touch监听  
		salesButton.setOnTouchListener(this);  
        //允许长按  
		salesButton.setLongClickable(true);
		//diy专区
		ImageButton diyButton = (ImageButton)findViewById(R.id.diyButton);
		diyButton.setBackgroundDrawable(new BitmapDrawable(bitmapArrayList.get(2)));
		diyButton.setOnClickListener(new MyOnClickListener());
		//设置Touch监听  
		diyButton.setOnTouchListener(this);  
        //允许长按  
		diyButton.setLongClickable(true);
		//整机专区
		ImageButton completeButton = (ImageButton)findViewById(R.id.completeButton);
		completeButton.setBackgroundDrawable(new BitmapDrawable(bitmapArrayList.get(3)));
		completeButton.setOnClickListener(new MyOnClickListener());
		//设置Touch监听  
		completeButton.setOnTouchListener(this);  
        //允许长按  
		completeButton.setLongClickable(true);
		//精品展示
		ImageButton boutiqueButton = (ImageButton)findViewById(R.id.boutiqueButton);
		boutiqueButton.setBackgroundDrawable(new BitmapDrawable(bitmapArrayList.get(4)));
		boutiqueButton.setOnClickListener(new MyOnClickListener());
		//设置Touch监听  
		boutiqueButton.setOnTouchListener(this);  
        //允许长按  
		boutiqueButton.setLongClickable(true);
        
//		firmButton.setImageBitmap(mBitmap[0]);
//    	salesButton.setImageBitmap(mBitmap[1]);
//        diyButton.setImageBitmap(mBitmap[2]);
//        completeButton.setImageBitmap(mBitmap[3]);
//        boutiqueButton.setImageBitmap(mBitmap[4]);
	}
	//点击事件
	class MyOnClickListener implements OnClickListener{
		public void onClick(View v)
		{
			switch(v.getId()) {
            
            case R.id.firmButton: 
            	Intent firmIntent = new Intent(MainActivity.this, FirmMienActivity.class);
				startActivity(firmIntent); 
                break;
            case R.id.salesButton:  
            	Intent salesIntent = new Intent(MainActivity.this, SalesPromotionActivity.class);
				startActivity(salesIntent);
                break;
            case R.id.diyButton:   
            	Intent diyIntent = new Intent(MainActivity.this, DiyDivsionActivity.class);
				startActivity(diyIntent);
                break;
            case R.id.completeButton:  
            	Intent completeIntent = new Intent(MainActivity.this, CompleteMachineActivity.class);
				startActivity(completeIntent);
                break;
            case R.id.boutiqueButton:   
            	Intent boutiqueIntent = new Intent(MainActivity.this, BoutiQueshowActivity.class);
				startActivity(boutiqueIntent);
                break;
            default: break;
            }    
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
