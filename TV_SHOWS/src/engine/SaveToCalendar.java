/**
 * 
 */
package engine;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Reminders;

public class SaveToCalendar {

	String seriesTitle;
	Activity activity;

	public SaveToCalendar(String title, String description, int year, int day,
			int month, int hour, int minutes, int runtime, Activity ac) {
		activity = ac;

		SetCalendarEvent(title, description, year, day, month, hour, minutes,
				runtime);
	}

	public void SetCalendarEvent(String title, String description, int year,
			int day, int month, int hour, int minutes, int runtime) {
		// For the time we need a method that converts the "airing time" string
		// in numbers

		// Construct event details
		long startMillis = 0;
		long endMillis = 0;
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(year, month, day, hour, minutes);
		startMillis = beginTime.getTimeInMillis();
		int minutesEnd = minutes + runtime; // only a dummy, needs some
											// calculations to avoid having
											// wrong numbers
		Calendar endTime = Calendar.getInstance();
		endTime.set(year, month, day, hour, minutesEnd);
		endMillis = endTime.getTimeInMillis();

		// Insert Event
		ContentResolver cr = activity.getContentResolver();
		ContentValues values = new ContentValues();
		TimeZone timeZone = TimeZone.getDefault();
		values.put(CalendarContract.Events.DTSTART, startMillis);
		values.put(CalendarContract.Events.DTEND, endMillis);
		values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
		values.put(CalendarContract.Events.TITLE, title);
		values.put(CalendarContract.Events.DESCRIPTION, description);
		values.put(CalendarContract.Events.CALENDAR_ID, 3);
		Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

		// Retrieve ID for new event
		String eventID = uri.getLastPathSegment();
		int eventIDint = Integer.parseInt(eventID);

		// Setup Reminder for the event
		ContentValues reminders = new ContentValues();
		reminders.put(Reminders.EVENT_ID, eventID);
		reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		reminders.put(Reminders.MINUTES, 10);

		Uri uri2 = cr.insert(Reminders.CONTENT_URI, reminders);
	}

}
