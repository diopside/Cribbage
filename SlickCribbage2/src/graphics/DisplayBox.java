package graphics;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class DisplayBox {

	private int x, y;
	private int state;
	private Image[] displays;
	private String header;
	
	public DisplayBox(int x, int y, String[] locs, String header){
		this.x = x;
		this.y = y;
		this.header = header;
		state = 0;
		initImages(locs);

	}

	private void initImages(String[] locs){
		displays = new Image[locs.length];

		try {
			for (int i = 0; i < displays.length; i ++){
				displays[i] = new Image(locs[i]);
			}
		} catch (SlickException exception) {
			exception.printStackTrace();
		}

	}
	
	public int getState(){
		return state;
	}
	
	public Image getCurrentImage(){
		return displays[state];
	}
	
	public void advanceState(){
		state = (state + 1 == displays.length) ? 0 : state + 1;
	}
	
	public boolean contains(int mouseX, int mouseY){
		Rectangle r = new Rectangle(x, y, displays[0].getWidth(), displays[0].getHeight());
		return r.contains(mouseX, mouseY);
	}




	
}
