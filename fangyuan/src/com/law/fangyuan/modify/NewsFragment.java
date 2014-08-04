package com.law.fangyuan.modify;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

import com.law.fangyuan.DropDownListView;
import com.law.fangyuan.DropDownListViewAdapter;
import com.law.fangyuan.FileCache;
import com.law.fangyuan.Focus;
import com.law.fangyuan.Http;
import com.law.fangyuan.NewsContent;
import com.law.fangyuan.R;
import com.law.fangyuan.VideoContent;
import com.law.fangyuan.DropDownListView.OnDropDownListener;

@SuppressLint("HandlerLeak")
public class NewsFragment extends Fragment {
	private String TAG = "NewsFragment";
	ShowNewsActivity activity;
	ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
	
	NewsAdapter mAdapter;
	String text;
	String accessTypeString;//进入状态
	ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;
	
	private AdvViewPager viewPager;
	private LinearLayout ll_point;
	private FrameLayout frameLayout;
	private ArrayList<View> arrayList;
	private int image_id[] = {  };
	private int frameheight;// 图片的高度
	private ArrayList<ImageView> imageViews;
	private Timer timer;

	private int window_width;

	private View view;// 此时触摸的view
	
	private String currentPageString = "1";//当前位置
	
	
	
	private int p = 1;
	private int com = 0;
	private boolean r = false;
	
	private FileCache cache;
	//private Loading loading;
	private Focus headerView = null;
	private JSONArray jsondata = null;
	private String classid,cache_name;
    private DropDownListView mListView = null;
    private DropDownListViewAdapter adapter = null;
    
    private LinearLayout	waittingLinearLayout;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//window_width = GOSHelper.getWindowsWidth(activity);
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		classid = args != null ? args.getString("catalogyId") : "";
		accessTypeString = args != null ? args.getString("accessType") : "";
		//initData();
		
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		//this.activity = (ShowNewsActivity)activity;
		super.onAttach(activity);
	}
	
	/** 此方法意思为fragment是否可见 ,可见时候加载数据 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			//fragment可见时加载数据
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
//					newsList.removeAll(newsList);
//					currentPageString = "1";
////					ArrayList<NewsEntity> newsEntities = activity.getNewsArrayList(currentPageString);
////					for (int i = 0; i < newsEntities.size(); i++) {
////						newsList.add(newsEntities.get(i));
////					}
					handler.obtainMessage(SET_NEWSLIST).sendToTarget();
					
				}
			}).start();
		}else{
			//fragment不可见时不执行操作
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
		mListView = (DropDownListView) view.findViewById(R.id.mListView);
		TextView item_textview = (TextView)view.findViewById(R.id.item_textview);
		detail_loading = (ImageView)view.findViewById(R.id.detail_loading);
		waittingLinearLayout = (LinearLayout)view.findViewById(R.id.waittingLinearLayout);
		item_textview.setText(text);
		
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
				switch (Integer.parseInt(accessTypeString)) {
				case 0:
				case 1:
					//intent.setClass(activity, NewsContent.class);
					break;
				case 2:
					//intent.setClass(activity, VideoContent.class);
					break;
				default:
					break;
				}
				activity.startActivityForResult(intent, 10);
			}
			
		});
		
		//initHeadImage(inflater);
		
		return view;
	}
	
	public void getData(String classid){
		cache_name = "list/"+classid+"_"+Integer.toString(p);
		//cache = new FileCache(activity);
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
		try {
			JSONObject json = new JSONObject(result);
			addJson(json.getJSONArray("r"));
			if(adapter==null){
				newImage(json.getJSONArray("top"));
				//adapter = new DropDownListViewAdapter(activity,jsondata);
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
    	        case 1:mListView.onDropDownComplete(activity.getString(R.string.update_at)+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));break;
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
		if(headerView!=null){
			headerView = (Focus) mListView.findViewWithTag("big_img");
			mListView.removeHeaderView(headerView);
			//headerView.removeAllViews();
		}
		//headerView=new Focus(activity);
		headerView.setTag("big_img");
		headerView.Init(big,classid);
		mListView.addHeaderView(headerView);
	}
	
    
	class getList extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result="";
			//Http http = new Http(activity);
			//result = http.GET("http://www.60886666.com/android/?m=getList&classid="+classid+"&page="+Integer.toString(p));
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
	
	
	
	
//	class MyOnRefreshListener2 implements OnRefreshListener2<ListView> {
//
//		private PullToRefreshListView mPtflv;
//
//		public MyOnRefreshListener2(PullToRefreshListView ptflv) {
//			this.mPtflv = ptflv;
//		}
//
//		@Override
//		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//			// 下拉刷新
//			String label = DateUtils.formatDateTime(activity.getApplicationContext(),
//					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
//							| DateUtils.FORMAT_SHOW_DATE
//							| DateUtils.FORMAT_ABBREV_ALL);
//
//			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//			new GetBeforeNewsTask(mPtflv).execute();
//
//		}
//
//		@Override
//		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//			// 上拉加载
//			new GetAfterNewsTask(mPtflv).execute();
//		}
//
//	}
//	
//	/**
//	 * 请求前十条网络获得新闻信息
//	 * 
//	 * @author Louis
//	 * 
//	 */
//	class GetBeforeNewsTask extends AsyncTask<String, Void, Integer> {
//		private PullToRefreshListView mPtrlv;
//		ArrayList<NewsEntity> newsEntities = new ArrayList<NewsEntity>();
//		public GetBeforeNewsTask(PullToRefreshListView ptrlv) {
//			this.mPtrlv = ptrlv;
//		}
//
//		@Override
//		protected Integer doInBackground(String... params) {
//			currentPageString = "1";
//			//newsEntities = activity.getNewsArrayList(currentPageString);
//			if (newsEntities.size() > 0) {
//				newsList.removeAll(newsList);
//				for (int i = 0; i < newsEntities.size(); i++) {
//					newsList.add(newsEntities.get(i));
//				}
//			}
//			return 1;
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			super.onPostExecute(result);
//			switch (result) {
//			case 1:
//				mAdapter = new NewsAdapter(activity, newsList);
//				mAdapter.notifyDataSetChanged();
//				break;
//			case 0:
//				Toast.makeText(activity, "请检查网络", Toast.LENGTH_SHORT)
//						.show();
//				break;
//			}
//			mPtrlv.onRefreshComplete();
//		}
//
//	}
//	
//	/**
//	 * 请求下面十条网络获得新闻信息
//	 * 
//	 * @author Louis
//	 * 
//	 */
//	class GetAfterNewsTask extends AsyncTask<String, Void, Integer> {
//		private PullToRefreshListView mPtrlv;
//		ArrayList<NewsEntity> newsEntities = new ArrayList<NewsEntity>();
//		public GetAfterNewsTask(PullToRefreshListView ptrlv) {
//			this.mPtrlv = ptrlv;
//		}
//
//		@Override
//		protected Integer doInBackground(String... params) {
//			int currentPageInt = Integer.parseInt(currentPageString);
//			currentPageInt++;
//			currentPageString = String.valueOf(currentPageInt);
//			//newsEntities = activity.getNewsArrayList(currentPageString);
//			if (newsEntities.size() > 0) {
//				for (int i = 0; i < newsEntities.size(); i++) {
//					newsList.add(newsEntities.get(i));
//				}
//			}
//			return 1;
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			super.onPostExecute(result);
//			switch (result) {
//			case 1:
//				mAdapter = new NewsAdapter(activity, newsList);
//				mAdapter.notifyDataSetChanged();
//				break;
//			case 0:
//				Toast.makeText(activity, "请检查网络", Toast.LENGTH_SHORT)
//						.show();
//				break;
//			}
//			mPtrlv.onRefreshComplete();
//		}
//
//	}
	
//	private void initData() {
//		if (accessTypeString.equals("0")) {
//			newsList = Constants.getNewsList();
//		}
//		else if (accessTypeString.equals("1")) {
//			newsList = Constants.getOneNewsList();
//		}
//	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SET_NEWSLIST:
				//detail_loading.setVisibility(View.GONE);
				adapter = null;
				getData(classid);
//				mAdapter = new NewsAdapter(activity, newsList);
//				mListView.setAdapter(mAdapter);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	/***
	 * 初始化 point
	 */
	int i;

	void initPoint() {
		imageViews = new ArrayList<ImageView>();
		ImageView imageView;

		for (i = 0; i < image_id.length; i++) {
			//imageView = new ImageView(activity);
			//imageView.setBackgroundResource(R.drawable.indicator);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			//ll_point.addView(imageView, layoutParams);

			//imageViews.add(imageView);
		}

	}

	/***
	 * 初始化 PagerChild
	 */
	void initPagerChild() {
		arrayList = new ArrayList<View>();
		for (int i = 0; i < image_id.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setTag(i);
			imageView.setScaleType(ScaleType.FIT_XY);
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					image_id[i]);
			Bitmap bitmap2 = getBitmap(bitmap, window_width);
			frameheight = bitmap2.getHeight();// 获取要显示的高度
			Log.e("jj", "frameheight=" + frameheight);
			imageView.setImageBitmap(bitmap2);
			arrayList.add(imageView);
		}
		initPoint();
	}

	/***
	 * 初始化 HeadImage
	 */
	void initHeadImage(LayoutInflater inflater) {
		
		frameLayout = (FrameLayout)inflater.inflate(R.layout.topnews_image, null);
		viewPager = (AdvViewPager) frameLayout.findViewById(R.id.viewpager);
		ll_point = (LinearLayout) frameLayout.findViewById(R.id.ll_point);
		//frameLayout = (FrameLayout) headview.findViewById(R.id.fl_main);
		initPagerChild();
//		LayoutParams layoutParams = frameLayout.getLayoutParams();
//		layoutParams.height = frameheight;
//		frameLayout.setLayoutParams(layoutParams);
		viewPager.setAdapter(new ViewPagerAdapter(arrayList));
		
		/***
		 * viewpager
		 * 
		 * PageChangeListener
		 */
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				draw_Point(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		draw_Point(0);// 默认首次进入
		if (accessTypeString.equals("0")) {
			//mListView.getRefreshableView().addHeaderView(frameLayout, null, false);//加载viewpager
		}
		else if (accessTypeString.equals("1")) {
			
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int currentPosition = position - 1;
				if (currentPosition >= 0)
//					Toast.makeText(activity, newsList.get(currentPosition).getTitle(), Toast.LENGTH_SHORT)
//							.show();
					Log.v(TAG, String.valueOf(currentPosition));
//					Intent newsContentIntent = new Intent(activity,NewsContentActivity.class);
//					newsContentIntent.putExtra("catagyId", newsList.get(currentPosition).getNewsCategoryId());
//					newsContentIntent.putExtra("newsId", newsList.get(currentPosition).getNewsId());
//					newsContentIntent.putExtra("newsTitle", newsList.get(currentPosition).getTitle());
//					newsContentIntent.putExtra("newsDate", newsList.get(currentPosition).getSource());
//					activity.startActivity(newsContentIntent);
			}
		});
		
		//mListView.setViewPager(viewPager);
	}
	
	/***
	 * 更新选中点
	 * 
	 * @param index
	 */
	private void draw_Point(int index) {
		for (int i = 0; i < imageViews.size(); i++) {
			imageViews.get(i).setImageResource(R.drawable.indicator);
		}
		imageViews.get(index).setImageResource(R.drawable.indicator_focused);
	}
	
	/***
	 * 对图片处理
	 * 
	 * @author zhangjia
	 * 
	 */
	Bitmap getBitmap(Bitmap bitmap, int width) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scale = (float) width / w;
		// 保证图片不变形.
		matrix.postScale(scale, scale);
		// w,h是原图的属性.
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	public class ViewPagerAdapter extends PagerAdapter {
		// 界面列表
		private List<View> views;

		public ViewPagerAdapter(List<View> views) {
			this.views = views;
		}

		// 销毁arg1位置的界面
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		// 获得当前界面数
		@Override
		public int getCount() {
			if (views != null) {
				// 返回一个比较大的数字
				return views.size();
			}
			return 0;
		}

		// 初始化arg1位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1));
			return views.get(arg1);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}	
		
	}
	
}

