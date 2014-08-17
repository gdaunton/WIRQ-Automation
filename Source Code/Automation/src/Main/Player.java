package Main;

import java.io.File;

public abstract class Player {

	private UI ui;
	private File file;
	
	public Player(UI main, File file){
		ui = main;
		this.file = file;
	}
	
	public static Player start(File song, UI ui){
		if(song.getName().substring(song.getName().length()-3, song.getName().length()).equals("mp3")){
			return new MP3(ui, song);
		}else if(song.getName().substring(song.getName().length()-3, song.getName().length()).equals("wav")){
			return new WAV(ui, song);
		}else{
			ui.printError("Unsupported file");
			return null;
		}
	}
	
	public abstract Song getSong();
	
	public File getFile(){
		return file;
	}
	
	public void setFile(File f){
		file = f;
	}
	
	public UI getUI(){
		return ui;
	}
	
	public abstract void pause();
	
	public abstract void play();
	
	public abstract void stop();
	
}
