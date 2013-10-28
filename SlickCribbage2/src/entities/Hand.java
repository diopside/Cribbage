package entities;

import java.util.ArrayList;
import java.util.Collection;

public class Hand {


	private ArrayList<Card> cards;

	public Hand(){
		cards = new ArrayList<Card>();
	}

	public void add(Card c){
		cards.add(c);
	}

	public Card getCard(int index){
		try {
			return cards.get(index);
		}
		catch (IndexOutOfBoundsException e){
			e.printStackTrace();
		}

		return null;
	}

	public Card remove(int index){
		return cards.remove(index);
	}

	public int size(){
		return cards.size();
	}

	public ArrayList<Card> getCards(){
		return cards;
	}

	public void muck(Collection<Card> deck){
		// this will add the cards back to the deck and ensure the list is empty
		deck.addAll(cards);
		cards.clear();
	}
}
