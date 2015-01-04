
package com.wglxy.example.dash1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

import android.widget.TimePicker;
import java.util.Calendar;

import android.os.Bundle;
import android.telephony.SmsManager;

/**
 * This is the activity for feature 6 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class F6Activity extends DashboardActivity 
{

/**
 * onCreate
 *
 * Called when the activity is first created. 
 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
 * 
 * Always followed by onStart().
 *
 * @param savedInstanceState Bundle
 */
	IntentFilter intentFilter;	
	
	Button btnSendSMS;	
	DatePicker datePicker;
	 int yr, month, day;
	 static final int DATE_DIALOG_ID = 1;
	  private EditText regno;    
	  private EditText mname;    
	    String rno="";  
        String smname="";
        String result="";
	  private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	
	    	
	        //---display the SMS received in the TextView---
	    /*	TextView SMSes = (TextView) findViewById(R.id.textView1); 
	    	SMSes.setText(intent.getExtras().getString("sms"));
	  */
	    	  Toast.makeText(getBaseContext(), intent.getExtras().getString("sms"),  Toast.LENGTH_SHORT).show();
	    }
	};
	
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.activity_f6);
    setTitleFromActivityLabel (R.id.title_text);
    intentFilter = new IntentFilter();
    intentFilter.addAction("SMS_RECEIVED_ACTION");
    
    //---register the receiver---
    registerReceiver(intentReceiver, intentFilter);
    
    Calendar today = Calendar.getInstance();
    yr = today.get(Calendar.YEAR);
    month = today.get(Calendar.MONTH);
    day = today.get(Calendar.DAY_OF_MONTH);  
   
    regno = (EditText) findViewById(R.id.editText1);
    mname =(EditText) findViewById(R.id.editText2);
    
    
    datePicker = (DatePicker) findViewById(R.id.datePicker1);
    
    btnSendSMS = (Button) findViewById(R.id.button1);        
    btnSendSMS.setOnClickListener(new View.OnClickListener() 
    {
        public void onClick(View v) 
        {    
        	
       rno = regno.getText().toString();
       smname=mname.getText().toString();
        	result= "reg:"+rno+","+smname+"," + datePicker.getMonth() + 1 + 
            		"/" + datePicker.getDayOfMonth() +
            		"/" + datePicker.getYear() ;
        	
   /*     	Toast.makeText(getBaseContext(),
            		"Date selected:" + datePicker.getMonth() + 1 + 
            		"/" + datePicker.getDayOfMonth() +
            		"/" + datePicker.getYear() + "\n"+rno+"\n"+smname  , 
                    Toast.LENGTH_SHORT).show();
                    */
                    
        	Toast.makeText(getBaseContext(),
            		"Date selected:" + result , 
                    Toast.LENGTH_SHORT).show();
          
            sendSMS("9421973083", "Registration "+result);
        	
            
            
            /*
            Intent i = new 
                Intent(android.content.Intent.ACTION_VIEW);
            i.putExtra("address", "5556; 5558; 5560");
            i.putExtra("sms_body", "Hello my friends!");
            i.setType("vnd.android-dir/mms-sms");
            startActivity(i);
            */                            
        }
    });
    
}

@Override
protected void onResume() {
	//---register the receiver---
    //registerReceiver(intentReceiver, intentFilter);
    super.onResume();
}

@Override
protected void onPause() {
	//---unregister the receiver---
    //unregisterReceiver(intentReceiver);
    super.onPause();
}
    
@Override
protected void onDestroy() {
	//---unregister the receiver---
    unregisterReceiver(intentReceiver);
    super.onPause();
}
 
private void sendSMS(String phoneNumber, String message)
{        
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";

    PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
        new Intent(SENT), 0);

    PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
        new Intent(DELIVERED), 0);

    //---when the SMS has been sent---
    registerReceiver(new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode())
            {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS sent", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "Generic failure", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getBaseContext(), "No service", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), "Null PDU", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "Radio off", 
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }, new IntentFilter(SENT));

    //---when the SMS has been delivered---
    registerReceiver(new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode())
            {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS delivered", 
                            Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getBaseContext(), "SMS not delivered", 
                            Toast.LENGTH_SHORT).show();
                    break;                        
            }
        }
    }, new IntentFilter(DELIVERED));        

    SmsManager sms = SmsManager.getDefault();
    sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
} 

    
} // end class
