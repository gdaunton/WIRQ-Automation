package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.SwingConstants;

public class UI implements ActionListener, ItemListener {

	private JFrame frame;
	public String songName = "";
	public String artistName = "";
	public String songLength = "";
	private JLabel s;
	private JLabel a;
	private JLabel l;
	private JLabel r;
	private Timer countdownTimer;
	public int timeRem;
	private boolean isStopped;
	private JTextPane log;
	private JFileChooser chooser;
	public String RedFolder = " ";
	public String BlueFolder = " ";
	public String GreenFolder = " ";
	public String YellowFolder = " ";
	public String WallFolder = " ";
	public String MicBreakFolder = " ";
	public String LogFolder = " ";
	public String PSAFolder = " ";
	private JScrollPane scroll;
	public String currRotation = "Red";
	private int micCount = 0;
	private Player player;
	private int min = 0;
	private int sec = 0;
	private RotationReader rotation;
	private LogOut logWriter;
	public boolean isMicBreak;
	private JLabel g;
	private ArrayList<File> yellowFiles;
	private ArrayList<File> redFiles;
	private ArrayList<File> blueFiles;
	private ArrayList<File> greenFiles;
	private ArrayList<File> wallFiles;
	private ArrayList<File> PSAFiles;
	private ArrayList<Layout>layout;
	private Color rotColor;
	private JLabel logo;
	private Layout currLayout = null;
	private boolean isLayouts = true;
	private JMenuBar menu;
	private JPanel leftPane;
	private JPanel TextPane;
	private JPanel midPane;
	private JPanel buttonPane;
	private BackgroundPanel main;
	

	public static void main(String args[]){
		new UI();
	}
	
	public UI(){
		layout = new ArrayList<Layout>();
		try {
			layout.add(new Layout(ImageIO.read(getClass().getClassLoader().getResourceAsStream("green.png")), new Color(60, 179, 113)));
			layout.add(new Layout(ImageIO.read(getClass().getClassLoader().getResourceAsStream("red.png")), new Color(255, 69, 0)));
			layout.add(new Layout(ImageIO.read(getClass().getClassLoader().getResourceAsStream("purple.png")), new Color(153, 102, 255)));
			layout.add(new Layout(ImageIO.read(getClass().getClassLoader().getResourceAsStream("orange.png")), new Color(255, 204, 51)));
			layout.add(new Layout(ImageIO.read(getClass().getClassLoader().getResourceAsStream("blue.png")), new Color(0, 191, 255)));
		} catch (IOException e5) {}
		
		new File("Automation Resources").mkdir();
		frame = new JFrame("WIRQ Automation");
		
		menu = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setForeground(Color.BLUE);
		
		JMenu help = new JMenu("Help");
		help.setForeground(Color.BLUE);

		JMenuItem About = new JMenuItem("About");
		About.addActionListener(this);
		About.setForeground(new Color(0, 102, 204));
		help.add(About);
		
		JMenuItem Errors = new JMenuItem("I Have Errors!");
		Errors.setForeground(new Color(0, 102, 204));
		Errors.addActionListener(this);
		help.add(Errors);
		
		JMenuItem prop = new JMenuItem("View Properties");
		prop.setForeground(new Color(0, 102, 204));
		prop.addActionListener(this);
		help.add(prop);

		JCheckBox layouts = new JCheckBox("Layouts On/Off");
		layouts.setHorizontalAlignment(SwingConstants.CENTER);
		layouts.setForeground(new Color(0, 102, 204));
		layouts.addItemListener(this);
		file.add(layouts);
		
		JMenuItem fileRed = new JMenuItem("Set Red Rotation Folder");
		fileRed.setForeground(new Color(0, 102, 204));
		fileRed.addActionListener(this);
		file.add(fileRed);

		JMenuItem fileBlue = new JMenuItem("Set Blue Rotation Folder");
		fileBlue.setForeground(new Color(0, 102, 204));
		fileBlue.addActionListener(this);
		file.add(fileBlue);

		JMenuItem fileGreen = new JMenuItem("Set Green Rotation Folder");
		fileGreen.setForeground(new Color(0, 102, 204));
		fileGreen.addActionListener(this);
		file.add(fileGreen);

		JMenuItem fileYellow = new JMenuItem("Set Yellow Rotation Folder");
		fileYellow.setForeground(new Color(0, 102, 204));
		fileYellow.addActionListener(this);
		file.add(fileYellow);

		JMenuItem fileLog = new JMenuItem("Set Log Output Folder");
		fileLog.setForeground(new Color(0, 102, 204));
		fileLog.addActionListener(this);
		file.add(fileLog);

		JMenuItem fileMic = new JMenuItem("Set MicBreak Audio Folder");
		fileMic.setForeground(new Color(0, 102, 204));
		fileMic.addActionListener(this);
		file.add(fileMic);

		JMenuItem fileWall = new JMenuItem("Set Wall Audio Folder");
		fileWall.setForeground(new Color(0, 102, 204));
		fileWall.addActionListener(this);
		file.add(fileWall);
		
		JMenuItem filePSA = new JMenuItem("Set PSA Audio Folder");
		filePSA.setForeground(new Color(0, 102, 204));
		filePSA.addActionListener(this);
		file.add(filePSA);
		
		JMenuItem rot = new JMenuItem("Edit Rotation");
		rot.setForeground(new Color(0, 102, 204));
		rot.addActionListener(this);
		file.add(rot);

		TextPane = new JPanel();
		TextPane.setOpaque(false);
		TextPane.setLayout(new BoxLayout(TextPane, BoxLayout.PAGE_AXIS));
		TextPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
		s = new JLabel("Song: " + songName);
		s.setBackground(Color.BLUE);
		s.setForeground(Color.BLUE);
		TextPane.add(s);
		TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
		a = new JLabel("Artist: " + artistName);
		a.setForeground(Color.BLUE);
		a.setAlignmentX(s.getAlignmentX());
		TextPane.add(a);
		TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
		l = new JLabel("Song Length: " + songLength);
		l.setForeground(Color.BLUE);
		l.setAlignmentX(s.getAlignmentX());
		TextPane.add(l);
		TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
		r = new JLabel("Time Remaining: 00:00");
		r.setForeground(Color.BLUE);
		r.setAlignmentX(s.getAlignmentX());
		TextPane.add(r);
		TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
		g = new JLabel("Genre: ");
		g.setForeground(Color.BLUE);
		g.setAlignmentX(s.getAlignmentX());
		TextPane.add(g);
		
		ImageIcon image = null;
		try { 
			image = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("wirq.png")));	
		} catch (Exception e1) { 
			e1.printStackTrace();
			printError("Can't read logo");
		} 

		leftPane = new JPanel();
		leftPane.setOpaque(false);
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.PAGE_AXIS));
		leftPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));
		logo = new JLabel(image);
		leftPane.add(logo);
		leftPane.add(Box.createRigidArea(new Dimension(0, 30)));
		leftPane.add(TextPane);
		
		log = new JTextPane();
		log.setEditable(false);
		log.setBackground(Color.lightGray);

		scroll = new JScrollPane(log);
		scroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 7));
		scroll.setWheelScrollingEnabled(true);
		scroll.setHorizontalScrollBar(null);

		midPane = new JPanel();
		midPane.setOpaque(false);
		midPane.setBounds(0, 0, 694, 411);
		midPane.setLayout(new BoxLayout(midPane, BoxLayout.LINE_AXIS));
		midPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		midPane.add(leftPane);
		midPane.add(scroll);

		buttonPane = new JPanel();
		buttonPane.setBounds(241, 411, 233, 43);
		buttonPane.setOpaque(false);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JButton p = new JButton("Pause");
		p.addActionListener(this);
		buttonPane.add(p);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton st = new JButton("Start");
		st.addActionListener(this);
		buttonPane.add(st);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton nx = new JButton("Next");
		nx.addActionListener(this);
		buttonPane.add(nx);


		try {
			main = new BackgroundPanel(ImageIO.read(getClass().getClassLoader().getResourceAsStream("green.png")));
		} catch (IOException e4) {}
		main.setLayout(null);
		main.setOpaque(false);
		main.add(midPane);
		main.add(buttonPane);

		menu.add(file);
		menu.add(help);

		Container contentPane = frame.getContentPane();
		contentPane.add(main, BorderLayout.CENTER);
		try {
			frame.setIconImage(ImageIO.read(new File("Automation Resources/wirqpic.jpg")));
		} catch (IOException e3) {
			print("For an icon place a jpg file in the Automation Resources folder and name it wirqpic");
		}
		frame.setJMenuBar(menu);
		frame.setSize(700,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					FileWriter fstream = new FileWriter(new File("Automation Resources/prop.dat"));
					PrintWriter out = new PrintWriter(fstream);
					out.println(RedFolder);
					out.println(BlueFolder);
					out.println(GreenFolder);
					out.println(YellowFolder);
					out.println(MicBreakFolder);
					out.println(LogFolder);
					out.println(WallFolder);
					out.println(PSAFolder);
					out.println(micCount);
					out.println(Boolean.toString(isLayouts));
					out.close();
				} catch (Exception e1) {
					printError("Error on Properties output");
					System.exit(0);
				}
				logWriter.writeOutput("Automation closed");
				logWriter.close();
			}
		});

		try {
			Scanner scan = new Scanner(new File("Automation Resources/prop.dat"));
			RedFolder = scan.nextLine();
			BlueFolder = scan.nextLine();
			GreenFolder = scan.nextLine();
			YellowFolder = scan.nextLine();
			MicBreakFolder = scan.nextLine();
			LogFolder = scan.nextLine();
			WallFolder = scan.nextLine();
			PSAFolder = scan.nextLine();
			micCount = Integer.parseInt(scan.nextLine());
			isLayouts = Boolean.parseBoolean(scan.nextLine());
			if(!LogFolder.equals(" "))
				logWriter = new LogOut(new File(LogFolder),this);
			else
				print("Set log folder before pressing start");
		} catch (Exception e2) {
			printError("Error on read");
			printError("If this is the first run please close and reopen");
			printError("Set log folder before closing");
		}
		layouts.setSelected(isLayouts);

		updateLayout();
		rotation = new RotationReader();
		refreash();
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		String m = a.getActionCommand();
		if(m.equals("Set Red Rotation Folder")){
			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select Audio Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false); 
			int val = chooser.showOpenDialog(null);
			if (val == JFileChooser.APPROVE_OPTION) { 
				RedFolder = chooser.getSelectedFile().toString();
				refreash();
			}

		}else if(m.equals("Set Blue Rotation Folder")){
			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select Audio Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false); 
			int val = chooser.showOpenDialog(null);
			if (val == JFileChooser.APPROVE_OPTION) { 
				BlueFolder = chooser.getSelectedFile().toString();
			}

		}else if(m.equals("Set Green Rotation Folder")){
			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select Audio Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false); 
			int val = chooser.showOpenDialog(null);
			if (val == JFileChooser.APPROVE_OPTION) { 
				GreenFolder = chooser.getSelectedFile().toString();
				refreash();
			}

		}else if(m.equals("Set Yellow Rotation Folder")){
			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select Audio Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false); 
			int val = chooser.showOpenDialog(null);
			if (val == JFileChooser.APPROVE_OPTION) { 
				YellowFolder = chooser.getSelectedFile().toString();
				refreash();
			}

		}else if(m.equals("Set Log Output Folder")){
			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select Audio Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false); 
			int val = chooser.showOpenDialog(null);
			if (val == JFileChooser.APPROVE_OPTION) {
				if(!LogFolder.equals(chooser.getSelectedFile().toString())){
					LogFolder = chooser.getSelectedFile().toString();
					logWriter = new LogOut(new File(LogFolder),this);
				}
			}

		}else if(m.equals("Set MicBreak Audio Folder")){
			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select Audio Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false); 
			int val = chooser.showOpenDialog(null);
			if (val == JFileChooser.APPROVE_OPTION) { 
				MicBreakFolder = chooser.getSelectedFile().toString();
				refreash();
			}

		}else if(m.equals("Set Wall Audio Folder")){
			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select Audio Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false); 
			int val = chooser.showOpenDialog(null);
			if (val == JFileChooser.APPROVE_OPTION) { 
				WallFolder = chooser.getSelectedFile().toString();
				refreash();
			}

		}		if(m.equals("Set PSA Audio Folder")){
			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Select Audio Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false); 
			int val = chooser.showOpenDialog(null);
			if (val == JFileChooser.APPROVE_OPTION) { 
				PSAFolder = chooser.getSelectedFile().toString();
				refreash();
			}

		}else if(m.equals("Start")){
			if(!isStopped && timeRem == 0)
				nextSong();
			else if (isStopped){
				countdownTimer.start();
				isStopped = false;
				player.play();
				logWriter.writeOutput("playing resumed");
			}else{
				printError("Error stop start malfunction please restart");
			}
		}else if(m.equals("Pause")){
			if(countdownTimer != null)
				countdownTimer.stop();
			if(player != null)
				player.pause();
			if(!isStopped){
				if(sec < 10){
					String seconds = "0" + sec;
					logWriter.writeOutput("playing paused at " + min + ":" + seconds);
				}else{
					logWriter.writeOutput("playing paused at " + min + ":" + sec);
				}
			}
			isStopped = true;
		}else if(m.equals("Next")){
			r.setText("Time Remaining:");
			if(player != null && countdownTimer != null){
				player.stop();
				countdownTimer.stop();
			}
			nextSong();
			if(isStopped){
				player.pause();
				countdownTimer.stop();
			}
		}else if(m.equals("View Properties")){
			JDialog properties = new JDialog(frame, "Properties");

			JPanel TextPane = new JPanel();
			TextPane.setLayout(new BoxLayout(TextPane, BoxLayout.PAGE_AXIS));
			TextPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
			JLabel r = new JLabel("Red Folder: " + RedFolder);
			TextPane.add(r);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel b = new JLabel("Blue Folder: " + BlueFolder);
			b.setAlignmentX(r.getAlignmentX());
			TextPane.add(b);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel g = new JLabel("Green Folder: " + GreenFolder);
			g.setAlignmentX(r.getAlignmentX());
			TextPane.add(g);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel y = new JLabel("Yellow Folder: " + YellowFolder);
			y.setAlignmentX(r.getAlignmentX());
			TextPane.add(y);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel mb = new JLabel("Mic Break Folder: " + MicBreakFolder);
			mb.setAlignmentX(r.getAlignmentX());
			TextPane.add(mb);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel log = new JLabel("Log Output Folder: " + LogFolder);
			log.setAlignmentX(r.getAlignmentX());
			TextPane.add(log);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel wall = new JLabel("Wall Folder: " + WallFolder);
			wall.setAlignmentX(r.getAlignmentX());
			TextPane.add(wall);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel psa = new JLabel("PSA Folder: " + PSAFolder);
			psa.setAlignmentX(r.getAlignmentX());
			TextPane.add(psa);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel mic = new JLabel("Current DJ index: " + micCount);
			mic.setAlignmentX(r.getAlignmentX());
			TextPane.add(mic);
			properties.getContentPane().add(TextPane);
			properties.pack();
			properties.setLocationRelativeTo(null);
			properties.setVisible(true);
		}else if(m.equals("About")){
			JDialog properties = new JDialog(frame, "About");

			JPanel TextPane = new JPanel();
			TextPane.setLayout(new BoxLayout(TextPane, BoxLayout.PAGE_AXIS));
			TextPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
			JLabel b = new JLabel("Made By Gareth D. 2012");
			b.setAlignmentX(Component.CENTER_ALIGNMENT);
			TextPane.add(b);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel g = new JLabel("Song Labeling: Song Name,Artist.mp3(or .wav)");
			g.setAlignmentX(Component.CENTER_ALIGNMENT);
			TextPane.add(g);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel y = new JLabel("Version 1.3");
			y.setAlignmentX(Component.CENTER_ALIGNMENT);
			TextPane.add(y);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel mb = new JLabel("For WIRQ FM");
			mb.setAlignmentX(Component.CENTER_ALIGNMENT);
			TextPane.add(mb);

			properties.getContentPane().add(TextPane);
			properties.pack();
			properties.setLocationRelativeTo(null);
			properties.setVisible(true);
		}else if(m.equals("I Have Errors!")){
			JDialog properties = new JDialog(frame, "Error Help");

			JPanel TextPane = new JPanel();
			TextPane.setLayout(new BoxLayout(TextPane, BoxLayout.PAGE_AXIS));
			TextPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
			JLabel b = new JLabel("If you have errors the Automation was most likely set up wrong");
			b.setAlignmentX(Component.CENTER_ALIGNMENT);
			TextPane.add(b);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel g = new JLabel("If more help is needed than email gdaunton@gmail.com as a last resort");
			g.setAlignmentX(Component.CENTER_ALIGNMENT);
			TextPane.add(g);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel y = new JLabel("Please include the error message in the log window");
			y.setAlignmentX(Component.CENTER_ALIGNMENT);
			TextPane.add(y);
			TextPane.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel mb = new JLabel("An answer is not guaranteed");
			mb.setAlignmentX(Component.CENTER_ALIGNMENT);
			TextPane.add(mb);

			properties.getContentPane().add(TextPane);
			properties.pack();
			properties.setLocationRelativeTo(null);
			properties.setVisible(true);
		}else if(m.equals("Edit Rotation")){
			JDialog rotation = new JDialog(frame, "Edit Rotation");
			final JTextArea editor = new JTextArea();
			editor.setBorder(BorderFactory.createEmptyBorder(0, 5, 80, 80));
			final JTextArea uneditor = new JTextArea();
			uneditor.setEditable(false);
			uneditor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
			JPanel Pane = new JPanel();
			Pane.setLayout(new BoxLayout(Pane, BoxLayout.PAGE_AXIS));
			Pane.add(uneditor);
			Pane.add(editor);
			JScrollPane scroll = new JScrollPane(Pane);
			scroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 7));
			scroll.setWheelScrollingEnabled(true);
			scroll.setHorizontalScrollBar(null);
			Scanner scan;
			try {
				scan = new Scanner(new File("Automation Resources/rotation.dat"));
				while(scan.hasNext()){
					String line = scan.nextLine();
					if(!line.substring(0,2).equals("//"))
						editor.append(line + "\n");
					else{
						uneditor.append(line + "\n");
					}
				}
				scan.close();
			} catch (FileNotFoundException e1) {}
			rotation.getContentPane().add(scroll);
			rotation.pack();
			rotation.setLocationRelativeTo(null);
			rotation.setVisible(true);
			
			rotation.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					try {
						StringReader sr = new StringReader(uneditor.getText() + editor.getText());
						PrintWriter out = new PrintWriter(new FileOutputStream(new File("Automation Resources/rotation.dat")));
						BufferedReader br = new BufferedReader(sr); 
						String nextLine = ""; 
						while ((nextLine = br.readLine()) != null){ 
							out.println(nextLine);
						} 
						out.close();
						sr.close();
						br.close();
					} catch (Exception e1) {}
					UI.this.rotation = new RotationReader();
				}
			});
		}
	}

	
	public void nextSong(){
		currRotation = rotation.getNextRotation();
		File nextSong = null;
		Random rand = new Random();
		if(currRotation.equals("Red")){
			isMicBreak = false;
			rotColor = Color.red;
			if(redFiles.size() == 0){
				redFiles = new ArrayList<File>(Arrays.asList((new File(RedFolder)).listFiles()));
			}
			nextSong = redFiles.get(rand.nextInt(redFiles.size()));
			redFiles.remove(nextSong);
		}else if(currRotation.equals("Blue")){
			isMicBreak = false;
			rotColor = Color.blue;
			if(blueFiles.size() == 0){
				blueFiles = new ArrayList<File>(Arrays.asList((new File(BlueFolder)).listFiles()));
			}
			nextSong = blueFiles.get(rand.nextInt(blueFiles.size()));
			blueFiles.remove(nextSong);
		}else if(currRotation.equals("Green")){
			isMicBreak = false;
			rotColor = Color.green;
			if(greenFiles.size() == 0){
				greenFiles = new ArrayList<File>(Arrays.asList((new File(GreenFolder)).listFiles()));
			}
			nextSong = greenFiles.get(rand.nextInt(greenFiles.size()));
			greenFiles.remove(nextSong);
		}else if(currRotation.equals("Yellow")){
			isMicBreak = false;
			rotColor = Color.yellow;
			if(yellowFiles.size() == 0){
				yellowFiles = new ArrayList<File>(Arrays.asList((new File(YellowFolder)).listFiles()));
			}
			nextSong = yellowFiles.get(rand.nextInt(yellowFiles.size()));
			yellowFiles.remove(nextSong);
		}else if(currRotation.equals("Wall")){
			isMicBreak = false;
			rotColor = Color.black;
			if(wallFiles.size() == 0){
				wallFiles = new ArrayList<File>(Arrays.asList((new File(WallFolder)).listFiles()));
			}
			nextSong = wallFiles.get(rand.nextInt(wallFiles.size()));
			wallFiles.remove(nextSong);
		}else if(currRotation.equals("PSA")){
			isMicBreak = true;
			rotColor = Color.black;
			if(PSAFiles.size() == 0){
				PSAFiles = new ArrayList<File>(Arrays.asList((new File(PSAFolder)).listFiles()));
			}
			nextSong = PSAFiles.get(rand.nextInt(PSAFiles.size()));
			PSAFiles.remove(nextSong);
		}else if(currRotation.equals("Mic Break")){
			isMicBreak = true;
			rotColor = Color.black;
			File levels = new File(MicBreakFolder);
			File[] files = levels.listFiles();
			nextSong = files[micCount];
			if(micCount < files.length-1)
				micCount++;
			else
				micCount = 0;
		}else{
			printError("Error currRotation null");
		}
			Song currSong;
			player = Player.start(nextSong, this);
			if(player == null){
				nextSong();
				return;
			}
			currSong = player.getSong();
			String song = currSong.getTitle();
			String artist = currSong.getArtist();
			int duration = currSong.getDuration();
			sec = duration % 60;
			min = duration/60;
			timeRem = ((min*60) + sec);
			l.setText("Song Length: " + timeRem);
			countdownTimer = new Timer(1000, new CountdownTimerListener());
			countdownTimer.start();
			printColor(Color.black,"-[");
			printColor(rotColor, currRotation);
			printColor(Color.black, "] " + song + " by " + artist);
			print("");
			logWriter.writeOutput("[" + currRotation + "] " + song + " by " + artist);
			s.setText("Song: " + song);
			a.setText("Artist: " + artist);
			g.setText("Genre: " + currSong.getGenre());
			if(sec < 10){
				String seconds = "0" + sec;
				l.setText("Song Length: " + min + ":" + seconds);
				r.setText("Time Remaining: "+ min + ":" + seconds);
			}else{
				l.setText("Song Length: " + min + ":" + sec);
				r.setText("Time Remaining: "+ min + ":" + sec);
			}
		}

	
	public void updateTime(){
		int seconds = timeRem % 60;
		int min = timeRem /60;
		String sec;
		String mint;
		if(seconds < 10){
			sec = "0" + seconds;
		}else
			sec = Integer.toString(seconds);
			mint = Integer.toString(min);
		r.setText("Time Remaining: " + mint + ":" + sec);
	}

	
	public void print(String output){
	    StyledDocument doc = log.getStyledDocument();

	    Style style = log.addStyle("Colored Text", null);
	    StyleConstants.setForeground(style, Color.black);
		if(output.equals(""))
			try { doc.insertString(doc.getLength(), "\n" , style);}
	    catch (BadLocationException e){}
		else{
			try { doc.insertString(doc.getLength(), " -" + output + "\n", style);}
		    catch (BadLocationException e){}
		}
	}
	
	public void printColor(Color color, String output){
		    StyledDocument doc = log.getStyledDocument();
		    Style style = log.addStyle("Colored Text", null);
		    StyleConstants.setForeground(style, color);
		    try { doc.insertString(doc.getLength(), output, style);}
		    catch (BadLocationException e){}
	}
	
	public void printError(String output){
		    StyledDocument doc = log.getStyledDocument();
		    Style style = log.addStyle("Colored Text", null);
		    StyleConstants.setForeground(style, Color.red);
		    try { doc.insertString(doc.getLength(), "-" + output + "\n", style);}
		    catch (BadLocationException e){}
	}
	
	public void refreash(){
		yellowFiles = new ArrayList<File>(Arrays.asList((new File(YellowFolder)).listFiles()));
		redFiles = new ArrayList<File>(Arrays.asList((new File(RedFolder)).listFiles()));
		blueFiles = new ArrayList<File>(Arrays.asList((new File(BlueFolder)).listFiles()));
		greenFiles = new ArrayList<File>(Arrays.asList((new File(GreenFolder)).listFiles()));
		wallFiles = new ArrayList<File>(Arrays.asList((new File(WallFolder)).listFiles()));
		PSAFiles = new ArrayList<File>(Arrays.asList((new File(PSAFolder)).listFiles()));
	}
	
	public void updateLayout(){
		if(isLayouts){
			currLayout = layout.get(new Random().nextInt(layout.size()));
		} else{
			try {
				currLayout = new Layout(ImageIO.read(getClass().getClassLoader().getResourceAsStream("gray.png")), Color.GRAY);
			} catch (IOException e) {}
		}
		menu.setBackground(currLayout.getBar());
		main.setImage(currLayout.getBack());
	}

	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED){
			isLayouts = true;
			updateLayout();
		}else if(e.getStateChange() == ItemEvent.DESELECTED){
			isLayouts = false;
			updateLayout();
		}
	}
	
	class CountdownTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (timeRem > 0) {
				timeRem--;
				updateTime();
			}else {
				r.setText("Time Remaining: 00:00");
				countdownTimer.stop();
				player.stop();
				nextSong();
			}
		}
	}
}

