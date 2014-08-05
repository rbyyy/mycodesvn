package com.law.fangyuan;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.law.fangyuan.AsyncBitmapLoader.ImageCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Focus extends LinearLayout{
	private ViewPager vp = null;
	private Context context = null;
	private String classid = null;
	
	public Focus(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				LayoutParams.WRAP_CONTENT));
	}
	
	public Focus(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public void Init(JSONArray big, String classid){
		this.classid = classid;
		vp=new ViewPager(context);
		float scale = context.getResources().getDisplayMetrics().density;
		vp.setLayoutParams(new ListView.LayoutParams(
				ListView.LayoutParams.MATCH_PARENT, 
				(int) (180 * scale + 0.5f)));
		ArrayList<View> listViews = getTopView(big);
		
		vp.setAdapter(new FocusAdapter(listViews));
		vp.setCurrentItem(0);
		this.addView(vp);
	}
	
	private ArrayList<View> getTopView(JSONArray json){
		ArrayList<View> listViews = new ArrayList<View>();
		AsyncBitmapLoader asyncbl = AsyncBitmapLoader.getInstance(context);
		try{
			int total = json.length();
			for(int i=0;i<total;i++){
				JSONObject big = (JSONObject) json.opt(i);
				View view=LayoutInflater.from(context).inflate(R.layout.news_img,null);
				((TextView)view.findViewById(R.id.title)).setText(big.getString("title").toString());
				((TextView)view.findViewById(R.id.id)).setText(big.getString("id").toString());
				ImageView iv = (ImageView)view.findViewById(R.id.toptitlepic);
				
				asyncbl.load(iv, big.getString("titlepic").toString(), new ImageCallBack(){
					
					@Override
					public void imageLoad(ImageView iv, Drawable d) {
						// TODO Auto-generated method stub
						if(d==null){
							iv.setImageResource(R.drawable.ic_launcher);
						}else{
							iv.setImageDrawable(d);
						}
					}
				});
				switch (i){
				case 0:
					((ImageView)view.findViewById(R.id.topindex)).setImageResource(R.drawable.focus_point_1);
					break;
				case 1:
					((ImageView)view.findViewById(R.id.topindex)).setImageResource(R.drawable.focus_point_2);
					break;
				case 2:
					((ImageView)view.findViewById(R.id.topindex)).setImageResource(R.drawable.focus_point_3);
					break;
				}
				view.setOnTouchListener(new OnTouchListener(){

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						vp.getParent().requestDisallowInterceptTouchEvent(true);
						return false;
					}
					
				});
				view.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						Bundle b = new Bundle();
						b.putString("classid", classid);
						b.putString("id", ((TextView)v.findViewById(R.id.id)).getText().toString());
						intent.putExtras(b);
						if(AMenu.isVideo(classid)){
							intent.setClass(context, VideoContent.class);
						}else{
							intent.setClass(context, NewsContent.class);
						}
						((Activity) context).startActivityForResult(intent, 10);
					}
					
				});
				listViews.add(view);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listViews;
	}

}
