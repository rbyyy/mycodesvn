package com.ssm.ssmshop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ssm.ssmshop.adapter.OrderPreviewAdapter;
import com.ssm.ssmshop.entity.Auth;
import com.ssm.ssmshop.entity.BaseResponse;
import com.ssm.ssmshop.entity.OrderPreview;
import com.ssm.ssmshop.entity.StateCode;
import com.ssm.ssmshop.exception.BaseException;
import com.ssm.ssmshop.helper.SSMHelper;
import com.ssm.ssmshop.httpoperation.GosHttpApplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class OrderCountActivity extends BaseActivity {
	
	/**等待动画*/
	private		ProgressDialog				progressDialog;
	/**列表*/
	private		ListView					orderCountListView;
	/**数据源*/
	private		OrderPreviewAdapter			orderPreviewAdapter;
	/**数据集合*/
	private		ArrayList<OrderPreview>		orderPreviewList = new ArrayList<OrderPreview>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar();
		setContentView(R.layout.activity_ordercount);
		orderCountListView = (ListView)findViewById(R.id.orderCountListView);
		progressDialog = new ProgressDialog(OrderCountActivity.this);
		SSMHelper.showProgressDialog(progressDialog);
		new Thread(){
			public void run() {
				requestShopOrder("1");
			}
		}.start();
	}
	/**设置actionbar*/
	protected void setActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.ico_backbee);
		actionBar.setTitle("订单预览");
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar));		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			OrderCountActivity.this.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	/**获取订单*/
	protected void requestShopOrder(String pageString) {
		try {
			String userIdString = SSMHelper.getSharePreStr(OrderCountActivity.this, GosHttpApplication.USER_ID_STRING);
			BaseResponse<OrderPreview> baseResponse = gosHttpOperation.invokerObtainShopOrderList(userIdString, pageString);
			int codeInt = baseResponse.getCode();
			if (codeInt == 1) {
				orderPreviewList = (ArrayList<OrderPreview>)baseResponse.getData();
				loginHandler.sendEmptyMessage(1);
			}
			else {
				loginHandler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginHandler.sendEmptyMessage(0);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginHandler.sendEmptyMessage(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginHandler.sendEmptyMessage(0);
		}
	}
	
	private Handler loginHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loginRequestFailed();
				break;
			case 1:
				loginRequestSuccessed();
				break;
			default:
				break;
			}
		}
	};
	/**登录请求失败*/
	protected void loginRequestFailed() {
		progressDialog.dismiss();
		Toast.makeText(OrderCountActivity.this, "获取订单失败", Toast.LENGTH_SHORT).show();
	}
	/**登录请求成功*/
	protected void loginRequestSuccessed() {
		//Toast.makeText(OrderCountActivity.this, "获取订单成功", Toast.LENGTH_SHORT).show();
		orderPreviewAdapter = new OrderPreviewAdapter(OrderCountActivity.this, orderPreviewList);
		orderCountListView.setAdapter(orderPreviewAdapter);
		orderCountListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent oneIntent = new Intent();
				oneIntent.setClass(OrderCountActivity.this, ShopOrderDetailActivity.class);
				oneIntent.putExtra("orderId", orderPreviewList.get(position).getOrderId());
				OrderCountActivity.this.startActivity(oneIntent);
			}
		});
		progressDialog.dismiss();
	}
	
}
