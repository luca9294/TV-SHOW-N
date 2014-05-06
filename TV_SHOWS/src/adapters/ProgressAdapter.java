package adapters;

import android.R.color;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import engine.Comment;
import engine.Episode;
import engine.Season;
import engine.SeenObject;

/**
 * Created with IntelliJ IDEA. User: Shahab Date: 8/22/12 Time: 11:37 AM To
 * change this template use File | Settings | File Templates.
 */
public class ProgressAdapter extends BaseAdapter {

	private static final String TAG = ProgressAdapter.class.getSimpleName();

	Vector<Season> seasons;
	Vector<SeenObject> objects;
	Context context;
	FragmentManager fm;

	public ProgressAdapter(Vector<Season> seasons, Context context,
			FragmentManager fm, Vector<SeenObject> objects) {
		this.seasons = seasons;
		this.context = context;
		this.fm = fm;
		this.objects = objects;

	}

	@Override
	public int getCount() {
		return seasons.size(); // total number of elements in the list
	}

	@Override
	public Object getItem(int i) {
		return seasons.get(i); // single item in the list
	}

	@Override
	public long getItemId(int i) {
		return i; // index number
	}

	@Override
	public View getView(final int index, View view, final ViewGroup parent) {

		if (view == null) {

			// if (! (seasons.get(index).id.equals("0"))){
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.activity_main_activity2, parent,
					false);// }

		}

		if (!(seasons.get(index).id.equals("0"))) {
			SeenObject object = objects.get(objects.size() - 1 - index);

			if (object.percentage.equals("100")) {
				TextView user = (TextView) view.findViewById(R.id.ses);
				TextView div = (TextView) view.findViewById(R.id.div);

				div.setText(object.completed + "/" + object.completed
						+ " Episodes SEEN");
				div.setTextColor(Color.parseColor("#008000"));

				user.setTextColor(Color.parseColor("#008000"));

				user.setText(seasons.get(index).toString() + "\t SEEN");

			} else {
				TextView user = (TextView) view.findViewById(R.id.ses);
				TextView div = (TextView) view.findViewById(R.id.div);

				div.setText(object.completed + "/" + object.total
						+ " Episodes SEEN");
				div.setTextColor(Color.RED);

				user.setTextColor(Color.BLACK);
				user.setText(seasons.get(index).toString());

			}

		}

		return view;

	}

}