package fragments;

import info.androidhive.slidingmenu.R;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import adapters.SeasonAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import engine.Season;
import engine.Episode;


;

public class EpisodeFragment extends Fragment {
	private ShareActionProvider mShareActionProvider;
	String id; 
	String season_n; 
	String code; 
	
	
	public EpisodeFragment(){
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_episode, container,
				false);
		
		
	   setHasOptionsMenu(true);
	 ;
		Bundle bundle = getArguments();

		id = bundle.getString("id");
		 season_n = bundle.getString("season_n");
		code = bundle.getString("code");

		
		try {
			Episode episode = new Episode(id, code, season_n, this.getActivity().getApplicationContext());
			episode.getComments();
			
			TextView season_n_e = (TextView) rootView.findViewById(R.id.season_n);
			TextView id_e = (TextView) rootView.findViewById(R.id.id);
			TextView title = (TextView) rootView.findViewById(R.id.title);
			TextView first_aired_date = (TextView) rootView.findViewById(R.id.first_air_date);
			TextView percentage = (TextView) rootView.findViewById(R.id.percentage);
			TextView overview = (TextView) rootView.findViewById(R.id.overview);
			overview.setMovementMethod(new ScrollingMovementMethod());
			WebView web = (WebView) rootView.findViewById(R.id.imageEpisode);
			web.loadUrl(episode.image);
			//web.setInitialScale(100);
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
		
		
		Button button =  (Button) rootView.findViewById(R.id.viewComments);
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
				android.app.FragmentTransaction ft = fragmentManager.beginTransaction();
				 ft.replace(R.id.frame_container, fragment);
				 ft.addToBackStack("");
				 ft.commit();
				
				
			}
		});

		return rootView;

	}
	
	


	

}