
package com.wglxy.example.dash1;
/*
 * student file
 * 
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.OutputStream;


import java.io.InputStream;
import java.io.BufferedReader;
import java.util.List;
import java.util.Locale;
/**
 * This is the activity for feature 3 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class F3Activity extends DashboardActivity 
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
	Button btnSendSMS;		
	IntentFilter intentFilter;	
	String addressString = "No address found";
	String ManulLoc="";
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
	 
	private static final int READ_BLOCK_SIZE = 100;
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.activity_f3);
    setTitleFromActivityLabel (R.id.title_text);
  
    intentFilter = new IntentFilter();
    intentFilter.addAction("SMS_RECEIVED_ACTION");

    //---register the receiver---
    registerReceiver(intentReceiver, intentFilter);
    LocationManager locationManager;
   	String context = Context.LOCATION_SERVICE;
   	locationManager = (LocationManager)getSystemService(context);
   	String provider = LocationManager.GPS_PROVIDER;
   	Location location =locationManager.getLastKnownLocation(provider);
   	updateWithNewLocation(location);
    
 	//file reading for location
   	
    try 
    {  
    	
    	/*//---Internal Storage---
        FileInputStream fIn = openFileInput("textfile.txt");
        InputStreamReader isr = new InputStreamReader(fIn); 
        */
        //---SD Storage---                	
    	File sdCard = Environment.getExternalStorageDirectory();
    	File directory = new File (sdCard.getAbsolutePath() + "/MyFiles");
    	File file = new File(directory, "location_name.txt");
    	FileInputStream fIn = new FileInputStream(file);                	
        InputStreamReader isr = new InputStreamReader(fIn);
    	//----------------
       
        char[] inputBuffer = new char[READ_BLOCK_SIZE];
        String s = "";

        int charRead;
        while ((charRead = isr.read(inputBuffer))>0)
        {                    
            //---convert the chars to a String---
            String readString = 
                String.copyValueOf(inputBuffer, 0, charRead);                    
            s += readString;

           inputBuffer = new char[READ_BLOCK_SIZE];
        } 
        //---set the EditText to the text that has been 
        // read---                                
      
        ManulLoc=s;
        Toast.makeText(getBaseContext(),
            ManulLoc, 
            Toast.LENGTH_SHORT).show();
        TextView myLocationText;
        myLocationText = (TextView)findViewById(R.id.textView1);
        myLocationText.setText("Your Current Position is:\n" +
        		ManulLoc + "....\n" );
        		
    } 
    catch (IOException ioe) { 
        ioe.printStackTrace(); 
    } 
   	if(addressString==null)
   	{
   		addressString=ManulLoc;
   	}
   	
    Button Activity77 = (Button) findViewById(R.id.button1);
	Activity77.setOnClickListener(new View.OnClickListener() {
	public void onClick(View view) {
		
		sendSMS("9421973083", "Student "+addressString);
	}
	}
	);
    
    
}
    
private void updateWithNewLocation(Location location) {
	String latLongString;
	TextView myLocationText;
	myLocationText = (TextView)findViewById(R.id.textView1);
	
	if (location != null) {
	double lat = location.getLatitude();
	double lng = location.getLongitude();
	latLongString = "Lat:" + lat + "\nLong:" + lng;
	double latitude = location.getLatitude();
	double longitude = location.getLongitude();
	Geocoder gc = new Geocoder(this, Locale.getDefault());
	try {
	List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
	StringBuilder sb = new StringBuilder();
	if (addresses.size() > 0) {
	Address address = addresses.get(0);
	for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
	sb.append(address.getAddressLine(i)).append("\n");
	sb.append(address.getLocality()).append("\n");
	sb.append(address.getPostalCode()).append("\n");
	sb.append(address.getCountryName());
	}
	addressString = sb.toString();
	} catch (IOException e) {}
	} else {
	latLongString = "No location found";
	addressString=null;
	}
	//myLocationText.setText("Your Current Position is:\n" +
	//latLongString + "\n" + addressString);
	
	
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
