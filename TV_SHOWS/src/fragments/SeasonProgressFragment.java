package fragments;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Season;
import engine.TvShowProgress;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.R.layout;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SeasonProgressFragment extends Fragment {

	public SeasonProgressFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_seasonprogress, container,false);

		Bundle bundle = getArguments();

		String code = bundle.getString("code");
		String id = bundle.getString("id");
		
		try {
			TvShowProgress tsp = new TvShowProgress(this.getActivity().getApplicationContext(), code);
			Season season = new Season (id, null, null, code, this.getActivity().getApplicationContext());
			season.getEpisodes();
			TextView title = (TextView) rootView.findViewById(R.id.season_title);
			title.setText(tsp.title + " - " + "Season " + id);
			
			
			
			
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










}
