package com.gos.lyfx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class StoreInfoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storageinfo);
		findView();
	}
	
	protected void findView() {
		((ImageButton)findViewById(R.id.leftNaviButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StoreInfoActivity.this.finish();
			}
		});
		((TextView)findViewById(R.id.topTitleView)).setText("库存信息");
		((Button)findViewById(R.id.storeInfoButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent oneIntent = new Intent();
				oneIntent.setClass(StoreInfoActivity.this, DepotInfoActivity.class);
				StoreInfoActivity.this.startActivity(oneIntent);
			}
		});
		
		((Button)findViewById(R.id.busInfoButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent oneIntent = new Intent();
				oneIntent.setClass(StoreInfoActivity.this, BusInfoActivity.class);
				StoreInfoActivity.this.startActivity(oneIntent);
			}
		});
		
	}
	
}
