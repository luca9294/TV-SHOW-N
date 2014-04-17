package engine;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class Search {
	String toSearch;
	Context context;
	TraktAPI api;
	public JSONArray data;
	public Vector<Search_result> vector;

	public Search(String toSearch, Context context)
			throws InterruptedException, ExecutionException, JSONException {
		this.toSearch = toSearch;
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
			boolean ended = object.getBoolean("ended");
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

		DataGrabber e = new DataGrabber(context, toSearch);
		e.execute();
		data = e.get();
	}

	private class DataGrabber extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressdialog;
		private Context parent;
		private String id;
		private JSONArray data;

		public DataGrabber(Context parent, String id) {
			this.parent = parent;
			this.id = id;
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

			data = api.getDataArrayFromJSON("search/shows.json/%k?query="
					+ toSearch, false);

			return data;

		}

	}

	public Vector<Search_result> getResults() {

		return vector;
	}

}
