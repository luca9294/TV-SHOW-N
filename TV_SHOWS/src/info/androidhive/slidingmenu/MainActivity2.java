package info.androidhive.slidingmenu;



import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class MainActivity2 extends Activity {
	TraktAPI api;
	JSONArray data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
       
        try {
			Search se = new Search ("Revenge",this);
			  Log.e("C", "CIAO");
			  String s = se.data.toString();
			  Log.e("C", s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }
    
    
    
    
    

	public void getArrayJSON() throws InterruptedException, ExecutionException {
		api = new TraktAPI(this);
	//	String title_c = toSearch.replace(" ", "-");
		//title_c = title_c.toLowerCase();

		DataGrabber e = new DataGrabber(this, "Revenge");
		e.execute();
		data = e.get();
	}

	private class DataGrabber extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressdialog;
		private Context parent;
		private String id;
		private JSONArray data;

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
		protected JSONArray doInBackground(String... params) {
			//data = api.getDataObjectFromJSON(
				//	"show/summary.json/361cd031c2473b06997c87c25ec9c057/" + id,
					//true);

			 data = api.getDataArrayFromJSON("search/shows.json/%k?query=revenge",true);

			return data;

		}
    
    
	}
    
}
