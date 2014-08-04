package com.ssm.songshangmen.adapter;

import java.util.ArrayList;

import com.ssm.songshangmen.R;
import com.ssm.songshangmen.entity.GoodsType;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenusListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<GoodsType> mListData;
	private Context context;
	
	public MenusListAdapter(Context context, ArrayList<GoodsType> menusDataList) {
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
		MenusListHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_menuslist, null);
			holder = new MenusListHolder();
			holder.menuNumberTextView = (TextView)convertView.findViewById(R.id.menusNumberTextView);
			holder.menusNameTextView = (TextView)convertView.findViewById(R.id.menusTextView);
			convertView.setTag(holder);
		}
		else {
			holder = (MenusListHolder)convertView.getTag();
		}
		if (position == 0) {
			int color = context.getResources().getColor(R.color.dish_menu_selected);
			convertView.setBackgroundColor(color);
			holder.menuNumberTextView.setTextColor(Color.WHITE);
			holder.menusNameTextView.setTextColor(Color.WHITE);
		}
		
		holder.menuNumberTextView.setText(String.valueOf(mListData.get(position).getNumber()));
		holder.menusNameTextView.setText(mListData.get(position).getName().toString());
		return convertView;
	}
	
	static class MenusListHolder{	
		TextView   menuNumberTextView;
		TextView   menusNameTextView;
	}

}
