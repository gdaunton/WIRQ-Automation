package Main;

import jaco.mp3.player.MP3Player;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

public class MP3 extends Player{
	
	private MP3Player player;
	private File mp3;
	public Song currSong;
	
	public MP3(UI main, File file){
		super(main, file);
		mp3 = file;
		try {
			currSong = getProperties(mp3);
		} catch (UnsupportedAudioFileException e1) {} 
		catch (IOException e1) {}
		if(currSong == null || currSong.getArtist() == null || currSong.getTitle() == null){
			try {
				currSong = getSongProp();
			} catch (UnsupportedAudioFileException e) {} 
			catch (IOException e) {}
		}
		player = new MP3Player(mp3);
		player.play();
	}

	public Song getSongProp() throws UnsupportedAudioFileException, IOException{
			int seconds = 0;
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(getFile());
			if (fileFormat instanceof TAudioFileFormat) {
				Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
				String key = "duration";
				Long duration = (Long)properties.get(key);
				seconds = (int)((duration / 1000)/1000);
			}else {
				getUI().printError("Error reading duration, Skipping to next Song");
				getUI().nextSong();
				throw new UnsupportedAudioFileException();
			}
			String title = "";
			String artist = "";
			if(!getUI().isMicBreak){
			int index1 = 0;
			int index2 = 0;
			int index3 = 0;
			for(int i = 0; i <= getFile().toString().length()-1; i++){
				getFile();
				if(getFile().toString().charAt(i) == File.separatorChar)
					index1 = i;
				if(getFile().toString().charAt(i) == ',')
					index2 = i;
				if(getFile().toString().charAt(i) == '.')
					index3 = i;
			}
			title = getFile().toString().substring(index1+1,index2);
			artist = getFile().toString().substring(index2+1,index3);
			}else{
				int index1 = 0;
				int index2 = 0;
				for(int i = 0; i <= getFile().toString().length()-1; i++){
					if(getFile().toString().charAt(i) == '/')
						index1 = i;
					if(getFile().toString().charAt(i) == '.')
						index2 = i;
				}
				title = getFile().toString().substring(index1+1,index2);
				artist = "Mic Break or PSA";
			}
			Song nextSong = new Song(title, artist, "none", seconds);
			return nextSong;
	}
	
	public Song getProperties(File file) throws UnsupportedAudioFileException, IOException {
		AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
		if (fileFormat instanceof TAudioFileFormat) {
			Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
			String song = (String)properties.get("title");
			String artist = (String)properties.get("author");
			Long duration = (Long)properties.get("duration");
			String genre = (String)properties.get("mp3.id3tag.genre");
			int sec = (int)((duration / 1000)/1000);
			Song currSong = new Song(song, artist, genre, sec);
			return currSong;
		} else {
			throw new UnsupportedAudioFileException();
		}
	}
	
	public void pause(){
		player.pause();
	}
	
	public void play(){
		player.play();
	}
	
	public void stop(){
		player.stop();
	}
	
	public Song getSong(){
		return currSong;
	}
	
}
