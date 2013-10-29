package states;

import graphics.Button;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class MainMenu extends BasicGameState{

	private final int ID;
	private float timeElapsed; // this will be used in rendering the different backgrounds
	private Image[] backgrounds;
	private Button newGameBtn, optionsBtn, statisticsBtn, quitBtn;
	
	
	
	public MainMenu(int id){
		ID = id;
		timeElapsed = 0;
		backgrounds = new Image[4];

	}

	private void initImages(){
		try {
			backgrounds[0] = new Image("res/backgrounds/mm1.png");
			backgrounds[1] = new Image("res/backgrounds/mm2.png");
			backgrounds[2] = new Image("res/backgrounds/mm3.png");
			backgrounds[3] = new Image("res/backgrounds/mm4.png");
			
			
			newGameBtn = new Button(0 , 150,"res/buttons/newgamebtn.png");
			optionsBtn = new Button(0 , 270,"res/buttons/optionsbtn.png");
			statisticsBtn = new Button(0, 390, "res/buttons/statisticsbtn.png");
			quitBtn = new Button(0, 510, "res/buttons/quitbtn.png");
			
			
			// This will center the buttons on the screen
			newGameBtn.setX(Cribbage.WIDTH/2 - newGameBtn.getImage().getWidth()/2);
			statisticsBtn.setX(Cribbage.WIDTH/2 - statisticsBtn.getImage().getWidth()/2);
			quitBtn.setX(Cribbage.WIDTH/2 - quitBtn.getImage().getWidth()/2);
			optionsBtn.setX(Cribbage.WIDTH/2 - optionsBtn.getImage().getWidth()/2);

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

		// The following section of code will rotate between different alphas of the 4 backgrounds to create a visual effect on the screen
		Image i1;
		Image i2;
		if (timeElapsed >= 0f && timeElapsed < 1000f){
			i1 = backgrounds[0].copy();
			i2 = backgrounds[1].copy();
			i1.setAlpha((float) ((1000f - timeElapsed) / 1000f));
			i2.setAlpha(1f - i1.getAlpha());
		}
		else if (timeElapsed >= 1000f && timeElapsed < 2000f){
			i1 = backgrounds[1].copy();
			i2 = backgrounds[2].copy();
			i1.setAlpha((float) ((2000f - timeElapsed) / 1000f));
			i2.setAlpha(1f - i1.getAlpha());
		}
		else if (timeElapsed >= 2000f && timeElapsed < 3000f){
			i1 = backgrounds[2].copy();
			i2 = backgrounds[3].copy();
			i1.setAlpha((float) ((3000f - timeElapsed) / 1000f));
			i2.setAlpha(1f - i1.getAlpha());
		}
		else {
			i1 = backgrounds[3].copy();
			i2 = backgrounds[0].copy();
			i1.setAlpha( ((4000f - timeElapsed) / 1000f));
			i2.setAlpha(1f - i1.getAlpha());
		}
		i1.draw();
		i2.draw();
		
		newGameBtn.render();
		statisticsBtn.render();
		optionsBtn.render();
		quitBtn.render();
	}


	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		timeElapsed = (timeElapsed >= 4000) ? 0 : timeElapsed + 5;
		
		int mouseX = input.getAbsoluteMouseX();
		int mouseY = input.getAbsoluteMouseY();
		

		if (input.isMousePressed(input.MOUSE_LEFT_BUTTON)){

			if (newGameBtn.contains(mouseX, mouseY)){
				game.enterState(Cribbage.GAME_ID);
			}
			
			else if (statisticsBtn.contains(mouseX, mouseY)){
				game.enterState(Cribbage.STATISTICS_MENU_ID);
			}
			
			else if (quitBtn.contains(mouseX, mouseY)){
				container.exit();
			}
			
			else if (optionsBtn.contains(mouseX, mouseY)){
				game.enterState(Cribbage.OPTIONS_MENU_ID);
			}
		}

	}


	public int getID() {
		return ID;
	}
}
