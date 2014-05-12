package fragments;

import info.androidhive.slidingmenu.R;

import java.text.ParseException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Activity;
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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import engine.Calendar;
import engine.Season;
import engine.Tv_Show;
import fragments.EpisodeFragment.MyDialogFragment10;
import fragments.EpisodeFragment.MyDialogFragment12;
import fragments.EpisodeFragment.MyDialogFragment6;
import fragments.EpisodeFragment.MyDialogFragment9;

public class TvFragment extends Fragment {

	Tv_Show prova;
	Context context;
	public boolean seenBool = false;
	public boolean watchBool = false;
	public boolean loves = false;
	public boolean hates = false;

	Vector<Season> vector;
	private Menu menu;

	public TvFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		this.getActivity().setTitle("SUGGESTIONS");
	
		View rootView = inflater
				.inflate(R.layout.tv_fragment, container, false);
		context = this.getActivity().getApplicationContext();
		Bundle bundle = getArguments();

		String toSearch = bundle.getString("toSearch");
		String[] strings;
		

		
		
		// TextView title =(TextView)findViewById(R.id.title);
		TextView premiere = (TextView) rootView.findViewById(R.id.premiere);
		TextView country = (TextView) rootView.findViewById(R.id.country);
		TextView percentage = (TextView) rootView.findViewById(R.id.percentage);
		TextView runtime = (TextView) rootView.findViewById(R.id.runtime);
		TextView genre = (TextView) rootView.findViewById(R.id.genre);
		TextView overview = (TextView) rootView.findViewById(R.id.overview);
		TextView seasons = (TextView) rootView.findViewById(R.id.season_n);
		WebView image = (WebView) rootView.findViewById(R.id.image);

		try {

			prova = new Tv_Show(toSearch, getActivity().getApplicationContext(), this.getActivity());
			loves = prova.loves;
			hates = prova.hates;
			getActivity().setTitle(prova.title_n);
			premiere.setText(prova.first_aired_iso);
			country.setText(prova.country);
			runtime.setText(prova.runtime + "m");
			genre.setText(prova.genre);
			seasons.setText(prova.seasons_n);
			percentage.setText(prova.percentage + "%");
			overview.setText(prova.overview);
			overview.setMovementMethod(new ScrollingMovementMethod());
			image.loadUrl((prova.image).replace(".jpg", "-300.jpg"));
			image.setInitialScale(157);
			image.setFocusable(false);
			image.setClickable(false);

			vector = prova.getSeasons();

			strings = new String[vector.size()];

			for (int i = 0; i < prova.getSeasons().size(); i++) {
				strings[i] = vector.get(i).toString();

			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this
					.getActivity().getApplicationContext(),
					R.layout.single_spinner, R.id.text1, strings);

			final Spinner spinner = (Spinner) rootView
					.findViewById(R.id.spinner);
			spinner.setAdapter(adapter);

			Button confirm = (Button) rootView.findViewById(R.id.button1);

			confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					int pos = spinner.getSelectedItemPosition();
					Season season = vector.get(pos);
					Fragment fragment = new SeasonFragment();
					Bundle args = new Bundle();
					args.putString("id", season.id);
					args.putString("n_episode", season.n_episode);
					args.putString("image", season.image);
					args.putString("code", season.code);
					fragment.setArguments(args);

					FragmentManager fragmentManager = getFragmentManager();
					android.app.FragmentTransaction ft = fragmentManager
							.beginTransaction();
					ft.replace(R.id.frame_container, fragment);
					ft.addToBackStack("");
					ft.commit();

					// fm.beginTransaction().replace(R.id.frame_container,
					// fragment).commit();

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

		seenBool = isSeen();
		watchBool = prova.in_watching;

		return rootView;

	}

	// for each season of a tv show, it checkes if it is seen.
	public boolean isSeen() {
		boolean result = true;
		for (Season i : vector) {
			try {
				i.getEpisodes();
				if (Integer.parseInt(i.id) != 0) {
					if (!i.checkSeen()) {
						Log.e("", "FALSE");
						result = false;
						break;
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

		return result;

	}

	public void getSeasonOverview(View view) {
	}

	// loads option menu
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.rate, menu);

	}

	// handles selection of an option item
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("passed", "");

		// Handles item selection
		switch (item.getItemId()) {

		// if like and dislike buttons are selected
		case R.id.action_like:
			if (user.isEmpty()) {
				new MyDialogFragment2().show(getFragmentManager(), "MyDialog");
			}

			else {
				try {

					if (!loves && !hates) {

						item.setIcon(R.drawable.ic_action_good_dark);
						prova.makeARate("love");
						new MyDialogFragment().show(getFragmentManager(),
								"MyDialog");
						loves = true;
					}

					else if (loves) {
						item.setIcon(R.drawable.ic_action_good);
						prova.makeARate("unrate");
						new MyDialogFragment0().show(getFragmentManager(),
								"MyDialog");
						loves = false;

					}

					else if (!prova.loves && prova.hates) {
						prova.makeARate("love");
						new MyDialogFragment().show(getFragmentManager(),
								"MyDialog");
						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();

						ft.detach(this);
						ft.attach(this);
						ft.commit();

					}

				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ExecutionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				// ft.detach(this);
				// ft.attach(this);
				// ft.commit();

			}

			return true;

		case R.id.action_dislike:
			if (user.isEmpty()) {
				new MyDialogFragment2().show(getFragmentManager(), "MyDialog");
			}

			else {

				try {
					if (!loves && !hates) {
						item.setIcon(R.drawable.ic_action_bad_dark);
						prova.makeARate("hate");
						new MyDialogFragment1().show(getFragmentManager(),
								"MyDialog");
						hates = true;

					}

					else if (hates) {
						item.setIcon(R.drawable.ic_action_bad);
						prova.makeARate("unrate");
						new MyDialogFragment0().show(getFragmentManager(),
								"MyDialog");
						hates = false;
					}

					else if (!hates && loves) {
						prova.makeARate("hate");
						new MyDialogFragment1().show(getFragmentManager(),
								"MyDialog");
						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();

						ft.detach(this);
						ft.attach(this);
						ft.commit();

					}

				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ExecutionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}
			return true;

			// if UNRATE item is selected
		case R.id.action_unrate:
			if (user.isEmpty()) {
				new MyDialogFragment2().show(getFragmentManager(), "MyDialog");
			}

			else {

				try {
					prova.makeARate("unrate");
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ExecutionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				new MyDialogFragment0().show(getFragmentManager(), "MyDialog");
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.detach(this);
				ft.attach(this);
				ft.commit();

			}
			return true;

			// if SEEN LIST item is selected
		case R.id.action_seenlist: // dialog 2

			if (user.isEmpty()) {
				new MyDialogFragment2().show(getFragmentManager(), "MyDialog");
			}

			else {
				if (seenBool == false) {
					try {
						new MyDialogFragment3().show(getFragmentManager(),
								"MyDialog");
						prova.addToSeen(seenBool, null);

						FragmentTransaction ft = getFragmentManager()
								.beginTransaction();
						ft.detach(this);
						ft.attach(this);
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

					// seen.setTextColor(Color.GREEN);
					seenBool = true;
				} else {

					for (Season i : vector) {

						try {
							i.getEpisodes();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ExecutionException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (Integer.parseInt(i.id) != 0) {
							try {
								i.removeFromSeen();
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
					}

					new MyDialogFragment4().show(getFragmentManager(),
							"MyDialog");

					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					ft.detach(this);
					ft.attach(this);
					ft.commit();

					// setOptionTitle(R.id.action_seenlist, "Add to seen list");
					// seen.setTextColor(Color.WHITE);
					seenBool = false;
				}

				return true;
			}

			// if WATCHLIST item is selected
		case R.id.action_watchlist:
			if (user.isEmpty()) {
				new MyDialogFragment2().show(getFragmentManager(), "MyDialog");
			} else {
				if (watchBool) {
					try {
						prova.addToWatch(false);
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

					new MyDialogFragment6().show(getFragmentManager(),
							"MyDialog");

				}

				else {
					try {
						prova.addToWatch(true);
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
					new MyDialogFragment5().show(getFragmentManager(),
							"MyDialog");

				}

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.detach(this);
				ft.attach(this);
				ft.commit();

				return true;
			}

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	// changes names of the option menu
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// menu.removeItem(R.id.action_like)
		if (isSeen()) {
			menu.findItem(R.id.action_seenlist).setTitle(
					"Remove from seen list");
		}

		if (prova.in_watching) {
			menu.findItem(R.id.action_watchlist).setTitle(
					"Remove from watch list");

		}

		if (prova.loves) {

			menu.findItem(R.id.action_like).setIcon(
					R.drawable.ic_action_good_dark);

		}

		if (prova.hates) {

			menu.findItem(R.id.action_dislike).setIcon(
					R.drawable.ic_action_bad_dark);

		}

	}

	/***
	 **** dialog fragments
	 ***/

	public class MyDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You LOVE this TV SHOW").setPositiveButton("Ok", null)
					.create();
		}

	}

	public class MyDialogFragment1 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You HATE this TV SHOW").setPositiveButton("Ok", null)
					.create();
		}

	}

	public class MyDialogFragment0 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You have unrate this TV SHOW")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment2 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

					.setMessage(
							"You must be logged in order to perform any of the menu actions!")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment3 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Show added to the Seen List")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment4 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Show removed from Seen List")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment5 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Show added to the Watchlist")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment6 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Show removed to the Watchlist")
					.setPositiveButton("Ok", null).create();
		}

	}

}
