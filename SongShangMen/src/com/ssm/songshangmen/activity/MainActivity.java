package com.ssm.songshangmen.activity;

import com.ssm.songshangmen.constants.Constants;
import com.ssm.songshangmen.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class MainActivity extends FragmentActivity {
	
	private FragmentTabHost 		m_tabHost;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	init();
 
    }
	/**tabbat设置*/
	private void init() {
		m_tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost); 
		m_tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent); 
		
		int count = Constants.mTabClassArray.length;
		for (int i = 0; i < count; i++) {
			//为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = m_tabHost.newTabSpec(Constants.mTextviewArray[i]).setIndicator(getIndicatorView(i));
			//将Tab按钮添加进Tab选项卡中
			m_tabHost.addTab(tabSpec, Constants.mTabClassArray[i], null);
			//设置Tab按钮的背景  
			//m_tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bg); 
		}

	}
	
	
	private View getIndicatorView(int index) {  
        View v = getLayoutInflater().inflate(R.layout.tab_item_view, null); 
        
        ImageView iv = (ImageView)v.findViewById(R.id.imageviewTabbar);
        iv.setImageResource(Constants.mImageViewArray[index]);
        
        TextView tv = (TextView)v.findViewById(R.id.tabText);  
        tv.setText(Constants.mTextviewArray[index]); 
        
        return v;  
    } 
	
	  
    @Override  
    protected void onDestroy() {  
        // TODO Auto-generated method stub  
        super.onDestroy();  
        m_tabHost = null;  
    } 

}
