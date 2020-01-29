package abalone.server;

import abalone.Game;
import abalone.Marble;
import abalone.exceptions.BoardException;
import abalone.exceptions.ClientUnavailableException;
import abalone.exceptions.IllegalMoveException;
import abalone.protocol.ProtocolMessages;
import abalone.protocol.ProtocolMessages.Directions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class AbaloneClientHandler implements Runnable {

    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;
    private Game currentGame; 
    //    private int clientSupportChatting = 0;
    //    private int clientSupportChalleng  = 0;
    //    private int clientSupportLeaderboard = 0;
    private Marble color;
    private AbaloneServer srv;
    private String clientName;
    /**
     * constructor of the AbaloneClientHandler, given the socket, server and name of the client. 
     * @param sock the socket that will connect to the client. 
     * @param srv the server 
     * @param name name of the client
     */
    
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

    /**
     * returns the name of the client.
     * @return returns the name of the client
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * returns the color of the client. 
     * @return the color of the client
     */
    public Marble getMarble() {
        return color;
    }

    /**
     * sets the color of the client. 
     * @param color the marble the client needs to be
     */
    public void setColor(Marble color) {
        this.color = color;
    }

    /**
     * set the game of the client.
     * @param game the game the client needs to connect to
     */
    public void addGame(Game game) {
        this.currentGame = game;
    }
    
    /**
     * removes the game for the client, this happens when a client disconnects or when the game has ended.
     */
    public void removeGame() { 
        this.currentGame = null;
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
                    shutdown(); 
                }
                msg = in.readLine();
            }
        } catch (IOException e) {
            shutdown();
        }

    }

    /**
     * will call the right method given the command from the client.
     * @param msg a string with a command from the client
     * @throws IOException throws an exception if the IO is not available
     * @throws ClientUnavailableException throws an exception if the client is not available
     */
    private void handleCommand(String msg) throws IOException, ClientUnavailableException {
        String command = msg.substring(0, 1);
        String[] inputSrv = msg.split(";");

        switch (command) {
            case ProtocolMessages.HELLO:
                //clientSupportChatting = Integer.parseInt(inputSrv[1]);
                //clientSupportChallenge = Integer.parseInt(inputSrv[2]);
                //clientSupportLeaderboard = Integer.parseInt(inputSrv[3]);
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
                sendMessage(response);
                //srv.echo(response); 
                srv.echo(srv.handleQueueSizeQuery());

                if (srv.queueFull(wantedGame)) {
                    srv.setupGame(wantedGame);
                }
                break;
            case ProtocolMessages.MOVE:
                if (currentGame != null) {
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
                        
                        try {
                            currentGame.addMove(inputSrv[1], inputSrv[2], indexes);
                        } catch (BoardException e) {
                            System.out.println("> [" + clientName + "] Exception: " + e.getMessage());
                            sendIllegalMoveException(e.getMessage());
                        } catch (IllegalMoveException e) {
                            System.out.println("> [" + clientName + "] Exception: " + e.getMessage());
                            sendIllegalMoveException(e.getMessage());
                        }

                    } else {
                        System.out.println("> [" + clientName + "] Exception: " + "ERROR: There is no given direction");
                        sendIllegalMoveException("ERROR: There is no given direction");
                    }
                } else {
                    System.out.println("> [" + clientName + "] Exception: " + "game has ended");
                    sendIllegalMoveException("game has ended");
                }
                
                break;

            case ProtocolMessages.QUEUE_SIZE: {
                sendMessage(srv.handleQueueSizeQuery());
            }
                break;
            case ProtocolMessages.EXIT:
                shutdown();
                break;
          
            case ProtocolMessages.BONUS:
                if (inputSrv.length > 1) {
                    if (inputSrv[1].contentEquals(ProtocolMessages.CHAT)) {
                        if (inputSrv.length > 3) {
                            String message = "b;c;" + clientName + ProtocolMessages.DELIMITER + inputSrv[3];
                            srv.multipleSend(message, currentGame.getPlayers());
                        }
                    }
                }
                break;
            default:
                break;
        }

    }
    /**
     * sends the given message to the client.
     * @param msg to send to the client 
     * @throws IOException exception if the IO is not available
     * @throws ClientUnavailableException exception if the client is not available
     */
    
    public void sendMessage(String msg) throws ClientUnavailableException {
        if (out != null) {
            try {
                out.write(msg);
                out.newLine();
                out.flush();
                System.out.println("Wrote to: " + clientName + ": " + msg);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new ClientUnavailableException("Could not write to client");
            }
        } else {
            throw new ClientUnavailableException("client not connected");
        }
    }

    /**
     * send an IllegalMoveException message to the connected client. 
     * @param reason the message to send
     * @throws IOException exception if the IO is not available
     * @throws ClientUnavailableException exception if the client is not available
     */
    public void sendIllegalMoveException(String reason) throws IOException, ClientUnavailableException {
        String message = ProtocolMessages.GameResult.EXCEPTION + ProtocolMessages.DELIMITER + "i"
                + ProtocolMessages.DELIMITER + reason + ProtocolMessages.EOC;
        sendMessage(message);
    }

    /**
     * shuts down the connection.
     * @ensures to close the socket and the in and output of the readers. 
     */
    public void shutdown() {
        if (currentGame != null) {
            try {
                
                srv.multipleSend(ProtocolMessages.EXIT + ProtocolMessages.EOC, currentGame.getPlayers());
            } catch (ClientUnavailableException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            srv.echo(srv.handleQueueSizeQuery());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClientUnavailableException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("> [" + clientName + "] Shutting down.");
        srv.removeClient(clientName);
        try {
            sock.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error during closing socket");
        }

    }
    
    /**
     * Closing the sockets of the client to the client handler.
     */
    public void closing() {
        try {
            sock.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error during closing socket");
        }
    }

}
