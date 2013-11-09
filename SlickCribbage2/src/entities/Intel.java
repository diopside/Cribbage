package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class Intel {


	public static final double AVERAGE_POINTS = 4.72;
	
	
	public HandResult count(Hand hand, boolean isCrib, Stats stats, boolean isPlayer){
		HandResult hr = new HandResult();
		int points = 0;
		int fifteens = 0;
		int flushPoints = 0;
		int numPairs = 0;
		int runPoints = 0;
		int runMult = 0;
		int nobs = 0;
		
		
		int[] vals = new int[5];
		int[] cVals = new int[5];
				
		int[] pairs = new int[13];
		
		for (int i = 0; i < hand.size(); i ++){
			Card c = hand.getCard(i);
			vals[i] = c.value();
			pairs[c.value() - 1] ++;
			cVals[i] = c.countingVal();
		}
		
		fifteens = getFifteens(cVals.clone());
		flushPoints = getFlushPoints(hand, isCrib);
		
		
		// get pair points
		for (int i = 0; i < 13; i ++){
			if (pairs[i] == 2){
				numPairs ++;
			}
			else if (pairs[i] == 3){
				numPairs += 3;
			}
			else if (pairs[i] == 4){
				numPairs += 6;
			}
		}
		
		// check for nobs
		if (has(11, vals)){
			int i = valueIndex(11, hand.getCards());
			
			if(i != 4 && hand.getCard(i).suit() == hand.getCard(4).suit())
				nobs = 1;
		}
		
		
		// check for runs
		int startIndex = 0;
		int runLength = 0;
		for (int i = 0; i < pairs.length; i ++){
			if (pairs[i] > 0){
				if (runLength == 0)
					startIndex = i;
				runLength ++;
			}
			else {
				if (runLength < 3){
					runLength = 0;
				}
				else
					break;
			}
		}
		
		// check for run multiplicity
		if( runLength > 2){
			for (int i = startIndex; i < startIndex + runLength ; i ++){
				if (pairs[i] == 1 && runMult == 0)
					runMult = 1;
				
				else if (pairs[i] == 2 && runMult < 2)
					runMult = 2;
				
				else if (pairs[i] == 2 && runMult == 2)
					runMult = 4;
			}
			runPoints = runLength;
		}
		
		if (runPoints > 2 && numPairs == 3){
			runMult = 3;
		}
		 
		// end runs
		
		if (isPlayer){
			stats.addFifteenPoints(fifteens * 2);
			stats.addFlushPoints(flushPoints);
			stats.addNobPoints(nobs);
			stats.addPairPoints(numPairs * 2);
			stats.addRunPoints(runPoints * runMult);
		}
		
		hr.setPoints(fifteens * 2 + nobs + flushPoints + numPairs * 2 + runPoints * runMult  );
		hr.setMessage(" Points: "+hr.getPoints()+"    15s-"+ 2* fifteens + "  Nobs-"+nobs+"  Flush-"+flushPoints+"  Pairs-"+ numPairs * 2+ "  Runs-"+runPoints*runMult);
		

		return hr;

	}
	
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	
	public int getFlushPoints(Hand hand, boolean isCrib){
		int[] suit = new int[4];
		int suitPts = 0;
		
		if (hand.getCard(0).suit() == hand.getCard(1).suit() && hand.getCard(0).suit() == hand.getCard(2).suit() 
				&& hand.getCard(0).suit() == hand.getCard(3).suit()){
			if (hand.getCard(0).suit() == hand.getCard(4).suit()){
				suitPts = 5;
			}
			else
				suitPts = 4;
		}
		
		if (isCrib)
			suitPts = (suitPts == 5) ? 5 : 0;
		
		return suitPts;
		
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	public int getFifteens(int[] a){
		int fifteens = 0;
		
		// 5 card fifteen test
		int sum = a[0] + a[1] + a[2] + a[3] + a[4];
		if (sum == 15)
			fifteens ++;
		// 4 card fifteen test
		for (int i = 0; i < 5; i ++)
			if (sum - a[i] == 15)
				fifteens ++;
		// 3 card fifteen test
		for (int i = 0; i < 3; i ++)
			for (int j = i + 1; j < 4; j ++)
				for (int k = j + 1; k < 5; k ++)
					if (a[i] + a[j] + a[k] == 15)
						fifteens ++;
		// 2 card fifteen test
		for (int i = 0; i < 4; i ++)
			for (int j = i + 1; j < 5; j++)
				if (a[i] + a[j] == 15)
					fifteens ++;
		return fifteens;
	}
	//******************************************************************************************************************************************************8
	 public int[] getValueArray(ArrayList<Card> cards){
		 int[] values = new int[cards.size()];
		 
		 for (int i = 0; i < values.length; i ++){
			 values[i] = cards.get(i).value();
		 }
		 
		 return values;
	 }
	
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	public HandResult count(Hand hand, Card card, boolean isCrib, Stats stats, boolean isPlayer){
		
		if (hand.size() != 4)
			return null;
		
		Hand h = new Hand();
		for (Card c: hand.getCards())
			h.add(c);
		h.add(card);
		
		return count(h, isCrib, stats, isPlayer);
		
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************

	
	public boolean has(int val, int[] vals){
		for (int i = 0; i < vals.length; i ++){
			if (vals[i] == val)
				return true;
		}
		return false;
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	public int valueIndex(int val, ArrayList<Card> cards){
		int index = -1;
		
		for (int i = 0; i < cards.size(); i ++){
			if (cards.get(i).value() == val){
				index = i;
				break;
			}
		}
		return index;
	}
	//************************************************************************************************************************************************************
	//************************************************************************************************************************************************************
	

	public int[] getDiscardIndices(ArrayList<Card> hand){
		int[] indices = new int[2];
		indices[0] = 0;
		indices[1] = 1;
		
		
		return indices;
	}
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	public double expectedDiscardVal(int val1, int val2){
		/*
		 * It was computationally expensive to estimate the value provided/lost by discarding into a crib.
		 * This is a table of values approximating the value a combination of 2 cards nets when cribbed
		 */
		int v1, v2;
		double expectedValue = 0.0;
		if (val1 < val2){
			v1 = val1;
			v2 = val2;
		}
		else{
			v1 = val2;
			v2 = val1;
		}
		
		if (v1 == 1){
			if (v2 == 13) return 3.3;     else if (v2 == 12) return 3.3;    else if (v2 == 11) return 3.5;  else if (v2 == 10) return 3.3;   else if (v2 == 9) return 3.3;
			else if (v2 == 8) return 3.7;   else if (v2 == 7) return 3.7;   else if (v2 == 6) return 3.7;   else if (v2 == 5) return 5.2;   else if (v2 == 4) return 5.2;
			else if (v2 == 3) return 4.6;   else if (v2 == 2) return 4.4;   else if (v2 == 1) return 5.2;  
		}
		if (v1 == 2){
			if (v2 == 13) return 3.6;     else if (v2 == 12) return 3.6;    else if (v2 == 11) return 3.8;  else if (v2 == 10) return 3.6;   else if (v2 == 9) return 3.7;
			else if (v2 == 8) return 3.7;   else if (v2 == 7) return 3.9;   else if (v2 == 6) return 3.9;   else if (v2 == 5) return 5.2;   else if (v2 == 4) return 4.6;
			else if (v2 == 3) return 6.9;   else if (v2 == 2) return 5.8;  
		}
		if (v1 == 3){
			if (v2 == 13) return 3.7;     else if (v2 == 12) return 3.7;    else if (v2 == 11) return 3.9;  else if (v2 == 10) return 3.6;   else if (v2 == 9) return 3.7;
			else if (v2 == 8) return 3.9;   else if (v2 == 7) return 3.7;   else if (v2 == 6) return 3.8;   else if (v2 == 5) return 5.9;   else if (v2 == 4) return 5.0;
			else if (v2 == 3) return 5.9;   
		}
		if (v1 == 4){
			if (v2 == 13) return 3.5;     else if (v2 == 12) return 3.5;    else if (v2 == 11) return 3.7;  else if (v2 == 10) return 3.4;   else if (v2 == 9) return 3.6;
			else if (v2 == 8) return 3.9;   else if (v2 == 7) return 3.7;   else if (v2 == 6) return 3.9;   else if (v2 == 5) return 6.3;   else if (v2 == 4) return 5.5;
		}
		if (v1 == 5){
			if (v2 == 13) return 6.3;     else if (v2 == 12) return 6.4;    else if (v2 == 11) return 6.7;  else if (v2 == 10) return 6.3;   else if (v2 == 9) return 5.1;
			else if (v2 == 8) return 5.3;   else if (v2 == 7) return 5.8;   else if (v2 == 6) return 6.4;   else if (v2 == 5) return 8.5;
		}
		if (v1 == 6){
			if (v2 == 13) return 2.9;     else if (v2 == 12) return 3.0;    else if (v2 == 11) return 3.2;  else if (v2 == 10) return 3.0;   else if (v2 == 9) return 4.9;
			else if (v2 == 8) return 4.6;   else if (v2 == 7) return 4.9;   else if (v2 == 6) return 5.6;   
		}
		if (v1 == 7){
			if (v2 == 13) return 3.1;     else if (v2 == 12) return 3.1;    else if (v2 == 11) return 3.3;  else if (v2 == 10) return 3.1;   else if (v2 == 9) return 4.0;
			else if (v2 == 8) return 6.4;   else if (v2 == 7) return 5.8;  
		}
		if (v1 == 8){
			if (v2 == 13) return 3.0;     else if (v2 == 12) return 3.1;    else if (v2 == 11) return 3.3;  else if (v2 == 10) return 3.7;   else if (v2 == 9) return 4.5;
			else if (v2 == 8) return 5.3;   
		}
		if (v1 == 9){
			if (v2 == 13) return 2.8;     else if (v2 == 12) return 2.8;    else if (v2 == 11) return 3.7;  else if (v2 == 10) return 4.1;   else if (v2 == 9) return 4.9;
		}
		if (v1 == 10){
			if (v2 == 13) return 2.7;     else if (v2 == 12) return 3.3;    else if (v2 == 11) return 4.3;  else if (v2 == 10) return 4.6;   
		}
		if (v1 == 11){
			if (v2 == 13) return 3.8;     else if (v2 == 12) return 4.5;    else if (v2 == 11) return 5.1; 
		}
		if (v1 == 12){
			if (v2 == 13) return 3.4;     else if (v2 == 12) return 4.5;    
		}
		if (v1 == 13)
			if (v2 == 13) return 4.4;

		return expectedValue;
			
	}
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	
	public ArrayList<Card> determineDiscard(Hand hand, boolean isDealer){
		
		ArrayList<CardTuple> combinations = new ArrayList<CardTuple>(); // these will store the hand indices and the expected value for those hands
		ArrayList<Card> discards = hand.getCards();
		
		
		for (int i = 0; i < 3; i ++)  // these for loops will simulate the binomial coefficient of (6 ~ 4) making all 15 unique combinations
			for (int j = i + 1; j < 4; j ++)
				for (int k = j + 1; k < 5; k ++)
					for (int l = k + 1; l < 6; l++){
						double eV = expectedHandValue(i,j,k,l,hand, isDealer);						
						combinations.add(new CardTuple(i, j, k, l, eV));
					}
		
		int maxValIndex = 0;
		for (int i = 0; i < combinations.size(); i ++) // this loop will determine the index of the hand with the best net expected value
			maxValIndex = (combinations.get(maxValIndex).getExpectedValue() > combinations.get(i).getExpectedValue()) ? maxValIndex : i; 
		
		int[] indices = combinations.get(maxValIndex).getIndices();
		Arrays.sort(indices);
		discards.remove(indices[3]); discards.remove(indices[2]); discards.remove(indices[1]); discards.remove(indices[0]); 
		
		return discards;
		
	}
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	public double expectedHandValue(int a, int b, int c, int d, Hand hand, boolean isDealer){
		double eV = 0.0;
		ArrayList<Card> cards = (ArrayList<Card>) hand.getCards().clone(); // just to eliminate hand.getCards method calls further down to aid readability
		
		ArrayList<Card> deck = new ArrayList<Card>();
		for (int s = 0; s < 4; s ++)
			for (int t = 1; t < 14; t ++)
				deck.add(new Card(t, s));
		deck.removeAll(cards); //create a deck and remove every card shared by the computer's hand
		
		Hand h = new Hand();
		h.add(cards.get(a));  h.add(cards.get(b));  h.add(cards.get(c));  h.add(cards.get(d));
		cards.removeAll(h.getCards()); // now this will hold the cards supposed to be discarded which will then be tested for their crib value
		
		
		if (isDealer) // add the net value added from the cards being discarded
			eV += .66* (expectedDiscardVal(cards.get(0).value(), cards.get(1).value()) - AVERAGE_POINTS);
		else
			eV -= .66 * (expectedDiscardVal(cards.get(0).value(), cards.get(1).value()) - AVERAGE_POINTS);
		
		
		for (Card card: deck){
			h.add(card);
			eV += count(h, false, null, false).getPoints() / 46.0;
			h.getCards().remove(card);
		} // sum the potential 5 card combinations to get the average hand total
		System.out.println(eV + "  " + a + "-" + b + "-"+ c + "-"+d);
		return eV;
	}
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	public int getPeggingIndex(Hand hand, ArrayList<Card> peggingStack, int count){
		Hand playableHand = new Hand();
		for (Card c: hand.getCards()){
			if (c.countingVal() + count <= 31)
				playableHand.add(c);
		}
		
		int[] values = getValueArray(playableHand.getCards());
		
		if (count == 0){ // if the round of pegging is fresh it brings a special set of circumstances regarding the card to play
			return getCardToLeadIndex(playableHand.getCards());
		}
		 if (peggingStack.size() > 1 && peggingStack.get(0).value() == peggingStack.get(1).value() ){ // if can make a pair royal or double pair royal DO THAT
				if (valueIndex(peggingStack.get(0).value(), playableHand.getCards()) >= 0)
					return valueIndex(peggingStack.get(0).value(), playableHand.getCards());
			}
		if (indexOfCanBringCountTo(15, count, playableHand) >= 0 ){ // making a 15 takes the nexy priority
			return indexOfCanBringCountTo(15, count, playableHand);
		}
		if (peggingStack.size() > 1 && indexOfCanMakeRun(peggingStack, playableHand) >= 0 ){ // making a run comes after
			return indexOfCanMakeRun(peggingStack, playableHand);
		}
		if (has(peggingStack.get(0).value(), getValueArray(playableHand.getCards())) ){ // see if you can make a pair
			return valueIndex(peggingStack.get(0).value(), playableHand.getCards());
		}
		if (indexOfCanBringCountTo(31, count, playableHand) >= 0 ){ // see if you can get the count to 31
			return indexOfCanBringCountTo(31, count, playableHand);
		}
		
		if (playableHand.getCard( getDefensiveIndex(count, playableHand, peggingStack)).value() + count <= 31 ){ // find a somewhat non harmful card to play
			return getDefensiveIndex(count, playableHand, peggingStack);
		}
			
		 // if all else fails find some index of a playable card
			for (int i = 0; i < playableHand.getCards().size(); i ++){
				if (playableHand.getCard(i).value() + count <= 31)
					return i;
			}
		
			
		return 0;
		
	}//**********************************************************************************************************************
	
	public int getDefensiveIndex(int count, Hand hand, ArrayList<Card> peggingStack){
		int[] values = getValueArray(hand.getCards());
		if (count > 21){
			if (has(5,values)) // a chance to get out of a 5 trap
				return valueIndex(5, hand.getCards());
			else { // otherwise play the card that gets it closest to 31
				for (int i = 31 - count; i > 0; i --){
					if (has(i, values)) return valueIndex(i, hand.getCards());
				}
			}
		}
		
		if (count < 5){
			for (int i = 9; i > 5; i --){
				if (has(i, values)) return valueIndex(i, hand.getCards());
			}
			
			for (int i = 4; i > 0; i --){
				if (has(i, values)) return valueIndex(i, hand.getCards());
			}
			for (int i = 13; i > 9; i --){
				if (has(i, values)) return valueIndex(i, hand.getCards());
			}
			if (has(5, values)) return valueIndex(5, hand.getCards());
		}
		
		else{
			for (int i = 13; i > 0; i --){
				if (has(i, values)) return valueIndex(i, hand.getCards());
			}
		}
		
		return 0;
		
	}
	
	//**********************************************************************************************************************
	public int indexOfCanBringCountTo(int value, int count, Hand hand){
		/*
		 * This method will attempt to find a card capable of bringing the count to a value (most likely 15)
		 */
		ArrayList<Card> cards = hand.getCards();
		int index = -1;
		
		for (int i = 0; i < cards.size(); i ++)
			if (cards.get(i).countingVal() + count == value)
				index = i;
		
		
		return index;
	}
	
	//**********************************************************************************************************************
	
	public int indexOfCanMakeRun(ArrayList<Card> peggingStack, Hand hand){
		/*
		 * Right now this method will only do a very shallow test for runs of 3
		 */
		int index = -1;
		int[] values = getValueArray(hand.getCards());
		
		int difference = peggingStack.get(0).value() - peggingStack.get(1).value();
		
		if (difference == -1){
			if (has(peggingStack.get(0).value() - 1, values))
				return valueIndex(peggingStack.get(0).value() - 1, hand.getCards());
			else if(has(peggingStack.get(0).value() + 2, values))
				return valueIndex(peggingStack.get(0).value() + 2, hand.getCards());
		}
		else if (difference == 1){
			if (has(peggingStack.get(0).value() + 1, values))
				return valueIndex(peggingStack.get(0).value() + 1, hand.getCards());
			else if(has(peggingStack.get(0).value() - 2, values))
				return valueIndex(peggingStack.get(0).value() - 2, hand.getCards());
		}
		else if (difference == 2){
			if (has(peggingStack.get(0).value() - 1, values))
				return valueIndex(peggingStack.get(0).value() - 1, hand.getCards());
		}
		else if (difference == -2){
			if (has(peggingStack.get(0).value() + 1, values))
				return valueIndex(peggingStack.get(0).value() + 1, hand.getCards());
		}
		
		return index;
	}
	
	//**********************************************************************************************************************
	
	public int getCardToLeadIndex(ArrayList<Card> cards){
		/*
		 * Ways to improve, favor playing a card you have a pair of if no low card, or addition of trapping 31
		 */
		int value = 0;
		int[] values = getValueArray(cards);
		
		
		// If has low cards, obtain one that allows summing to 15 else obtain a low card
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < values.length; i ++)
			if (values[i] < 5)
				indices.add(i);
		
		if (indices.size() > 1){
			for (int i = 0; i < indices.size() - 1; i ++){
				for (int j = i + 1; j < indices.size(); j ++)
					if (values[indices.get(i)] + values[indices.get(j)] + 10 == 15){
						return indices.get(i);
					}
			}
		}
		if (indices.size() == 1)
			return indices.get(0);
		
		
		// end obtain low card block
		
		
		// return middle cards favoring extremes followed by 10's favored by unlikelihood of a pairing 10 in the opponents hand
		if (has(9, values))		return valueIndex(9, cards);
		else if (has(6, values))		return valueIndex(6, cards);
		else if (has(8, values))		return valueIndex(8, cards);
		else if (has(7, values))		return valueIndex(7, cards);
		else if (has(13, values))	return valueIndex(13, cards);
		else if (has(12, values))	return valueIndex(12, cards);
		else if (has(10, values))	return valueIndex(10, cards);
		else if (has(11, values))	return valueIndex(11, cards);
		else 					return value;
		
	}

	public static void main(String[] args){
		Intel intel = new Intel();
		Hand hand = new Hand(); Hand discards = new Hand();
		Stack<Card> deck = new Stack<Card>();
		
		for (int s = 0; s < 4; s ++)
			for (int t = 1; t < 14; t ++)
				deck.add(new Card(t, s));
		Collections.shuffle(deck);
		
		for (int i = 0; i < 6; i ++){
			hand.add(deck.pop());
		}
		
		hand.getCards().clear();
		hand.add(new Card(12, 0));
		hand.add(new Card(11, 1));
		hand.add(new Card(3, 3));
		hand.add(new Card(5, 0));
		hand.add(new Card(9, 1));
		hand.add(new Card(4, 0));
		
		
		
		System.out.println(hand.toString());
		discards.getCards().addAll(intel.determineDiscard(hand, false));
		System.out.println(discards.toString());
		
	}



}






























