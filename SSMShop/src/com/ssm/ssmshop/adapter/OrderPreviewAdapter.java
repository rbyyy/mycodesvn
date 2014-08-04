package com.ssm.ssmshop.adapter;

import java.util.ArrayList;

import com.ssm.ssmshop.entity.OrderPreview;

import com.ssm.ssmshop.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderPreviewAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<OrderPreview> mListData;
	private Context context;
	
	public OrderPreviewAdapter(Context context, ArrayList<OrderPreview> menusDataList) {
		mInflater = LayoutInflater.from(context);
		mListData = menusDataList;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListData.size();
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
		OrderPreviewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_ordercountlist, null);
			holder = new OrderPreviewHolder();
			holder.buyNameTextView = (TextView)convertView.findViewById(R.id.buyNameTextView);
			holder.buyAddressTextView = (TextView)convertView.findViewById(R.id.buyAddressTextView);
			holder.buyPhoneTextView = (TextView)convertView.findViewById(R.id.buyPhoneTextView);
			holder.totalPriceTextView = (TextView)convertView.findViewById(R.id.totalPriceTextView);
			convertView.setTag(holder);
		}
		else {
			holder = (OrderPreviewHolder)convertView.getTag();
		}
		holder.buyNameTextView.setText(mListData.get(position).getBuyName());
		holder.buyAddressTextView.setText("地址:"+mListData.get(position).getBuyAddress());
		holder.buyPhoneTextView.setText("电话:"+mListData.get(position).getBuyPhone());
		holder.totalPriceTextView.setText("总价:"+mListData.get(position).getBuyTotalPrice()+"元");
		
		return convertView;
	}
	/**holder*/
	static class OrderPreviewHolder{
		TextView		buyNameTextView;
		TextView		buyAddressTextView;
		TextView		buyPhoneTextView;
		TextView		totalPriceTextView;
	}

}
