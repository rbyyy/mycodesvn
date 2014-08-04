package com.gov.nzjcy;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MultimediaVideoActivity extends BaseActivity {
	
	/** head 头部 的左侧菜单 按钮*/
	private LinearLayout	top_left_Layout;
	/** head 头部 的右侧菜单 按钮*/
	private ImageButton		top_right_headImageButton;
	/**head的标题*/
	private TextView		topTitleTextView;
	/**播放视频视图*/
	private VideoView videoView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multimediavideo);
//		requestWindowFeature(Window.FEATURE_NO_TITLE); 
//	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式 
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏 
	    initView();
	    initVideoView();
	}
	//初始化界面
	protected void initView() {
		top_left_Layout = (LinearLayout)findViewById(R.id.leftPopLinearLayout);//左边的返回按钮
		topTitleTextView = (TextView)findViewById(R.id.topTitleTextview);
		String titlString = getIntent().getStringExtra("title");
		topTitleTextView.setText(titlString);//标题
		top_right_headImageButton = (ImageButton)findViewById(R.id.loginImageButton);//用户登录按钮
		//左边返回按钮的点击事件
		top_left_Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MultimediaVideoActivity.this.finish();
			}
		});
		//隐藏右边的按钮
		top_right_headImageButton.setVisibility(View.INVISIBLE);
	}
	/**初始化视频*/
	protected void initVideoView() {
		videoView = (VideoView) findViewById(R.id.videoView); 
		String urlString = "http://m.nzjcy.gov.cn:81" + getIntent().getStringExtra("videoUrl");
	    videoView.setVideoURI(Uri.parse(urlString)); 
	    MediaController mediaController = new MediaController(this); 
	    videoView.setMediaController(mediaController); 
	    videoView.start(); 
	}
	
		
}
