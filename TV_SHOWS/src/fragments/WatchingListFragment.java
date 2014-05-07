package fragments;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.R.id;
import info.androidhive.slidingmenu.R.layout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import engine.Search;
import engine.SeenList;
import engine.TraktAPI;
import engine.TvShow_result;
import engine.Tv_Show;
import engine.WatchingList;
import adapters.SearchAdapter;
import adapters.WatchingAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WatchingListFragment extends Fragment {
	private TraktAPI api;
	private Vector<TvShow_result> string; 
	
	public WatchingListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_watching, container,
				false);

		String mySeenList = "MyWatchingList";
	
		DataGrabber3 data2 = new DataGrabber3(this.getActivity().getApplicationContext());
		data2.execute();
		string= new Vector<TvShow_result>();
			
			try {
				JSONArray array;
				array = data2.get();
				for (int i = 0; i < array.length(); i++){
					JSONObject object = array.getJSONObject(i);
					String code = object.getString("tvdb_id");
					TvShow_result show = new TvShow_result(code, this.getActivity().getApplicationContext());
					string.add(show);
					
					
				}
				
				
				
				
			WatchingAdapter adapter = new WatchingAdapter( this.getActivity().getApplicationContext(),string);
			ExpandableListView expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
			
		
			
			expListView.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					
					parent.expandGroup(groupPosition);
					Log.e("", "CIAOOOOO");
					
					return true;
				}
			});

		
			expListView.setAdapter(adapter);
		
			
			
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
			
			
			
		
		
		this.getActivity().setTitle(mySeenList);

		//ListView list = (ListView) rootView.findViewById(R.id.list);
		//final WatchingList watchingList;



		return rootView;
		
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
			api = new TraktAPI(parent);
			data = new JSONArray();
			
			
			
			data = api.getDataArrayFromJSON("user/watchlist/episodes.json/%k/" + user, false);
		

			return data;
			
			

		}

	}
	
	
	
	
	
	
}