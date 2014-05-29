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

import engine.Episode;
import engine.MyDatabase;
import engine.Search;
import engine.SeenList;
import engine.TraktAPI;
import engine.TvShow_result;
import engine.Tv_Show;
import engine.WatchingList;
import fragments.SeasonProgressFragment.MyDialogFragment2;
import adapters.ExpandableListAdapter;
import adapters.SearchAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
	TraktAPI api;
	Vector<TvShow_result> string;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	View p;
	Vector<String> codes = new Vector<String>();
	HashMap<String, List<String>> listDataChild;
	Context context;
	int position, position2;
	Fragment fragment;
	Activity a;

	public WatchingListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.getActivity().getApplicationContext());

		String user = prefs.getString("user", "");
		View rootView = inflater.inflate(R.layout.fragment_watching, container,
				false);
		fragment = this;
		if (!user.isEmpty()){
		
		a = this.getActivity();

		listDataChild = new HashMap<String, List<String>>();
		String mySeenList = "MyWatchList";

		DataGrabber3 data2 = new DataGrabber3(this.getActivity()
				.getApplicationContext());
		data2.execute();
		string = new Vector<TvShow_result>();
		context = this.getActivity().getApplicationContext();
		
		try {
			JSONArray array;
			array = data2.get();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				String code = object.getString("tvdb_id");
				codes.add(code);
				TvShow_result show = new TvShow_result(code, this.getActivity()
						.getApplicationContext());

				show.getEpisodesWatching();
				string.add(show);

				listDataChild.put(show.id, show.episodesVector);

			}

			DataGrabber4 data1 = new DataGrabber4(this.getActivity()
					.getApplicationContext());
			data1.execute();

			JSONArray array1;
			array1 = data1.get();

			for (int i = 0; i < array1.length(); i++) {
				JSONObject object = array1.getJSONObject(i);
				String code = object.getString("tvdb_id");
				TvShow_result show = new TvShow_result(code, this.getActivity()
						.getApplicationContext());

				if (!codes.contains(code)) {
					show.getEpisodesWatching();
					string.add(show);
					List<String> d = new Vector<String>();

					listDataChild.put(show.id, d);

				}

			}

			listAdapter = new ExpandableListAdapter(this.getActivity()
					.getApplicationContext(), string, listDataChild);
			expListView = (ExpandableListView) rootView
					.findViewById(R.id.lvExp);

			expListView.setAdapter(listAdapter);
			expListView.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {

					position = groupPosition;
					position2 = childPosition;

					new MyDialogFragment2().show(getFragmentManager(),
							"MyDialog");

					return false;

				}
			});

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

		this.getActivity().setTitle(mySeenList);}
		
		
		else{
			context = this.getActivity().getApplicationContext();
			MyDatabase mdb = new MyDatabase(this.getActivity().getApplicationContext(), this.getActivity()); 
            Vector<String> shows = mdb.getTvShows2();
				
            string = new Vector<TvShow_result>();
            listDataChild = new HashMap<String, List<String>>();
            
			for (String code : shows){	
				
        	TvShow_result show = new TvShow_result(code, this.getActivity().getApplicationContext());

			try {
				show.getEpisodesWatching();
				string.add(show);
	            Vector<String> s = show.episodesVector;
		
				listDataChild.put(show.id, s);
				
				
		
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
			
			}
			listAdapter = new ExpandableListAdapter(this.getActivity()
					.getApplicationContext(), string, listDataChild);
			expListView = (ExpandableListView) rootView
					.findViewById(R.id.lvExp);

			expListView.setAdapter(listAdapter);
		
			
			
			
			expListView.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {

					position = groupPosition;
					position2 = childPosition;

					new MyDialogFragment2().show(getFragmentManager(),
							"MyDialog");

					return false;

				}
			});
			
			
			
		}

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

			data = api.getDataArrayFromJSON("user/watchlist/episodes.json/%k/"
					+ user, false);

			return data;

		}

	}

	class DataGrabber4 extends AsyncTask<String, Void, JSONArray> {
		private ProgressDialog progressdialog;
		private Context parent;

		private JSONArray data;

		public DataGrabber4(Context parent) {
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

			data = api.getDataArrayFromJSON("user/watchlist/shows.json/%k/"
					+ user, false);

			return data;

		}

	}

	public class MyDialogFragment2 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

					.setMessage(
							"Do you confirm to remove \nthe episode from your WatchList")
					.setNegativeButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {

										TvShow_result tvr = string
												.get(position);
										tvr.getEpisodesWatching();

										String str = tvr.episodesVector
												.get(position2);
										str = str.replace("<b>", "")
												.replace("</b>", "")
												.replace("<br>", "")
												.replace("</br>", "");

										String idFinal = "" + str.charAt(1)
												+ str.charAt(2);
										idFinal = idFinal.replace(" ", "");

										String season_nFinal = ""
												+ str.charAt(str.length() - 2)
												+ str.charAt(str.length() - 1);
										season_nFinal = season_nFinal.replace(
												" ", "");
										
										SharedPreferences prefs = PreferenceManager
												.getDefaultSharedPreferences(context);

										String user = prefs.getString("user", "");
										
										
										if (!user.isEmpty()){
										Episode e = new Episode(idFinal,
												tvr.id, season_nFinal, context, a);
										e.addToWatching(true, null);}
										
										
										else{
											MyDatabase mdb = new MyDatabase( context, new Activity ());
											mdb.deleteEpisode2(Integer.valueOf(season_nFinal), Integer.valueOf(idFinal), Integer.valueOf(tvr.id));
											
										}

										FragmentTransaction ft = getFragmentManager()
												.beginTransaction();
										ft.detach(fragment);
										ft.attach(fragment);
										ft.commit();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ExecutionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

								}

							}).setPositiveButton("Cancel", null).create();
		}
	}

}