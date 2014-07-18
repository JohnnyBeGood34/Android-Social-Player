package DataAccessLayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BddPlayList extends SQLiteOpenHelper{
	
	private static final String CREATE_BASE_PLAYLIST = "";
	public BddPlayList(Context contexte, String name, CursorFactory factory, int version)
	{
		super(contexte,name,factory,version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BASE_PLAYLIST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*Due*/
	}
}
