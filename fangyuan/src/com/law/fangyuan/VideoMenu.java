package com.law.fangyuan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoMenu {
	private Activity activity;
	private FileCache cache;
	private String classid,cache_name;
	private ListView mListView = null;
	private VideoList mVideoList = null;
	private LeftMenuAdapter menuListAdapter=null;
	
	public VideoMenu(Activity a,String id){
		activity = a;
		classid = id;
		cache = new FileCache(activity);
		mVideoList = new VideoList(activity,cache);
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
				mVideoList.show(classid);
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
		cache_name = "video_title";
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
			mVideoList.show(classid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class getTitle extends AsyncTask<Object, Object, Object>{
		
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result = "{\"r\":[{\"classid\":\"1048\",\"classname\":\"\u6cd5\u6cbb\u73b0\u573a\",\"ico\":0,\"channel\":\"1\"},{\"classid\":1060,\"classname\":\"\u653f\u6cd5\u59d4\u4e66\u8bb0\u8bbf\u8c08\",\"ico\":1,\"channel\":\"1\"},{\"classid\":\"1058\",\"classname\":\"\u6cd5\u6cbb\u524d\u6cbf\",\"ico\":2,\"channel\":\"1\"},{\"classid\":\"1086\",\"classname\":\"\u6cd5\u6cbb\u8bb2\u5802\",\"ico\":3,\"channel\":\"1\"},{\"classid\":\"1051\",\"classname\":\"\u8d70\u8fdb\u6cd5\u5ead\",\"ico\":4,\"channel\":\"1\"},{\"classid\":\"1120\",\"classname\":\"\u68c0\u5bdf\u89c6\u70b9\",\"ico\":5,\"channel\":\"1\"},{\"classid\":\"1121\",\"classname\":\"\u4e2d\u539f119\",\"ico\":6,\"channel\":\"1\"}]}";
/*			Http http = new Http(activity);
			result = http.GET("http://www.60886666.com/android/?m=getNav");
			if(!result.equals("")){
				cache.set(cache_name, result);
			}*/
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
