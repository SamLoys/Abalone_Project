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
	boolean finished = false;
	private static final int maximumMoves = 96;

	HashMap<String, MoveCheck> checkmap;
	HashMap<String, Marble> marbleMap;

	public Game(int Amountplayers, AbaloneServer srv, String player1Name, String player2Name) {
		board = new Board(Amountplayers);
		playerNames = new String[2];
		playerNames[0] = player1Name;
		playerNames[1] = player2Name;
		checkmap = new HashMap<String, MoveCheck>();
		checkmap.put(player1Name, new MoveCheck(Marble.Black, board));
		checkmap.put(player2Name, new MoveCheck(Marble.White, board));

		marbleMap = new HashMap<String, Marble>();
		marbleMap.put(player1Name, Marble.Black);
		marbleMap.put(player2Name, Marble.White);

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

		marbleMap = new HashMap<String, Marble>();
		marbleMap.put(player1Name, Marble.Black);
		marbleMap.put(player2Name, Marble.Green);
		marbleMap.put(player3Name, Marble.White);
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

		marbleMap = new HashMap<String, Marble>();
		marbleMap.put(player1Name, Marble.Black);
		marbleMap.put(player2Name, Marble.Green);
		marbleMap.put(player3Name, Marble.White);
		marbleMap.put(player4Name, Marble.Red);
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
		int scoreteam1;
		int scoreteam2;
		int scoreteam3;
		while (moves < maximumMoves && finished == false) {
		
			switch (playerNames.length) {

			case 2:
				scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
				scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
				if (scoreteam1 >= 6) {
					String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
							+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
					finished= true;
					srv.multipleSend(msg, playerNames);
					srv.removeGame(this);
					// player 1 wins
				} else if (scoreteam2 >= 6) {
					String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
							+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
					finished= true;
					srv.multipleSend(msg, playerNames);
					srv.removeGame(this);
					// player 2 wins
				}
				break;
			case 3:
				scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
				scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
				scoreteam3 = board.getScore(marbleMap.get(playerNames[2]));
				if (scoreteam1 >= 6) {
					String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
							+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
					finished= true;
					srv.multipleSend(msg, playerNames);
					srv.removeGame(this);
					// player 1 wins
				} else if (scoreteam2 >= 6) {
					String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
							+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
					finished= true;
					srv.multipleSend(msg, playerNames);
					srv.removeGame(this);
					// player 2 wins
				} else if (scoreteam3 >= 6) {
					String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
							+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
					finished= true;
					srv.multipleSend(msg, playerNames);
					srv.removeGame(this);
					// player 2 wins
				}
				break;
			case 4:
				scoreteam1 = board.getScore(marbleMap.get(playerNames[0])) + board.getScore(marbleMap.get(playerNames[2]));
				scoreteam2 = board.getScore(marbleMap.get(playerNames[1])) + board.getScore(marbleMap.get(playerNames[3]));
				if (scoreteam1 >= 6) {
					String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
							+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
					finished= true;
					srv.multipleSend(msg, playerNames);
					srv.removeGame(this);
					// player 1 wins
				} else if (scoreteam2 >= 6) {
					String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
							+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
					finished= true;
					srv.multipleSend(msg, playerNames);
					srv.removeGame(this);
					// player 2 wins
				} 
				break;

			default:
				break;
			}
		}
	
		switch (playerNames.length) {
		case 2:
			scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
			scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
			
			if (scoreteam1 == scoreteam2) {
				// draw
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);

			} else if (scoreteam1 > scoreteam2) {
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);
				// player 1 wins
			} else if (scoreteam1 < scoreteam2) {
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);
				// player 2 wins
			}
			break;

		case 3:
			scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
			scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
			scoreteam3 = board.getScore(marbleMap.get(playerNames[2]));

			if (scoreteam1 == scoreteam2 && scoreteam1 == scoreteam3) {
				// draw
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);

			} else if (scoreteam1 > scoreteam2 && scoreteam1 > scoreteam3) {
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);
				// player 1 wins
			} else if (scoreteam2 > scoreteam1 && scoreteam2 > scoreteam3) {
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);
				// player 2 wins
			} else if (scoreteam3 > scoreteam1 && scoreteam3 > scoreteam2) {

				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "3" + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);
			} else {
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);
			}
			break;

		case 4:
			scoreteam1 = board.getScore(marbleMap.get(playerNames[0])) + board.getScore(marbleMap.get(playerNames[2]));
			scoreteam2 = board.getScore(marbleMap.get(playerNames[1])) + board.getScore(marbleMap.get(playerNames[3]));

			if (scoreteam1 == scoreteam2) {
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);

			}

			if (scoreteam1 < scoreteam2) {
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);
			}
			if (scoreteam2 < scoreteam1) {
				String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
						+ ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
				finished= true;
				srv.multipleSend(msg, playerNames);
				srv.removeGame(this);
			}
			break;

		default:
			break;
		}
		

	}

	public void addMove(String name, String direction, ArrayList indexes)
			throws IOException, ClientUnavailableException {

		// first check if the player has the right to move
		ArrayList<Integer> newIndexes = new ArrayList<>();
		ArrayList<Integer> totalMove = new ArrayList<>();
		ArrayList<Integer> totalMoveToProtocol = new ArrayList<Integer>();

		if (name.equals(getNextPlayer()) && finished== false) {
			newIndexes = board.protocolToIndex(indexes);
			// System.out.println("this is the next Indexes value: "+ newIndexes);
			totalMove = checkmap.get(name).moveChecker(newIndexes, direction);
			// System.out.println("this is the totalmove value: "+ totalMove);
			if (totalMove.size() == 0) {
				// something went wrong it is not good

			}
			boolean scores = board.move(totalMove, direction);
			if (scores) {
				board.addScore(marbleMap.get(name));
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
			message = message + name + ProtocolMessages.DELIMITER + direction + indexesString + ProtocolMessages.EOC;
			srv.multipleSend(message, playerNames);

		}

	}

	public String getNextPlayer() {
		while (moves >= playerNames.length) {
			moves = moves - playerNames.length;
		}
		return playerNames[moves];

	}

}
