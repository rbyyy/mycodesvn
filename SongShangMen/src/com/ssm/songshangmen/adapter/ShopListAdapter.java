package com.ssm.songshangmen.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ssm.songshangmen.R;
import com.ssm.songshangmen.entity.Shop;
import com.ssm.songshangmen.helper.SSMHelper;

public class ShopListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<Shop> mListData;
	private ImageLoader imageLoader;
	
	public ShopListAdapter(Context context, ImageLoader _ImageLoader, ArrayList<Shop> shopsDataList) {
		mInflater = LayoutInflater.from(context);
		imageLoader = _ImageLoader;
		mListData = shopsDataList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListData.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ShopListHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_shoplistview, null);
			holder = new ShopListHolder();
			holder.shopImageView = (ImageView)convertView.findViewById(R.id.shopsImageView);
			holder.shopOneFlagImageView = (ImageView)convertView.findViewById(R.id.shopsFlagImageView);
			holder.shopTwoFlagImageView = (ImageView)convertView.findViewById(R.id.shopsTwoFlagImageView);
			holder.shopTitleTextView = (TextView)convertView.findViewById(R.id.shopTitleTextView);
			holder.shopLevelRatingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);
			holder.shopDistanceTextView = (TextView)convertView.findViewById(R.id.distanceTextView);
			holder.shopFreeSendLimitTextView = (TextView)convertView.findViewById(R.id.freeSendLimitTextView);
			holder.shopSendTimeTextView = (TextView)convertView.findViewById(R.id.sendTimeTextView);
			convertView.setTag(holder);
		}
		else {
			holder = (ShopListHolder)convertView.getTag();
		}
		imageLoader.displayImage(mListData.get(position).getPicture(), holder.shopImageView, SSMHelper.displayOptions());
		holder.shopTitleTextView.setText(mListData.get(position).getName());
		holder.shopLevelRatingBar.setRating(mListData.get(position).getLevel());
		if (mListData.get(position).getDistance() > 1000) {
			double dis = mListData.get(position).getDistance()/1000;
			holder.shopDistanceTextView.setText(String.valueOf(dis) + "公里");
		}
		else {
			holder.shopDistanceTextView.setText(String.valueOf(mListData.get(position).getDistance())+"米");
		}
		
		holder.shopFreeSendLimitTextView.setText("满"+String.valueOf(mListData.get(position).getSendLimit())+
				"元免"+String.valueOf(mListData.get(position).getSendFee())+"元配送费");
		holder.shopSendTimeTextView.setText(mListData.get(position).getSendTime()+"分钟");

		return convertView;
	}
	
	static class ShopListHolder{
		ImageView  	shopImageView;
		ImageView  	shopOneFlagImageView;
		ImageView  	shopTwoFlagImageView;
		TextView   	shopTitleTextView;
		RatingBar  	shopLevelRatingBar;
		TextView	shopDistanceTextView;
		TextView   	shopFreeSendLimitTextView;	
		TextView   	shopSendTimeTextView;
	}

}