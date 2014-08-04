package com.law.fangyuan;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.law.fangyuan.DropDownListView.OnDropDownListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Laws extends Activity {
	private int p = 1;
	private int com = 0;
	private boolean r = false;
	private FileCache cache;
	private String classid,cache_name;
	private JSONArray jsondata = null;
	private DropDownListView listView  = null;
	private DropDownListViewAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.com_list);
        AApp.getInstance().add(this);
        cache = new FileCache(this);
        classid = "559";
        listView = (DropDownListView)findViewById(R.id.com_list);
    	listView.setIsDropDownStyle(true);
    	listView.setIsOnBottomStyle(true);
    	listView.setIsAutoLoadOnBottom(true);
        listView.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onDropDown() {
            	if(r) return;
            	r = true;
            	p=1;
            	com =1;
            	jsondata = null;
            	new getList().execute();
            }
        });

        // set on bottom listener
        listView.setOnBottomListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	if(r) return;
            	r = true;
            	p = p+1;
            	com =2;
            	getData();
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle b = new Bundle();
				b.putString("classid", classid);
				b.putString("id", ((TextView)v.findViewById(R.id.id)).getText().toString());
				intent.putExtras(b);
				intent.setClass(Laws.this, NewsContent.class);
				Laws.this.startActivityForResult(intent, 10);
			}
			
		});
		((ImageView) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Laws.this.finish();
			}
			
		});
        getData();
	}
	
	public void getData(){
		cache_name = "list/"+classid+"_"+Integer.toString(p);
		String result = cache.get(cache_name,3600);
		if(result.equals("")){
			new getList().execute();
		}else{
			InitList(result);
		}
	}
	
	public void addJson(JSONArray r){
		if(jsondata==null){
			jsondata = r;
		}else{
			for(int i=0; i<r.length();i++){
				jsondata.put(r.opt(i));
			}
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public void InitList(String result){
		try {
			JSONObject json = new JSONObject(result);
			addJson(json.getJSONArray("r"));
			if(adapter==null){
				adapter = new DropDownListViewAdapter(this,jsondata);
		        listView.setAdapter(adapter);
		        listView.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						listView.setVisibility(View.VISIBLE);
						((Loading)Laws.this.findViewById(R.id.loading)).setVisibility(View.GONE);
					}
		        	
		        });
			}else{
                adapter.notifyDataSetChanged();
    	        switch(com){
    	        case 1:listView.onDropDownComplete(getString(R.string.update_at)+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));break;
    	        case 2:listView.onBottomComplete();break;
    	        }
    	        com = 0;
			}
			r = false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	class getList extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result="";
			Http http = new Http(Laws.this);
			result = http.GET("http://www.60886666.com/android/?m=getList&classid="+classid+"&page="+Integer.toString(p));
			if(!result.equals("")){
				cache.set(cache_name, result);
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			InitList(result.toString());
		}
		
	}
}
