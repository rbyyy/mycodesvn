package com.law.fangyuan.modify;

import com.law.fangyuan.AApp;
import com.law.fangyuan.AMenu;
import com.law.fangyuan.About;
import com.law.fangyuan.Check;
import com.law.fangyuan.FileCache;
import com.law.fangyuan.MemberCenter;
import com.law.fangyuan.R;
import com.law.fangyuan.Setting;
import com.law.fangyuan.Update;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Settings extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.setting, container, false);
		
		AApp.getInstance().add(getActivity());
        InitConfig(v);
        member(v);
        update(v);
        delateCache(v);
        about(v);
        exit(v);
//		((ImageView) v.findViewById(R.id.ivTitleBtnLeft)).setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				//Setting.this.finish();
//			}
//			
//		});
		
		return v;       
    }
	
	public void InitConfig(View v){
		final SharedPreferences sp = AMenu.getSharedPreferences(getActivity());
		CheckBox news_push = (CheckBox) v.findViewById(R.id.setting_2_checkbox);//推送新闻
		news_push.setChecked(sp.getBoolean("news_push", true));
		news_push.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor edit = sp.edit();
				edit.putBoolean("news_push", arg1);
				edit.commit();
				if(arg1){
					Intent check = new Intent(getActivity(), Check.class);
					getActivity().startService(check);
				}else{
					Intent check = new Intent(getActivity(), Check.class);
					getActivity().stopService(check);
				}
			}
			
		});
		CheckBox news_sound = (CheckBox) v.findViewById(R.id.setting_4_checkbox);//推送声音
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
		CheckBox news_cache = (CheckBox) v.findViewById(R.id.setting_5_checkbox);//缓存新闻
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
	
	public void member(View v){
		final RelativeLayout member = (RelativeLayout) v.findViewById(R.id.member_center);
		member.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				member.setBackgroundResource(R.color.nav_bottom_background_color);
				Intent intent = new Intent();
				intent.setClass(getActivity(), MemberCenter.class);
				getActivity().startActivity(intent);
				member.setBackgroundResource(R.color.background);
			}
			
		});
	}
	
	public void update(View v){
		final RelativeLayout update = (RelativeLayout) v.findViewById(R.id.check_update);
		update.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				update.setBackgroundResource(R.color.nav_bottom_background_color);
				new Update(getActivity());
			}
			
		});
	}
	
	public void delateCache(View v){
		final RelativeLayout del = (RelativeLayout) v.findViewById(R.id.delete_cache);
		del.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				del.setBackgroundResource(R.color.nav_bottom_background_color);
				new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.ic_delete)
				.setTitle(R.string.delete_cache_msg)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int whichButton) {
		    		/* User clicked OK so do some stuff */
		    			dialog.dismiss();
		   				FileCache cache = new FileCache(getActivity());
		   				cache.deleteAll();
		   				getActivity().deleteDatabase("webview.db");
		   				getActivity().deleteDatabase("webviewCache.db");
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
	
	public void about(View v){
		final RelativeLayout about = (RelativeLayout) v.findViewById(R.id.about);
		about.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				about.setBackgroundResource(R.color.nav_bottom_background_color);
				Intent intent = new Intent();
				intent.setClass(getActivity(), About.class);
				getActivity().startActivity(intent);
				about.setBackgroundResource(R.color.background);
			}
			
		});
	}
	
	public void exit(View v){
		final RelativeLayout exit = (RelativeLayout) v.findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				exit.setBackgroundResource(R.color.nav_bottom_background_color);
				new AlertDialog.Builder(getActivity())
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
