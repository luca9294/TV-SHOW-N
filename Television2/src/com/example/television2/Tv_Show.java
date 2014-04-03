package com.example.television2;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class Tv_Show {
	String title, first_aired_iso, country, overview;
	String year, runtime, image, genre, percentage, loves, hate, status,
			title_n, seasons_n;

	JSONObject object;
	TraktAPI api;
	Context context;

	public Tv_Show(String title, Context context) throws InterruptedException,
			ExecutionException, JSONException {
		this.title = title;
		this.context = context;
		getTvShowJSON();

		title_n = object.getString("title");
		year = object.getString("year");
		first_aired_iso = object.getString("first_aired_iso").substring(0, 10);
		country = object.getString("country");
		overview = object.getString("overview");
		runtime = object.getString("runtime");
		status = object.getString("status");
		image = object.getJSONObject("images").getString("poster");
		percentage = object.getJSONObject("ratings").getString("percentage");
		genre = object.getJSONArray("genres").getString(0);

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
		object = e.get();
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

}
