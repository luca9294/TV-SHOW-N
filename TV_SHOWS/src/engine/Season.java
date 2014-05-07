package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import engine.Episode.DataGrabber4;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class Season {

	private TraktAPI api;
	public String id, n_episode, image, code;
	public Context parent;
	public Vector<Episode> episodes;
	public Vector<Episode> inWacthList;
	public boolean watched, wish;
	public boolean watchedn;
	private JSONObject seen;

	public Season(String id, String n_episode, String image, String code,
			Context parent) throws InterruptedException, ExecutionException,
			JSONException {
		this.id = id;
		this.n_episode = n_episode;
		this.image = image;
		this.code = code;
		this.parent = parent;

		// getEpisodes();
	}

	public String toString() {

		return "Season " + id + " - " + n_episode + " Episodes";

	}

	public void getEpisodes() throws InterruptedException, ExecutionException,
			JSONException, ParseException {

		api = new TraktAPI(parent);

		DataGrabber dg = new DataGrabber(parent);
		JSONArray array = new JSONArray();
		dg.execute();
		array = dg.get();

		// watched = object.getJSONObject("season").getBoolean("watched");
		// wish = object.getJSONObject("episode").getBoolean("in_watchlist");

		episodes = new Vector<Episode>();

		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);

			String id_e = object.getString("episode");
			// String s_n = season_n;
			String title = object.getString("title");
			String first_aired = object.getString("first_aired_iso");
			if (first_aired.length() > 10) {
				first_aired = first_aired.substring(0, 10);
			}
			String overview = object.getString("overview");
			String image = object.getJSONObject("images").getString("screen");
			String percentage = object.getJSONObject("ratings").getString(
					"percentage");

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(parent);

			String user = prefs.getString("user", "");
			String pass = prefs.getString("pass", "");

			if (!user.isEmpty()) {

				watchedn = object.getBoolean("watched");

				wish = object.getBoolean("in_watchlist");
			}

			String first = first_aired;
			// SimpleDateFormat df = new
			// SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
			Date result;

			Date now = new Date();
			String response = "";
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd",
					Locale.US);
			String strCurrDate = sdfDate.format(now);

			Date currentDate;
			result = sdfDate.parse(first);
			currentDate = sdfDate.parse(strCurrDate);

			if (currentDate.before(result)) {
				if (user.isEmpty()) {
					Episode e = new Episode(id_e, id, title, first_aired,
							overview, image, percentage, code, parent, false,
							false);
					episodes.add(e);

				} else {

					Episode e = new Episode(id_e, id, title, first_aired,
							overview, image, percentage, code, parent, true,
							true);
					episodes.add(e);
				}

			} else {
				if (user.isEmpty()) {
					Episode e = new Episode(id_e, id, title, first_aired,
							overview, image, percentage, code, parent, false,
							false);
					episodes.add(e);

				} else {

					Episode e = new Episode(id_e, id, title, first_aired,
							overview, image, percentage, code, parent,
							watchedn, wish);
					episodes.add(e);
				}
			}
		}

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

		inWacthList = new Vector<Episode>();

		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			String code1 = object.getString("tvdb_id");
			if (code1.equals(code)) {
				myTvShow = object;
				break;

			}
		}

		JSONArray episodes = new JSONArray();
		episodes = myTvShow.getJSONArray("episodes");

		for (int i = 0; i < episodes.length(); i++) {

			JSONObject object = episodes.getJSONObject(i);
			String id1 = object.getString("number");

			String season = object.getString("season");
		
				Episode e = new Episode(id1, code, season, parent);
				inWacthList.add(e);

			

		}
	}

	public boolean checkSeen() throws InterruptedException, ExecutionException,
			JSONException {

		boolean result = true;
		String r = "";
		for (int i = 0; i < episodes.size(); i++) {
			Episode e = episodes.get(i);
			Log.e("EPISODE", e.title);
			if (e.watched) {

			}
			if (!e.watched) {
				result = false;

				break;
			}

		}

		return result;
	}

	public boolean checkWatch() throws InterruptedException,
			ExecutionException, JSONException {

		boolean result = true;
		String r = "";
		for (int i = 0; i < episodes.size(); i++) {
			Episode e = episodes.get(i);
			if (e.wish) {

			}
			if (!e.wish) {
				result = false;
				Log.e("", "CS");

				break;
			}

		}

		return result;
	}

	public void addToSeen(boolean s) throws JSONException,
			InterruptedException, ExecutionException {
		seen = new JSONObject();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(parent);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");

		seen.put("username", user);
		seen.put("password", pass);
		seen.put("tvdb_id", code);
		seen.put("season", id);

		DataGrabber2 grabber = new DataGrabber2(s);

		grabber.execute();
		grabber.get();

	}

	public void removeFromSeen() throws JSONException, InterruptedException,
			ExecutionException {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(parent);
		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");
		seen = new JSONObject();
		seen.put("username", user);
		seen.put("password", pass);
		seen.put("tvdb_id", code);

		JSONArray array = new JSONArray();

		for (int i = 0; i < episodes.size(); i++) {
			JSONObject object = new JSONObject();
			object.put("season", episodes.get(i).season_n);
			object.put("episode", episodes.get(i).id);
			array.put(object);

		}
		seen.put("episodes", array);

		episodes.get(0).addToSeen(true, seen);

	}

	public void addToWatch(boolean bol) throws JSONException,
			InterruptedException, ExecutionException {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(parent);
		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");
		seen = new JSONObject();
		seen.put("username", user);
		seen.put("password", pass);
		seen.put("tvdb_id", code);

		JSONArray array = new JSONArray();

		for (int i = 0; i < episodes.size(); i++) {
			JSONObject object = new JSONObject();
			object.put("season", episodes.get(i).season_n);
			object.put("episode", episodes.get(i).id);
			array.put(object);

		}
		seen.put("episodes", array);

		episodes.get(0).addToWatching(bol, seen);

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
			data = api.getDataArrayFromJSON("show/season.json/%k/" + code + "/"
					+ id, false);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}

	}

	// data grabber of seen
	class DataGrabber2 extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressdialog;
		private Context parent;
		private String id;
		private JSONObject data;
		private boolean s;

		public DataGrabber2(boolean s) {
			this.s = s;
		}

		@Override
		protected void onPreExecute() {
			// progressdialog = ProgressDialog.show(parent,"",
			// "Retrieving data ...", true);
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			// api.setCred("luca9294", "1Aa30011992");
			try {
				data = getDataFromJSON(
						"http://api.trakt.tv/show/season/seen/361cd031c2473b06997c87c25ec9c057",
						true, "", seen);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.e("seen season", data.toString());

			return data;

		}

		public JSONObject getDataFromJSON(String url, boolean login,
				String type, JSONObject postdata) throws JSONException,
				ClientProtocolException, IOException {

			// Construct HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// If login add login information to a JSONObject
			HttpPost httppost = new HttpPost(url);
			JSONObject jsonpost;
			if (postdata == null) {
				jsonpost = new JSONObject();
			} else {
				jsonpost = postdata;
			}

			httppost.setEntity(new StringEntity(jsonpost.toString()));
			// Perform POST
			HttpResponse response = httpclient.execute(httppost);
			// Return the data in the requested format
			InputStream inputStream = response.getEntity().getContent();

			String result = convertInputStreamToString(inputStream);

			return new JSONObject(result);

		}

		private String convertInputStreamToString(InputStream inputStream)
				throws IOException {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = "";
			String result = "";
			while ((line = bufferedReader.readLine()) != null)
				result += line;

			inputStream.close();
			return result;

		}

	}

	private class DataGrabber3 extends AsyncTask<String, Void, JSONArray> {
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
