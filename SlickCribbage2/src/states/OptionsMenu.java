package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class OptionsMenu extends BasicGameState{
	private final int ID;
	
	public OptionsMenu(int id){
		ID = id;
	}


	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

	}


	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {

	}


	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

	}


	public int getID() {
		return ID;
	}
}
