package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import engine.Login.DataGrabber;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class Comment {
	public String id, text;
	public String date;
	String code, season, episode;
	Context context;
	TraktAPI api;
	JSONObject object, comment;

	public Comment(String id, String text, String date, Context context) {
		this.id = id;
		this.text = text;
		this.date = date;
		this.context = context;

		Date dateN = new Date(Long.parseLong(date) * 1000);

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		this.date = dateN.toGMTString();

	}

	public Comment(JSONObject object) {
		this.comment = object;

	}

	public void send() throws InterruptedException, ExecutionException {
		DataGrabber e = new DataGrabber(context, "");
		e.execute();
		object = e.get();
		Log.e("PROVA:C", object.toString());
	}

	public boolean isOK() throws JSONException {
		if (object.getString("status").equals("success")) {
			return true;

		} else {
			return false;
		}

	}

	class DataGrabber extends AsyncTask<String, Void, JSONObject> {
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

			// api.setCred("luca9294", "1Aa30011992");
			try {
				data = getDataFromJSON(
						"http://api.trakt.tv/comment/episode/361cd031c2473b06997c87c25ec9c057",
						true, "", comment);
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