package engine;

import java.util.Vector;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
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

				Log.e("NAME", calName);
				calId = managedCursor.getString(idColumn);

				Log.e("ID", calId);

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

}
