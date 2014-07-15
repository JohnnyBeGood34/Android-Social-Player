package com.example.android_social_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ArrayList<Song> songList;
	private ListView songView;

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
	}

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
}
