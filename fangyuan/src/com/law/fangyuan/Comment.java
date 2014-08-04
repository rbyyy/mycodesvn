package com.law.fangyuan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Comment extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_comment);
		AApp.getInstance().add(this);
		new AddComment(Comment.this);
		
		((ImageView) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Comment.this.finish();
			}
			
		});
		
		((ImageView) findViewById(R.id.back)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((ImageView)arg0.findViewById(R.id.back)).setImageResource(R.drawable.press_back_bar);
				Comment.this.finish();
			}
			
		});
	}
}
