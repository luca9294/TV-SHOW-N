package info.androidhive.slidingmenu;

public class Season {
	public String id, n_episode,image;
	//Vector<Episodes> episodes;
	
	public Season(String id, String n_episode, String image){
		this.id = id;
		this.n_episode = n_episode;
		this.image = image;
		
	}
	
	
	public String toString(){
		
		return "Season " + id + " - " + n_episode + " Episodes";
		
	}
	
	
	
}
