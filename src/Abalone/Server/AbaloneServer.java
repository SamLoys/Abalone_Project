package Abalone.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Abalone.*;
import Abalone.Exceptions.ClientUnavailableException;
import Abalone.Exceptions.ExitProgram;
import Abalone.protocol.ProtocolMessages;
import Abalone.protocol.ServerProtocol;

public class AbaloneServer implements ServerProtocol, Runnable {

	private ServerSocket ssock;

	private List<Game> games = new ArrayList<Game>();
	private List<String> userNames = new ArrayList<String>();

	HashMap<String, AbaloneClientHandler> clientsMap = new HashMap<>();

	private List<String> queueTwo = new ArrayList<String>();
	private List<String> queueThree = new ArrayList<String>();
	private List<String> queueFour = new ArrayList<String>();

	private int nextPlayerNo;
	private AbaloneServerTUI myTUI;

	private boolean serverSupportChatting = false;
	private boolean serverSupportChallenge = false;
	private boolean serverSupportLeaderboard = false;

	private String serverName;

	public AbaloneServer() {

		myTUI = new AbaloneServerTUI();
		nextPlayerNo = 1;
		serverName = "Aba_lonely";
	}

	public String getServerName() {
		return serverName;
	}

	public AbaloneClientHandler getClientHandler(String name) {
		if (clientsMap.containsKey(name)) {
			return clientsMap.get(name);
		}
		return null;

	}

	public void setClientHandlerToName(String name, AbaloneClientHandler handler) {
		clientsMap.put(name, handler);
	}

	public synchronized void addToQueue(String name, int lobby) {
		switch (lobby) {
		case 2:
			queueTwo.add(name);
			break;
		case 3:
			queueThree.add(name);
			break;
		case 4:
			queueFour.add(name);
			break;
		}

	}

	public synchronized boolean queueFull(int lobby) {
		switch (lobby) {
		case 2:
			if (queueTwo.size() >= 2) {
				return true;
			}
			return false;

		case 3:
			if (queueThree.size() >= 3) {
				return true;
			}
			return false;
		case 4:
			if (queueFour.size() >= 4) {
				return true;
			}
			return false;
		default:
			return false;
		}
	}

	public int getQueueSize(int lobby) {
		switch (lobby) {
		case 2:
			return queueTwo.size();
		case 3:
			return queueThree.size();
		case 4:
			return queueFour.size();
		default:
			return 9999;
		}
	}

	public synchronized void echo(String msg) throws IOException, ClientUnavailableException {
		for (String name : clientsMap.keySet()) {
			clientsMap.get(name).sendMessage(msg);
		}
	}

	public synchronized void multipleSend(String msg, String[] players) throws IOException, ClientUnavailableException {
		for (String name : players) {
			clientsMap.get(name).sendMessage(msg);
		}
	}

	public synchronized String setUserName(String wantedName) {
		int nameCounter = 1;
		while (userNames.contains(wantedName)) {

			wantedName = wantedName + nameCounter++;
		}
		userNames.add(wantedName);
		return wantedName;

	}

	public void run() {
		// indicates if the server should connect more connection
		boolean getMoreConnections = true;
		myTUI.showMessage("Welcome to the server program of abalone");
		while (getMoreConnections) {
 
			try {
				setupSocket();
				while (true) {
					Socket sock = ssock.accept();
					String name = "Player " + String.format("%02d", nextPlayerNo++);
					myTUI.showMessage("New Player [" + name + "] connected!");
					AbaloneClientHandler handler = new AbaloneClientHandler(sock, this, name);
					new Thread(handler).start();

				}
			} catch (ExitProgram e) {
				e.printStackTrace();
				getMoreConnections = false;
			} catch (IOException e) {
				System.out.println("A server IO error occurred: " + e.getMessage());

				if (!myTUI.getBool("Do you want to open a new socket?")) {
					getMoreConnections = false;
				}
			}
		}
	}

	public void setupSocket() throws ExitProgram {
		ssock = null;
		while (ssock == null) {
			int port = myTUI.getInt("Please give the wanted port number");
			InetAddress IP = null;
			// try to open a new ServerSocket
			try {
				IP = InetAddress.getLocalHost();
				myTUI.showMessage("Attempting to open a socket at " + IP + "on port " + port + "...");

				ssock = new ServerSocket(port, 0, IP);

				myTUI.showMessage("Server started on IP " + IP);
				myTUI.showMessage("Server started at port " + port);
				myTUI.showMessage("Waiting for new players");
			} catch (IOException e) {
				myTUI.showMessage("ERROR: could not create a socket on " + IP + " and port " + port + ".");

				if (!myTUI.getBool("Do you want to try again?")) {
					throw new ExitProgram("User indicated to exit the " + "program.");
				}
			}
		}

	}

	public void setupGame(int lobby) {
		String player1Name = "";
		String player2Name = "";
		String player3Name = "";
		String player4Name = "";
		Game game;
		Thread gameThread;
		String[] Players;
		switch (lobby) {
		case 2:
			player1Name = queueTwo.get(0);
			queueTwo.remove(0);
			player2Name = queueTwo.get(0);
			queueTwo.remove(0);
			game = new Game(2, this, player1Name, player2Name);
			getClientHandler(player1Name).addGame(game);
			getClientHandler(player1Name).setColor(Marble.Black);
			getClientHandler(player2Name).addGame(game);
			getClientHandler(player2Name).setColor(Marble.White);
			games.add(game);

//			gameThread = new Thread(game);
//			gameThread.start();

			// construct an array with names to send to all the clients
			Players = new String[2];
			Players[0] = player1Name;
			Players[1] = player2Name;
			handleGameStart(Players);
			break;

		case 3:
			player1Name = queueThree.get(0);
			queueThree.remove(0);
			player2Name = queueThree.get(0);
			queueThree.remove(0);
			player3Name = queueThree.get(0);
			queueThree.remove(0);
			game = new Game(3, this, player1Name, player2Name, player3Name);
			getClientHandler(player1Name).addGame(game);
			getClientHandler(player1Name).setColor(Marble.Black);
			getClientHandler(player2Name).addGame(game);
			getClientHandler(player2Name).setColor(Marble.Green);
			getClientHandler(player3Name).addGame(game);
			getClientHandler(player3Name).setColor(Marble.White);
			games.add(game);

			// construct an array with names to send to all the clients
			Players = new String[3];
			Players[0] = player1Name;
			Players[1] = player2Name;
			Players[2] = player3Name;
			handleGameStart(Players);

//			gameThread = new Thread(game);
//			gameThread.start();
			break;

		case 4:
			player1Name = queueFour.get(0);
			queueFour.remove(0);
			player2Name = queueFour.get(0);
			queueFour.remove(0);
			player3Name = queueFour.get(0);
			queueFour.remove(0);
			player4Name = queueFour.get(0);
			queueFour.remove(0);
			game = new Game(4, this, player1Name, player2Name, player3Name, player4Name);
			getClientHandler(player1Name).addGame(game);
			getClientHandler(player1Name).setColor(Marble.Black);
			getClientHandler(player2Name).addGame(game);
			getClientHandler(player2Name).setColor(Marble.Green);
			getClientHandler(player3Name).addGame(game);
			getClientHandler(player3Name).setColor(Marble.White);
			getClientHandler(player4Name).addGame(game);
			getClientHandler(player4Name).setColor(Marble.Red);
			games.add(game);

			// construct an array with names to send to all the clients
			Players = new String[4];
			Players[0] = player1Name;
			Players[1] = player2Name;
			Players[2] = player3Name;
			Players[3] = player4Name;
			handleGameStart(Players);

//			gameThread = new Thread(game);
//			gameThread.start();
			break;
		}
	}

	public void removeGame(Game game) {
		this.games.remove(game); 
	}

	public void removeClient(String name) { 
		clientsMap.remove(name);
		try {
			userNames.remove(name); 
			queueFour.remove(name); 
			queueThree.remove(name);
			queueTwo.remove(name);
		}
		catch(Exception e) {
			System.out.println("error removing client name");
		}
		
	}

	public boolean[] getSupports() {
		boolean[] supports = new boolean[3];
		supports[0] = serverSupportChatting;
		supports[1] = serverSupportChallenge;
		supports[2] = serverSupportLeaderboard;

		return supports;

	}

	public static void main(String[] args) {
		AbaloneServer server = new AbaloneServer();
		new Thread(server).start();
	}

	@Override
	public synchronized String handleHello(String playerName, boolean chat, boolean challenge, boolean leaderboard) {
		String actualName = setUserName(playerName);
		int chatInt = chat ? 1 : 0;
		int challengeInt = challenge ? 1 : 0;
		int leaderboardInt = leaderboard ? 1 : 0;

		String response = ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + chatInt + ProtocolMessages.DELIMITER
				+ challengeInt + ProtocolMessages.DELIMITER + leaderboardInt + ProtocolMessages.DELIMITER + actualName
				+ ProtocolMessages.EOC;
		return response;
	}

	@Override
	public String handleJoin(String playerName, int gamesize) {
		addToQueue(playerName, gamesize);
		String response = ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + gamesize + ProtocolMessages.DELIMITER
				+ getQueueSize(gamesize) + ProtocolMessages.EOC;

		return response;
	}

	@Override
	public String handleGameStart(String[] playerNames) {

		String names = null;

		for (int i = 0; i < playerNames.length; i++) {
			names = names + ProtocolMessages.DELIMITER;
			names = names + playerNames[i];

		}
		String message = ProtocolMessages.GAME_START + names + ProtocolMessages.EOC;
		try {
			multipleSend(message, playerNames);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClientUnavailableException e) {
			e.printStackTrace();
		}
		// add games to the
		// echo the game is going to start
		return message;
	}

	@Override
	public String handlePlayerMove(String playerName) {
		// TODO Auto-generated method stub
		String nextplayer = playerName;
		String message = ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + nextplayer + ProtocolMessages.DELIMITER;
		return message;
	}

	@Override
	public String handleQueueSizeQuery() {
		String message = ProtocolMessages.QUEUE_SIZE + ProtocolMessages.DELIMITER + getQueueSize(2)
				+ ProtocolMessages.DELIMITER + getQueueSize(3) + ProtocolMessages.DELIMITER + getQueueSize(4)
				+ ProtocolMessages.EOC;

		// TODO Auto-generated method stub
		return message;
	}

}
