package com.law.fangyuan;


import org.json.JSONException;
import org.json.JSONObject;



import com.law.fangyuan.modify.NewMainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class VideoContent extends Activity {
	
	private Boolean check;
	private FileCache cache;
	private WebView newstext;
	private String id,classid,cache_name;
	private MyWebChromeClient mWebChromeClient;
	private View mCustomView;
	private FrameLayout	mCustomViewContainer;
	private Loading loading;
//	private WebView content;
	private WebChromeClient.CustomViewCallback 	mCustomViewCallback;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_content);
		
		AApp.getInstance().add(this);
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		classid = bundle.getString("classid");
		check = bundle.containsKey("check");
		cache = new FileCache(VideoContent.this);
		loading = (Loading)findViewById(R.id.loading);
//		content = (WebView)findViewById(R.id.newstext);
		newstext = (WebView) findViewById(R.id.newstext);
		loading.setVisibility(View.VISIBLE);
		newstext.setVisibility(View.GONE);
		InitWebView(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		((ImageView) findViewById(R.id.back)).setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				((ImageView)arg0.findViewById(R.id.back)).setImageResource(R.drawable.press_back_bar);
//				VideoContent.this.finish();
//			}
//			
//		});
		((TextView) findViewById(R.id.mcomm)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle b = new Bundle();
				b.putString("classid", classid);
				b.putString("id", id);
				intent.putExtras(b);
				intent.setClass(VideoContent.this, CommentList.class);
				startActivityForResult(intent, 10);
			}
			
		});
		((Button) findViewById(R.id.add_comment)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle b = new Bundle();
				b.putString("classid", classid);
				b.putString("id", id);
				intent.putExtras(b);
				intent.setClass(VideoContent.this, Comment.class);
				startActivityForResult(intent, 10);
			}
			
		});
		getData();
	}
	
	private void getData(){
		cache_name = "content/"+classid+"_"+id;
		String result = cache.get(cache_name,3600);
		new GetCommentCount().execute();
		if(result.equals("")){
			new getContent().execute();
		}else{
			InitContent(result);
		}
	}
	
	private void InitContent(String result) {
		TextView textview = (TextView) findViewById(R.id.title);
//		TextView newstime = (TextView) findViewById(R.id.newstime);
//		TextView username = (TextView) findViewById(R.id.username);
		try {
			JSONObject json = (JSONObject) new JSONObject(result).getJSONObject("r");
//			((TextView) findViewById(R.id.mcomm)).setText(json.getString("pl").toString());
			textview.setText(json.getString("title").toString());
//			newstime.setText(json.getString("newstime").toString());
//			username.setText("��Դ:"+json.getString("source").toString());
			String webtext = json.getString("newstext").toString();
			webtext = webtext.replace("\\\"", "\"").replace("\\'", "'");
			webtext = "<style>img{width:80%;}body{color:#2D2D2D;background:#F3F3F3;line-height:160%}p{text-indent:2em;}</style>"+webtext;
			newstext.loadDataWithBaseURL("http://www.60886666.com",webtext, "text/html", "utf-8", null);
			//newstext.loadUrl("http://player.youku.com/embed/XNTkyMzk4MzY4");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void InitWebView(Bundle savedInstanceState){
		newstext.restoreState(savedInstanceState);
		mCustomViewContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
		mWebChromeClient = new MyWebChromeClient();
		newstext.setWebChromeClient(mWebChromeClient);
	    
		newstext.setWebViewClient(new MyWebViewClient(){
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				loading.setVisibility(View.GONE);
				newstext.setVisibility(View.VISIBLE);
			}

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
			
		});
	       
	    // Configure the webview
	    WebSettings s = newstext.getSettings();
	    //s.setBuiltInZoomControls(true);
	    //s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//	    s.setUseWideViewPort(true);
//	    s.setLoadWithOverviewMode(true);
//	    s.setSavePassword(true);
//	    s.setSaveFormData(true);
	    s.setJavaScriptEnabled(true);    
	    // enable Web Storage: localStorage, sessionStorage
	    s.setDomStorageEnabled(true);
	}
	
    private class MyWebChromeClient extends WebChromeClient {
		private Bitmap 		mDefaultVideoPoster;
    	
    	@Override
		public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
		{
			//Log.i(LOGTAG, "here in on ShowCustomView");
    		//NewsContent.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    		newstext.setVisibility(View.GONE);	        
	        // if a view already exists then immediately terminate the new one
	        if (mCustomView != null) {
	            callback.onCustomViewHidden();
	            return;
	        }
	        mCustomViewContainer.addView(view);
	        mCustomView = view;
	        mCustomViewCallback = callback;
	        mCustomViewContainer.setVisibility(View.VISIBLE);	        
		}
		
		@Override
		public void onHideCustomView() {
			
			if (mCustomView == null)
				return;	       
			
			// Hide the custom view.
			mCustomView.setVisibility(View.GONE);
			
			// Remove the custom view from its container.
			mCustomViewContainer.removeView(mCustomView);
			mCustomView = null;
			mCustomViewContainer.setVisibility(View.GONE);
			mCustomViewCallback.onCustomViewHidden();
			
			newstext.setVisibility(View.VISIBLE);
			//NewsContent.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		@Override
		public Bitmap getDefaultVideoPoster() {
			//Log.i(LOGTAG, "here in on getDefaultVideoPoster");	
			if (mDefaultVideoPoster == null) {
				mDefaultVideoPoster = BitmapFactory.decodeResource(
						getResources(), R.drawable.default_video_poster);
		    }
			return mDefaultVideoPoster;
		}
		
    }
	
	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	// don't override URL so that stuff within iframe can work properly
	        // view.loadUrl(url);
	        return false;
	    }
	}

	class getContent extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result = "";
			Http http = new Http(VideoContent.this);
			result = http.GET("http://www.60886666.com/android/?m=getArticle&classid="+classid+"&id="+id+(check?"&check=1":""));
			if(!result.equals(""))cache.set(cache_name, result);
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			InitContent(result.toString());
		}		
	}
	
	class GetCommentCount extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result = "";
			Http http = new Http(VideoContent.this);
			result = http.GET("http://www.60886666.com/android/?m=getCommentCount&classid="+classid+"&id="+id);
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			((TextView)VideoContent.this.findViewById(R.id.mcomm)).setText(result.toString());
		}		
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCustomView != null) {
            	mWebChromeClient.onHideCustomView();
            	return true;
            }
            if(check){
				Intent news = new Intent();
				news.setClass(VideoContent.this,NewMainActivity.class);//Main
				VideoContent.this.startActivity(news);
				this.finish();
            }
    	}
    	return super.onKeyDown(keyCode, event);
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		newstext.saveState(outState);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		newstext.stopLoading();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		newstext.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		newstext.onResume();
	}
	
	
	
}
