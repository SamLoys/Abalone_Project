package Abalone.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import Abalone.Game;
import Abalone.Marble;
import Abalone.Exceptions.ClientUnavailableException;
import Abalone.Exceptions.ServerUnavailableException;
import Abalone.protocol.ProtocolMessages;
import Abalone.protocol.ProtocolMessages.Directions;

public class AbaloneClientHandler implements Runnable {

	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;

	private Game currentGame;

	private int clientSupportChatting = 0;
	private int clientSupportChallenge = 0;
	private int clientSupportLeaderboard = 0;
	private Marble color;

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
			currentGame = null;
		} catch (IOException e) {
			shutdown();
		}
	}

	public String getClientName() {
		return clientName;
	}

	public Marble getMarble() { 
		return color;
	}

	public void setColor(Marble color) {
		this.color = color;
	}

	public void addGame(Game game) {
		this.currentGame = game;
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
			clientSupportChatting = Integer.parseInt(inputSrv[1]);
			clientSupportChallenge = Integer.parseInt(inputSrv[2]);
			clientSupportLeaderboard = Integer.parseInt(inputSrv[3]);
			boolean[] supports = srv.getSupports();

			String response = srv.handleHello(inputSrv[4], supports[0], supports[1], supports[2]);
			String[] responseSplit = response.split(";");
			clientName = responseSplit[4];
			srv.setClientHandlerToName(clientName, this);
			sendMessage(response);

			break;

		case ProtocolMessages.JOIN:
			currentGame = null;
			int wantedGame = Integer.parseInt(inputSrv[1]);
			response = srv.handleJoin(clientName, wantedGame);
			// tell all
			srv.echo(response);

			if (srv.queueFull(wantedGame)) {
				srv.setupGame(wantedGame);
			}
			break;
		case ProtocolMessages.MOVE:
			ArrayList<Integer> indexes = new ArrayList<>();
			if (inputSrv.length < 3) {
				sendIllegalMoveException("not enough information");
				break;
			}
			if (inputSrv[2].equals(Directions.east) || inputSrv[2].equals(Directions.west)
					|| inputSrv[2].equals(Directions.northEast) || inputSrv[2].equals(Directions.northWest)
					|| inputSrv[2].equals(Directions.southEast) || inputSrv[2].equals(Directions.southWest)) {

				for (int i = 0; i < inputSrv.length; i++) {
					if (inputSrv[i].matches("([0-9]*)")) {
						indexes.add(Integer.parseInt(inputSrv[i]));
					}
				}
				String error = currentGame.addMove(inputSrv[1], inputSrv[2], indexes);
				if (error.equals("good")) {

				} else {
					System.out.println("> [" + clientName + "] Exception: " + error);
					sendIllegalMoveException(error);
				}
			} else {
				System.out.println("> [" + clientName + "] Exception: " + "ERROR: There is no given direction");
				sendIllegalMoveException("ERROR: There is no given direction");
			}

			break;

		case ProtocolMessages.QUEUE_SIZE: {
			sendMessage(srv.handleQueueSizeQuery());
		}
			break;
		case ProtocolMessages.EXIT:
			if (currentGame != null) {
				currentGame
			}
			shutdown();
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
				System.out.println("Wrote to: " + clientName + msg);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new ClientUnavailableException("Could not write to client");
			}
		} else {
			throw new ClientUnavailableException("client not connected");
		}
	}

	public void sendIllegalMoveException(String reason) throws IOException, ClientUnavailableException {
		String message = ProtocolMessages.GameResult.EXCEPTION + ProtocolMessages.DELIMITER + "i"
				+ ProtocolMessages.DELIMITER + reason + ProtocolMessages.EOC;
		sendMessage(message);
	}

	private void shutdown() {
		System.out.println("> [" + clientName + "] Shutting down.");
		srv.removeClient(clientName); 
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			System.out.println("Error during closing socket");
		}
		
		
	}

}
