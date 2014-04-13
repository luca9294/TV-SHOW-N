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

Date dateN  = new Date(Long.parseLong(date)*1000);
this.date = dateN.toString();


}


}
