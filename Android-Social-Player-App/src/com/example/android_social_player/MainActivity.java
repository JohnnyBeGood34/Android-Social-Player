package com.example.android_social_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import com.example.android_social_player.MusicService.MusicBinder;

import wifi.DiscoverDevices;


import BusinessLogicLayer.Song;
import CustomAdapters.SongAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;

public class MainActivity extends Activity implements MediaPlayerControl {
	//Song list view
	private ArrayList<Song> songList;
	private ListView songView;
	// Service class
	private MusicService musicService;
	private Intent playerIntent;
	private boolean musicBound = false;
	private MusicController controllerMusic;
	private boolean paused = false, playbackPaused = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/****PLAYER****/
		songView = (ListView) findViewById(R.id.song_list);
		songList = new ArrayList<Song>();

		getSongList();

		// To display the list of song, we sprt the data alphabetically
		Collections.sort(songList, new Comparator<Song>() {
			public int compare(Song one, Song two) {
				return one.getTitle().compareTo(two.getTitle());
			}
		});

		// Set view with custom adapter
		SongAdapter songAdapter = new SongAdapter(this, songList);
		songView.setAdapter(songAdapter);

		setControllerMusic();
	}

	private void setControllerMusic() {
		controllerMusic = new MusicController(this);
		controllerMusic.setPrevNextListeners(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playNext();
			}
		}, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playPrev();
			}
		});
		
		controllerMusic.setMediaPlayer(this);
		controllerMusic.setAnchorView(findViewById(R.id.song_list));
		controllerMusic.setEnabled(true);
	}

	private void playNext() {
		musicService.playNext();
		if (playbackPaused) {
			setControllerMusic();
			playbackPaused = false;
		}
		controllerMusic.show(0);
	}

	private void playPrev() {
		musicService.playPrev();
		if (playbackPaused) {
			setControllerMusic();
			playbackPaused = false;
		}
		controllerMusic.show(0);
	}

	/**
	 * Connect to the music service We play the music with MusicService class
	 * but we control it from the main Activity The callback methods will inform
	 * the class when the activity instance has successfully connected to the
	 * service instance. Then the activity can interact with it
	 */
	private ServiceConnection musicConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBinder binder = (MusicBinder) service;
			// Get service
			musicService = binder.getService();
			// Pass list of song
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// If the user click on the shuffle button
		case R.id.action_shuffle:
			musicService.setShuffle();
			break;
		// If the user click on the end button
		case R.id.action_end:
			stopService(playerIntent);
			musicService = null;
			System.exit(0);
			break;
		case R.id.action_wifi:
			Intent wifiIntent = new Intent(MainActivity.this,DiscoverDevices.class);
			startActivity(wifiIntent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		stopService(playerIntent);
		musicService = null;
		super.onDestroy();
	}

	/**
	 * Helper method to retrieve the audio file informations
	 */
	@SuppressLint("DefaultLocale")
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
			int durationColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.DURATION);
			
			int albumIdColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM_ID);
			// Then, add song to the list
			do {
				long songId = musicCursor.getLong(idColumn);
				String songTitle = musicCursor.getString(titleColumn);
				String songArtist = musicCursor.getString(artistColumn);
				long songDuration = musicCursor.getLong(durationColumn);
				long albumId = musicCursor.getLong(albumIdColumn);
				//Convert milliseconds to h:m:s
				String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(songDuration),
			            TimeUnit.MILLISECONDS.toMinutes(songDuration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(songDuration)),
			            TimeUnit.MILLISECONDS.toSeconds(songDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(songDuration)));
				
				songList.add(new Song(songId, songTitle, songArtist,hms));
				
				/*Add album photo*/
				Cursor cursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,new String[] { MediaStore.Audio.Albums._ID,MediaStore.Audio.Albums.ALBUM_ART },MediaStore.Audio.Albums._ID + "=?",new String[] { String.valueOf(albumId) }, null);
				if (cursor.moveToFirst()) {
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
					Log.i("ALBUM ART-------------",path);
					Uri uriPath = Uri.parse(path);
					ImageView imageAlbum = (ImageView)findViewById(R.id.album_img);
					Bitmap bitmap = null;
			        try {
			            bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), uriPath);
			            imageAlbum.setImageBitmap(bitmap);
			        } catch (Exception exception) {
			            Log.e("ERROR DURING SETTING IMAGE ALBUM",exception.toString());
			        }
				}
			} while (musicCursor.moveToNext());
		}
	}

	/**
	 * When the activity starts, we create the intent object if it doesn't
	 * exists yet, bind and start to it.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if (playerIntent == null) {
			playerIntent = new Intent(this, MusicService.class);
			bindService(playerIntent, musicConnection, Context.BIND_AUTO_CREATE);
			startService(playerIntent);
		}
	}

	/**
	 * OnClick method declared in song.xml OnClick on the song list
	 */
	public void songPicked(View view) {
		// Set the song position as the tag for each item in the list view
		musicService.setSong(Integer.parseInt(view.getTag().toString()));
		musicService.playSong();
		if (playbackPaused) {
			setControllerMusic();
			playbackPaused = false;
		}
		controllerMusic.show(0);
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getAudioSessionId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBufferPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		if (musicService != null && musicBound && musicService.isPng())
			return musicService.getPosn();
		else
			return 0;
	}

	@Override
	public int getDuration() {
		if (musicService != null && musicBound && musicService.isPng())
			return musicService.getDur();
		else
			return 0;
	}

	@Override
	public boolean isPlaying() {
		if (musicService != null && musicBound) {
			return musicService.isPng();
		}
		return false;
	}

	@Override
	public void pause() {
		playbackPaused = true;
		musicService.pausePlayer();
	}

	@Override
	public void seekTo(int pos) {
		musicService.seek(pos);
	}

	@Override
	public void start() {
		musicService.go();
	}

	@Override
	protected void onPause() {
		super.onPause();
		paused = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (paused) {
			setControllerMusic();
			paused = false;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		controllerMusic.hide();
		musicService.stopPlayer();
		musicService.releasePlayer();
	}
	
}
