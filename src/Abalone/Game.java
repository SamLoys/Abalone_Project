package Abalone;

import Abalone.Server.AbaloneServer;

public class Game implements Runnable{
	
	private Board board; 
	
	
	public Game(int players, AbaloneServer srv, String Player1Name, String player2Name ) {
		board = new Board(players);
	}
	
	public Game(int players, AbaloneServer srv, String Player1Name, String player2Name, String player3Name ) {
		board = new Board(players);
	}
	
	public Game(int players, AbaloneServer srv, String Player1Name, String player2Name, String player3Name, String player4name ) {
		board = new Board(players);
	}
	
	
	public void run() {
		boolean continueGame = true;
		while (continueGame) {
			reset();
			play();
			continueGame =false;
//			System.out.println("\n> Play another time? (y/n)?");
//			continueGame = TextIO.getBoolean();
		}
	}
	
	public void reset() {
		board.reset();
	};
	
	public void play() {
		update();
		board.move(81, 82, Directions.east);
		update();
	}
	
	public void update(){
		System.out.println(board.toString());
	}


	
}
