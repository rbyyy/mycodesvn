package com.law.fangyuan;

import java.io.File;

import com.law.fangyuan.modify.NewMainActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		InitService();
		InitNet();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	private void InitSplash(){
        String img = this.getFilesDir().toString()+"/splash.png";
        if(new File(img).exists()){
        	Bitmap bitmap = BitmapFactory.decodeFile(img);
        	((ImageView) findViewById(R.id.img)).setImageBitmap(bitmap);
        }
        new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
		   		Intent main = new Intent();
		   		main.setClass(Splash.this,NewMainActivity.class);//Main
				Splash.this.startActivity(main);
				Splash.this.finish();
				Splash.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}
        	
        }, 3000);
	}
	
	private void InitNet(){
		if(!AMenu.isConnection(Splash.this)){
			Toast.makeText(Splash.this, R.string.net_disconnection, Toast.LENGTH_LONG).show();
		}
		InitSplash();
//		new AlertDialog.Builder(Splash.this)
//		.setIcon(android.R.drawable.ic_delete)
//		.setTitle(R.string.prompt)
//		.setMessage(R.string.net_disconnection)
//		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//    		public void onClick(DialogInterface dialog, int whichButton) {
//    		/* User clicked OK so do some stuff */
//    			dialog.dismiss();
//    			Splash.this.finish();
//    		}
//		}).show();
	}
	
	private void InitService(){
		if(!AMenu.isServiceRunning(Splash.this, "com.law.fangyuan.Check")){
			SharedPreferences cfg = AMenu.getSharedPreferences(Splash.this);
			if(!cfg.getBoolean("news_push", true))return;
			Intent check = new Intent(this, Check.class);
			startService(check);
		}
	}

}
