package info.androidhive.slidingmenu;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class TvFragment extends Fragment {

	public TvFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.getActivity().setTitle("SUGGESTIONS");

		View rootView = inflater
				.inflate(R.layout.tv_fragment, container, false);
		Bundle bundle = getArguments();

		String toSearch = bundle.getString("toSearch");
		String[] strings;
		Tv_Show prova;

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

			Vector<Season> vector = prova.getSeasons();
			strings = new String[vector.size()];

			for (int i = 0; i < prova.getSeasons().size(); i++) {
				strings[i] = vector.get(i).toString();

			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this
					.getActivity().getApplicationContext(),
					R.layout.single_spinner, R.id.text1, strings);

			Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
			spinner.setAdapter(adapter);

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

		
		WebView id1 = (WebView) rootView.findViewById(R.id.web1);
		//id1.loadUrl(urls2.get(index).get(0));
		id1.setInitialScale(160);
		id1.setFocusable(false);
		id1.setClickable(false);
		id1.setVisibility(View.VISIBLE);
		id1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();

				switch (action) {
				case MotionEvent.ACTION_CANCEL:
					return true;
				case MotionEvent.ACTION_UP:

					/*
					 * Intent intent = new Intent(arg0.getContext(),
					 * HomeFragment.class); intent.putExtra("toSearch",
					 * urls2.get(index).get(1));
					 * arg0.getContext().startActivity(intent);
					 */
					Fragment fragment = new TvFragment();
					Bundle args = new Bundle();
					//args.putString("toSearch", urls2.get(index).get(1));
					fragment.setArguments(args);
					fm.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
					;

					return true;

				}

				return false;
			}
		});
		
		
		return rootView;

		
	}

	public void getSeasonOverview(View view){
	}
	
}
