package com.gov.nzjcy.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.gov.nzjcy.NewsContentActivity;
import com.gov.nzjcy.R;
import com.gov.nzjcy.entity.NewsEntity;
import com.gov.nzjcy.constants.Constants;
import com.gov.nzjcy.constants.Options;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	private String	TAG = "NewsAdapter";
	ArrayList<NewsEntity> newsList;
	Activity activity;
	LayoutInflater inflater = null;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	public NewsAdapter(Activity activity, ArrayList<NewsEntity> newsList) {
		this.activity = activity;
		this.newsList = newsList;
		inflater = LayoutInflater.from(activity);
		options = Options.getListOptions();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsList == null ? 0 : newsList.size();
	}

	@Override
	public NewsEntity getItem(int position) {
		// TODO Auto-generated method stub
		if (newsList != null && newsList.size() != 0) {
			return newsList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.list_item, null);
			mHolder = new ViewHolder();
			mHolder.item_layout = (RelativeLayout)view.findViewById(R.id.item_layout);
			mHolder.item_title = (TextView)view.findViewById(R.id.item_title);
			mHolder.item_source = (TextView)view.findViewById(R.id.item_source);
			mHolder.alt_mark = (ImageView)view.findViewById(R.id.alt_mark);
			mHolder.right_image = (ImageView)view.findViewById(R.id.right_image);			
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		//获取position对应的数据
		NewsEntity news = getItem(position);
		mHolder.item_title.setText(news.getTitle());
		mHolder.item_source.setText(news.getSource());
		List<String> imgUrlList = news.getPicList();
		if(imgUrlList !=null && imgUrlList.size() !=0 && !imgUrlList.get(0).toString().equals("0")){
			if(imgUrlList.size() == 1){
				mHolder.right_image.setVisibility(View.VISIBLE);
				imageLoader.displayImage(imgUrlList.get(0), mHolder.right_image, options);
			}else{
				mHolder.right_image.setVisibility(View.GONE);
			}
		}else{
			mHolder.right_image.setVisibility(View.GONE);
		}
//		int markResID = getAltMarkResID(news.getMark(),news.getCollectStatus());
//		if(markResID != -1){
//			mHolder.alt_mark.setVisibility(View.VISIBLE);
//			mHolder.alt_mark.setImageResource(markResID);
//		}else{
//			mHolder.alt_mark.setVisibility(View.GONE);
//		}
		
//		view.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Log.v(TAG, String.valueOf(position));
//				Intent newsContentIntent = new Intent(activity,NewsContentActivity.class);
//				activity.startActivity(newsContentIntent);
//			}
//		});

		return view;
	}

	static class ViewHolder {
		RelativeLayout item_layout;
		//title
		TextView item_title;
		//图片源
		TextView item_source;
		//右上方TAG标记图片
		ImageView alt_mark;
		//右边图片
		ImageView right_image;
	}
	/** 根据属性获取对应的资源ID  */
	public int getAltMarkResID(int mark,boolean isfavor){
		if(isfavor){
			return R.drawable.ic_mark_favor;
		}
		switch (mark) {
		case Constants.mark_recom:
			return R.drawable.ic_mark_recommend;
		case Constants.mark_hot:
			return R.drawable.ic_mark_hot;
		case Constants.mark_frist:
			return R.drawable.ic_mark_first;
		case Constants.mark_exclusive:
			return R.drawable.ic_mark_exclusive;
		case Constants.mark_favor:
			return R.drawable.ic_mark_favor;
		default:
			break;
		}
		return -1;
	}
	
}
