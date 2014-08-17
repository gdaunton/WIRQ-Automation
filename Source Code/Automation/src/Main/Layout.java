package Main;

import java.awt.Color;
import java.awt.Image;

public class Layout {

	private Image back = null;
	private Color menu = null;
	
	public Layout(Image Background, Color Bar){
		back = Background;
		menu = Bar;
	}
	
	public Color getBar(){
		return menu;
	}
	
	public Image getBack(){
		return back;
	}
}
