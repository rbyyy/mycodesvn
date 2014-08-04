package com.ssm.songshangmen.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.RemoteViews;
import com.ssm.songshangmen.R;


public class Download extends Service {
	
	private int rate;
	private String url;
	private String fileName;
	private File file;
	private Notification n;
	private NotificationManager nm;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		url = intent.getStringExtra("url");
		fileName = intent.getStringExtra("name");
		file = new File(intent.getStringExtra("path"));
		new download().execute();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void install(){
		if(file.exists()){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://"+file.toString()), "application/vnd.android.package-archive");
			this.startActivity(intent);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void showNotification(){
		nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		n = new Notification(R.drawable.ic_launcher, "更新", System.currentTimeMillis());
		RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.download);  
		contentView.setTextViewText(R.id.fileName, fileName);
		n.contentView = contentView;
		n.flags = Notification.FLAG_AUTO_CANCEL;
//		n.flags = Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		n.contentIntent = contentIntent;
//		n.setLatestEventInfo(this, title, text, contentIntent);
		nm.notify(R.string.app_name, n);
	}
	
	class download extends AsyncTask<Object, Object, Object>{
		
		RemoteViews cv;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showNotification();
			cv = n.contentView;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				URL u = new URL(url);
				HttpURLConnection conn = (HttpURLConnection)u.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				int num = 0;
				int count = 0;
				byte[] buf = new byte[1024];
				while((num = is.read(buf))>0){
					count += num;
					if(rate%5==0){
						rate = (int)(((float)count / length) * 100);
						cv.setTextViewText(R.id.rate, rate + "%");
						cv.setProgressBar(R.id.progress, 100, rate, false);
						nm.notify(R.string.app_name, n);
					}
					fos.write(buf, 0, num);
				}
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			n.contentView = null;
			n.flags = Notification.FLAG_AUTO_CANCEL;
			Intent intent = new Intent();
			PendingIntent contentIntent = PendingIntent.getActivity(Download.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			n.setLatestEventInfo(Download.this, getString(R.string.download_complete), getString(R.string.download_complete_msg), contentIntent);
			nm.notify(R.string.app_name, n);
			Download.this.stopSelf();
			install();
		}
		
	}

}