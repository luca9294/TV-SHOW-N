package engine;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

private static final String DATABASE_NAME = "DatabaseName10";
private static final int DATABASE_VERSION = 1;

public MyDatabase(Context context) {
super(context, DATABASE_NAME, null, DATABASE_VERSION);

// you can use an alternate constructor to specify a database location
// (such as a folder on the sd card)
// you must ensure that this folder is available and you have permission
// to write to it
//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

}

public Cursor getWines(String regione, String tipo) {

SQLiteDatabase db = getReadableDatabase();
SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

String [] sqlSelect = {"0 _id", "Nome","produttore","regioni","TipologiaT","Vendemmia"};
String sqlTables = "Regioni,Italiano,Tipi";

qb.setTables("Italiano LEFT JOIN Regioni ON (Regioni.codice = Italiano.Regione) LEFT JOIN Tipi ON (Tipi.codice = Italiano.Tipologia) ");
//qb.setTables(sqlTables);


Cursor c;

if ((regione.equals("23")) && (tipo.equals("23")) ){
	c = qb.query(db, sqlSelect, null, null,
			null, null, null);


			c.moveToFirst();
	
	
}

else if (regione.equals("23") && !(tipo.equals("23"))){
	 c = qb.query(db, sqlSelect, "Tipologia ='"+tipo+"'", null,
			 null, null, null);


			c.moveToFirst();
	
	
}

else if (!(regione.equals("23")) && (tipo.equals("23"))){
	 c = qb.query(db, sqlSelect, "Regione = '"+regione+"'", null,
			 null, null, null);


			c.moveToFirst();
	
	
}



else{

 c = qb.query(db, sqlSelect, "Regione = '"+regione+"' AND Tipologia ='"+tipo+"'", null,
null, null, null);

 c.moveToFirst(); 
}




return c;

}



public Cursor getWine(String nome, String produttore, String Vendemmia ) {

SQLiteDatabase db = getReadableDatabase();
SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

String [] sqlSelect = {"0 _id", "Vitigni","Denominazione","Alcol","scheda"};


qb.setTables("Italiano");
//qb.setTables(sqlTables);


Cursor c;


	c = qb.query(db, sqlSelect, "Nome = '"+nome+"' AND Vendemmia = '"+Vendemmia+"' AND produttore = '"+produttore+"'", null,
			null, null, null);


			c.moveToFirst();
			return c;
}







public String getIdRegion(String regione2) {
if (regione2.equals("Tutte")){
	return "23";
	
}
	
	
else{
SQLiteDatabase db = getReadableDatabase();
SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

String [] sqlSelect = {"0 _id","codice"};
String sqlTables = "Regioni";

qb.setTables(sqlTables);
Cursor c = qb.query(db, sqlSelect, "regioni = " +"'"+regione2+"'", null,
null, null, null);
c.moveToFirst();


return (c.getString(1));
}
}


public String getIdTipo(String tipo2) {
if (tipo2.equals("Tutti"))
{
return "23";	
}
	
else{
SQLiteDatabase db = getReadableDatabase();
SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

String [] sqlSelect = {"0 _id","codice"};
String sqlTables = "Tipi";

qb.setTables(sqlTables);
Cursor c = qb.query(db, sqlSelect, "TipologiaT = " +"'"+tipo2+"'", null,
null, null, null);
c.moveToFirst();


return (c.getString(1));
}
}

public Cursor getEmployees() {

SQLiteDatabase db = getReadableDatabase();
SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

String [] sqlSelect = {"0 _id", "Nome"};
String sqlTables = "Italiano";

qb.setTables(sqlTables);
//Cursor c = qb.query(db, sqlSelect, "Nome = 'Perdera'", null,
//null, null, null);
Cursor c = qb.query(db, sqlSelect, null, null,
null, null, null);

c.moveToFirst();
return c;

}


public String getIdPiatto(String piatto) {
if (piatto.equals("Tutti"))
{
return "23";	
}
	
else{
SQLiteDatabase db = getReadableDatabase();
SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

String [] sqlSelect = {"0 _id","codice"};
String sqlTables = "Piatti";

qb.setTables(sqlTables);
Cursor c = qb.query(db, sqlSelect, "Tipologia = " +"'"+piatto+"'", null,
null, null, null);
c.moveToFirst();


return (c.getString(1));
}
}


public String getIdCottura(String cottura) {
if (cottura.equals("Tutte") || cottura.equals("Nessuna Cottura"))
{
return "23";	
}

	
else{
SQLiteDatabase db = getReadableDatabase();
SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

String [] sqlSelect = {"0 _id","codice"};
String sqlTables = "Cotture";

qb.setTables(sqlTables);
Cursor c = qb.query(db, sqlSelect, "Tipologia = " +"'"+cottura+"'", null,
null, null, null);
c.moveToFirst();


return (c.getString(1));
}
}

public Cursor getWines2(String regione, String tipo, String cottura, String piatto) {

SQLiteDatabase db = getReadableDatabase();
SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

String [] sqlSelect = {"0 _id", "Nome","produttore","regioni","TipologiaT","Vendemmia"};
String sqlTables = "Regioni,Italiano,Tipi";

qb.setTables("Italiano LEFT JOIN Regioni ON (Regioni.codice = Italiano.Regione) LEFT JOIN Tipi ON (Tipi.codice = Italiano.Tipologia) ");
//qb.setTables(sqlTables);


Cursor c;


if ((regione.equals("23")) && (tipo.equals("23") && (cottura.equals("23")) && (piatto.equals("23")))){
	c = qb.query(db, sqlSelect, null, null,
			null, null, null);
			c.moveToFirst();	
}

else{ 
	String s1 = "(cott1='"+cottura+"' OR cott2='"+cottura+"' OR cott3='"+cottura+"' OR cott4='"+cottura+"')";
	String s2 = "(abb1='"+piatto+"' OR abb2='"+piatto+"' OR abb3='"+piatto+"' OR abb4='"+piatto+"' OR abb5='"+piatto+"')";
	String s3 = "(Regione = '"+regione+"')";
	String s4 = "(Tipologia = '"+tipo+"')";
	
	LinkedList <String> prova = new LinkedList<String>();
	if ( ! cottura.equals("23")){
	prova.add(s1);}
	if ( ! piatto.equals("23")){
	prova.add(s2);}
	if ( ! regione.equals("23")){
	prova.add(s3);}
	if ( ! tipo.equals("23")){
	prova.add(s4);}
	
	
	String query = prova.getFirst();
	prova.removeFirst();
	
	while (prova.isEmpty() == false ){
		
		query = query + " AND " + prova.getFirst();
		prova.removeFirst();
		
	}
	
	
	
	
	
	
//String queryg=  "(cott1='"+cottura+"' OR cott2='"+cottura+"' OR cott3='"+cottura+"' OR cott4='"+cottura+"') AND (abb1='"+piatto+"' OR abb2='"+piatto+"' OR abb3='"+piatto+"' OR abb4='"+piatto+"' OR abb5='"+piatto+"') AND (Regione = '"+regione+"')   AND (Classificazione = '"+tipo+"') ";
c = qb.query(db, sqlSelect, query, null, null, null, null);
c.moveToFirst(); 
}




return c;

}








}







