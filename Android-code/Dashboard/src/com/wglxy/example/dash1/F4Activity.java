
package com.wglxy.example.dash1;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.telephony.SmsManager;
/**
 * This is the activity for feature 4 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class F4Activity extends DashboardActivity 
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
	String[] presidents= {"Maleria","typhoid","Swine flu","Flu","Cancer","pneumonia","AIDS","chickenpox","hookworm"};
	Button btnSendSMS;		
	IntentFilter intentFilter;	
	 int index;
	 String tempSelected="";
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
    setContentView (R.layout.activity_f4);
    setTitleFromActivityLabel (R.id.title_text);
    intentFilter = new IntentFilter();
    intentFilter.addAction("SMS_RECEIVED_ACTION");

    //---register the receiver---
    registerReceiver(intentReceiver, intentFilter);
            
        Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item, presidents);
 
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(new OnItemSelectedListener()
        {
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
            {
               index = arg0.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), 
                    "You have selected item : " + presidents[index], 
                    Toast.LENGTH_SHORT).show(); 
                tempSelected="Immu "+presidents[index];
              //  sendSMS("8237781377", "Immu "+presidents[index]);
            }

			public void onNothingSelected(AdapterView<?> arg0) {} 
        });
        
        btnSendSMS = (Button) findViewById(R.id.button2);        
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {     
            	
            		sendSMS("9421973083","immunization "+tempSelected);
            	
            	
                                            
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
