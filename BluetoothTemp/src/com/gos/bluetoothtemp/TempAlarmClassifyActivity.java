package com.gos.bluetoothtemp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.bluetoothtemp.data.TempAlarm;
import com.gos.bluetoothtemp.data.TempAlarmDao;
import com.gos.bluetoothtemp.http.Http;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TempAlarmClassifyActivity extends ActionBarActivity {
	
	/**报警类型*/
	private	 ArrayList<HashMap<String, Object>> alarmTypeArrayList = new ArrayList<HashMap<String,Object>>();
	/**等待*/
	private  ProgressDialog dialog = null;
	/**报警类型列表*/
	private	 ListView		alarmTypeListView;
	/**报警数据表*/
	private	TempAlarmDao			tempAlarmDao;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tempalarmclassify);
		tempAlarmDao = new TempAlarmDao(this);
		setActionBar();
		alarmTypeListView = (ListView)findViewById(R.id.tempAlarmClassifyListView);
    	SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
		String serverAddressString = serverAddressInfo.getString("serveraddress", "");
		if (serverAddressString.equals("")) {
			Toast.makeText(TempAlarmClassifyActivity.this, "服务器地址不能为空", Toast.LENGTH_SHORT).show();
		}
		else {
			new getAlarmClassifyInfo().execute();
		}

	}
	/**设置actionbar*/
	protected void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setTitle("报警类型");		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			TempAlarmClassifyActivity.this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
    /**
     * 获取温度类型信息提示
     * */
    Runnable loadingTwo = new Runnable(){

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			// TODO Auto-generated method stub
	        dialog = new ProgressDialog(TempAlarmClassifyActivity.this);
	        dialog.setMessage("获取报警类型数据中，请稍等...");
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
	
    /**获取温度报警类型信息*/
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
			Http http = new Http(TempAlarmClassifyActivity.this);
			JSONObject paramJsonObject2 = new JSONObject();
	    	paramJsonObject2.put("comStr", "1005");//命令
	    	JSONObject paramOne = new JSONObject();
	    	paramOne.put("data", paramJsonObject2);
	    	
	    	String query = URLEncodedUtils.format(Http.stripNulls(new BasicNameValuePair("data", paramOne.toString())), HTTP.UTF_8);
	    	SharedPreferences serverAddressInfo = getSharedPreferences("serveraddress_info", 0);  
			String serverAddressString = serverAddressInfo.getString("serveraddress", "");
//			String serverAddressString = "116.255.209.108:8011";
			if (!serverAddressString.equals("")) {
				try {
					String httpString = "http://"+ serverAddressString + "/getInterface.aspx?" + query;
			    	return http.GET(httpString);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
//			String httpString = "http://116.255.209.108:8011/getInterface.aspx?" + query;
//	    	return http.GET(httpString);
			
	    	return "";
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
				Toast.makeText(TempAlarmClassifyActivity.this, "服务器地址不正确", Toast.LENGTH_SHORT).show();
			}
			
		}	
	} 
    /**温度类型数据解析*/
	protected void parseTempClassifyData(String resultString) {
    	if (resultString.equals("")) {
    		Dialog alertDialog = new AlertDialog.Builder(this). 
                    setTitle("提示"). 
                    setMessage("获取数据失败"). 
                    setIcon(R.drawable.ic_launcher). 
                    create(); 
            alertDialog.show(); 
		}
    	else {
    		JSONObject jsonObject = JSONObject.parseObject(resultString);
    		JSONObject dataObject = jsonObject.getJSONObject("data");
    		String stateString = dataObject.getString("state");
    		if (stateString.equals("1")) {
    			JSONArray infoArray = dataObject.getJSONArray("info");
    			for (int i = 0; i < infoArray.size(); i++) {
    				JSONObject oneObject = infoArray.getJSONObject(i);
    				//bigSmallArrayList.add(oneObject.getString("canbinID"));
    				HashMap<String, Object> maps = new HashMap<String, Object>();
    				maps.put("msgTypeID", oneObject.get("msgTypeID"));
    				maps.put("msgType", oneObject.get("msgType"));
    				ArrayList<TempAlarm> tempAlarmArrayList = tempAlarmDao.readTempAlarmListByAlarmTypeAndDeal("temp", oneObject.get("msgTypeID").toString());
    				maps.put("msgCount", tempAlarmArrayList.size());
    				alarmTypeArrayList.add(maps);
    			}
    			TempClassifyAdapter adapter = new TempClassifyAdapter();
    			alarmTypeListView.setAdapter(adapter);
    		}
    		else {
    			Dialog alertDialog = new AlertDialog.Builder(this). 
                        setTitle("提示"). 
                        setMessage("获取数据失败"). 
                        setIcon(R.drawable.ic_launcher). 
                        create(); 
                alertDialog.show();
    		}
		}
	}
	
	/**温度类型数据源*/
	class TempClassifyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return alarmTypeArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TempAlarmClassifyHolder 	holder;
			if (convertView == null) {
				holder = new TempAlarmClassifyHolder();
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.item_tempalarmclassify, null);
				holder.tempAlarmClassifyNameTextView = (TextView)convertView.findViewById(R.id.tempAlarmClassifyNameTextView);
				holder.tempAlarmUnreadMsgTextView = (TextView)convertView.findViewById(R.id.tempAlarmUnreadMsgTextView);
				convertView.setTag(holder);
			}
			else {
				holder = (TempAlarmClassifyHolder)convertView.getTag();
			}
			/**报警类型*/
			holder.tempAlarmClassifyNameTextView.setText(alarmTypeArrayList.get(position).get("msgType").toString());
			holder.tempAlarmUnreadMsgTextView.setText(alarmTypeArrayList.get(position).get("msgCount").toString());
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent oneIntent = new Intent();
					oneIntent.setClass(TempAlarmClassifyActivity.this, TempAlarmActivity.class);
					oneIntent.putExtra("msgTypeID", alarmTypeArrayList.get(position).get("msgTypeID").toString());
					oneIntent.putExtra("msgType", alarmTypeArrayList.get(position).get("msgType").toString());
					TempAlarmClassifyActivity.this.startActivity(oneIntent);
				}
			});
			return convertView;
		}
		
	}
	
	class TempAlarmClassifyHolder{
		public	TextView		tempAlarmClassifyNameTextView;
		public	TextView		tempAlarmUnreadMsgTextView;
	}
	
}
