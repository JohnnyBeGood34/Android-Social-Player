package com.example.android_social_player;

import java.util.ArrayList;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

/**
 * Music service
 * @author coxyJo
 * All interfaces we are implementing will aid the process of interacting with the MediaPlayer class
 */
public class MusicService extends Service implements OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
	//The media player
	private MediaPlayer player;
	//Song list
	private ArrayList<Song> songs;
	//Current position
	private int songPosition;
	//Instance variable witch represent the inner Binder, used in the onBind() method
	private final IBinder musicBind = new MusicBinder();
	
	public void onCreate()
	{
		//Create the service
		super.onCreate();
		//Initialize the position
		this.songPosition = 0;
		//Create the player
		this.player = new MediaPlayer();
		//initialization
		initMusicPlayer();
	}
	/**
	 * Initialize the MediaPlayer class
	 */
	public void initMusicPlayer()
	{
		//player properties
		//Allow the player to play back music when the user device becomes idle
		this.player.setWakeMode(getApplicationContext(),PowerManager.PARTIAL_WAKE_LOCK);
		//Say we will play music
		this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//Set the class as listener for prepared, song completed playback and when error is thrown
		this.player.setOnPreparedListener(this);
		this.player.setOnCompletionListener(this);
		this.player.setOnErrorListener(this);
	}
	
	/**
	 * Pass list of songs from the activity
	 * @param theSongs
	 */
	public void setList(ArrayList<Song> theSongs){
		this.songs = theSongs;
	}
	/**
	 * Snippet for bind
	 * @author coxyJo
	 *
	 */
	public class MusicBinder extends Binder{
		MusicService getService(){
			return MusicService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return musicBind;
	}
	/**
	 * Execute when the user exit the app, then we have to stop the service
	 */
	@Override
	public boolean onUnbind(Intent intent)
	{
		player.stop();
		player.release();
		return false;
	}
	
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		//Start playback
		mp.start();		
	}
	/**
	 * Play a music
	 */
	public void playSong()
	{
		//Resetting the player
		player.reset();
		//Get song
		Song playSong = songs.get(songPosition);
		
		long currentSong = playSong.getId();
		//Set URI
		Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong);
		try{
			player.setDataSource(getApplicationContext(),trackUri);
		}
		catch(Exception e){
			Log.e("MUSIC SERVICE","EXCEPTION setting data resources",e);
		}
		//Prepare the player for playback Asynchronous
		//When the player is prepared, the onPrepare method will be executed
		player.prepareAsync();
	}
	
	/**
	 * Set the current song
	 * @param songIndex
	 */
	public void setSong(int songIndex){
		songPosition = songIndex;
	}
	/**
	 * Define methods for player accessing through activity
	 * @return
	 */
	public int getPosn(){
		return player.getCurrentPosition();
	}
	
	public int getDur(){
		return player.getDuration();
	}
	
	public boolean isPng(){
		return player.isPlaying();
	}
	
	public void pausePlayer()
	{
		player.pause();
	}
	
	public void seek(int position)
	{
		player.seekTo(position);
	}
	
	public void go()
	{
		player.start();
	}
	
	/**
	 * Functions to skipping to the next or previous track
	 */
	//Previous
	public void playPrev()
	{
		songPosition--;
		if(songPosition<0) songPosition=-1;
		  playSong();
	}
	
	//Next
	public void playNext()
	{
		songPosition ++;
		if(songPosition >= songs.size()) songPosition = 0;
		playSong();
	}
}
