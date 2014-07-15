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
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;

public class MainActivity extends Activity implements MediaPlayerControl {

	private ArrayList<Song> songList;
	private ListView songView;
	// Service class
	private MusicService musicService;
	private Intent playerIntent;
	private boolean musicBound = false;
	private MusicController controllerMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		controllerMusic.show(0);
	}

	private void playPrev() {
		musicService.playPrev();
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
			break;
		// If the user click on the end button
		case R.id.action_end:
			stopService(playerIntent);
			musicService = null;
			System.exit(0);
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
}
