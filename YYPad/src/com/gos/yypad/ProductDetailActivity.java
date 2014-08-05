package com.gos.yypad;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProductDetailActivity extends BaseActivity {
	
	private WebView productDetailWebView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productdetail);
		//标题
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		topTitle.setText("宝贝详情");
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		InitWebView(savedInstanceState);
		setBusinessDetail();
	}
	//宝贝详情设置
	@SuppressLint("SetJavaScriptEnabled")
	protected void setBusinessDetail() {
		String pictureUrlString = getIntent().getStringExtra("productImageUrl");
		String productRemarkString = getIntent().getStringExtra("productPremark");

		productRemarkString = "<style>img{width:200px;height:200px;} div{margin-left:80px;}</style>"+productRemarkString;
		productDetailWebView.loadDataWithBaseURL(null, productRemarkString, "text/html", "UTF-8", null);
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void InitWebView(Bundle savedInstanceState){
		productDetailWebView = (WebView) findViewById(R.id.productDetailWebView);
		productDetailWebView.restoreState(savedInstanceState);
//		mCustomViewContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
//		mWebChromeClient = new MyWebChromeClient();
//		newstext.setWebChromeClient(mWebChromeClient);
	    
		productDetailWebView.setWebChromeClient(new WebChromeClient(){
			
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
//				super.onPageFinished(view, url);
//				loading.setVisibility(View.GONE);
//				content.setVisibility(View.VISIBLE);
//				String c = CookieManager.getInstance().getCookie(url);
//				CookieSyncManager.getInstance().sync();
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
//				super.shouldOverrideUrlLoading(view, url);
//                if (url.startsWith("mailto:") || url.startsWith("geo:") ||url.startsWith("tel:")) {
//                	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                	startActivity(intent);
//                	return true;
//                }
//                view.loadUrl(url);
                return true;
			}
			
		});
		productDetailWebView.addJavascriptInterface(this, "Android");

	       
	    // Configure the webview
	    WebSettings s = productDetailWebView.getSettings();
	    s.setJavaScriptEnabled(true);    
	    // enable Web Storage: localStorage, sessionStorage
	    s.setDomStorageEnabled(true);
	}
}
