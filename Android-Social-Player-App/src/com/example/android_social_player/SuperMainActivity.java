package com.example.android_social_player;

import android.os.Bundle;
import android.app.Activity;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.super_main, menu);
		return true;
	}

}
