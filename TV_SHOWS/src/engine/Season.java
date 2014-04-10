package engine;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Season {

	private TraktAPI api;
	public String id, n_episode,image, code;
	public Context parent;
	public Vector<Episode> episodes;
	
	public Season(String id, String n_episode, String image, String code, Context parent) throws InterruptedException, ExecutionException, JSONException{
		this.id = id;
		this.n_episode = n_episode;
		this.image = image;
		this.code = code;
		this.parent=parent;
		
		//getEpisodes();
	}
	
	
	public String toString(){
		
		return "Season " + id + " - " + n_episode + " Episodes";
		
	}
	
	
	public void getEpisodes() throws InterruptedException, ExecutionException, JSONException{
		
		api = new TraktAPI(parent);
		
		
		DataGrabber dg = new DataGrabber(parent);
		JSONArray array = new JSONArray();
		dg.execute();
		array = dg.get();
		episodes = new Vector<Episode>();
		
		for (int i = 0; i<array.length();i++){
			JSONObject object = array.getJSONObject(i);
			
			String id_e = object.getString("episode");
			//String s_n = season_n;
			String title = object.getString("title");
			String first_aired = object.getString("first_aired_iso");
					if (first_aired.length() > 10){ 
						first_aired= 	first_aired.substring(0, 10);}
			String overview = object.getString("overview");
			String image = object.getJSONObject("images").getString("screen");
			String percentage = object.getJSONObject("ratings").getString("percentage");
			
			Episode e = new Episode(id_e, id, title, first_aired, overview, image, percentage, code, parent);
			episodes.add(e);
			
		}
	
	
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
			data = api.getDataArrayFromJSON(
					"show/season.json/%k/" + code + "/" + id,
					false);

			// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
			// true);

			return data;

		}

	}
	
	
	
}
