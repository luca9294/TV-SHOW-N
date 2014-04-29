package engine;

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

public class SeenList {
	Context context;
	TraktAPI api;
	public JSONArray data;
	public Vector<Search_result> vector;

	public SeenList(Context context)
			throws InterruptedException, ExecutionException, JSONException {
		this.context = context;
		getArrayJSON();
		makeVector();

	}

	public void makeVector() throws JSONException {
		vector = new Vector<Search_result>();
		
		for (int i = 0; i < data.length(); i++) {
			JSONObject object = data.getJSONObject(i);
			String title_n = object.getString("title");
			String country = object.getString("country");
			String year = object.getString("year");
			String poster = object.getJSONObject("images").getString("poster");
			String status = object.getString("status");
			boolean ended=false;
			if (status.equals("Ended")){ ended = true;}
			JSONArray array = object.getJSONArray("genres");
			String id = object.getString("tvdb_id");

			poster = poster.replace(".jpg", "-300.jpg");
			Search_result result = new Search_result(title_n, year, country,
					poster, array, id, ended);

			vector.add(result);

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

			
			data = api.getDataArrayFromJSON("user/library/shows/watched.json/%k/" +user+"/true", false);

			return data;

		}

	}

	public Vector<Search_result> getResults() {

		return vector;
	}

}
