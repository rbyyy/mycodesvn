package com.law.fangyuan;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Violation extends Activity {
	private String file="violation_search";
	private FileCache cache;
	private Loading loading = null;
	private JSONArray json = null;
	private LinearLayout search,add,result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.violation);
		AApp.getInstance().add(this);
		cache = new FileCache(this);
		loading = (Loading) Violation.this.findViewById(R.id.loading);
		search =(LinearLayout) Violation.this.findViewById(R.id.search);
		add = (LinearLayout) Violation.this.findViewById(R.id.add);
		result = (LinearLayout) Violation.this.findViewById(R.id.result);
		init();
	//	addSearch();
	}
	
	public void init(){
		try {
			String result = cache.get(file);
			if(result.equals("")){
				json = new JSONArray();
				autoSearch();
			}else{
				json = new JSONArray(result);
				addSearch();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((Button) findViewById(R.id.auto_search)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String brand = ((TextView)Violation.this.findViewById(R.id.brand_txt)).getText().toString();
				String plate = ((EditText)Violation.this.findViewById(R.id.plate)).getText().toString();
				String carframe = ((EditText)Violation.this.findViewById(R.id.carframe)).getText().toString();
				if(!brand.equals("")&&!plate.equals("")&&!carframe.equals("")){
					resultSearch(brand, plate, carframe);
				}				
			}
		});
		((Button) findViewById(R.id.add_search)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				autoSearch();
			}
		});
		((ImageView) findViewById(R.id.back)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				addSearch();
			}
		});
		((Button) findViewById(R.id.flush)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				loading.setVisibility(View.VISIBLE);
				new getInfo().execute();
			}
		});
	}
	
	public void autoSearch(){
		search.setVisibility(View.VISIBLE);
		add.setVisibility(View.GONE);
		result.setVisibility(View.GONE);
	}
	
	public void addSearch(){
		search.setVisibility(View.GONE);
		add.setVisibility(View.VISIBLE);
		result.setVisibility(View.GONE);
		ListView list = (ListView) findViewById(R.id.auto_list);
        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        try {
    		for(int i=0; i<json.length();i++){
    			JSONObject j = (JSONObject)json.opt(i);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("brand_img", R.drawable.ic_launcher);//图像资源的ID
                map.put("brand_txt", j.getString("brand").toString());//图像资源的ID
                map.put("plate", j.getString("plate").toString());
                map.put("carframe", j.getString("carframe").toString());
                listItem.add(map);
    		}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //生成适配器的Item和动态数组对应的元素
        list.setAdapter(new SimpleAdapter(this,listItem,//数据源 
            R.layout.violation_add_list,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"brand_img","brand_txt","plate","carframe"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.brand_img,R.id.brand_txt,R.id.plate,R.id.carframe}
        ));
        
        //添加点击
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                    long arg3) {        		
        		String plate = ((TextView)v.findViewById(R.id.plate)).getText().toString();
        		String brand = ((TextView)v.findViewById(R.id.brand_txt)).getText().toString();
        		String carframe = ((TextView)v.findViewById(R.id.carframe)).getText().toString();
        		if(!brand.equals("")&&!plate.equals("")&&!carframe.equals("")){
					resultSearch(brand, plate, carframe);
				}	
            }
        });        
	}
	
	public void resultSearch(String brand, String plate, String carframe){
		boolean exist = false;
		try {
			JSONObject j = new JSONObject();
			j.put("brand",brand);
			j.put("plate", plate);
			j.put("carframe", carframe);
			for(int i=0; i<json.length();i++){
				if(plate.equals(((JSONObject)json.opt(i)).getString("plate"))){
					exist = true;
					json.put(i, j);
					break;
				}
			}
			if(!exist){
				json.put(j);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cache.set(file, json.toString());
		new getInfo().execute();
		search.setVisibility(View.GONE);
		add.setVisibility(View.GONE);
		result.setVisibility(View.VISIBLE);
	}
	
	private void showResult(){
		loading.setVisibility(View.GONE);
	}
		
	class getInfo extends AsyncTask<Object, Object, Object>{
		
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result="";
//			Http http = new Http(activity);
//			result = http.POST("http://bespeak.zzjtgl.cn/zxywbl/jdc_cx.aspx?ywdm=A_1",params);
//			if(!result.equals("")){
//				cache.set(cache_name, result);
//			}
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			showResult();
		}
		
	}

}
