package com.gos.bluetoothtemp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.bluetoothtemp.TempAlarmClassifyActivity.TempClassifyAdapter;
import com.gos.bluetoothtemp.data.TempAlarm;
import com.gos.bluetoothtemp.data.TempAlarmDao;
import com.gos.bluetoothtemp.http.Http;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TempAlarmActivity extends ActionBarActivity {
	
	private	TempAlarmDao			tempAlarmDao;
	private ArrayList<TempAlarm>	tempAlarmArrayList = new ArrayList<TempAlarm>();
	
	private Menu mOptionsMenu;
	
	private MyListAdapter 			mAdapter;
	/**报警类型*/
	private String					alarmTypeString;
	/**报警类型名称*/
	private	String					alarmTypeNameString;
	/**消息id*/
	private String					msgIdString = "";
	/**等待*/
	private  ProgressDialog dialog = null;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tempalarm);
		alarmTypeString = getIntent().getStringExtra("msgTypeID");
		alarmTypeNameString = getIntent().getStringExtra("msgType");
		setActionBar();
		
		SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
		serverAddressInfo.edit().putInt("temp", 0).commit();

		tempAlarmDao = new TempAlarmDao(this);
		tempAlarmArrayList = tempAlarmDao.readTempAlarmListByAlarmType("temp", alarmTypeString);
		mAdapter = new MyListAdapter();
		((ListView)findViewById(R.id.tempAlarmListView)).setAdapter(mAdapter);
	}
	/**设置actionbar*/
	protected void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setTitle(alarmTypeNameString);		
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        //添加菜单项
        MenuItem del=menu.add(0,0,0,"清空");
        //绑定到ActionBar 
        del.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        del.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				showDialog();
				return false;
			}
		});
        return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			TempAlarmActivity.this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**弹出对话框*/
	protected void showDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this). 
                setTitle("报警记录清空"). 
                setMessage("报警记录将被清空，是否继续？"). 
                setIcon(R.drawable.ic_launcher). 
                setPositiveButton("确定", new DialogInterface.OnClickListener() { 
                     
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    	if (tempAlarmDao.delete("temp")) {
        					tempAlarmArrayList.clear();
        					mAdapter.notifyDataSetChanged();
        				}
                    } 
                }). 
                setNegativeButton("取消", new DialogInterface.OnClickListener() { 
                     
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    } 
                }).create(); 
        alertDialog.show(); 
	}
	 
	public void setRefreshActionButtonState(boolean refreshing) {
//	  if (mOptionsMenu == null) {
//	    return;
//	  }
//	 
//	  final MenuItem refreshItem = mOptionsMenu.findItem(R.id.menu_refresh);
//	  if (refreshItem != null) {
//	    if (refreshing) {
//	      MenuItemCompat.setActionView(refreshItem, R.layout.actionbar_indeterminate_progress);
//	    } else {
//	      MenuItemCompat.setActionView(refreshItem, null);
//	    }
//	  }
	}
	
	class MyListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tempAlarmArrayList.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			MyListHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.item_bluetoothtempalarm, null);
				holder = new MyListHolder();
				holder.tempAlarmFlagImageView = (ImageView)convertView.findViewById(R.id.tempAlarmFlagImageView);
				holder.tempAlarmDateTextView = (TextView)convertView.findViewById(R.id.tempAlarmDateTextView);
				holder.tempAlarmDealTextView = (TextView)convertView.findViewById(R.id.tempAlarmDealTextView);
				holder.tempAlarmContentTextView = (TextView)convertView.findViewById(R.id.tempAlarmContentTextView);
				convertView.setTag(holder);
			}
			else {
				holder = (MyListHolder)convertView.getTag();
			}
			
			holder.tempAlarmDateTextView.setText(tempAlarmArrayList.get(position).getTemp_date());
			if (tempAlarmArrayList.get(position).getTemp_deal().equals("1")) {
				holder.tempAlarmDealTextView.setText("已处理");
				holder.tempAlarmDealTextView.setTextColor(Color.GREEN);
				holder.tempAlarmFlagImageView.setBackgroundResource(R.drawable.mailone);
			}
			else {
				holder.tempAlarmDealTextView.setText("未处理");
				holder.tempAlarmDealTextView.setTextColor(Color.RED);
				holder.tempAlarmFlagImageView.setBackgroundResource(R.drawable.mail);
			}
			holder.tempAlarmContentTextView.setText(tempAlarmArrayList.get(position).getTemp_content());
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!tempAlarmArrayList.get(position).getTemp_deal().equals("1")){
						msgIdString = tempAlarmArrayList.get(position).getTemp_msg_id();
						msgDealDialog();
					}
				}
			});
			
			return convertView;
		}
		
	}
	
	public final class MyListHolder{
		public  ImageView			tempAlarmFlagImageView;
		public  TextView			tempAlarmDateTextView;
		public  TextView			tempAlarmDealTextView;
		public	TextView			tempAlarmContentTextView;
	}
	
	/**是否是已处理消息的弹窗*/
	protected void msgDealDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this). 
                setTitle("是否已处理"). 
                setMessage("该条报警是否已处理？"). 
                setIcon(R.drawable.ic_launcher). 
                setPositiveButton("确定", new DialogInterface.OnClickListener() { 
                     
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    	new getAlarmClassifyInfo().execute();
                    } 
                }). 
                setNegativeButton("取消", new DialogInterface.OnClickListener() { 
                     
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        // TODO Auto-generated method stub  
                    } 
                }).create(); 
        alertDialog.show(); 
	}
	
    /**
     * 信息上传
     * */
    Runnable loadingTwo = new Runnable(){

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// TODO Auto-generated method stub
	        dialog = new ProgressDialog(TempAlarmActivity.this);
	        dialog.setMessage("上传处理结果数据中，请稍等...");
	        dialog.setIndeterminate(true);
	        dialog.setCancelable(true);
	        dialog.setButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
	        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	        dialog.show();
		}
    	
    };
	
    /**获取温度类型信息*/
    class getAlarmClassifyInfo extends AsyncTask<Object, Object, Object>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadingTwo.run();
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			Http http = new Http(TempAlarmActivity.this);
			JSONObject paramJsonObject2 = new JSONObject();
	    	paramJsonObject2.put("comStr", "1006");//命令
	    	paramJsonObject2.put("msgID", msgIdString);//命令
	    	paramJsonObject2.put("msgState", "1");//命令
	    	
	    	JSONObject paramOne = new JSONObject();
	    	paramOne.put("data", paramJsonObject2);
	    	
	    	String query = URLEncodedUtils.format(Http.stripNulls(new BasicNameValuePair("data", paramOne.toString())), HTTP.UTF_8);
	    	SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
			String serverAddressString = serverAddressInfo.getString("serveraddress", "");  
			if (!serverAddressString.equals("")) {
				try {
					String httpString = "http://"+ serverAddressString + "/getInterface.aspx?" + query;
			    	return http.GET(httpString);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
			String httpString = "http://116.255.209.108:8011/getInterface.aspx?" + query;
	    	return http.GET(httpString);
			
//	    	return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog!=null){
				dialog.dismiss();
			}
			
			String resultString = result.toString();
			if ((!result.equals(""))&&(!resultString.startsWith("<html>"))) {
				parseTempClassifyData(resultString);
			}
			else {
				Toast.makeText(TempAlarmActivity.this, "服务器地址不正确", Toast.LENGTH_SHORT).show();
			}
			
		}	
	} 
    /**温度类型数据解析*/
	protected void parseTempClassifyData(String resultString) {
    	if (resultString.equals("")) {
    		Dialog alertDialog = new AlertDialog.Builder(this). 
                    setTitle("提示"). 
                    setMessage("上传数据失败"). 
                    setIcon(R.drawable.ic_launcher). 
                    create(); 
            alertDialog.show(); 
		}
    	else {
    		JSONObject jsonObject = JSONObject.parseObject(resultString);
    		JSONObject dataObject = jsonObject.getJSONObject("data");
    		String stateString = dataObject.getString("state");
    		if (stateString.equals("1")) {
    			if (tempAlarmDao.updateDataByMsgId("temp", msgIdString)) {
    				tempAlarmArrayList = tempAlarmDao.readTempAlarmListByAlarmType("temp", alarmTypeString);
    				mAdapter.notifyDataSetChanged();
    				Dialog alertDialog = new AlertDialog.Builder(this). 
                            setTitle("提示"). 
                            setMessage("上传数据成功"). 
                            setIcon(R.drawable.ic_launcher). 
                            create(); 
                    alertDialog.show();
				}
    		}
    		else {
    			Dialog alertDialog = new AlertDialog.Builder(this). 
                        setTitle("提示"). 
                        setMessage("上传数据失败"). 
                        setIcon(R.drawable.ic_launcher). 
                        create(); 
                alertDialog.show();
    		}
		}
	}
	
	public void onBackPressed() {
		SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
		serverAddressInfo.edit().putInt("temp", 0).commit();
		TempAlarmActivity.this.finish();
	}
	
}
