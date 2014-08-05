package com.law.fangyuan.modify;

import java.util.ArrayList;
import java.util.HashMap;

import com.law.fangyuan.AApp;
import com.law.fangyuan.Lawyer;
import com.law.fangyuan.MemberCenter;
import com.law.fangyuan.R;
import com.law.fangyuan.Services;
import com.law.fangyuan.Violation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Servicess extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.services, container, false); 
		
		AApp.getInstance().add(getActivity());
		GridView gridview = (GridView) v.findViewById(R.id.services);
	    SimpleAdapter saMenuItem = new SimpleAdapter(getActivity(),   
	    		InitData(), //数据源   
	    		R.layout.main_item, //xml实现   
	    		new String[]{"ItemImage","ItemText","ItemId"}, //对应map的Key   
	    		new int[]{R.id.ItemImage,R.id.ItemText,R.id.ItemId});  //对应R的Id 
	    //添加Item到网格中
	    gridview.setAdapter(saMenuItem); 
	    gridview.setOnItemClickListener(new OnItemClickListener() { 

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent m = new Intent();
				switch(Integer.parseInt(((TextView)view.findViewById(R.id.ItemId)).getText().toString())){
				case 0:
					Bundle b = new Bundle();
					b.putString("url", "http://chengyun.60886666.com/net.asp");
					m.putExtras(b);
					m.setClass(getActivity(),Lawyer.class);
					getActivity().startActivity(m);
				break;
				case 1:
					Bundle b0 = new Bundle();
					b0.putString("url", "http://ws.hncourt.org/m/");
					m.putExtras(b0);
					m.setClass(getActivity(),Lawyer.class);
					getActivity().startActivity(m);
				break;
				case 2:
					Bundle b1 = new Bundle();
					b1.putString("url", "http://www.60886666.com/android/service/phone.php");
					m.putExtras(b1);
					m.setClass(getActivity(),Lawyer.class);
					getActivity().startActivity(m);
				break;
				case 3:
					Bundle b2 = new Bundle();
					b2.putString("url", "http://www.60886666.com/android/service/airplane.php");
					m.putExtras(b2);
					m.setClass(getActivity(),Lawyer.class);
					getActivity().startActivity(m);
				break;
				case 4:
					Bundle b3 = new Bundle();
					b3.putString("url", "http://www.60886666.com/android/service/train.php");
					m.putExtras(b3);
					m.setClass(getActivity(),Lawyer.class);
					getActivity().startActivity(m);
				break;
				case 5:
					Bundle b4 = new Bundle();
					b4.putString("url", "http://www.60886666.com/android/service/lawsuit.php");
					m.putExtras(b4);
					m.setClass(getActivity(),Lawyer.class);
					getActivity().startActivity(m);
				break;
				}
			}
	    }); 
	    ImageView userLoginImageView = (ImageView) v.findViewById(R.id.userLoginImageView);
	    userLoginImageView.setVisibility(View.VISIBLE);
		userLoginImageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), MemberCenter.class);
				getActivity().startActivity(intent);
			}
			
		});
		
		return v;       
    }
	
	private ArrayList<HashMap<String, Object>> InitData(){
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.service_1);
		map.put("ItemText", "新闻报料");
		map.put("ItemId", "0"); 
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.service_2);
		map.put("ItemText", "裁判文书");
		map.put("ItemId", "1");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.service_3);
		map.put("ItemText", "常用电话");
		map.put("ItemId", "2");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.service_4);
		map.put("ItemText", "航班查询");
		map.put("ItemId", "3");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.service_5);
		map.put("ItemText", "火车查询");
		map.put("ItemId", "4");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.service_6);
		map.put("ItemText", "诉讼费计算");
		map.put("ItemId", "5");
		meumList.add(map);
		return meumList;
	}
	
}
