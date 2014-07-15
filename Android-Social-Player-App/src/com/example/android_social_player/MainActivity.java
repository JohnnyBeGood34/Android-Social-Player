package com.example.android_social_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.example.android_social_player.MusicService.MusicBinder;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ArrayList<Song> songList;
	private ListView songView;
	//Service class
	private MusicService musicService;
	private Intent playerIntent;
	private boolean musicBound = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		songView = (ListView) findViewById(R.id.song_list);
		songList = new ArrayList<Song>();
		
		getSongList();
		
		//To display the list of song, we sprt the data alphabetically
		Collections.sort(songList,new Comparator<Song>(){
			public int compare(Song one, Song two){
				return one.getTitle().compareTo(two.getTitle());
			}
		});
		
		//Set view with custom adapter
		SongAdapter songAdapter = new SongAdapter(this,songList);
		songView.setAdapter(songAdapter);
	}

	/**
	 * Connect to the music service
	 * We play the music with MusicService class but we control it from the main Activity
	 * The callback methods will inform the class when the activity instance has successfully connected to the service instance.
	 * Then the activity can interact with it
	 */
	private ServiceConnection musicConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBinder binder = (MusicBinder)service;
			//Get service
			musicService = binder.getService();
			//Pass list of song
			musicService.setList(songList);
			musicBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			musicBound = false;
		}
		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Helper method to retrieve the audio file informations
	 */
	public void getSongList() {
		ContentResolver musicResolver = getContentResolver();
		// Get Music list URI
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		// Cursor to look over the User's music list
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null,
				null);

		// Check if we have valid data
		if (musicCursor != null && musicCursor.moveToFirst()) {
			// Get columns
			int titleColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
			int artistColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

			// Then, add song to the list
			do {
				long songId = musicCursor.getLong(idColumn);
				String songTitle = musicCursor.getString(titleColumn);
				String songArtist = musicCursor.getString(artistColumn);

				songList.add(new Song(songId, songTitle, songArtist));

			} while (musicCursor.moveToNext());
		}
	}
	/**
	 * When the activity starts, we create the intent object if it doesn't exists yet, bind and start to it.
	 */
	@Override
	protected void onStart(){
		super.onStart();
		if(playerIntent == null)
		{
			playerIntent = new Intent(this,MusicService.class);
			bindService(playerIntent, musicConnection, Context.BIND_AUTO_CREATE);
			startService(playerIntent);
		}
	}
}
