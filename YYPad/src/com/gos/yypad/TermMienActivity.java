package com.gos.yypad;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

public class TermMienActivity extends BaseActivity {
	
	private WebView			descriptionWebView;
	private String			descriptionString;
	private String			titleString;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_termmien);
		//标题
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		titleString = getIntent().getStringExtra("titles");
		topTitle.setText(titleString);
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TermMienActivity.this.finish();
			}
		});
		
		
		descriptionString = getIntent().getStringExtra("description");
		descriptionWebView = (WebView)findViewById(R.id.descriptionWebView);
		descriptionWebView.loadDataWithBaseURL("http://yykj.zzfeidu.com/interface/getInterface.aspx", 
				descriptionString, "text/html", "utf-8", null);
		descriptionWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		descriptionWebView.clearCache(true);
		descriptionWebView.destroyDrawingCache();
	}
}
