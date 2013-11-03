package graphics;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import states.Cribbage;




public class MessageWindow {


	private Image window;
	private int x, y;
	private int pScore, cScore;
	private static final int PSCORE_X = 20, PSCORE_Y = 20, CSCORE_X = 155;
	private ArrayList<Message> messages;


	public MessageWindow(int x, int y, String loc){
		this.x = x;
		this.y = y;
		messages = new ArrayList<Message>();
		initImage(loc);
	}

	private void initImage(String loc){
		try {
			window = new Image(loc);
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}

	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Image getWindow(){
		return window;
	}

	public void render(Graphics g){
		window.draw(x, y);
	}

	public void addMessage(String s, boolean allowRepeatMessages){
		addMessage(new Message(s), allowRepeatMessages);
	}
	public void reset(){
		// this will revert the window in the event of a new game or something

	}

	public void addMessage(Message m, boolean allowRepeatMessages){
		if (!allowRepeatMessages && messages.size() > 0){
			if (m.equals(messages.get(messages.size() - 1))){
				return;
			}
		}

		if (messages.size() > 20){
			messages.remove(0);
			messages.add(m);
			for (String line: m.getLines()){
				System.out.println(line);
			}
		}
		else{
			messages.add(m);
			for (String line: m.getLines()){
				System.out.println(line);
			}
		}

	}

	public void printMessages(Graphics g){
		int startY = 25;
		int numLines = 0;
		int characterMax = 30;
		
		int messageStart = (messages.size() > 5) ? messages.size() - 5 : 0;
		
		for (int i = messageStart; i < messages.size(); i ++){
			String[] lines = messages.get(i).getLines();
			for (int j = 0; j < lines.length; j ++){
				g.drawString(lines[j], x + 15, y + startY + 16*(i-messageStart+1) + 13*numLines);
				numLines ++;
			}
		}
		                                                       
	}

	public void setPScore(int i){
		pScore = i;
	}
	public void setCScore(int i){
		cScore = i;
	}




	public void render(Graphics g, boolean playerDeals){
		window.draw(x, y);

		/*
		 * Player relevant information will be painted in red, the color of that players pegs, likewise for computer but with blue
		 * Other messages will be displayed in white
		 */

		if (playerDeals){
			g.setColor(Cribbage.PLAYER_COLOR);
			g.drawString("D-Player: "+pScore, x + PSCORE_X, y + PSCORE_Y);

			g.setColor(Cribbage.COMPUTER_COLOR);
			g.drawString("Computer: "+cScore, x + CSCORE_X, y + PSCORE_Y);
		}
		else {
			g.setColor(Cribbage.PLAYER_COLOR);
			g.drawString("Player: "+pScore, x + PSCORE_X, y + PSCORE_Y);

			g.setColor(Cribbage.COMPUTER_COLOR);
			g.drawString("D-Computer: "+cScore, x + CSCORE_X, y + PSCORE_Y);
		}


		g.setColor(Color.white);

		printMessages(g);

	}


	//***********************************************************************************************************************

	
	
}




























