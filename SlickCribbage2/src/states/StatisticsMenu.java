package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import entities.Stats;

public class StatisticsMenu extends BasicGameState{
	
	
	private final int ID;
	
	private boolean mainMenuSelected;
	private Image mainMenu1, mainMenu2;
	private Image header;
	private Stats[] stats;
	private int index;
	
	
	public StatisticsMenu(int id){
		ID = id;
	}


	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		stats = new Stats[0];
		initImages();
		index = 0;
	}
	
	private void initImages(){
		try {
			mainMenu1 = new Image("res/buttons/mainmenu1.png");
			mainMenu2 = new Image("res/buttons/mainmenu2.png");
			header = new Image("res/buttons/statistics/header.png");
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}


	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		int numToDisplay = (stats.length > 2) ? 3 : stats.length;
		
		header.draw(Cribbage.WIDTH/2 - header.getWidth()/2, 30);

		if (mainMenuSelected)
			mainMenu2.draw(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2 , 600);
		else
			mainMenu1.draw(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2, 600);

		if (stats.length > 0)
			for (int i = 0; i < numToDisplay; i ++){
				int currentIndex = (index + i) % (stats.length);
				g.drawString("GAME: "+(currentIndex + 1), 140 + 350 * i, 140);
				drawAverages(stats[currentIndex], g, i);
				drawPointInfo(stats[currentIndex], g, i);
			}

	}

	public void drawAverages(Stats s, Graphics g, int i){
		int totalScore, numTurns;

		g.drawString("Final Score: ", 140 + 350*i, 160);

		totalScore = s.getPlayerScores().get(s.getPlayerScores().size() - 1);
		numTurns = s.getPlayerScores().size();
		g.drawString("     Player - " +(totalScore) , 140+350*i,180);

		totalScore = s.getComputerScores().get(s.getComputerScores().size() - 1);
		numTurns = s.getComputerScores().size();
		g.drawString("     Computer - " +(totalScore) , 140+350*i, 200);
		
	
	}

	public void drawPointInfo(Stats s, Graphics g, int i){
		g.drawString("Pegging Points: " + s.getPegPoints(), 140+350*i, 320);

		int handPoints = 0;
		for (Integer b: s.getPlayerHands()){
			handPoints += b.intValue();
		}

		g.drawString("Total points from Hands: " + handPoints, 140+350*i, 350);
		g.drawString("Hand points from Fifteens: " + s.getFifteenPoints(), 140+350*i, 370);
		g.drawString("Hand points from Pairs: " + s.getPairPoints(), 140+350*i, 390);
		g.drawString("Hand points from Runs: " + s.getRunPoints(), 140+350*i, 410);
		g.drawString("Hand points from Flushes: " + s.getFlushPoints(), 140+350*i, 430);
		g.drawString("Hand points from Nobs: " + s.getNobPoints(), 140+350*i, 450);
	}

	public void updateStats(Stats s){
		Stats[] newStats = new Stats[stats.length + 1];
		for (int i = 0; i < stats.length; i ++){
			newStats[i] = stats[i];
		}
		newStats[newStats.length - 1] = s;
		stats = newStats.clone();

	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		Input input = container.getInput();
		int mouseX = input.getAbsoluteMouseX();
		int mouseY = input.getAbsoluteMouseY();
		
		Rectangle r = new Rectangle(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2 , 600, mainMenu1.getWidth(), mainMenu2.getHeight());
		mainMenuSelected = r.contains(mouseX, mouseY);
		
		if (mainMenuSelected && input.isMousePressed(input.MOUSE_LEFT_BUTTON))
			game.enterState(Cribbage.MAIN_MENU_ID);


	}


	public int getID() {
		return ID;
	}
}
