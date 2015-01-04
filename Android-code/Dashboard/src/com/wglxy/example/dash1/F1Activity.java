package com.wglxy.example.dash1;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
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
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * This is the activity for feature 1 in the dashboard application. It displays
 * some text and provides a way to get back to the home activity.
 * 
 */

public class F1Activity extends DashboardActivity {

	/**
	 * onCreate
	 * 
	 * Called when the activity is first created. This is where you should do
	 * all of your normal static set up: create views, bind data to lists, etc.
	 * This method also provides you with a Bundle containing the activity's
	 * previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().
	 * 
	 * @param savedInstanceState
	 *            Bundle
	 */
	Button btnSendSMS;
	IntentFilter intentFilter;
	String ManulLoc = "";
	private EditText textBox;
	private static final int READ_BLOCK_SIZE = 100;
	String addressString = "No address found";
	String name;
	String phone;
	Button btnSend;
	int myLatitude, myLongitude;
	GsmCellLocation location;
	int cellID, lac;
	private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			// ---display the SMS received in the TextView---
			/*
			 * TextView SMSes = (TextView) findViewById(R.id.textView1);
			 * SMSes.setText(intent.getExtras().getString("sms"));
			 */
			Toast.makeText(getBaseContext(),
					intent.getExtras().getString("sms"), Toast.LENGTH_SHORT)
					.show();
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_f1);
		setTitleFromActivityLabel(R.id.title_text);
		intentFilter = new IntentFilter();
		intentFilter.addAction("SMS_RECEIVED_ACTION");

		// get newtwork id
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		location = (GsmCellLocation) tm.getCellLocation();
		cellID = location.getCid();
		lac = location.getLac();
		Toast.makeText(getBaseContext(), "cellId" + cellID + "loc" + lac,
				Toast.LENGTH_SHORT).show();

		if (RqsLocation(cellID, lac)) {
			try {

				Toast.makeText(
						getBaseContext(),
						"lang=" + String.valueOf((float) myLatitude / 1000000)
								+ " longitude "
								+ String.valueOf((float) myLongitude / 1000000),
						Toast.LENGTH_SHORT).show();
				Geocoder gcd = new Geocoder(getBaseContext(),
						Locale.getDefault());

				// following for the directly get location
				// List<Address> addresses =
				// gcd.getFromLocation((float)myLatitude/1000000,
				// (float)myLongitude/1000000, 1);
				List<Address> addresses = gcd.getFromLocation(16.92155,
						74.57515, 1);
				// Toast.makeText(getBaseContext(),
				// "omg="+addresses.get(0).getLocality(),
				// Toast.LENGTH_SHORT).show();

				// System.out.println(addresses.get(0).getLocality());
				EditText myLocationText1;
				myLocationText1 = (EditText) findViewById(R.id.editText1);
				myLocationText1.setText(addresses.get(0).getLocality()
						.toString());

				TextView myLocationText;
				myLocationText = (TextView) findViewById(R.id.textView1);

				myLocationText.setText("Your Current Position is:\n"
						+ addresses.get(0).getLocality().toString());
				addressString = addresses.get(0).getLocality().toString();
			} catch (Exception e) {
				Toast.makeText(getBaseContext(),
						"error=" + e.getMessage().toString(),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(getBaseContext(),
					"uable to load city may be network problem",
					Toast.LENGTH_SHORT).show();
		}

		// ---register the receiver---
		registerReceiver(intentReceiver, intentFilter);

	
		
		// LocationManager locationManager;
		// String context = Context.LOCATION_SERVICE;
		// locationManager = (LocationManager)getSystemService(context);
		// String provider = LocationManager.GPS_PROVIDER;
		// Location location =locationManager.getLastKnownLocation(provider);
		// updateWithNewLocation(location);

		textBox = (EditText) findViewById(R.id.editText1);
		Button saveBtn = (Button) findViewById(R.id.button2);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String str = textBox.getText().toString();
				try {

					/*
					 * //---Internal Storage--- FileOutputStream fOut =
					 * openFileOutput("textfile.txt", MODE_WORLD_READABLE);
					 * //----------------------
					 */

					// ---SD Card Storage---
					File sdCard = Environment.getExternalStorageDirectory();
					File directory = new File(sdCard.getAbsolutePath()
							+ "/MyFiles");
					directory.mkdirs();
					File file = new File(directory, "location_name.txt");
					FileOutputStream fOut = new FileOutputStream(file);
					// ---------------------

					OutputStreamWriter osw = new OutputStreamWriter(fOut);

					// ---write the string to the file---
					osw.write(str);
					osw.flush();
					osw.close();

					// ---display file saved message---
					Toast.makeText(getBaseContext(),
							"File saved successfully!", Toast.LENGTH_SHORT)
							.show();

					// ---clears the EditText---
					textBox.setText("");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				// ==================================
			}
		});

		Button loadBtn = (Button) findViewById(R.id.button3);
		loadBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {

					/*
					 * //---Internal Storage--- FileInputStream fIn =
					 * openFileInput("textfile.txt"); InputStreamReader isr =
					 * new InputStreamReader(fIn);
					 */
					// ---SD Storage---
					File sdCard = Environment.getExternalStorageDirectory();
					File directory = new File(sdCard.getAbsolutePath()
							+ "/MyFiles");
					File file = new File(directory, "location_name.txt");
					FileInputStream fIn = new FileInputStream(file);
					InputStreamReader isr = new InputStreamReader(fIn);
					// ----------------

					char[] inputBuffer = new char[READ_BLOCK_SIZE];
					String s = "";

					int charRead;
					while ((charRead = isr.read(inputBuffer)) > 0) {
						// ---convert the chars to a String---
						String readString = String.copyValueOf(inputBuffer, 0,
								charRead);
						s += readString;

						inputBuffer = new char[READ_BLOCK_SIZE];
					}
					// ---set the EditText to the text that has been
					// read---
					textBox.setText(s);
					ManulLoc = s;
					Toast.makeText(getBaseContext(),
							"File loaded successfully!", Toast.LENGTH_SHORT)
							.show();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});

		// for loading friends numbers

		try {

			/*
			 * //---Internal Storage--- FileInputStream fIn =
			 * openFileInput("textfile.txt"); InputStreamReader isr = new
			 * InputStreamReader(fIn);
			 */
			// ---SD Storage---
			File sdCard = Environment.getExternalStorageDirectory();
			File directory = new File(sdCard.getAbsolutePath() + "/MyFiles");
			File file = new File(directory, "phone_no.txt");
			FileInputStream fIn = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fIn);
			// ----------------

			char[] inputBuffer = new char[READ_BLOCK_SIZE];
			String s = "";

			int charRead;
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// ---convert the chars to a String---
				String readString = String
						.copyValueOf(inputBuffer, 0, charRead);
				s += readString;

				inputBuffer = new char[READ_BLOCK_SIZE];
			}
			// ---set the EditText to the text that has been
			// read---

			StringTokenizer st = new StringTokenizer(s);
			while (st.hasMoreTokens()) {
				name = st.nextToken();
				phone = st.nextToken();

			}

			Toast.makeText(getBaseContext(), phone, Toast.LENGTH_SHORT).show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		if (addressString == null) {
			try {

				/*
				 * //---Internal Storage--- FileInputStream fIn =
				 * openFileInput("textfile.txt"); InputStreamReader isr = new
				 * InputStreamReader(fIn);
				 */
				// ---SD Storage---
				File sdCard = Environment.getExternalStorageDirectory();
				File directory = new File(sdCard.getAbsolutePath() + "/MyFiles");
				File file = new File(directory, "location_name.txt");
				FileInputStream fIn = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fIn);
				// ----------------

				char[] inputBuffer = new char[READ_BLOCK_SIZE];
				String s = "";

				int charRead;
				while ((charRead = isr.read(inputBuffer)) > 0) {
					// ---convert the chars to a String---
					String readString = String.copyValueOf(inputBuffer, 0,
							charRead);
					s += readString;

					inputBuffer = new char[READ_BLOCK_SIZE];
				}
				// ---set the EditText to the text that has been
				// read---
				textBox.setText(s);
				ManulLoc = s;
				Toast.makeText(getBaseContext(), ManulLoc, Toast.LENGTH_SHORT)
						.show();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			addressString = ManulLoc;
		}
		// sending sms
		btnSendSMS = (Button) findViewById(R.id.button1);
		btnSendSMS.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendSMS("9421973083", "Amb " + addressString);

				// sendSMS(phone, "I need Help ");
				/*
				 * Intent i = new Intent(android.content.Intent.ACTION_VIEW);
				 * i.putExtra("address", "5556; 5558; 5560");
				 * i.putExtra("sms_body", "Hello my friends!");
				 * i.setType("vnd.android-dir/mms-sms"); startActivity(i);
				 */
			}
		});

		btnSend = (Button) findViewById(R.id.button4);
		btnSend.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				sendSMS(phone, "I need Help " + addressString);

			}
		});

	}

	/*
	 * private void updateWithNewLocation(Location location) { String
	 * latLongString; TextView myLocationText; myLocationText =
	 * (TextView)findViewById(R.id.textView1);
	 * 
	 * if (location != null) { double lat = location.getLatitude(); double lng =
	 * location.getLongitude(); latLongString = "Lat:" + lat + "\nLong:" + lng;
	 * double latitude = location.getLatitude(); double longitude =
	 * location.getLongitude(); Geocoder gc = new Geocoder(this,
	 * Locale.getDefault());
	 * 
	 * 
	 * try { //---------code by pavan pawar
	 * 
	 * 
	 * 
	 * //-----------
	 * 
	 * List<Address> addresses = gc.getFromLocation((float)myLatitude/1000000,
	 * (float)myLongitude/1000000, 1); //List<Address> addresses =
	 * gc.getFromLocation(latitude, longitude, 1); //original by rushi
	 * StringBuilder sb = new StringBuilder(); if (addresses.size() > 0) {
	 * 
	 * Address address = addresses.get(0); Toast.makeText(getBaseContext(),
	 * "omg="+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
	 * sb.append(addresses.get(0).getLocality()).append("\n"); // modified by
	 * pavan m. pawar 8 /3/2013 //for (int i = 0; i <
	 * address.getMaxAddressLineIndex(); i++)
	 * //sb.append(address.getAddressLine(i)).append("\n");
	 * //sb.append(address.getLocality()).append("\n");
	 * //sb.append(address.getPostalCode()).append("\n");
	 * //sb.append(address.getCountryName()); } addressString = sb.toString(); }
	 * catch (IOException e) {} } else { latLongString = "No location found";
	 * addressString=null; }
	 * 
	 * 
	 * myLocationText.setText("Your Current Position is:\n" + latLongString +
	 * "\n" + addressString);
	 * 
	 * 
	 * }
	 */
	@Override
	protected void onResume() {
		// ---register the receiver---
		// registerReceiver(intentReceiver, intentFilter);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// ---unregister the receiver---
		// unregisterReceiver(intentReceiver);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// ---unregister the receiver---
		unregisterReceiver(intentReceiver);
		super.onPause();
	}

	// --------by pavan m pawar get location online
	private Boolean RqsLocation(int cid, int lac) {

		Boolean result = false;

		String urlmmap = "http://www.google.com/glm/mmap";

		try {
			URL url = new URL(urlmmap);
			URLConnection conn = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.connect();

			OutputStream outputStream = httpConn.getOutputStream();
			WriteData(outputStream, cid, lac);

			InputStream inputStream = httpConn.getInputStream();
			DataInputStream dataInputStream = new DataInputStream(inputStream);

			dataInputStream.readShort();
			dataInputStream.readByte();
			int code = dataInputStream.readInt();
			if (code == 0) {
				myLatitude = dataInputStream.readInt();
				myLongitude = dataInputStream.readInt();

				result = true;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	private void WriteData(OutputStream out, int cid, int lac)
			throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		dataOutputStream.writeShort(21);
		dataOutputStream.writeLong(0);
		dataOutputStream.writeUTF("en");
		dataOutputStream.writeUTF("Android");
		dataOutputStream.writeUTF("1.0");
		dataOutputStream.writeUTF("Web");
		dataOutputStream.writeByte(27);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(3);
		dataOutputStream.writeUTF("");

		dataOutputStream.writeInt(cid);
		dataOutputStream.writeInt(lac);

		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.flush();
	}

	// -----------

	private void sendSMS(String phoneNumber, String message) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
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

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
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
