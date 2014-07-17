package CustomAdapters;

import java.util.ArrayList;

import com.example.android_social_player.R;

import BusinessLogicLayer.Song;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
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
		RelativeLayout songLayout = (RelativeLayout)songInflater.inflate(R.layout.song, parent,false);
		//Get titles and artists views
		TextView songView = (TextView)songLayout.findViewById(R.id.song_title);
		TextView artistView = (TextView)songLayout.findViewById(R.id.song_artist);
		TextView durationSong = (TextView)songLayout.findViewById(R.id.song_duration);
		Song currentSong = songs.get(position);
		
		songView.setText(currentSong.getTitle());
		artistView.setText(currentSong.getArtist());
		durationSong.setText(currentSong.getDuration());
		songLayout.setTag(position);
		
		return songLayout;
	}

}
