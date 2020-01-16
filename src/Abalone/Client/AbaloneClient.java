package Abalone.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import Abalone.Board;
import Abalone.Marble;
import Abalone.Exceptions.ExitProgram;
import Abalone.Exceptions.ProtocolException;
import Abalone.Exceptions.ServerUnavailableException;
import Abalone.protocol.*;

public class AbaloneClient implements ClientProtocol {
	private Socket sock;
	private BufferedReader networkIN;
	private BufferedWriter networkOUT;
	private AbaloneClientTUI clientTui;
	private String name;
	private Marble color;
	private boolean clientSupportChatting = false;
	private boolean clientSupportChallenge = false;
	private boolean clientSupportLeaderboard = false;
	private boolean serverSupportChatting = false;
	private boolean serverSupportChallenge = false;
	private boolean serverSupportLeaderboard = false;
	private boolean handshakeComplete = false;
	private boolean joiningComplete = false;
	private int gameSize = 0;
	private boolean gameStarted = false;
	private boolean yourTurn = false;
	Board clientBoard;
	String[] gamePlayers;

	public static void main(String args[]) {
		AbaloneClient client = new AbaloneClient();
		client.start();
	}

	public String getName() {
		return name;
	}

	public AbaloneClient() {
		this.clientTui = new AbaloneClientTUI(this);
		handshakeComplete = false;
		joiningComplete = false;
		gameSize = 0;

	}

	public void start() {
		clientTui.showMessage("Welcome to the Abalone Client program");
		name = clientTui.getString("Please give your wanted user name");
		clientTui.showMessage("Welcome " + name + " we will now setup the connection..");
		try {
			createConnection();
			handleHandshake(clientSupportChatting, clientSupportChallenge, clientSupportLeaderboard, name);
			gameSize = clientTui.getInt("How many player game would you like to join?", 2, 4);
			joinQueue(gameSize);
			Thread threadTUI = new Thread(clientTui);
			threadTUI.start();
			readServer();

		} catch (ExitProgram | ServerUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createConnection() throws ExitProgram {
		clearConnection();
		clearConnection();
		while (sock == null) {
			InetAddress host = null;
			int port = clientTui.getInt("Please enter the port");
			host = clientTui.getIp();
			// try to open a Socket to the server
			try {
				// InetAddress addr = InetAddress.getByName(host);
				System.out.println("Attempting to connect to " + host + ":" + port + "...");
				sock = new Socket(host, port);
				networkIN = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				networkOUT = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			} catch (IOException e) {
				System.out.println("ERROR: could not create a socket on " + host + " and port " + port + ".");

				// Do you want to try again? (ask user, to be implemented)
				if (false) {
					throw new ExitProgram("User indicated to exit.");
				}
			}
		}
	}

	public synchronized void sendMessage(String message) throws ServerUnavailableException {
		if (networkOUT != null) {
			try {
				networkOUT.write(message);
				networkOUT.newLine();
				networkOUT.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new ServerUnavailableException("Could not write to server");
			}
		} else {
			throw new ServerUnavailableException("Server not yet made");
		}

	}

	public String readLineFromServer() throws ServerUnavailableException {
		if (networkIN != null) {
			try {
				// Read and return answer from Server
				String answer = networkIN.readLine();
				if (answer == null) {
					throw new ServerUnavailableException("Could not read " + "from server.");
				}

				return answer;
			} catch (IOException e) {
				throw new ServerUnavailableException("Could not read " + "from server.");
			}
		} else {
			throw new ServerUnavailableException("Could not read " + "from server.");
		}
	}

	public void closeConnection() {
		System.out.println("Closing the connection...");
		try {
			networkIN.close();
			networkIN.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearConnection() {
		sock = null;
		networkIN = null;
		networkOUT = null;
	}

	public void handleServerCommands(String msg) {
		String command = msg.substring(0, 1);
		String[] inputSrv = msg.split(";");
		// getting commands from the server
		switch (command) {
		case ProtocolMessages.HELLO:

			serverSupportChatting = Integer.parseInt(inputSrv[1]) == 1 ? true : false;
			serverSupportChallenge = Integer.parseInt(inputSrv[2]) == 1 ? true : false;
			serverSupportLeaderboard = Integer.parseInt(inputSrv[3]) == 1 ? true : false;
			this.name = inputSrv[4];
			clientTui.showMessage("The server has assigned you as the following name: " + name);
			handshakeComplete = true;
			break;

		case ProtocolMessages.JOIN:
			if (Integer.parseInt(inputSrv[1]) == gameSize) {
				// this message is for you
				if (joiningComplete == false) {
					clientTui.showMessage("You succesfully joined");
					clientTui.showMessage("there are " + inputSrv[2] + "out of " + inputSrv[1] + " joined");
					joiningComplete = true;
				} else {
					clientTui.showMessage("new Player joined queue , now " + inputSrv[2] + "out of " + inputSrv[1]);
				}

			}
			break;

		case ProtocolMessages.GAME_START:
			gameStarted = true;
			// only gets this message if the server sends it to me
			for (int i = 0; i < inputSrv.length; i++) {
				if (inputSrv[i].equals(name)) {
					if (inputSrv.length == 4) {
						gamePlayers = new String[2];
						gamePlayers[0] = inputSrv[1];
						gamePlayers[1] = inputSrv[2];
						clientTui.showMessage("The game has started with the following players: " + gamePlayers[0]
								+ " and " + gamePlayers[1]);
						clientTui.showMessage("The game will be played in this order");
						if (gamePlayers[0].equals(name)) {
							clientTui.showMessage(
									"this is you, go enter your move, remember typing h will print the help menu");
							yourTurn = true;

						}
						setPropperColor();

						clientBoard = new Board(2);
						showBoard();
					}
					if (inputSrv.length == 5) {
						gamePlayers = new String[3];
						gamePlayers[0] = inputSrv[1];
						gamePlayers[1] = inputSrv[2];
						gamePlayers[2] = inputSrv[3];
						clientTui.showMessage("The game has started with the following players: " + gamePlayers[0]
								+ " and " + gamePlayers[1] + " and " + gamePlayers[2]);
						clientTui.showMessage("The game will be played in this order");
						if (gamePlayers[0].equals(name)) {
							clientTui.showMessage(
									"this is you, go enter your move, remember typing h will print the help menu");
							yourTurn = true;

						}
						setPropperColor();
						clientBoard = new Board(3);
						showBoard();

					}
					if (inputSrv.length == 6) {
						gamePlayers = new String[4];
						gamePlayers[0] = inputSrv[1];
						gamePlayers[1] = inputSrv[2];
						gamePlayers[2] = inputSrv[3];
						gamePlayers[3] = inputSrv[4];
						clientTui.showMessage("The game has started with the following players: " + gamePlayers[0]
								+ " and " + gamePlayers[1] + " and " + gamePlayers[2] + " and " + gamePlayers[3]);
						clientTui.showMessage("The game will be played in this order");
						if (gamePlayers[0].equals(name)) {
							clientTui.showMessage(
									"this is you, go enter your move, remember typing h will print the help menu");
							yourTurn = true;
						}
						setPropperColor();
						clientBoard = new Board(4);
						showBoard();
					}
				}
			}

			break;

		case ProtocolMessages.MOVE:
			yourTurn = false;
			clientTui.showMessage(
					"Player " + inputSrv[2] + "has moved " + "\n it is now the turn of player: " + inputSrv[1]);
			String direction = inputSrv[3];
			ArrayList<Integer> indexes = new ArrayList<>();
			for (int i = 3; i < inputSrv.length; i++) {
				if (!inputSrv[i].equals("*")) {
					indexes.add(Integer.parseInt(inputSrv[i]));
				}

			}
			updateBoard(indexes, direction);
			showBoard();
			if (inputSrv[1].equals(name)) {
				clientTui.showMessage("it is now your turn, enter your move");
				yourTurn = true;
			}
			break;
		case ProtocolMessages.GAME_FINISHED:
			break;
		case ProtocolMessages.QUEUE_SIZE:
			clientTui.showMessage("the queue for 2 players is : " + inputSrv[1] + "\n" + "The queue for 3 players is : "
					+ inputSrv[2] + "\n" + "the queue for 4 players is : " + inputSrv[3]);
			break;
		case ProtocolMessages.EXIT:
			break;
		default:
			break;
		}
	}

	public void setPropperColor() {
		switch (gamePlayers.length) {
		case 2:
			if (gamePlayers[0].equals(name)) {
				color = Marble.Black;
			} else {
				color = Marble.White;
			}
			break;

		case 3:
			if (gamePlayers[0].equals(name)) {
				color = Marble.Black;
			} else if (gamePlayers[1].equals(name)) {
				color = Marble.Green;
			} else {
				color = Marble.Red;
			}
			break;

		case 4:
			if (gamePlayers[0].equals(name)) {
				color = Marble.Black;
			} else if (gamePlayers[1].equals(name)) {
				color = Marble.Green;
			} else if (gamePlayers[2].equals(name)) {
				color = Marble.White;
			} else {
				color = Marble.Red;
			}
			break;
		}
	}

	public Marble getPlayerMarble(String name) {
		switch (gameSize) {
		case 2: 
			if (gamePlayers[0].equals(name)) {
				return Marble.Black;
			} else
				return Marble.White;

		case 3:
			if (gamePlayers[0].equals(name)) {
				return Marble.Black;
			} else if (gamePlayers[1].equals(name)) {
				return Marble.Green;
			} else
				return Marble.Red;

		case 4:
			if (gamePlayers[0].equals(name)) {
				return Marble.Black;
			} else if (gamePlayers[1].equals(name)) {
				return Marble.Green;
			} else if (gamePlayers[2].equals(name)) {
				return Marble.White;
			}else {return Marble.Red;}
		
		}
		return Marble.Death;
	}

	public void readServer() throws ServerUnavailableException {
		while (true) {

			String serverMessage = readLineFromServer();
			handleServerCommands(serverMessage);
			// needs to keep reading the server messages
		}
	}

	public void showBoard() {
		clientTui.showMessage(clientBoard.toString());
	}

	public void updateBoard(ArrayList<Integer> indices, String direction) {

		// clientBoard.mo
	}

//---------------------- protocol messages to send down below
	@Override
	public void handleHandshake(boolean chat, boolean challenge, boolean leaderboard, String playerName)
			throws ServerUnavailableException {
		// TODO Auto-generated method stub
		int chatInt = chat ? 1 : 0;
		int challengeInt = challenge ? 1 : 0;
		int leaderboardInt = leaderboard ? 1 : 0;
		sendMessage(ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + chatInt + ProtocolMessages.DELIMITER
				+ challengeInt + ProtocolMessages.DELIMITER + leaderboardInt + ProtocolMessages.DELIMITER + name
				+ ProtocolMessages.EOC);
		while (!handshakeComplete) {
			String serverMessage = readLineFromServer();
			handleServerCommands(serverMessage);
		}
	}

	@Override
	public void joinQueue(int gamesize) throws ServerUnavailableException {
		// TODO Auto-generated method stub

		sendMessage(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + gamesize + ProtocolMessages.EOC);
		while (!joiningComplete) {
			clientTui.showMessage("Waiting for join conformaton");
			String serverMessage = readLineFromServer();
			handleServerCommands(serverMessage);
		}
	}

	@Override
	public void sendMove(String playerName, String direction, int[] marbleIndices) throws ServerUnavailableException {
		// TODO Auto-generated method stub
		if (yourTurn) {
			String marblesString = "";
			for (int i = 0; i < marbleIndices.length; i++) {
				marblesString = marblesString + ProtocolMessages.DELIMITER;
				marblesString = marblesString + marbleIndices[i];
			}
			sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + playerName + marblesString
					+ ProtocolMessages.EOC);
		} else {
			clientTui.showMessage("It is not your turn, please wait");
		}

	}

	@Override
	public void getCurrentQueueSizes() throws ServerUnavailableException {
		// TODO Auto-generated method stub
		sendMessage(ProtocolMessages.QUEUE_SIZE + ProtocolMessages.EOC);
	}

	@Override
	public void sendExit() throws ServerUnavailableException {
		sendMessage(ProtocolMessages.EXIT + ProtocolMessages.EOC);
		// TODO Auto-generated method stub

	}

}
