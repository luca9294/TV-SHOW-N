package engine;

import java.text.ParseException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
			throws InterruptedException, ExecutionException, JSONException, ParseException {
		this.context = context;
		this.code = code;
		getArrayJSON();
		
		initialize();

	}

	public SeenObject getSeason(String id) {
		SeenObject result = null;

		for (SeenObject o : vector) {
			if (o.id.equals(id)) {

				result = o;
				break;

			}

			else {

				result = null;
			}

		}
		return result;

	}

	public void initialize() throws JSONException, InterruptedException, ExecutionException, ParseException {

		vector = new Vector<SeenObject>();
        
		
		
		
		
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		
		if (!user.isEmpty()){
		
		JSONObject object = data.getJSONObject(0);
		JSONObject show = object.getJSONObject("show");
		JSONObject progress_o = object.getJSONObject("progress");
		JSONArray seasons = object.getJSONArray("seasons");

		title = show.getString("title");
		progress = progress_o.getString("percentage");
		completed = progress_o.getString("completed");
		left = progress_o.getString("left");
		total = Integer.parseInt(completed) + Integer.parseInt(left);

		for (int i = 0; i < seasons.length(); i++) {
			JSONObject o = seasons.getJSONObject(i);
			String season = o.getString("season");
			String completed = o.getString("completed");
			String left = o.getString("left");
			String percentage = o.getString("percentage");

			SeenObject so = new SeenObject(percentage, completed, left, season);

			vector.add(so);
		}}
		
		
		
		else {
			
			MyDatabase mdb = new MyDatabase (context, new Activity ());
			Tv_Show tvs = new Tv_Show(code, context, new Activity());
			title = tvs.title_n;
			completed = String.valueOf( mdb.getCount(Integer.parseInt(code)));
			total = tvs.totalNEpisodes();
			left = String.valueOf(total - Integer.valueOf(completed));
			progress = String.valueOf((int)((Double.valueOf(completed) / (double )total)*100));
			
			Log.e("completed", completed);
			Log.e("total", String.valueOf(total));
			Log.e("left", left);
			Log.e("progress", progress);
			
			String number = tvs.seasons_n;
			
			for (Season s : tvs.getSeasons()){
				int c = Integer.valueOf(s.n_episode);
				if(!s.id.equals("0"))
				{
					
				if(s.id.equals(number)){
					c = 0;
					s.getEpisodes();
					for (Episode e : s.episodes){
						if (!e.isFuture()){c++;}
						
					}
					
					
					
					
				}
				String season = s.id;
				String completed = String.valueOf(mdb.getCountPerSeason(Integer.valueOf(code), Integer.valueOf(s.id)));
				String left = String.valueOf(c - Integer.valueOf(completed));
				String percentage = String.valueOf((int)(((double)Integer.valueOf(completed) / (double)c) * 100) );

				SeenObject so = new SeenObject(percentage, completed, left, season);

				vector.add(0, so);
				
				}
				
				
				
			}
			
			
			
			
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
					+ user + "/" + code, true);

			return data;

		}

	}

	public Vector<SeenObject> getResults() {

		return vector;
	}

}
