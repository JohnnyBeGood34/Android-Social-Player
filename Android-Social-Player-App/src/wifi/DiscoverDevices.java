package wifi;

import java.util.ArrayList;

import com.example.android_social_player.R;

import CustomAdapters.WifiPeerListAdapter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class DiscoverDevices extends Activity implements IActivityDiscoverDevice{
	
	WifiP2pManager wifiManager;
	Channel wifiChannel;
	WiFiDirectBroadcastReceiver wifiReceiver;
	IntentFilter intentFilter;
	private ListView peerView;

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
		peerView = (ListView) findViewById(R.id.peer_list);
		WifiPeerListAdapter peerAdapter = new WifiPeerListAdapter(this, peers);
		peerView.setAdapter(peerAdapter);
	}
	/**
	 * Try to connect to peer selected
	 * Method invoked by picking a peer from list of peers
	 */
	public void peerPicked(View view)
	{
		int peerPosition = Integer.parseInt(view.getTag().toString());
		wifiReceiver.setPeerPosition(peerPosition);
		wifiReceiver.connectToPeer();
	}
}
