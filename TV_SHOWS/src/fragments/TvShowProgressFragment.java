package fragments;

import info.androidhive.slidingmenu.R;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import engine.SeenList;
import engine.TvShowProgress;

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
