package com.example.android_social_player;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SongAdapter extends BaseAdapter{
	
	private ArrayList<Song> songs;
	private LayoutInflater songInflater;
	
	//Constructor
	public SongAdapter(Context contexte,ArrayList<Song> theSongs)
	{
		this.songs = theSongs;
		this.songInflater = LayoutInflater.from(contexte);
	}
	
	@Override
	public int getCount() {
		return songs.size();
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
		//Song Layout
		LinearLayout songLayout = (LinearLayout)songInflater.inflate(R.layout.song, parent,false);
		//Get titles and artists views
		TextView songView = (TextView)songLayout.findViewById(R.id.song_title);
		TextView artistView = (TextView)songLayout.findViewById(R.id.song_artist);
		
		Song currentSong = songs.get(position);
		
		songView.setText(currentSong.getTitle());
		artistView.setText(currentSong.getArtist());
		
		songLayout.setTag(position);
		
		return songLayout;
	}

}
