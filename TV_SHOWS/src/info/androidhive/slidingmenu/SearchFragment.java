package info.androidhive.slidingmenu;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SearchFragment extends Fragment {
	
	public SearchFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        
        try {
			
        	 Bundle bundle = getArguments();

     		String toSearch = bundle.getString("toSearch");
        	
        	ListView list = (ListView) rootView.findViewById(R.id.list);
        	Search search = new Search (toSearch, this.getActivity().getApplicationContext());
        	SearchAdapter adapter = new SearchAdapter (search.getResults(), this.getActivity().getApplicationContext(), this.getFragmentManager());
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
