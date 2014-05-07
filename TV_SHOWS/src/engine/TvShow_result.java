package engine;

import java.text.ParseException;
import java.util.List;
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

public class TvShow_result {
	public String title, year, nation, image_link;
	TraktAPI api;
	Context parent;
	// public JSONArray genres;
	public String id;
	public Vector<String> episodesVector;

	public TvShow_result(String id, Context parent) {
		
		this.id = id;
		this.parent = parent;

	}

	public void getEpisodesWatching() throws InterruptedException,
			ExecutionException, JSONException, ParseException {

		api = new TraktAPI(parent);

		DataGrabber3 dg = new DataGrabber3(parent);
		JSONArray array = new JSONArray();
		dg.execute();
		array = dg.get();
		JSONObject myTvShow = new JSONObject();

		// watched = object.getJSONObject("season").getBoolean("watched");
		// wish = object.getJSONObject("episode").getBoolean("in_watchlist");

		episodesVector = new Vector<String>();

		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			String code1 = object.getString("tvdb_id");
			if (code1.equals(id)) {
				myTvShow = object;
				break;

			}
		}

		title = myTvShow.getString("title");
		year = myTvShow.getString("year");
		nation = myTvShow.getString("country");
		image_link =myTvShow.getJSONObject("images").getString("poster").replace(".jpg", "-300.jpg");
		
		JSONArray episodes = new JSONArray();
		episodes = myTvShow.getJSONArray("episodes");

		for (int i = 0; i < episodes.length(); i++) {

			JSONObject object = episodes.getJSONObject(i);
			String id1 = object.getString("number");

			String season = object.getString("season");
			String title = object.getString("title");

			String result = "#" + id + " Season:"+ season+ " " + title;
			
		
			episodesVector.add(result);

		}
	}

	class DataGrabber3 extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressdialog;
		private Context parent;

		private JSONArray data;

		public DataGrabber3(Context parent) {
			this.parent = parent;

		}

		@Override
		protected void onPreExecute() {
			// progressdialog = ProgressDialog.show(parent,"",
			// "Retrieving data ...", true);
		}

		@Override
		protected JSONArray doInBackground(String... params) {

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(parent);

			String user = prefs.getString("user", "");

			data = api.getDataArrayFromJSON("user/watchlist/episodes.json/%k/"
					+ user, false);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}

	}

}
