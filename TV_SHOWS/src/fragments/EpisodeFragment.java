package fragments;

import info.androidhive.slidingmenu.R;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import adapters.SeasonAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import engine.Comment;
import engine.MainActivity;
import engine.Season;
import engine.Episode;
import fragments.LoginFragment.MyDialogFragment;

;

public class EpisodeFragment extends Fragment {
	private ShareActionProvider mShareActionProvider;
	String id;
	String season_n;
	String code;
	Context context;
	Episode episode;
	
	
	boolean love; 
	boolean hate; 
	
	
	public EpisodeFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_episode, container,
				false);
		context = this.getActivity().getApplicationContext();


		
		
		setHasOptionsMenu(true);
		;
		Bundle bundle = getArguments();

		id = bundle.getString("id");
		season_n = bundle.getString("season_n");
		code = bundle.getString("code");

		try {
			 episode = new Episode(id, code, season_n, this
					.getActivity().getApplicationContext());
			episode.getComments();
	
			TextView season_n_e = (TextView) rootView
					.findViewById(R.id.season_n);
			TextView id_e = (TextView) rootView.findViewById(R.id.id);
			TextView title = (TextView) rootView.findViewById(R.id.title);
			TextView first_aired_date = (TextView) rootView
					.findViewById(R.id.first_air_date);
			TextView percentage = (TextView) rootView
					.findViewById(R.id.percentage);
			TextView overview = (TextView) rootView.findViewById(R.id.overview);
			overview.setMovementMethod(new ScrollingMovementMethod());
			WebView web = (WebView) rootView.findViewById(R.id.imageEpisode);
			web.loadUrl(episode.image);
			// web.setInitialScale(100);
			web.setFocusable(false);
			web.setClickable(false);
			web.setVisibility(View.VISIBLE);

			season_n_e.setText(episode.season_n);
			id_e.setText(episode.id);
			title.setText(episode.title);
			first_aired_date.setText(episode.first_aired_date);
			percentage.setText(episode.percentage + "%");
			overview.setText(episode.overview);
			
			final ImageView positive = (ImageView) rootView.findViewById(R.id.imageView1);
			final ImageView negative = (ImageView) rootView.findViewById(R.id.imageView3);
			
			if(episode.rating.equals("love")){
			positive.setImageResource(R.drawable.ic_action_good_dark);
			love = true;
			}
		
			else if (episode.rating.equals("hate"))
			{
				hate = true;
				negative.setImageResource(R.drawable.ic_action_bad_dark);}
			
			
			
			positive.setOnClickListener(new OnClickListener(){
				
				
				@Override
				public void onClick(View v) {
					
					
					
					
					
					if (!love && !hate){
						positive.setImageResource(R.drawable.ic_action_good_dark);
						love = true;
						Log.e("", "PRIMO");
						new MyDialogFragment6().show(getFragmentManager(),
								"MyDialog");
					
					
					}
					
					
					
					else if (love && !hate){
						positive.setImageResource(R.drawable.ic_action_good);
						love  = false;
						Log.e("", "SECONDO");
					}
					
					
					
					
					else if (hate && !love){
						positive.setImageResource(R.drawable.ic_action_good_dark);
						negative.setImageResource(R.drawable.ic_action_bad);
						hate = false;
						love = true;
						Log.e("", "TERZO");
						new MyDialogFragment6().show(getFragmentManager(),
								"MyDialog");
					
					}
					
				}
				
				
				
			});
			
			
			negative.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					if (!love && !hate){
						negative.setImageResource(R.drawable.ic_action_bad_dark);
						hate = true;
						new MyDialogFragment7().show(getFragmentManager(),
								"MyDialog");
					}
					
					
					else if (hate && !love){
						negative.setImageResource(R.drawable.ic_action_bad);
						hate = false;
					}
					
					
					
					else if (love && !hate){
						positive.setImageResource(R.drawable.ic_action_good);
						negative.setImageResource(R.drawable.ic_action_bad_dark);
						love = false;
					    hate = true;
					    new MyDialogFragment7().show(getFragmentManager(),
								"MyDialog");
					
					}
					
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

		Button comment = (Button) rootView.findViewById(R.id.sendComment);
		comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(context);

				String user = prefs.getString("user", "");
				String pass = prefs.getString("passed", "");

				if (user.isEmpty()) {

					new MyDialogFragment2().show(getFragmentManager(),
							"MyDialog");
				}

				else {

					new MyDialogFragment3().show(getFragmentManager(),
							"MyDialog");

				}

			}

		});

		Button button = (Button) rootView.findViewById(R.id.viewComments);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Fragment fragment = new CommentsFragment();

				Bundle args = new Bundle();
				args.putString("id", id);
				args.putString("season_n", season_n);
				args.putString("code", code);
				fragment.setArguments(args);

				FragmentManager fragmentManager = getFragmentManager();
				android.app.FragmentTransaction ft = fragmentManager
						.beginTransaction();
				ft.replace(R.id.frame_container, fragment);
				ft.addToBackStack("");
				ft.commit();

			}
		});
		
		
		/*
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("passed", "");
		JSONObject prova = new  JSONObject();
		try {
			prova.accumulate("username", user);
			prova.accumulate("password", pass);
			prova.accumulate("tvdb_id", code);
			prova.accumulate("season", season_n);
			prova.accumulate("episode", id);
			prova.accumulate("comment", "SAU GEIL");
			prova.accumulate("spoiler", "SAU GEIL");
			prova.accumulate("review", "SAU GEIL");
			 
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Comment com = new Comment (prova);
		try {
			com.send();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		*/
		

		return rootView;

	}

	public class MyDialogFragment2 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You must be logged in order to comment!")
					.setPositiveButton("Ok", null).create();
		}

	}
	
	
	
	public class MyDialogFragment4 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Comment inserted with success")
					.setPositiveButton("Ok", null).create();
		}

	}
	
	
	
	
	public class MyDialogFragment5 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You must be logged in order to comment!")
					.setPositiveButton("Ok", null).create();
		}

	}
	
	
	
	
	public class MyDialogFragment6 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You LOVE this episode!")
					.setPositiveButton("Ok", null).create();
		}

	}
	
	
	public class MyDialogFragment7 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You hate this episode!")
					.setPositiveButton("Ok", null).create();
		}

	}
	
	
	
	
	
	
	
	
	

	public class MyDialogFragment3 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);

			View promptView = layoutInflater.inflate(R.layout.prompts, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			alertDialogBuilder.setView(promptView);
			TextView title = (TextView) promptView.findViewById(R.id.title);
			title.setText(episode.title+"\n");			
			
			final EditText edit = (EditText) promptView.findViewById(R.id.userInput);
			
			edit.setSelection(0);
			
			
			Dialog dialog = new AlertDialog.Builder(getActivity())
			.setPositiveButton("Cancel", null)
					.setNegativeButton("Ok", new DialogInterface.OnClickListener(){

				

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							try {
								if (episode.makeAComment(edit.getText().toString())){;
								
								new MyDialogFragment4().show(getFragmentManager(),
										"MyDialog");
								}
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						
						
						
						
						
						
					})
					
					.setView(promptView).create();
			
			
			dialog.getWindow().setLayout(600, 400);
			
			return dialog;

		}

	}

}