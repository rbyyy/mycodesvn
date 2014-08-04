package com.ssm.songshangmen.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ssm.songshangmen.R;
import com.ssm.songshangmen.adapter.ShopListAdapter;
import com.ssm.songshangmen.entity.BaseResponse;
import com.ssm.songshangmen.entity.Shop;
import com.ssm.songshangmen.exception.BaseException;
import com.ssm.songshangmen.exception.ParseException;
import com.ssm.songshangmen.helper.SSMHelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TakeOutActivity extends BaseActivity {
	
	protected 	ImageLoader imageLoader = ImageLoader.getInstance();
	/**等待动画*/
	private		ProgressDialog			progressDialog;
	/**商店列表*/
	private		ArrayList<Shop>			shopsList = new ArrayList<Shop>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_takeout);
		findview();
		progressDialog = new ProgressDialog(TakeOutActivity.this);
		SSMHelper.showProgressDialog(progressDialog);
		//setShopList();
		new Thread(){
			public void run() {
				requestShopInfo();
			}
		}.start();
	}

	protected void findview() {
		LinearLayout leftBackLinearLayout = (LinearLayout)findViewById(R.id.leftBackLinearLayout);
		leftBackLinearLayout.setVisibility(View.VISIBLE);
		leftBackLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TakeOutActivity.this.finish();
			}
		});
		((TextView)findViewById(R.id.title)).setText("外卖");
	}
	
	/**获取商店信息*/
	protected void requestShopInfo() {
		try {
			BaseResponse<Shop> shopList = gosHttpOperation.invokerObtainShopListByDistance("1", "116.417384", "39.920178");
			int codeInt = shopList.getCode();
			if (codeInt == 1) {
				shopsList = (ArrayList<Shop>)shopList.getData();
				handler.sendEmptyMessage(1);
			}
			else {
				handler.sendEmptyMessage(0);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**对结果进行处理*/
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				requestDataFailed();
				break;
			case 1:
				requestDataSuccessed();
				break;
			default:
				break;
			}
		}
	};
	
	/**获取数据失败*/
	protected void requestDataFailed() {
		SSMHelper.showMsg(TakeOutActivity.this, "获取数据失败");
		progressDialog.dismiss();
	}
	
	/**获取数据成功*/
	protected void requestDataSuccessed() {
		setShopList();
		progressDialog.dismiss();
	}
	
	/**商店列表*/
	protected void setShopList() {
		ShopListAdapter shopListAdapter = new ShopListAdapter(TakeOutActivity.this, imageLoader, shopsList);
		ListView shopListView = (ListView)findViewById(R.id.takeoutListView);
		shopListView.setAdapter(shopListAdapter);
		shopListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent oneIntent = new Intent();
				oneIntent.setClass(TakeOutActivity.this, ShopMenuActivity.class);
				oneIntent.putExtra("shopName", shopsList.get(position).getName());
				oneIntent.putExtra("shopId", shopsList.get(position).getId());
				TakeOutActivity.this.startActivity(oneIntent);
			}
		});
	}
	
}
