package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import cn.sharesdk.douban.Douban;
import cn.sharesdk.dropbox.Dropbox;
import cn.sharesdk.evernote.Evernote;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.flickr.Flickr;
import cn.sharesdk.foursquare.FourSquare;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.google.GooglePlus;
import cn.sharesdk.instagram.Instagram;
import cn.sharesdk.kaixin.KaiXin;
import cn.sharesdk.linkedin.LinkedIn;
import cn.sharesdk.mingdao.Mingdao;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareAllGird;
import cn.sharesdk.pinterest.Pinterest;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sohu.suishenkan.SohuSuishenkan;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.tumblr.Tumblr;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.vkontakte.VKontakte;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.yixin.friends.Yixin;
import cn.sharesdk.yixin.moments.YixinMoments;
import cn.sharesdk.youdao.YouDao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.adapter.NewsAdapter;
import com.gov.nzjcy.entity.NewsEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class NewsContentActivity extends BaseActivity {
	
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
	/**评论编辑框*/
	private TextView		commentTextView;
	/**评论条数*/
	private TextView		commentCountTextView;
	/**评论的linelayout*/
	private LinearLayout	commentLinearLayout;
	/**顶端的LineLayout*/
	private LinearLayout	topContentLinearLayout;
	/**评论总数*/
	String					newsCommentCount;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newscontent);
		ShareSDK.initSDK(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		newsCatagoryId = getIntent().getStringExtra("catagyId");
		newsIdString = getIntent().getStringExtra("newsId");
		newsTitleString = getIntent().getStringExtra("newsTitle");
		newsDateString = getIntent().getStringExtra("newsDate");
		
		initView();
		new Thread()
		{
			public void run() {
				getNewsContentByNewsId();
				getNewsCommentCount(newsIdString);
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
				NewsContentActivity.this.finish();
			}
		});
		//修改图片
		top_right_headImageButton.setBackgroundResource(R.drawable.weshare);
		//隐藏右边的按钮
		//top_right_headImageButton.setVisibility(View.INVISIBLE);
		//右边登录按钮的点击事件
		top_right_headImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 图文分享
				showShare(false, null);
			}
		});
	}
	
	public void onCancel(Platform arg0, int arg1) {
		
	}
	
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		System.out.println("===================================");
		Platform qzonePlatform = ShareSDK.getPlatform(NewsContentActivity.this, TencentWeibo.NAME);
		String accessTokenString = qzonePlatform.getDb().getToken();
		System.out.println(accessTokenString);
	}
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		
	}

	//获取新闻内容
	protected void getNewsContentByNewsId() {
		newsContentDescribe = "";
		try {
			HttpResponse aString = gosHttpOperation.invokerNewsContentResponseByNewsId(newsCatagoryId, newsIdString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					JSONArray responeArray = dataObject.getJSONArray("responseList");
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
				
							newsContentDescribe = oneObject.getString("description").trim();
							String oneNewString = "";
							String[] newsStrings = newsContentDescribe.split("<img");
							for (int j = 0; j < newsStrings.length - 1; j++) {
								oneNewString = oneNewString + newsStrings[j]+"<img style='width:340px;height:350px'";
							}
							oneNewString = oneNewString + newsStrings[newsStrings.length-1];
							newsContentDescribe = "<p style='text-align:center;font-size:20px;'>"+newsTitleString+"</p>"+"<p style='text-align:center;'>"+newsDateString+"</p>" + "<base href=\"http://m.nzjcy.gov.cn:81\" />" +oneNewString;
						}
						handler.sendEmptyMessage(1);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.sendEmptyMessage(0);
	}
	
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
	/**
	 * 获取新闻评论条数
	 * 
	 * */
	protected void getNewsCommentCount(String newsIdString) {
		try {
			HttpResponse aString = gosHttpOperation.invokerGetCommentCountResponseByNewsId(newsIdString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
					JSONArray responeArray = dataObject.getJSONArray("responseList");
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							JSONObject oneObject = responeArray.getJSONObject(i);
							newsCommentCount = oneObject.getString("count").trim();
						}
						newsCommnetCountHandler.sendEmptyMessage(1);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 新闻评论条数显示
	 * */
	Handler newsCommnetCountHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				showComment();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	//新闻内容界面显示
	protected void showNewsContent() {
		newsContentWebView = (WebView)findViewById(R.id.newsContentWebview);
		//禁用cache
		newsContentWebView.loadDataWithBaseURL("http://140.206.70.110:8805/NanZhao/index.action", 
				newsContentDescribe, "text/html", "utf-8", null);
		newsContentWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		newsContentWebView.clearCache(true);
		newsContentWebView.destroyDrawingCache();
		
		showComment();
	}
	//显示评论框和评论条数
	protected void showComment() {
		//commentLinearLayout = (LinearLayout)findViewById(R.id.commentContentLinelayout);
		commentTextView = (TextView)findViewById(R.id.commentTextView);
		commentTextView.setOnClickListener(new OnMyClickListener());
		commentCountTextView = (TextView)findViewById(R.id.commentCountTextView);
		if (newsCommentCount == null) {
			commentCountTextView.setText("0评论");
		}
		else {
			commentCountTextView.setText(newsCommentCount+"评论");
		}
		commentCountTextView.setOnClickListener(new OnMyClickListener());
	}
	
	class OnMyClickListener implements OnClickListener
	{
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.commentTextView:
				Intent toPopIntent = new Intent(NewsContentActivity.this,  PopCommentActivity.class);  
				toPopIntent.putExtra("newstitle", newsTitleString);
				toPopIntent.putExtra("newsIdStr", newsIdString);
				startActivity(toPopIntent);
				break;
			case R.id.commentCountTextView:
				Intent commentIntent = new Intent(NewsContentActivity.this, CommentContentActivity.class);
				commentIntent.putExtra("newstitle", newsTitleString);
				commentIntent.putExtra("newsIdStr", newsIdString);
				NewsContentActivity.this.startActivity(commentIntent);
				break;
			default:
				break;
			}
		}
	}
	
	/** 
     * 使用快捷分享完成图文分享 
     */  
    private void showGrid(boolean silent) { 
    	
        Intent i = new Intent(NewsContentActivity.this, ShareAllGird.class);  
        // 分享时Notification的图标  
        i.putExtra("notif_icon", R.drawable.ic_launcher);  
        // 分享时Notification的标题  
        i.putExtra("notif_title", this.getString(R.string.app_name));  
  
//        // title标题，在印象笔记、邮箱、信息、微信（包括好友和朋友圈）、人人网和QQ空间使用，否则可以不提供  
//        i.putExtra("title", this.getString(R.string.share));  
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用，否则可以不提供  
//        i.putExtra("titleUrl", "http://sharesdk.cn");  
//        // text是分享文本，所有平台都需要这个字段  
//        i.putExtra("text", newsTitleString);  
//        // imagePath是本地的图片路径，所有平台都支持这个字段，不提供，则表示不分享图片  
//        i.putExtra("imagePath", MainActivity.TEST_IMAGE);  
//        // url仅在微信（包括好友和朋友圈）中使用，否则可以不提供  
//        i.putExtra("url", "http://sharesdk.cn");  
//        // thumbPath是缩略图的本地路径，仅在微信（包括好友和朋友圈）中使用，否则可以不提供  
//        i.putExtra("thumbPath", MainActivity.TEST_IMAGE);  
//        // appPath是待分享应用程序的本地路劲，仅在微信（包括好友和朋友圈）中使用，否则可以不提供  
//        i.putExtra("appPath", MainActivity.TEST_IMAGE);  
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供  
//        i.putExtra("comment", this.getString(R.string.share));  
//        // site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供  
//        i.putExtra("site", this.getString(R.string.app_name));  
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供  
//        i.putExtra("siteUrl", "http://sharesdk.cn");  
  
        // 是否直接分享  
        i.putExtra("silent", silent);  
        this.startActivity(i);  
    }  
      
//    /** 
//     * 将action转换为String 
//     */  
//    public static String actionToString(int action) {  
//        switch (action) {  
//            case AbstractWeibo.ACTION_AUTHORIZING: return "ACTION_AUTHORIZING";  
//            case AbstractWeibo.ACTION_GETTING_FRIEND_LIST: return "ACTION_GETTING_FRIEND_LIST";  
//            case AbstractWeibo.ACTION_FOLLOWING_USER: return "ACTION_FOLLOWING_USER";  
//            case AbstractWeibo.ACTION_SENDING_DIRECT_MESSAGE: return "ACTION_SENDING_DIRECT_MESSAGE";  
//            case AbstractWeibo.ACTION_TIMELINE: return "ACTION_TIMELINE";  
//            case AbstractWeibo.ACTION_USER_INFOR: return "ACTION_USER_INFOR";  
//            case AbstractWeibo.ACTION_SHARE: return "ACTION_SHARE";  
//            default: {  
//                return "UNKNOWN";  
//            }  
//        }  
//    } 
    
    
	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
	/**ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
	 * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
	 * 或者看网络集成文档 http://wiki.sharesdk.cn/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 *
	 *
	 * 平台配置信息有三种方式：
	 * 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://sharesdk.cn/androidDoc/cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
	 */
	private void showShare(boolean silent, String platform) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher, getBaseContext().getString(R.string.app_name));
		oks.setAddress("12345678901");
		//oks.setTitle(getBaseContext().getString(R.string.evenote_title));
		oks.setTitleUrl("http://sharesdk.cn");
		String nameString = newsTitleString + "。想要浏览更多内容，请到 http://m.nzjcy.gov.cn:81/nzjcy.apk 下载南召检察客户端。";
		oks.setText(nameString);
		//oks.setImagePath(MainActivity.TEST_IMAGE);
		//oks.setImageUrl(MainActivity.TEST_IMAGE_URL);
		oks.setUrl("http://www.sharesdk.cn");
		//oks.setFilePath(MainActivity.TEST_IMAGE);
		oks.setComment(getBaseContext().getString(R.string.share));
		oks.setSite(getBaseContext().getString(R.string.app_name));
		oks.setSiteUrl("http://sharesdk.cn");
		oks.setVenueName("ShareSDK");
		oks.setVenueDescription("This is a beautiful place!");
		oks.setLatitude(23.056081f);
		oks.setLongitude(113.385708f);
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}

		// 取消注释，可以实现对具体的View进行截屏分享
//		oks.setViewToShare(getPage());

		// 去除注释，可令编辑页面显示为Dialog模式
//		oks.setDialogMode();

		// 去除注释，在自动授权时可以禁用SSO方式
//		oks.disableSSOWhenAuthorize();

		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
//		oks.setCallback(new OneKeyShareCallback());
		//oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());

		// 去除注释，演示在九宫格设置自定义的图标
//		Bitmap logo = BitmapFactory.decodeResource(menu.getResources(), R.drawable.ic_launcher);
//		String label = menu.getResources().getString(R.string.app_name);
//		OnClickListener listener = new OnClickListener() {
//			public void onClick(View v) {
//				String text = "Customer Logo -- ShareSDK " + ShareSDK.getSDKVersionName();
//				Toast.makeText(menu.getContext(), text, Toast.LENGTH_SHORT).show();
//				oks.finish();
//			}
//		};
//		oks.setCustomerLogo(logo, label, listener);

		// 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
//		oks.addHiddenPlatform(SinaWeibo.NAME);
//		oks.addHiddenPlatform(TencentWeibo.NAME);
		oks.addHiddenPlatform(Yixin.NAME);
		oks.addHiddenPlatform(Wechat.NAME);
		oks.addHiddenPlatform(Mingdao.NAME);
		oks.addHiddenPlatform(Renren.NAME);
		oks.addHiddenPlatform(Facebook.NAME);
		oks.addHiddenPlatform(Twitter.NAME);
		oks.addHiddenPlatform(Yixin.NAME);
		oks.addHiddenPlatform(YouDao.NAME);
		oks.addHiddenPlatform(FourSquare.NAME);
		oks.addHiddenPlatform(GooglePlus.NAME);
		oks.addHiddenPlatform(Instagram.NAME);
		oks.addHiddenPlatform(LinkedIn.NAME);
		oks.addHiddenPlatform(Tumblr.NAME);
		oks.addHiddenPlatform(SohuSuishenkan.NAME);
		oks.addHiddenPlatform(Pinterest.NAME);
		oks.addHiddenPlatform(Flickr.NAME);
		oks.addHiddenPlatform(Dropbox.NAME);
		oks.addHiddenPlatform(VKontakte.NAME);
		oks.addHiddenPlatform(YixinMoments.NAME);
		oks.addHiddenPlatform(Evernote.NAME);
		oks.addHiddenPlatform(Douban.NAME);
		oks.addHiddenPlatform(KaiXin.NAME);
		oks.addHiddenPlatform(QQ.NAME);
		oks.addHiddenPlatform(Wechat.NAME);
		oks.addHiddenPlatform(WechatFavorite.NAME);
		oks.addHiddenPlatform(QZone.NAME);
		oks.addHiddenPlatform(WechatMoments.NAME);
		
		oks.show(getBaseContext());
	}
	
	
	
}
