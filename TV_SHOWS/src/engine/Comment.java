package engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

Date dateN  = new Date(Long.parseLong(date)*1000);


DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

this.date = dateN.toGMTString();
}


}
