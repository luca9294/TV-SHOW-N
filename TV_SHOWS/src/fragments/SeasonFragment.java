package fragments;

import info.androidhive.slidingmenu.R;

import java.text.ParseException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Season;
import fragments.EpisodeFragment.MyDialogFragment10;
import fragments.EpisodeFragment.MyDialogFragment12;
import fragments.EpisodeFragment.MyDialogFragment9;
import adapters.SeasonAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SeasonFragment extends Fragment {
	View rootView;
	ListView view;
	Season season;
	String code;
	SeasonAdapter adapter;
	Context context;
	boolean check = false;
	boolean seenBool = false;
	boolean watchBool = false;
	Fragment fragment;

	public SeasonFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragment = this;
		context = this.getActivity().getApplicationContext();

		rootView = inflater.inflate(R.layout.fragment_season, container, false);

		Bundle bundle = getArguments();

		String id = bundle.getString("id");
		String n_episode = bundle.getString("n_episode");
		String image = bundle.getString("image");
		code = bundle.getString("code");
		setHasOptionsMenu(true);
		try {
			season = new Season(id, n_episode, image, code, this.getActivity()
					.getApplicationContext());

			try {
				season.getEpisodes();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			TextView seasonTitle = (TextView) rootView
					.findViewById(R.id.season);
			seasonTitle.setText(season.toString());
			WebView web = (WebView) rootView.findViewById(R.id.imageSeason);
			web.loadUrl((season.image).replace(".jpg", "-300.jpg"));
			web.setInitialScale(170);
			web.setFocusable(false);
			web.setClickable(false);
			web.setVisibility(View.VISIBLE);

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);

			String user = prefs.getString("user", "");
			String pass = prefs.getString("passed", "");

			if (user.isEmpty()) {

				LinearLayout l = (LinearLayout) rootView
						.findViewById(R.id.linear);

				l.setVisibility(View.GONE);
			}

			Button seen = (Button) rootView.findViewById(R.id.seenListSeason);
			Button watch = (Button) rootView.findViewById(R.id.watchingList);
			if (season.checkSeen() == true) {
				seen.setText("SEASON SEEN");
				seen.setTextColor(Color.GREEN);
				seenBool = true;

			} else {
				seen.setText("ADD\nSEEN\nLIST");
				seen.setTextColor(Color.WHITE);
				seenBool = false;
			}

			if (season.checkWatch() == true) {
				watch.setText("IN WATCHLIST");
				watch.setTextColor(Color.GREEN);
				watchBool = true;

			} else {
				watch.setText("ADD\nTO WATCH LIST");
				watch.setTextColor(Color.WHITE);
				watchBool = false;
			}

			view = (ListView) rootView.findViewById(R.id.listEpisodes);
			// view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

			adapter = new SeasonAdapter(false, season.episodes, this
					.getActivity().getApplicationContext(),
					getFragmentManager());
			view.setAdapter(adapter);

			view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Fragment fragment = new EpisodeFragment();

					Bundle args = new Bundle();
					args.putString("id", season.episodes.get(arg2).id);
					args.putString("season_n",
							season.episodes.get(arg2).season_n);
					args.putString("code", code);
					fragment.setArguments(args);

					FragmentManager fragmentManager = getFragmentManager();
					android.app.FragmentTransaction ft = fragmentManager
							.beginTransaction();
					ft.replace(R.id.frame_container, fragment);
					ft.addToBackStack("");
					ft.commit();

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
		}

		final Button seen = (Button) rootView.findViewById(R.id.seenListSeason);
		seen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(context);

				String user = prefs.getString("user", "");
				String pass = prefs.getString("passed", "");

				if (user.isEmpty()) {

					new MyDialogFragment().show(getFragmentManager(),
							"MyDialog");
				}

				else {

					if (seenBool == false) {
						try {
							season.addToSeen(seenBool);

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
						new MyDialogFragment1().show(getFragmentManager(),
								"MyDialog");

						// seen.setText("SEASON SEEN");
						// seen.setTextColor(Color.GREEN);
						seenBool = true;

						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						ft.detach(fragment);
						ft.attach(fragment);
						ft.commit();

					} else {
						try {
							season.removeFromSeen();

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
						new MyDialogFragment3().show(getFragmentManager(),
								"MyDialog");

						seen.setText("ADD\nSEEN\nLIST");
						seen.setTextColor(Color.WHITE);
						seenBool = false;
					}
				}

			}

		});

		final Button watching = (Button) rootView
				.findViewById(R.id.watchingList);
		watching.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(context);

				String user = prefs.getString("user", "");
				String pass = prefs.getString("passed", "");

				if (user.isEmpty()) {

					new MyDialogFragment().show(getFragmentManager(),
							"MyDialog");
				}

				else {

					if (watchBool == false) {
						try {
							season.addToWatch(false);

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
						new MyDialogFragment2().show(getFragmentManager(),
								"MyDialog");

						// watching.setText("SEASON SEEN");
						// watching.setTextColor(Color.GREEN);
						watchBool = true;

						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						ft.detach(fragment);
						ft.attach(fragment);
						ft.commit();

					} else {
						try {
							season.addToWatch(true);

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
						new MyDialogFragment3().show(getFragmentManager(),
								"MyDialog");

						watchBool = false;
					}
				}
			}

		});

		return rootView;

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main_activity2, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.selection:

			if (!check) {
				adapter = new SeasonAdapter(true, season.episodes, this
						.getActivity().getApplicationContext(),
						getFragmentManager());
				view.setAdapter(adapter);
				check = true;
				item.setTitle("BACK");
			}

			else {
				adapter = new SeasonAdapter(false, season.episodes, this
						.getActivity().getApplicationContext(),
						getFragmentManager());
				view.setAdapter(adapter);
				check = false;
				item.setTitle("SELECT");

				view.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Fragment fragment = new EpisodeFragment();

						Bundle args = new Bundle();
						args.putString("id", season.episodes.get(arg2).id);
						args.putString("season_n",
								season.episodes.get(arg2).season_n);
						args.putString("code", code);
						fragment.setArguments(args);

						FragmentManager fragmentManager = getFragmentManager();
						android.app.FragmentTransaction ft = fragmentManager
								.beginTransaction();
						ft.replace(R.id.frame_container, fragment);
						ft.addToBackStack("");
						ft.commit();

					}
				});

			}

			return true;

			// case R.id.action_add:
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class MyDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

					.setMessage(
							"You must be logged in order to add any episode to any list!")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment1 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Season added to the \"Seen List\"!")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment2 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Season added to the \"Watchlist\"!")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment3 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Season removed from the list!")
					.setPositiveButton("Ok", null).create();
		}

	}

}