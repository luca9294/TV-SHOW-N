package fragments;

import info.androidhive.slidingmenu.R;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Season;
import adapters.SeasonAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class SeasonFragment extends Fragment {
	View rootView;
	ListView view;
	Season season;
	String code;
	SeasonAdapter adapter;
	boolean check = false;

	public SeasonFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

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

			season.getEpisodes();

			TextView seasonTitle = (TextView) rootView
					.findViewById(R.id.season);
			seasonTitle.setText(season.toString());
			WebView web = (WebView) rootView.findViewById(R.id.imageSeason);
			web.loadUrl((season.image).replace(".jpg", "-300.jpg"));
			web.setInitialScale(170);
			web.setFocusable(false);
			web.setClickable(false);
			web.setVisibility(View.VISIBLE);

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

}