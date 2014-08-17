package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class RotationReader {
	private File file;
	private ArrayList<String> rotation;
	private int currIndex = 0;
	public RotationReader(){
		file = new File("Automation Resources/rotation.dat");
		rotation = new ArrayList<String>();
		if(file.exists()){
			try {
				Scanner scan = new Scanner(file);
				while(scan.hasNext()){
					String line = scan.nextLine();
					if(!line.substring(0,2).equals("//"))
						rotation.add(line);
				}
				scan.close();
			} catch (FileNotFoundException e) {
			}
		}else{
			try {
				FileWriter fstream = new FileWriter(file);
				PrintWriter out = new PrintWriter(fstream);
				out.println("//use this file to specify the rotation path for automation to follow");
				out.println("//Red = Red, Blue = Blue, Yellow = Yellow ect.(All should be capitalized)");
				out.println("//Mic Break = Mic Break");
				out.println("//THIS FILE IS CASE SENSITIVE");
				out.println("//All lines with double slashes is a comment");
				out.println("//each rotation object (red, blue) should have its own line");
				out.println("//ex: Wall");
				out.close();
				Scanner scan = new Scanner(file);
				while(scan.hasNext()){
					String line = scan.nextLine();
					if(!line.substring(0,2).equals("//"))
						rotation.add(line);
				}
				scan.close();
			} catch (IOException e1) {}
		}
	}

	public String getNextRotation(){
		String value = rotation.get(currIndex);
		if(currIndex == rotation.size()-1)
			currIndex = 0;
		else
			currIndex++;
		return value;
	}

}
