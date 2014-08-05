package com.law.fangyuan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class Setting extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        AApp.getInstance().add(this);
        InitConfig();
        member();
        update();
        delateCache();
        about();
        exit();
		((ImageView) findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Setting.this.finish();
			}
			
		});
	}
	
	public void InitConfig(){
		final SharedPreferences sp = AMenu.getSharedPreferences(Setting.this);
		CheckBox news_push = (CheckBox) findViewById(R.id.setting_2_checkbox);//推送新闻
		news_push.setChecked(sp.getBoolean("news_push", true));
		news_push.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor edit = sp.edit();
				edit.putBoolean("news_push", arg1);
				edit.commit();
				if(arg1){
					Intent check = new Intent(Setting.this, Check.class);
					Setting.this.startService(check);
				}else{
					Intent check = new Intent(Setting.this, Check.class);
					Setting.this.stopService(check);
				}
			}
			
		});
		CheckBox news_sound = (CheckBox) findViewById(R.id.setting_4_checkbox);//推送声音
		news_sound.setChecked(sp.getBoolean("news_sound", true));
		news_sound.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor edit = sp.edit();
				edit.putBoolean("news_sound", arg1);
				edit.commit();
			}
			
		});
		CheckBox news_cache = (CheckBox) findViewById(R.id.setting_5_checkbox);//缓存新闻
		news_cache.setChecked(sp.getBoolean("news_cache", true));
		news_cache.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor edit = sp.edit();
				edit.putBoolean("news_cache", arg1);
				edit.commit();
			}
			
		});
	}
	
	public void member(){
		final RelativeLayout member = (RelativeLayout) findViewById(R.id.member_center);
		member.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				member.setBackgroundResource(R.color.nav_bottom_background_color);
				Intent intent = new Intent();
				intent.setClass(Setting.this, MemberCenter.class);
				Setting.this.startActivity(intent);
				member.setBackgroundResource(R.color.background);
			}
			
		});
	}
	
	public void update(){
		final RelativeLayout update = (RelativeLayout) findViewById(R.id.check_update);
		update.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				update.setBackgroundResource(R.color.nav_bottom_background_color);
				new Update(Setting.this);
			}
			
		});
	}
	
	public void delateCache(){
		final RelativeLayout del = (RelativeLayout) findViewById(R.id.delete_cache);
		del.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				del.setBackgroundResource(R.color.nav_bottom_background_color);
				new AlertDialog.Builder(Setting.this)
				.setIcon(android.R.drawable.ic_delete)
				.setTitle(R.string.delete_cache_msg)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int whichButton) {
		    		/* User clicked OK so do some stuff */
		    			dialog.dismiss();
		   				FileCache cache = new FileCache(Setting.this);
		   				cache.deleteAll();
		   				Setting.this.deleteDatabase("webview.db");
		   				Setting.this.deleteDatabase("webviewCache.db");
		    			del.setBackgroundResource(R.color.background);
		    		}
				})
				.setNegativeButton(R.string.cancal, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
		    			del.setBackgroundResource(R.color.background);
					}
					
				})
				.show();
			}
			
		});
	}
	
	public void about(){
		final RelativeLayout about = (RelativeLayout) findViewById(R.id.about);
		about.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				about.setBackgroundResource(R.color.nav_bottom_background_color);
				Intent intent = new Intent();
				intent.setClass(Setting.this, About.class);
				Setting.this.startActivity(intent);
				about.setBackgroundResource(R.color.background);
			}
			
		});
	}
	
	public void exit(){
		final RelativeLayout exit = (RelativeLayout) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				exit.setBackgroundResource(R.color.nav_bottom_background_color);
				new AlertDialog.Builder(Setting.this)
				.setTitle(R.string.exit_message)
				.setPositiveButton(R.string.confirm,
			        new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							AApp.getInstance().exit();
						}})
				.setNegativeButton(R.string.cancal,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
						exit.setBackgroundResource(R.color.background);
					}
				})			
				.show();
			}
			
		});
	}
	
}
