package Abalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Abalone.Exceptions.ClientUnavailableException;
import Abalone.Server.AbaloneServer;
import Abalone.protocol.ProtocolMessages;

public class Game implements Runnable {

	private Board board;
	private String[] playerNames;
	private AbaloneServer srv;
	private int moves;
	private static final int maximumMoves = 96;

	HashMap<String, MoveCheck> checkmap;

	public Game(int Amountplayers, AbaloneServer srv, String player1Name, String player2Name) {
		board = new Board(Amountplayers);
		playerNames = new String[2];
		playerNames[0] = player1Name;
		playerNames[1] = player2Name;
		checkmap = new HashMap<String, MoveCheck>();
		checkmap.put(player1Name, new MoveCheck(Marble.Black, board));
		checkmap.put(player2Name, new MoveCheck(Marble.White, board));

		this.srv = srv;
	}

	public Game(int players, AbaloneServer srv, String player1Name, String player2Name, String player3Name) {
		board = new Board(players);
		playerNames = new String[3];
		playerNames[0] = player1Name;
		playerNames[1] = player2Name;
		playerNames[2] = player3Name;

		checkmap = new HashMap<String, MoveCheck>();
		checkmap.put(player1Name, new MoveCheck(Marble.Black, board));
		checkmap.put(player2Name, new MoveCheck(Marble.Green, board));
		checkmap.put(player3Name, new MoveCheck(Marble.White, board));
		this.srv = srv;
	}

	public Game(int players, AbaloneServer srv, String player1Name, String player2Name, String player3Name,
			String player4Name) {
		board = new Board(players);
		playerNames = new String[4];
		playerNames[0] = player1Name;
		playerNames[1] = player2Name;
		playerNames[2] = player3Name;
		playerNames[3] = player4Name;

		checkmap = new HashMap<String, MoveCheck>();
		checkmap.put(player1Name, new MoveCheck(Marble.Black, board));
		checkmap.put(player2Name, new MoveCheck(Marble.Green, board));
		checkmap.put(player3Name, new MoveCheck(Marble.White, board));
		checkmap.put(player4Name, new MoveCheck(Marble.Red, board));
		this.srv = srv;
	}

	public void run() {
		boolean continueGame = true;
		while (continueGame) {
			reset();
			try {
				play();
			} catch (IOException | ClientUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			continueGame = false;
//			System.out.println("\n> Play another time? (y/n)?");
//			continueGame = TextIO.getBoolean();
		}
	}

	public void reset() {
		board.reset();
	};

	public void play() throws IOException, ClientUnavailableException {
		// update();
		while (moves < maximumMoves) {

		}
		srv.multipleSend("end of game", playerNames);
		// update();
	}

	public void addMove(String name, String direction, ArrayList indexes)
			throws IOException, ClientUnavailableException {

		// first check if the player has the right to move
		ArrayList<Integer> newIndexes = new ArrayList<>();
		ArrayList<Integer> totalMove = new ArrayList<>();
		ArrayList<Integer> totalMoveToProtocol = new ArrayList<Integer>();
		if (name.equals(getNextPlayer())) { 
			newIndexes = board.protocolToIndex(indexes);
			System.out.println("this is the next Indexes value: "+ newIndexes);
			totalMove = checkmap.get(name).moveChecker(newIndexes, direction); 
			System.out.println("this is the totalmove value: "+ totalMove);
			if (totalMove.size() > 5) {
				// something went wrong it is not good

			}
			boolean scores = board.move(totalMove, direction);
			if (scores) {
				board.addScore(checkmap.get(name).getColor());
			}
			
			moves++;
			totalMoveToProtocol = board.indexToProtocol(totalMove);
			String Nextplayer = getNextPlayer();
			String message = srv.handlePlayerMove(Nextplayer);
			String indexesString = "";

			for (int i : totalMoveToProtocol) {
				indexesString = indexesString + ProtocolMessages.DELIMITER;
				indexesString = indexesString + i;
			}
			message = message + name + ProtocolMessages.DELIMITER + direction + indexesString
					+ ProtocolMessages.EOC;
			srv.multipleSend(message, playerNames);

		}

	}

	public String getNextPlayer() {
		while (moves >= playerNames.length) {
			moves = moves - playerNames.length;
		}
		return playerNames[moves];

	}

	public void update() {
		// System.out.println(board.toString());
	}

}
