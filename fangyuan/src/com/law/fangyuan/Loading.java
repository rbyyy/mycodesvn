package com.law.fangyuan;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Loading extends LinearLayout {

	public Loading(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		this.setGravity(Gravity.CENTER);
		//this.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
/*		ProgressBar progress = new ProgressBar(context);
		progress.setLayoutParams(params);
		this.addView(progress);
		TextView tv = new TextView(context);
		tv.setLayoutParams(params);
		tv.setTextColor(getResources().getColor(R.color.red));
		tv.setText(R.string.loading);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(20);
		this.addView(tv);*/
		
		ImageView iv = new ImageView(context);
		iv.setLayoutParams(params);
		iv.setImageResource(R.anim.loading);
		final AnimationDrawable ani = (AnimationDrawable)iv.getDrawable();
		iv.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener(){

			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				ani.start();
				return true;
			}
			
		});
		this.addView(iv);
	}

}