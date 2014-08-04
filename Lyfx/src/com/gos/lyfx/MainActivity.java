package com.gos.lyfx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends Activity {

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
	}
	
	protected void findView() {
		((Button)findViewById(R.id.discoveryButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent oneIntent = new Intent();
				oneIntent.setClass(MainActivity.this, PrinterReceiptActivity.class);
				MainActivity.this.startActivity(oneIntent);
			}
		});
		
		((ImageButton)findViewById(R.id.leftNaviButton)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.topTitleView)).setText("货品流通系统");
		
		((Button)findViewById(R.id.busStoreButton)).setOnClickListener(new ButtonClickAction());
		((Button)findViewById(R.id.saleSyncButton)).setOnClickListener(new ButtonClickAction());
		((Button)findViewById(R.id.shopInfoButton)).setOnClickListener(new ButtonClickAction());
		((Button)findViewById(R.id.businessClearButton)).setOnClickListener(new ButtonClickAction());
		((Button)findViewById(R.id.depotStoreButton)).setOnClickListener(new ButtonClickAction());
		((Button)findViewById(R.id.productStorageButton)).setOnClickListener(new ButtonClickAction());
		((Button)findViewById(R.id.productInfoButton)).setOnClickListener(new ButtonClickAction());
		((Button)findViewById(R.id.saleManResultButton)).setOnClickListener(new ButtonClickAction());
		((Button)findViewById(R.id.storageInfoButton)).setOnClickListener(new ButtonClickAction());
	}
	
	class ButtonClickAction implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent oneIntent = new Intent();
			switch (v.getId()) {
				case R.id.busStoreButton:
					oneIntent.setClass(MainActivity.this, BusStoreActivity.class);
					oneIntent.putExtra("accessType", "0");
			   		MainActivity.this.startActivity(oneIntent);
					break;
				case R.id.saleSyncButton:
					oneIntent.setClass(MainActivity.this, SaleSyncActivity.class);
					oneIntent.putExtra("accessType", "0");
			   		MainActivity.this.startActivity(oneIntent);
					break;
				case R.id.shopInfoButton:
					oneIntent.setClass(MainActivity.this, ShopInfoActivity.class);
					oneIntent.putExtra("accessType", "0");
			   		MainActivity.this.startActivity(oneIntent);
					break;
				case R.id.businessClearButton:
					oneIntent.setClass(MainActivity.this, BusinessClearActivity.class);
					oneIntent.putExtra("accessType", "0");
			   		MainActivity.this.startActivity(oneIntent);
					break;
				case R.id.depotStoreButton:
					oneIntent.setClass(MainActivity.this, DepotStoreActivity.class);
					oneIntent.putExtra("accessType", "0");
			   		MainActivity.this.startActivity(oneIntent);
					break;

				case R.id.productInfoButton:
					oneIntent.setClass(MainActivity.this, ProductInfoActivity.class);
					oneIntent.putExtra("accessType", "0");
			   		MainActivity.this.startActivity(oneIntent);
					break;
				case R.id.saleManResultButton:
					oneIntent.setClass(MainActivity.this, SaleManResultActivity.class);
					oneIntent.putExtra("accessType", "0");
			   		MainActivity.this.startActivity(oneIntent);
					break;
				case R.id.storageInfoButton://saleManResultButton storageInfoButton
					oneIntent.setClass(MainActivity.this, StoreInfoActivity.class);
					oneIntent.putExtra("accessType", "0");
			   		MainActivity.this.startActivity(oneIntent);
					break;
//				case R.id.productStorageButton:
//					oneIntent.setClass(MainActivity.this, ProductStoreActivity.class);
//					oneIntent.putExtra("accessType", "0");
//			   		MainActivity.this.startActivity(oneIntent);
//					break;
				default:
					break;
			}
		}
		
	}

}
