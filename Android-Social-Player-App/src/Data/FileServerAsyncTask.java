package Data;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

public class FileServerAsyncTask extends AsyncTask{
	
	private Context contexte;
	private TextView statusText;
	
	public FileServerAsyncTask(Context contexte,View statusView)
	{
		this.contexte = contexte;
		this.statusText =(TextView) statusView;
	}
	
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
