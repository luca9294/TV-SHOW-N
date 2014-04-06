package engine;

import org.json.*;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class Episode {
	

	public String id, title, first_aired, overview, image, percentage;
	
	public Episode(String id, String title, String first_aired, String overview, String image, String percentage){
		this.id = id;
		this.title = title;
		this.first_aired = first_aired;
		this.overview = overview;
		this.image = image;
		this.percentage = percentage;
	}
	
	

	
}
