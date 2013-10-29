package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StatisticsMenu extends BasicGameState{
	
	
	private final int ID;
	
	private boolean mainMenuSelected;
	private Image mainMenu1, mainMenu2;
	
	
	public StatisticsMenu(int id){
		ID = id;
	}


	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		initImages();
	}
	
	private void initImages(){
		try {
			mainMenu1 = new Image("res/buttons/mainmenu1.png");
			mainMenu2 = new Image("res/buttons/mainmenu2.png");
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}


	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		
		if (mainMenuSelected)
			mainMenu2.draw(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2 , 600);
		else
			mainMenu1.draw(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2, 600);

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
