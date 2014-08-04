package com.gos.lyfx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SaleSyncActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesync);
		findView();
	}
	
	protected void findView() {
		((ImageButton)findViewById(R.id.leftNaviButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SaleSyncActivity.this.finish();
			}
		});
		((TextView)findViewById(R.id.topTitleView)).setText("销售同步");
		((Button)findViewById(R.id.printPiaoButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent oneIntent = new Intent();
				oneIntent.setClass(SaleSyncActivity.this, PrinterReceiptActivity.class);
				SaleSyncActivity.this.startActivity(oneIntent);
			}
		});	
	}
	
}
