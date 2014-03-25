package com.example.television2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		final EditText tvshow = (EditText) findViewById(R.id.tvshow);
		final Button button = (Button) findViewById(R.id.button1);
		final Context context = this;
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String toSearch = tvshow.getText().toString();
				toSearch = toSearch.replace(".", "");

				new MyTask(SearchActivity.this).execute();

				Intent intent = new Intent(getApplicationContext(),
						TVShowActivity.class);
				intent.putExtra("toSearch", toSearch);
				startActivity(intent);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	public class MyTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog progress;

		public MyTask(SearchActivity activity) {
			progress = new ProgressDialog(activity);
		}

		public void onPreExecute() {
			progress.setMessage("Searching....");
			progress.show();
		}

		public Void doInBackground(Void... unused) {

			return null;

		}

		public void onPostExecute(Void unused) {
			progress.dismiss();
		}
	}

}
