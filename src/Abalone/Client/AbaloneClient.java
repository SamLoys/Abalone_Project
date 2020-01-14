package Abalone.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import Abalone.Exceptions.ExitProgram;
import Abalone.Exceptions.ProtocolException;
import Abalone.Exceptions.ServerUnavailableException;
import Abalone.protocol.*;

public class AbaloneClient implements ClientProtocol {
	private Socket sock;
	private BufferedReader networkIN;
	private BufferedWriter networkOUT;
	private AbaloneClientTUI clientTui;

	public static void main(String args[]) {
		(new AbaloneClient()).start();
	}

	public AbaloneClient() {
		this.clientTui = new AbaloneClientTUI(this);
	}

	public void start() {
		try {
			createConnection();
			handleHello();
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
			int port = clientTui.getInt("Please enter the port you want to connect to");
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
	
	public synchronized void sendMessage(String message) throws ServerUnavailableException{
		if (networkOUT != null) {
			try {
				networkOUT.write(message);
				networkOUT.newLine();
				networkOUT.flush();
			}
			catch (IOException e){
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

	public void handleHello() throws ServerUnavailableException, ProtocolException {

	}

	public void readServer() {
		while (true) {

		}
	}

}
