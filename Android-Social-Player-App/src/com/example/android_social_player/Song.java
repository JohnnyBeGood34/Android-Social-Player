package com.example.android_social_player;
/**
 * Class Song to handle User's songs
 * @author coxyJo
 *
 */
public class Song {
	private long id;
	private String title;
	private String artist;
	private String duration;
	
	public Song(long id,String title,String artist,String duration)
	{
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.duration = duration;
	}
	
	public long getId()
	{
		return this.id;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getArtist()
	{
		return this.artist;
	}
	
	public String getDuration()
	{
		return this.duration;
	}
	
}
