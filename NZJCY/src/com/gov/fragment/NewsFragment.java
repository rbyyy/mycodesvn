package com.gov.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import org.json.JSONArray;

import com.gov.nzjcy.NewsContentActivity;
import com.gov.nzjcy.OneNewsContentActivity;
import com.gov.nzjcy.R;
import com.gov.nzjcy.ShowNewsActivity;
import com.gov.nzjcy.adapter.NewsAdapter;
import com.gov.nzjcy.entity.NewsEntity;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.component.AdvViewPager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.gov.nzjcy.util.FileCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

@SuppressLint("HandlerLeak")
public class NewsFragment extends Fragment {
	private String TAG = "NewsFragment";
	ShowNewsActivity activity;
	ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
	PullToRefreshListView mListView;
	NewsAdapter mAdapter;
	String text;
	String accessTypeString;//进入状态
	ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;
	
	private AdvViewPager viewPager;
	private LinearLayout ll_point;
	private FrameLayout frameLayout;
	private ArrayList<View> arrayList;
	private int image_id[] = { R.drawable.a, R.drawable.b, R.drawable.c };
	private ArrayList<HashMap<String, String>> imageArrayList = new ArrayList<HashMap<String, String>>();
	private int frameheight;// 图片的高度
	private ArrayList<ImageView> imageViews;
	private Timer timer;

	private int window_width;

	private View view;// 此时触摸的view
	
	private String currentPageString = "1";//当前位置
	
	private LayoutInflater	inflaterOne;
	
	protected 	ImageLoader imageLoader = ImageLoader.getInstance();
	protected   DisplayImageOptions options;
	
	private LinearLayout	waittingLinearLayout;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		window_width = GOSHelper.getWindowsWidth(activity);
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		accessTypeString = args != null ? args.getString("accessType") : "";
		//initData();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.logo).cacheInMemory(true).cacheOnDisc(true).build();  
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = (ShowNewsActivity)activity;
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
					requestUrlHttp("0", "1");
					//handler.obtainMessage(SET_NEWSLIST).sendToTarget();
					handler.sendEmptyMessage(0);
				}
			}).start();
		}else{
			//fragment不可见时不执行操作
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	public void requestUrlHttp(String isDelString, String pageNumberString) {
		if (isDelString.equals("0")) {
			if (imageArrayList.size()>0) {
				imageArrayList.removeAll(imageArrayList);
			}
			if (newsList.size() > 0) {
				newsList.removeAll(newsList);
			}
		}
		Bundle args = getArguments();
		accessTypeString = args != null ? args.getString("accessType") : "";
		if (accessTypeString.equals("0")) {
			imageArrayList = activity.jsonTopNews();
			if (imageArrayList.size() > 0) {
				Log.v("tag", "one");
				String imString = imageArrayList.get(0).get("contentUrl");
				Log.v("NewsFragment", imString);
			}
			ArrayList<NewsEntity> newsEntities = activity.jsonNewsFromWeb(pageNumberString);
			for (int i = 0; i < newsEntities.size(); i++) {
				newsList.add(newsEntities.get(i));
			}
		}
		else if (accessTypeString.equals("1")) {
			ArrayList<NewsEntity> newsEntities = activity.getNewsArrayList(pageNumberString);
			for (int i = 0; i < newsEntities.size(); i++) {
				newsList.add(newsEntities.get(i));
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		inflaterOne = inflater;
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mListView);
		TextView item_textview = (TextView)view.findViewById(R.id.item_textview);
		waittingLinearLayout = (LinearLayout)view.findViewById(R.id.waittingLinearLayout);
		detail_loading = (ImageView)view.findViewById(R.id.detail_loading);
		item_textview.setText(text);
		
		mListView.setMode(Mode.BOTH); 
		mListView.setOnRefreshListener(new MyOnRefreshListener2(mListView));
		
		frameLayout = (FrameLayout)inflaterOne.inflate(R.layout.topnews_image, null);
		viewPager = (AdvViewPager) frameLayout.findViewById(R.id.viewpager);
		ll_point = (LinearLayout) frameLayout.findViewById(R.id.ll_point);
		
		if (accessTypeString.equals("0")) {
			mListView.getRefreshableView().addHeaderView(frameLayout, null, false);//加载viewpager
		}
		else if (accessTypeString.equals("1")) {
			
		}
		
		return view;
	}
	
	class MyOnRefreshListener2 implements OnRefreshListener2<ListView> {

		private PullToRefreshListView mPtflv;

		public MyOnRefreshListener2(PullToRefreshListView ptflv) {
			this.mPtflv = ptflv;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 下拉刷新
			String label = DateUtils.formatDateTime(activity.getApplicationContext(),
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);

			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			new GetBeforeNewsTask(mPtflv).execute();

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 上拉加载
			new GetAfterNewsTask(mPtflv).execute();
		}

	}
	
	/**
	 * 请求前十条网络获得新闻信息
	 * 
	 * @author Louis
	 * 
	 */
	class GetBeforeNewsTask extends AsyncTask<String, Void, Integer> {
		private PullToRefreshListView mPtrlv;
		ArrayList<NewsEntity> newsEntities = new ArrayList<NewsEntity>();
		public GetBeforeNewsTask(PullToRefreshListView ptrlv) {
			this.mPtrlv = ptrlv;
		}

		@Override
		protected Integer doInBackground(String... params) {
//			currentPageString = "1";
//			newsEntities = activity.getNewsArrayList(currentPageString);
//			if (newsEntities.size() > 0) {
//				newsList.removeAll(newsList);
//				for (int i = 0; i < newsEntities.size(); i++) {
//					newsList.add(newsEntities.get(i));
//				}
//			}
			requestUrlHttp("0", "1");
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 1:
				mAdapter = new NewsAdapter(activity, newsList);
				mAdapter.notifyDataSetChanged();
				break;
			case 0:
				Toast.makeText(activity, "请检查网络", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			mPtrlv.onRefreshComplete();
		}

	}
	
	/**
	 * 请求下面十条网络获得新闻信息
	 * 
	 * @author Louis
	 * 
	 */
	class GetAfterNewsTask extends AsyncTask<String, Void, Integer> {
		private PullToRefreshListView mPtrlv;
		ArrayList<NewsEntity> newsEntities = new ArrayList<NewsEntity>();
		public GetAfterNewsTask(PullToRefreshListView ptrlv) {
			this.mPtrlv = ptrlv;
		}

		@Override
		protected Integer doInBackground(String... params) {
			int currentPageInt = Integer.parseInt(currentPageString);
			currentPageInt++;
			currentPageString = String.valueOf(currentPageInt);
//			newsEntities = activity.getNewsArrayList(currentPageString);
//			if (newsEntities.size() > 0) {
//				for (int i = 0; i < newsEntities.size(); i++) {
//					newsList.add(newsEntities.get(i));
//				}
//			}
			requestUrlHttp("1", currentPageString);
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 1:
				mAdapter = new NewsAdapter(activity, newsList);
				mAdapter.notifyDataSetChanged();
				break;
			case 0:
				Toast.makeText(activity, "请检查网络", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			mPtrlv.onRefreshComplete();
		}

	}
	
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
			case 0:
				initHeadImage();
				waittingLinearLayout.setVisibility(View.GONE);
				mAdapter = new NewsAdapter(activity, newsList);
				mListView.setAdapter(mAdapter);
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
		
		if (imageArrayList.size() > 0) {
			ll_point.removeAllViews();//去除上次的
		}
		
		for (i = 0; i < imageArrayList.size(); i++) {//image_id.length
			imageView = new ImageView(activity);
			imageView.setBackgroundResource(R.drawable.indicator);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			ll_point.addView(imageView, layoutParams);

			imageViews.add(imageView);
		}

	}

	/***
	 * 初始化 PagerChild
	 */
	void initPagerChild() {
		arrayList = new ArrayList<View>();
		if (imageArrayList.size() > 0) {
			for (int i = 0; i < imageArrayList.size(); i++) {//image_id.length
				ImageView imageView = new ImageView(activity);
				imageView.setTag(i);
				imageView.setScaleType(ScaleType.FIT_XY);
//				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//						image_id[i]);
//				Bitmap bitmap2 = getBitmap(bitmap, window_width);
//				frameheight = bitmap2.getHeight();// 获取要显示的高度
//				Log.e("jj", "frameheight=" + frameheight);
//				imageView.setImageBitmap(bitmap2);
				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//Toast.makeText(activity, "123", Toast.LENGTH_SHORT).show();
						int tagInt = Integer.parseInt(v.getTag().toString()); 
						if (accessTypeString.equals("0")) {
							Intent newsContentIntent = new Intent(activity,OneNewsContentActivity.class);
							newsContentIntent.putExtra("newsId", imageArrayList.get(tagInt).get("contentUrl"));
							newsContentIntent.putExtra("newsTitle", imageArrayList.get(tagInt).get("imgTitle"));
							newsContentIntent.putExtra("newsDate", "123");
							activity.startActivity(newsContentIntent);
						}
					}
				});
				
				imageLoader.displayImage(imageArrayList.get(i).get("imgUrl"), imageView);
				
				
				arrayList.add(imageView);
			}
			initPoint();
		}
	}

	/***
	 * 初始化 HeadImage
	 */
	void initHeadImage() {
		
//		frameLayout = (FrameLayout)inflaterOne.inflate(R.layout.topnews_image, null);
//		viewPager = (AdvViewPager) frameLayout.findViewById(R.id.viewpager);
//		ll_point = (LinearLayout) frameLayout.findViewById(R.id.ll_point);
		
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
		
		// 默认首次进入
		if (accessTypeString.equals("0")) {
			if (newsList.size()>0) {
				draw_Point(0);
			}
			//draw_Point(0);
			//mListView.getRefreshableView().addHeaderView(frameLayout, null, false);//加载viewpager
		}
		else if (accessTypeString.equals("1")) {
			
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (accessTypeString.equals("0")) {
					int currentPosition = position - 2;
					Intent newsContentIntent = new Intent(activity,OneNewsContentActivity.class);
					newsContentIntent.putExtra("newsId", newsList.get(currentPosition).getNewsId());
					newsContentIntent.putExtra("newsTitle", newsList.get(currentPosition).getTitle());
					newsContentIntent.putExtra("newsDate", newsList.get(currentPosition).getSource());
					activity.startActivity(newsContentIntent);
				}
				else if (accessTypeString.equals("1")) {
					int currentPosition = position - 1;
					if (currentPosition >= 0)
						Toast.makeText(activity, newsList.get(currentPosition).getTitle(), Toast.LENGTH_SHORT)
								.show();
						Log.v(TAG, String.valueOf(currentPosition));
						Intent newsContentIntent = new Intent(activity,NewsContentActivity.class);
						newsContentIntent.putExtra("catagyId", newsList.get(currentPosition).getNewsCategoryId());
						newsContentIntent.putExtra("newsId", newsList.get(currentPosition).getNewsId());
						newsContentIntent.putExtra("newsTitle", newsList.get(currentPosition).getTitle());
						newsContentIntent.putExtra("newsDate", newsList.get(currentPosition).getSource());
						activity.startActivity(newsContentIntent);
				}
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
	 * @author 
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

