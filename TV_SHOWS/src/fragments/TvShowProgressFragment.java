package fragments;

import info.androidhive.slidingmenu.R;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import adapters.ProgressAdapter;
import adapters.SearchAdapter;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import engine.Season;
import engine.SeenList;
import engine.SeenObject;
import engine.TvShowProgress;
import engine.Tv_Show;

public class TvShowProgressFragment extends Fragment {

	Context context;
	
	public TvShowProgressFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

	

		
		View rootView = inflater.inflate(R.layout.fragment_tvshowprogress, container,
				false);

		String mySeenList = "MySeenList";

		this.getActivity().setTitle(mySeenList);
		context = this.getActivity().getApplicationContext();
		
		Bundle bundle = getArguments();

		String code = bundle.getString("toP");
		

		
		
		try {
			
			TvShowProgress tvsp = new TvShowProgress(context, code);
			TextView title = (TextView) rootView.findViewById(R.id.tvshow_title);
			TextView progress = (TextView) rootView.findViewById(R.id.percentage_show);
			title.setText(tvsp.title);
		
			progress.setText(tvsp.progress + "%");
			
			ProgressBar progressbar = (ProgressBar) rootView.findViewById(R.id.pbar1);
			progressbar.setMax(100);
			progressbar.setProgress(Integer.parseInt(tvsp.progress));
			
			if (progress.getText().equals("100%")){
				 Resources res = getResources();;
				progressbar.setProgressDrawable(res.getDrawable(R.drawable.greenprogress2));
				progress.setTextColor(Color.GREEN);
				
			}
			
			Tv_Show show = new Tv_Show(code,context);
			
			Vector<Season> vector = show.getSeasons();

			String[] strings = new String[vector.size()];
			
			ProgressAdapter adapter = new ProgressAdapter(vector, context, this.getFragmentManager(), tvsp.vector);
		
			
			
			
			for (int i = 0; i < vector.size(); i++) {
				strings[i] = vector.get(i).toString();

			}
			/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this
					.getActivity().getApplicationContext(),
					R.layout.activity_main_activity2,R.id.ses, strings);*/
			
			
			
			ListView list = (ListView) rootView.findViewById(R.id.list_g);
			
		 list.setAdapter(adapter);


			
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
}
