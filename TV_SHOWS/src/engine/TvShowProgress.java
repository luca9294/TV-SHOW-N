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
import android.util.Log;

public class TvShowProgress {

	Context context;
	TraktAPI api;
	public JSONArray data;
	public Vector<SeenObject> vector;
	public String code, title, progress, completed, left;
	public int total;

	public TvShowProgress(Context context, String code)
			throws InterruptedException, ExecutionException, JSONException {
		this.context = context;
		this.code = code;
		getArrayJSON();
		initialize();

	}

	public void initialize() throws JSONException {

		vector = new Vector<SeenObject>();
		
		
		JSONObject object = data.getJSONObject(0);
		JSONObject show = object.getJSONObject("show");
		JSONObject progress_o = object.getJSONObject("progress");
		JSONArray seasons = object.getJSONArray("seasons");
		
		title = show.getString("title");
		progress = progress_o.getString("percentage");
		completed = progress_o.getString("completed");
		left = progress_o.getString("left");
		total = Integer.parseInt(completed)+Integer.parseInt(left);
		
		for (int i = 0; i<seasons.length(); i++){
			JSONObject o = seasons.getJSONObject(i);
			String season = o.getString("season");
			String completed = o.getString("completed");
			String left = o.getString("left");
			String percentage = o.getString("percentage");
			
		
			
			SeenObject so = new SeenObject(percentage, completed, left, season);
			
			vector.add(so);
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

			data = api.getDataArrayFromJSON("user/progress/watched.json/%k/"
					+ user + "/"+ code, true);
			

			return data;

		}

	}

	public Vector<SeenObject> getResults() {

		return vector;
	}

}
