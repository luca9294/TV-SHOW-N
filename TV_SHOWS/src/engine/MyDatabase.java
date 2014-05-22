package engine;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
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

		// you can use an alternate constructor to specify a database location
		// (such as a folder on the sd card)
		// you must ensure that this folder is available and you have permission
		// to write to it
		// super(context, DATABASE_NAME,
		// context.getExternalFilesDir(null).getAbsolutePath(), null,
		// DATABASE_VERSION);

	}

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
			Log.e("test", code);
			Tv_Show tvs = new Tv_Show(code, context, a);
			results.add(tvs);
			c.moveToNext();

		}

		return results;

	}

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

	public void insertTvShows(int i) {
		String query = "INSERT INTO  SeenTvShow VALUES (" + i + ");";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

	public void deleteAll() {
		String query = "DELETE FROM  SeenTvShow;";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

	public void insertEpisodes(String title, int season_n, int id, int idTvShow) {
		title = title.replace("'", "''");
		String query = "INSERT INTO  SeenEpisodes VALUES ('" + title + "', "
				+ season_n + ", " + id + ", " + idTvShow + ");";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

	public void deleteTvShow(int i) {
		String query = "DELETE FROM  SeenTvShow WHERE id = " + i + ";";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

	}

}
