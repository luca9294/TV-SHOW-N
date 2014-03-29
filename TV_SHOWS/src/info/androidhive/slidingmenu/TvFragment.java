package info.androidhive.slidingmenu;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class TvFragment extends Fragment {
	
	public TvFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		this.getActivity().setTitle("SUGGESTIONS");
 
        View rootView = inflater.inflate(R.layout.tv_fragment, container, false);
        Bundle bundle = getArguments();

		String toSearch = bundle.getString("toSearch");

		// TextView title =(TextView)findViewById(R.id.title);
		TextView premiere = (TextView) rootView.findViewById(R.id.premiere);
		TextView country = (TextView) rootView.findViewById(R.id.country);
		TextView percentage = (TextView) rootView.findViewById(R.id.percentage);
		TextView runtime = (TextView) rootView.findViewById(R.id.runtime);
		TextView genre = (TextView) rootView.findViewById(R.id.genre);
		TextView overview = (TextView) rootView.findViewById(R.id.overview);
		WebView image = (WebView) rootView.findViewById(R.id.image);

		try {

			Tv_Show prova = new Tv_Show(toSearch, getActivity().getApplicationContext());

			getActivity().setTitle(prova.title_n);
			premiere.setText(prova.first_aired_iso);
			country.setText(prova.country);
			runtime.setText(prova.runtime + "m");
			genre.setText(prova.genre);
			percentage.setText(prova.percentage + "%");
			overview.setText(prova.overview);
			overview.setMovementMethod(new ScrollingMovementMethod());
			image.loadUrl((prova.image).replace(".jpg", "-300.jpg"));
			image.setInitialScale(157);
			image.setFocusable(false);
			image.setClickable(false);

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
