package com.ssm.ssmshop.adapter;

import java.util.ArrayList;

import com.ssm.ssmshop.R;
import com.ssm.ssmshop.entity.Goods;
import com.ssm.ssmshop.entity.OrderPreview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderDetailAdapter extends BaseAdapter {

	private LayoutInflater 		mInflater;
	private	ArrayList<Goods> 	mDataList;
	
	public OrderDetailAdapter(Context context, ArrayList<Goods> goodsDataList) {
		mInflater = LayoutInflater.from(context);
		mDataList = goodsDataList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		OrderDetailHolder holder;
		if (convertView == null) {
			holder = new OrderDetailHolder();
			convertView = mInflater.inflate(R.layout.item_shoporderdetaillist, null);
			holder.dishNameTextView = (TextView)convertView.findViewById(R.id.dishNameTextView);
			holder.dishNumberTextView = (TextView)convertView.findViewById(R.id.dishNumberTextView);
			holder.dishPriceTextView = (TextView)convertView.findViewById(R.id.dishPriceTextView);
			convertView.setTag(holder);
		}
		else {
			holder = (OrderDetailHolder)convertView.getTag();
		}
		holder.dishNameTextView.setText(mDataList.get(position).getName());
		holder.dishNumberTextView.setText(mDataList.get(position).getNumber());
		holder.dishPriceTextView.setText(mDataList.get(position).getPrice());
		
		return convertView;
	}
	
	class OrderDetailHolder{
		public		TextView	dishNameTextView;
		public		TextView	dishNumberTextView;
		public		TextView	dishPriceTextView;
	}

}
