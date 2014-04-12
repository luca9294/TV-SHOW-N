package engine;

/*******************************************************************************
 * Copyright 2011 EscAbe
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Class for interacting with the trakt.tv API
 * 
 * @author escabe
 * 
 */
public class TraktAPI {
	private static final String TAG = "TraktAPI";



	private Context context;

	// "Constants"
	private String apikey = "361cd031c2473b06997c87c25ec9c057";
	private String baseurl = "http://api.trakt.tv/";

	 String username;
	 String password;
	private SharedPreferences prefs;

	/**
	 * Constructor.
	 * 
	 * @param c
	 *            Context which is needed to be able to retrieve the
	 *            Preferences.
	 */
	public TraktAPI(Context c) {
		// Get preferences object and retrieve username and password
		context = c;
		prefs = PreferenceManager.getDefaultSharedPreferences(c);		
	//	username = "luca9295";
	//	password = "Aa30011992";
	}

	
	
	public void setCred(String user, String passwd){
		username = user;
		password = passwd;
		
	}
	
	
	
	
	
	
	public String ResizePoster(String image, int size) {

		String p = image.replaceAll("/(\\d*)(\\.?\\d*?)?\\.jpg",
				"/$1-138$2.jpg");
		return p;
		/*
		 * switch (size) { case 1: return "http://escabe.org/resize.php?image="
		 * + p + "&h=" +
		 * context.getResources().getDimensionPixelSize(R.dimen.PosterSmallHeight
		 * ) + "&w=" +
		 * context.getResources().getDimensionPixelSize(R.dimen.PosterSmallWidth
		 * ); case 2: return "http://escabe.org/resize.php?image=" + p + "&h=" +
		 * context
		 * .getResources().getDimensionPixelSize(R.dimen.PosterMediumHeight) +
		 * "&w=" +
		 * context.getResources().getDimensionPixelSize(R.dimen.PosterMediumWidth
		 * ); default: return p; }
		 */
	}

	public String ResizeAvatar(String image, int size) {
		return image;
		/*
		 * switch (size) { case 1: return "http://escabe.org/resize.php?image="
		 * + image + "&h=" +
		 * context.getResources().getDimensionPixelSize(R.dimen
		 * .AvatarSmallHeight) + "&w=" +
		 * context.getResources().getDimensionPixelSize
		 * (R.dimen.AvatarSmallWidth); case 2: return
		 * "http://escabe.org/resize.php?image=" + image + "&h=" +
		 * context.getResources
		 * ().getDimensionPixelSize(R.dimen.AvatarMediumHeight) + "&w=" +
		 * context
		 * .getResources().getDimensionPixelSize(R.dimen.AvatarMediumWidth);
		 * default: return image; }
		 */
	}

	public String ResizeScreen(String image, int size) {
		String s = image.replaceAll("/(\\d*)(\\.?\\d*?)?\\.jpg",
				"/$1-218$2.jpg");
		return s;
		/*
		 * switch (size) { case 1: return "http://escabe.org/resize.php?image="
		 * + s + "&h=" +
		 * context.getResources().getDimensionPixelSize(R.dimen.ScreenSmallHeight
		 * ) + "&w=" +
		 * context.getResources().getDimensionPixelSize(R.dimen.ScreenSmallWidth
		 * ); case 2:
		 * 
		 * default: return s; }
		 */
	}

	/*public void Mark(Activity parent, Object... params) {
		Marker m = new Marker(parent);
		m.execute(params);
	}*/

	public boolean LoggedIn() {
		JSONObject result = getDataObjectFromJSON("account/test/%k", true);
		if (result == null)
			return false;
		if (result.optString("status").equals("success"))
			return true;
		Toast.makeText(context, "Trakt returned " + result.optString("error"),
				Toast.LENGTH_SHORT).show();
		return false;
	}

	

	/**
	 * Encodes p as SHA1 Hash.
	 * 
	 * @param p
	 *            Password.
	 * @return SHA1 encoded password.
	 */
	private String EncodePassword(String p) {
		MessageDigest sha;
		try {
			sha = MessageDigest.getInstance("SHA-1");
			sha.update(p.getBytes("iso-8859-1"));
			byte[] hash = sha.digest();
			p = "";
			for (int i = 0; i < hash.length; i++) {
				// FIX for http://code.google.com/p/trakt-app/issues/detail?id=1
				p = String.format("%s%02x", p, hash[i] & 0xFF);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
		}
		return p;
	}

	Object getDataFromJSON(String url, boolean login, String type) {
		return getDataFromJSON(url, login, type, null);
	}

	/**
	 * Actually retrieve data from the server and encode as either JSONObject or
	 * JSONArray
	 * 
	 * @param url
	 *            API URL (baseurl part needs to be omitted).
	 * @param login
	 *            send user login details to API.
	 * @param type
	 *            "array" or "object" specifying return type.
	 * @return JSONObject or JSONArray which was returned by the server.
	 */
	public Object getDataFromJSON(String url, boolean login, String type,
			JSONObject postdata) {
		// Build URL. URLS may contain certain tags which will be replaced
		url = baseurl + url;
		url = url.replaceAll("%k", apikey);
		url = url.replaceAll("%u", username);

		// Construct HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
	
		
	
		
		String user = prefs.getString("user", "");
		String pass = prefs.getString("passed", "");
	
		
		
		
		
		if (user.isEmpty()) {
			// If login add login information to a JSONObject
			HttpPost httppost = new HttpPost(url);
			JSONObject jsonpost;
			if (postdata == null) {
				jsonpost = new JSONObject();
			} else {
				jsonpost = postdata;
			}
			try {
				jsonpost.put("username", user);
				jsonpost.put("password", pass);
				httppost.setEntity(new StringEntity(jsonpost.toString()));
				// Perform POST
				String response = httpclient.execute(httppost,
						new BasicResponseHandler());
				// Return the data in the requested format
				if (type == "array") {
					return new JSONArray(response);
				} else {
					return new JSONObject(response);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "getDataFromJSON with login failed 1", e);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "getDataFromJSON with login failed 2", e);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "getDataFromJSON with login failed 3", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(
						context.getApplicationContext(),
						"Connection to Trakt.tv host failed. Do you have internet access?",
						Toast.LENGTH_SHORT).show();
				Log.e(TAG, "getDataFromJSON with login failed", e);
			}
		} else { // No login
			// Simply perform a GET
			HttpGet httpget = new HttpGet(url);
			try {
				String response = httpclient.execute(httpget,
						new BasicResponseHandler());
				// Return the data in the requested format
				if (type == "array") {
					return new JSONArray(response);
				} else {
					return new JSONObject(response);
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "getDataFromJSON without login failed", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(
						context.getApplicationContext(),
						"Connection to Trakt.tv host failed. Do you have internet access?",
						Toast.LENGTH_SHORT).show();
				Log.e(TAG, "getDataFromJSON without login failed", e);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "getDataFromJSON without login failed", e);
			}
		}
		return null;

	}

	/**
	 * Retrieve an array of data without logging in.
	 * 
	 * @param url
	 *            API URL.
	 * @return JSONArray containing data returned by server.
	 */
	public JSONArray getDataArrayFromJSON(String url) {
		return (JSONArray) getDataFromJSON(url, false, "array");

	}

	/**
	 * Retrieve an array of data allows logging in.
	 * 
	 * @param url
	 *            API URL.
	 * @param login
	 *            Send login to server
	 * @return JSONArray containing data returned by server.
	 */
	public JSONArray getDataArrayFromJSON(String url, boolean login) {
		return (JSONArray) getDataFromJSON(url, login, "array");
	}

	/**
	 * Retrieve a single object of data.
	 * 
	 * @param url
	 *            API URL.
	 * @param login
	 *            Send login to server
	 * @return JSONObject containing data returned by server.
	 */
	public JSONObject getDataObjectFromJSON(String url, boolean login) {
		return (JSONObject) getDataFromJSON(url, login, "object");
	}

	public JSONObject getDataObjectFromJSON(String url, boolean login,
			JSONObject post) {
		return (JSONObject) getDataFromJSON(url, login, "object", post);
	}

}
