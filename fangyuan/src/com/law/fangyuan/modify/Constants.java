package com.law.fangyuan.modify;

import java.util.ArrayList;
import java.util.List;

import com.law.fangyuan.Services;
import com.law.fangyuan.Setting;
import com.law.fangyuan.R;


public final class Constants {
	
	public static String mTextviewArray[] = {"新闻", "视频", "便民", "设置"};
	//定义数组来存放按钮图片
	public static int mImageViewArray[] = {R.drawable.tab_item_firstview, R.drawable.tab_item_secondview,
		R.drawable.tab_item_threeview, R.drawable.tab_item_fourview};
	
	public static Class<?> mTabClassArray[]= {
		NewShowNewsActivity.class,
		NewShowVideoNewsActivity.class,
		Servicess.class,
		Settings.class
	};
	
	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String IMAGES = "com.gos.yypad.IMAGES";
		public static final String IMAGE_POSITION = "com.gos.yypad.IMAGE_POSITION";
	}
	//法治新闻
	public static ArrayList<NewsClassify> getData() {
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(192);
		classify.setTitle("法治要闻");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(582);
		classify.setTitle("平安建设");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(200);
		classify.setTitle("法庭内外");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(199);
		classify.setTitle("检察天地");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(197);
		classify.setTitle("警界风云");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(201);
		classify.setTitle("司法行政");
		newsClassify.add(classify);
		
		classify = new NewsClassify();
		classify.setId(1029);
		classify.setTitle("公告");
		newsClassify.add(classify);
		
		classify = new NewsClassify();
		classify.setId(559);
		classify.setTitle("法律法规");
		newsClassify.add(classify);
		
		classify = new NewsClassify();
		classify.setId(1032);
		classify.setTitle("国际要闻");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1033);
		classify.setTitle("国内要闻");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(267);
		classify.setTitle("省内要闻");
		newsClassify.add(classify);
		
		
		return newsClassify;
	}

	//时政要闻
	public static ArrayList<NewsClassify> getOneData() {
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(1032);
		classify.setTitle("国际要闻");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1033);
		classify.setTitle("国内要闻");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(267);
		classify.setTitle("省内要闻");
		newsClassify.add(classify);

		return newsClassify;
	}

	//视频点播
	public static ArrayList<NewsClassify> getTwoData() {
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(1048);
		classify.setTitle("法治现场");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1060);
		classify.setTitle("政法委书记访谈");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1058);
		classify.setTitle("法治前沿");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1086);
		classify.setTitle("法治讲堂");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1051);
		classify.setTitle("走进法庭");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1120);
		classify.setTitle("检察视点");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1121);
		classify.setTitle("中原119");
		newsClassify.add(classify);

		return newsClassify;
	}
	
}
