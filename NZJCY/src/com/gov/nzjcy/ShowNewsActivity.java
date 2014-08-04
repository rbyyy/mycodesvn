package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.fragment.NewsFragment;
import com.gov.nzjcy.adapter.NewsFragmentPagerAdapter;
import com.gov.nzjcy.constants.Constants;
import com.gov.nzjcy.entity.NewsClassify;
import com.gov.nzjcy.entity.NewsEntity;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.util.FileCache;
import com.shownews.view.ColumnHorizontalScrollView;
import com.gov.nzjcy.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("HandlerLeak")
public class ShowNewsActivity extends BaseActivity {
	private String TAG = "ShowNewsActivity";
	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	LinearLayout ll_more_columns;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	/** 新闻分类列表*/
	private ArrayList<NewsClassify> newsClassify=new ArrayList<NewsClassify>();
	/** 当前选中的栏目*/
	private int columnSelectIndex = 0;
	/** 左阴影部分*/
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
//	/** Item宽度 */
//	private int mItemWidth = 0;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	
//	/** head 头部 的中间的loading*/
//	private ProgressBar top_progress;
//	/** head 头部 中间的刷新按钮*/
//	private ImageView top_refresh;
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	//private ImageView top_more;
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	
	/**解析进入的类型*/
	private String			accessTypeString;
	
	private FileCache 		cache;
	private String 			classid,cache_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shownews);
		mScreenWidth = GOSHelper.getWindowsWidth(this);
		//mItemWidth = mScreenWidth / 5;// 一个Item宽度为屏幕的1/7
		accessTypeString = getIntent().getStringExtra("accessType");
		initView();
	}
	/** 初始化layout控件*/
	private void initView() {
		//顶部滚动条
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		//ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		if (accessTypeString.equals("0")) {
			topTitleTextView.setText("南召检察院心连心网站");//标题
		}
		else if (accessTypeString.equals("1")) {
			topTitleTextView.setText("阳光检务");//标题
		}
		
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowNewsActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		//右边登录按钮的点击事件
//		top_right_headImageButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		setChangelView();
	}
	/** 
	 *  当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
//		new Thread(){
//			public void run() {
//				
//				if (accessTypeString.equals("0")) {
//					getNewsArrayList(mainOneArrayList, "3", "1");
//				}
//				else if (accessTypeString.equals("1")) {
//					getNewsArrayList(sunOneArrayList, "3", "1");
//				}
//			}
//		}.start();
		initFragment();
	}
	/** 获取Column栏目 数据*/
	private void initColumnData() {
		if (accessTypeString.equals("0")) {
			newsClassify = Constants.getData();
		}
		else if (accessTypeString.equals("1")) {
			newsClassify = Constants.getOneData();
		}
		
	}

	/** 
	 *  初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count =  newsClassify.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
		for(int i = 0; i< count; i++){
			
			//TextView localTextView = (TextView).inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
			//localTextView.setBackgroundColor(Color.BLACK);
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(newsClassify.get(i).getTitle());
			columnTextView.setTextColor(Color.WHITE);
			if(columnSelectIndex == i){
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
			          for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
				          View localView = mRadioGroup_content.getChildAt(i);
				          if (localView != v)
				        	  localView.setSelected(false);
				          else{
				        	  localView.setSelected(true);
				        	  columnSelectIndex = i;
				        	  mViewPager.setCurrentItem(i);
				        	  
				          }
			          }
			          //Toast.makeText(getApplicationContext(), newsClassify.get(v.getId()).getTitle(), Toast.LENGTH_SHORT).show();
				}
			});
			TextPaint paint = columnTextView.getPaint();
			float len = paint.measureText(newsClassify.get(i).getTitle());
			int mItemWidthOne = (int)len + 15; 
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidthOne , LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
			mRadioGroup_content.addView(columnTextView, i ,params);
		}
	}
	/** 
	 *  选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		//判断是否选中
		for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}
	/** 
	 *  初始化Fragment
	 * */
	private void initFragment() {
		
		int count =  newsClassify.size();
		for(int i = 0; i< count;i++){
			Bundle data = new Bundle();
    		data.putString("text", newsClassify.get(i).getTitle());
    		data.putString("accessType", accessTypeString);
			NewsFragment newfragment = new NewsFragment();
			newfragment.setArguments(data);
			fragments.add(newfragment);
		}
		
		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
//		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
	}
	/** 
	 *  ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener= new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			mViewPager.setCurrentItem(position);
			
			selectTab(position);
		}
	};
	/**
	 * 获取相应条数的新闻（含新闻标题，新闻配图和新闻发布时间）
	 * */
	public ArrayList<NewsEntity> getNewsArrayList(String pageNumberString) {
		ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
		String categoryIDString = "";
		if (accessTypeString.equals("0")) {
			
		}
		else if (accessTypeString.equals("1")) {
			switch (columnSelectIndex) {
			case 0:
				categoryIDString = "8";
				break;
			case 1:
				categoryIDString = "11";
				break;
			case 2:
				categoryIDString = "12";
				break;
			case 3:
				categoryIDString = "13";
				break;
			case 4:
				categoryIDString = "14";
				break;
			case 5:
				categoryIDString = "15";
				break;
			default:
				break;
			}
		}
		cache = new FileCache(this);
		String cache_name = "list/"+categoryIDString+"_" + pageNumberString;
		String result = cache.get(cache_name,3600);
		if(result.equals("")){
			try {
				HttpResponse aString = gosHttpOperation.invokerNewsResponseByCategoryId(categoryIDString, pageNumberString);
				HttpEntity entity = aString.getEntity();
				InputStream is = entity.getContent();
				String reString = GOSHelper.convertStreamToString(is);
				if(result.equals("")){
					cache.set(cache_name, reString);
				}
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
								NewsEntity news = new NewsEntity();
								news.setNewsId(oneObject.getString("id").trim());
								news.setNewsCategoryId(oneObject.getString("categoryID").trim());
								news.setTitle(oneObject.getString("title").trim());
								List<String> url_list = new ArrayList<String>();
								String url = oneObject.getString("picID").trim();
								news.setPicOne(url);
								url_list.add(url);
								news.setPicList(url_list);
								news.setSource(oneObject.getString("dateandtime").trim());
								newsList.add(news);
							}
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
		}else{
			String reString = result;
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
							NewsEntity news = new NewsEntity();
							news.setNewsId(oneObject.getString("id").trim());
							news.setNewsCategoryId(oneObject.getString("categoryID").trim());
							news.setTitle(oneObject.getString("title").trim());
							List<String> url_list = new ArrayList<String>();
							String url = oneObject.getString("picID").trim();
							news.setPicOne(url);
							url_list.add(url);
							news.setPicList(url_list);
							news.setSource(oneObject.getString("dateandtime").trim());
							newsList.add(news);
						}
					}
				}
			}
		}
		
			
		return newsList;
	}
	/**
	 * 1.0.13 获取顶部图片新闻
	 * */
//	public ArrayList<HashMap<String, String>> getTopNewsArrayList() {
//		ArrayList<HashMap<String, String>> topNewsArrayList = new ArrayList<HashMap<String,String>>();
//		topNewsArrayList = gosHttpOperation.invokerJsonTopNews();
//		
//		if (topNewsArrayList.size() > 0) {
//			return topNewsArrayList;
//		}
//		return null;
//	}
    /**
     * 1.0.13 获取顶部图片新闻
     * by 果悦科技
     * */
    public ArrayList<HashMap<String, String>> jsonTopNews() {
    	ArrayList<HashMap<String, String>> topNewsArrayList = new ArrayList<HashMap<String, String>>();
        //StringBuffer sff = new StringBuffer();//一定要new一个  
    	
    	cache = new FileCache(this);
		String cache_name = "list/"+"Picture"+"_"+Integer.toString(1);
		String result = cache.get(cache_name,3600);
		if (result.equals("")) {
			 try  
		        {  
		            Document doc = Jsoup.connect("http://www.nzjcy.gov.cn/index.asp").get();  
		            Element contentElement = doc.getElementById("sina_roll");
		            String urlString = contentElement.attr("abs:src");
		            if(result.equals("")){
						cache.set(cache_name, urlString);
					}
		            Document docOneDocument = Jsoup.connect(urlString).get();
		            Element contentElementOne = docOneDocument.getElementById("KinSlideshow");
		            Elements links = contentElementOne.select("a[href^=../nzcms_show_news]");  
		            //注意这里是Elements不是Element。同理getElementById返回Element，getElementsByClass返回时Elements  
		            for(Element link : links){ 
		            	HashMap<String, String> map = new HashMap<String, String>();
		            	String urlString1 = link.attr("abs:href");
		            	map.put("contentUrl", urlString1);
		            	String urlString2 = link.select("img").attr("abs:src");
		            	map.put("imgUrl", urlString2);
		            	String altString = link.select("img").attr("alt");
		            	map.put("imgTitle", altString);
		                topNewsArrayList.add(map);
		            }  
		        }  
		        catch (Exception e)  
		        {  
		        	e.printStackTrace();
		        }
		}
		else {
			try  
	        {  
	            Document docOneDocument = Jsoup.connect(result).get();
	            Element contentElementOne = docOneDocument.getElementById("KinSlideshow");
	            Elements links = contentElementOne.select("a[href^=../nzcms_show_news]");  
	            //注意这里是Elements不是Element。同理getElementById返回Element，getElementsByClass返回时Elements  
	            for(Element link : links){ 
	            	HashMap<String, String> map = new HashMap<String, String>();
	            	String urlString1 = link.attr("abs:href");
	            	map.put("contentUrl", urlString1);
	            	String urlString2 = link.select("img").attr("abs:src");
	            	map.put("imgUrl", urlString2);
	            	String altString = link.select("img").attr("alt");
	            	map.put("imgTitle", altString);
	                topNewsArrayList.add(map);
	            }  
	        }  
	        catch (Exception e)  
	        {  
	        	e.printStackTrace();
	        }
		}
    	
        return topNewsArrayList;
	}
    /**获取新闻内容*/
    public ArrayList<NewsEntity> jsonNewsFromWeb(String pagenumberString) {
    	ArrayList<NewsEntity> topNewsArrayList = new ArrayList<NewsEntity>();
    	String urlString = "http://www.nzjcy.gov.cn/nzcms_list_news.asp?";
    	switch (columnSelectIndex) {
			case 1:
				urlString = urlString + "id=668&sort_id=657&Page="+pagenumberString;
				break;
			case 0:
				urlString = urlString + "id=674&sort_id=658&Page="+pagenumberString;
				break;
			case 8:
				urlString = urlString + "id=732&sort_id=731&Page="+pagenumberString;
				break;
			case 9:
				urlString = urlString + "id=691&sort_id=660&Page="+pagenumberString;
				break;
			case 6:
				urlString = urlString + "id=696&sort_id=661&Page="+pagenumberString;
				break;
			case 7:
				urlString = urlString + "id=697&sort_id=662&Page="+pagenumberString;
				break;
			case 10:
				urlString = urlString + "id=721&sort_id=716&Page="+pagenumberString;
				break;
			case 3:
				urlString = urlString + "id=746&sort_id=738&Page="+pagenumberString;
				break;
			case 5:
				urlString = urlString + "id=735&sort_id=734&Page="+pagenumberString;
				break;
			case 4:
				urlString = urlString + "id=740&sort_id=739&Page="+pagenumberString;
				break;
			case 2:
				urlString = urlString + "id=750&sort_id=749&Page="+pagenumberString;
				break;
			default:
				break;
    	}
    	
        try  
        {  
        	 Document doc = Jsoup.connect(urlString).cookie("auth", "token").get();  
//        	 if (doc.toString() == null || doc.toString().trim().equals("")) {
//        		 return topNewsArrayList;
//        	 }
             Elements links = doc.select("table.dx");  //^=nzcms_show_news
             //注意这里是Elements不是Element。同理getElementById返回Element，getElementsByClass返回时Elements  
//             String linksString = links.toString();
             
             for(Element link : links){  
             	Elements linkOneElements = link.select("a[href]");
             	Elements linkTwoElements = link.select("[width=140]");
             	
             	NewsEntity news = new NewsEntity();
             	String showNewsUrlString = linkOneElements.attr("abs:href");
             	
             	news.setNewsId(showNewsUrlString);
             	
				news.setNewsCategoryId("");
				String titleString = linkOneElements.text();
				news.setTitle(titleString);
				List<String> url_list = new ArrayList<String>();
				
				news.setPicList(url_list);
				String dateString = linkTwoElements.text().replace("发稿：", "");
				news.setSource(dateString);
				topNewsArrayList.add(news);
                 //这里没有什么好说的。  
                 //sff.append(link.attr("abs:href")).append("  ").append(link.text()).append(" ");  
             }  
             //myString = sff.toString();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }
        return topNewsArrayList;
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	protected void initSlidingMenu() {
//		side_drawer = new DrawerView(this).initSlidingMenu();
//	}
//	
//	private long mExitTime;
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if(side_drawer.isMenuShowing() ||side_drawer.isSecondaryMenuShowing()){
//				side_drawer.showContent();
//			}else {
//				if ((System.currentTimeMillis() - mExitTime) > 2000) {
//					Toast.makeText(this, "在按一次退出",
//							Toast.LENGTH_SHORT).show();
//					mExitTime = System.currentTimeMillis();
//				} else {
//					finish();
//				}
//			}
//			return true;
//		}
//		//拦截MENU按钮点击事件，让他无任何操作
//		if (keyCode == KeyEvent.KEYCODE_MENU) {
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}
