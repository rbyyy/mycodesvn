package com.law.fangyuan;

import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Lawyer extends Activity {
	private String url = "http://www.60886666.com/ask/wap";
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs);
		AApp.getInstance().add(this);
		InitWebView();
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null && bundle.containsKey("url")){
			web.loadUrl(bundle.getString("url"));
		}else{
			web.loadUrl(url);
		}
		((ImageView) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Lawyer.this.finish();
			}
			
		});
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void InitWebView(){
		web = (WebView) findViewById(R.id.bbs_web);
		WebSettings s = web.getSettings();
		s.setBlockNetworkImage(false);
		s.setJavaScriptEnabled(true);
		s.setJavaScriptCanOpenWindowsAutomatically(true);
		s.setDatabaseEnabled(true);
		s.setGeolocationEnabled(true);
	    s.setGeolocationDatabasePath(getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());
	    s.setDomStorageEnabled(true);
		s.setDefaultTextEncodingName("utf-8");
		web.setWebViewClient(new WebClient());
		web.setWebChromeClient(new GeoClient());
		SyncCookie();
	}
	
	private void SyncCookie(){
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		HashMap<String,String> cookies = AMenu.getCookies(Lawyer.this);
		if(cookies.containsKey("DedeUserID")){
			cookieManager.removeAllCookie();
			cookieManager.setAcceptCookie(true);
			Iterator<String> iter = cookies.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next().toString();
				cookieManager.setCookie("60886666.com", key+"="+cookies.get(key).toString()+"; domain=60886666.com");
			}
		}
	}
	
	class WebClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			super.shouldOverrideUrlLoading(view, url);
            if (url.startsWith("mailto:") || url.startsWith("geo:") ||url.startsWith("tel:")) {
            	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            	startActivity(intent);
            	return true;
            }
            view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			((Loading)Lawyer.this.findViewById(R.id.loading)).setVisibility(View.GONE);
			((WebView)Lawyer.this.findViewById(R.id.bbs_web)).setVisibility(View.VISIBLE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			web.loadDataWithBaseURL(url,"<h2>������?</h2><a href='"+url+"'>ˢ��</a>", "text/html", "utf-8", null);
		}
	}
	
	class GeoClient extends WebChromeClient{

		@SuppressLint("NewApi")
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			// TODO Auto-generated method stub
			String message = consoleMessage.message();
			int lineNumber = consoleMessage.lineNumber();
			String sourceID = consoleMessage.sourceId();
			String messageLevel = consoleMessage.message();
			Log.i("[WebView]", String.format("[%s] sourceID: %s lineNumber: %n message: %s",messageLevel, sourceID, lineNumber, message));
			return super.onConsoleMessage(consoleMessage);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				Callback callback) {
			// TODO Auto-generated method stub
			super.onGeolocationPermissionsShowPrompt(origin, callback);
			callback.invoke(origin, true, false);
		}
		
	}
}
