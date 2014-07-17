package wifi;

import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;
import android.widget.Toast;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver{
	
	private WifiP2pManager wifiManager;
	private Channel wifiChannel;
	private DiscoverDevices activity;
	private ArrayList<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	private int peerPosition;
	
	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,DiscoverDevices activity)
	{
		super();
		this.wifiManager = manager;
		this.wifiChannel = channel;
		this.activity = activity;
	}
	
	@Override
	public void onReceive(Context contexte, Intent intent) {
		String action = intent.getAction();
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) 
		{
            // Check to see if Wi-Fi is enabled and notify appropriate activity
			
        } 
		else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) 
        {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        	if(wifiManager != null){
        		wifiManager.requestPeers(wifiChannel, peerListListener);
        	}
        } 
		else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) 
        {
            // Respond to new connection or disconnections
        	//Check if Wifi P2P is enable
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
			if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
			{
				//Wifi P2P is enabled
				Log.i("WIFIP2P","ENABLE");
			}
			else
			{
				//Wifi P2P is disabled
				Toast.makeText(contexte, "WIFI DISABLED", Toast.LENGTH_SHORT).show();
			}
        } 
		else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) 
        {
            // Respond to this device's wifi state changing
        	
        }

	}
	
	private PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            // Out with the old, in with the new.
            peers.clear();
            peers.addAll(peerList.getDeviceList());

            // Notify listAdapter 
            activity.inflatePeerAdapter(peers);
            
            if (peers.size() == 0) {
                Log.e("PEERS EMPTY", null);
                return;
            }
        }
    };

    public void setPeerPosition(int position)
    {
    	this.peerPosition = position;
    }
    
    public void connectToPeer()
    {
    	//obtain a peer from the WifiP2pDeviceList
    	WifiP2pDevice device = peers.get(peerPosition);
    	WifiP2pConfig config = new WifiP2pConfig();
    	config.deviceAddress = device.deviceAddress;
    	wifiManager.connect(wifiChannel, config, new ActionListener() {

    	    @Override
    	    public void onSuccess() {
    	        //success logic
    	    }

    	    @Override
    	    public void onFailure(int reason) {
    	        //failure logic
    	    }
    	});

    }
}
