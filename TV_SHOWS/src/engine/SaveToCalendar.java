/**
 * 
 */
package engine;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract;


public class SaveToCalendar {

	String seriesTitle;
	
	public void SetCalendarEvent(String title, String description, int year, int day, int month, int hour, int minutes, int runtime){
		// For the time we need a method that converts the "airing time" string in numbers
		
		// Construct event details
		long startMillis = 0;
		long endMillis = 0;
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(year, month, day, hour, minutes);
		startMillis = beginTime.getTimeInMillis();
		int minutesEnd = minutes + runtime; //only a dummy, needs some calculations to avoid having wrong numbers
		Calendar endTime = Calendar.getInstance();
		endTime.set(year, month, day, hour, minutesEnd);
		endMillis = endTime.getTimeInMillis();

		// Insert Event
		ContentResolver cr = null; //= getContentResolver();
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
	}
}
