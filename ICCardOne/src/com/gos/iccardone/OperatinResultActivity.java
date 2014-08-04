package com.gos.iccardone;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gos.iccardone.exception.BaseException;
import com.gos.iccardone.exception.ParseException;
import com.gos.iccardone.helper.GOSHelper;
import com.gos.iccardone.httpoperation.GosHttpApplication;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OperatinResultActivity extends BaseActivity {
	/**交易明细结果列表*/
	private ArrayList<HashMap<String, Object>>	operatinResultArrayList = new ArrayList<HashMap<String,Object>>();
	/**开始日期*/
	private String				startDateString;
	/**结束日期*/
	private String				endDateString;
	/**等待框*/
	private ProgressDialog 		mSaveDialog = null;
	/**查询类型*/
    private String				queryTypeString = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operatinresult);
		mSaveDialog = ProgressDialog.show(OperatinResultActivity.this, "交易查询", "正在查询中，请稍等...", true);
		startDateString = getIntent().getStringExtra("startdate");
		endDateString = getIntent().getStringExtra("enddate");
		queryTypeString = getIntent().getStringExtra("businessType");
		new Thread(){
			public void run() {
				operatinRequest(startDateString, endDateString, queryTypeString);
			}
		}.start();
		findview();
	}
	
	protected void findview() {
		
	}
    /**网络请求*/
    protected void operatinRequest(String startDateString, String endDateString, String extraString) {
    	try {
			String cardIdString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_NAME_STRING);
			String tokenString = GOSHelper.getSharePreStr(this, GosHttpApplication.USER_TOKEN_STRING);

			HttpResponse aHttpResponse = gosHttpOperation.invokerOperatinResponse(cardIdString, startDateString, endDateString, extraString, tokenString);
			HttpEntity entity = aHttpResponse.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
			if (reString == null || reString.equals("")) {
				loginResultHandler.sendEmptyMessage(0);
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				String codeString = jsonObject.getString("code");
				if (codeString.equals("1")) {
					JSONArray responeArray = jsonObject.getJSONArray("data");
					JSONObject totalObject = responeArray.getJSONObject(0);
					JSONArray totalJsonArray = null;
					if (extraString.equals("1")) {
						totalJsonArray = totalObject.getJSONArray("extra");
					}
					else {
						totalJsonArray = totalObject.getJSONArray("total");
					}
					if (totalJsonArray == null) {
						loginResultHandler.sendEmptyMessage(2);
					}
					else if (totalJsonArray.size() > 0) {
						for (int i = 0; i < totalJsonArray.size(); i++) {
							JSONObject oneObject = totalJsonArray.getJSONObject(i);
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("transSn", oneObject.get("transSn"));
							map.put("transTime", oneObject.get("transTime"));
							String castString = "";
							if (oneObject.get("transType").toString().equals("02")) {
								castString = "转入";
							}
							else if (oneObject.get("transType").toString().equals("10")) {
								if (queryTypeString.equals("1")) {
									castString = "转出";
								}
								else {
									castString = "现金消费";
								}
								
							}
							else {
								castString = "其它";
							}
							
//							switch (Integer.valueOf(oneObject.get("transType").toString())) {
//							case 2:
//								castString = "现金充值";
//								break;
//							case 10:
//								castString = "正常消费";
//								break;
//							default:
//								castString = "其它";
//								break;
//							}
							map.put("transType", castString);
							map.put("merchNo", oneObject.get("merchNo"));
							map.put("ptVal", oneObject.get("ptVal"));
							map.put("settAmt", oneObject.get("settAmt"));
							map.put("settDate", oneObject.get("settDate"));
							map.put("status", oneObject.get("status"));
							operatinResultArrayList.add(map);
						}
						loginResultHandler.sendEmptyMessage(1);
					}
					else {
						loginResultHandler.sendEmptyMessage(2);
					}
				}
				else {
					loginResultHandler.sendEmptyMessage(0);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			e.printStackTrace();
			loginResultHandler.sendEmptyMessage(0);
		}
	}
    /**登录结果处理handler*/
	@SuppressLint("HandlerLeak")
	Handler loginResultHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loginFailed();
				break;
			case 1:
				accessToView();
				break;
			case 2:
				loginFailedOne();
				break;
			default:
				break;
			}
		}
	};
	/**查询失败的显示*/
	protected void loginFailed() {
		mSaveDialog.dismiss();
		Toast.makeText(this, "此次查询失败", Toast.LENGTH_SHORT).show();
	}
	/**查询为空的显示*/
	protected void loginFailedOne() {
		mSaveDialog.dismiss();
		if (queryTypeString.equals("1")) {
			Dialog alertDialog = new AlertDialog.Builder(this).
					setTitle("查询结果").
					setMessage("您所查询的时间段内，未发生转账").
					setNegativeButton("确定", new DialogInterface.OnClickListener() { 
	                     
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                        // TODO Auto-generated method stub  
		                    } 
		                }).create();
			alertDialog.show();
		}
		else {
			Dialog alertDialog = new AlertDialog.Builder(this).
					setTitle("查询结果").
					setMessage("您所查询的时间段内，未发生交易").
					setNegativeButton("确定", new DialogInterface.OnClickListener() { 
	                     
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                        // TODO Auto-generated method stub  
	                    } 
	                }).
					create();
			alertDialog.show();
		}
	}
	/**进入登录成功之后的界面*/
	protected void accessToView() {
		mSaveDialog.dismiss();
		MyOperatinResultAdapter operatinAdapter = new MyOperatinResultAdapter();
		((ListView)findViewById(R.id.operatinResultListView)).setAdapter(operatinAdapter);
	}
	
	class MyOperatinResultAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return operatinResultArrayList.size();
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
			OperatinResultHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.item_operatinresultlistview, null);
				holder = new OperatinResultHolder();
				holder.transSnTextView = (TextView)convertView.findViewById(R.id.transSnTextView);
				holder.transTimeTextView = (TextView)convertView.findViewById(R.id.transTimeTextView);
				holder.transTypeTextView = (TextView)convertView.findViewById(R.id.transTypeTextView);
				holder.merchNoTextView = (TextView)convertView.findViewById(R.id.merchNoTextView);
				holder.ptValTextView = (TextView)convertView.findViewById(R.id.ptValTextView);
				holder.settAmtTextView = (TextView)convertView.findViewById(R.id.settAmtTextView);
				holder.settDateTextView = (TextView)convertView.findViewById(R.id.settDateTextView);
				holder.statusTextView = (TextView)convertView.findViewById(R.id.sttusTextView);
				convertView.setTag(holder);
			}
			else {
				holder = (OperatinResultHolder)convertView.getTag();
			}
			
			
			holder.transSnTextView.setText(operatinResultArrayList.get(position).get("transSn").toString());
			String dateOneString = operatinResultArrayList.get(position).get("transTime").toString();//显示的日期
			String dateOneString2 =dateOneString.substring(0, 4) +"-"+dateOneString.substring(4, 6) +"-"+dateOneString.substring(6, 8) +
			 " "+dateOneString.substring(8, 10) +":"+dateOneString.substring(10, 12) +":"+dateOneString.substring(12, 14);
			holder.transTimeTextView.setText(dateOneString2);
			holder.transTypeTextView.setText(operatinResultArrayList.get(position).get("transType").toString());
			holder.merchNoTextView.setText(operatinResultArrayList.get(position).get("merchNo").toString());
			holder.ptValTextView.setText(operatinResultArrayList.get(position).get("ptVal").toString());
			holder.settAmtTextView.setText(operatinResultArrayList.get(position).get("settAmt").toString());
			String dateTwoString = operatinResultArrayList.get(position).get("settDate").toString();
			String dateTwoString2 =dateTwoString.substring(0, 4) +"-"+dateTwoString.substring(4, 6) +"-"+dateTwoString.substring(6, 8);
			holder.settDateTextView.setText(dateTwoString2);
			holder.statusTextView.setText(operatinResultArrayList.get(position).get("status").toString());
			
			
			return convertView;
		}
		
	}
	
	public final class OperatinResultHolder
	{
		public TextView		transSnTextView;
		public TextView		transTimeTextView;
		public TextView		transTypeTextView;
		public TextView		merchNoTextView;
		public TextView		ptValTextView;
		public TextView		settAmtTextView;
		public TextView		settDateTextView;
		public TextView		statusTextView;	
	}
	
}
