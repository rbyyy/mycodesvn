package com.gos.yypad;

import java.util.ArrayList;

import com.gos.yypad.Splash.MyViewPageClickListener;
import com.gos.yypad.helper.GOSHelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class PopImagePreviewActivity extends Activity {
	
	private ViewPager 	mViewPager;
	
	private String		pictureUrlString;
	
	private String[]				pictureStrings;
	
	private	int						currentPictureItem;//当前第几张图
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popimagepreview);
		TextView topTitle = (TextView)findViewById(R.id.topTitleView);
		topTitle.setTextColor(Color.WHITE);
		topTitle.setText("图片预览");
		//返回按钮
		ImageButton leftNaviImageButton = (ImageButton)findViewById(R.id.leftNaviButton);
		leftNaviImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		pictureUrlString = getIntent().getStringExtra("productImageUrl");
		currentPictureItem = 0;
		
		pictureStrings = pictureUrlString.split(",");
		
		mViewPager = (ViewPager)findViewById(R.id.popimage_viewpager);
		final ArrayList<ImageView> views = new ArrayList<ImageView>();
		
		for (int i = 0; i < pictureStrings.length; i++) {//image_id.length
			ImageView imageView = new ImageView(this);

			
			String[] urlStrings = pictureStrings[i].split("\\//");
			String[] oneUrlStrings = urlStrings[urlStrings.length-1].split("\\/");
			String twoUrlString = urlStrings[urlStrings.length-1].replace(oneUrlStrings[0], "");

			String extPath = GOSHelper.getExternDir()+twoUrlString;
			
			try {
				Bitmap bitmap = GOSHelper.getBitmapByFileName(extPath);
				imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
			}

			imageView.setScaleType(ScaleType.FIT_XY);
			views.add(imageView);
		}
		
		
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mViewPager.setAdapter(mPagerAdapter);
		
	}
	
}
