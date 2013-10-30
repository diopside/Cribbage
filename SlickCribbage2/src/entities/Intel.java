package entities;

import java.util.ArrayList;
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
		 
		// end runs
		
		if (isPlayer){
			stats.addFifteenPoints(fifteens * 2);
			stats.addFlushPoints(flushPoints);
			stats.addNobPoints(nobs);
			stats.addPairPoints(numPairs * 2);
			stats.addRunPoints(runPoints);
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
		int index = 0;
		
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

	public static void main(String[] args){
		Hand hand = new Hand();
		ArrayList<Card> deck = new ArrayList<Card>();
		HandResult hr = new HandResult();
		Intel intel = new Intel();
		
		for (int s = 0; s < 4; s ++){
			for (int c = 1; c < 14; c ++){
				deck.add(new Card(c, s));
			}
		}
		

		for (int i = 1; i < 14; i ++){
			
			double points = 0;
			for (int t = 0; t < 5000; t ++){
				Collections.shuffle(deck);
				hand.add(new Card(i, 0));
				hand.add(new Card(i, 1));
				hand.add(new Card(deck.get(0).value(), deck.get(0).suit()));
				hand.add(new Card(deck.get(1).value(), deck.get(1).suit()));
				hand.add(new Card(deck.get(2).value(), deck.get(2).suit()));
				
				System.out.println(hand.toString());
				
				hr = intel.count(hand, false, null, false);
				points += hr.getPoints() /5000.0;
				
				hand.getCards().clear();
			}
			
			System.out.println(points);
			
			break;
			
		}


	}



}
