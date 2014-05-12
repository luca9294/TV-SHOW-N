package fragments;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Comment;
import engine.Episode;
import engine.Search;
import info.androidhive.slidingmenu.R;
import adapters.CommentAdapter;
import adapters.SearchAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CommentsFragment extends Fragment {

	public CommentsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_comments, container,
				false);
		Bundle bundle = getArguments();

		String id = bundle.getString("id");
		String season_n = bundle.getString("season_n");
		String code = bundle.getString("code");

		Episode episode;
		try {
			episode = new Episode(id, code, season_n, this.getActivity()
					.getApplicationContext(),this.getActivity());
			episode.getEpisode();
			episode.getComments();
			Log.e("", episode.comments.toString());

			ListView list = (ListView) rootView.findViewById(R.id.list);

			TextView title = (TextView) rootView.findViewById(R.id.car);
			title.setText("Episode " + id + " Season " + season_n);

			TextView title_ = (TextView) rootView.findViewById(R.id.title);
			title_.setText(episode.title);

			CommentAdapter adapter = new CommentAdapter(episode.comments, this
					.getActivity().getApplicationContext(),
					this.getFragmentManager());
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
