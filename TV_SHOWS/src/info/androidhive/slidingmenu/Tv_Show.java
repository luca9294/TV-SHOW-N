package info.androidhive.slidingmenu;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class Tv_Show {
	String title, first_aired_iso, country, overview;
	String year, runtime, image, genre, percentage, loves, hate, status,
			title_n, seasons_n;

	JSONObject summary;
	JSONArray season;
	TraktAPI api;
	Context context;

	public Tv_Show(String title, Context context) throws InterruptedException,
			ExecutionException, JSONException {
		this.title = title;
		this.context = context;
		getTvShowJSON();

		title_n = summary.getString("title");
		year = summary.getString("year");
		first_aired_iso = summary.getString("first_aired_iso").substring(0, 10);
		country = summary.getString("country");
		overview = summary.getString("overview");
		runtime = summary.getString("runtime");
		status = summary.getString("status");
		image = summary.getJSONObject("images").getString("poster");
		percentage = summary.getJSONObject("ratings").getString("percentage");
		genre = summary.getJSONArray("genres").getString(0);
		seasons_n = season.getJSONObject(0).getString("season");
		

	}

	public String getTitle() {

		return title;
	}

	public void getSeasonsN() throws InterruptedException, ExecutionException {
		api = new TraktAPI(context);
		String title_c = title.replace(" ", "-");
		title_c = title_c.toLowerCase();

		DataGrabber e = new DataGrabber(context, title_c);
		e.execute();

	}

	public void getTvShowJSON() throws InterruptedException, ExecutionException {
		api = new TraktAPI(context);
		String title_c = title.replace(" ", "-");
		title_c = title_c.toLowerCase();

		DataGrabber e = new DataGrabber(context, title_c);
		e.execute();
		summary = e.get();
		
		
		DataGrabber2 d = new DataGrabber2(context, title_c);
		d.execute();
		season = d.get();
	}

	private class DataGrabber extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressdialog;
		private Context parent;
		private String id;
		private JSONObject data;

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
		protected JSONObject doInBackground(String... params) {
			data = api.getDataObjectFromJSON(
					"show/summary.json/361cd031c2473b06997c87c25ec9c057/" + id,
					true);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}

	}
	
	
	
	private class DataGrabber2 extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressdialog;
		private Context parent;
		private String id;
		private JSONArray data;

		public DataGrabber2(Context parent, String id) {
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
			data = api.getDataArrayFromJSON(
					"show/seasons.json/361cd031c2473b06997c87c25ec9c057/" + id,
					true);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}

	}
	
	
	

}
