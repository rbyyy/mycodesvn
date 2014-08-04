package com.law.fangyuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Politics extends Activity {
	private SlidingLayout centerLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);
		AApp.getInstance().add(this);
		LinearLayout leftLayout = (LinearLayout) findViewById(R.id.left);
		LinearLayout rightLayout = (LinearLayout) findViewById(R.id.right);
		centerLayout = (SlidingLayout) findViewById(R.id.center);

		centerLayout.setBrotherLayout(leftLayout, rightLayout);
		InitEven();
		new PoliticsMenu(Politics.this,"0");
	}
	
	private void InitEven(){
		((ImageButton) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (centerLayout.getPage() == SlidingLayout.MIDDLE)
					centerLayout.setPage(SlidingLayout.LEFT);
				else
					centerLayout.setPage(SlidingLayout.MIDDLE);
			}
		});
		((ImageButton) findViewById(R.id.setting)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Politics.this, Setting.class);
				Politics.this.startActivity(intent);
				Politics.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		if (centerLayout.getPage() != SlidingLayout.MIDDLE)
			centerLayout.setPage(SlidingLayout.MIDDLE);
		else
			super.onBackPressed();
	}
}
