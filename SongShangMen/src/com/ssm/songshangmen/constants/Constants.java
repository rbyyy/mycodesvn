package com.ssm.songshangmen.constants;

import com.ssm.songshangmen.R;

import com.ssm.songshangmen.activity.FirstViewActivity;
import com.ssm.songshangmen.activity.FourViewActivity;
import com.ssm.songshangmen.activity.SecondViewActivity;
import com.ssm.songshangmen.activity.ThreeViewActivity;


public final class Constants {
	
	public static String mTextviewArray[] = {"首页", "我的", "设置", "更多"};
	//定义数组来存放按钮图片
	public static int mImageViewArray[] = {R.drawable.tab_item_firstview, R.drawable.tab_item_secondview,
		R.drawable.tab_item_threeview, R.drawable.tab_item_fourview};
	
	public static Class<?> mTabClassArray[]= {
		FirstViewActivity.class,
		SecondViewActivity.class,
		ThreeViewActivity.class,
		FourViewActivity.class
	};
	
	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
}
