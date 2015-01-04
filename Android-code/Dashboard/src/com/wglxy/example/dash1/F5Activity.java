
package com.wglxy.example.dash1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
 * This is the activity for feature 5 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class F5Activity extends DashboardActivity 
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
	
	 EditText textBox1; 
	
	
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.activity_f5);
    setTitleFromActivityLabel (R.id.title_text);
    
    //for saving the friend list
    
    textBox1 = (EditText) findViewById(R.id.editText1);
    Button save = (Button) findViewById(R.id.button1);
    save.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {         
        	
        	String str = textBox1.getText().toString();
            try 
            {                 	
            
            	/*//---Internal Storage---
                FileOutputStream fOut = 
                    openFileOutput("textfile.txt", 
                    MODE_WORLD_READABLE);
                //----------------------*/ 
                                   
               //---SD Card Storage---
                File sdCard = Environment.getExternalStorageDirectory();
            	File directory = new File (sdCard.getAbsolutePath() + "/MyFiles");
            	directory.mkdirs();
            	File file = new File(directory, "phone_no.txt"); 
                FileOutputStream fOut = new FileOutputStream(file);
                //---------------------
                
                OutputStreamWriter osw = new 
                OutputStreamWriter(fOut);
                
                //---write the string to the file--- 
                osw.write(str);              
                osw.flush(); 
                osw.close();
                
                //---display file saved message---
                Toast.makeText(getBaseContext(), 
                    "File saved successfully!", 
                    Toast.LENGTH_SHORT).show();
             
                //---clears the EditText---
                textBox1.setText("");
             } 
             catch (IOException ioe) 
             { 
                 ioe.printStackTrace(); 
             }  
             //==================================
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
   
    super.onPause();
}



    
} // end class
