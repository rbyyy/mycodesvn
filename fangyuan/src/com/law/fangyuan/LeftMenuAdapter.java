package com.law.fangyuan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeftMenuAdapter extends BaseAdapter {
	
	private String classid;
	private Context context;
	private JSONArray json = null;
	
	public LeftMenuAdapter(Context mContext,JSONArray j,String classid){
		json = j;
		context = mContext;
		this.classid = classid;
	}

	@Override
	public int getCount() {
		return json.length();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = View.inflate(context, R.layout.left_menu_item, null);
		}
		convertView.setTag(position);
		try {
			JSONObject tmp=(JSONObject) json.opt(position);
			String cid = tmp.getString("classid").toString();
			String ico = tmp.getString("ico").toString();
			if(ico.equals("")){	ico = "0";	}
			((ImageView)convertView.findViewById(R.id.ivIcon)).setImageResource(context.getResources().getIdentifier("navigation_ico_"+ico, "drawable", context.getPackageName()));
			((TextView)convertView.findViewById(R.id.classname)).setText(tmp.getString("classname").toString());
			((TextView)convertView.findViewById(R.id.classid)).setText(cid);
			if(cid.equals(classid)){
				convertView.setBackgroundResource(R.drawable.navigation_list_foucs);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

}
