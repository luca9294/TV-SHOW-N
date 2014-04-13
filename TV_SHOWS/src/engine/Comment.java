package engine;

import java.util.Calendar;
import java.util.Date;

public class Comment {
public String id, text;
public String date;
String code,season,episode;


public Comment(String id, String text, String date){
this.id = id;
this.text = text;
this.date = date;

/*final Calendar cal = Calendar.getInstance();
cal.setTimeInMillis(Long.parseLong(date));
Date dateN = cal.getTime();
this.date = dateN.toString();*/
}


}
