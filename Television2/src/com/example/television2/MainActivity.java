package com.example.television2;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnTouchListener {
	TraktAPI api;
	private JSONObject data, object;
	private JSONArray data2;
	Button menu1, menu4, menu5, menu6;
	Button menu2, menu3, title;
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	static LinearLayout slidingPanel;
	static boolean isExpanded;
	private DisplayMetrics metrics;
	private RelativeLayout headerPanel;
	private RelativeLayout menuPanel;
	static int panelWidth;
	private ImageView menuViewButton, menuViewButton1;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	private SimpleGestureFilter detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bar();

		setTitle("SUGGESTIONS");

		ListView listView = (ListView) findViewById(R.id.sample);
		CustomAdapter customAdapter;
		try {

			final Vector<Vector<String>> vector = getList();

			int mean = vector.size() / 2;
			customAdapter = new CustomAdapter(vector.subList(0, mean),
					vector.subList(mean, vector.size()), this);
			listView.setAdapter(customAdapter);

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector();
		LinearLayout lowestLayout = (LinearLayout) this
				.findViewById(R.id.slidingPanel);
		lowestLayout.setOnTouchListener(activitySwipeDetector);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar:
			item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
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
			});
		}
		return true;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return gestureDetector.onTouchEvent(arg1);
	}

	public Vector<Vector<String>> getList() throws InterruptedException,
			ExecutionException, JSONException {
		api = new TraktAPI(this.getApplicationContext());
		DataGrabber e = new DataGrabber(this);
		e.execute();
		JSONArray array = e.get();
		Vector<Vector<String>> list = new Vector<Vector<String>>();

		for (int i = 0; i < 50; i++) {
			JSONObject object = array.getJSONObject(i);
			String URL = object.getString("poster");
			URL = URL.replace(".jpg", "-300.jpg");
			String title = object.getString("title");
			Vector<String> singola = new Vector<String>();
			singola.add(URL);
			singola.add(title);
			list.add(singola);

		}

		return list;

	}
	
	
	public static boolean doSomething(){

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

		
		
		
	

	public void bar() {
		isExpanded = false;
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		panelWidth = (int) ((metrics.widthPixels) * 0.55);

		menuPanel = (RelativeLayout) findViewById(R.id.menuPanel);
		menuPanelParameters = (FrameLayout.LayoutParams) menuPanel
				.getLayoutParams();
		menuPanelParameters.width = panelWidth;
		menuPanel.setLayoutParams(menuPanelParameters);

		slidingPanel = (LinearLayout) findViewById(R.id.slidingPanel);
		slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel
				.getLayoutParams();
		slidingPanelParameters.width = metrics.widthPixels;
		slidingPanel.setLayoutParams(slidingPanelParameters);

		menu1 = (Button) findViewById(R.id.menu_item_1);
		menu2 = (Button) findViewById(R.id.menu_item_2);
		menu3 = (Button) findViewById(R.id.menu_item_3);
		menu4 = (Button) findViewById(R.id.menu_item_4);
		menu5 = (Button) findViewById(R.id.menu_item_5);
		menu6 = (Button) findViewById(R.id.menu_item_6);

	}

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
