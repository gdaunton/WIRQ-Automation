package Main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WAV extends Player{

	private Clip song;
	private File f;
	private long lastPos = 0;
    
	public WAV(UI main, File file){
		super(main, file);
		f = file;
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(file);
			song = AudioSystem.getClip();
			song.open(audio);
			song.start();
		} 
		catch (MalformedURLException e) {} 
		catch (LineUnavailableException e) {} 
		catch (IOException e) {} 
		catch (UnsupportedAudioFileException e) {}
	}
 

	@Override
	public void pause() {
		lastPos = song.getMicrosecondPosition();
		song.stop();
	}

	@Override
	public void stop() {
		song.stop();
	}

	@Override
	public Song getSong() {
		int seconds = getDuration(f);
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

	@Override
	public void play(){
		song.setMicrosecondPosition(lastPos);
		song.start();
	}
	
	public static int getDuration(File file)
	{   
	    AudioInputStream stream = null;

	    try 
	    {
	        stream = AudioSystem.getAudioInputStream(file);

	        AudioFormat format = stream.getFormat();

	        return (int)(file.length() / format.getSampleRate() / (format.getSampleSizeInBits() / 8.0) / format.getChannels());
	    }
	    catch (Exception e) 
	    {
	        // log an error
	        return -1;
	    }
	    finally
	    {
	        try { stream.close(); } catch (Exception ex) { }
	    }
	}
}
