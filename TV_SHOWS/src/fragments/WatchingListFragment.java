package fragments;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.R.id;
import info.androidhive.slidingmenu.R.layout;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Search;
import engine.SeenList;
import engine.WatchingList;
import adapters.SearchAdapter;
import adapters.WatchingAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class WatchingListFragment extends Fragment {

	public WatchingListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);

		String mySeenList = "MyWatchingList";

		this.getActivity().setTitle(mySeenList);

		ListView list = (ListView) rootView.findViewById(R.id.list);
		final WatchingList watchingList;

		try {
			watchingList = new WatchingList(this.getActivity().getApplicationContext());
			
			
			WatchingAdapter adapter = new WatchingAdapter(watchingList.tvshows,
					this.getActivity().getApplicationContext(),
					this.getFragmentManager());
			list.setAdapter(adapter);

		/*	list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Fragment fragment = new TvShowProgressFragment();
					FragmentManager fm = getFragmentManager();
					Bundle args = new Bundle();

					args.putString("toP", seenList.getResults().get(arg2).id);

					fragment.setArguments(args);

					FragmentTransaction ft = fm.beginTransaction();
					ft.replace(R.id.frame_container, fragment);
					ft.addToBackStack("");
					ft.commit();

				}
			});*/

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return rootView;

	}
}