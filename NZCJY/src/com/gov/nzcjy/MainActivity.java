package com.gov.nzcjy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	private final static String TAG = "MainActivity";
    private long exitTime=0;
    private WebView webView;
    private boolean IsInWeb=false;
    //
    FrameLayout bootLayout;
    
    //RelativeLayout logoLayout;
    //private ImageView bootLogo;
    Animation mTranslateAnimation;
    //
    public ProgressDialog pBar;
    private Handler handler = new Handler();

    private int newVerCode = 0;
    private String newVerName = "";
    @SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏actionBar
        //getActionBar().hide();
        
        bootLayout=(FrameLayout)findViewById(R.id.bootLayout);
        //bootLayout.setVisibility(View.INVISIBLE);
        //logoLayout=(RelativeLayout)findViewById(R.id.logoLayout);
        //启动LOGO尺寸
        /*bootLogo=(ImageView)findViewById(R.id.bootLogo);
        ViewGroup.LayoutParams para = bootLogo.getLayoutParams();
        para.width = para.height = this.getWindowManager().getDefaultDisplay().getWidth()/2;
        bootLogo.setLayoutParams(para);*/
        /*RelativeLayout.LayoutParams mp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        mp.topMargin =this.getWindowManager().getDefaultDisplay().getHeight()/20;
        logoLayout.setLayoutParams(mp);*/

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
        //
        webView=(WebView)findViewById(R.id.webView);
        //禁用cache
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.destroyDrawingCache();
        //允许JS
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //响应JS
        class DemoJavaScriptInterface111 {
            //是否在Web，用来决定返回键的处理办法
            @JavascriptInterface
            public void Set_IsInWeb(boolean v_) {
                IsInWeb=v_;
                Log.d("JavascriptInterface",new Boolean(v_).toString());
            }
            //web载入完毕
            @JavascriptInterface
            public void LoadOK() {
                Log.d("JavascriptInterface","LoadOK(");
                Log.d("JavascriptInterface","AutoUpdateVersion)");
                AutoUpdateVersion();//放到下边的动画后边的话，就不会执行，好奇怪
                //bootLayout.startAnimation(mTranslateAnimation);
                //bootLayout.setVisibility(View.INVISIBLE);
                new Thread()
				{
					public void run() {
						// 发送消息，通知handler在主线程中更新UI 
						connectHanlderFive.sendEmptyMessage(0);
					}
				}.start();
                Log.d("JavascriptInterface","LoadOK)");
            }
        }
        webView.addJavascriptInterface(new DemoJavaScriptInterface111(), "Machine");
        //alert
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,JsResult result) {//Required functionality here
                //return super.onJsAlert(view, url, message, result);
                if (message.length() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("").setMessage(message).show();
                    result.cancel();
                    return true;
                }
                return false;

            }
        });
        webView.setWebViewClient(new WebViewClient(){
        	 @Override  
             public boolean shouldOverrideUrlLoading(WebView view, String url) {  
                 //设置点击网页里面的链接还是在当前的webview里跳转  
                 view.loadUrl(url);    
                 return true;    
             }   
        });
       
        webView.loadUrl("http://m.nzjcy.gov.cn:81/#app");
        //shouldOverrideUrlLoading(webView, "http://m.nzjcy.gov.cn:81/#app");
        //4.0以后不允许主线程new HttpGet()

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
//                .detectDiskWrites().detectNetwork().penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
    }
    
    
  //主线程
    private Handler connectHanlderFive = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display image");  
            // 更新UI，显示图片  
            bootLayout.startAnimation(mTranslateAnimation);
        }  
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Log.d("onKey IsInWeb", new Boolean(IsInWeb).toString());
            Log.v("onKey IsInWeb ver", new Boolean(IsInWeb).toString());
            if (IsInWeb) {
                webView.loadUrl("javascript:GoBack()");
                return false;
            }else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    MainActivity.this.finish();
                    System.exit(0);
                }
                return true;
            }
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
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("vercode="+String.valueOf(vercode)+",newVerCode="+newVerCode).create();
            if (newVerCode > vercode) {
                doNewVersionUpdate();
            } else {

            }
        }
    }
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
