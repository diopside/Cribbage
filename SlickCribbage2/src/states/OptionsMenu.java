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
		
		cColorVal = 2;
		
		
		pColorBox = new Rectangle(PCOLOR_X, PCOLOR_Y, 240, 160);
		cColorBox = new Rectangle(CCOLOR_X, CCOLOR_Y, 240, 160);
		soundBox = new Rectangle(SOUND_X, SOUND_Y, 240, 160);
		diffBox = new Rectangle(NAME_X, NAME_Y, 240, 160);
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
			mainMenu2.draw(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2 , 720 -mainMenu1.getHeight() - 120);
		else
			mainMenu1.draw(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2, 720 -mainMenu1.getHeight() - 120);
		
		pColors[pColorVal].draw(PCOLOR_X, PCOLOR_Y);
		cColors[cColorVal].draw(CCOLOR_X, CCOLOR_Y);
		sound[soundVal].draw(SOUND_X, SOUND_Y);
		diff[diffVal].draw(NAME_X, NAME_Y);
		backgrounds[backgroundVal].draw(BACKGROUND_X, BACKGROUND_Y);
		
		
	}


	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		int mouseX = input.getAbsoluteMouseX();
		int mouseY = input.getAbsoluteMouseY();
		
		Rectangle r = new Rectangle(Cribbage.WIDTH/2 - mainMenu1.getWidth()/2 , 720 -mainMenu1.getHeight() - 120, mainMenu1.getWidth(), mainMenu2.getHeight());
		mainMenuSelected = r.contains(mouseX, mouseY);
		
		if (mainMenuSelected && input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
			setData();
			game.initStatesList(container);
			game.enterState(Cribbage.MAIN_MENU_ID);
		}
		
		if (pColorBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
			pColorVal = (pColorVal + 1 >= pColors.length)? 0 : pColorVal + 1;
			click.play();
		}
			
		if (cColorBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
			cColorVal = (cColorVal + 1 >= cColors.length)? 0 : cColorVal + 1;
			click.play();
		}
			
		if (diffBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
			diffVal = (diffVal + 1 >= diff.length)? 0 : diffVal + 1;
			click.play();
		}
			
		if (soundBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
			soundVal = (soundVal + 1 >= sound.length)? 0 : soundVal + 1;
			click.play();
		}
			
		if (backgroundBox.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
			backgroundVal = (backgroundVal + 1 >= backgrounds.length)? 0 : backgroundVal + 1;
			click.play();
		}
			
		
	

	}
	
	public void setData(){
		Cribbage.SOUND = (soundVal == 0) ? true : false;
		Cribbage.PLAYER_COLOR = getColor(pColorVal);
		Cribbage.COMPUTER_COLOR = getColor(cColorVal);
		Cribbage.DIFFICULTY = diffVal;
		Cribbage.BACKGROUND = getBackgroundString();
	}
	
	public String getBackgroundString(){
		if (backgroundVal == 0)
			return "res/backgrounds/mm1.png";
		else if (backgroundVal == 1)
			return "res/backgrounds/mm2.png";
		else if (backgroundVal == 2)
			return "res/backgrounds/mm3.png";
		else if (backgroundVal == 3)
			return "res/backgrounds/mm4.png";
		else
			return "res/backgrounds/table.jpg";
	}

	public Color getColor(int i){
		if (i == 0)
			return Color.red;
		else if (i == 1)
			return Color.yellow;
		else if (i == 2)
			return Color.blue;
		else if (i == 3)
			return Color.green;
		else if (i == 4)
			return Color.pink;
		else
			return Color.orange;
		
	}

	public int getID() {
		return ID;
	}
}















