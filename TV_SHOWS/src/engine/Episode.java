package engine;

import java.util.concurrent.ExecutionException;

import org.json.*;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class Episode {

	private TraktAPI api;
	public String id, season_n, title, first_aired_date, overview, image,
			percentage, code;
	public Context parent;

	public Episode(String id, String season_n, String title,
			String first_aired_date, String overview, String image,
			String percentage, String code, Context parent) {
		this.id = id;
		this.season_n = season_n;
		this.title = title;
		this.first_aired_date = first_aired_date;
		this.overview = overview;
		this.image = image;
		this.percentage = percentage;
		this.code = code;
		this.parent = parent;
	}

	public Episode(String id, String code, String season_n, Context parent)
			throws InterruptedException, ExecutionException, JSONException {
		this.id = id;
		this.code = code;
		this.season_n = season_n;
		this.parent = parent;

		getEpisode();
	}

	public void getEpisode() throws InterruptedException, ExecutionException,
			JSONException {
		api = new TraktAPI(parent);
		DataGrabber dg = new DataGrabber(parent);
		dg.execute();

		JSONObject object = dg.get();

		title = object.getJSONObject("episode").getString("title");
		first_aired_date = object.getJSONObject("episode")
				.getString("first_aired_iso").replace("T", " ");
		overview = object.getJSONObject("episode").getString("overview");
		image = object.getJSONObject("episode").getJSONObject("images")
				.getString("screen");
		percentage = object.getJSONObject("episode").getJSONObject("ratings")
				.getString("percentage");

	}

	private class DataGrabber extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressdialog;
		private Context parent;

		private JSONObject data;

		public DataGrabber(Context parent) {
			this.parent = parent;

		}

		@Override
		protected void onPreExecute() {
			// progressdialog = ProgressDialog.show(parent,"",
			// "Retrieving data ...", true);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			data = api.getDataObjectFromJSON("show/episode/summary.json/%k/"
					+ code + "/" + season_n + "/" + id, false);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}

	}

}