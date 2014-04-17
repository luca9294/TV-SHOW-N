package fragments;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.R.id;
import info.androidhive.slidingmenu.R.layout;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Search;

import adapters.SearchAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SearchFragment extends Fragment {

	public SearchFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);

		try {

			Bundle bundle = getArguments();

			String toSearch = bundle.getString("toSearch");

			this.getActivity().setTitle(toSearch);

			ListView list = (ListView) rootView.findViewById(R.id.list);
			final Search search = new Search(toSearch, this.getActivity()
					.getApplicationContext());
			SearchAdapter adapter = new SearchAdapter(search.getResults(), this
					.getActivity().getApplicationContext(),
					this.getFragmentManager());
			list.setAdapter(adapter);

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Fragment fragment = new TvFragment();
					FragmentManager fm = getFragmentManager();
					Bundle args = new Bundle();
					args.putString("toSearch", search.getResults().get(arg2).id);
					fragment.setArguments(args);

					android.app.FragmentTransaction ft = fm.beginTransaction();
					ft.replace(R.id.frame_container, fragment);
					ft.addToBackStack("");
					ft.commit();

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

		return rootView;
	}
}
