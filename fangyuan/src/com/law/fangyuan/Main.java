package com.law.fangyuan;

import java.util.ArrayList;
import java.util.HashMap;

import com.law.fangyuan.modify.ShowNewsActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		AApp.getInstance().add(this);
		//new Update(Main.this,false);
		GridView gridview = (GridView) findViewById(R.id.menu);
	    SimpleAdapter saMenuItem = new SimpleAdapter(this,   
	    		InitData(), //����Դ   
	    		R.layout.main_item, //xmlʵ��   
	    		new String[]{"ItemImage","ItemText","ItemId"}, //��Ӧmap��Key   
	    		new int[]{R.id.ItemImage,R.id.ItemText,R.id.ItemId});  //��ӦR��Id 
	    //���Item��������
	    gridview.setAdapter(saMenuItem); 
	    gridview.setOnItemClickListener(new OnItemClickListener() { 

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent news = new Intent();
				switch(Integer.parseInt(((TextView)view.findViewById(R.id.ItemId)).getText().toString())){
				case 0:
					//news.setClass(Main.this,News.class);
					news.setClass(Main.this, ShowNewsActivity.class);
					news.putExtra("accessType", "0");
			   		Main.this.startActivity(news);
				break;
				case 1:
					//news.setClass(Main.this,Video.class);
					news.setClass(Main.this, ShowNewsActivity.class);
					news.putExtra("accessType", "2");
			   		Main.this.startActivity(news);
				break;
				case 2:
					news.setClass(Main.this,Announce.class);
			   		Main.this.startActivity(news);
				break;
				case 3:
					news.setClass(Main.this,Services.class);
			   		Main.this.startActivity(news);
				break;
				case 4:
					news.setClass(Main.this,Laws.class);
			   		Main.this.startActivity(news);
				break;
				case 5:
					news.setClass(Main.this,Lawyer.class);
			   		Main.this.startActivity(news);
				break;
				case 6:
					news.setClass(Main.this,MemberCenter.class);
			   		Main.this.startActivity(news);
				break;
				case 7:
					//news.setClass(Main.this,Politics.class);
					news.setClass(Main.this, ShowNewsActivity.class);
					news.putExtra("accessType", "1");
			   		Main.this.startActivity(news);
				break;
				case 8:
					news.setClass(Main.this,Setting.class);
			   		Main.this.startActivity(news);
				break;
				}
			}
	    }); 
	}
	
	private ArrayList<HashMap<String, Object>> InitData(){
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.main_item_1);
		map.put("ItemText", "��������");
		map.put("ItemId", "0"); 
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.main_item_8);
		map.put("ItemText", "ʱ��Ҫ��");
		map.put("ItemId", "7");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.main_item_2);
		map.put("ItemText", "��Ƶ�㲥");
		map.put("ItemId", "1");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.main_item_3);
		map.put("ItemText", "Ȩ������");
		map.put("ItemId", "2");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.main_item_4);
		map.put("ItemText", "�������");
		map.put("ItemId", "3");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.main_item_5);
		map.put("ItemText", "���ɷ���");
		map.put("ItemId", "4");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.main_item_6);
		map.put("ItemText", "��ʦ��ѯ");
		map.put("ItemId", "5");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.main_item_7);
		map.put("ItemText", "��Ա����");
		map.put("ItemId", "6");
		meumList.add(map);
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.main_item_9);
		map.put("ItemText", "��������");
		map.put("ItemId", "8");
		meumList.add(map);
		return meumList;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
			AMenu.exit(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
