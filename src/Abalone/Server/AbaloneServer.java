package Abalone.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Abalone.*;
import Abalone.Exceptions.ClientUnavailableException;
import Abalone.Exceptions.ExitProgram;
import Abalone.protocol.ProtocolMessages;
import Abalone.protocol.ServerProtocol;

public class AbaloneServer implements ServerProtocol, Runnable {

	private ServerSocket ssock;
	private List<AbaloneClientHandler> clients;
	private List<Game> games;
	private List<String> userNames = new ArrayList<String>();

	private List<String> queueTwo = new ArrayList<String>();
	private List<String> queueThree = new ArrayList<String>();
	private List<String> queueFour = new ArrayList<String>();

	private int nextPlayerNo;
	private AbaloneServerTUI myTUI;

	private int serverSupportChatting = 0;
	private int serverSupportChallenge = 0;
	private int serverSupportLeaderboard = 0;

	private String serverName;

	public AbaloneServer() {
		clients = new ArrayList<>();
		myTUI = new AbaloneServerTUI();
		nextPlayerNo = 1;
		serverName = "Aba_lonely";
	}

	public String getServerName() {
		return serverName;
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
			return queueTwo.size();
		case 4:
			return queueFour.size();
		default:
			return 9999;
		}
	}

	public synchronized void echo(String msg) throws IOException, ClientUnavailableException {
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).sendMessage(msg);
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
		serverName = myTUI.getString("Please give a name for the server");
		while (getMoreConnections) {

			try {
				setupSocket();
				while (true) {
					Socket sock = ssock.accept();
					String name = "Player " + String.format("%02d", nextPlayerNo++);
					myTUI.showMessage("New Player [" + name + "] connected!");
					AbaloneClientHandler handler = new AbaloneClientHandler(sock, this, name);
					new Thread(handler).start();
					clients.add(handler);
					System.out.println("This many clients" + clients.size());
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
		switch (lobby) {
		case 2:
			player1Name = queueTwo.get(0);
			queueTwo.remove(0);
			player2Name = queueTwo.get(0);
			queueTwo.remove(0);
			game = new Game(2,this, player1Name, player2Name );
			games.add(game); 
			gameThread = new Thread(game);
			gameThread.start();  
			break;

		case 3:
			player1Name = queueThree.get(0);
			queueThree.remove(0);
			player2Name = queueThree.get(0);
			queueThree.remove(0);
			player3Name = queueThree.get(0);
			queueThree.remove(0);
			game = new Game(3,this, player1Name, player2Name, player3Name );
			games.add(game); 
			gameThread = new Thread(game);
			gameThread.start();  

		case 4: 
			player1Name = queueFour.get(0);
			queueFour.remove(0);
			player2Name = queueFour.get(0);
			queueFour.remove(0);
			player3Name = queueFour.get(0);
			queueFour.remove(0);
			player4Name = queueFour.get(0);
			queueFour.remove(0);
			game = new Game(4,this, player1Name, player2Name, player3Name, player4Name );
			games.add(game); 
			gameThread = new Thread(game);
			gameThread.start();  
			break;
		}
	}

	public void removeClient(AbaloneClientHandler client) {
		this.clients.remove(client);
	}

	public String getSupports() {
		return serverSupportChatting + ";" + serverSupportChallenge + ";" + serverSupportLeaderboard + ";";
	}

	public static void main(String[] args) {
		AbaloneServer server = new AbaloneServer();
		new Thread(server).start();
	}

}
