package fragments;


import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.R.id;
import info.androidhive.slidingmenu.R.layout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
import adapters.ExpandableListAdapter;
import adapters.SearchAdapter;

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
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	
	public WatchingListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_watching, container,
				false);
		listDataChild = new HashMap<String, List<String>>();
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
				
					show.getEpisodesWatching();
					string.add(show);
					
					listDataChild.put(show.id, show.episodesVector);
					
					
				}
				
				
				
				
				listAdapter = new ExpandableListAdapter( this.getActivity().getApplicationContext(),string,listDataChild);
			   expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
			
			
			
		//	expListView.setAdapter(listAdapter);

		
			expListView.setAdapter(listAdapter);
		
			
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		
		
		this.getActivity().setTitle(mySeenList);

		//ListView list = (ListView) rootView.findViewById(R.id.list);
		//final WatchingList watchingList;



		return rootView;
			
	}
	
	
	
	
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Top 250");
		listDataHeader.add("Now Showing");
		listDataHeader.add("Coming Soon..");

		// Adding child data
		List<String> top250 = new ArrayList<String>();
		top250.add("The Shawshank Redemption");
		top250.add("The Godfather");
		top250.add("The Godfather: Part II");
		top250.add("Pulp Fiction");
		top250.add("The Good, the Bad and the Ugly");
		top250.add("The Dark Knight");
		top250.add("12 Angry Men");

		List<String> nowShowing = new ArrayList<String>();
		nowShowing.add("The Conjuring");
		nowShowing.add("Despicable Me 2");
		nowShowing.add("Turbo");
		nowShowing.add("Grown Ups 2");
		nowShowing.add("Red 2");
		nowShowing.add("The Wolverine");

		List<String> comingSoon = new ArrayList<String>();
		comingSoon.add("2 Guns");
		comingSoon.add("The Smurfs 2");
		comingSoon.add("The Spectacular Now");
		comingSoon.add("The Canyons");
		comingSoon.add("Europa Report");

		listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
		listDataChild.put(listDataHeader.get(1), nowShowing);
		listDataChild.put(listDataHeader.get(2), comingSoon);
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