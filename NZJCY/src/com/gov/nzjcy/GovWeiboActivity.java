package com.gov.nzjcy;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GovWeiboActivity extends BaseActivity {
	
	private	String	TAG = "GovWeiboActivity";//log的标示
	private WebView	webView;//微博webview
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	//private ImageView top_more;
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_govweibo);
		initView();//初始化界面
		if (isNetworkConnected(this)) {
			setWebView();//加载web页面
		}
		else {
			Toast.makeText(GovWeiboActivity.this, "没有网络，请检测网络链接！", Toast.LENGTH_SHORT).show();
		}
	}
	/**判断网络链接*/
	public boolean isNetworkConnected(Context context) { 
		if (context != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null) { 
			return mNetworkInfo.isAvailable(); 
			} 
		} 
		return false; 
	}
	
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("官方微博");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GovWeiboActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		//右边登录按钮的点击事件
//		top_right_headImageButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}
	//加载webview页面
	protected void setWebView() {
		webView = (WebView)findViewById(R.id.govweiboWebView);
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
               // IsInWeb=v_;
                Log.d("JavascriptInterface",new Boolean(v_).toString());
            }
            //web载入完毕
            @JavascriptInterface
            public void LoadOK() {
                Log.d("JavascriptInterface","LoadOK(");
                Log.d("JavascriptInterface","AutoUpdateVersion)");

                new Thread()
				{

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(GovWeiboActivity.this);
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
       
		webView.loadUrl("http://m.3g.qq.com/wbread/copage/singleguest?id=nzhjcy&lp=0,0,5,0,6");
	}
	
}
