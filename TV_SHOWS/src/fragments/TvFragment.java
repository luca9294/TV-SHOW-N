package fragments;

import info.androidhive.slidingmenu.R;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
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
		final Vector<Season> vector;
		try {

			prova = new Tv_Show(toSearch, getActivity().getApplicationContext());

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
		}

		return rootView;

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
		case R.id.action_like:
			new MyDialogFragment().show(getFragmentManager(), "MyDialog");
			return true;
		case R.id.action_dislike:
			new MyDialogFragment1().show(getFragmentManager(), "MyDialog");
			return true;

			// if SEEN LIST item is selected
		case R.id.action_seenlist: // dialog 2

			if (user.isEmpty()) {
				new MyDialogFragment2().show(getFragmentManager(), "MyDialog");
			}

			else {
				if (seenBool == false) {
					try {
						prova.addToSeen(seenBool, null);

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

					//setOptionTitle(R.id.action_seenlist, "Remove from seen list");
					// seen.setTextColor(Color.GREEN);
					seenBool = true;
				} else {
					
					 try {
						prova.addToSeen(seenBool, null);
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
					 
					
					new MyDialogFragment4().show(getFragmentManager(),
							"MyDialog");

					//setOptionTitle(R.id.action_seenlist, "Add to seen list");
					// seen.setTextColor(Color.WHITE);
					seenBool = false;
				}

				return true;
			}
		case R.id.action_watchlist:
			new MyDialogFragment3().show(getFragmentManager(), "MyDialog");
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/***
	 **** dialog fragments
	 ***/

	public class MyDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Like option pressed").setPositiveButton("Ok", null)
					.create();
		}

	}

	public class MyDialogFragment1 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("disike option pressed").setPositiveButton("Ok", null)
					.create();
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

			.setMessage("Watchlist option pressed")
					.setPositiveButton("Ok", null).create();
		}

	}
	
	private void setOptionTitle(int id, String title)
	{
	 MenuItem item = menu.findItem(id);
	 item.setTitle(title);
	}
}
