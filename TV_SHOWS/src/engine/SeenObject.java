package engine;

import org.json.JSONArray;

public class SeenObject {

	public String percentage, completed, left;
	public int total;
	public String id;

	public SeenObject(String percentage, String completed, String left,
			String id) {
		this.percentage = percentage;
		this.completed = completed;
		this.left = left;
		this.id = id;
		total = Integer.parseInt(completed) + Integer.parseInt(left);
	}

}
