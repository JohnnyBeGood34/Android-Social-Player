package com.example.android_social_player;

import java.util.ArrayList;

import wifi.WiFiDirectBroadcastReceiver;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;

public class DiscoverDevices extends Activity implements IActivityDiscoverDevice{
	
	WifiP2pManager wifiManager;
	Channel wifiChannel;
	BroadcastReceiver wifiReceiver;
	IntentFilter intentFilter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover_devices);
		
		wifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		wifiChannel = wifiManager.initialize(this, getMainLooper(), null);
		wifiReceiver = new WiFiDirectBroadcastReceiver(wifiManager,wifiChannel,this);
		
		intentFilter = new IntentFilter();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		
		/**
		 * Discovering peers 
		 * Asynchronous Call
		 */
		wifiManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener(){
			//On failure callback
			@Override
			public void onFailure(int reason) {
				Log.e("ERROR DURING DISCOVERING PEERS----",String.valueOf(reason));
			}
			//On success Callback
			@Override
			public void onSuccess() {
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.discover_devices, menu);
		return true;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		registerReceiver(wifiReceiver,intentFilter);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(wifiReceiver);
	}

	@Override
	public void inflatePeerAdapter(ArrayList<WifiP2pDevice> peers) {
		
	}
}
