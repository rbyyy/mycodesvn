package com.ssm.ssmbee.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ssm.ssmbee.R;
import com.ssm.ssmbee.entity.OrderMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MenuExpandableListAdapter extends BaseExpandableListAdapter {

	private ArrayList<String> groupList;
	private ArrayList<List<OrderMenu>> childList;
	
	private Context context;
	private LayoutInflater inflater;

	public MenuExpandableListAdapter(Context context, ArrayList<String> groupListOne, ArrayList<List<OrderMenu>> childListOne) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		groupList = groupListOne;
		childList = childListOne;
	}
	// 返回父列表个数
	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	// 返回子列表个数
	@Override
	public int getChildrenCount(int groupPosition) {
		return childList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return groupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			groupHolder = new GroupHolder();
			convertView = inflater.inflate(R.layout.group, null);
			groupHolder.textView = (TextView) convertView
					.findViewById(R.id.group);
			groupHolder.imageView = (ImageView) convertView
					.findViewById(R.id.image);
			groupHolder.textView.setTextSize(15);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		groupHolder.textView.setText(getGroup(groupPosition).toString());
		if (isExpanded)// ture is Expanded or false is not isExpanded
			groupHolder.imageView.setImageResource(R.drawable.expanded);
		else
			groupHolder.imageView.setImageResource(R.drawable.collapse);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item, null);
		}
		TextView shopNameTextView = (TextView) convertView.findViewById(R.id.shopNameTextView);
		shopNameTextView.setTextSize(13);
		shopNameTextView.setText(((OrderMenu)getChild(groupPosition, childPosition)).getShopName());
		TextView shopAddressTextView = (TextView) convertView.findViewById(R.id.shopAddressTextView);
		shopAddressTextView.setTextSize(13);
		shopAddressTextView.setText(((OrderMenu)getChild(groupPosition, childPosition)).getShopAddress());
		TextView shopPhoneTextView = (TextView) convertView.findViewById(R.id.shopPhoneTextView);
		shopPhoneTextView.setTextSize(13);
		shopPhoneTextView.setText(((OrderMenu)getChild(groupPosition, childPosition)).getShopPhone());
		TextView buyNameTextView = (TextView) convertView.findViewById(R.id.buyNameTextView);
		buyNameTextView.setTextSize(13);
		buyNameTextView.setText(((OrderMenu)getChild(groupPosition, childPosition)).getBuyName());
		TextView buyAddressTextView = (TextView) convertView.findViewById(R.id.buyAddressTextView);
		buyAddressTextView.setTextSize(13);
		buyAddressTextView.setText(((OrderMenu)getChild(groupPosition, childPosition)).getBuyAddress());
		TextView buyPhoneTextView = (TextView) convertView.findViewById(R.id.buyPhoneTextView);
		buyPhoneTextView.setTextSize(13);
		buyPhoneTextView.setText(((OrderMenu)getChild(groupPosition, childPosition)).getBuyPhone());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean onGroupClick(final ExpandableListView parent, final View v, int groupPosition, final long id) {

		return false;
	}

	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Toast.makeText(context, childList.get(groupPosition).get(childPosition).getShopName(), Toast.LENGTH_SHORT).show();
		return false;
	}

	class GroupHolder {
		TextView textView;
		ImageView imageView;
	}


}
