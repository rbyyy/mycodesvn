package com.ssm.songshangmen.adapter;

import java.util.ArrayList;

import com.ssm.songshangmen.R;
import com.ssm.songshangmen.activity.ActivityCallBridge;
import com.ssm.songshangmen.adapter.DishesListAdapter.DishesListHolder;
import com.ssm.songshangmen.entity.OrderGoods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class OrderGoodsListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<OrderGoods> mListData;
	private ActivityCallBridge mBridge;
	
	public OrderGoodsListAdapter(Context context, ArrayList<OrderGoods> orderGoodsDataList) {
		mInflater = LayoutInflater.from(context);
		mListData = orderGoodsDataList;
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
		final OrderGoodsListHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_ordergoodslist, null);
			holder = new OrderGoodsListHolder();
			holder.orderGoodsNameTextView = (TextView)convertView.findViewById(R.id.orderGoodsNameTextView);
			holder.orderGoodsMinusImageButton = (ImageButton)convertView.findViewById(R.id.minusImageButton);
			holder.orderGoodsNumberTextView = (TextView)convertView.findViewById(R.id.orderGoodsNumberTextView);
			holder.orderGoodsPlusImageButton = (ImageButton)convertView.findViewById(R.id.plusImageButton);
			holder.orderGoodsPriceTextView = (TextView)convertView.findViewById(R.id.orderGoodsPriceTextView);
			holder.orderGoodsItemIdTextView = (TextView)convertView.findViewById(R.id.ItemId);
			convertView.setTag(holder);
		}
		else {
			holder = (OrderGoodsListHolder)convertView.getTag();
		}
		holder.orderGoodsNameTextView.setText(mListData.get(position).getName());
		if (mListData.get(position).getGoodsNumber() > 0) {
			holder.orderGoodsNumberTextView.setText(String.valueOf(mListData.get(position).getGoodsNumber()));
		}
		holder.orderGoodsMinusImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				int dishesNumber = mListData.get(position).getGoodsNumber();
//				dishesNumber--;
				mBridge = ActivityCallBridge.getInstance();//
		        mBridge.invokeMethod(0, -1, position);
//		        mListData.get(position).setGoodsNumber(dishesNumber);
//				if (dishesNumber <= 0) {
//					holder.orderGoodsMinusImageButton.setVisibility(View.INVISIBLE);
//					holder.orderGoodsNumberTextView.setVisibility(View.INVISIBLE);
//					holder.orderGoodsNumberTextView.setText(String.valueOf(dishesNumber));
//				}
//				else {
//					holder.orderGoodsNumberTextView.setText(String.valueOf(dishesNumber));
//				}
			}
		});
		holder.orderGoodsPlusImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				int dishesNumber = mListData.get(position).getGoodsNumber();
//				dishesNumber++;
				mBridge = ActivityCallBridge.getInstance();//
		        mBridge.invokeMethod(0, 1, position);
//		        mListData.get(position).setGoodsNumber(dishesNumber);
//				holder.orderGoodsMinusImageButton.setVisibility(View.VISIBLE);
//				holder.orderGoodsNumberTextView.setVisibility(View.VISIBLE);
//				holder.orderGoodsNumberTextView.setText(String.valueOf(dishesNumber));
			}
		});
		holder.orderGoodsPriceTextView.setText("￥"+String.valueOf(mListData.get(position).getPrice()));
		//holder.orderGoodsItemIdTextView.setText(mListData.get(position).getShopId());
		
		return convertView;
	}
	
	static class OrderGoodsListHolder{	
		TextView    orderGoodsNameTextView;
		ImageButton	orderGoodsMinusImageButton;
		TextView	orderGoodsNumberTextView;
		ImageButton	orderGoodsPlusImageButton;
		TextView	orderGoodsPriceTextView;
		TextView	orderGoodsItemIdTextView;
	}

}
