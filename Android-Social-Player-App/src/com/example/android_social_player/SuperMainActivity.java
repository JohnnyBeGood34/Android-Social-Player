package com.example.android_social_player;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SuperMainActivity extends Activity {
 
	private Button host;
	private Button client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_super_main);
		
		host = (Button) findViewById(R.id.hostRole);
		client = (Button) findViewById(R.id.clientRole);
		
		host.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SuperMainActivity.this,MainActivity.class);
				intent.putExtra("ROLE", "host");
				startActivity(intent);
			}
		});
		
		client.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SuperMainActivity.this,MainActivity.class);
				intent.putExtra("ROLE", "client");
				startActivity(intent);
			}
		});
		
		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		if (!wifi.isWifiEnabled()){
			//wifi is disabled
			createNetErrorDialog();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.super_main, menu);
		return true;
	}
	/**
	 * Ask the user to enable Wifi
	 */
	protected void createNetErrorDialog() {

	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("You need a network connection to use this application. Please turn on mobile network or Wi-Fi in Settings.")
	        .setTitle("Unable to connect")
	        .setCancelable(false)
	        .setPositiveButton("Settings",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
	                startActivity(i);
	            }
	        }
	    )
	    .setNegativeButton("Cancel",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                
	            }
	        }
	    );
	    AlertDialog alert = builder.create();
	    alert.show();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
}
