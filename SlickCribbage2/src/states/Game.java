package states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import entities.Card;
import entities.Hand;
import entities.HandResult;
import entities.Intel;
import entities.Stats;
import graphics.Board;
import graphics.MessageWindow;

public class Game extends BasicGameState{

	private final int ID; // state identifier for StateBasedGame
	
	public static final int CARD_WIDTH = 100, CARD_HEIGHT = 140, DECK_X = Cribbage.WIDTH/2 - CARD_WIDTH/2, DECK_Y = Cribbage.HEIGHT/2 - CARD_HEIGHT/2, MARGIN = 25, 
			PLAYER_HAND_X = MARGIN, PLAYER_HAND_Y = Cribbage.HEIGHT - CARD_HEIGHT - MARGIN, COMPUTER_HAND_X = PLAYER_HAND_X, COMPUTER_HAND_Y = MARGIN,
			CRIB_X = DECK_X - 150, CRIB_Y = DECK_Y, PLAYER_PEGGING_X = 100, PLAYER_PEGGING_Y = PLAYER_HAND_Y - 175, 
			COMPUTER_PEGGING_X = PLAYER_PEGGING_X, COMPUTER_PEGGING_Y = COMPUTER_HAND_Y + 175, BOARD_X = 630, BOARD_Y = 0;
	
	public static int DIFFICULTY, PLAYER_COLOR, COMPUTER_COLOR, PAUSE_DURATION = 500;
	
	
	private String playerName; // will be read from the options text file
	private int playerScore, playerPreviousScore, computerScore, computerPreviousScore, wins, losses, state, 
			pauseLength, mouseX, mouseY, count;
	
	private long enterStateTime;
	
	private boolean playerDeals, transitioningState, displayComputerHand, playerWentLast, gameOver;
	
	// Card and hand variables
	private Hand playerHand, computerHand, crib;
	private Card cutCard;
	private ArrayList<Card> playerPlayedCards, computerPlayedCards, peggingStack;
	private Stack<Card> deck;
	private Board board;
	
	private Intel ai; // this will store decision making and card/hand calculating aspects of the game
	
	
	private ArrayList<Integer> selections; // will be used to keep track of selected card indices during pegging or discarding.
	private int[] cribAngles; // will be used to add extra flavor, crib cards spun randomly in essence
	
	// House for the images to be displayed, cards will be in a 13 x 4 array dictated by suit and face number
	private Image background, cardBack;
	private Image[][] cardImages;
	
	private Sound card, shuffle, click, peg;
	
	private MessageWindow window;
	private Stats stats;
	
	public Game(int id){
		ID = id;
	}


	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		board = new Board(BOARD_X, BOARD_Y, "res/backgrounds/board1.png");
		playerHand = new Hand(); computerHand = new Hand(); crib = new Hand();
		cutCard = null; 
		playerPlayedCards = new ArrayList<Card>(); computerPlayedCards = new ArrayList<Card>(); peggingStack = new ArrayList<Card>();
		deck = new Stack<Card>();
		selections = new ArrayList<Integer>();
		window = new MessageWindow(980, 360, "res/windows/messagewindow.png");
		window.addMessage("Cut the deck to draw a card.  Low card deals in cribbage!", false); // add an inital message
		ai = new Intel();
		stats = new Stats();
		
		cribAngles = new int[4];
		for (int i = 0; i < cribAngles.length; i ++)
			cribAngles[i] = (int) (Math.random() * 360);
		
		cardImages = new Image[13][4];
		
		initImages();
		initSounds();
		
		
		 createDeck();
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	
	private void initImages(){
		try {
			background = new Image(Cribbage.BACKGROUND);
			cardBack = new Image("res/cards/back.png");
			String imageLoc = "";
			for (int suit = 0; suit < 4; suit ++){

				for (int value = 1; value < 14; value ++){
					//these loops will provide a procedure to load all 52 card art assets into memory in the proper order based off of the way they are named in the res folder
					//these images aren't the size I would like them to be, so they get resized before being added to the array
					if (value == 1)	imageLoc = "a";
					else if (value == 11)	imageLoc = "j";
					else if (value == 12)	imageLoc = "q";
					else if (value == 13)	imageLoc = "k";
					else	imageLoc = ""+value;

					if (suit == 0)	imageLoc += "c";
					else if (suit == 1)	imageLoc += "d";
					else if (suit == 2)	imageLoc += "h";
					else		imageLoc += "s";

					imageLoc += ".png";
					System.out.println(imageLoc+ "\n");
					Image card = new Image("res/cards/"+imageLoc);
					cardImages[value-1][suit] = card;
				}
			}
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}
	
	//******************************************************************************************************************************************************
	private void initSounds(){
		try {
			card = new Sound("res/sounds/card.wav");
			shuffle = new Sound("res/sounds/shuffle.wav");
			click = new Sound("res/sounds/click.wav");
			peg = new Sound("res/sounds/peg.wav");
		} catch (SlickException exception) {
			exception.printStackTrace();
		}
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************

	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		background.draw();
		board.render(g);
		
		cardBack.draw(DECK_X, DECK_Y);// draw the deck
		if (cutCard != null)// draw the cutCard
			cardImages[cutCard.value() - 1][cutCard.suit()].draw(DECK_X - 5, DECK_Y - 5);
		
		drawHands();
		drawCrib();
		drawPeggingCards();
		
		if (state == 3){
			// draw the count during the pegging state
			g.drawString("Count: "+count, PLAYER_PEGGING_X, PLAYER_PEGGING_Y - 25);
		}
		
		
		window.render(g, playerDeals);




	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	
	private void drawHands(){
		
		for (int i = 0; i < playerHand.size(); i ++){
			boolean isSelected = false;
			Card c = playerHand.getCard(i);
			for (Integer iS: selections)
				if (iS == i)
					isSelected = true;

			if (isSelected){ // any selected card will be signified by lowering the alpha
				Image a = cardImages[c.value()-1][c.suit()].copy();
				a.setAlpha(.5f);
				a.draw(PLAYER_HAND_X + i*CARD_WIDTH, PLAYER_HAND_Y);
			}
			else
				cardImages[c.value()-1][c.suit()].draw(PLAYER_HAND_X + i*CARD_WIDTH, PLAYER_HAND_Y);
		}// end draw player hand

		if (displayComputerHand){
			for (int i = 0; i < computerHand.size(); i ++){
				Card c = computerHand.getCard(i);
				cardImages[c.value()-1][c.suit()].draw(COMPUTER_HAND_X + i*CARD_WIDTH, COMPUTER_HAND_Y);
			}
		} // end draw computer hand face up
		else
			for (int i = 0; i < computerHand.size(); i ++)
				cardBack.draw(COMPUTER_HAND_X + i * CARD_WIDTH, COMPUTER_HAND_Y);
	} // end drawHands
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************

	public void drawCrib(){

		for (int i = 0; i < crib.size(); i ++){
			if (state < 6){
				Image x = cardBack.copy();
				x.rotate(cribAngles[i]);
				if (playerDeals)
					x.draw(CRIB_X, CRIB_Y + 75);
				else
					x.draw(CRIB_X, CRIB_Y - 75);
			}
			else {
				Card c = crib.getCard(i);
				if (playerDeals)
					cardImages[c.value()-1][c.suit()].draw(PLAYER_HAND_X + i*CARD_WIDTH, PLAYER_HAND_Y - 150);
				else
					cardImages[c.value()-1][c.suit()].draw(COMPUTER_HAND_X + i*CARD_WIDTH, COMPUTER_HAND_Y + 150);
					
				
			}
			
		}
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	
	
	private void drawPeggingCards(){
		
		for (int i = 0; i < playerPlayedCards.size(); i ++){
			Card c = playerPlayedCards.get(i);
			cardImages[c.value()-1][c.suit()].draw(PLAYER_PEGGING_X+ i * 30, PLAYER_PEGGING_Y);
		}
		
		for (int i = 0; i < computerPlayedCards.size(); i ++){
			Card c = computerPlayedCards.get(i);
			cardImages[c.value()-1][c.suit()].draw(COMPUTER_PEGGING_X+ i * 30, COMPUTER_PEGGING_Y);
		}
		
	}
	
	
	
	//************************************************************************************************************************************************************
	//
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		if (!Cribbage.SOUND){
			container.setSoundOn(false);
		}
		Input input = container.getInput();
		mouseX = input.getAbsoluteMouseX(); mouseY = input.getAbsoluteMouseY();
		
		if (pauseLength > 0) // This will add pauses between things like dealing cards or the computer playing a pegging card for instance;
			pauseLength = (pauseLength > 0) ? pauseLength - delta : 0;
		
		else if (transitioningState){
			//If the state is going from one state or another, it will force a pause exited by hitting enter.  Pause length takes priority before state transitions
			window.addMessage("Moving to the next phase! Press enter to continue.", false);
			if(input.isKeyPressed(input.KEY_ENTER)){
				
				selections.clear();
				transitioningState = false;
				exitState();
			}
		}// end transition block
		else { // Game state progression and tests will happen if it isn't paused or transitioning states

			if (state == 0) determineDeal(container.getInput());
			else if (state == 1) deal();
			else if (state == 2) discardToCrib(container.getInput());
			else if (state == 3) peg(container.getInput());
			else if (state == 4) countFirstHand();
			else if (state == 5) countSecondHand();
			else if (state == 6) countCrib();
			else if (state == 7) endGame(container.getInput(), game);

			

		}
		
		window.setCScore(computerScore);
		window.setPScore(playerScore);
		
		if (isGameOver()){
			state = 7;
		}
		
		input.clearKeyPressedRecord();
	} // end method update
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	public void exitState(){
		if (state == 0){ // determine deal
			state ++;
			muck();
			shuffle();
		}
		else if (state == 1){ // deal
			state ++;
		}
		else if (state == 2){ // discard and cut
			state ++;
			selections.clear();
			count = 0;
		}
		else if (state == 3){ // peg
			state ++;
			computerHand.getCards().addAll(computerPlayedCards);
			playerHand.getCards().addAll(playerPlayedCards);
			computerPlayedCards.clear();
			playerPlayedCards.clear();
			peggingStack.clear();
			count = 0;
			
		}
		else if (state== 4){ // count 1
			state ++;
		}
		else if (state== 5){ // count 2
			state ++;
		}
		else if (state== 6){ // count crib
			state  = 1;
			muck();
			shuffle();
			playerDeals = !playerDeals;
			stats.updateScores(playerScore, computerScore);
		}

		
		enterStateTime = System.currentTimeMillis();
	}

	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************

	private void createDeck(){
		// this will create 52 unique cards, suits ranging from 0-3 and values ranging from 1-13
		// Ace = 1, J = 11, Q = 12, K = 13
		for (int s = 0; s < 4; s ++){
			for (int c = 1; c < 14; c ++){
				deck.add(new Card(c, s));
			}
		}
		shuffle();
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void shuffle(){
		Collections.shuffle(deck);
		shuffle.play();
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void muck(){
		playerHand.muck(deck);
		computerHand.muck(deck);
		crib.muck(deck);
		
		if (cutCard != null){
			deck.add(cutCard);
			cutCard = null;
		}
		
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void determineDeal(Input input){
		enterStateTime = (enterStateTime == 0) ? System.currentTimeMillis() : enterStateTime;
		
		displayComputerHand = true; // render the computer card face up
		
		
		if (playerHand.size() == 0){ // This block will handle the case of the player not yet cutting for a card
			Rectangle deckLoc = new Rectangle(DECK_X, DECK_Y, CARD_WIDTH, CARD_HEIGHT);
			if (deckLoc.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
				playerHand.add(deck.pop());
				card.play();
			}
		}
		if (computerHand.size() == 0 && System.currentTimeMillis() - enterStateTime > 1000){
			computerHand.add(deck.pop());
			card.play();
		}
		
		if (playerHand.size() == 1 && computerHand.size() == 1){
			if (playerHand.getCard(0).value() < computerHand.getCard(0).value()){ // player cuts lower and wins deal
				playerDeals = true;
				pauseLength = PAUSE_DURATION;
				transitioningState = true;
				window.addMessage("The player wins the deal!", false);
			}
			else if (playerHand.getCard(0).value() > computerHand.getCard(0).value()){ // computer cuts lower and wins deal
				playerDeals = false;
				pauseLength = PAUSE_DURATION;
				transitioningState = true;
				window.addMessage("The computer wins the deal!", false);
			}
			else { // player ties the computer
				muck();
				shuffle();
				pauseLength = PAUSE_DURATION;
				window.addMessage("The computer and player tie!!", false);
			}
		}
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void deal(){
		displayComputerHand = false;  // render the back of the cards for computer

		if (playerHand.size() == computerHand.size()){ // deal first card
			if (playerDeals)
				computerHand.add(deck.pop());
			else
				playerHand.add(deck.pop());
		}
		else 
			if (playerHand.size() > computerHand.size())
				computerHand.add(deck.pop());
			else
				playerHand.add(deck.pop());
		
		card.play();
		
		if (playerHand.size() + computerHand.size() == 12){
			exitState();
		}
		
		pauseLength = 200;

	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************

	private void discardToCrib(Input input){
		if (computerHand.size() == 6 && System.currentTimeMillis() - enterStateTime > 1000) computerDiscard();
		
		
		int index = -1;
		if (playerHand.size() == 6){
			window.addMessage("Select 2 cards to discard into the crib. These may be selected with the number keys or the mouse! When you have selected two cards, press enter.", false);
			
			if (input.isKeyPressed(input.KEY_1)) index = 0;
			if (input.isKeyPressed(input.KEY_2)) index = 1;
			if (input.isKeyPressed(input.KEY_3)) index = 2;
			if (input.isKeyPressed(input.KEY_4)) index = 3;
			if (input.isKeyPressed(input.KEY_5)) index = 4;
			if (input.isKeyPressed(input.KEY_6)) index = 5;
			
			if (input.isMousePressed(input.MOUSE_LEFT_BUTTON))
				if (mouseX >= PLAYER_HAND_X && mouseX <= PLAYER_HAND_X + playerHand.size() * CARD_WIDTH)
					if (mouseY >= PLAYER_HAND_Y && mouseY <= PLAYER_HAND_Y + CARD_HEIGHT)
						index = (mouseX - PLAYER_HAND_X) / CARD_WIDTH;
					
			if (index >= 0){ // this checks to see if a selection was made
				click.play();
				if (selections.contains(index))
					selections.remove(selections.indexOf(index));
				else if (selections.size() > 1){
					selections.remove(0);
					selections.add(index);
				}
				else if (selections.size() < 2)
					selections.add(index);
			}	// end block of 
		} // end case of hand = 6 cards
		
		if (selections.size() == 2 && input.isKeyPressed(input.KEY_ENTER)){
			Collections.sort(selections); // put in ascending order
			
			crib.add(playerHand.getCard(selections.get(1)));
			playerHand.remove(selections.get(1)); // add the first card to the crib
			
			crib.add(playerHand.getCard(selections.get(0)));
			playerHand.remove(selections.get(0)); // add the second card to the crib
		
		}
		
		
		if (crib.size() == 4 && cutCard == null){
			selections.clear();
			if (playerDeals){
				cutCard = deck.pop();
				card.play();
				window.addMessage("The computer cuts the deck and reveals a "+ cutCard.toString(), false);
			}
			else{
				window.addMessage("Cut the deck to reveal the shared card.", false);
				Rectangle deckLoc = new Rectangle(DECK_X, DECK_Y, CARD_WIDTH, CARD_HEIGHT);
				if (deckLoc.contains(mouseX, mouseY) && input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
					cutCard = (deck.pop());
					card.play();
				}
			}
			if (cutCard != null && cutCard.value() == 11){
				if (playerDeals){
					playerScore(2);
					window.addMessage("The player scores 2 for heels!", false);
				}
				else{
					computerScore(2);
					window.addMessage("The computer scores 2 for heels!", false);
				}
			}
		}
		
		if (computerHand.size() == 4 && playerHand.size() == 4 && cutCard != null){
			
			transitioningState = true;
		}
		
		
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void computerDiscard(){
		int[] indices = ai.getDiscardIndices(computerHand.getCards());
		Arrays.sort(indices);
		
		crib.add(computerHand.getCard(indices[1]));
		computerHand.remove(indices[1]);
		
		crib.add(computerHand.getCard(indices[0]));
		computerHand.remove(indices[0]);
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void peg(Input input){
		
		if (count == 31){
			startNewPeggingRound();
		}
		
		if (playerHand.size() == 4 && computerHand.size() == 4) // determine who plays the first card
			playerWentLast = (playerDeals) ? true : false;
	
		
		if (playerHand.size() + computerHand.size() > 0){
			if (computerCanGo() || playerCanGo()){
				if (playerWentLast){
					if (computerCanGo())
						computerPlayCard();
					else if (playerCanGo()){
						window.addMessage("The computer informs you: 'Go...' ", false);
						playerPlayCard(input);
					}
				} // end case of player playing the previous card

				else {
					if (playerCanGo())
						playerPlayCard(input);
					else if (computerCanGo())
						computerPlayCard();
				} // end case of computer playing the previous card
			} // end someone can go block
			else{
				if (playerWentLast){ 
					playerScore(1);
					window.addMessage("The player scores 1 for go!", false);
					stats.addPegPoints(1);
					pauseLength = PAUSE_DURATION;

				}

				else{ 
					computerScore(1);
					window.addMessage("The computer scores 1 for go!", false);
					pauseLength = PAUSE_DURATION;
				}
				startNewPeggingRound();
			}
		} // cards in hand block

		if (playerHand.size() == 0 && computerHand.size() == 0){
			if (count != 31){
				if (playerWentLast){ 
					playerScore(1);
					window.addMessage("The player scores 1 for go!", false);
					stats.addPegPoints(1);
					pauseLength = PAUSE_DURATION;

				}

				else{ 
					computerScore(1);
					window.addMessage("The computer scores 1 for go!", false);
					pauseLength = PAUSE_DURATION;
				}
			}
			transitioningState = true;
		}
		

	}

	
	//********************************************************************************************************************************************************
	
	private void startNewPeggingRound(){
		peggingStack.clear();
		count = 0;
		pauseLength = PAUSE_DURATION;
		window.addMessage("A new round of pegging begins.", false);
	}
	
	private boolean playerCanGo(){
		if (playerHand.size() == 0){
			return false;
		}
		for (Card c: playerHand.getCards()){
			if (c.countingVal() + count <= 31)
				return true;
		}
		return false;
	}
	//********************************************************************************************************************************************************
	private boolean computerCanGo(){
		if (computerHand.size() == 0){
			return false;
		}
		for (Card c: computerHand.getCards()){
			if (c.countingVal() + count <= 31)
				return true;
		}
		return false;
	}
	//********************************************************************************************************************************************************
	
	private boolean canPlay(Card c){
		return (count + c.countingVal() < 32);
	}
	
	//********************************************************************************************************************************************************
	private void playerPlayCard(Input input){
		
		
		window.addMessage("Select a card to play.", false);
		
		int index = -1;
		if (playerHand.size() > 0){
			if (input.isKeyPressed(input.KEY_1) && playerHand.size() > 0) index = 0;
			if (input.isKeyPressed(input.KEY_2) && playerHand.size() > 1) index = 1;
			if (input.isKeyPressed(input.KEY_3) && playerHand.size() > 2) index = 2;
			if (input.isKeyPressed(input.KEY_4) && playerHand.size() > 3) index = 3;
			
			if (input.isMousePressed(input.MOUSE_LEFT_BUTTON))
				if (mouseX >= PLAYER_HAND_X && mouseX <= PLAYER_HAND_X + playerHand.size() * CARD_WIDTH)
					if (mouseY >= PLAYER_HAND_Y && mouseY <= PLAYER_HAND_Y + CARD_HEIGHT)
						index = (mouseX - PLAYER_HAND_X) / CARD_WIDTH;
		}
		
		if (index >= 0){
			click.play();
			if (selections.size() > 0)
				selections.clear();
			selections.add(index);
		}
		
		if (input.isKeyPressed(input.KEY_ENTER) && selections.size() == 1){
			Card c = playerHand.getCard(selections.get(0));
			
			if (canPlay(c)){
				playerPlayedCards.add(c);
				playerHand.remove(selections.get(0));
				
				int points = playCard(c);
				if (points > 0) {
					window.addMessage("The player scores " + points + "!", false);
					playerScore(points);
					stats.addPegPoints(points);
				}

				playerWentLast = true;
				pauseLength = PAUSE_DURATION;
				selections.clear();
			}
					
		}
	
	}
	//********************************************************************************************************************************************************
	private void computerPlayCard(){

		for (int i = 0; i < computerHand.size(); i ++){
			Card c = computerHand.getCard(i);
			if (canPlay(c)){
				computerPlayedCards.add(c);
				computerHand.remove(i);

				int points = playCard(c);
				if (points > 0){
					window.addMessage("The computer scores " + points + "!", false);
					computerScore(points);
				}

				playerWentLast = false;
				pauseLength = PAUSE_DURATION;
				break;
				
			}
		}
	}
	
	//*********************************************************************************************************************************************************
	private int determinePeggingPoints(){
		int points = 0;
		String s = "";
		
		if (count == 15){
			s += "15 is made! ";
			points += 2;
		}
		else if (count == 31){
			s += "31 is made! ";
			points += 2;
		}
		
		int runPoints = getRunPoints();
		if (runPoints > 0){
			s += "A " + runPoints + " card run is made! ";
			points += runPoints;
		}
		int pairPoints = getPairPoints();
		if (pairPoints > 0){
			if (pairPoints == 2) s += "A pair is made!";
			else if (pairPoints == 6) s += "A pair royal is made!";
			else if (pairPoints == 12) s += "A double pair royal is made!";
			
			points += pairPoints;
		}
		
		window.addMessage(s, false);
		
		return points;
	}
	
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private int getPairPoints(){
		int num = 1;
		for (int i = 1; i < peggingStack.size(); i ++){
			if (peggingStack.get(0).value() == peggingStack.get(i).value())
				num ++;
			else
				break;
		}

		if (num == 2) return 2;
		else if (num == 3) return 6;
		else if (num == 4) return 12;
		
		return 0;
	}
	
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private int getRunPoints(){
		ArrayList<Card> validCards = new ArrayList<Card>(); 
		int[] orderedVals, sortedVals;

		outerloop: // this will trim the cards being checked by searching for a pairing card.  Any pair eliminates the potential run at the index previous to the pairing card
			for (Card c: peggingStack){
				for (Card v: validCards){
					if (c.value() == v.value())
						break outerloop;
				}
				validCards.add(c);
			}
		
		orderedVals = new int[validCards.size()];  // This block will create an array version of the cards in List-validCards
		for (int i = 0; i < orderedVals.length; i ++){
			orderedVals[i] = validCards.get(i).value();
		}

		for (int i = validCards.size(); i > 2; i --){
			sortedVals = Arrays.copyOfRange(orderedVals, 0 , i);
			Arrays.sort(sortedVals);
			if (sortedVals[0] + sortedVals.length - 1 == sortedVals[sortedVals.length - 1]){ // see if the cards from the selected range after being sorted constitute a run
				return sortedVals.length;
			}
		}
		return 0;
	}
	
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void countFirstHand(){
		if (playerDeals)
			countComputerHand();
		else
			countPlayerHand();
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void countSecondHand(){
		if (playerDeals)
			countPlayerHand();
		else
			countComputerHand();
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void countComputerHand(){
		displayComputerHand = true;
		HandResult hr = ai.count(computerHand, cutCard, false, stats, false);
		window.addMessage("The computer counts its hand: "+ hr.getMessage(), false);
		computerScore(hr.getPoints());
		transitioningState = true;
		stats.addComputerHand(hr);
		
		
	}
	
	
	
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	
	private void countPlayerHand(){
		HandResult hr = ai.count(playerHand, cutCard, false, stats, true);
		window.addMessage("The player counts his hand: "+ hr.getMessage(), false);
		playerScore(hr.getPoints());
		transitioningState = true;
		stats.addPlayerHand(hr);
	}
	
	
	
	
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void countCrib(){
		
		
		if (playerDeals){
			HandResult hr = ai.count(crib, cutCard, true, stats, true);
			window.addMessage("The player counts his crib: "+ hr.getMessage(), false);
			playerScore(hr.getPoints());
			transitioningState = true;
			stats.addPlayerHand(hr);
		}
		else{
			HandResult hr = ai.count(crib, cutCard, true, stats, false);
			window.addMessage("The computer counts its crib: "+ hr.getMessage(), false);
			computerScore(hr.getPoints());
			transitioningState = true;
			stats.addComputerHand(hr);
		}
		
		
	}
	
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private int playCard(Card c){
		card.play();
		peggingStack.add(0, c);
		count += c.countingVal();
		
		return determinePeggingPoints();

	}

	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void endGame(Input input, StateBasedGame game){

		String winner = (playerScore > 120) ? "player" : "computer";
		window.addMessage("The " + winner + " has won the game! Press enter to exit the game and view the results screen.", false);
		
		
		
		

		if (input.isKeyPressed(input.KEY_ENTER)){
			ResultsScreen screen = (ResultsScreen) game.getState(Cribbage.RESULTS_SCREEN_ID);
			stats.updateScores(playerScore, computerScore);
			screen.linkStats(stats);
			game.enterState(Cribbage.RESULTS_SCREEN_ID);
		}
		

	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void playerScore(int s){
		playerScore += s;
		board.updatePlayerScore(playerScore);
		peg.play();
	}

	private void computerScore(int s){
		computerScore += s;
		board.updateComputerScore(computerScore);
		peg.play();
	}

	private boolean isGameOver(){
		return (playerScore > 120 || computerScore > 120);
	}
	
	
	public int getID() {
		return ID;
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	
	private void print(String s){
		System.out.println(s);
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void print(Double d){
		System.out.println(d);
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	private void printHands(){
		print("COMPUTER HAND:");
		for (Card c: computerHand.getCards())
			print(c.toString());
		print("\n PLAYER HAND:");
		for (Card c: playerHand.getCards())
			print(c.toString());
		
	}
}









