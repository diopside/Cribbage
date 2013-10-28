package entities;

public class HandResult {


	private String message;
	private int points;
	
	public HandResult(){
		points = 0;
		message = "";
	}
	
	public HandResult(String s, int points){
		this.points = points;
		message = s;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
