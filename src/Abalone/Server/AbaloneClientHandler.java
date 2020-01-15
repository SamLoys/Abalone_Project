package Abalone.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import Abalone.Exceptions.ClientUnavailableException;
import Abalone.Exceptions.ServerUnavailableException;
import Abalone.protocol.ProtocolMessages;

public class AbaloneClientHandler implements Runnable {

	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;

	/** The connected HotelServer */
	private AbaloneServer srv;

	/** Name of this ClientHandler */
	private String clientName;

	public AbaloneClientHandler(Socket sock, AbaloneServer srv, String name) {
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			this.sock = sock;
			this.srv = srv;
			this.clientName = name;
		} catch (IOException e) {
			shutdown();
		}
	}

	public String getClientName() {
		return clientName;
	}

	@Override
	public void run() {
		String msg;
		try {
			msg = in.readLine();
			while (msg != null) {
				System.out.println("> [" + clientName + "] Incoming: " + msg);
				try {
					handleCommand(msg);
				} catch (ClientUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				out.newLine(); 
//				out.flush();
				msg = in.readLine();
			}
			shutdown();
		} catch (IOException e) {
			shutdown();
		}

	}

	private void handleCommand(String msg) throws IOException, ClientUnavailableException {
		String command = msg.substring(0, 1);
		String[] inputSrv = msg.split(";");

		switch (command) {
		case ProtocolMessages.HELLO:

			String actualName = srv.setUserName(inputSrv[4]);
			System.out.println("> [" + clientName + "] is now " + actualName);
			clientName = actualName;

			String response = ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + srv.getSupports() + actualName
					+ ProtocolMessages.EOC;
			sendMessage(response);
			break;

		case ProtocolMessages.JOIN:
			int wantedGame = Integer.parseInt(inputSrv[1]);
			srv.addToQueue(clientName, wantedGame);
			response = ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + wantedGame
					+ ProtocolMessages.DELIMITER + srv.getQueueSize(wantedGame) + ProtocolMessages.EOC;   
			srv.echo(response);
			if (srv.queueFull(wantedGame)) {
				srv.setupGame(wantedGame);
			}
		default:
			break;
		}

	}
	
	

	public void sendMessage(String msg) throws IOException, ClientUnavailableException {
		if (out != null) {
			try {
				out.write(msg);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new ClientUnavailableException("Could not write to client");
			}
		} else {
			throw new ClientUnavailableException("client not connected");
		}
	}

	private void shutdown() {
		System.out.println("> [" + clientName + "] Shutting down.");
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		srv.removeClient(this);
	}

}
