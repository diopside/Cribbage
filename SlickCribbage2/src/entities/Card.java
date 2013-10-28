package entities;

import java.util.Stack;

public class Card {

	private int value, suit;

	public Card(int val, int suit){
		this.value = val;
		this.suit = suit;
	}

	public int value(){
		return value;
	}

	public int suit(){
		return suit;
	}

	public int countingVal(){
		if (value >= 10){
			return 10;
		}
		else
			return value;
	}

	public String toString(){
		String s = "";
		if (value == 1)	s += "Ace";
		else if (value == 11)	s += "Jack";
		else if (value == 12)	s += "Queen";
		else if (value == 13)	s += "King";
		else	s += "" + value;

		if (suit == 0) s += " of clubs";
		else if (suit == 1) s += " of diamonds";
		else if (suit == 2) s+= " of hearts";
		else s+= " of spades";

		return s;

	}

	public boolean equals(Card c){
		return (c.value() == this.value && c.suit() == this.suit);
	}
	
}



