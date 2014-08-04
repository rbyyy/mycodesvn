package com.gov.nzjcy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;



public class OneNewsContentActivity extends BaseActivity {
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	//private ImageView top_more;
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**新闻显示内容页*/
	private	WebView			newsContentWebView;
	/**新闻类别id*/
	private String			newsCatagoryId;
	/**新闻id*/
	private String			newsIdString;
	/**新闻标题*/
	private String			newsTitleString;
	/**新闻内容*/
	private String			newsContentDescribe;
	/**新闻发布时间*/
	private String			newsDateString;
	/**顶端的LineLayout*/
	private LinearLayout	topContentLinearLayout;

	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onenewscontent);
		ShareSDK.initSDK(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		newsIdString = getIntent().getStringExtra("newsId");
		newsDateString = getIntent().getStringExtra("newsDate");
		newsTitleString = getIntent().getStringExtra("newsTitle");
		initView();
		new Thread()
		{
			public void run() {
				getNewsContentByNewsId();
			}
		}.start();
		
	}
	//初始化界面
	protected void initView() {
		topContentLinearLayout = (LinearLayout)findViewById(R.id.topNewsContentLinearLayout);
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText(newsTitleString);//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OneNewsContentActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);		
	}
	//获取新闻内容
	protected void getNewsContentByNewsId() {
		newsContentDescribe = "";
		try{  
        	 Document doc = Jsoup.connect(newsIdString).get();  
             Elements links = doc.select("font.p24");  //^=nzcms_show_news
             for (Element link : links) {
            	 if (newsDateString.equals("123")) {
            		 Elements linksTwo =doc.select("td[height=20]");
            		 for (Element linkTwo : linksTwo) {
						String dateTitleString = linkTwo.text().toString();
						String strarr[] = dateTitleString.split("发布时间：");
						String oneStrArr[] = strarr[1].split("\\‖");
						newsContentDescribe = newsContentDescribe + "<p align=\"center\" style='font-size:18px;font-weight:bold;line-height:45px;font-family:\"宋体\"'>" 
	            	             + link.text() + "</p>" +
	            	             "<p align=\"center\">"+ oneStrArr[0] +"</p>";
						Log.v("tag", oneStrArr[0]);
					}
				}
            	 else {
            		 newsContentDescribe = newsContentDescribe + "<p align=\"center\" style='font-size:18px;font-weight:bold;line-height:45px;font-family:\"宋体\"'>" 
            	             + link.text() + "</p>" +
            	             "<p align=\"center\">"+ newsDateString +"</p>";
				}
			}
            Elements newsPicElement = doc.select("img[onload=if(this.width>700){this.width=600}]");
            String imgSrcString = "";
            for (Element linkOnElement : newsPicElement) {
				imgSrcString = "<img src=\""+ linkOnElement.attr("src") +"\" style='width:340px;height:350px'" +">";
			}
            newsContentDescribe = newsContentDescribe + "<p>" + imgSrcString + "</p>";
            Element contentElement = doc.getElementById("z");
            newsContentDescribe = newsContentDescribe + "<p>" + contentElement.toString() + "</p>";
            handler.sendEmptyMessage(1);
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				showNewsContent();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	//新闻内容界面显示
	protected void showNewsContent() {
		newsContentWebView = (WebView)findViewById(R.id.oneNewsContentWebview);
		//禁用cache
		newsContentWebView.loadDataWithBaseURL("http://www.nzjcy.gov.cn/", 
				newsContentDescribe, "text/html", "utf-8", null);
		newsContentWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		newsContentWebView.clearCache(true);
		newsContentWebView.destroyDrawingCache();
	}
}
