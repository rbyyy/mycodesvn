package com.gov.nzjcy.constants;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.gov.nzjcy.BaseActivity;
import com.gov.nzjcy.entity.NewsClassify;
import com.gov.nzjcy.entity.NewsEntity;

public final class Constants {
	
	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String IMAGES = "com.gos.yypad.IMAGES";
		public static final String IMAGE_POSITION = "com.gos.yypad.IMAGE_POSITION";
	}
	//首页的数据项
	public static ArrayList<NewsClassify> getData() {
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(0);
		classify.setTitle("检务公开");//1
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1);
		classify.setTitle("检察动态");//0
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(2);
		classify.setTitle("依申请公开");//10
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(3);
		classify.setTitle("起诉书公开");//7
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(4);
		classify.setTitle("说理文书公开 ");//9
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(5);
		classify.setTitle("查办预防职务犯罪");//8
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(6);
		classify.setTitle("检察理论实践");//4
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(7);
		classify.setTitle("司法解释与规范性文件");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(8);
		classify.setTitle("青少年维权");//2
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(9);
		classify.setTitle("检察文化");//3
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(10);
		classify.setTitle("服务企业发展");//6
		newsClassify.add(classify);
		return newsClassify;
	}
	//首页的新闻项内容
	public static ArrayList<NewsEntity> getNewsList() {
		ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
		for(int i =0 ; i < 10 ; i++){
			NewsEntity news = new NewsEntity();
			news.setId(i);
			news.setNewsId("123");
			news.setCollectStatus(false);
			news.setCommentNum(i + 10);
			news.setInterestedStatus(true);
			news.setLikeStatus(true);
			news.setReadStatus(false);
			news.setNewsCategory("0123");
			news.setNewsCategoryId("1");
			news.setTitle("kankanaknakankakn");
			List<String> url_list = new ArrayList<String>();
			news.setTitle("南召县委党校讲师为县检察院党员上党课");
			String url = "http://www.nzjcy.gov.cn/nzcms_nzweb/nzcms_up/nz_pic/nzcms20145131693192081.jpg";
			news.setPicOne(url);
			url_list.add(url);
			news.setPicList(url_list);
			news.setPublishTime(Long.valueOf(i));
			news.setReadStatus(false);
			news.setSource("2014年5月13日");
			news.setSummary("45345345345");
			news.setMark(i);
			news.setIsLarge(false);
			newsList.add(news);
		}
		return newsList;
	}
	//阳光检务的数据项
	public static ArrayList<NewsClassify> getOneData() {
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(0);
		classify.setTitle("检务公开指南");
		newsClassify.add(classify);	
		
		classify = new NewsClassify();
		classify.setId(1);
		classify.setTitle("机构设置");
		newsClassify.add(classify);
		
		classify = new NewsClassify();
		classify.setId(2);
		classify.setTitle("南召检察手机报");
		newsClassify.add(classify);
		
		classify = new NewsClassify();
		classify.setId(3);
		classify.setTitle("官方微信");		
		newsClassify.add(classify);
		
		classify = new NewsClassify();
		classify.setId(4);
		classify.setTitle("重大工作部署");
		newsClassify.add(classify);
		
		classify = new NewsClassify();
		classify.setId(5);
		classify.setTitle("典型案例");
		newsClassify.add(classify);
		
//		classify = new NewsClassify();
//		classify.setId(5);
//		classify.setTitle("案例指导");
//		newsClassify.add(classify);

		return newsClassify;
	}
	//阳光检务的新闻项内容
	public static ArrayList<NewsEntity> getOneNewsList() {
		ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
		for(int i =0 ; i < 10 ; i++){
			NewsEntity news = new NewsEntity();
			news.setId(i);
			news.setNewsId("1");
			news.setCollectStatus(false);
			news.setTitle("南召手机周报-检察专刊20期");
			List<String> url_list = new ArrayList<String>();
			String url = "";
			if (!url.equals("")) {
				news.setPicOne(url);
				url_list.add(url);
			}
			news.setPicList(url_list);
			news.setSource("2014年5月13日");
			news.setMark(i);

			newsList.add(news);
		}
		return newsList;
	}
	
	
	
	/** mark=0 ：推荐 */
	public final static int mark_recom = 0;
	/** mark=1 ：热门 */
	public final static int mark_hot = 1;
	/** mark=2 ：首发 */
	public final static int mark_frist = 2;
	/** mark=3 ：独家 */
	public final static int mark_exclusive = 3;
	/** mark=4 ：收藏 */
	public final static int mark_favor = 4;
	
}
