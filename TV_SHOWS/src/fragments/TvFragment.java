package fragments;

import info.androidhive.slidingmenu.R;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
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

			final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
			spinner.setAdapter(adapter);
			
			
			Button confirm = (Button) rootView.findViewById(R.id.button1);
				
			confirm.setOnClickListener(new OnClickListener(){

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
			android.app.FragmentTransaction ft = fragmentManager.beginTransaction();
			 ft.replace(R.id.frame_container, fragment);
			 ft.addToBackStack("");
			 ft.commit();
			
			
	
			//fm.beginTransaction().replace(R.id.frame_container, fragment).commit();
			
			
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
		
	
		
		

		
				/*	Fragment fragment = new TvFragment();
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
		});*/
		
		
		return rootView;

		
	}

	public void getSeasonOverview(View view){
	}
	
}
