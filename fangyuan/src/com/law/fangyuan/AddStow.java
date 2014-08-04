package com.law.fangyuan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.widget.Toast;

public class AddStow {
	private String id;
	private Activity activity;
	private ProgressDialog dialog;
	
	public AddStow(Activity activity, String id) {
		this.id = id;
		this.activity = activity;
		new Request().execute();
	}
	
	public void Complete(String result){
		Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_LONG).show();
	}
	
	Runnable loading = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
	        dialog = new ProgressDialog(activity);
	        dialog.setMessage(activity.getString(R.string.stowing));
	        dialog.setIndeterminate(true);
	        dialog.setCancelable(true);
	        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	        dialog.show();
		}
    	
    };
    
	class Request extends AsyncTask<Object, Object, Object>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading.run();
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			String result = "";
			Http http = new Http(activity);
			result = http.GET("http://www.60886666.com/plus/stow.php?android=1&aid="+id);
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			Complete(result.toString());
		}		
	}
}
