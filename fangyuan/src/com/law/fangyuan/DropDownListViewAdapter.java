package com.law.fangyuan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.law.fangyuan.AsyncBitmapLoader.ImageCallBack;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DropDownListViewAdapter extends BaseAdapter {
	
	private Context context;
	private JSONArray json = null;
	AsyncBitmapLoader asyncLoader = null;
	
	public DropDownListViewAdapter(Context mContext,JSONArray j){
		json = j;
		context = mContext;
		asyncLoader = AsyncBitmapLoader.getInstance(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return json.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = View.inflate(context, R.layout.news_item, null);
		}
		try {
			convertView.setTag(position);
			
			JSONObject tmp=(JSONObject) json.opt(position);
			((TextView)convertView.findViewById(R.id.id)).setText(tmp.getString("id").toString());
			((TextView)convertView.findViewById(R.id.title)).setText(Html.fromHtml(tmp.getString("title").toString()));
			((TextView)convertView.findViewById(R.id.newstime)).setText(tmp.getString("newstime").toString());
			//((TextView)convertView.findViewById(R.id.smalltext)).setText(Html.fromHtml(tmp.getString("smalltext").toString()));
			if(tmp.has("classid"))((TextView)convertView.findViewById(R.id.classid)).setText(tmp.getString("classid").toString());
			ImageView iv = (ImageView)convertView.findViewById(R.id.titlepic);
			String titlepic = tmp.getString("titlepic").toString();
			if(titlepic.equals("")){
				iv.setVisibility(View.GONE);
			}else{
				iv.setVisibility(View.VISIBLE);
				iv.setImageResource(R.drawable.ic_launcher);
				asyncLoader.load(iv, titlepic, new ImageCallBack(){
					@Override
					public void imageLoad(ImageView iv, Drawable d) {
						// TODO Auto-generated method stub
						if(d==null){
							iv.setVisibility(View.GONE);
						}else{
							iv.setImageDrawable(d);
						}
					}
				});
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

}
