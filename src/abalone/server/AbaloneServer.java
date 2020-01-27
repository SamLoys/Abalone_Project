package abalone.server;

import abalone.Game;
import abalone.Marble;
import abalone.exceptions.ClientUnavailableException;
import abalone.exceptions.ExitProgram;
import abalone.protocol.ProtocolMessages;
import abalone.protocol.ServerProtocol;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AbaloneServer implements ServerProtocol, Runnable {

    private ServerSocket ssock; 

    private List<Game> games = new ArrayList<Game>(); 
    private List<String> userNames = new ArrayList<String>();

    HashMap<String, AbaloneClientHandler> clientsMap = new HashMap<>();

    private List<String> queueTwo = new ArrayList<String>();
    private List<String> queueThree = new ArrayList<String>();
    private List<String> queueFour = new ArrayList<String>();

    private int nextPlayerNo;
    private AbaloneServerTui myTui;

    private boolean serverSupportChatting = true;
    private boolean serverSupportChallenge = false;
    private boolean serverSupportLeaderboard = false;

    private String serverName;

    /**
     * Constructor of the Abalone server.
     * Creates a new Abalone Server.
     * @ensures to create a new AbaloneServerTui and set the nextPlayerNo to 1
     * 
     */
    public AbaloneServer() {

        myTui = new AbaloneServerTui();
        nextPlayerNo = 1;
        serverName = "Aba_lonely";
    }
    
    
    /**
     * a special constructor only used in the system test.
     * @throws IOException exception
     */
    public AbaloneServer(int port) throws IOException {

        myTui = new AbaloneServerTui();
        System.out.println("Sarting server");
        nextPlayerNo = 1;
        serverName = "Aba_lonely";
        
        InetAddress ip = InetAddress.getLocalHost();
        ssock = new ServerSocket(port, 0, ip);
        Socket sock = ssock.accept(); 
        String name = "Player " + String.format("%02d", nextPlayerNo++);
        myTui.showMessage("New Player [" + name + "] connected!");
        AbaloneClientHandler handler = new AbaloneClientHandler(sock, this, name);
        new Thread(handler).start();
    }

    /**
     * returns the server name.
     * @return serverName String
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * returns the clientHandler connected to a given player name.
     * @param name name of the player for which clientHandler you want
     * @return the correct client handler, null if name is not found
     */
    public AbaloneClientHandler getClientHandler(String name) {
        if (clientsMap.containsKey(name)) {
            return clientsMap.get(name);
        }
        return null;

    }

    /**
     * puts a ClientHandler to a given name in a map.
     * @param name the name of the player. 
     * @param handler the ClientHandler of the given name. 
     * @ensures to set the clienthandler to the given name.
     */
    public void setClientHandlerToName(String name, AbaloneClientHandler handler) {
        clientsMap.put(name, handler);
    }

    /**
     * add a player to a given queue.
     * @requires that the lobby is a valid lobby number.  
     * @param name  name of the player. 
     * @param lobby the wanted lobby number. 
     */
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
            
            default:
                break;
        }
    }

    /**
     * returns true if the lobby is full, false if empty or not recognized.  
     * @param lobby wanted lobby.
     * @return true if full, false if empty or not found. 
     */
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

    /**
     * returns the size of the queue.
     * @requires need a valid lobby integer. 
     * @param lobby integer of the wanted lobby
     * @return the size of the queue, if lobby is not found returns 9999
     */
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

    /**
     * send a message to all the connected clients.
     * @param msg the wanted message
     * @throws IOException exception if the IO is not available
     * @throws ClientUnavailableException exception if the client is not available
     */
    public synchronized void echo(String msg) throws IOException, ClientUnavailableException {
        for (String name : clientsMap.keySet()) {
            clientsMap.get(name).sendMessage(msg);
        }
    }

    /**
     * sends a message to multiple clients .
     * @param msg the message you want to send
     * @param players the players you want to send the message to
     * @throws IOException Exception if IO not available
     * @throws ClientUnavailableException Exception if the Client is not available
     */
    public synchronized void multipleSend(String msg, String[] players) throws IOException, ClientUnavailableException {
        for (String name : players) {
            clientsMap.get(name).sendMessage(msg);
        }
    }

    /**
     * given a wanted user name will return a valid user name and add it to the queue. 
     * @param wantedName wanted user name. 
     * @return a user name that will now be assigned to the client. 
     */
    public synchronized String setUserName(String wantedName) {
        int nameCounter = 1;
        while (userNames.contains(wantedName)) {

            wantedName = wantedName + nameCounter++;
        }
        userNames.add(wantedName);
        return wantedName;

    }

    /**
     * the run method of the server.
     * sets up a server. 
     */
    public void run() {
        // indicates if the server should connect more connection
        boolean getMoreConnections = true;
        myTui.showMessage("Welcome to the server program of abalone");
        while (getMoreConnections) {

            try {
                setupSocket();
                while (true) {
                    Socket sock = ssock.accept();
                    String name = "Player " + String.format("%02d", nextPlayerNo++);
                    myTui.showMessage("New Player [" + name + "] connected!");
                    AbaloneClientHandler handler = new AbaloneClientHandler(sock, this, name);
                    new Thread(handler).start();

                }
            } catch (ExitProgram e) {
                e.printStackTrace();
                getMoreConnections = false;
            } catch (IOException e) {
                System.out.println("A server IO error occurred: " + e.getMessage());

                if (!myTui.getBool("Do you want to open a new socket?")) {
                    getMoreConnections = false;
                }
            }
        }
    }

    /**
     * sets up a socket connection.
     * @throws ExitProgram if the users does not want to try again
     */
    public void setupSocket() throws ExitProgram {
        ssock = null;
        while (ssock == null) {
            int port = myTui.getInt("Please give the wanted port number");
            InetAddress ip = null;
            // try to open a new ServerSocket
            try {
                ip = InetAddress.getLocalHost();
                myTui.showMessage("Attempting to open a socket at " + ip + "on port " + port + "...");

                ssock = new ServerSocket(port, 0, ip); 

                myTui.showMessage("Server started on IP " + ip);
                myTui.showMessage("Server started at port " + port);
                myTui.showMessage("Waiting for new players");
            } catch (IOException e) {
                myTui.showMessage("ERROR: could not create a socket on " + ip + " and port " + port + ".");

                if (!myTui.getBool("Do you want to try again?")) {
                    throw new ExitProgram("User indicated to exit the " + "program.");
                }
            }
        }

    }
    
    /**
     * sets up a game with the given lobby.
     * @param lobby the parameter for the wanted lobby
     */

    public void setupGame(int lobby) {
        String player1Name = "";
        String player2Name = "";
        String player3Name = "";
        String player4Name = "";
        Game game;
        String[] players;
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
          
          
                players = new String[2];
                players[0] = player1Name;
                players[1] = player2Name;
                handleGameStart(players);
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
                players = new String[3];
                players[0] = player1Name;
                players[1] = player2Name;
                players[2] = player3Name;
                handleGameStart(players);
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
                players = new String[4];
                players[0] = player1Name;
                players[1] = player2Name;
                players[2] = player3Name;
                players[3] = player4Name;
                handleGameStart(players);
                break;
            
            default:
                break;
        }
    }

    /**
     * removes a game from the list.
     * @param game the game to be removed.
     */
    public void removeGame(Game game) {
        this.games.remove(game);
    }
    
    /**
     * removes the client from the server.
     * @param name the name of the client that needs to be deleted.
     */

    public void removeClient(String name) {
        clientsMap.remove(name);
        try {
            userNames.remove(name);
            queueFour.remove(name);
            queueThree.remove(name);
            queueTwo.remove(name);
        } catch (Exception e) {
            System.out.println("error removing client name");
        }

    }

    /**
     * get an array of booleans for the supports of the chatting, challenge, and leaderboard. 
     * @return and array of boolean.
     */
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
        String nextplayer = playerName;
        String message = ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + nextplayer + ProtocolMessages.DELIMITER;
        return message;
    }

    @Override
    public String handleQueueSizeQuery() {
        String message = ProtocolMessages.QUEUE_SIZE + ProtocolMessages.DELIMITER + getQueueSize(2)
                + ProtocolMessages.DELIMITER + getQueueSize(3) + ProtocolMessages.DELIMITER + getQueueSize(4)
                + ProtocolMessages.EOC;

        return message;
    }

}
