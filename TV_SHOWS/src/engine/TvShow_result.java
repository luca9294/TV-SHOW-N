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
import android.util.Log;

public class TvShow_result {
	public String title, year, nation, image_link;
	TraktAPI api;
	Context parent;
	 public JSONArray genres;
	public String id;
	public Vector<String> episodesVector;

	public TvShow_result(String id, Context parent) {
		
		this.id = id;
		this.parent = parent;

	}

	public void getEpisodesWatching() throws InterruptedException,
			ExecutionException, JSONException, ParseException {
        boolean found = false;
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
				found = true;
				break;

			}
		}
		
		
		
        if (found){
		title = myTvShow.getString("title");
		year = myTvShow.getString("year");
		nation = myTvShow.getString("country");
		genres = myTvShow.getJSONArray("genres"); 
		image_link = myTvShow.getJSONObject("images").getString("poster").replace(".jpg", "-300.jpg");
		
		JSONArray episodes = new JSONArray();
		episodes = myTvShow.getJSONArray("episodes");

		for (int i = 0; i < episodes.length(); i++) {

			JSONObject object = episodes.getJSONObject(i);
			String id1 = object.getString("number");

			String season = object.getString("season");
			String title = object.getString("title");
			
			

			String result = "<b>#" + id1 + " " + title + "</b><br>Season "+ season ;
			
		
			episodesVector.add(result);

		}}
        
        
        
        
        
        else{
        	DataGrabber4 dg1 = new DataGrabber4(parent);
    		
    		dg1.execute();
    		JSONArray array1 = dg1.get();
    		Log.e("cdd", array1.toString());
    		JSONObject myTvShow1 = new JSONObject();
        	
    		for (int i = 0; i < array1.length(); i++) {
    			JSONObject object = array1.getJSONObject(i);
    			String code1 = object.getString("tvdb_id");
    			if (code1.equals(id)) {
    				myTvShow1 = object;
    				break;

    			}
    		}
    		
    		
        	
    		title = myTvShow1.getString("title");
    		year = myTvShow1.getString("year");
    		nation = myTvShow1.getString("country");
    		genres = myTvShow1.getJSONArray("genres"); 
    		image_link = myTvShow1.getJSONObject("images").getString("poster").replace(".jpg", "-300.jpg");
        	
        	
        	
        	
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
	
	
	class DataGrabber4 extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressdialog;
		private Context parent;

		private JSONArray data;

		public DataGrabber4(Context parent) {
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

			data = api.getDataArrayFromJSON("user/watchlist/shows.json/%k/"
					+ user, true);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}
		

		

	}

}
