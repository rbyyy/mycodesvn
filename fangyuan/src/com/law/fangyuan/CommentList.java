package com.law.fangyuan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

public class CommentList extends Activity {
	
	private String id,classid,tbname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_list);
		AApp.getInstance().add(this);
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		classid = bundle.getString("classid");
		tbname = bundle.getString("tbname");
		InitWeb();
		InitEven();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void InitWeb(){
		final String url = "http://www.60886666.com/plus/feedback_ajax.php?android=1&dopost=getlist&aid="+id;
		final WebView web = (WebView) findViewById(R.id.clist);
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				super.shouldOverrideUrlLoading(view, url);
				web.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				((Loading)CommentList.this.findViewById(R.id.loading)).setVisibility(View.GONE);
				((WebView)CommentList.this.findViewById(R.id.clist)).setVisibility(View.VISIBLE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				web.loadDataWithBaseURL(url,"<h2>ÍøÂç³ö´í£¡</h2><a href='"+url+"'>Ë¢ÐÂ</a>", "text/html", "utf-8", null);
			}
		});
		web.loadUrl(url);
	}
	
	private void InitEven(){
		
		((ImageView) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CommentList.this.finish();
			}
			
		});
		
		((ImageView) findViewById(R.id.back)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((ImageView)arg0.findViewById(R.id.back)).setImageResource(R.drawable.press_back_bar);
				CommentList.this.finish();
			}
			
		});
		((Button) findViewById(R.id.add_comment)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle b = new Bundle();
				b.putString("classid", classid);
				b.putString("tbname", tbname);
				b.putString("id", id);
				intent.putExtras(b);
				intent.setClass(CommentList.this, Comment.class);
				startActivityForResult(intent, 10);
			}
			
		});
	}
	
}
