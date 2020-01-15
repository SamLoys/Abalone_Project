package Abalone.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

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
	private int clientSupportChatting = 0;
	private int clientSupportChallenge = 0;
	private int clientSupportLeaderboard = 0;
	private int serverSupportChatting = 0;
	private int serverSupportChallenge = 0;
	private int serverSupportLeaderboard = 0;
	private boolean handshakeComplete = false;
	private boolean joiningComplete = false;
	private int gameSize = 0;
	private boolean gameStarted = false;

	public static void main(String args[]) {
		AbaloneClient client = new AbaloneClient();
		client.start();
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
			sendHello();
			sendJoin();
			Thread threadTUI = new Thread(clientTui);
			threadTUI.start();
			readServer();

		} catch (ExitProgram | ServerUnavailableException | ProtocolException e) {
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

	public void sendHello() throws ServerUnavailableException, ProtocolException {
		sendMessage(ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + clientSupportChatting
				+ ProtocolMessages.DELIMITER + clientSupportChallenge + ProtocolMessages.DELIMITER
				+ clientSupportLeaderboard + ProtocolMessages.DELIMITER + name + ProtocolMessages.EOC);
		while (!handshakeComplete) {
			String serverMessage = readLineFromServer();
			handleServerCommands(serverMessage);
		}
	}

	public void sendJoin() throws ServerUnavailableException {
		gameSize = clientTui.getInt("How many player game would you like to join?", 2, 4);
		sendMessage(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + gameSize + ProtocolMessages.EOC);
		while (!joiningComplete) {
			clientTui.showMessage("Waiting for join conformaton");
			String serverMessage = readLineFromServer();
			handleServerCommands(serverMessage);
		}

	}

	public void handleServerCommands(String msg) {
		String command = msg.substring(0, 1);
		String[] inputSrv = msg.split(";");
		// getting commands from the server
		switch (command) {
		case ProtocolMessages.HELLO:
			serverSupportChatting = Integer.parseInt(inputSrv[1]);
			serverSupportChallenge = Integer.parseInt(inputSrv[2]);
			serverSupportLeaderboard = Integer.parseInt(inputSrv[3]);
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
			break;

		case ProtocolMessages.MOVE:
			break;
		case ProtocolMessages.GAME_FINISHED:
			break;
		case ProtocolMessages.QUEUE_SIZE:
			break;
		case ProtocolMessages.EXIT:
			break;
		default:
			break;
		}
	}

	public void readServer() throws ServerUnavailableException {
		while (true) {

			String serverMessage = readLineFromServer();
			handleServerCommands(serverMessage);
			// needs to keep reading the server messages
		}
	}

}
