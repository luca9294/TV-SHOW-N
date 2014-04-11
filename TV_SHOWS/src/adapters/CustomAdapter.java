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
import android.widget.Toast;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import fragments.MainActivity;
import fragments.TvFragment;

/**
 * Created with IntelliJ IDEA. User: Shahab Date: 8/22/12 Time: 11:37 AM To
 * change this template use File | Settings | File Templates.
 */
public class CustomAdapter extends BaseAdapter {

	private static final String TAG = CustomAdapter.class.getSimpleName();

	List<Vector<String>> urls1;
	List<Vector<String>> urls2;
	Context context;
	FragmentManager fm;

	public CustomAdapter(List<Vector<String>> list, List<Vector<String>> list2,
			Context context, FragmentManager fm) {
		this.urls1 = list;
		this.urls2 = list2;
		this.context = context;
		this.fm = fm;

	}

	@Override
	public int getCount() {
		return urls1.size(); // total number of elements in the list
	}

	@Override
	public Object getItem(int i) {
		return urls1.get(i); // single item in the list
	}

	@Override
	public long getItemId(int i) {
		return i; // index number
	}

	@Override
	public View getView(final int index, View view, final ViewGroup parent) {

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.single_item, parent, false);

		}

		// {summary, keywords, status,
		// resolution,type,version,milestone,reporter};

		WebView id = (WebView) view.findViewById(R.id.web);
		id.loadUrl(urls1.get(index).get(0));
		id.setInitialScale(160);
		id.setFocusable(false);
		id.setClickable(false);
		id.setVisibility(View.VISIBLE);
		id.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();

				switch (action) {
				case MotionEvent.ACTION_CANCEL:
					return true;
				case MotionEvent.ACTION_UP:

					Fragment fragment = new TvFragment();
					Bundle args = new Bundle();
					args.putString("toSearch", urls1.get(index).get(1));
					fragment.setArguments(args); 
					android.app.FragmentTransaction ft = fm.beginTransaction();
					 ft.replace(R.id.frame_container, fragment);
					 ft.addToBackStack("");
					 ft.commit();

					return true;

				}

				return false;
			}
		});

		// (new OnClickListener(){

		WebView id1 = (WebView) view.findViewById(R.id.web1);
		id1.loadUrl(urls2.get(index).get(0));
		id1.setInitialScale(160);
		id1.setFocusable(false);
		id1.setClickable(false);
		id1.setVisibility(View.VISIBLE);
		id1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();

				switch (action) {
				case MotionEvent.ACTION_CANCEL:
					return true;
				case MotionEvent.ACTION_UP:

					/*
					 * Intent intent = new Intent(arg0.getContext(),
					 * HomeFragment.class); intent.putExtra("toSearch",
					 * urls2.get(index).get(1));
					 * arg0.getContext().startActivity(intent);
					 */
					Fragment fragment = new TvFragment();
					Bundle args = new Bundle();
					args.putString("toSearch", urls2.get(index).get(1));
					fragment.setArguments(args);
				
				
					
					
					
			
					android.app.FragmentTransaction ft = fm.beginTransaction();
					 ft.replace(R.id.frame_container, fragment);
					 ft.addToBackStack("");
					 ft.commit();
					
					
					
					return true;

				}

				return false;
			}
		});

		return view;

	}

}