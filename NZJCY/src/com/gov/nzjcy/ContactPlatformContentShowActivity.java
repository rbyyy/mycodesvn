package com.gov.nzjcy;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactPlatformContentShowActivity extends BaseActivity {
	
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**webview*/
	private WebView			contactplatformContentWebview;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactplatformshow);
		initView();
		showNewsContent();
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		String titleString = getIntent().getStringExtra("title");
		topTitleTextView.setText(titleString);//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContactPlatformContentShowActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
	}
	//新闻内容界面显示
	protected void showNewsContent() {
		String webDescribeString = getIntent().getStringExtra("description");
		contactplatformContentWebview = (WebView)findViewById(R.id.contactplatformContentWebview);
		//禁用cache
		contactplatformContentWebview.loadDataWithBaseURL("http://www.nzjcy.gov.cn/", 
				webDescribeString, "text/html", "utf-8", null);
		contactplatformContentWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		contactplatformContentWebview.clearCache(true);
		contactplatformContentWebview.destroyDrawingCache();
	}
	
	
}
