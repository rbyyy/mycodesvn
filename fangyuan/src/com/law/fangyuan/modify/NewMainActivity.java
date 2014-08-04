package com.law.fangyuan.modify;

import com.law.fangyuan.R;
import com.law.fangyuan.Update;
import com.law.fangyuan.modify.Constants;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class NewMainActivity extends FragmentActivity {
	private FragmentTabHost 		m_tabHost;
	private long exitTime = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmain);
        new Update(NewMainActivity.this,false);
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
        
        ImageView iv = (ImageView)v.findViewById(R.id.imageview);
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
    
	//按两次返回键退出
	 @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
       	if ((System.currentTimeMillis() - exitTime) > 2000) {
               Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
               exitTime = System.currentTimeMillis();
           } else {
               NewMainActivity.this.finish();
               System.exit(0);
           }
           return true;
       }
       return super.onKeyDown(keyCode, event);
   }
    
}
