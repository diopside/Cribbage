package graphics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import states.Cribbage;

public class Board {

	private int x, y;
	private Image board;
	private int ps0, ps1, cs0, cs1;
	
	
	
	public Board(int x, int y, String loc){
		this.x = x;
		this.y = y;
		initImage(loc);
	}
	
	private void initImage(String loc){
		
		try {
			board = new Image(loc);
			board = board.getScaledCopy(.95f);
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}
	
	public void render(Graphics g){

		board.draw(x, y);

		drawHoles(g);
		
		drawBars(g);

	}
	
	public void drawBars(Graphics g){
		
		Rectangle pBar = new Rectangle(x + 14, y + 127, 614, 6); 
		g.setColor(Cribbage.PLAYER_COLOR);
		g.fill(pBar);
		g.draw(pBar);
		
		Rectangle cBar = new Rectangle(x + 14, y + 134, 614, 6); 
		g.setColor(Cribbage.COMPUTER_COLOR);
		g.fill(cBar);
		g.draw(cBar);
		
		g.setColor(Color.black);
	}

	public void drawHoles(Graphics g){

		/*
		 * Lots of magic numbers in here.  I'll have to redo this at some point
		 */
		

		int xOff = 13 + 20;
		int yOff = 40;

		for (int i = 1; i < 31; i ++){
			g.setColor(Color.black);
			if (i == ps1 || i + 60 == ps1 || i == ps0 || i + 60 == ps0)
				g.setColor(Cribbage.PLAYER_COLOR);
			if ( (i -1) % 5 == 0)
				xOff += 15;
			Circle c = new Circle(x + xOff + 15*i, y + yOff, 4);
			
			g.fill(c);
			g.draw(c);
			g.setColor(Color.black);
			
			if (i == 30)
				xOff = (int) c.getCenterX();
		}
		
		 yOff = 80;
		for (int i = 1; i < 31; i ++){
			g.setColor(Color.black);
			if (i  + 30  == ps1 || i  + 30 == ps0 || i + 90 == ps0 || i + 90 == ps1)
				g.setColor(Cribbage.PLAYER_COLOR);
			
			if ( (i - 1)%5 == 0 && i != 1)
				xOff -= 15;
			
			Circle c = new Circle(xOff + 15 - 15*i, y + yOff, 4);
			g.fill(c);
			g.draw(c);
			g.setColor(Color.black);
			
			
		}
		
		

		 xOff = 13 + 20;
		 yOff = 142 + 27;

		for (int i = 1; i < 31; i ++){
			g.setColor(Color.black);
			if (i == cs1 || i + 60 == cs1 || i == cs0 || i + 60 == cs0)
				g.setColor(Cribbage.COMPUTER_COLOR);
			if ( (i -1) % 5 == 0)
				xOff += 15;
			Circle c = new Circle(x + xOff + 15*i, y + yOff, 4);
			
			g.fill(c);
			g.draw(c);
			g.setColor(Color.black);
			
			if (i == 30)
				xOff = (int) c.getCenterX();
		}
		
		yOff += 40;
		
		for (int i = 1; i < 31; i ++){
			g.setColor(Color.black);
			if (i  + 30  == cs1 || i  + 30 == cs0 || i + 90 == cs0 || i + 90 == cs1)
				g.setColor(Cribbage.COMPUTER_COLOR);
			
			if ( (i - 1)%5 == 0 && i != 1)
				xOff -= 15;
			
			Circle c = new Circle(xOff + 15 - 15*i, y + yOff, 4);
			g.fill(c);
			g.draw(c);
			g.setColor(Color.black);
			
			
		}
	}

	
	public void updatePlayerScore(int score){
		
		ps0 = ps1;
		ps1 = score;
	}
	
	public void updateComputerScore(int score){
		
		cs0 = cs1;
		cs1 = score;
	}


	
}
