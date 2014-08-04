package com.law.fangyuan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class BBS extends Activity {
	private String url = "http://www.60886666.com/bbs";
	private WebView web;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs);
		AApp.getInstance().add(this);
		web = (WebView) findViewById(R.id.bbs_web);
		web.getSettings().setBlockNetworkImage(false);
		web.setWebViewClient(new WebViewClient(){

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
				((Loading)BBS.this.findViewById(R.id.loading)).setVisibility(View.GONE);
				((WebView)BBS.this.findViewById(R.id.bbs_web)).setVisibility(View.VISIBLE);
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
	
}
