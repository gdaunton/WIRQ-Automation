package Main;

public class Song {
	private String Title;
	private String Artist;
	private int Duration;
	private String Genre;

	public Song(String name, String artist, String genre, int duration){
		Title = name;
		Artist = artist;
		Duration = duration;
		Genre = genre;
	}
	
	public String getTitle(){
		return Title;
	}
	
	public String getArtist(){
		return Artist;
	}
	
	public int getDuration(){
		return Duration;
	}
	
	public String getGenre(){
		return Genre;
	}
}
