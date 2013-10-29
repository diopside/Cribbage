package entities;

import java.util.ArrayList;

public class Stats {

	private int  playerMostPoints, computerMostPoints, pairPoints, runPoints, nobPoints, fifteenPoints, thirtyOnePoints, flushPoints, pegPoints;
	
	private ArrayList<Integer> playerScores;
	private ArrayList<Integer> computerScores;
	private ArrayList<Integer> playerHands;
	private ArrayList<Integer> computerHands;
	
	
	public Stats(){
		playerScores = new ArrayList<Integer>();
		computerScores = new ArrayList<Integer>();
		computerHands = new ArrayList<Integer>();
		playerHands = new ArrayList<Integer>();
		
		updateScores(0,0);
	}
	
	public void updateScores(int p, int c){
		playerScores.add(p);
		computerScores.add(c);
		
	}
	
	public void addPlayerHand(HandResult hr){
		int i = hr.getPoints();
		playerHands.add(i);
		
		playerMostPoints = (playerMostPoints > i) ? playerMostPoints : i;
	}
	
	public void addComputerHand(HandResult hr){
		int i = hr.getPoints();
		computerHands.add(i);
		
		computerMostPoints = (computerMostPoints > i) ? computerMostPoints : i;
	}

	public ArrayList<Integer> getPlayerScores() {
		return this.playerScores;
	}

	public void setPlayerScores(ArrayList<Integer> playerScores) {
		this.playerScores = playerScores;
	}

	public ArrayList<Integer> getComputerScores() {
		return this.computerScores;
	}

	public void setComputerScores(ArrayList<Integer> computerScores) {
		this.computerScores = computerScores;
	}

	public ArrayList<Integer> getPlayerHands() {
		return this.playerHands;
	}

	public void setPlayerHands(ArrayList<Integer> playerHands) {
		this.playerHands = playerHands;
	}

	public ArrayList<Integer> getComputerHands() {
		return this.computerHands;
	}

	public void setComputerHands(ArrayList<Integer> computerHands) {
		this.computerHands = computerHands;
	}

	public int getPlayerMostPoints() {
		return this.playerMostPoints;
	}

	public void setPlayerMostPoints(int playerMostPoints) {
		this.playerMostPoints = playerMostPoints;
	}

	public int getComputerMostPoints() {
		return this.computerMostPoints;
	}

	public void setComputerMostPoints(int computerMostPoints) {
		this.computerMostPoints = computerMostPoints;
	}
	
	public void addPegPoints(int a){
		pegPoints += a;
	}
	
	public void addPairPoints(int a){
		pairPoints += a;
	}
	public void addRunPoints(int a){
		runPoints += a;
	}
	
	public void addFlushPoints(int a){
		flushPoints += a;
	}
	public void addFifteenPoints(int a){
		fifteenPoints += a;
	}
	
	public void addNobPoints(int a){
		nobPoints += a;
	}
	public int getPegPoints(){
		return pegPoints;
	}
	public int getRunPoints(){
		return runPoints;
	}
	public int getPairPoints(){
		return pairPoints;
	}
	public int getFlushPoints(){
		return flushPoints;
	}
	public int getFifteenPoints(){
		return fifteenPoints;
	}
	public int getNobPoints(){
		return nobPoints;
	}
	
	
	
	
	
}
