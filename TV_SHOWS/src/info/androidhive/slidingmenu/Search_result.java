package info.androidhive.slidingmenu;

import java.util.List;
import java.util.Vector;

import org.json.JSONArray;

public class Search_result {
	public String title, year, nation, image_link; 
	public boolean ended;
	public JSONArray genres;
	public String id;
	
	public Search_result(String title, String year, String nation, String image_link,JSONArray array,String id, boolean ended ){
		this.title = title;
		this.year = year;
		this.nation = nation;
		this.image_link = image_link;
		this.id = id;
		this.genres = array;
		this.ended = ended;
	}
	
	
	
}
