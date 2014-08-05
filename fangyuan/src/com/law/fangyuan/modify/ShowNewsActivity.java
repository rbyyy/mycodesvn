package com.law.fangyuan.modify;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
//import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import android.widget.Toast;



import com.law.fangyuan.R;


@SuppressLint("HandlerLeak")
public class ShowNewsActivity extends Fragment {//extends FragmentActivity
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
	private ImageView		topTitleImageView;
	
	/**解析进入的类型*/
	private String			accessTypeString;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.activity_shownews, container, false);
		mScreenWidth = GOSHelper.getWindowsWidth(getActivity());
		//mItemWidth = mScreenWidth / 5;// 一个Item宽度为屏幕的1/7
		//accessTypeString = getActivity().getIntent().getStringExtra("accessType");
		initView(v);
		return v;
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_shownews);
//		mScreenWidth = GOSHelper.getWindowsWidth(this);
//		//mItemWidth = mScreenWidth / 5;// 一个Item宽度为屏幕的1/7
//		accessTypeString = getIntent().getStringExtra("accessType");
//		initView();
//	}
	/** 初始化layout控件*/
	private void initView(View v) {
		//顶部滚动条
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)v.findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout)v.findViewById(R.id.mRadioGroup_content);
		//ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		rl_column = (RelativeLayout)v.findViewById(R.id.rl_column);
		mViewPager = (ViewPager)v.findViewById(R.id.mViewPager);
		shade_left = (ImageView)v.findViewById(R.id.newivTitleBtnLeft);

		//左边返回按钮的点击事件
		shade_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//getActivity().finish();
			}
		});
		
		setChangelView();
	}
	/** 
	 *  当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		//initFragment();
	}
	/** 获取Column栏目 数据*/
	private void initColumnData() {
		newsClassify = Constants.getData();
//		if (accessTypeString.equals("0")) {
//			newsClassify = Constants.getData();
//		}
//		else if (accessTypeString.equals("1")) {
//			newsClassify = Constants.getOneData();
//		}
//		else if (accessTypeString.equals("2")) {
//			newsClassify = Constants.getTwoData();
//		}
	}

	/** 
	 *  初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count =  newsClassify.size();
		mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
		for(int i = 0; i< count; i++){
			
			//TextView localTextView = (TextView).inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(getActivity());
			columnTextView.setTextAppearance(getActivity(), R.style.top_category_scroll_view_item_text);
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(newsClassify.get(i).getTitle());
			columnTextView.setTextColor(Color.rgb(145, 145, 145));
			if(columnSelectIndex == i){
				columnTextView.setSelected(true);
				columnTextView.setTextColor(Color.rgb(1, 140, 221));
			}
			columnTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
			          for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
				          View localView = mRadioGroup_content.getChildAt(i);
				          if (localView != v)
				          {
				        	  TextView oneTextView = (TextView)localView;
			        	  	  oneTextView.setTextColor(Color.rgb(145, 145, 145));
				        	  localView.setSelected(false);
				          }  
				          else{
				        	  TextView oneTextView = (TextView)localView;
				        	  oneTextView.setTextColor(Color.rgb(1, 140, 221));
				        	  localView.setSelected(true);
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
				 TextView oneTextView = (TextView)checkView;
	        	 oneTextView.setTextColor(Color.rgb(1, 140, 221));
			} else {
				TextView oneTextView = (TextView)checkView;
      	  	  	oneTextView.setTextColor(Color.rgb(145, 145, 145));
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
			data.putString("catalogyId", newsClassify.get(i).getId().toString());
    		data.putString("text", newsClassify.get(i).getTitle());
    		data.putString("accessType", accessTypeString);
			NewsFragment newfragment = new NewsFragment();
			newfragment.setArguments(data);
			fragments.add(newfragment);
		}
		
//		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
////		mViewPager.setOffscreenPageLimit(0);
//		mViewPager.setAdapter(mAdapetr);
//		mViewPager.setOnPageChangeListener(pageListener);
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
//	/**
//	 * 获取相应条数的新闻（含新闻标题，新闻配图和新闻发布时间）
//	 * */
//	public ArrayList<NewsEntity> getNewsArrayList(String pageNumberString) {
//		ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
//		String categoryIDString = "3";
//		if (accessTypeString.equals("0")) {
//			
//		}
//		else if (accessTypeString.equals("1")) {
//			categoryIDString = String.valueOf(columnSelectIndex + 3);
//		}
//		
//		try {
//			HttpResponse aString = gosHttpOperation.invokerNewsResponseByCategoryId(categoryIDString, pageNumberString);
//			HttpEntity entity = aString.getEntity();
//			InputStream is = entity.getContent();
//			String reString = GOSHelper.convertStreamToString(is);
//		
//			if (reString == null || reString.equals("")) {
//				
//			}
//			else {
//				JSONObject jsonObject = JSON.parseObject(reString);
//				JSONObject dataObject = jsonObject.getJSONObject("data");
//				String stateString = dataObject.getString("state");
//				if (stateString.equals("1")) {
//					JSONArray responeArray = dataObject.getJSONArray("responseList");
//					
//					if (responeArray.size() > 0) {
//						for (int i = 0; i < responeArray.size(); i++) {
//							JSONObject oneObject = responeArray.getJSONObject(i);
//							NewsEntity news = new NewsEntity();
//							news.setNewsId(oneObject.getString("id").trim());
//							news.setNewsCategoryId(oneObject.getString("categoryID").trim());
//							news.setTitle(oneObject.getString("title").trim());
//							List<String> url_list = new ArrayList<String>();
//							String url = oneObject.getString("picID").trim();
//							news.setPicOne(url);
//							url_list.add(url);
//							news.setPicList(url_list);
//							news.setSource(oneObject.getString("dateandtime").trim());
//							newsList.add(news);
//						}
//					}
//				}
//			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (BaseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}	
//		return newsList;
//	}
	
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
