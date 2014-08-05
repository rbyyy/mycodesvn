package com.law.fangyuan.modify;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.law.fangyuan.DropDownListView;
import com.law.fangyuan.DropDownListViewAdapter;
import com.law.fangyuan.FileCache;
import com.law.fangyuan.Focus;
import com.law.fangyuan.Http;
import com.law.fangyuan.MemberCenter;
import com.law.fangyuan.R;
import com.law.fangyuan.DropDownListView.OnDropDownListener;
import com.law.fangyuan.VideoContent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NewShowVideoNewsActivity extends Fragment {
	private String TAG = "ShowNewsActivity";
	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	LinearLayout ll_more_columns;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	private PagerTabStrip pagerTabStrip;//一个viewpager的指示器，效果就是一个横的粗的下划线  
	
	private List<View> viewList;//把需要滑动的页卡添加到这个list中
	/** 新闻分类列表*/
	private ArrayList<NewsClassify> newsClassify=new ArrayList<NewsClassify>();
//	/**上一个栏目*/
//	private int beforeSelectIndex = -1;
	/** 当前选中的栏目*/
	private int columnSelectIndex = 0;
//	/**下一个栏目*/
//	private int nextSelectIndex = 1;
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
	
	String text;
//	ImageView detail_loading;
	
	private int p = 1;
	private int com = 0;
	private boolean r = false;
	
	private FileCache cache;
	//private Loading loading;
	private Focus headerView = null;
	private JSONArray jsondata = null;
	private String classid,cache_name;
//    private DropDownListView mListView = null;
    private DropDownListViewAdapter adapter = null;
    
//    private LinearLayout	waittingLinearLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.activity_newshownews, container, false);
		mScreenWidth = GOSHelper.getWindowsWidth(getActivity());
		//mItemWidth = mScreenWidth / 5;// 一个Item宽度为屏幕的1/7
		//accessTypeString = getActivity().getIntent().getStringExtra("accessType");
		classid = "1048";
		initView(v);
		return v;
	}

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
		//用户登录
		ImageView userLoginImageView = (ImageView) v.findViewById(R.id.userLoginImageView);
	    userLoginImageView.setVisibility(View.VISIBLE);
		userLoginImageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), MemberCenter.class);
				getActivity().startActivity(intent);
			}
			
		});
		//
		setChangelView();
	}
	/** 
	 *  当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}
	/** 获取Column栏目 数据*/
	private void initColumnData() {
		newsClassify = Constants.getTwoData();
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
			columnTextView.setHeight(80);
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
		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
		for(int i = 0; i< count;i++){
			View oneView = initOneView("测试"+i);
			viewList.add(oneView);  
		}
		if ((adapter == null) || (columnSelectIndex == 0)) {
			adapter = null;
			getData(classid);
		}
		
//		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
////		mViewPager.setOffscreenPageLimit(0);
//		mViewPager.setAdapter(mAdapetr);
//		mViewPager.setOnPageChangeListener(pageListener);
		
		PagerAdapter pagerAdapter = new PagerAdapter() {  
			  
            @Override  
           public boolean isViewFromObject(View arg0, Object arg1) {  
                return arg0 == arg1;  
            }  
  
            @Override  
            public int getCount() {  
  
                return viewList.size();  
            }  
  
            @Override  
            public void destroyItem(ViewGroup container, int position,  
                    Object object) {   
               ((ViewPager)container).removeView(viewList.get(position));
 
           }  
  
           @Override  
            public int getItemPosition(Object object) {  
  
                return super.getItemPosition(object);  
            }  
           
//           @Override  
//           public CharSequence getPageTitle(int position) {  
//  
//        	  //直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。当然你可以使用。  
//        	   return newsClassify.get(position).getTitle();
//            }

  
           @Override  
            public Object instantiateItem(ViewGroup container, int position) {  
        	   ((ViewPager)container).addView(viewList.get(position));
				return viewList.get(position); 
           }  
		}; 
		mViewPager.setAdapter(pagerAdapter); 
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
			jsondata = null;
			adapter = null;

			classid = newsClassify.get(position).getId().toString();
			getData(classid);
		}
	};
	
	
	
	public View initOneView(String oneString) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
		DropDownListView mListView = (DropDownListView) view.findViewById(R.id.mListView);
		TextView item_textview = (TextView)view.findViewById(R.id.item_textview);
		ImageView detail_loading = (ImageView)view.findViewById(R.id.detail_loading);
		LinearLayout waittingLinearLayout = (LinearLayout)view.findViewById(R.id.waittingLinearLayout);
		item_textview.setText(oneString);

		
		mListView.setIsDropDownStyle(true);
		mListView.setIsOnBottomStyle(true);
		mListView.setIsAutoLoadOnBottom(true);
		mListView.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onDropDown() {
            	if(r) return;
            	r = true;
            	p=1;
            	com =1;
            	jsondata = null;
            	new getList().execute();
            }
        });

        // set on bottom listener
		mListView.setOnBottomListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	if(r) return;
            	r = true;
            	p = p+1;
            	com =2;
            	getData(classid);
            }
        });
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle b = new Bundle();
				b.putString("classid", classid);
				b.putString("id", ((TextView)v.findViewById(R.id.id)).getText().toString());
				intent.putExtras(b);
				intent.setClass(getActivity(), VideoContent.class);
				getActivity().startActivityForResult(intent, 10);
			}
			
		});
		
		//initHeadImage(inflater);
		
		return view;
	}
	
	public void getData(String classid){
		cache_name = "list/"+classid+"_"+Integer.toString(p);
		cache = new FileCache(getActivity());
		String result = cache.get(cache_name,3600);
		if(result.equals("")){
			new getList().execute();
		}else{
			InitList(result);
		}
	}
	
	public void addJson(JSONArray r){
		if(jsondata==null){
			jsondata = r;
		}else{
			for(int i=0; i<r.length();i++){
				jsondata.put(r.opt(i));
			}
		}
	}
    
    @SuppressLint("SimpleDateFormat")
	public void InitList(String result){
    	View viewOne = viewList.get(columnSelectIndex);
    	final LinearLayout waittingLinearLayout = (LinearLayout)viewOne.findViewById(R.id.waittingLinearLayout);
    	final DropDownListView mListView = (DropDownListView) viewOne.findViewById(R.id.mListView);
		try {
			JSONObject json = new JSONObject(result);
			addJson(json.getJSONArray("r"));
			if(adapter==null){
				newImage(json.getJSONArray("top"));
				adapter = new DropDownListViewAdapter(getActivity(),jsondata);
				mListView.setAdapter(adapter);
				mListView.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mListView.setVisibility(View.VISIBLE);
						//loading.setVisibility(View.GONE);
						waittingLinearLayout.setVisibility(View.GONE);
					}
		        	
		        });
			}else{
                adapter.notifyDataSetChanged();
    	        switch(com){
    	        case 1:mListView.onDropDownComplete(getActivity().getString(R.string.update_at)+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));break;
    	        case 2:mListView.onBottomComplete();break;
    	        }
    	        com = 0;
			}
			r = false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public void newImage(JSONArray big){
		if (big.length() > 0) {
			View viewOne = viewList.get(columnSelectIndex);
	    	final DropDownListView mListView = (DropDownListView) viewOne.findViewById(R.id.mListView);
	    	headerView = (Focus) mListView.findViewWithTag("big_img"+columnSelectIndex);
	    	if(headerView!=null){
				headerView = (Focus) mListView.findViewWithTag("big_img"+columnSelectIndex);
				mListView.removeHeaderView(headerView);
				headerView.removeAllViews();
			}

			headerView=new Focus(getActivity());
			headerView.setTag("big_img"+columnSelectIndex);
			headerView.Init(big,classid);
			mListView.addHeaderView(headerView);
		}
	}
	
    
	class getList extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result="";
			Http http = new Http(getActivity());
			result = http.GET("http://www.60886666.com/android/?m=getList&classid="+classid+"&page="+Integer.toString(p));
			if(!result.equals("")){
				cache.set(cache_name, result);
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			InitList(result.toString());
		}
		
	}
	
}
