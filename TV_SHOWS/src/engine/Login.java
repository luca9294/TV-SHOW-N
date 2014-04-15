package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Login {
	private String user, passwd;
	private Context context;
	TraktAPI api;
	JSONObject object;

	public Login(String user, String passwd, Context context)
			throws InterruptedException, ExecutionException, JSONException {
		this.user = user;
		this.passwd = passwd;
		this.context = context;
		getSeasonsN();
		if (isSuccess()) {
			// Context applicationContext =
			// MainActivity.getContextOfApplication();
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = prefs.edit();
			// put your value
			editor.putString("user", user);
			editor.putString("pass", passwd);
			editor.putBoolean("logged", true);
			api.setCred(user, passwd);
			editor.commit();

		}

	}

	public void getSeasonsN() throws InterruptedException, ExecutionException {
		api = new TraktAPI(context);

		DataGrabber e = new DataGrabber(context, "");
		e.execute();
		object = e.get();

	}

	public boolean isSuccess() throws JSONException {

		if (object.getString("status").equals("failure")) {
			return false;
		}

		else {
			return true;
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
						"http://api.trakt.tv/account/test/361cd031c2473b06997c87c25ec9c057",
						true, "", null);
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

			jsonpost.accumulate("username", user);
			jsonpost.accumulate("password", passwd);

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