package BusinessLogicLayer;

import java.util.ArrayList;

public class PlayList {
	private long id;
	private String name;
	private String imagePath;
	private ArrayList<Song> songs;
	
	public PlayList(long idPlaylist,String name,String imagePath)
	{
		this.id = idPlaylist;
		this.name = name;
		this.imagePath = imagePath;
		songs = new ArrayList<Song>();
	}
	
	public long getIdPlaylist()
	{
		return this.id;
	}
	
	public String getNamePlaylist()
	{
		return this.name;
	}
	
	public String getImagePathPlaylist()
	{
		return this.imagePath;
	}
	
	public ArrayList<Song> getSongsPlaylist()
	{
		return this.songs;
	}
	
	public void setSong(Song theSong)
	{
		this.songs.add(theSong);
	}
}
