package com.gos.iccardone;

import java.util.Calendar;

import com.gos.iccardone.helper.GOSHelper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OperatinActivity extends BaseActivity {
	
	//当前选用的项
	private int currentSelectDatePicker = 0;
	//用来保存年月日：  
    private int mYear;  
    private int mMonth;  
    private int mDay;  
    //声明一个独一无二的标识，来作为要显示DatePicker的Dialog的ID：  
    static final int DATE_DIALOG_ID = 0;  
    /**开始日期*/
    private TextView	startDateTextView;
    /**结束日期*/
    private TextView	endDateTextView;
    /**查询类型*/
    private String		queryTypeString = "";
    /**按钮*/
    private Button		queryOperatinButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operatin);
		queryTypeString = getIntent().getStringExtra("businessType");
		findview();
	}
	
	protected void findview() {
		startDateTextView = (TextView)findViewById(R.id.startDateTextView);
		startDateTextView.setFocusable(false);   
		startDateTextView.setFocusableInTouchMode(false);   
		startDateTextView.requestFocus();
		startDateTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //调用Activity类的方法来显示Dialog:调用这个方法会允许Activity管理该Dialog的生命周期，  
                //并会调用 onCreateDialog(int)回调函数来请求一个Dialog  
				currentSelectDatePicker = 0;
                showDialog(DATE_DIALOG_ID);  
			}
		});
		//获得当前的日期：  
        final Calendar currentDate = Calendar.getInstance();  
        mYear = currentDate.get(Calendar.YEAR);  
        mMonth = currentDate.get(Calendar.MONTH);  
        mDay = currentDate.get(Calendar.DAY_OF_MONTH);  

        endDateTextView = (TextView)findViewById(R.id.endDateTextView);
        endDateTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                //调用Activity类的方法来显示Dialog:调用这个方法会允许Activity管理该Dialog的生命周期，  
                //并会调用 onCreateDialog(int)回调函数来请求一个Dialog  
				currentSelectDatePicker = 1;
                showDialog(DATE_DIALOG_ID);  
			}
		});
		//获得当前的日期：  
        final Calendar currentDateOne = Calendar.getInstance();  
        mYear = currentDateOne.get(Calendar.YEAR);  
        mMonth = currentDateOne.get(Calendar.MONTH);  
        mDay = currentDateOne.get(Calendar.DAY_OF_MONTH); 
        
        queryOperatinButton = (Button)findViewById(R.id.queryOperatinButton);
        if (queryTypeString.equals("1")) {
			queryOperatinButton.setText("查询转账明细");
		}
        else {
        	queryOperatinButton.setText("查询交易明细");
		}
        queryOperatinButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEmpty();
			}
		});
	}
	 //需要定义弹出的DatePicker对话框的事件监听器：  
    private DatePickerDialog.OnDateSetListener mDateSetListener =new OnDateSetListener() {  
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
        	
            mYear = year;  
            mMonth = monthOfYear;  
            mDay = dayOfMonth;
            //设置文本的内容：  
            if (currentSelectDatePicker == 0) {
            	startDateTextView.setText(new StringBuilder()  
                .append(mYear).append("年")  
                .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始  
                .append(mDay).append("日"));  
			}
            else if (currentSelectDatePicker == 1) {
            	endDateTextView.setText(new StringBuilder()  
                .append(mYear).append("年")  
                .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始  
                .append(mDay).append("日")); 
			}
            
        }  
    }; 
    /** 
     * 当Activity调用showDialog函数时会触发该函数的调用： 
     */  
    @Override  
    protected Dialog onCreateDialog(int id) {  
       switch (id) {  
        case DATE_DIALOG_ID:  
            return new DatePickerDialog(OperatinActivity.this, mDateSetListener, mYear, mMonth, mDay);  
        }  
        return null;  
    }
    /**为空判断*/
    protected void isEmpty() {
		String startDateString = startDateTextView.getText().toString().replace("年", "-").replace("月", "-").replace("日", "");
		String formatStr="yyyyMMdd";//然后再格式化为想要的格式
        String dateFromatStr="yyyy-MM-dd";//先把字符串按这个格式格式化为日期类型
        String dateOneString = GOSHelper.StringToDate(startDateString, dateFromatStr,formatStr);
		
        String endDateString = endDateTextView.getText().toString().replace("年", "-").replace("月", "-").replace("日", "");
        String dateTwoString = GOSHelper.StringToDate(endDateString, dateFromatStr,formatStr);
        
        
		if (startDateString == null || startDateString.equals("")) {
			Toast.makeText(this, "开始日期不能为空", Toast.LENGTH_SHORT).show();
		}
		else if (endDateString == null || endDateString.equals("")) {
			Toast.makeText(this, "结束日期不能为空", Toast.LENGTH_SHORT).show();
		}
		else if ((dateTwoString.compareTo(dateOneString)) < 0) {
			Toast.makeText(this, "结束日期不能比开始日期小", Toast.LENGTH_SHORT).show();
		}
		else {
			Intent oneIntent = new Intent(OperatinActivity.this, OperatinResultActivity.class);
			oneIntent.putExtra("startdate", dateOneString);
			oneIntent.putExtra("enddate", dateTwoString);
			oneIntent.putExtra("businessType", queryTypeString);
			OperatinActivity.this.startActivity(oneIntent);
		}
	}

	
}
