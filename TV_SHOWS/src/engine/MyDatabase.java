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
	
	
	public void printAll() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables("SeenEpisodes");
		// qb.setTables(sqlTables);

		Cursor c;

		c = db.query("SeenEpisodes", null, null, null, null, null, null);

		c.moveToFirst();

		while (!c.isAfterLast()) {
			String code = c.getString(0);
			Log.e("title", code);

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
	
	
	public boolean containsTvShow(int idTvShow, int id, int season_n) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();

		// qb.setTables(sqlTables);

		Cursor c= db.rawQuery("select idTvShow from SeenEpisodes where idTvShow=" + idTvShow +" AND season_n = "+ season_n +" AND id = "+id+";", null);

	

		c.moveToFirst();
       if (c.getCount() > 0){
		
			if (c.getInt(0) == idTvShow) {
				result = true;
			 
			}
       }

		return result;
	}
	
	
	public boolean containsOneEpisode(int idTvShow) {
		boolean result = false;

		SQLiteDatabase db = getReadableDatabase();

		// qb.setTables(sqlTables);

		Cursor c= db.rawQuery("select idTvShow from SeenEpisodes where idTvShow=" + idTvShow +";", null);

	

		c.moveToFirst();
       if (c.getCount() > 0){
		
			if (c.getInt(0) == idTvShow) {
				result = true;
			 
			}
       }

		return result;
	}
	
	
	
	
	public void insertTvEpisodes(String title, int idTvShow, int id, int season_n) {
		if (!containsTvShow(idTvShow)){
			this.insertTvShows(idTvShow);
			
		}
		String query = "INSERT INTO SeenEpisodes VALUES ('" + title + "', "+season_n+", "+id+", "+idTvShow+");";
		// String query = "DELETE FROM SeenTvShow";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);

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
		
		String query1 = "DELETE FROM  SeenEpisodes WHERE idTvShow = " + i + ";";
		SQLiteDatabase db1 = getWritableDatabase();
		db1.execSQL(query1);


	}
	
	
	public void deleteEpisode(int season_n, int id, int idTvShow) {
		String query = "DELETE FROM  SeenEpisodes WHERE idTvShow = " + idTvShow + " AND season_n ="+season_n+" AND id = "+id+";";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);
		
		if (!this.containsOneEpisode(idTvShow)){
			this.deleteTvShow(idTvShow);
			
		}
		
	}
	
	
	
	
	
	
	
	public int getCount(int i){
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCount= db.rawQuery("select count(*) from SeenEpisodes where idTvShow=" + i +";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		Log.e("COUNT", String.valueOf(count));
		
		
		return count;
	}
	
	
	public int getCountPerSeason(int idTvShow, int idSeason){
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCount= db.rawQuery("select count(*) from SeenEpisodes where idTvShow=" + idTvShow +" AND season_n = "+ idSeason +";", null);
		mCount.moveToFirst();
		int count= mCount.getInt(0);
		mCount.close();
		Log.e("COUNT2", String.valueOf(count));
		
		
		return count;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
