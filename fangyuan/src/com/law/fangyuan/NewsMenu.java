package com.law.fangyuan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NewsMenu {

	private Activity activity;
	private FileCache cache;
	private String classid,cache_name;
	private ListView mListView = null;
	private NewsList mNewsList = null;
	private LeftMenuAdapter menuListAdapter=null;
	
	public NewsMenu(Activity a,String id){
		activity = a;
		classid = id;
		cache = new FileCache(activity);
		mNewsList = new NewsList(activity,cache);
		showMenu();
	}
	
	public void showMenu(){
		mListView = (ListView)activity.findViewById(R.id.left_menu_list);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				classid = ((TextView)v.findViewById(R.id.classid)).getText().toString();
				mNewsList.show(classid);
				setSelected(classid);
			}
			
		});
		getData();
	}
	
	
	public void setSelected(String classid){
        for(int i = 0; i < mListView.getCount(); i++)
        {  
            LinearLayout linearlayout = (LinearLayout)mListView.getChildAt(i);
            if(classid.equals(((TextView)linearlayout.findViewById(R.id.classid)).getText().toString())){
            	linearlayout.setBackgroundResource(R.drawable.navigation_list_foucs);
            }else{
            	linearlayout.setBackgroundResource(R.drawable.navigation_list_background);
            }
        }  
	}
	
	private void getData(){
		cache_name = "news_title";
		String result = cache.get(cache_name,86400);
		if(result.equals("")){
			new getTitle().execute();
		}else{
			InitMenu(result);
		}
	}
	
	private void InitMenu(String result){
		JSONObject json;
		try {
			json = new JSONObject(result);
			JSONArray jsondata = json.getJSONArray("r");
			if(classid.equals("0")){
				classid = ((JSONObject) jsondata.opt(0)).getString("classid").toString();
			}
			menuListAdapter = new LeftMenuAdapter(activity,jsondata,classid);
			mListView.setAdapter(menuListAdapter);
			mNewsList.show(classid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class getTitle extends AsyncTask<Object, Object, Object>{
		
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result="{\"r\":[{\"classid\":\"192\",\"classname\":\"\u6cd5\u6cbb\u8981\u95fb\",\"ico\":\"0\",\"channel\":\"1\"},{\"classid\":\"582\",\"classname\":\"\u5e73\u5b89\u5efa\u8bbe\",\"ico\":\"1\",\"channel\":\"1\"},{\"classid\":\"200\",\"classname\":\"\u6cd5\u5ead\u5185\u5916\",\"ico\":\"2\",\"channel\":\"1\"},{\"classid\":\"199\",\"classname\":\"\u68c0\u5bdf\u5929\u5730\",\"ico\":\"3\",\"channel\":\"1\"},{\"classid\":\"197\",\"classname\":\"\u8b66\u754c\u98ce\u4e91\",\"ico\":\"4\",\"channel\":\"1\"},{\"classid\":\"201\",\"classname\":\"\u53f8\u6cd5\u884c\u653f\",\"ico\":\"5\",\"channel\":\"1\"}]}";
//			"{\"r\":[{\"classid\":\"192\",\"classname\":\"\u6cd5\u6cbb\u65b0\u95fb\",\"ico\":\"0\",\"channel\":\"1\"},{\"classid\":\"582\",\"classname\":\"\u653f\u6cd5\u59d4\",\"ico\":\"1\",\"channel\":\"1\"},{\"classid\":\"197\",\"classname\":\"\u516c\u5b89\",\"ico\":\"2\",\"channel\":\"1\"},{\"classid\":\"199\",\"classname\":\"\u68c0\u5bdf\",\"ico\":\"3\",\"channel\":\"1\"},{\"classid\":\"200\",\"classname\":\"\u6cd5\u9662\",\"ico\":\"4\",\"channel\":\"1\"},{\"classid\":\"201\",\"classname\":\"\u53f8\u6cd5\",\"ico\":\"5\",\"channel\":\"1\"},{\"classid\":\"583\",\"classname\":\"\u7efc\u6cbb\",\"ico\":\"7\",\"channel\":\"1\"},{\"classid\":\"205\",\"classname\":\"\u6d88\u9632\",\"ico\":\"8\",\"channel\":\"1\"}]}";
//			Http http = new Http(activity);
//			result = http.GET("http://www.60886666.com/android/?m=getNav");
//			if(!result.equals("")){
//				cache.set(cache_name, result);
//			}
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			InitMenu(result.toString());
		}
		
	}
	
}
