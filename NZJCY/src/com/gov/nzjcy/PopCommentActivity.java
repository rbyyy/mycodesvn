package com.gov.nzjcy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gov.nzjcy.exception.BaseException;
import com.gov.nzjcy.exception.ParseException;
import com.gov.nzjcy.helper.GOSHelper;
import com.gov.nzjcy.httpoperation.GosHttpApplication;

import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.MotionEvent;

public class PopCommentActivity extends BaseActivity {
	
	private LinearLayout 		layout; 
	private EditText			popCommentEditText;
	private Button				commentButton;
	private String				newsIdString;
	private String				newsTitleString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popcomment);
		
		newsIdString = getIntent().getStringExtra("newsIdStr");
		newsTitleString = getIntent().getStringExtra("newstitle");
		
		layout=(LinearLayout)findViewById(R.id.commentLinearLayout);
		
        layout.setOnClickListener(new OnClickListener() {  
              
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                //Toast.makeText(getApplicationContext(), "点击边框退出", Toast.LENGTH_SHORT).show();   
            	//closeInputMethod();
            	closeInputMethod();
            } 
        });
        
        
        popCommentEditText = (EditText)findViewById(R.id.popCommentEditText);
        popCommentEditText.setFocusable(true);  
        popCommentEditText.setFocusableInTouchMode(true);  
        popCommentEditText.requestFocus();
        
//        InputMethodManager inputManager =  
//                (InputMethodManager)popCommentEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
//            inputManager.showSoftInput(popCommentEditText, 0);
        
        Timer timer = new Timer();  
        timer.schedule(new TimerTask()  
        {  
              
            public void run()  
            {  
                InputMethodManager inputManager =  
                    (InputMethodManager)popCommentEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
                inputManager.showSoftInput(popCommentEditText, 0);  
            }  
             
        },  100); 
        
        commentButton = (Button)findViewById(R.id.commentButton);
        commentButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String userIdString = GOSHelper.getSharePreStr(PopCommentActivity.this, GosHttpApplication.USER_ID_STRING);
				if (userIdString.equals("")) {
					new AlertDialog.Builder(PopCommentActivity.this)
					 .setTitle("提示") 
					 .setMessage("登录后才能评论")
					 	.setPositiveButton("确定", null)
					 	.show();
				}
				else {
					String commentString = popCommentEditText.getText().toString();
					
					if ((commentString != null) && !commentString.equals("")) {
						new Thread()
						{
							public void run() {
								sendCommentContent(newsIdString, userIdString, popCommentEditText.getText().toString());
							}
						}.start();
					}
					else {
						Toast.makeText(PopCommentActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
        
	}
	
	@Override  
    public boolean onTouchEvent(MotionEvent event){  
        finish();  
        return true;  
    }  
      
    public void exitbutton1(View v) {  //取消  
        this.finish();        
    } 
    
    public void exitbutton0(View v) {  //添加  
        this.finish();  
    } 
    
    private void closeInputMethod() {
    	InputMethodManager imms = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	boolean isOpens = imms.isActive();
    	if (isOpens) {
			imms.hideSoftInputFromWindow( popCommentEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
    }
    
    @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         String tagString = "MainActivity:" + " keyCode:" + keyCode + " evnet:"
                 + event;
         //Toast.makeText(PopCommentActivity.this, tagString, Toast.LENGTH_SHORT).show();
         Log.v("tag", "MainActivity:" + " keyCode:" + keyCode + " evnet:"
                 + event);
         return super.onKeyDown(keyCode, event);
     } 
    /**
     * 发表评论
     * */
    protected void sendCommentContent(String newsIdString, String userIdString, String commentContentString) {
    	try {
			HttpResponse aString = gosHttpOperation.invokerSendCommentResponseByNewsId(newsIdString, userIdString, commentContentString);
			HttpEntity entity = aString.getEntity();
			InputStream is = entity.getContent();
			String reString = GOSHelper.convertStreamToString(is);
		
			if (reString == null || reString.equals("")) {
				
			}
			else {
				JSONObject jsonObject = JSON.parseObject(reString);
				JSONObject dataObject = jsonObject.getJSONObject("data");
				String stateString = dataObject.getString("state");
				if (stateString.equals("1")) {
//					JSONArray responeArray = dataObject.getJSONArray("responseList");
//					if (responeArray.size() > 0) {
//						for (int i = 0; i < responeArray.size(); i++) {
//							JSONObject oneObject = responeArray.getJSONObject(i);
//						}
//					}
					newsCommnetHandler.sendEmptyMessage(1);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    /**
	 * 新闻评论条数显示
	 * */
	Handler newsCommnetHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				showCommentContent();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**
	 * 显示评论内容
	 * */
	protected void showCommentContent() {
		Intent commentIntent = new Intent(PopCommentActivity.this, CommentContentActivity.class);
		commentIntent.putExtra("newstitle", newsTitleString);
		commentIntent.putExtra("newsIdStr", newsIdString);
		PopCommentActivity.this.startActivity(commentIntent);
	}
	
}
