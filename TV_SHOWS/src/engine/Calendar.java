package engine;

import android.os.Bundle;
import android.os.RemoteException;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentProviderClient;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;

public class Calendar {
	String id;
	Vector<String> calendars;
	Context context;
	Activity a;

	public Calendar(Context context, Activity a) {
		boolean found = false;

		this.context = context;
		this.a = a;

		calendars = new Vector<String>();

		String[] projection = new String[] { "_id", "name" };
		Uri calendars = Uri.parse("content://com.android.calendar/calendars");

		Cursor managedCursor = a.managedQuery(calendars, projection, "0=0",
				null, null);

		if (managedCursor.moveToFirst()) {
			String calName;
			String calId = "e";
			int nameColumn = managedCursor.getColumnIndex("name");

			int idColumn = managedCursor.getColumnIndex("_id");
			do {

				calName = managedCursor.getString(nameColumn);

				calId = managedCursor.getString(idColumn);

			

				if (calName.equals("TV-SHOWS Calendar")) {

					id = calId;
					found = true;
					break;
				}

				id = calId;

			} while (managedCursor.moveToNext());

			Log.e("ID FINALE", id);
			if (!found) {

				createCalendar();
				id = String.valueOf((Integer.parseInt(id) + 1));

			}

		}

	}

	@SuppressWarnings("deprecation")
	public void addToCalendar(Episode e) throws ParseException {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date result = sdfDate.parse(e.first_aired_date);

		String year = e.first_aired_date.substring(0, 4);
		String month = e.first_aired_date.substring(5, 7);

		String day = e.first_aired_date.substring(8);
		Log.e("year", year);

		GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(year),
				Integer.parseInt(month) - 1, Integer.parseInt(day));
		
	    //  "first_aired_iso":"2009-10-29T22:30:00-05:00",
		
		String complete = e.complete;
		
		complete = complete.substring(complete.indexOf("T") + 1);
		
		
	
		
		cal.setTimeZone(TimeZone.getTimeZone("GMT" + complete.substring(8,11)));
		cal.set(java.util.Calendar.HOUR, Integer.parseInt(complete.substring(0, 2)));
		cal.set(java.util.Calendar.MINUTE, Integer.parseInt(complete.substring(3, 5)));
		cal.set(java.util.Calendar.SECOND, 0);
		cal.set(java.util.Calendar.MILLISECOND, 0);
		long start = cal.getTimeInMillis();
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, start);
		values.put(Events.DTEND, start);
		values.put(Events.TITLE, e.title + " - " + e.show);
		values.put(Events.CALENDAR_ID, id);
		values.put(Events.EVENT_TIMEZONE, (TimeZone.getTimeZone("GMT" + complete.substring(8,11)).getID()));
		values.put(Events.DESCRIPTION, "Channel: " + e.network + "\n" + e.overview);
		// reasonable defaults exist:
		values.put(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);

		Uri uri = context.getContentResolver().insert(Events.CONTENT_URI,
				values);
		long eventId = new Long(uri.getLastPathSegment());

	}

	public void createCalendar() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String user = prefs.getString("user", "");

		ContentValues values1 = new ContentValues();
		values1.put(Calendars.ACCOUNT_NAME, user);
		values1.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
		values1.put(Calendars.NAME, "TV-SHOWS Calendar");
		values1.put(Calendars.CALENDAR_DISPLAY_NAME, "TV-SHOWS Calendar");
		values1.put(Calendars.CALENDAR_COLOR, 0x0066FF);
		values1.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
		values1.put(Calendars.OWNER_ACCOUNT, "some.account@googlemail.com");
		values1.put(Calendars.CALENDAR_TIME_ZONE, "USA/NewYork");
		Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI
				.buildUpon();
		builder.appendQueryParameter(Calendars.ACCOUNT_NAME,
				"com.grokkingandroid");
		builder.appendQueryParameter(Calendars.ACCOUNT_TYPE,
				CalendarContract.ACCOUNT_TYPE_LOCAL);
		builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,
				"true");
		Uri uri1 = context.getContentResolver()
				.insert(builder.build(), values1);

	}
	
	
	public boolean isInCalendar(Episode e){
		boolean result = false;
		String[] projection = new String[] {"title" };
		Uri calendars = Uri.parse("content://com.android.calendar/events");
		     
		Cursor managedCursor =
		   a.managedQuery(calendars, projection,
		   "calendar_id=5", null, null);
		
		if (managedCursor.moveToFirst()) {
			 String calName;
			 String calId; 
			 int nameColumn = managedCursor.getColumnIndex("title"); 
			
		
			 do {
			    calName = managedCursor.getString(nameColumn);
			    if (calName.equals(e.title + " - " + e.show)){
			    	result = true;
			    	break;
			    
			    
			    }
			    
			    
			    Log.e("TEST", calName);
			   // calId = managedCursor.getString(idColumn);
			   // Log.e("", calId);
			 } while (managedCursor.moveToNext());
		
		
		
		}
		
		
		
		return result;
		

}
	
	public void removeFromCalendar(Episode e){
		int id = getIndexEpisode(e);
		Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, id).buildUpon()
		        .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
		        .appendQueryParameter(Calendars.ACCOUNT_NAME, "com.grokkingandroid")
		        .appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
		        .build();
		    
		    ContentProviderClient client = a.getContentResolver().acquireContentProviderClient(CalendarContract.AUTHORITY);
		    try {
				client.delete(uri, null, null);
			} catch (RemoteException exception) {
				// TODO Auto-generated catch block
				exception.printStackTrace();
			}
		
		    client.release();
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public int getIndexEpisode(Episode e){
		
		int result = 0;
		String[] projection = new String[] {"title", "_id" };
		Uri calendars = Uri.parse("content://com.android.calendar/events");
			     
			Cursor managedCursor =
			   a.managedQuery(calendars, projection,
			   "calendar_id=5", null, null);
			
			if (managedCursor.moveToFirst()) {
				 String calName;
				 String calId; 
				 int nameColumn = managedCursor.getColumnIndex("title"); 
				
				 int idColumn = managedCursor.getColumnIndex("_id");
				 do {
				    calName = managedCursor.getString(nameColumn);	
				    calId = managedCursor.getString(idColumn);
				
				    if (calName.equals(e.title + " - " + e.show)){
				    	result = Integer.parseInt(calId);
				    	
				    }
				    
				 } while (managedCursor.moveToNext());
				}
		
		return result;
		
		
	}
	

}
