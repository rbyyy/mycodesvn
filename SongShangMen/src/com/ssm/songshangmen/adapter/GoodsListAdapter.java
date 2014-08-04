package com.ssm.songshangmen.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ssm.songshangmen.R;
import com.ssm.songshangmen.entity.Goods;
import com.ssm.songshangmen.helper.SSMHelper;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<Goods> mListData;
	private ImageLoader imageLoader;
	
	public GoodsListAdapter(Context context, ImageLoader _ImageLoader, ArrayList<Goods> goodsDataList) {
		mInflater = LayoutInflater.from(context);
		imageLoader = _ImageLoader;
		mListData = goodsDataList;
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
		GoodsListHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_goodslistview, null);
			holder = new GoodsListHolder();
			holder.goodsImageView = (ImageView)convertView.findViewById(R.id.goodsImageView);
			holder.goodsOneFlagImageView = (ImageView)convertView.findViewById(R.id.goodsFlagImageView);
			holder.goodsTwoFlagImageView = (ImageView)convertView.findViewById(R.id.goodsTwoFlagImageView);
			holder.goodsTitleTextView = (TextView)convertView.findViewById(R.id.titleTextView);
			holder.goodsContentTextView = (TextView)convertView.findViewById(R.id.contentTextView);
			holder.goodsCouponPriceTextView = (TextView)convertView.findViewById(R.id.couponPriceTextView);
			holder.goodsRealPriceTextView = (TextView)convertView.findViewById(R.id.realPriceTextView);
			holder.goodsDescTextView = (TextView)convertView.findViewById(R.id.orderDescTextView);
			convertView.setTag(holder);
		}
		else {
			holder = (GoodsListHolder)convertView.getTag();
		}
		imageLoader.displayImage(mListData.get(position).getPicture(), holder.goodsImageView, SSMHelper.displayOptions());
		holder.goodsTitleTextView.setText(mListData.get(position).getName());
		holder.goodsContentTextView.setText(mListData.get(position).getDescription());
		holder.goodsCouponPriceTextView.setText(String.valueOf(mListData.get(position).getSalePrice()));
		holder.goodsRealPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
		holder.goodsRealPriceTextView.setText(String.valueOf(mListData.get(position).getPrice()));
		holder.goodsDescTextView.setText(mListData.get(position).getDescription());
		return convertView;
	}
	
	static class GoodsListHolder{
		ImageView goodsImageView;
		ImageView goodsOneFlagImageView;
		ImageView goodsTwoFlagImageView;
		TextView  goodsTitleTextView;
		TextView  goodsContentTextView;
		TextView  goodsCouponPriceTextView;	
		TextView  goodsRealPriceTextView;
		TextView  goodsDescTextView;
	}

}
