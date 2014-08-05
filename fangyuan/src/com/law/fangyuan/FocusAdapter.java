package com.law.fangyuan;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class FocusAdapter extends PagerAdapter {
	private ArrayList<View> listViews;
	
	public FocusAdapter(ArrayList<View> list){
		listViews = list;
	}
	// 显示数目
	@Override
	public int getCount() {
		return listViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		((ViewPager) arg0).removeView(listViews.get(arg1));
	}

	// 获取每一个item， 
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(listViews.get(arg1));
		return listViews.get(arg1);
	}
}
