package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MultimediaInfoActivity extends BaseActivity {
	
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**多媒体信息列表*/
	private ListView		multimediaInfoListView;
	/**多媒体信息*/
	private ArrayList<HashMap<String, String>> multimediaInfoArrayList;
	/**多媒体适配器*/
	private MyMultimediaInfoListAdapter 	   mMyMultimediaInfoListAdapter;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multimediainfo);
		initView();
		multimediaInfoArrayList = new ArrayList<HashMap<String,String>>();
		new Thread()
		{
			public void run() {
				queryMultimediaInfo("0");
			}
		}.start();
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		topTitleTextView.setText("多媒体信息");//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MultimediaInfoActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
		multimediaInfoListView = (ListView)findViewById(R.id.multimediaInfoListView);
	}
	/**多媒体信息*/
	protected void queryMultimediaInfo(String pageNumberString) {
		try {			
			HttpResponse aString = gosHttpOperation.invokerVideoNewsResponse(pageNumberString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String stateString = jsonObject.getString("state");
				if (stateString.equals("1")) {
					Log.v("测试视频信息", "测试视频信息成功");
					JSONArray responeArray = jsonObject.getJSONArray("responseList");
					
					if (responeArray.size() > 0) {
						for (int i = 0; i < responeArray.size(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							JSONObject oneObject = responeArray.getJSONObject(i);
							String userIDString = oneObject.getString("title").trim();
							map.put("title", userIDString);
							String dateString = oneObject.getString("dateandtime").trim();
							map.put("dateandtime", dateString);
							String videoUrlString = oneObject.getString("movURL").trim();
							map.put("movURL", videoUrlString);
							multimediaInfoArrayList.add(map);
						}
					}
					videoMsgHandler.sendEmptyMessage(1);
				}
				else
				{
					Log.v("测试视频信息", "测试视频信息失败");
					videoMsgHandler.sendEmptyMessage(0);
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
	/**信息显示handler*/
	@SuppressLint("HandlerLeak")
	Handler videoMsgHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				multiMediaInfoShow("0");
				break;
			case 1:
				multiMediaInfoShow("1");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**多媒体信息展示*/
	protected void multiMediaInfoShow(String flagString) {
		if (flagString.equals("0")) {
			
		}
		else {
			mMyMultimediaInfoListAdapter = new MyMultimediaInfoListAdapter();
			multimediaInfoListView.setAdapter(mMyMultimediaInfoListAdapter);
		}
	}
	/**adapter*/
	private class MyMultimediaInfoListAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return multimediaInfoArrayList.size();
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
			multimediaInfoListViewHolder holder;
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.list_multimediainfoitem, null);
				holder = new multimediaInfoListViewHolder();
				//取到各个控件的对象
				 holder.titleTextView = (TextView)convertView.findViewById(R.id.multimediaInfoTextView);//配件名称
				 holder.dateTextView = (TextView)convertView.findViewById(R.id.multimediaInfoDateTextView);//配件状态
				 convertView.setTag(holder);
			}
			else {
				holder = (multimediaInfoListViewHolder)convertView.getTag();
			}
			//标题
			holder.titleTextView.setText((String)multimediaInfoArrayList.get(position).get("title"));
			//日期
			holder.dateTextView.setText((String)multimediaInfoArrayList.get(position).get("dateandtime"));
			
			//为每个item添加click事件
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent oneIntent = new Intent(MultimediaInfoActivity.this, MultimediaVideoActivity.class);
					oneIntent.putExtra("title", (String)multimediaInfoArrayList.get(position).get("title"));
					oneIntent.putExtra("videoUrl", (String)multimediaInfoArrayList.get(position).get("movURL"));
					MultimediaInfoActivity.this.startActivity(oneIntent);
				}
			});
			return convertView;
		}
	}
	//配件列表控件类
	/**存放控件*/
   public final class multimediaInfoListViewHolder{
       public TextView 			titleTextView;
       public TextView			dateTextView;
   }
	   
	
}
