package com.law.fangyuan.modify;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AdvViewPager extends ViewPager {
	
	float mDownX = 0;
	float mDownY = 0;
	
	public AdvViewPager(Context context) {
		super(context);
	}	
	
	public AdvViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
//		switch (arg0.getActionMasked()) {
//		case MotionEvent.ACTION_MOVE:
//			getParent().requestDisallowInterceptTouchEvent(true);
//			break;
//		case MotionEvent.ACTION_UP:
//			getParent().requestDisallowInterceptTouchEvent(true);
//			break;
//		case MotionEvent.ACTION_DOWN:
//			getParent().requestDisallowInterceptTouchEvent(true);
//			break;
//		default:
//			break;
//		}
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(arg0);
	}
	
//	@Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }
	
}
