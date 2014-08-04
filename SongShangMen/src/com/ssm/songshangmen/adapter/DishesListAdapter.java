package com.ssm.songshangmen.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ssm.songshangmen.R;
import com.ssm.songshangmen.activity.ActivityCallBridge;
import com.ssm.songshangmen.entity.OrderGoods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class DishesListAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private ArrayList<OrderGoods> mListData;
	private ImageLoader imageLoader;
	private int			dishesNumberInt = 0;
	private ActivityCallBridge mBridge;
	private int menuTypeInt;

	public DishesListAdapter(Context context, ImageLoader _ImageLoader, ArrayList<OrderGoods> shopsDataList, int menuTypeSelect) {
		mInflater = LayoutInflater.from(context);
		imageLoader = _ImageLoader;
		mListData = shopsDataList;
		menuTypeInt = menuTypeSelect;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final DishesListHolder	holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_disheslist, null);
			holder = new DishesListHolder();
			holder.dishesNameTextView = (TextView)convertView.findViewById(R.id.dishesNameTextView);
			holder.dishesPriceTextView = (TextView)convertView.findViewById(R.id.dishesPriceTextView);
			holder.dishesMinusImageButton = (ImageButton)convertView.findViewById(R.id.minusImageButton);
			holder.dishesNumberTextView = (TextView)convertView.findViewById(R.id.dishesNumberTextView);
			holder.dishesPlusImageButton = (ImageButton)convertView.findViewById(R.id.plusImageButton);
			holder.dishesItemIdTextView = (TextView)convertView.findViewById(R.id.ItemId);
			convertView.setTag(holder);
		}
		else {
			holder = (DishesListHolder)convertView.getTag();
		}
		holder.dishesNameTextView.setText(mListData.get(position).getName());
		holder.dishesPriceTextView.setText(String.valueOf(mListData.get(position).getPrice()));
		holder.dishesItemIdTextView.setText(String.valueOf(mListData.get(position).getGoodsId()));
		if (mListData.get(position).getGoodsNumber() >= 0) {
			holder.dishesMinusImageButton.setVisibility(View.VISIBLE);
			holder.dishesNumberTextView.setVisibility(View.VISIBLE);
			holder.dishesNumberTextView.setText(String.valueOf(mListData.get(position).getGoodsNumber()));
		}
	    holder.dishesMinusImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int dishesNumber = mListData.get(position).getGoodsNumber();
				dishesNumber--;
				mBridge = ActivityCallBridge.getInstance();//
		        mBridge.invokeMethod(menuTypeInt, -1, position);
				mListData.get(position).setGoodsNumber(dishesNumber);
				if (dishesNumber <= 0) {
					holder.dishesMinusImageButton.setVisibility(View.INVISIBLE);
					holder.dishesNumberTextView.setVisibility(View.INVISIBLE);
					holder.dishesNumberTextView.setText(String.valueOf(dishesNumber));
				}
				else {
					holder.dishesNumberTextView.setText(String.valueOf(dishesNumber));
				}
			}
		});
	    holder.dishesPlusImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int dishesNumber = mListData.get(position).getGoodsNumber();
				dishesNumber++;
				mBridge = ActivityCallBridge.getInstance();//
		        mBridge.invokeMethod(menuTypeInt, 1, position);
				mListData.get(position).setGoodsNumber(dishesNumber);
				holder.dishesMinusImageButton.setVisibility(View.VISIBLE);
				holder.dishesNumberTextView.setVisibility(View.VISIBLE);
				holder.dishesNumberTextView.setText(String.valueOf(dishesNumber));
			}
		});
		return convertView;
	}
	
	static class DishesListHolder{	
		TextView    dishesNameTextView;
		TextView	dishesPriceTextView;
		ImageButton	dishesMinusImageButton;
		TextView	dishesNumberTextView;
		ImageButton	dishesPlusImageButton;
		TextView	dishesItemIdTextView;
	}

}
