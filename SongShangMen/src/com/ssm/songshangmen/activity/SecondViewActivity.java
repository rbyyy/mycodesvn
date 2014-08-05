package com.ssm.songshangmen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ssm.songshangmen.R;

public class SecondViewActivity extends Fragment {
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.activity_secondview, container, false); 
		findview(v);
		return v;       
    }
	/**界面初始化*/
	protected void findview(View v) {
		((Button)v.findViewById(R.id.userloginButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent oneIntent = new Intent();
				oneIntent.setClass(getActivity(), UserLoginActivity.class);
				SecondViewActivity.this.startActivity(oneIntent);
			}
		});
		
	}
	
}
