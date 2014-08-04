package com.law.fangyuan;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AMenu {

	public static void NewsBottomEven(final Activity activity, int index) {
		final ImageView news_all = (ImageView) activity
				.findViewById(R.id.news_all);
		final ImageView news_recommend = (ImageView) activity
				.findViewById(R.id.news_recommend);
		final ImageView news_push = (ImageView) activity
				.findViewById(R.id.news_push);
		final ImageView news_collect = (ImageView) activity
				.findViewById(R.id.news_collect);
		news_recommend.setImageResource(R.drawable.news_recommend);
		news_push.setImageResource(R.drawable.news_push);
		news_collect.setImageResource(R.drawable.news_collect);
		news_all.setImageResource(R.drawable.news_all);
		if (index == 1) {
			news_all.setImageResource(R.drawable.news_all_pressed);
		}else{
			news_all.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent news = new Intent();
					news.setClass(activity, News.class);
					activity.startActivity(news);
					activity.finish();
					activity.overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
				}

			});
		}
		if (index == 2) {
			news_recommend.setImageResource(R.drawable.news_recommend_pressed);
		}else{
			news_recommend.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent news = new Intent();
					news.setClass(activity, NewsRecommend.class);
					activity.startActivity(news);
					activity.finish();
					activity.overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
				}

			});
		}
		if (index == 3) {
			news_push.setImageResource(R.drawable.news_push_pressed);
		}else{
			news_push.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent news = new Intent();
					news.setClass(activity, NewsPush.class);
					activity.startActivity(news);
					activity.finish();
					activity.overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
				}

			});
		}
		if (index == 4) {
			news_collect.setImageResource(R.drawable.news_collect_pressed);
		}else{
			news_collect.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent news = new Intent();
					news.setClass(activity, NewsCollect.class);
					activity.startActivity(news);
					activity.finish();
					activity.overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
				}

			});
		}
	}

	public static String getVersion(Activity a) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = a.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(a.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	public static boolean isVideo(String classid) {
		if (classid.equals("1048") || classid.equals("1060") || classid.equals("1058") || classid.equals("1086") || classid.equals("1051") || classid.equals("1120") || classid.equals("1121")) {
			return true;
		}
		return false;
	}

	public static boolean isConnection(Activity a) {
		ConnectivityManager manager = (ConnectivityManager) a
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className)) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
	
	public static boolean isLogin(Activity a){
		HashMap<String,String> cookies = getCookies(a);
        if(!cookies.containsKey("DedeUserID")){
			Intent member = new Intent();
			member.setClass(a, MemberLogin.class);
			a.startActivity(member);
			a.finish();
        }
        return true;
	}
	
	public static HashMap<String,String> getCookies(Activity a){
		SharedPreferences sp = getSharedPreferences(a);
		String[] cookies = sp.getString("cookie", "").split(";");
		HashMap<String,String> mcookie = new HashMap<String,String>();
		for(int i=0; i<cookies.length; i++){
			String[] tmp = cookies[i].split("=");
			if(tmp.length == 2){
				mcookie.put(tmp[0], tmp[1]);
			}			
		}
		return mcookie;
	}
	
	public static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences("fangyuan_config", 0);
	}

	public static void exit(final Activity a) {
		new AlertDialog.Builder(a)
				.setTitle(R.string.exit_message)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								AApp.getInstance().exit();
							}
						})
				.setNegativeButton(R.string.cancal,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
							}
						}).show();
	}

	// 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	// 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	@SuppressWarnings("resource")
	public String MD5(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		MessageDigest digest = null;
		byte buf[] = new byte[1024];
		try {
			digest = MessageDigest.getInstance("MD5");
			FileInputStream ins = new FileInputStream(file);
			int len = 0;
			while ((len = ins.read(buf, 0, 1024)) != -1) {
				digest.update(buf, 0, len);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		BigInteger bigint = new BigInteger(1, digest.digest());
		return bigint.toString(16);
	}

	@SuppressWarnings("deprecation")
	public static int getPhoneAndroidSDK() {
		// TODO Auto-generated method stub
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;
	}

	public static String stripTags(String htmlStr) {
		if (htmlStr == null || "".equals(htmlStr))
			return "";
		String textStr = "";
		java.util.regex.Pattern pattern;
		java.util.regex.Matcher matcher;

		try {
			String regEx_remark = "<!--.+?-->";
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
																										// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
																									// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			htmlStr = htmlStr.replaceAll("\n", "");
			htmlStr = htmlStr.replaceAll("\t", "");
			pattern = Pattern.compile(regEx_remark);// 过滤注释标签
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll("");

			pattern = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤script标签

			pattern = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤style标签

			pattern = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤html标签

			pattern = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			htmlStr = matcher.replaceAll(""); // 过滤html标签

			textStr = htmlStr.trim();

		} catch (Exception e) {
			System.out.println("获取HTML中的text出错:");
			e.printStackTrace();
		}

		return textStr;// 返回文本字符串
	}

}
