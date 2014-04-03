package info.androidhive.slidingmenu;

import android.R.color;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

/**
 * Created with IntelliJ IDEA. User: Shahab Date: 8/22/12 Time: 11:37 AM To
 * change this template use File | Settings | File Templates.
 */
public class EpisodeAdapter extends BaseAdapter {

	private static final String TAG = EpisodeAdapter.class.getSimpleName();

	Vector<Episode> episodes;
	Context context;
	FragmentManager fm;

	public EpisodeAdapter(Vector<Episode> episodes, Context context,
			FragmentManager fm) {
		this.episodes = episodes;
		this.context = context;
		this.fm = fm;

	}

	@Override
	public int getCount() {
		return episodes.size(); // total number of elements in the list
	}

	@Override
	public Object getItem(int i) {
		return episodes.get(i); // single item in the list
	}

	@Override
	public long getItemId(int i) {
		return i; // index number
	}

	@Override
	public View getView(final int index, View view, final ViewGroup parent) {

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.episode_view, parent, false);

		}

		TextView number = (TextView) view.findViewById(R.id.number);
		number.setText("#"+episodes.get(index).id);

		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(episodes.get(index).title);

		TextView air_date = (TextView) view.findViewById(R.id.air_date);
		air_date.setText(episodes.get(index).first_aired);

		return view;

	}

	
	

	
}