package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogOut {
	private File file;
	private String today;
	private UI ui;
	private PrintWriter out;
	private DateFormat dateFormat;
	private File logFolder;
	public LogOut(File logOutput, UI main){
		ui = main;
		logFolder = logOutput;
		dateFormat = new SimpleDateFormat("MM.dd.yyyy");
		Date date = new Date();
		today = dateFormat.format(date);
		file = new File(logOutput.toString() + "/" + today + ".txt");
		try {
			out = new PrintWriter(new FileOutputStream(file, true));
		} catch (FileNotFoundException e) {
			try {
				FileWriter filew = new FileWriter(file);
				out = new PrintWriter(filew);
			} catch (IOException e1) {
				ui.print("Error does not have write permission");
			}

		}
	}

	public void writeOutput(String output){
		String today = dateFormat.format(new Date());
		SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
		String timeStamp = time.format(new Date());
		if(this.today.equals(today))
			out.println(timeStamp + " " + output);
		else{
			this.close();
			this.today = today;
			file = new File(logFolder.toString() + "/" + today + ".txt");
			try {
				FileWriter filew = new FileWriter(file);
				out = new PrintWriter(filew, true);
			} catch (IOException e) {}
			out.println(timeStamp + " " + output);
		}
	}

	public void close(){
		out.close();
	}
}
