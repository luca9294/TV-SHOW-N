package engine;

import java.text.ParseException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class WatchingList {
	Context context;
	TraktAPI api;
	public JSONArray data;
	public Vector<Tv_Show> tvshows;

	public WatchingList(Context context) throws InterruptedException,
			ExecutionException, JSONException, ParseException {
		this.context = context;
		getArrayJSON();
		makeVector();

	}

	public void makeVector() throws JSONException, InterruptedException, ExecutionException, ParseException {
		tvshows = new Vector<Tv_Show>();

		for (int i = 0; i < data.length(); i++) {
			JSONObject object = data.getJSONObject(i);
			String id = object.getString("tvdb_id");
			Tv_Show tv = new Tv_Show(id,context);
			tvshows.add(tv);

		}
	}

	public void getArrayJSON() throws InterruptedException, ExecutionException {
		api = new TraktAPI(context);
		// String title_c = toSearch.replace(" ", "-");
		// title_c = title_c.toLowerCase();

		DataGrabber e = new DataGrabber(context);
		e.execute();
		data = e.get();
	}

	private class DataGrabber extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressdialog;
		private Context parent;
		private JSONArray data;

		public DataGrabber(Context parent) {
			this.parent = parent;
		}

		@Override
		protected void onPreExecute() {
			// progressdialog = ProgressDialog.show(parent,"",
			// "Retrieving data ...", true);
		}

		@Override
		protected JSONArray doInBackground(String... params) {
			// data = api.getDataObjectFromJSON(
			// "show/summary.json/361cd031c2473b06997c87c25ec9c057/" + id,
			// true);

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(parent);

			String user = prefs.getString("user", "");
			String pass = prefs.getString("pass", "");

			data = api.getDataArrayFromJSON(
					"user/watchlist/shows.json/%k/" + user,
					false);

			return data;

		}

	}


}
