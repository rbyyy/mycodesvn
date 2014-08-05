package com.law.fangyuan;


import java.util.ArrayList;
import java.util.HashMap;

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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PoliticsContent extends Activity {
	
	private Boolean check;
	private FileCache cache;
	private WebView newstext;
	private String id,classid,cache_name,title;
	private MyWebChromeClient mWebChromeClient;
	private View mCustomView;
	private FrameLayout	mCustomViewContainer;
	private Loading loading;
	private LinearLayout content;
	private WebChromeClient.CustomViewCallback 	mCustomViewCallback;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_content);
		
		AApp.getInstance().add(this);
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		classid = bundle.getString("classid");
		check = bundle.containsKey("check");
		cache = new FileCache(PoliticsContent.this);
		loading = (Loading)findViewById(R.id.loading);
		content = (LinearLayout)findViewById(R.id.content);
		loading.setVisibility(View.VISIBLE);
		content.setVisibility(View.GONE);
		InitWebView(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		((TextView) findViewById(R.id.mcomm)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle b = new Bundle();
				b.putString("classid", classid);
				b.putString("id", id);
				intent.putExtras(b);
				intent.setClass(PoliticsContent.this, CommentList.class);
				startActivityForResult(intent, 10);
			}
			
		});
		((Button) findViewById(R.id.add_comment)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//PoliticsContent.this.findViewById(R.id.pl_menu).setVisibility(View.GONE);
				PoliticsContent.this.findViewById(R.id.pl_menu_form).setVisibility(View.VISIBLE);
				new AddComment(PoliticsContent.this);
			}
			
		});
		((ImageView) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PoliticsContent.this.finish();
			}
			
		});
		((ImageView)findViewById(R.id.share_btn)).setVisibility(View.VISIBLE);
		((ImageView)findViewById(R.id.share_btn)).setOnClickListener(new OnClickListener(){

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int y = v.getBottom()+v.getHeight();// * 3 / 2;
				int x = getWindowManager().getDefaultDisplay().getWidth()-v.getWidth();// / 4;
				showMenu(x, y);

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
	
	private void showMenu(int x, int y){
		LinearLayout layout = (LinearLayout) LayoutInflater.from(PoliticsContent.this).inflate(
				R.layout.news_down_menu, null);
		ListView listView = (ListView) layout.findViewById(R.id.menu_list);
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("list_img", R.drawable.biz_news_newspage_share_icon);
		map.put("list_txt", "分享");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("list_img", R.drawable.biz_news_newspage_un_collect_icon);
		map.put("list_txt", "收藏");
		meumList.add(map);
		listView.setAdapter(new SimpleAdapter(this,   
	    		meumList, //数据源   
	    		R.layout.news_down_item, //xml实现   
	    		new String[]{"list_img","list_txt"}, //对应map的Key   
	    		new int[]{R.id.list_img,R.id.list_txt,}));
		final PopupWindow popupWindow = new PopupWindow(PoliticsContent.this);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//		AMenu.dip2px(context, dpValue)
		popupWindow.setWidth(AMenu.dip2px(PoliticsContent.this, 60));
		popupWindow.setHeight(AMenu.dip2px(PoliticsContent.this, 80));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// showAsDropDown会把里面的view作为参照物，所以要那满屏幕parent
		// popupWindow.showAsDropDown(findViewById(R.id.tv_title), x, 10);
		popupWindow.showAtLocation(findViewById(R.id.main), Gravity.LEFT
				| Gravity.TOP, x, y);//需要指定Gravity，默认情况是center.

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				popupWindow.dismiss();
				if(arg2==0){
					String msg = "我在法制河南手机客户端，发现了文章《"+title+"》，与你分享：手机访问http://www.60886666.com/android/?id="+id;
					new Share(PoliticsContent.this,msg).InitPopWin(arg1);
				}
			}
		});
	}
	
	private void InitContent(String result) {
		TextView textview = (TextView) findViewById(R.id.title);
		TextView newstime = (TextView) findViewById(R.id.newstime);
		TextView username = (TextView) findViewById(R.id.username);
		try {
			final JSONObject json = (JSONObject) new JSONObject(result).getJSONObject("r");
			title = json.getString("title").toString();
//			((TextView) findViewById(R.id.mcomm)).setText(json.getString("pl").toString());
			textview.setText(json.getString("title").toString());
			newstime.setText(json.getString("newstime").toString());
			username.setText("来源:"+json.getString("source").toString());
			String webtext = json.getString("newstext").toString();
			webtext = webtext.replace("\\\"", "\"").replace("\\'", "'");
			webtext = "<style>img{width:80%;}body{color:#2D2D2D;background:#F3F3F3;line-height:160%}p{text-indent:2em;}</style>"+webtext;
			newstext.loadDataWithBaseURL("http://www.60886666.com",webtext, "text/html", "utf-8", null);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void InitWebView(Bundle savedInstanceState){
		newstext = (WebView) findViewById(R.id.newstext);
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
				content.setVisibility(View.VISIBLE);
//				String c = CookieManager.getInstance().getCookie(url);
//				CookieSyncManager.getInstance().sync();
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
		newstext.addJavascriptInterface(this, "Android");

	       
	    // Configure the webview
	    WebSettings s = newstext.getSettings();
	    //s.setBuiltInZoomControls(true);
	    //s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
	    //s.setUseWideViewPort(true);
	    //s.setLoadWithOverviewMode(true);
	    //s.setSavePassword(true);
	    //s.setSaveFormData(true);
	    s.setJavaScriptEnabled(true);    
	    // enable Web Storage: localStorage, sessionStorage
	    s.setDomStorageEnabled(true);
	}
	
	@JavascriptInterface
	public void ToNews(String id, String classid){
		PoliticsContent.this.finish();
		Intent i = new Intent(PoliticsContent.this, PoliticsContent.class);
		Bundle b = new Bundle();
		b.putString("classid", classid);
		b.putString("id", id);
		b.putString("check", "check");
		i.putExtras(b);
		PoliticsContent.this.startActivity(i);
	}
	
    private class MyWebChromeClient extends WebChromeClient {
		private Bitmap 		mDefaultVideoPoster;
    	
    	@Override
		public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
		{
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
			Http http = new Http(PoliticsContent.this);
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
			Http http = new Http(PoliticsContent.this);
			result = http.GET("http://www.60886666.com/android/?m=getCommentCount&classid="+classid+"&id="+id);
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			((TextView)PoliticsContent.this.findViewById(R.id.mcomm)).setText(result.toString());
		}		
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		if(findViewById(R.id.pl_menu_form).isShown()){
				//findViewById(R.id.pl_menu).setVisibility(View.VISIBLE);
				findViewById(R.id.pl_menu_form).setVisibility(View.GONE);
				return true;
    		}
            if (mCustomView != null) {
            	mWebChromeClient.onHideCustomView();
            	return true;
            }
            if(check){
				Intent news = new Intent();
				news.setClass(PoliticsContent.this,NewMainActivity.class);//Main
				PoliticsContent.this.startActivity(news);
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
