package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class Episode {

	private TraktAPI api;
	public String id, season_n, title, first_aired_date, overview, image,
			percentage, code, rating;

	public boolean love, hate;
	private JSONObject rate;
	public Vector<Comment> comments = new Vector<Comment>();

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
		love = false;
		hate = false;
	}

	public Episode(String id, String code, String season_n, Context parent)
			throws InterruptedException, ExecutionException, JSONException {
		this.id = id;
		this.code = code;
		this.season_n = season_n;
		this.parent = parent;

		getEpisode();

		love = false;
		hate = false;
	}

	public void getEpisode() throws InterruptedException, ExecutionException,
			JSONException {
		api = new TraktAPI(parent);
		DataGrabber dg = new DataGrabber(parent);
		dg.execute();

		JSONObject object = dg.get();
		rating = object.getJSONObject("episode").getString("rating");

		if (rating.equals("love")) {
			love = true;

		}

		else if (rating.equals("hate")) {
			hate = true;

		}

		title = object.getJSONObject("episode").getString("title");
		first_aired_date = object.getJSONObject("episode")
				.getString("first_aired_iso").replace("T", " ");
		if (first_aired_date.length() > 10) {
			first_aired_date = first_aired_date.substring(0, 10);
		}
		overview = object.getJSONObject("episode").getString("overview");
		image = object.getJSONObject("episode").getJSONObject("images")
				.getString("screen");
		percentage = object.getJSONObject("episode").getJSONObject("ratings")
				.getString("percentage");

	}

	public boolean makeAComment(String str) throws JSONException,
			InterruptedException, ExecutionException {
		JSONObject jsonpost = new JSONObject();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(parent);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");

		Log.e("user", user);
		Log.e("pass", pass);

		jsonpost.put("username", user);
		jsonpost.put("password", pass);
		jsonpost.put("tvdb_id", code);
		// jsonpost.put("title", "Revenge");
		jsonpost.put("season", season_n);
		jsonpost.put("episode", id);
		jsonpost.put("comment", str);

		Comment comment = new Comment(jsonpost);

		comment.send();

		if (comment.isOK()) {

			return true;

		}

		else {
			return false;

		}

	}
	
	public void makeARate(String str) throws JSONException,
	InterruptedException, ExecutionException {
rate = new JSONObject();
SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(parent);

String user = prefs.getString("user", "");
String pass = prefs.getString("pass", "");

Log.e("user", user);
Log.e("pass", pass);

rate.put("username", user);
rate.put("password", pass);
rate.put("tvdb_id", code);
// jsonpost.put("title", "Revenge");
rate.put("season", season_n);
rate.put("episode", id);
rate.put("rating", str);

DataGrabber3 grabber = new DataGrabber3(parent, pass);

grabber.execute();
grabber.get();



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

	public void getComments() throws InterruptedException, ExecutionException,
			JSONException {
		api = new TraktAPI(parent);
		DataGrabber2 dg = new DataGrabber2(parent);
		dg.execute();

		JSONArray array = dg.get();

		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);

			String user = object.getJSONObject("user").getString("username");
			String text = object.getString("text_html");

			String date = object.getString("inserted");

			Comment comment = new Comment(user, text, date, parent);

			comments.add(comment);

		}
	}

	private class DataGrabber2 extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressdialog;
		private Context parent;

		private JSONArray data;

		public DataGrabber2(Context parent) {
			this.parent = parent;

		}

		@Override
		protected void onPreExecute() {
			// progressdialog = ProgressDialog.show(parent,"",
			// "Retrieving data ...", true);
		}

		@Override
		protected JSONArray doInBackground(String... params) {
			data = api.getDataArrayFromJSON("show/episode/comments.json/%k/"
					+ code + "/" + season_n + "/" + id, false);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}

	}
	
	
	
	
	
	
	class DataGrabber3 extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressdialog;
		private Context parent;
		private String id;
		private JSONObject data;

		public DataGrabber3(Context parent, String id) {
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
						"http://api.trakt.tv/rate/episode/361cd031c2473b06997c87c25ec9c057",
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
