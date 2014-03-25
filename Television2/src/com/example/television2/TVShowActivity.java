package com.example.television2;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class TVShowActivity extends Activity {
	Tv_Show prova;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tvshow);
		Bundle bundle = getIntent().getExtras();

		String toSearch = bundle.getString("toSearch");

		// TextView title =(TextView)findViewById(R.id.title);
		TextView premiere = (TextView) findViewById(R.id.premiere);
		TextView country = (TextView) findViewById(R.id.country);
		TextView percentage = (TextView) findViewById(R.id.percentage);
		TextView runtime = (TextView) findViewById(R.id.runtime);
		TextView genre = (TextView) findViewById(R.id.genre);
		TextView overview = (TextView) findViewById(R.id.overview);
		WebView image = (WebView) findViewById(R.id.image);

		try {

			prova = new Tv_Show(toSearch, this);

			setTitle(prova.title_n);
			premiere.setText(prova.first_aired_iso);
			country.setText(prova.country);
			runtime.setText(prova.runtime + "m");
			genre.setText(prova.genre);
			percentage.setText(prova.percentage + "%");
			overview.setText(prova.overview);
			overview.setMovementMethod(new ScrollingMovementMethod());
			image.loadUrl((prova.image).replace(".jpg", "-300.jpg"));
			image.setInitialScale(157);
			image.setFocusable(false);
			image.setClickable(false);

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tvshow, menu);
		return true;
	}

}
