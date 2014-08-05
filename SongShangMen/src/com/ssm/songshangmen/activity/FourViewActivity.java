package com.ssm.songshangmen.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssm.songshangmen.R;

public class FourViewActivity extends Fragment {
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.activity_fourview, container, false); 
		findview(v);
		return v;       
    }
	/**界面初始化*/
	protected void findview(View v) {
		
	}
	
}