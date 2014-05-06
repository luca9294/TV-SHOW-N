package fragments;

import info.androidhive.slidingmenu.R;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import adapters.ProgressAdapter;
import adapters.SearchAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import engine.Season;
import engine.SeenList;
import engine.SeenObject;
import engine.TvShowProgress;
import engine.Tv_Show;

public class TvShowProgressFragment extends Fragment {

	Context context;
	String code = "";
	Handler mHandler = new Handler();
	ProgressBar progressbar = null;

	public TvShowProgressFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_tvshowprogress,
				container, false);

		String mySeenList = "MySeenList";

		this.getActivity().setTitle(mySeenList);
		context = this.getActivity().getApplicationContext();

		Bundle bundle = getArguments();

		code = bundle.getString("toP");

		try {

			final TvShowProgress tvsp = new TvShowProgress(context, code);
			TextView title = (TextView) rootView
					.findViewById(R.id.tvshow_title);
			TextView progress = (TextView) rootView
					.findViewById(R.id.percentage_show);
			title.setText(tvsp.title);

			progress.setText(tvsp.progress + "%");

			progressbar = (ProgressBar) rootView.findViewById(R.id.pbar1);
			progressbar.setMax(100);
			progressbar.setProgress(Integer.parseInt(tvsp.progress));

			new Thread(new Runnable() {
				final TvShowProgress tvsp = new TvShowProgress(context, code);

				public void run() {

					// Update the progress bar using thread to solve update - problems
					mHandler.post(new Runnable() {
						public void run() {
							progressbar.setProgress(Integer
									.parseInt(tvsp.progress));
						}
					});

				}
			}).start();

			if (progress.getText().equals("100%")) {
				Resources res = getResources();
				;
				progressbar.setProgressDrawable(res
						.getDrawable(R.drawable.greenprogress2));

				progress.setTextColor(Color.parseColor("#008000"));
			}

			Tv_Show show = new Tv_Show(code, context);

			final Vector<Season> vector = show.getSeasons();

			if (vector.get(vector.size() - 1).id.equals("0")) {
				vector.remove(vector.size() - 1);
			}

			Vector<SeenObject> vector1 = tvsp.vector;

			ProgressAdapter adapter = new ProgressAdapter(vector, context,
					this.getFragmentManager(), vector1);

			ListView list = (ListView) rootView.findViewById(R.id.list_g);

			list.setAdapter(adapter);

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Fragment fragment = new SeasonProgressFragment();
					FragmentManager fm = getFragmentManager();
					Bundle args = new Bundle();

					args.putString("code", tvsp.code);
					args.putString("id", vector.get(arg2).id);

					fragment.setArguments(args);

					FragmentTransaction ft = fm.beginTransaction();
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
