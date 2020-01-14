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
		//indicates if the server should connect more connection
		boolean getMoreConnections = true;
		
		while (getMoreConnections) {
			
			try {
				setupSocket(); 
			}
			catch (ExitProgram e){
				e.printStackTrace();
				
			}
		}
	}
	
	public void setupSocket()throws ExitProgram {
		ssock = null;
		while (ssock == null) {
			int port = myTUI.getInt("Please the wanted port number");
			InetAddress IP = null;
			// try to open a new ServerSocket
			try {
				 IP = InetAddress.getLocalHost();
			myTUI.showMessage("Attempting to open a socket at "+ IP + "on port " + port + "...");
				
				 
				ssock = new ServerSocket(port, 0, IP);
				 
				myTUI.showMessage("Server started on IP " + IP);
				myTUI.showMessage("Server started at port " + port);
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
	
	public void main(String[] args) {
		AbaloneServer server = new AbaloneServer();
		System.out.println("Welcome to the Abalone Game Starting...");
		new Thread(server).start();
	}
	

}
