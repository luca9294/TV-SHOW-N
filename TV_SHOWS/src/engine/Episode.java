package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class Episode {

	private TraktAPI api;
	public String id, season_n, title, first_aired_date, overview, image,
			percentage, code, rating, show, network, complete;
	public boolean love, hate, watched, wish;
	private JSONObject rate, seen, watching;
	public Vector<Comment> comments = new Vector<Comment>();
	public Context parent;
	public Activity a;

	public Episode(String id, String season_n, String title,
			String first_aired_date, String overview, String image,
			String percentage, String code, Context parent, boolean watched,
			boolean wish, Activity a) {
		this.id = id;
		this.season_n = season_n;
		this.title = title;
		this.first_aired_date = first_aired_date;
		this.overview = overview;
		this.image = image;
		this.percentage = percentage;
		this.code = code;
		this.parent = parent;
		this.watched = watched;
		this.wish = wish;
		this.a = a;
		love = false;
		hate = false;
	}

	public Episode(String id, String code, String season_n, Context parent,
			Activity a) throws InterruptedException, ExecutionException,
			JSONException {
		this.id = id;
		this.code = code;
		this.season_n = season_n;
		this.parent = parent;
		this.a = a;

		getEpisode();

		love = false;
		hate = false;
	}

	public boolean isFuture() throws ParseException {

		Date now = new Date();
		String response = "";
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String strCurrDate = sdfDate.format(now);

		Date currentDate;

		Date result = sdfDate.parse(first_aired_date);

		currentDate = sdfDate.parse(strCurrDate);

		if (currentDate.before(result)) {

			return true;

		} else {

			return false;
		}

	}

	// finds the watchseries.lt line of the stream (click here to play)
	public String getHTML(String urlToRead) {

		Log.e("URL", urlToRead);
		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			boolean found1 = false;

			// finds first occurrence of nowvideo
			while ((line = rd.readLine()) != null) {
				if (line.contains("<span>nowvideo</span>")) {
					result += "\n" + line;
					found1 = true;
					break;
				}

				else if (line.contains("http://www.nowvideo.sx/video/")) {
					result += "\n" + line;
					found1 = true;
					break;

				}

			}

			if (found1 == false) {

				while ((line = rd.readLine()) != null) {

					result += "\n" + line;
					break;

				}
			}

			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// method that returns the line where there is the name of the tvshow in
	// watchseries.lt
	public String getHTML2(String urlToRead) {

		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));

			while ((line = rd.readLine()) != null) {
				if (line.contains("/serie/")) {
					result += "\n" + line;
					break;
				}

			}

			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void getEpisode() throws InterruptedException, ExecutionException,
			JSONException {
		api = new TraktAPI(parent);
		DataGrabber dg = new DataGrabber(parent);
		dg.execute();

		JSONObject object = dg.get();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(parent);

		String user = prefs.getString("user", "");

		if (!user.isEmpty()) {
			rating = object.getJSONObject("episode").getString("rating");

			if (rating.equals("love")) {
				love = true;

			}

			else if (rating.equals("hate")) {
				hate = true;

			}

			watched = object.getJSONObject("episode").getBoolean("watched");
			wish = object.getJSONObject("episode").getBoolean("in_watchlist");

		}

		else {

			MyDatabase mdb = new MyDatabase(parent, new Activity());
			watched = mdb.containsTvShow(Integer.valueOf(code),
					Integer.valueOf(id), Integer.valueOf(season_n));
			wish = mdb.containsTvShow2(Integer.valueOf(code),
					Integer.valueOf(id), Integer.valueOf(season_n));

		}

		show = object.getJSONObject("show").getString("title");
		network = object.getJSONObject("show").getString("network");
		title = object.getJSONObject("episode").getString("title");
		complete = object.getJSONObject("episode").getString("first_aired_iso");
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

	public void addToSeen(boolean s, JSONObject o) throws JSONException,
			InterruptedException, ExecutionException {
		if (o == null) {
			seen = new JSONObject();
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(parent);

			String user = prefs.getString("user", "");
			String pass = prefs.getString("pass", "");

			seen.put("username", user);
			seen.put("password", pass);
			seen.put("tvdb_id", code);
			// jsonpost.put("title", "Revenge");
			object.put("season", season_n);
			object.put("episode", id);
			array.put(object);
			seen.put("episodes", array);

			DataGrabber4 grabber = new DataGrabber4(s);

			grabber.execute();
			grabber.get();
		}

		else {
			seen = new JSONObject();
			seen = o;

			DataGrabber4 grabber = new DataGrabber4(s);

			grabber.execute();
			grabber.get();
		}

	}

	public void addToWatching(boolean w, JSONObject o) throws JSONException,
			InterruptedException, ExecutionException, ParseException {

		if (o == null) {

			watching = new JSONObject();
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(parent);

			String user = prefs.getString("user", "");
			String pass = prefs.getString("pass", "");

			watching.put("username", user);
			watching.put("password", pass);
			watching.put("tvdb_id", code);
			// jsonpost.put("title", "Revenge");
			object.put("season", season_n);
			object.put("episode", id);
			array.put(object);
			watching.put("episodes", array);

			DataGrabber5 grabber = new DataGrabber5(w, this);

			grabber.execute();
			grabber.get();

			if (w == false) {
				Calendar c = new Calendar(parent, a);
				c.addToCalendar(this);
			} else {
				Calendar c = new Calendar(parent, a);
				c.removeFromCalendar(this);
			}
		}

		else {
			watching = new JSONObject();
			watching = o;

			DataGrabber5 grabber = new DataGrabber5(w, this);

			grabber.execute();
			grabber.get();

		}

	}

	// gets the exact name of the tv serie
	public String getName() throws IOException, InterruptedException,
			ExecutionException, JSONException, ParseException {

		Tv_Show tvs = new Tv_Show(code, parent, a);
		DataGrabber17 db = new DataGrabber17("http://watchseries.lt/search/"
				+ tvs.title_n.replace(".", ""));
		db.execute();

		String nome = db.get();
		int index = nome.indexOf("href=\"/serie/");
		nome = nome.substring(index);
		nome = nome.substring(13, nome.length() - 2);

		return nome;

	}

	// method that returns the final nowvideo link of the streaming episode
	public String f() throws InterruptedException, ExecutionException,
			JSONException, ParseException, IOException {

		this.getName();

		Tv_Show tvs = new Tv_Show(code, parent, a);
		String title = tvs.title_n.toLowerCase().replace(" ", "_");

		DataGrabber16 db = new DataGrabber16("http://watchseries.lt/episode/"
				+ getName() + "_s" + season_n + "_e" + id + ".html");

		db.execute();
		String s = db.get();
		int index = s.indexOf("/open/cale/");
		int index2 = s.indexOf(".html");
		if (index != -1) {
			s = "http://watchseries.lt" + s.substring(index, index2) + ".html";

			DataGrabber16 db1 = new DataGrabber16(s); // loads the page with
														// that link (click here
														// to play)

			db1.execute();

			s = db1.get();

			index = s.indexOf("www.nowvideo.sx");

			if (index != -1) {
				s = s.substring(index);
				index2 = s.indexOf("\"");
				s = s.substring(0, index2);// takes the whole final link
			}

			else {
				s = "";
			}

		}

		else {

			s = "";
		}

		return s;

	}

	// data grabbers

	class DataGrabber16 extends AsyncTask<String, Void, String> {
		String str;

		public DataGrabber16(String str) {
			this.str = str;
		}

		@Override
		protected void onPreExecute() {
			// progressdialog = ProgressDialog.show(parent,"",
			// "Retrieving data ...", true);
		}

		@Override
		protected String doInBackground(String... params) {

			String c = getHTML(str);

			return c;
		}
	}

	class DataGrabber17 extends AsyncTask<String, Void, String> {
		String str;

		public DataGrabber17(String str) {
			this.str = str;
		}

		@Override
		protected void onPreExecute() {
			// progressdialog = ProgressDialog.show(parent,"",
			// "Retrieving data ...", true);
		}

		@Override
		protected String doInBackground(String... params) {
			String c = getHTML2(str);

			return c;
		}

		private ArrayList pullLinks(String text) {
			ArrayList links = new ArrayList();

			String regex = "/open/cale/[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String urlStr = m.group();
				if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
					urlStr = urlStr.substring(1, urlStr.length() - 1);
				}
				links.add(urlStr);
			}
			return links;
		}

		private ArrayList pullLinks2(String text) {
			ArrayList links = new ArrayList();

			String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String urlStr = m.group();
				if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
					urlStr = urlStr.substring(1, urlStr.length() - 1);
				}
				links.add(urlStr);
			}
			return links;
		}

		public String getString() throws IOException, InterruptedException,
				ExecutionException, JSONException, ParseException {
			Tv_Show tvs = new Tv_Show(code, parent, a);
			String titlen = tvs.title_n.toLowerCase().replace(" ", "_");
			String URL = "http://watchseries.lt/episode/" + titlen + "_s"
					+ season_n + "_e" + id + ".html";
			DataGrabber16 dbg = new DataGrabber16(
					"http://watchseries.lt/episode/" + "revenge" + "_s" + "1"
							+ "_e" + "1" + ".html");
			dbg.execute();

			BufferedReader bufReader = new BufferedReader(new StringReader(
					dbg.get()));
			String result = "";
			String result2 = "";
			String line = null;
			int i = 0;
			while ((line = bufReader.readLine()) != null) {
				if (line.contains("<span>nowvideo</span>")) {
					result = result + "\n" + line;
					break;
				}
				i++;
			}

			/*
			 * ArrayList a = pullLinks(result);
			 * 
			 * String result3 = ""; for (int ie = 0; ie < a.size(); ie++) { //
			 * System.out.println(a.get(ie)); result2 = (String) a.get(ie); //
			 * System.out.println(result2); break; }
			 * 
			 * bufReader = new BufferedReader(new StringReader(
			 * getHTML("http://watchseries.lt" + result2)));
			 * 
			 * while ((line = bufReader.readLine()) != null) { if
			 * (line.contains("nowvideo")) { result3 = line; break; } } a =
			 * pullLinks2(result3); String re = (String) a.get(0); re =
			 * re.replace("sx", "at"); Log.e("e", re);
			 */
			return bufReader.readLine();

		}
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
				if (s == false) {
					data = getDataFromJSON(
							"http://api.trakt.tv/show/episode/seen/361cd031c2473b06997c87c25ec9c057",
							true, "", seen);
				} else {
					data = getDataFromJSON(
							"http://api.trakt.tv/show/episode/unseen/361cd031c2473b06997c87c25ec9c057",
							true, "", seen);
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

			Log.e("seen", data.toString());

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

	class DataGrabber5 extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog progressdialog;
		private Context parent;
		private String id;
		private JSONObject data;
		private boolean w;
		private Episode e;

		public DataGrabber5(boolean w, Episode e) {
			this.w = w;
			this.e = e;
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
				if (w == false) {
					data = getDataFromJSON(
							"http://api.trakt.tv/show/episode/watchlist/361cd031c2473b06997c87c25ec9c057",
							true, "", watching);

				} else {
					data = getDataFromJSON(
							"http://api.trakt.tv/show/episode/unwatchlist/361cd031c2473b06997c87c25ec9c057",
							true, "", watching);
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
