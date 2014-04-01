package info.androidhive.slidingmenu;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;






import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HomeFragment extends Fragment {
	
	private JSONArray data2;
	TraktAPI api;
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
         
     

		ListView listView = (ListView) rootView.findViewById(R.id.sample);
		CustomAdapter customAdapter;
		try {

			final Vector<Vector<String>> vector = getList();

			int mean = vector.size() / 2;
			customAdapter = new CustomAdapter(vector.subList(0, mean),
					vector.subList(mean, vector.size()), this.getActivity().getApplicationContext(),this.getFragmentManager());
			listView.setAdapter(customAdapter);

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
        
        
        
        
        
        
        
        
        
        return rootView;
		}
        
        
        
        
        
        
        
        
        
        
        
        
        
       
    
	
	
	public Vector<Vector<String>> getList() throws InterruptedException,
	ExecutionException, JSONException {
api = new TraktAPI(this.getActivity().getApplicationContext());
DataGrabber e = new DataGrabber(this.getActivity().getApplicationContext());
e.execute();
JSONArray array = e.get();
Vector<Vector<String>> list = new Vector<Vector<String>>();

for (int i = 0; i < 30; i++) {
	JSONObject object = array.getJSONObject(i);
	String URL = object.getString("poster");
	URL = URL.replace(".jpg", "-300.jpg");
	String title = object.getString("tvdb_id");
	Vector<String> singola = new Vector<String>();
	singola.add(URL);
	singola.add(title);
	list.add(singola);

}

return list;

}


/*	public static boolean doSomething(){

	if (!isExpanded) {
		isExpanded = true;

		// Expand
		new ExpandAnimation(slidingPanel, panelWidth,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.55f, 0, 0.0f, 0,
				0.0f);
	} else {
		isExpanded = false;

		// Collapse
		new CollapseAnimation(slidingPanel, panelWidth,
				TranslateAnimation.RELATIVE_TO_SELF, 0.55f,
				TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0,
				0.0f, 0, 0.0f);
	}
	return isExpanded;
}

*/		




private class DataGrabber extends AsyncTask<String, Void, JSONArray> {
private ProgressDialog progressdialog;
private Context parent;
private String id;

public DataGrabber(Context parent) {
	this.parent = parent;

}

@Override
protected void onPreExecute() {
	// progressdialog = ProgressDialog.show(parent,"",
	// "Retrieving data ...", true);
}

@Override
protected JSONArray doInBackground(String... params) {
	data2 = api.getDataArrayFromJSON(
			"shows/trending.json/361cd031c2473b06997c87c25ec9c057",
			true);

	// data2 = api.getDataArrayFromJSON("show/season.json/%k/revenge/3",
	// true);

	return data2;

}




}

}

	
	
	
	
	
	
	
