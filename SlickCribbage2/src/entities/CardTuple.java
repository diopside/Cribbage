package entities;

public class CardTuple {

	/*
	 * This class will be used in determining which cards should be discarded by the computer.
	 * It will store 4 indices of cards to be kept along with their net expected value
	 *
	 */
	
	
	private int[] indices;
	private double expectedValue;
	
	public CardTuple(int i, int j, int k, int l, double eV){
		
		indices = new int[4];
		indices[0] = i; indices[1] = j; indices[2] = k; indices[3] = l;
		expectedValue = eV;
	}
	

	public double getExpectedValue(){
		return expectedValue;
	}
	public int[] getIndices(){
		return indices;
	}
	
}
