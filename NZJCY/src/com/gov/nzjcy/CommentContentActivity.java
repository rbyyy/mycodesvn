package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.adapter.NewsAdapter;
import com.gov.nzjcy.entity.NewsCommentEntity;
import com.gov.nzjcy.entity.NewsEntity;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class CommentContentActivity extends BaseActivity {
	
	/**打印标示*/
	private String	TAG = "CommentContentActivity";
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout					top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton						top_right_headImageButton;
	/**head的标题*/
	private TextView						topTitleTextView;
	/**新闻id*/
	private String							newsIdsString;
	/**新闻列表*/
	private PullToRefreshListView   		commentContentPullToRefreshListView;
	/**新闻评论内容*/
	private ArrayList<NewsCommentEntity>	commentContentArrayList;
	/**新闻评论内容数据源*/
	private CommentContentListAdapter		commentContentAdapter;
	/**当前页数*/
	private	String							currentPageString = "1";
	/**等待*/
	private LinearLayout					commnetcontentLinearLayout;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commentcontent);
		initTopTitle();
		newsIdsString = getIntent().getStringExtra("newsIdStr");
		new Thread()
		{
			public void run() {
				commentContentArrayList = requestNewsCommentByNewsId(newsIdsString, "1");
				connectHanlder.sendEmptyMessage(0);
			}
		}.start();
		
		initCommentView();
	}
	//初始化标题栏
	protected void initTopTitle() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		//String newsTitleString = getIntent().getStringExtra("newstitle");
		topTitleTextView.setText("全部评论");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommentContentActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		//右边登录按钮的点击事件
//			top_right_headImageButton.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//				}
//			});
		commnetcontentLinearLayout = (LinearLayout)findViewById(R.id.commnetcontentLinearLayout);
		commentContentArrayList = new ArrayList<NewsCommentEntity>();
	}
	//初始化新闻评论列表
	protected void initCommentView() {
		commentContentPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.commentContentPullToRefreshListView);
		commentContentPullToRefreshListView.setMode(Mode.BOTH);
		commentContentPullToRefreshListView.setOnRefreshListener(new MyOnRefreshListenerOne(commentContentPullToRefreshListView));
	}
	//
	class MyOnRefreshListenerOne implements OnRefreshListener2<ListView> {

		private PullToRefreshListView mPtflv;

		public MyOnRefreshListenerOne(PullToRefreshListView ptflv) {
			this.mPtflv = ptflv;
		}

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// 下拉刷新
			String label = DateUtils.formatDateTime(getApplicationContext(),
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
	 * 请求前十条新闻评论
	 * 
	 * GetBeforeNewsTask
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
			currentPageString = "1";
			ArrayList<NewsCommentEntity> newsCommentEntities = requestNewsCommentByNewsId(newsIdsString , currentPageString);
			if (newsCommentEntities.size() > 0) {
				commentContentArrayList.removeAll(commentContentArrayList);
				for (int i = 0; i < newsCommentEntities.size(); i++) {
					commentContentArrayList.add(newsCommentEntities.get(i));
				}
			}
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 1:
				connectHanlder.sendEmptyMessage(0);
				break;
			case 0:
				Toast.makeText(CommentContentActivity.this, "请检查网络", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			mPtrlv.onRefreshComplete();
		}

	}
	
	/**
	 * 请求后十条新闻评论
	 * 
	 * GetAfterNewsTask
	 * 
	 */
	class GetAfterNewsTask extends AsyncTask<String, Void, Integer> {
		private PullToRefreshListView mPtrlv;
		ArrayList<NewsCommentEntity> newsCommentEntities = new ArrayList<NewsCommentEntity>();
		public GetAfterNewsTask(PullToRefreshListView ptrlv) {
			this.mPtrlv = ptrlv;
		}

		@Override
		protected Integer doInBackground(String... params) {
			int currentPageInt = Integer.parseInt(currentPageString);
			currentPageInt++;
			currentPageString = String.valueOf(currentPageInt);
			newsCommentEntities = requestNewsCommentByNewsId(newsIdsString , currentPageString);
			if (newsCommentEntities.size() > 0) {
				for (int i = 0; i < newsCommentEntities.size(); i++) {
					commentContentArrayList.add(newsCommentEntities.get(i));
				}
			}
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 1:
				connectHanlder.sendEmptyMessage(0);
				break;
			case 0:
				Toast.makeText(CommentContentActivity.this, "请检查网络", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			mPtrlv.onRefreshComplete();
		}

	}
	
	/**获取新闻评论*/
	protected ArrayList<NewsCommentEntity> requestNewsCommentByNewsId(String newsIdString, String pageNumberString) {
		ArrayList<NewsCommentEntity> newsCommentEntities = new ArrayList<NewsCommentEntity>();
		try {
			HttpResponse aString = gosHttpOperation.invokerGetCommentResponseByNewsId(newsIdString, pageNumberString);
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
							NewsCommentEntity newsComment = new NewsCommentEntity();
							newsComment.setCommentArticleId(oneObject.getString("articleID").trim());
							newsComment.setCommentUserId(oneObject.getString("peopleID").trim());
							newsComment.setCommentUserName(oneObject.getString("username").trim());
							newsComment.setCommentDescription(oneObject.getString("description").trim());
							newsComment.setCommentDateAndTime(oneObject.getString("dateandtime").trim());
							newsComment.setCommentState(oneObject.getString("state").trim());
							newsCommentEntities.add(newsComment);
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
		
		return newsCommentEntities;
				
	}
	//新闻内容handler主线程
    private Handler connectHanlder = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            Log.d(TAG, "display view");  
            commnetcontentLinearLayout.setVisibility(View.INVISIBLE);
            // 更新UI，显示图片  
            setNewsCommentContent();
        }  
    };
	//新闻评论内容的显示
	protected void setNewsCommentContent() {
		commentContentAdapter = new CommentContentListAdapter();
		commentContentPullToRefreshListView.setAdapter(commentContentAdapter);
	}
	//新闻评论内容数据源
	@SuppressLint("SimpleDateFormat")
	private class CommentContentListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commentContentArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			CommentContentListViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.commentlist_item, null);
				holder = new CommentContentListViewHolder();
				//取到各个控件的对象
				 holder.usernameTitle = (TextView)convertView.findViewById(R.id.usernameTextView);//用户名
				 holder.dateandtimeTitle = (TextView)convertView.findViewById(R.id.datetimeTextView);//日期
				 holder.commentContentTitle = (TextView)convertView.findViewById(R.id.newsCommentContentTextView);//评论内容
				 convertView.setTag(holder);
			}
			else {
				holder = (CommentContentListViewHolder)convertView.getTag();
			}
			//用户名
			holder.usernameTitle.setText(commentContentArrayList.get(position).getCommentUserName());
			//日期
			String dtStart = commentContentArrayList.get(position).getCommentDateAndTime();
			String[] dateStrings = dtStart.split("\\.");
			holder.dateandtimeTitle.setText(dateStrings[0]);
			//评论内容
			holder.commentContentTitle.setText(commentContentArrayList.get(position).getCommentDescription());
			
			return convertView;
		}
	}
	//配件列表控件类
	/**存放控件*/
   public final class CommentContentListViewHolder{
       public TextView 			usernameTitle;
       public TextView			dateandtimeTitle;
       public TextView 			commentContentTitle;
   }
  
}
