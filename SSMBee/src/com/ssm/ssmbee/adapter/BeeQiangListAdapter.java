package com.ssm.ssmbee.adapter;

import java.util.ArrayList;

import com.ssm.ssmbee.BeeQiangCallBridge;
import com.ssm.ssmbee.R;
import com.ssm.ssmbee.entity.OrderMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BeeQiangListAdapter extends BaseAdapter {
	
	private Context 				context;
	private LayoutInflater 			inflater;
	private	ArrayList<OrderMenu>	orderMenuDataList;
	private BeeQiangCallBridge 		mBridge;

	public BeeQiangListAdapter(Context context, ArrayList<OrderMenu> dataList) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		orderMenuDataList = dataList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orderMenuDataList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		OrderMenuHolder holder;
		if (convertView == null) {
			holder = new OrderMenuHolder();
			convertView = inflater.inflate(R.layout.item_grablistview, null);
			holder.beeQiangImageView = (ImageView)convertView.findViewById(R.id.beeQiangImageView);
			holder.shopNameTextView = (TextView)convertView.findViewById(R.id.shopNameTextView);
			holder.shopAddressTextView = (TextView)convertView.findViewById(R.id.shopAddressTextView);
			holder.shopPhoneTextView = (TextView)convertView.findViewById(R.id.shopPhoneTextView);
			holder.buyNameTextView = (TextView)convertView.findViewById(R.id.buyNameTextView);
			holder.buyAddressTextView = (TextView)convertView.findViewById(R.id.buyAddressTextView);
			holder.buyPhoneTextView = (TextView)convertView.findViewById(R.id.buyPhoneTextView);
			convertView.setTag(holder);
		}
		else {
			holder = (OrderMenuHolder)convertView.getTag();
		}
		holder.beeQiangImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBridge = BeeQiangCallBridge.getInstance();
		        mBridge.invokeMethod( position, orderMenuDataList.get(position).getOrderId());
			}
		});
		holder.shopNameTextView.setText(orderMenuDataList.get(position).getShopName());
		holder.shopAddressTextView.setText(orderMenuDataList.get(position).getShopAddress());
		holder.shopPhoneTextView.setText(orderMenuDataList.get(position).getShopPhone());
		holder.buyNameTextView.setText(orderMenuDataList.get(position).getBuyName());
		holder.buyAddressTextView.setText(orderMenuDataList.get(position).getBuyAddress());
		holder.buyPhoneTextView.setText(orderMenuDataList.get(position).getBuyPhone());
		
		return convertView;
	}
	
	class OrderMenuHolder{
		ImageView		beeQiangImageView;
		TextView		shopNameTextView;
		TextView		shopAddressTextView;
		TextView		shopPhoneTextView;
		TextView		buyNameTextView;
		TextView		buyAddressTextView;
		TextView		buyPhoneTextView;
	}

}
