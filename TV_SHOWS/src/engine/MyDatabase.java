package engine;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "TvShows";
	private static final int DATABASE_VERSION = 1;
	Context context;
	Activity a;

	public MyDatabase(Context context, Activity a) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		this.a = a;

	}

	
	//method that returns a vector with all the IDs (codes) of the tv shows in the watchlist
	public Vector<String> getTvShows2() {
		Vector<String> results = new Vector<String>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables("WatchTvShow");
		// qb.setTables(sqlTables);

		Cursor c;

		c = db.query("WatchTvShow", null, null, null, null, null, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {
			String code = String.valueOf(c.getInt(0));

			results.add(code);
			c.moveToNext();

		}

		return results;

	}

	//method that controls if a season is seen or not
	public boolean checkSeen(Season s) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();

		// qb.setTables(sqlTables);

		Cursor c = db.rawQuery(
				"select idTvShow from SeenEpisodes where idTvShow=" + s.code
						+ " AND season_n = " + s.id + ";", null);

		c.moveToFirst();

		if (c.getCount() == s.count) { //count = broadcasted episodes

			result = true;

		}

		return result;

	}

	//method that controls if a season is in the watchlist or not
	public boolean checkWatch(Season s) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();

		// qb.setTables(sqlTables);

		Cursor c = db.rawQuery(
				"select idTvShow from WatchEpisodes where idTvShow=" + s.code
						+ " AND season_n = " + s.id + ";", null);

		c.moveToFirst();

		if (c.getCount() == Integer.valueOf(s.n_episode)) { //n_episode = number of episodes, broadcasted or not

			result = true;

		}

		return result;

	}
	
	
	//returns a vector of seen Tv_Shows
	public Vector<Tv_Show> getTvShows() throws InterruptedException,
			ExecutionException, JSONException, ParseException {
		Vector<Tv_Show> results = new Vector<Tv_Show>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables("SeenTvShow");
		// qb.setTables(sqlTables);

		Cursor c;

		c = db.query("SeenTvShow", null, null, null, null, null, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {
			String code = String.valueOf(c.getInt(0));

			Tv_Show tvs = new Tv_Show(code, context, a);
			results.add(tvs);
			c.moveToNext();

		}

		return results;

	}

	
	//prints logs of all the seen tvshows
	public void print() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables("SeenTvShow");
		// qb.setTables(sqlTables);

		Cursor c;

		c = db.query("SeenTvShow", null, null, null, null, null, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {
			String code = String.valueOf(c.getInt(0));
			Log.e("test", code);

			c.moveToNext();

		}

	}

	//prints all the episodes in the watchlist
	public void printAll() {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c;

		c = db.query("WatchEpisodes", null, null, null, null, null, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {
			String code = String.valueOf(c.getInt(3));
			Log.e("code", code);

			c.moveToNext();

		}

	}

	//checks if a tv show is already in the seen list
	public boolean containsTvShow(int id) {
		boolean result = false;
		String[] sqlSelect = { "id" };
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("SeenTvShow");
		// qb.setTables(sqlTables);

		Cursor c;

		c = db.query("SeenTvShow", null, null, null, null, null, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {
			int re = c.getInt(0);
			if (re == id) {
				result = true;
				break;
			}

			c.moveToNext();
		}
		return result;
	}

	
	//checks if a tv show is already in the watchlist
	public boolean containsTvShow2(int id) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("WatchTvShow");
		// qb.setTables(sqlTables);

		Cursor c;

		c = db.query("WatchTvShow", null, null, null, null, null, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {
			int re = c.getInt(0);
			if (re == id) {
				result = true;
				break;
			}

			c.moveToNext();
		}
		return result;
	}

	//checks if an episode is already in the seen list
	public boolean containsTvShow(int idTvShow, int id, int season_n) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();

		// qb.setTables(sqlTables);

		Cursor c = db.rawQuery(
				"select idTvShow from SeenEpisodes where idTvShow=" + idTvShow
						+ " AND season_n = " + season_n + " AND id = " + id
						+ ";", null);

		c.moveToFirst();
		if (c.getCount() > 0) {

			if (c.getInt(0) == idTvShow) {
				result = true;

			}
		}

		return result;
	}

	//checks if an episode is already in the watchlist
	public boolean containsTvShow2(int idTvShow, int id, int season_n) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();

		// qb.setTables(sqlTables);

		Cursor c = db.rawQuery(
				"select idTvShow from WatchEpisodes where idTvShow=" + idTvShow
						+ " AND season_n = " + season_n + " AND id = " + id
						+ ";", null);

		c.moveToFirst();
		if (c.getCount() > 0) {

			if (c.getInt(0) == idTvShow) {
				result = true;

			}
		}

		return result;
	}
	
	
	//checks if at least one episode of the the tv show with ID id is in the watchlist. (note: difference episode/episodes in a season)
	public boolean isInEpisode(int id) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();

		// qb.setTables(sqlTables);

		Cursor c = db
				.rawQuery("select idTvShow from WatchEpisodes where idTvShow="
						+ id + ";", null);

		c.moveToFirst();
		if (c.getCount() > 0) {

			if (c.getInt(0) == id) {
				result = true;

			}
		}

		return result;
	}

	//checks if there is at least one episode of the tv show idTvShow in the SeenEpisodes DB
	public boolean containsOneEpisode(int idTvShow) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();

		// qb.setTables(sqlTables);

		Cursor c = db.rawQuery(
				"select idTvShow from SeenEpisodes where idTvShow=" + idTvShow
						+ ";", null);

		c.moveToFirst();
		if (c.getCount() > 0) {

			if (c.getInt(0) == idTvShow) {
				result = true;

			}
		}

		return result;
	}

	
	public boolean containsOneEpisode2(int idTvShow) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();

		// qb.setTables(sqlTables);

		Cursor c = db.rawQuery(
				"select idTvShow from WatchEpisodes where idTvShow ="
						+ idTvShow + ";", null);

		c.moveToFirst();
		if (c.getCount() > 0) {

			if (c.getInt(0) == idTvShow) {
				result = true;

			}
		}

		return result;
	}

	
	//inserts the code of a tv show in the SeenTvShow DB, if the code was not inserted, and then inserts episodes in SeenEpisodes DB
	public void insertTvEpisodes(String title, int idTvShow, int id,
			int season_n) {
		if (!containsTvShow(idTvShow)) {
			this.insertTvShows(idTvShow);

		}
		String query = "INSERT INTO SeenEpisodes VALUES ('" + title + "', "
				+ season_n + ", " + id + ", " + idTvShow + ");";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}
	

	//inserts the code of a tv show in the WatchTvShow DB, if the code was not inserted, and then inserts episodes in WatchEpisodes DB
	public void insertTvEpisodes2(String title, int idTvShow, int id,
			int season_n) {
		if (!containsTvShow2(idTvShow)) {
			this.insertTvShows2(idTvShow);
		}
		title = title.replace("'", "''");
		String query = "INSERT INTO WatchEpisodes VALUES ('" + title + "', "
				+ season_n + ", " + id + ", " + idTvShow + ");";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

	//inserts just the tv show in the SeenTvShow DB
	public void insertTvShows(int i) {
		String query = "INSERT INTO  SeenTvShow VALUES (" + i + ");";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

	//inserts just the tv show in the WatchTvShow DB
	public void insertTvShows2(int i) {
		String query = "INSERT INTO  WatchTvShow VALUES (" + i + ");";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

	//deletes the tv show i from the WatchTvShow DB
	public void deleteTvShows2(int i) {
		String query = "DELETE FROM  WatchTvShow WHERE id= " + i + ";";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

	//deletes all tv shows from thw SeenTvShow DB
	public void deleteAll() {
		String query = "DELETE FROM  SeenTvShow;";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

	/**/
	public void insertEpisodes(String title, int season_n, int id, int idTvShow) {
		title = title.replace("'", "''");
		String query = "INSERT INTO  SeenEpisodes VALUES ('" + title + "', "
				+ season_n + ", " + id + ", " + idTvShow + ");";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);
		if (!containsTvShow(idTvShow)) {
			this.insertTvShows(idTvShow);

		}

	}

	/**/
	public void insertEpisodes2(String title, int season_n, int id, int idTvShow) {
		title = title.replace("'", "''");
		String query = "INSERT INTO  WatchEpisodes VALUES ('" + title + "', "
				+ season_n + ", " + id + ", " + idTvShow + ");";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

	//deletes both tv show AND all its episodes from the SeenTvShow DB 
	public void deleteTvShow(int i) {
		String query = "DELETE FROM  SeenTvShow WHERE id = " + i + ";";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

		String query1 = "DELETE FROM  SeenEpisodes WHERE idTvShow = " + i + ";";
		SQLiteDatabase db1 = getWritableDatabase();
		db1.execSQL(query1);

	}

	//deletes a single episode from the SeenEpisode DB
	public void deleteEpisode(int season_n, int id, int idTvShow) {
		String query = "DELETE FROM  SeenEpisodes WHERE idTvShow = " + idTvShow
				+ " AND season_n =" + season_n + " AND id = " + id + ";";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

		if (!this.containsOneEpisode(idTvShow)) {
			this.deleteTvShow(idTvShow);

		}

	}

	//deletes one episode from the watchlist --> if it's the only episode, it also deletes the tv show from the WatchTvShow DB
	public void deleteEpisode2(int season_n, int id, int idTvShow) {
		String query = "DELETE FROM  WatchEpisodes WHERE idTvShow = "
				+ idTvShow + " AND season_n =" + season_n + " AND id = " + id
				+ ";";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

		if (!this.containsOneEpisode2(idTvShow)) {
			this.deleteTvShows2(idTvShow);

		}

	}

	//counts how many seen episodes there are in a tv show
	public int getCount(int i) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCount = db.rawQuery(
				"select count(*) from SeenEpisodes where idTvShow=" + i + ";",
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();

		return count;
	}

	//counts how many seen episodes there are in a season
	public int getCountPerSeason(int idTvShow, int idSeason) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCount = db.rawQuery(
				"select count(*) from SeenEpisodes where idTvShow=" + idTvShow
						+ " AND season_n = " + idSeason + ";", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();

		return count;
	}

	
	//synchronizes the local SeenEpisodes DB with the server
	public void sync() throws JSONException, InterruptedException,
			ExecutionException {
		Cursor c;
		SQLiteDatabase db = getReadableDatabase();
		c = db.query("SeenTvShow", null, null, null, null, null, null);

		c.moveToFirst();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");

		while (!c.isAfterLast()) {
			String code = String.valueOf(c.getInt(0));
			Cursor c2 = db.rawQuery(
					"SELECT * from SeenEpisodes where idTvShow=" + code + ";",
					null);
			JSONObject ob = new JSONObject();
			ob.put("username", user);
			ob.put("password", pass);
			ob.put("tvdb_id", code);
			JSONArray episodes = new JSONArray();
			c2.moveToFirst();
			while (!c2.isAfterLast()) {
				JSONObject episode = new JSONObject();
				episode.put("season", String.valueOf(c2.getInt(1)));
				episode.put("episode", String.valueOf(c2.getInt(2)));

				c2.moveToNext();
				episodes.put(episode);

			}
			ob.put("episodes", episodes);
			Episode e = new Episode("1", "164951", "1", context, new Activity());
			e.addToSeen(false, ob);
			this.deleteTvShow(Integer.valueOf(code));

			c.moveToNext();

		}

	}

	//synchronizes the local WatchEpisodes DB with the server
	public void sync2() throws JSONException, InterruptedException,
			ExecutionException, ParseException {
		Cursor c;
		SQLiteDatabase db = getReadableDatabase();
		c = db.query("WatchTvShow", null, null, null, null, null, null);

		c.moveToFirst();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");
		String pass = prefs.getString("pass", "");
		JSONObject ob1 = new JSONObject();
		JSONArray shows = new JSONArray();
		ob1.put("username", user);
		ob1.put("password", pass);
		JSONObject show;

		while (!c.isAfterLast()) {
			String code = String.valueOf(c.getInt(0));

			if (this.containsOneEpisode2(Integer.valueOf(code))) { //sends single episodes, not the whole tvshow
				Cursor c2 = db.rawQuery(
						"SELECT * from WatchEpisodes where idTvShow=" + code
								+ ";", null);
				JSONObject ob = new JSONObject();
				ob.put("username", user);
				ob.put("password", pass);
				ob.put("tvdb_id", code);
				JSONArray episodes = new JSONArray();
				c2.moveToFirst();
				while (!c2.isAfterLast()) {
					JSONObject episode = new JSONObject();
					episode.put("season", String.valueOf(c2.getInt(1)));
					episode.put("episode", String.valueOf(c2.getInt(2)));

					c2.moveToNext();
					episodes.put(episode);
					// deleteEpisode2(c2.getInt(1), c2.getInt(2), c2.getInt(3));
				}
				ob.put("episodes", episodes);

				Episode e = new Episode("1", "164951", "1", context,
						new Activity());
				e.addToWatching(false, ob);
				this.deleteTvShows2(Integer.valueOf(code));
			}

			else { //sends the whole tv show
				show = new JSONObject();
				show.put("tvdb_id", code);
				shows.put(show);
				this.deleteTvShows2(Integer.valueOf(code));
			}

			c.moveToNext();
			Tv_Show tvs = new Tv_Show(code, context, new Activity());
			tvs.addToWatch(true);

		}
		SQLiteDatabase dbq = getWritableDatabase();
		dbq.execSQL("DELETE from WatchEpisodes" + ";");

	}

	
	//gets the names of the watched episode, in order to display them tidely in the app
	public Vector<String> getStrings(int id) {
		Vector<String> results = new Vector<String>();

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables("WatchEpisodes");
		// qb.setTables(sqlTables);

		Cursor c = db.rawQuery("select * from WatchEpisodes where idTvShow="
				+ id + ";", null);

		c.moveToFirst();

		while (!c.isAfterLast()) {
			String result = "<b>#" + c.getInt(2) + " " + c.getString(0)
					+ "</b><br>Season " + c.getInt(1);

			results.add(result);

			c.moveToNext();

		}

		return results;

	}
}
