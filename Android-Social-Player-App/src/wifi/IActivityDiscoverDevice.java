package wifi;

import java.util.ArrayList;

import android.net.wifi.p2p.WifiP2pDevice;

public interface IActivityDiscoverDevice {
	public void inflatePeerAdapter(ArrayList<WifiP2pDevice> peers);
}
