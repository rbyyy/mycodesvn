package com.gos.lyfx;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BusinessClearActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_businessclear);
		findView();
	}
	
	protected void findView() {
		((ImageButton)findViewById(R.id.leftNaviButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BusinessClearActivity.this.finish();
			}
		});
		((TextView)findViewById(R.id.topTitleView)).setText("业务结算");
	}
	
}
