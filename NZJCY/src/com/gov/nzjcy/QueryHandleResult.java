package com.gov.nzjcy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QueryHandleResult extends Activity {
	
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	//private ImageView top_more;
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_queryhandleresult);
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("查询结果");//标题
		
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QueryHandleResult.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		
		String webString = getIntent().getStringExtra("queryResult");
		WebView webView = (WebView)findViewById(R.id.queryHandleResultWebView);
		webView.loadDataWithBaseURL("http://m.nzjcy.gov.cn:81/", webString, "text/html", "utf-8", null);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.destroyDrawingCache();
	}
}
