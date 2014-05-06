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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.slidingmenu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Episode;

/**
 * Created with IntelliJ IDEA. User: Shahab Date: 8/22/12 Time: 11:37 AM To
 * change this template use File | Settings | File Templates.
 */
public class SeasonAdapter extends BaseAdapter {

	private static final String TAG = SeasonAdapter.class.getSimpleName();

	Vector<Episode> episodes;
	Context context;
	FragmentManager fm;
	boolean r;

	public SeasonAdapter(boolean r, Vector<Episode> episodes, Context context,
			FragmentManager fm) {
		this.episodes = episodes;
		this.context = context;
		this.fm = fm;
		this.r = r;

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
		ViewHolder holder;
		// View rowView = view;
		if (view == null) {
			// holder = new ViewHolder();
			// ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());

			view = inflater.inflate(R.layout.episode_view, parent, false);
			holder = new ViewHolder();

			holder.numbern = (TextView) view.findViewById(R.id.number);
			holder.titlen = (TextView) view.findViewById(R.id.title);
			holder.airn = (TextView) view.findViewById(R.id.air_date);
			holder.seen = (TextView) view.findViewById(R.id.seenView);
			holder.box = (CheckBox) view.findViewById(R.id.checkBox1);
			view.setTag(holder);
			Log.e("", "SONO QUA");

		}

		else

			holder = (ViewHolder) view.getTag();

		holder.numbern.setText("#" + episodes.get(index).id);
		holder.titlen.setText(episodes.get(index).title);
		holder.airn.setText(episodes.get(index).first_aired_date);

		String first = episodes.get(index).first_aired_date;

		Date result;

		Date now = new Date();
		String response = "";
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String strCurrDate = sdfDate.format(now);

		Date currentDate;
		try {

			result = sdfDate.parse(first);

			currentDate = sdfDate.parse(strCurrDate);

			if (currentDate.after(result)) {
				holder.airn.setTextColor(Color.parseColor("#B22222"));
				// holder.airn.setBackgroundColor(Color.WHITE);

			}

			else {
				holder.airn.setTextColor(Color.parseColor("#4DBD33"));
				// holder.airn.setBackgroundColor(Color.WHITE);

			}

			if (episodes.get(index).watched && currentDate.after(result)) {
				holder.seen.setText("SEEN");
				holder.seen.setTextColor(Color.GREEN);

			}

			else {
				holder.seen.setText("");
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (r) {
			holder.box.setVisibility(View.VISIBLE);

		}

		return view;

	}

	public class ViewHolder {
		public TextView numbern, titlen, airn, seen;
		public CheckBox box;

	}
}