package states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import entities.Stats;

public class ResultsScreen extends BasicGameState {


	private final int ID;
	private Stats stats;
	private Image graphBack, mainMenu1, mainMenu2;
	private boolean mainMenuSelected;

	public ResultsScreen(int id){
		ID = id;
	}

	private void initImages(){
		try {
			graphBack = new Image("res/windows/graphback.png");
			mainMenu1 = new Image("res/buttons/mainmenu1.png");
			mainMenu2 = new Image("res/buttons/mainmenu2.png");
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		initImages();
	}

	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		g.setBackground(Color.black);

		drawGraph(g);

		g.setColor(Color.white);
		g.drawString("Final Scores:", 80, 60);
		g.drawString("     Player - "+ stats.getPlayerScores().get(stats.getPlayerScores().size() - 1), 80, 80);
		g.drawString("     Computer - "+ stats.getComputerScores().get(stats.getComputerScores().size() - 1), 80, 100);

		g.drawString("Number of Turns:  "+stats.getPlayerScores().size(), 80, 160);

		drawAveragePoints(g);
		
		drawPointInfo(g);
		
		if (mainMenuSelected)
			mainMenu2.draw(160, 550);
		else
			mainMenu1.draw(160, 550);

	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		int mouseX = input.getAbsoluteMouseX();
		int mouseY = input.getAbsoluteMouseY();
		
		Rectangle r = new Rectangle(160, 550, mainMenu1.getWidth(), mainMenu2.getHeight());
		mainMenuSelected = r.contains(mouseX, mouseY);
		
		if (mainMenuSelected && input.isMousePressed(input.MOUSE_LEFT_BUTTON))
			game.enterState(Cribbage.MAIN_MENU_ID);

	}
	public int getID() {
		return ID;
	}

	public void linkStats(Stats s){
		stats = s;
	}

	private void drawGraph(Graphics g){
		int graphX = Cribbage.WIDTH - graphBack.getWidth();
		int graphY = Cribbage.HEIGHT - graphBack.getHeight();

		int drawStartY = graphY + graphBack.getHeight();
		int xL = graphBack.getWidth() / stats.getPlayerScores().size();
		int yL = graphBack.getHeight() / 121;


		graphBack.draw(graphX, graphY);

		g.setColor(Color.darkGray);
		g.drawLine(graphX + 5, drawStartY - (yL * 31), graphX + graphBack.getWidth() - 6, drawStartY - (yL * 31));
		g.drawLine(graphX + 5, drawStartY - (yL * 61), graphX + graphBack.getWidth() - 6, drawStartY - (yL * 61));
		g.drawLine(graphX + 5, drawStartY - (yL * 91), graphX + graphBack.getWidth() - 6, drawStartY - (yL * 91));
		g.drawLine(graphX + 5, drawStartY - (yL * 121), graphX + graphBack.getWidth() - 6, drawStartY - (yL * 121));

		g.setColor(Color.white);
		g.drawString("31", graphX - 20,  drawStartY - (yL * 31));
		g.drawString("61", graphX - 20, drawStartY - (yL * 61));
		g.drawString("91", graphX - 20, drawStartY - (yL * 91));
		g.drawString("121", graphX - 28, drawStartY - (yL * 121));
		g.drawString("Score Graph", graphX + .5f * graphBack.getWidth() - 15, graphY - 18);

		int x0 = 6 + graphX;
		int y0 = -6 + drawStartY;
		int x1;
		int y1;

		g.setLineWidth(5);
		g.setColor(Cribbage.COMPUTER_COLOR);

		for (int i = 1; i < stats.getComputerScores().size(); i ++){
			x1 = i * xL + graphX;
			y1 = drawStartY - (yL * stats.getComputerScores().get(i));
			g.drawLine(x0, y0, x1, y1);

			x0 = x1;
			y0 = y1;

		}

		x0 = 6 + graphX;
		y0 = -6 + drawStartY;

		g.setColor(Cribbage.PLAYER_COLOR);

		for (int i = 1; i < stats.getPlayerScores().size(); i ++){
			x1 = i * xL + graphX;
			y1 = drawStartY - (yL * stats.getPlayerScores().get(i));
			g.drawLine(x0, y0, x1, y1);

			x0 = x1;
			y0 = y1;

		}

	}// end method drawGraph
	
	private void drawAveragePoints(Graphics g){
		float totalScore, numTurns;
		
		g.drawString("Average Turn Points: ", 80, 220);
		
		totalScore = stats.getPlayerScores().get(stats.getPlayerScores().size() - 1);
		numTurns = stats.getPlayerScores().size();
		g.drawString("     Player - " +(totalScore/numTurns) , 80, 240);
		
		totalScore = stats.getComputerScores().get(stats.getComputerScores().size() - 1);
		numTurns = stats.getComputerScores().size();
		g.drawString("     Computer - " +(totalScore/numTurns) , 80, 260);
	}
	
	private void drawPointInfo(Graphics g){
		g.drawString("Pegging Points: " + stats.getPegPoints(), 80, 320);
		
		int handPoints = 0;
		for (Integer i: stats.getPlayerHands()){
			handPoints += i.intValue();
		}
		
		g.drawString("Total points from Hands: " + handPoints, 80, 350);
		g.drawString("Hand points from Fifteens: " + stats.getFifteenPoints(), 80, 370);
		g.drawString("Hand points from Pairs: " + stats.getPairPoints(), 80, 390);
		g.drawString("Hand points from Runs: " + stats.getRunPoints(), 80, 410);
		g.drawString("Hand points from Flushes: " + stats.getFlushPoints(), 80, 430);
		g.drawString("Hand points from Nobs: " + stats.getNobPoints(), 80, 450);
	}
	
	
	
	

}






