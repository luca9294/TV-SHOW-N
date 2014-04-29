package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import engine.Episode.DataGrabber3;
import engine.Episode.DataGrabber4;
import engine.Episode.DataGrabber5;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class Tv_Show {
	public String title, first_aired_iso, country, overview;
	public String year, runtime, image, genre, percentage, status;
	public String title_n;
	public String seasons_n;
	public boolean in_watching, loves, hates;
	private JSONObject seen, watch, rate;

	JSONObject summary;
	JSONArray season;
	TraktAPI api;
	Context context;

	Vector<Season> seasons;

	public Tv_Show(String title, Context context) throws InterruptedException,
			ExecutionException, JSONException {
		this.title = title;
		this.context = context;
		getTvShowJSON();
		hates = false;
		loves = false;
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
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");
		if (!user.isEmpty()) {
			in_watching = summary.getBoolean("in_watchlist");
			String rate = summary.getString("rating");
			if (rate.equals("love")) {
				loves = true;

			}

			else if (rate.equals("hate")) {
				hates = true;

			}

		}

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

	public void getTvShowJSON() throws InterruptedException,
			ExecutionException, JSONException {
		api = new TraktAPI(context);
		String title_c = title.replace(" ", "-");
		title_c = title_c.toLowerCase();

		DataGrabber e = new DataGrabber(context, title_c);
		e.execute();
		summary = e.get();

		DataGrabber2 d = new DataGrabber2(context, title_c);
		d.execute();
		season = d.get();
		seasons = new Vector<Season>();

		for (int i = 0; i < season.length(); i++) {
			JSONObject object = season.getJSONObject(i);
			String id = object.getString("season");
			String episodes = object.getString("episodes");
			String image = object.getJSONObject("images").getString("poster");
			Season s = new Season(id, episodes, image, title, context);

			seasons.add(s);

		}

	}

	public Vector<Season> getSeasons() {

		return seasons;

	}

	//adds tv show to the watchlist
	public void addToWatch(boolean s) throws JSONException,
			InterruptedException, ExecutionException {

		watch = new JSONObject();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");

		watch.put("username", user);
		watch.put("password", pass);

		JSONObject object = new JSONObject();
		object.put("tvdb_id", title);
		object.put("title", title_n);
		object.put("year", year);

		JSONArray array = new JSONArray();
		array.put(object);

		watch.put("shows", array);

		DataGrabber4 grabber = new DataGrabber4(s);

		grabber.execute();
		grabber.get();

	}

	// adds rate (love, hate , unrate) to the tv show
	public void makeARate(String str) throws JSONException,
			InterruptedException, ExecutionException {
		rate = new JSONObject();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");

		Log.e("user", user);
		Log.e("pass", pass);

		rate.put("username", user);
		rate.put("password", pass);
		rate.put("tvdb_id", title);
		// jsonpost.put("title", "Revenge");
		rate.put("rating", str);

		DataGrabber5 grabber = new DataGrabber5(context, pass);

		grabber.execute();
		grabber.get();

	}
	
	
	//adds tv show to the seenlist
	public void addToSeen(boolean s, JSONObject o) throws JSONException,
			InterruptedException, ExecutionException {
		if (o == null) {
			seen = new JSONObject();
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);

			String user = prefs.getString("user", "");
			String pass = prefs.getString("pass", "");

			seen.put("username", user);
			seen.put("password", pass);
			// seen.put("imdb_id", code);
			seen.put("tvdb_id", title);
			seen.put("title", title_n);
			seen.put("year", year);

			DataGrabber3 grabber = new DataGrabber3(s);

			grabber.execute();
			grabber.get();
		}

		else {
			seen = new JSONObject();
			seen = o;

			DataGrabber3 grabber = new DataGrabber3(s);

			grabber.execute();
			grabber.get();
		}

	}

	//removes tv show from the seenlist
	public void removeFromSeen() throws JSONException {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");
		seen = new JSONObject();
		seen.put("username", user);
		seen.put("title", title_n);
		seen.put("year", year);

		JSONArray array = new JSONArray();

		/*
		 * for (int i = 0; i < prova.size(); i++) { JSONObject object = new
		 * JSONObject(); object.put("season", episodes.get(i).season_n);
		 * object.put("episode", episodes.get(i).id); array.put(object);
		 * 
		 * } seen.put("episodes", array);
		 * 
		 * episodes.get(0).addToSeen(true, seen);
		 */
	}

	
	//data grabber of the season (summary)
	private class DataGrabber extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressdialog;
		private Context context;
		private String id;
		private JSONObject data;

		public DataGrabber(Context context, String id) {
			this.context = context;
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
					false);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}

	}

	
	//data grabber of the season number
	private class DataGrabber2 extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressdialog;
		private Context context;
		private String id;
		private JSONArray data;

		public DataGrabber2(Context context, String id) {
			this.context = context;
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
					false);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}

	}

	// data grabber for seen
	class DataGrabber3 extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressdialog;
		private Context context;
		private String id;
		private JSONObject data;
		private boolean s;

		public DataGrabber3(boolean s) {
			this.s = s;
		}

		@Override
		protected void onPreExecute() {
			// progressdialog = ProgressDialog.show(context,"",
			// "Retrieving data ...", true);
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			// api.setCred("luca9294", "1Aa30011992");
			try {
				data = getDataFromJSON(
						"http://api.trakt.tv/show/seen/361cd031c2473b06997c87c25ec9c057",
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

			Log.e("seen show", data.toString());

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

		// method to parse the date
		public Date dateParser(String dateString) {
			String dateExample = "2009-10-29T22:30:00-05:00"; // just a reminder
																// for
																// the format,
																// will
																// be deleted
																// once
																// work has been
																// completed
			Date airDate = new Date();
			// String dateExample2 = "2001-07-04T12:08:56.235-0700";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			try {
				airDate = df.parse(dateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return airDate;
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

	
	//data grabber for watchlist
	class DataGrabber4 extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressdialog;
		private Context parent;
		private String id;
		private JSONObject data;
		private boolean s;

		public DataGrabber4(boolean s) {
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
				if (s == true) {
					data = getDataFromJSON(
							"http://api.trakt.tv/show/watchlist/361cd031c2473b06997c87c25ec9c057",
							true, "", watch);
				} else {
					data = getDataFromJSON(
							"http://api.trakt.tv/show/unwatchlist/361cd031c2473b06997c87c25ec9c057",
							true, "", watch);
				}
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

			Log.e("watch", data.toString());

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
	
	
	//data grabber for rate option
	class DataGrabber5 extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressdialog;
		private Context parent;
		private String id;
		private JSONObject data;

		public DataGrabber5(Context parent, String id) {
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

			// api.setCred("luca9294", "1Aa30011992");
			try {
				data = getDataFromJSON(
						"http://api.trakt.tv/rate/show/361cd031c2473b06997c87c25ec9c057",
						true, "", rate);
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

			Log.e("e", data.toString());

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

}
