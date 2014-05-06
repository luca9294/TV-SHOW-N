package adapters;

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

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Search_result;
import engine.Tv_Show;

/**
 * Created with IntelliJ IDEA. User: Shahab Date: 8/22/12 Time: 11:37 AM To
 * change this template use File | Settings | File Templates.
 */
public class WatchingAdapter extends BaseAdapter {

	private static final String TAG = CustomAdapter.class.getSimpleName();

	Vector<Tv_Show> vector;
	Context context;
	FragmentManager fm;

	public WatchingAdapter(Vector<Tv_Show> vector, Context context,
			FragmentManager fm) {
		this.vector = vector;
		this.context = context;
		this.fm = fm;

	}

	@Override
	public int getCount() {
		return vector.size(); // total number of elements in the list
	}

	@Override
	public Object getItem(int i) {
		return vector.get(i); // single item in the list
	}

	@Override
	public long getItemId(int i) {
		return i; // index number
	}

	@Override
	public View getView(final int index, View view, final ViewGroup parent) {

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.result_item2, parent, false);

		}

		// {summary, keywords, status,
		// resolution,type,version,milestone,reporter};

		WebView id = (WebView) view.findViewById(R.id.web);
		id.loadUrl((vector.get(index).image).replace(".jpg", "-300.jpg"));
		id.setInitialScale(108);
		id.setFocusable(false);
		id.setClickable(false);

		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(vector.get(index).title);

		TextView year = (TextView) view.findViewById(R.id.year);
		year.setText(vector.get(index).year);

		TextView country = (TextView) view.findViewById(R.id.nation);

		String str = vector.get(index).country;
		if (str.equals("null")) {
			str = "";

		}
		country.setText(str);

		TextView genres = (TextView) view.findViewById(R.id.genres);
		genres.setText(vector.get(index).genre);

		return view;

	}

}