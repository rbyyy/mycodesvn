package com.gos.iccardone;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class BusinessActivity extends BaseActivity {
	
	private long 				exitTime=0;
	//gridview
	private GridView 	topGridView;
	//ListView
	private ListView	bottomListView;
	//gridview数据源
	private ArrayList<HashMap<String, Object>>  gridViewData;
	//listview数据源
	private ArrayList<HashMap<String, Object>>  listViewData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gridViewData = new ArrayList<HashMap<String,Object>>();
		listViewData = new ArrayList<HashMap<String,Object>>();
		setContentView(R.layout.activity_business);
		initTopView();
		initBottomView();
	}
	
	/**设置上栏界面*/
	protected void initTopView() {
		gridViewData = MyLeFuGridViewInitData();
		MyLeFuGridViewAdapter adapter = new MyLeFuGridViewAdapter();
		topGridView = (GridView)findViewById(R.id.lefuGridView);
		topGridView.setAdapter(adapter);
		topGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent currentIntent = new Intent();
				switch(Integer.parseInt(((TextView)view.findViewById(R.id.ItemId)).getText().toString())){
				case 0:
					//currentIntent.setClass(Main.this,News.class);
//					currentIntent.setClass(BusinessActivity.this, ShowNewsActivity.class);
//					currentIntent.putExtra("accessType", "0");
//					BusinessActivity.this.startActivity(currentIntent);
				break;
				case 1:
					currentIntent.setClass(BusinessActivity.this, BankTransferActivity.class);
					BusinessActivity.this.startActivity(currentIntent);
				break;
				case 2:
					currentIntent.setClass(BusinessActivity.this,AccountSettingActivity.class);
					BusinessActivity.this.startActivity(currentIntent);
				break;
				case 3:
					currentIntent.setClass(BusinessActivity.this,ModifiyPasswordActivity.class);
					BusinessActivity.this.startActivity(currentIntent);
				break;
				default:
					break;
				}
			}
		});
	}
	
	/**上栏数据*/
	private class MyLeFuGridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gridViewData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			MyLeFuGridViewHodler holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.item_lefugridview, null);
				holder = new MyLeFuGridViewHodler();
				holder.oneImageView = (ImageView)convertView.findViewById(R.id.lefuGridItemImageView);
				holder.oneTextView = (TextView)convertView.findViewById(R.id.lefuGridItemTextView);
				holder.twoTextView = (TextView)convertView.findViewById(R.id.ItemId);
				convertView.setTag(holder);
			}
			else {
				holder = (MyLeFuGridViewHodler)convertView.getTag();
			}
			//设置图片
			holder.oneImageView.setImageResource(Integer.parseInt(gridViewData.get(position).get("image").toString()));
			//设置文本
			holder.oneTextView.setText(gridViewData.get(position).get("text").toString());
			//设置文本
			holder.twoTextView.setText(gridViewData.get(position).get("ItemId").toString());
			return convertView;
		}
		
	}
	/**holder*/
	public final class MyLeFuGridViewHodler {
		public ImageView		oneImageView;
		public TextView			oneTextView;
		public TextView			twoTextView;
	}
	/**数据源*/
	public ArrayList<HashMap<String, Object>> MyLeFuGridViewInitData() {
		ArrayList<HashMap<String, Object>> gridArrayList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("image", R.drawable.griditemone);
		map.put("text", "乐付宝");
		map.put("ItemId", "0");
		gridArrayList.add(map);
		
		map = new HashMap<String, Object>();
		map.put("image", R.drawable.griditemtwo);
		map.put("text", "转账");
		map.put("ItemId", "1");
		gridArrayList.add(map);
		
		map = new HashMap<String, Object>();
		map.put("image", R.drawable.griditemthree);
		map.put("text", "账户查询");
		map.put("ItemId", "2");
		gridArrayList.add(map);
		
		map = new HashMap<String, Object>();
		map.put("image", R.drawable.griditemfour);
		map.put("text", "密码管理");
		map.put("ItemId", "3");
		gridArrayList.add(map);
		
		return gridArrayList;
	}
	/**设置下栏数据*/
	protected void initBottomView() {
		listViewData = MyLeFuListViewInitData();
		MyLeFuListViewAdapter adapter = new MyLeFuListViewAdapter();
		bottomListView = (ListView)findViewById(R.id.lefuListView);
		bottomListView.setAdapter(adapter);
		bottomListView.setOnItemClickListener(new MyListItemClickListener());
	}
	/**实现item点击事件*/
	class MyListItemClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Class TargetClass = null; 
			switch (position) {
				case 0:
					TargetClass = OperatinActivity.class;
					break;
				case 1:
					TargetClass = OperatinActivity.class;
					break;
				case 2:
					
					break;
				default:
					break;
			}
			if(TargetClass != null){
				Intent intent = new Intent(BusinessActivity.this, TargetClass);
				if (position == 0) {
					intent.putExtra("businessType", "1");
				}
				else {
					intent.putExtra("businessType", "0");
				}
				startActivity(intent);
			}
		}
		
	}
	/**下栏数据*/
	private class MyLeFuListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listViewData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			MyLeFuListViewHolder holder;
			if (convertView == null) {
				convertView =  LayoutInflater.from(getApplication()).inflate(R.layout.item_lefulistview, null);
				holder = new MyLeFuListViewHolder();
				holder.oneImageView = (ImageView)convertView.findViewById(R.id.titleImageView);
				holder.oneTextView = (TextView)convertView.findViewById(R.id.titleTextView);
				holder.twoTextView = (TextView)convertView.findViewById(R.id.descTextView);
				convertView.setTag(holder);
			}
			else {
				holder = (MyLeFuListViewHolder)convertView.getTag();
			}
			//图片
			holder.oneImageView.setImageResource(Integer.parseInt(listViewData.get(position).get("image").toString()));
			//标题
			holder.oneTextView.setText(listViewData.get(position).get("text").toString());
			//描述
			holder.twoTextView.setText(listViewData.get(position).get("desc").toString());
			
			return convertView;
		}
		
	}
	/**holder*/
	public final class MyLeFuListViewHolder {
		public ImageView		oneImageView;
		public TextView			oneTextView; 
		public TextView			twoTextView;
	}
	/**数据源*/
	public ArrayList<HashMap<String, Object>> MyLeFuListViewInitData() {
		ArrayList<HashMap<String, Object>> listArrayList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("image", R.drawable.listitemone);
//		map.put("text", "账单查询");
//		map.put("desc", "我的账单");
//		listArrayList.add(map);
//		
//		map = new HashMap<String, Object>();
		map.put("image", R.drawable.listitemtwo);
		map.put("text", "转账流水");
		map.put("desc", "查询转账明细");
		listArrayList.add(map);
		
		map = new HashMap<String, Object>();
		map.put("image", R.drawable.listitemthree);
		map.put("text", "交易明细");
		map.put("desc", "查询交易明细");
		listArrayList.add(map);
		
		return listArrayList;
	}
	
	//按两次返回键退出
	 @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
      	if ((System.currentTimeMillis() - exitTime) > 2000) {
              Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
              exitTime = System.currentTimeMillis();
          } else {
              BusinessActivity.this.finish();
              System.exit(0);
          }
          return true;
      }
      return super.onKeyDown(keyCode, event);
  }
	
}
