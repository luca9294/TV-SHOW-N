package fragments;

import java.text.ParseException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import engine.Episode;
import engine.MyDatabase;
import engine.Season;
import engine.SeenObject;
import engine.TvShowProgress;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.R.layout;
import adapters.ProgressSeasonAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SeasonProgressFragment extends Fragment {
	private int index = 0;
	Season season = null;
	Fragment fragment = this;
	TvShowProgress tsp = null;
	String id = "";
	View rootView = null;
	Context context;
	Handler mHandler = new Handler();

	public SeasonProgressFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_seasonprogress,
				container, false);
context = this.getActivity().getApplicationContext();
		Bundle bundle = getArguments();

		String code = bundle.getString("code");
		id = bundle.getString("id");

		try {
			final ProgressBar progressbar = (ProgressBar) rootView
					.findViewById(R.id.pbar1);
			progressbar.setMax(100);
			tsp = new TvShowProgress(
					this.getActivity().getApplicationContext(), code);
			new Thread(new Runnable() {
				SeenObject object = tsp.getSeason(id);

				public void run() {

					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							progressbar.setProgress(Integer
									.parseInt(object.percentage));
						}
					});

				}
			}).start();
			season = new Season(id, null, null, code, this.getActivity()
					.getApplicationContext(), this.getActivity());
			season.getEpisodes();
			TextView title = (TextView) rootView
					.findViewById(R.id.season_title);
			title.setText(tsp.title + "\nSeason " + id);
			SeenObject object = tsp.getSeason(id);
			tsp.initialize();

			// progressbar.setProgress(0);

			// progressbar.setProgress(Integer.parseInt(object.percentage));

			TextView perc = (TextView) rootView
					.findViewById(R.id.percentage_show);
			TextView seen = (TextView) rootView.findViewById(R.id.seen);
			perc.setText(object.percentage + "%");

			if (perc.getText().equals("100%")) {
				Resources res = getResources();
				;
				progressbar.setProgressDrawable(res
						.getDrawable(R.drawable.greenprogress2));

				perc.setTextColor(Color.parseColor("#008000"));
				seen.setTextColor(Color.parseColor("#008000"));
				seen.setText(object.completed + "/" + object.total
						+ " Episodes SEEN");

			}

			else {

				seen.setTextColor(Color.RED);
				seen.setText(object.completed + "/" + object.total
						+ " Episodes SEEN");

			}

			ListView view = (ListView) rootView.findViewById(R.id.list_g);

			Vector<Episode> vector = season.episodes;

			for (int i = vector.size() - 1; i >= 0; i--) {
				if (vector.get(i).isFuture()) {
					vector.remove(i);

				}

				else {
					break;

				}

			}

			ProgressSeasonAdapter adapter = new ProgressSeasonAdapter(vector,
					this.getActivity().getApplicationContext(),
					this.getFragmentManager());
			view.setAdapter(adapter);

			view.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					if (season.episodes.get(arg2).watched) {
						index = arg2;
						new MyDialogFragment2().show(getFragmentManager(),
								"MyDialog");

					}

					else {
						index = arg2;
						new MyDialogFragment1().show(getFragmentManager(),
								"MyDialog");

					}

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

		return rootView;
	}

	// dialog fragments
	public class MyDialogFragment1 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

					.setMessage("Confirm to sign \nas SEEN the episode")
					.setNegativeButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {

										SharedPreferences prefs = PreferenceManager
												.getDefaultSharedPreferences(context);

										String user = prefs.getString("user", "");
										
										if (!user.isEmpty()){
										
										season.episodes.get(index).addToSeen(
												false, null);}
										
										else{
											Episode e = season.episodes.get(index);
											MyDatabase mdb = new MyDatabase(context,new Activity()) ;
											mdb.insertEpisodes(e.title, Integer.valueOf(e.season_n),Integer.valueOf( e.id), Integer.valueOf(e.code));
											
											
											
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
									}

								}

							}).setPositiveButton("Cancel", null).create();

		}
	}

	// dialog fragments
	public class MyDialogFragment2 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

					.setMessage("Confirm to sign \nas UNSEEN the episode")
					.setNegativeButton("Confirm",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									
									SharedPreferences prefs = PreferenceManager
											.getDefaultSharedPreferences(context);

									String user = prefs.getString("user", "");
									try {
									if (!user.isEmpty()){
								

										season.episodes.get(index).addToSeen(
												true, null);

										if (!tsp.getSeason(id).completed
												.equals("1")) {
											FragmentTransaction ft = getFragmentManager()
													.beginTransaction();
											ft.detach(fragment);
											ft.attach(fragment);
											ft.commit();
										}

										else {

											FragmentTransaction ft = getFragmentManager()
													.beginTransaction();
											fragment = new SeenListFragment();
											ft.replace(R.id.frame_container,
													fragment);
											ft.addToBackStack("");
											ft.commit();
										}}
									
									else{
										
										
										Episode e = season.episodes.get(index);
										MyDatabase mdb = new MyDatabase(context,new Activity()) ;
										mdb.deleteEpisode(Integer.valueOf(e.season_n),Integer.valueOf( e.id), Integer.valueOf(e.code));
										
										FragmentTransaction ft = getFragmentManager()
												.beginTransaction();
										ft.detach(fragment);
										ft.attach(fragment);
										ft.commit();
										
										
										
										
										
									}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ExecutionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

							}).setPositiveButton("Cancel", null).create();
		}
	}

}
