package Abalone;

import java.io.IOException;
import java.util.ArrayList;

import Abalone.Exceptions.ClientUnavailableException;
import Abalone.Server.AbaloneServer;

public class Game implements Runnable{
	
	private Board board; 
	private String[] playerNames; 
	private AbaloneServer srv;
	private int moves; 
	private static final int maximumMoves = 96; 
	
	
	public Game(int Amountplayers, AbaloneServer srv, String player1Name, String player2Name ) {
		board = new Board(Amountplayers);  
		playerNames = new String[2];
		playerNames[0] = player1Name;
		playerNames[1] = player2Name; 
		this.srv = srv;
	}
	
	public Game(int players, AbaloneServer srv, String player1Name, String player2Name, String player3Name ) {
		board = new Board(players);
		playerNames = new String[3];
		playerNames[0] = player1Name;
		playerNames[1] = player2Name; 
		playerNames[2] = player3Name;
		this.srv = srv;
	}
	
	public Game(int players, AbaloneServer srv, String player1Name, String player2Name, String player3Name, String player4Name ) {
		board = new Board(players);
		playerNames = new String[4];
		playerNames[0] = player1Name;
		playerNames[1] = player2Name; 
		playerNames[2] = player3Name;
		playerNames[3] = player4Name;
		this.srv = srv;
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
		//update();
		while (moves < maximumMoves) {
			
			
	
		}
		srv.multipleSend("end of game", playerNames);
		//update();
	}
	
	public void addMove(String name, String direction, ArrayList indexes) {
		//check the move
		//if valid
		//srv.send multiple the move message
		String next = getNextPlayer();
		String message = srv.handlePlayerMove(next);
		srv.multipleSend(message, playerNames); 
		moves++; 
	}
	
	public String getNextPlayer() {
		if (playerNames.length == 2) {
			
		}
	}
	
	public void update(){
		//System.out.println(board.toString());
	}


	
}
