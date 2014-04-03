package info.androidhive.slidingmenu;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

public class SeasonFragment extends Fragment {
	
	public SeasonFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_season, container, false);
         
        Bundle bundle = getArguments();

		String id = bundle.getString("id");
		String n_episode = bundle.getString("n_episode");
		String image = bundle.getString("image");
		String code = bundle.getString("code");
		
		
	try {
		Season season =  new Season(id, n_episode,image, code, this.getActivity().getApplicationContext());
	
		TextView seasonTitle = (TextView) rootView.findViewById(R.id.season);
		seasonTitle.setText(season.toString());
		WebView web = (WebView) rootView.findViewById(R.id.imageSeason);
		web.loadUrl(season.image);
		web.setInitialScale(160);
		web.setFocusable(false);
		web.setClickable(false);
		web.setVisibility(View.VISIBLE);
		
		ListView view = (ListView) rootView.findViewById(R.id.listEpisodes);
		
		EpisodeAdapter adapter = new EpisodeAdapter(season.episodes, this.getActivity().getApplicationContext(),getFragmentManager());
		view.setAdapter(adapter);
	
	
	
	
	
	
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
