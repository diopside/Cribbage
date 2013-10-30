package states;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Cribbage extends StateBasedGame {
	
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = WIDTH * 9 / 16;
	public static final int MAIN_MENU_ID = 0, GAME_ID = 1, RESULTS_SCREEN_ID = 2, OPTIONS_MENU_ID = 3, STATISTICS_MENU_ID = 4;
	public static int DIFFICULTY;
	public static String BACKGROUND;
	public static Color PLAYER_COLOR, COMPUTER_COLOR;
	public static boolean SOUND;

	public Cribbage(String name) {
		super(name);
		
		DIFFICULTY = 0;
		BACKGROUND = "res/backgrounds/mm1.png";
		PLAYER_COLOR = Color.red;
		COMPUTER_COLOR = Color.blue;
		SOUND = true;
		
		
		
		addState(new MainMenu(MAIN_MENU_ID));
		addState(new Game(GAME_ID));
		addState(new ResultsScreen(RESULTS_SCREEN_ID));
		addState(new OptionsMenu(OPTIONS_MENU_ID));
		addState(new StatisticsMenu(STATISTICS_MENU_ID));
		
		enterState(MAIN_MENU_ID);
	}

	public void initStatesList(GameContainer container) throws SlickException {
		for (int i = 0; i < 5; i ++){
			getState(i).init(container, this);
		}
	}
	
	public static void main(String[] args){
		
		try {
			AppGameContainer game = new AppGameContainer(new Cribbage("Cribbage"), WIDTH, HEIGHT, false);
			game.setTargetFrameRate(100);
			game.setShowFPS(false);
			game.setSmoothDeltas(true);
			game.start();
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}
	

	
	
	
	
	
	
	
	
	
}
