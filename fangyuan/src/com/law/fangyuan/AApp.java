package com.law.fangyuan;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class AApp extends Application{
	
	private static AApp instance;
	private List<Activity> activitys = new LinkedList<Activity>();
	
	public static AApp getInstance(){
		if(null == instance){
			instance = new AApp();
		}
		return instance;
	}
	
	public void add(Activity activity){
		activitys.add(activity);
	}
	
	public void exit(){
		for(Activity activity:activitys){
			activity.finish();
		}
		System.exit(0);
	}
}
