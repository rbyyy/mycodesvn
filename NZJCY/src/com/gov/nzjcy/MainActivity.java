package com.gov.nzjcy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import com.gov.nzjcy.MainActivity;
import com.gov.nzjcy.NetworkTool;
import com.gov.nzjcy.ServiceConfig;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;
import com.gov.nzjcy.testutil.TestNetPort;
import com.gov.nzjcy.util.DensityUtil;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;


@SuppressLint("HandlerLeak")
public class MainActivity extends BaseActivity  {
	private final static String TAG = "MainActivity";
	private long exitTime=0;
	private FrameLayout 		bootLayout;
	private	Animation 			mTranslateAnimation;
	private ImageButton			oneImageButton;
	private ImageButton			twoImageButton;
	private ImageButton			threeImageButton;
	private ImageButton			fourImageButton;
	private ImageButton			fiveImageButton;
	private ImageButton			sixImageButton;
	private ImageButton			sevenImageButton;
	private ImageButton			eightImageButton;
	private ImageButton			nineImageButton;
	private ImageButton			tenImageButton;
	private ImageButton			elvenImageButton;
	private ImageButton			userloginImageButton;
	
	LinearLayout topLogoHeadImageLinearLayout;
	
	public ProgressDialog pBar;
    private Handler handler = new Handler();

    private static int newVerCode = 0;
    private String newVerName = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		int mScreenWidth = GOSHelper.getWindowsWidth(this);
		//int mScreenHeight = GOSHelper.getWindowsHeight(this);
		if (mScreenWidth == 480) {
			setContentView(R.layout.activity_mainone);
		}
		else {
			setContentView(R.layout.activity_main);
		}
		
		bootLayout = (FrameLayout)findViewById(R.id.bootLayout);
//		Class<?> c = null;
//        Object obj = null;
//        Field field = null;
//        int x = 0,sbar = 0;
//        try{
//                c = Class.forName("com.android.internal.R$dimen");
//                obj = c.newInstance();
//                field = c.getField("status_bar_height");
//                x = Integer.parseInt(field.get(obj).toString());
//                sbar = getResources().getDimensionPixelSize(x);
//        }catch(Exception e){
//                e.printStackTrace();
//        }
//		
//        int mStatesHeight = GOSHelper.getApplicationShowHeight(this);
//		
//		int itemWidth = mScreenWidth / 4;//每个item的宽度
//		int itemHeight = mScreenHeight / 2;//把屏幕分为两半
//	
//		
//		int pWidth = DensityUtil.px2dip(MainActivity.this, itemWidth);
//		int pHeight = DensityUtil.px2dip(MainActivity.this, itemHeight);
//		
//		topLogoHeadImageLinearLayout = (LinearLayout)findViewById(R.id.topLogoHeadImageLinearLayout);
//		topLogoHeadImageLinearLayout.setLayoutParams(new RelativeLayout.LayoutParams
//				(RelativeLayout.LayoutParams.MATCH_PARENT, 400));
		
		
		//FrameLayout oneFrameLayout = (FrameLayout)findViewById(R.id.oneFrameLayout);
		//oneFrameLayout.setLayoutParams(new TableRow.LayoutParams(pWidth, 160));

		
//		FrameLayout twoFrameLayout = (FrameLayout)findViewById(R.id.twoFrameLayout);
//		FrameLayout threeFrameLayout = (FrameLayout)findViewById(R.id.threeFrameLayout);
//		FrameLayout fourFrameLayout = (FrameLayout)findViewById(R.id.fourFrameLayout);
		
		
		setMenu();
		pushAnimation();
		InitLauncher();
		
	} 
	
	//页面内跳转
	private void InitLauncher(){
        new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				bootLayout.startAnimation(mTranslateAnimation);
				new Thread(){
					public void run() {
						AutoUpdateVersion();
					}
				}.start();
			}
        	
        }, 3000);
	}
	//跳转时的动画
	protected void pushAnimation() {
		mTranslateAnimation = new TranslateAnimation(0, -this.getWindowManager().getDefaultDisplay().getWidth(), 0,0);// 移动

        mTranslateAnimation.setDuration(500);
        // mAnimationSet.setFillAfter(true);
        // mTranslateAnimation.setFillAfter(true);
        mTranslateAnimation
                .setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        bootLayout.setVisibility(View.GONE);
                    }

                    public void onAnimationRepeat(Animation animation) {

                    }
                });
	}
	//菜单设计
	protected void setMenu() {
		oneImageButton = (ImageButton)findViewById(R.id.oneButton);
		oneImageButton.setOnClickListener(new MyOnClickListenter());
		
		twoImageButton = (ImageButton)findViewById(R.id.twoButton);
		twoImageButton.setOnClickListener(new MyOnClickListenter());
		
		threeImageButton = (ImageButton)findViewById(R.id.threeButton);
		threeImageButton.setOnClickListener(new MyOnClickListenter());
		
		fourImageButton = (ImageButton)findViewById(R.id.fourButton);
		fourImageButton.setOnClickListener(new MyOnClickListenter());
		
		fiveImageButton = (ImageButton)findViewById(R.id.fiveButton);
		fiveImageButton.setOnClickListener(new MyOnClickListenter());
		
		sixImageButton = (ImageButton)findViewById(R.id.sixButton);
		sixImageButton.setOnClickListener(new MyOnClickListenter());
		
		sevenImageButton = (ImageButton)findViewById(R.id.sevenButton);
		sevenImageButton.setOnClickListener(new MyOnClickListenter());
		
		eightImageButton = (ImageButton)findViewById(R.id.eightButton);
		eightImageButton.setOnClickListener(new MyOnClickListenter());
		
		nineImageButton = (ImageButton)findViewById(R.id.nineButton);
		nineImageButton.setOnClickListener(new MyOnClickListenter());
		
		tenImageButton = (ImageButton)findViewById(R.id.tenButton);
		tenImageButton.setOnClickListener(new MyOnClickListenter());
		
		elvenImageButton = (ImageButton)findViewById(R.id.elvenButton);
		elvenImageButton.setOnClickListener(new MyOnClickListenter());
		
		userloginImageButton = (ImageButton)findViewById(R.id.userloginImageButton);
		userloginImageButton.setOnClickListener(new MyOnClickListenter());
		
	}
	//菜单点击事件
	class MyOnClickListenter implements OnClickListener{
		public void onClick(View v)
		{
			switch(v.getId()) {
            
            case R.id.oneButton: 
            	Intent oneIntent = new Intent(MainActivity.this, ShowNewsActivity.class);
            	oneIntent.putExtra("accessType", "0");
				MainActivity.this.startActivity(oneIntent);
                break;
            case R.id.twoButton:  
            	Intent twoIntent = new Intent(MainActivity.this, GovWeiboActivity.class);
            	MainActivity.this.startActivity(twoIntent);
                break;
            case R.id.threeButton:   
            	Intent threeIntent = new Intent(MainActivity.this, ShowNewsActivity.class);
            	threeIntent.putExtra("accessType", "1");
				MainActivity.this.startActivity(threeIntent);
                break;
            case R.id.fourButton:  
            	Intent fourIntent = new Intent(MainActivity.this, AskForOpenActivity.class);//TestNetPort AskForOpenActivity
            	MainActivity.this.startActivity(fourIntent);
                break;
            case R.id.fiveButton:   
            	Intent fiveIntent = new Intent(MainActivity.this, OnlineBookActivity.class);
            	MainActivity.this.startActivity(fiveIntent);
                break;
            case R.id.sixButton:  
            	String userIdStringOne = GOSHelper.getSharePreStr(MainActivity.this, GosHttpApplication.USER_ID_STRING);
            	String userTypeString = GOSHelper.getSharePreStr(MainActivity.this, GosHttpApplication.USER_TYPE_STRING);
            	if (!userIdStringOne.equals("") && userTypeString.equals("5")) {
                	Intent sixIntent = new Intent(MainActivity.this, ContactPlatformOneActivity.class);
                	MainActivity.this.startActivity(sixIntent);
				}
            	else {
            		Intent sixIntent = new Intent(MainActivity.this, ContactPlatformActivity.class);
                	MainActivity.this.startActivity(sixIntent);
				}
            	
                break;
            case R.id.sevenButton:   
            	Intent sevenIntent = new Intent(MainActivity.this, InfoQueryActivity.class);
            	MainActivity.this.startActivity(sevenIntent);
                break;
            case R.id.eightButton:   
            	Intent eightIntent = new Intent(MainActivity.this, OnlineReportActivity.class);
            	MainActivity.this.startActivity(eightIntent);
                break;
            case R.id.nineButton:   
            	Intent nineIntent = new Intent(MainActivity.this, SueComplaintActivity.class);
            	MainActivity.this.startActivity(nineIntent);
                break;
            case R.id.tenButton:   
            	Intent tenIntent = new Intent(MainActivity.this, ChiefEmailActivity.class);
            	MainActivity.this.startActivity(tenIntent);
                break;
            case R.id.elvenButton:   
            	Intent elvenIntent = new Intent(MainActivity.this, MultimediaInfoActivity.class);
            	MainActivity.this.startActivity(elvenIntent);
                break;
            case R.id.userloginImageButton: 
            	String userIdString = GOSHelper.getSharePreStr(MainActivity.this, GosHttpApplication.USER_ID_STRING);
            	if (userIdString.equals("")) {
            		Intent userLoginIntent = new Intent(MainActivity.this, UserLoginActivity.class);
                	MainActivity.this.startActivity(userLoginIntent);
				}
            	else {
            		Intent userLoginResultIntent = new Intent(MainActivity.this, UserLoginResultActivity.class);
            		MainActivity.this.startActivity(userLoginResultIntent);
				}
                break;
            default: break;
            }    
		}
	}
	//按两次返回键退出
	 @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
        	if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	//自动升级
    void AutoUpdateVersion(){
        Log.d("JavascriptInterface","AutoUpdateVersion(");
        if (getServerVerCode()) {
            int vercode =ServiceConfig. getVerCode(this);
            Log.d("vercode=",String.valueOf(vercode));
            Log.d("newVerCode=",String.valueOf(newVerCode));
            alertMessageHandler.sendEmptyMessage(vercode);
        }
    }
    Handler alertMessageHandler = new Handler(){
    	@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
    		int vercode = msg.what;
    		new AlertDialog.Builder(MainActivity.this)
            .setTitle("vercode="+String.valueOf(vercode)+",newVerCode="+newVerCode).create();
		    if (newVerCode > vercode) {
		        doNewVersionUpdate();
		    } else {
		
		    }
			super.handleMessage(msg);
		}
    };
    
    private boolean getServerVerCode() {
        Log.d("getServerVerCode","");
        try {
            String url=ServiceConfig.UPDATE_SERVER+ ServiceConfig.UPDATE_VERJSON;
            String verjson = NetworkTool.getContent(url);
            JSONArray array = new JSONArray(verjson);
            Log.d("verjson",verjson);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                try {
                    newVerCode = Integer.parseInt(obj.getString("verCode"));
                    newVerName = obj.getString("verName");
                    Log.d("newVerCode=",String.valueOf(newVerCode));
                    Log.d("newVerName=",String.valueOf(newVerName));
                } catch (Exception e) {
                    newVerCode = -1;
                    newVerName = "";
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e("getServerVerCode", e.getMessage());
            return false;
        }
        return true;
    }

    private void doNewVersionUpdate() {
        int verCode =ServiceConfig. getVerCode(this);
        String verName =ServiceConfig. getVerName(this);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        sb.append(" Code:");
        sb.append(verCode);
        sb.append(", 发现新版本:");
        sb.append(newVerName);
        //sb.append(" Code:");
        //sb.append(newVerCode);
        sb.append(", 是否更新?");
        Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                        // 设置内容
                .setPositiveButton("更新",// 设置确定按钮
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                pBar = new ProgressDialog(MainActivity.this);
                                pBar.setTitle("正在下载");
                                pBar.setMessage("请稍候...");
                                pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                downFile(ServiceConfig.UPDATE_SERVER
                                        + ServiceConfig.UPDATE_APKNAME);
                            }

                        })
                .setNegativeButton("暂不更新",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 点击"取消"按钮之后退出程序
                                //finish();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }

    void downFile(final String url) {
        pBar.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {

                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                ServiceConfig.UPDATE_SAVENAME);
                        fileOutputStream = new FileOutputStream(file);

                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                            }
                        }

                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }

    void down() {
        handler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });

    }

    void update() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), ServiceConfig.UPDATE_SAVENAME)),
                "application/vnd.android.package-archive");
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());

        /*Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());*/
    }
	 
}
