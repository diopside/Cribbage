package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class OptionsMenu extends BasicGameState{
	
	private final int ID;
	private Image[] backgrounds;
	private int selectedBackground;
	private String name;
	private boolean sound;
	private Sound click;
	private int playerColor, computerColor;
	private Image mainMenu1, mainMenu2;
	private boolean mainMenuSelected;
	
	
	public OptionsMenu(int id){
		ID = id;
	}


	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		initImages();
		initSound();
	}
	
	private void initSound(){
		try {
			click = new Sound("res/sounds/click.wav");
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}
	
	private void initImages(){
		backgrounds = new Image[5];
		
		try {
			backgrounds[0] = new Image("res/backgrounds/mm1.png");
			backgrounds[1] = new Image("res/backgrounds/mm2.png");
			backgrounds[2] = new Image("res/backgrounds/mm3.png");
			backgrounds[3] = new Image("res/backgrounds/mm4.png");
			backgrounds[4] = new Image("res/backgrounds/table.jpg");
			
			mainMenu1 = new Image("res/buttons/mainmenu1.png");
			mainMenu2 = new Image("res/buttons/mainmenu2.png");
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
		
	}


	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		backgrounds[selectedBackground].draw();
		
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
