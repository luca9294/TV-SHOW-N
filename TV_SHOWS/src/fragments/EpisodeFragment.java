package fragments;

import info.androidhive.slidingmenu.R;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import adapters.SeasonAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import engine.Comment;
import engine.MainActivity;
import engine.Season;
import engine.Episode;
import fragments.LoginFragment.MyDialogFragment;

;

public class EpisodeFragment extends Fragment {
	private ShareActionProvider mShareActionProvider;
	String id;
	String season_n;
	String code;
	Context context;

	public EpisodeFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_episode, container,
				false);
		context = this.getActivity().getApplicationContext();

	
		
		
		
		
		
		
		setHasOptionsMenu(true);
		;
		Bundle bundle = getArguments();

		id = bundle.getString("id");
		season_n = bundle.getString("season_n");
		code = bundle.getString("code");

		try {
			Episode episode = new Episode(id, code, season_n, this
					.getActivity().getApplicationContext());
			episode.getComments();

			TextView season_n_e = (TextView) rootView
					.findViewById(R.id.season_n);
			TextView id_e = (TextView) rootView.findViewById(R.id.id);
			TextView title = (TextView) rootView.findViewById(R.id.title);
			TextView first_aired_date = (TextView) rootView
					.findViewById(R.id.first_air_date);
			TextView percentage = (TextView) rootView
					.findViewById(R.id.percentage);
			TextView overview = (TextView) rootView.findViewById(R.id.overview);
			overview.setMovementMethod(new ScrollingMovementMethod());
			WebView web = (WebView) rootView.findViewById(R.id.imageEpisode);
			web.loadUrl(episode.image);
			// web.setInitialScale(100);
			web.setFocusable(false);
			web.setClickable(false);
			web.setVisibility(View.VISIBLE);

			season_n_e.setText(episode.season_n);
			id_e.setText(episode.id);
			title.setText(episode.title);
			first_aired_date.setText(episode.first_aired_date);
			percentage.setText(episode.percentage + "%");
			overview.setText(episode.overview);

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

		Button comment = (Button) rootView.findViewById(R.id.sendComment);
		comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(context);

				String user = prefs.getString("user", "");
				String pass = prefs.getString("passed", "");

				if (user.isEmpty()) {

					new MyDialogFragment2().show(getFragmentManager(),
							"MyDialog");
				}

				else {

					new MyDialogFragment3().show(getFragmentManager(),
							"MyDialog");

				}

			}

		});

		Button button = (Button) rootView.findViewById(R.id.viewComments);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fragment fragment = new CommentsFragment();

				Bundle args = new Bundle();
				args.putString("id", id);
				args.putString("season_n", season_n);
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
		
		
		/*
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("passed", "");
		JSONObject prova = new  JSONObject();
		try {
			prova.accumulate("username", user);
			prova.accumulate("password", pass);
			prova.accumulate("tvdb_id", code);
			prova.accumulate("season", season_n);
			prova.accumulate("episode", id);
			prova.accumulate("comment", "SAU GEIL");
			prova.accumulate("spoiler", "SAU GEIL");
			prova.accumulate("review", "SAU GEIL");
			 
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Comment com = new Comment (prova);
		try {
			com.send();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		*/
		

		return rootView;

	}

	public class MyDialogFragment2 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You must be logged in order to comment!")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment3 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);

			View promptView = layoutInflater.inflate(R.layout.prompts, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			alertDialogBuilder.setView(promptView);

			return new AlertDialog.Builder(getActivity())

			.setMessage("PROVA").setPositiveButton("Ok", null)
					.setView(promptView).create();

		}

	}

}