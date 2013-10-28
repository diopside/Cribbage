package graphics;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Button {

	private Image image;
	private int x, y;
	
	
	public Button(int x, int y, String s){
		this.x = x;
		this.y = y;
		initImage(s);
	}
	
	private void initImage(String s){
		try {
			image = new Image(s);
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}

	public int getX() {
		return this.x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return this.y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Image getImage(){
		return image;
	}
	
	
	public void render(){
		image.draw(x, y);
	}
	
	public boolean contains(int mX, int mY){
		Rectangle r = new Rectangle(x, y, image.getWidth(), image.getHeight());
		
		return r.contains(mX, mY);
	}
	
	
	
}
