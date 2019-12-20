package Abalone;


public class Marble {
	public enum states {
		Empty, White, Black, Red, Blue, Death;
	}

	private states currentState;
	private int amountPlayers; 
	
	/**
	 * Is the constructor of the marble class
	 * Sets the state to the given state, and stores the amount of players
	 * 
	 * @param s
	 * @param i
	 * 
	 */
	public Marble(states s, int i) {
		this.currentState = s; 
		this.amountPlayers = i; 
	}
	
	public states getState() {
		return this.currentState; 
	}
	
	public void setState(states s) {
		this.currentState = s; 
	}
	
	

	
}
