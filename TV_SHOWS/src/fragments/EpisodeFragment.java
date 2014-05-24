package fragments;

import info.androidhive.slidingmenu.R;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import adapters.SeasonAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
import engine.Calendar;
import engine.Comment;
import engine.MainActivity;
import engine.MyDatabase;
import engine.Season;
import engine.Episode;
import fragments.LoginFragment.MyDialogFragment;
import fragments.TvFragment.MyDialogFragment0;
import fragments.TvFragment.MyDialogFragment2;

;

public class EpisodeFragment extends Fragment {
	private ShareActionProvider mShareActionProvider;
	String id;
	String season_n;
	String code;
	String titles;
	Context context;
	Episode episode;
	boolean calendar;
	boolean love;
	boolean hate;

	public boolean seenBool = false;
	public boolean watchBool = false;

	public EpisodeFragment() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// load fragment and all its informations

		View rootView = inflater.inflate(R.layout.fragment_episode, container,
				false);
		setHasOptionsMenu(true);
		context = this.getActivity().getApplicationContext();
		Bundle bundle = getArguments();
		id = bundle.getString("id");
		season_n = bundle.getString("season_n");
		code = bundle.getString("code");

		try {
			episode = new Episode(id, code, season_n, this.getActivity()
					.getApplicationContext(), this.getActivity());
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
			titles = episode.title;
			season_n_e.setText(episode.season_n);
			id_e.setText(episode.id);
			title.setText(episode.title);
			first_aired_date.setText(episode.first_aired_date);
			percentage.setText(episode.percentage + "%");
			overview.setText(episode.overview);

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this.getActivity()
							.getApplicationContext());

			final String user = prefs.getString("user", "");
			String pass = prefs.getString("pass", "");

			final ImageView positive = (ImageView) rootView
					.findViewById(R.id.imageView1);
			final ImageView negative = (ImageView) rootView
					.findViewById(R.id.imageView3);

			// displays like and dislike buttons in different colors
			if (user != "") {
				if (episode.rating.equals("love")) {
					positive.setImageResource(R.drawable.ic_action_good_dark);
					love = true;

				}

				else if (episode.rating.equals("hate")) {
					hate = true;
					negative.setImageResource(R.drawable.ic_action_bad_dark);

				}
			}

			// displays seen button in different colors
			Button seen = (Button) rootView.findViewById(R.id.seenList);
			if (episode.watched == true) {
				seen.setText("SEEN");
				seen.setTextColor(Color.GREEN);
				seenBool = true;
			} else {
				seenBool = false;
			}

			// displays watching button in different colors
			Button watching = (Button) rootView.findViewById(R.id.watchingList);
			if (episode.wish == true) {
				watching.setText("IN\nWATCHED\nLIST");
				watching.setTextColor(Color.GREEN);
				watchBool = true;
			} else {
				watchBool = false;
			}

			// handles action when the "like" button is pressed
			positive.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (user != "") {

						if (!love && !hate) {
							positive.setImageResource(R.drawable.ic_action_good_dark);
							love = true;
							Log.e("", "PRIMO");
							new MyDialogFragment6().show(getFragmentManager(),
									"MyDialog");
							try {
								episode.makeARate("love");
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

						else if (love && !hate) {
							positive.setImageResource(R.drawable.ic_action_good);
							love = false;
							Log.e("", "SECONDO");
							try {
								episode.makeARate("unrate");
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

						else if (hate && !love) {
							positive.setImageResource(R.drawable.ic_action_good_dark);
							negative.setImageResource(R.drawable.ic_action_bad);
							hate = false;
							love = true;
							Log.e("", "TERZO");
							new MyDialogFragment6().show(getFragmentManager(),
									"MyDialog");

							try {
								episode.makeARate("love");
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

					}

					else {
						new MyDialogFragment8().show(getFragmentManager(),
								"MyDialog");

					}

				}
			});

			// handles action when the "dislike" button is pressed
			negative.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (user != "") {
						if (!love && !hate) {
							negative.setImageResource(R.drawable.ic_action_bad_dark);
							hate = true;
							new MyDialogFragment7().show(getFragmentManager(),
									"MyDialog");

							try {
								episode.makeARate("hate");
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

						else if (hate && !love) {
							negative.setImageResource(R.drawable.ic_action_bad);
							hate = false;

							try {
								episode.makeARate("unrate");
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

						else if (love && !hate) {
							positive.setImageResource(R.drawable.ic_action_good);
							negative.setImageResource(R.drawable.ic_action_bad_dark);
							love = false;
							hate = true;
							new MyDialogFragment7().show(getFragmentManager(),
									"MyDialog");
							try {
								episode.makeARate("hate");
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

					}

					else {

						new MyDialogFragment8().show(getFragmentManager(),
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

		// handles action when the "send comment" button is pressed
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

		// handles action when the "view comments" button is pressed
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

		// handles action when the "Add to/remove from seen list" button is
		// pressed
		final Button seen = (Button) rootView.findViewById(R.id.seenList);
		seen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(context);

				String user = prefs.getString("user", "");
				String pass = prefs.getString("passed", "");

				if (user.isEmpty()) {
					if (seenBool == false) {
					MyDatabase mdb = new	MyDatabase( context, new Activity() );
					mdb.insertTvEpisodes(episode.title, Integer.valueOf(episode.code), Integer.valueOf(episode.id),Integer.valueOf(episode.season_n ));
					new MyDialogFragment10().show(getFragmentManager(),
							"MyDialog");
					seen.setText("SEEN");
					seen.setTextColor(Color.GREEN);
					seenBool = true;
		
					}	
					else{
						
						MyDatabase mdb = new	MyDatabase( context, new Activity() );
						mdb.deleteEpisode( Integer.valueOf(episode.season_n), Integer.valueOf(episode.id),Integer.valueOf(episode.code ));
						new MyDialogFragment12().show(getFragmentManager(),
								"MyDialog");

						seen.setText("ADD\nSEEN\nLIST");
						seen.setTextColor(Color.WHITE);
						seenBool = false;
						
					}
					
					
				 
				}

				else {

					if (seenBool == false) {
						try {
							episode.addToSeen(seenBool, null);

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
						new MyDialogFragment10().show(getFragmentManager(),
								"MyDialog");

						seen.setText("SEEN");
						seen.setTextColor(Color.GREEN);
						seenBool = true;
					} else {
						try {
							episode.addToSeen(seenBool, null);

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
						new MyDialogFragment12().show(getFragmentManager(),
								"MyDialog");

						seen.setText("ADD\nSEEN\nLIST");
						seen.setTextColor(Color.WHITE);
						seenBool = false;
					}
				}

			}

		});

		// handles action when the "Add to/remove from watching list" button is
		// pressed
		final Button watching = (Button) rootView
				.findViewById(R.id.watchingList);
		watching.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(context);

				String user = prefs.getString("user", "");
				String pass = prefs.getString("passed", "");

				if (user.isEmpty()) {

					new MyDialogFragment9().show(getFragmentManager(),
							"MyDialog");
				}

				else {

					if (watchBool == false) {
						try {
							episode.addToWatching(watchBool, null);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						new MyDialogFragment11().show(getFragmentManager(),
								"MyDialog");

						watching.setText("IN\nWATCHING\nLIST");
						watching.setTextColor(Color.GREEN);
						watchBool = true;
					} else {
						try {
							episode.addToWatching(watchBool, null);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						new MyDialogFragment12().show(getFragmentManager(),
								"MyDialog");

						watching.setText("ADD\nWATCHING\nLIST");
						watching.setTextColor(Color.WHITE);
						watchBool = false;
					}

				}

			}

		});

		/*
		 * SharedPreferences prefs = PreferenceManager
		 * .getDefaultSharedPreferences(context);
		 * 
		 * String user = prefs.getString("user", ""); String pass =
		 * prefs.getString("passed", ""); JSONObject prova = new JSONObject();
		 * try { prova.accumulate("username", user);
		 * prova.accumulate("password", pass); prova.accumulate("tvdb_id",
		 * code); prova.accumulate("season", season_n);
		 * prova.accumulate("episode", id); prova.accumulate("comment",
		 * "SAU GEIL"); prova.accumulate("spoiler", "SAU GEIL");
		 * prova.accumulate("review", "SAU GEIL");
		 * 
		 * 
		 * } catch (JSONException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 * 
		 * Comment com = new Comment (prova); try { com.send(); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (ExecutionException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		return rootView;

	}

	// loads option menu
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main_activity3, menu);

	}

	// checks if episode has already been added to the calendar
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// menu.removeItem(R.id.action_like)
		Calendar c = new Calendar(context, this.getActivity());
		if (c.isInCalendar(episode)) {
			calendar = true;
			menu.findItem(R.id.selection).setTitle("Remove from the calendar");

		} else {
			calendar = false;
			menu.findItem(R.id.selection).setTitle("Add to the calendar");

		}

	}

	// handles selection of an option item
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("passed", "");

		// Handles item selection
		switch (item.getItemId()) {

		// if add to/remove from calendar buttons are pressed 
		case R.id.selection:
			Calendar c = new Calendar(context, this.getActivity());
			try {
				if (episode.isFuture()) {

					if (!calendar) {
						try {
							c.addToCalendar(episode);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						new MyDialogFragment1().show(getFragmentManager(),
								"MyDialog");

					}

					else {
						c.removeFromCalendar(episode);
						new MyDialogFragment0().show(getFragmentManager(),
								"MyDialog");

					}

				} else {

					new MyDialogFragment111().show(getFragmentManager(),
							"MyDialog");

				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return true;
	}

	// dialog fragments
	public class MyDialogFragment0 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("The episode has been removed from the Calendar")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment111 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("This episode has already been broadcast! ")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment1 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("The episode has been added to the Calendar")
					.setPositiveButton("Ok", null).create();
		}

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

			.setMessage("You LOVE this episode!").setPositiveButton("Ok", null)
					.create();
		}

	}

	public class MyDialogFragment7 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You hate this episode!").setPositiveButton("Ok", null)
					.create();
		}

	}

	public class MyDialogFragment8 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("You must be logged in order to rate!")
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
			title.setText(episode.title + "\n");

			final EditText edit = (EditText) promptView
					.findViewById(R.id.userInput);

			edit.setSelection(0);

			Dialog dialog = new AlertDialog.Builder(getActivity())
					.setPositiveButton("Cancel", null)
					.setNegativeButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									try {
										if (episode.makeAComment(edit.getText()
												.toString())) {
											;

											new MyDialogFragment4().show(
													getFragmentManager(),
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

	public class MyDialogFragment9 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

					.setMessage(
							"You must be logged in order to add any episode to any list!")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment10 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Episode added to the \"Seen List\"!")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment11 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Episode added to the \"Watchlist\"!")
					.setPositiveButton("Ok", null).create();
		}

	}

	public class MyDialogFragment12 extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())

			.setMessage("Episode removed from the list!")
					.setPositiveButton("Ok", null).create();
		}

	}
}