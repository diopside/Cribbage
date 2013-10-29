package states;

import org.newdawn.slick.Color;
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
	private Image[] backgrounds, pColors, cColors, sound, diff;
	private Image name;
	private Sound click;
	private Image mainMenu1, mainMenu2;
	private boolean mainMenuSelected, nameSelected;
	private int pColorVal, cColorVal, soundVal, diffVal, backgroundVal;
	private Rectangle pColorBox, cColorBox, nameBox, soundBox, diffBox, backgroundBox;
	
	private static final int PCOLOR_X = 120, PCOLOR_Y = 80, CCOLOR_X = PCOLOR_X, CCOLOR_Y = PCOLOR_Y + 240,
			NAME_X = PCOLOR_X + 400, NAME_Y = PCOLOR_Y, DIFF_X = NAME_X, DIFF_Y = NAME_Y + 240, SOUND_X = NAME_X + 400,
			SOUND_Y = PCOLOR_Y, BACKGROUND_X = SOUND_X, BACKGROUND_Y = SOUND_Y + 240;
	
	
	public OptionsMenu(int id){
		ID = id;
	}


	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		initImages();
		initSound();
		
		pColorBox = new Rectangle(PCOLOR_X, PCOLOR_Y, 240, 160);
		cColorBox = new Rectangle(CCOLOR_X, CCOLOR_Y, 240, 160);
		soundBox = new Rectangle(SOUND_X, SOUND_Y, 240, 160);
		diffBox = new Rectangle(DIFF_X, DIFF_Y, 240, 160);
		nameBox = new Rectangle(NAME_X, NAME_Y, 240, 160);
		backgroundBox = new Rectangle(BACKGROUND_X, BACKGROUND_Y, 240, 160);
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
		pColors = new Image[6];
		cColors = new Image[6];
		sound = new Image[2];
		diff = new Image[3];
		
		try {
			name = new Image("res/buttons/options/name.png");
			
			for (int i = 0; i < 7; i ++){
				if (i < 5)
					backgrounds[i] = new Image("res/buttons/options/back"+i+".png");
				if (i < 6){
					pColors[i] = new Image("res/buttons/options/pcolor"+i+".png");
					cColors[i] = new Image("res/buttons/options/ccolor"+i+".png");
				}
				if (i < 2)
					sound[i] = new Image("res/buttons/options/sound"+i+".png");
				if (i < 3)
					diff[i] = new Image("res/buttons/options/diff"+i+".png");
		
			}
			
			mainMenu1 = new Image("res/buttons/mainmenu1.png");
			mainMenu2 = new Image("res/buttons/mainmenu2.png");
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
		
	}


	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

		g.setBackground(Color.black);
		
		if (mainMenuSelected)
			mainMenu2.draw(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2 , 720 -mainMenu1.getHeight());
		else
			mainMenu1.draw(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2, 720 -mainMenu1.getHeight());
		
		pColors[pColorVal].draw(PCOLOR_X, PCOLOR_Y);
		cColors[cColorVal].draw(CCOLOR_X, CCOLOR_Y);
		sound[soundVal].draw(SOUND_X, SOUND_Y);
		diff[diffVal].draw(DIFF_X, DIFF_Y);
		backgrounds[backgroundVal].draw(BACKGROUND_X, BACKGROUND_Y);
		name.draw(NAME_X, NAME_Y);
		
		
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
		
		if (pColorBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON))
			pColorVal = (pColorVal + 1 >= pColors.length)? 0 : pColorVal + 1;
		if (cColorBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON))
			cColorVal = (cColorVal + 1 >= cColors.length)? 0 : cColorVal + 1;
		if (diffBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON))
			diffVal = (diffVal + 1 >= diff.length)? 0 : diffVal + 1;
		if (soundBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON))
			soundVal = (soundVal + 1 >= sound.length)? 0 : soundVal + 1;
		if (backgroundBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON))
			backgroundVal = (backgroundVal + 1 >= backgrounds.length)? 0 : backgroundVal + 1;
	

	}


	public int getID() {
		return ID;
	}
}
