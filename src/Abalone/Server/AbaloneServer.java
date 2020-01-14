package Abalone.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Abalone.*;
import Abalone.Exceptions.ExitProgram;
import Abalone.protocol.ServerProtocol;



public class AbaloneServer implements ServerProtocol, Runnable {

	private ServerSocket ssock;
	private List<AbaloneClientHandler> clients;
	private List<Game> games;
	private int nextPlayerNo;
	private AbaloneServerTUI myTUI;

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

	public void run() {
		// indicates if the server should connect more connection
		boolean getMoreConnections = true;

		while (getMoreConnections) {

			try {
				setupSocket();
				while (true) {
					Socket sock = ssock.accept();
					String name = "Player " + String.format("%02d", nextPlayerNo++);
					myTUI.showMessage("New Player [" + name + "] connected!");
					AbaloneClientHandler handler = new AbaloneClientHandler();
					new Thread(handler).start(); 
					clients.add(handler);
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
				myTUI.showMessage("Waiting for new players" + port);
			} catch (IOException e) {
				myTUI.showMessage("ERROR: could not create a socket on " + IP + " and port " + port + ".");

				if (!myTUI.getBool("Do you want to try again?")) {
					throw new ExitProgram("User indicated to exit the " + "program.");
				}
			}
		}

	}

	public void setupGame() {

	}
	
	
	public void removeClient(AbaloneClientHandler client) {
		this.clients.remove(client);
	} 

	public static void main(String[] args) {
		AbaloneServer server = new AbaloneServer();
		System.out.println("Welcome to the Abalone Game Starting...");
		new Thread(server).start();
	}

}
