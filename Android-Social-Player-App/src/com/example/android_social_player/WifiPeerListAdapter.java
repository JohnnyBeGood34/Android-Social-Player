package com.example.android_social_player;

import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WifiPeerListAdapter extends BaseAdapter {

	private ArrayList<WifiP2pDevice> arrayDevices;
	private LayoutInflater layoutInflater;

	public WifiPeerListAdapter(Context contexte,
			ArrayList<WifiP2pDevice> devices) {
		this.arrayDevices = devices;
		this.layoutInflater = LayoutInflater.from(contexte);
	}

	@Override
	public int getCount() {
		return arrayDevices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Song Layout
		LinearLayout peerLayout = (LinearLayout) layoutInflater.inflate(
				R.layout.song, parent, false);
		// Get peer device name
		TextView peerView = (TextView) peerLayout.findViewById(R.id.device_title);
		WifiP2pDevice currentPeer = arrayDevices.get(position);

		peerView.setText(currentPeer.deviceName);
		
		peerLayout.setTag(position);

		return peerLayout;
	}

}
