package Abalone.Client;

import Abalone.Audiopack.Audio;
import Abalone.Board;

import Abalone.Exceptions.BoardException;
import Abalone.Exceptions.ExitProgram;
import Abalone.Exceptions.IllegalMoveException;
import Abalone.Exceptions.ServerUnavailableException;
import Abalone.Marble;
import Abalone.MoveCheck;
import Abalone.SmartyAI;
import Abalone.protocol.ClientProtocol;
import Abalone.protocol.ProtocolMessages;
import Abalone.protocol.ServerProtocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AbaloneClient implements ClientProtocol {
    private Socket sock;
    private BufferedReader networkIN;
    private BufferedWriter networkOut;
    private AbaloneClientTui clientTui;
    private String name;
    private Marble color;
    private boolean clientSupportChatting = true;
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
    private boolean isAI;
    private SmartyAI aiPlayer = null;
    Board clientBoard;
    String[] gamePlayers;
    MoveCheck moveChecker;
    MoveCheck moveEnemyCheck;
    boolean running = true;

    /** Javadoc.
     * @param args Javadoc
     */
    public static void main(String args[]) {
        Audio muziek = null;
        try {
            muziek = new Audio();
        } catch (UnsupportedAudioFileException e) {
            //if music can't play no need to print
        } catch (IOException e) {
            //if music can't play no need to print
        } catch (LineUnavailableException e) {
            //if music can't play no need to print
        }
        Thread t1 = new Thread(muziek);
        t1.start();
        AbaloneClient client = new AbaloneClient();
        client.start();

    }

    public String getName() {
        return name;
    }
    
    /** Javadoc.
     */
    public AbaloneClient() {
        this.clientTui = new AbaloneClientTui(this);
        handshakeComplete = false;
        joiningComplete = false;
        gameSize = 0;

    }
    
    /** Javadoc.
     */
    public void start() {
        clientTui.showMessage("\r\n" + "\r\n"
                + "               _                            _                _           _                  \r\n"
                + " __      _____| | ___ ___  _ __ ___   ___  | |_ ___     __ _| |__   __ _| | ___  _ __   ___ \r\n"
                + " \\ \\ /\\ / / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\   / _` | '_ \\ / _` "
                + "| |/ _ \\| '_ \\ / _ \\\r\n"
                + "  \\ V  V /  __/ | (_| (_) | | | | | |  __/ | || (_) | | (_| | |_) | (_| | | (_) |"
                + " | | |  __/\r\n"
                + "   \\_/\\_/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/   \\__,_|_.__/ \\__,_"
                + "|_|\\___/|_| |_|\\___|\r\n"
                + "                                                                                            \r\n"
                + "\r\n" + "");
        name = clientTui.getUserName("Please give your wanted username");
        isAI = clientTui.getBool("Are you an AI?");
        clientTui.showMessage("Welcome " + name + " we will now setup the connection..");
        try {
            createConnection();
            handleHandshake(clientSupportChatting, clientSupportChallenge, clientSupportLeaderboard, name);
            getCurrentQueueSizes();
            String serverMessage = readLineFromServer();
            handleServerCommands(serverMessage);
            gameSize = clientTui.getInt("How many player game would you like to join?", 2, 4);
            joinQueue(gameSize);
            Thread threadTui = new Thread(clientTui);
            threadTui.start();
            readServer();

        } catch (ServerUnavailableException e) {
            clientTui.showMessage(e.getMessage());
            clientTui.showMessage("The connection has been lost");
            clientTui.stopThread();

            closeConnection();

        } catch (ExitProgram e) {
            clientTui.showMessage("closing... user didnt want to try again");
            closeConnection();
        }
    }
    
    /** Javadoc.
     * @throws ExitProgram Javadoc.
     */
    public void createConnection() throws ExitProgram {
        clearConnection();
        while (sock == null) {
            InetAddress host = null;
            int port = clientTui.getInt("Please enter port number", 0, 65535);
            host = clientTui.getIp();
            // try to open a Socket to the server
            try {
                // InetAddress addr = InetAddress.getByName(host);
                System.out.println("Attempting to connect to " + host + ":" + port + "...");
                sock = new Socket(host, port);
                networkIN = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                networkOut = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            } catch (IOException e) {
                System.out.println("ERROR: could not create a socket on " + host + " and port " + port + ".");

                // Do you want to try again? (ask user, to be implemented)
                if (!clientTui.getBool("Do you want to try again")) {
                    throw new ExitProgram("User indicated to exit.");
                }
            }
        }
    }
    
    /** Javadoc.
     * @param message Javadoc.
     * @throws ServerUnavailableException Javadoc.
     */
    public synchronized void sendMessage(String message) throws ServerUnavailableException {
        if (networkOut != null) {
            try {
                networkOut.write(message);
                networkOut.newLine();
                networkOut.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new ServerUnavailableException("Could not write to server");
            }
        } else {
            throw new ServerUnavailableException("Server not yet made");
        }

    }

    /** Javadoc.
     * @return Javadoc.
     * @throws ServerUnavailableException Javadoc.
     */
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

    /** Javadoc.
     */
    public void closeConnection() {
        System.out.println("Closing the connection...");
        try {
            running = false;
            networkIN.close();
            System.out.println("NetworkIN closed...");
            networkOut.close();
            System.out.println("NetworkOUT closed...");
            sock.close();
            System.out.println("Socked Closed...");

        } catch (IOException e) {
            clientTui.showMessage("Error during closing the connection");
        }
    }

    /** Javadoc.
     */
    public void clearConnection() {
        sock = null;
        networkIN = null;
        networkOut = null;
    }
    
    /** Javadoc.
     * @param msg Javadoc.
     * @throws ServerUnavailableException Javadoc.
     */
    public void handleServerCommands(String msg) throws ServerUnavailableException {
        if (!msg.equals("")) {

            String command = msg.substring(0, 1);
            String[] inputSrv = msg.split(";");
            // getting commands from the server
            switch (command) {

                case ProtocolMessages.GameResult.EXCEPTION:
                    clientTui.showMessage("The server send an exception");
                    if (inputSrv.length > 2) {
                        if (inputSrv[1].equals("i")) {
                            clientTui.showMessage("IllegalMoveException: " + inputSrv[2]);
                        }
                        if (inputSrv[1].equals("i")) {
                            clientTui.showMessage("JoinException: " + inputSrv[2]);
                        }
       
                    } else {
                        clientTui.showMessage("exception not complete");
                    }
                    break;
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
                            clientTui.showMessage("new Player joined queue , now " + inputSrv[2] 
                                    + "out of " + inputSrv[1]);
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
                                setPropperColor();
                                clientTui.showMessage("The game has started with the following players: " 
                                        + gamePlayers[0]
                                        + " and " + gamePlayers[1]);
                                clientTui.showMessage("The game will be played in this order");
       
                                clientBoard = new Board(2);
                                moveChecker = new MoveCheck(color, clientBoard);
                                try {
                                    showBoard();
                                    clientTui.printHelpMenu();
                                } catch (BoardException e) {
       
                                    System.out.println(e.getMessage());
                                }
                                if (gamePlayers[0].equals(name)) {
                                    clientTui.showMessage("it is your turn ," + color.toString()
                                            + " go enter your move, /n  remember typing h will print the help menu");
                                    yourTurn = true;
       
                                }
                                if (isAI) {
                                    aiPlayer = new SmartyAI(clientBoard, color, this, moveChecker, name);
                                    if (yourTurn) {
                                        aiPlayer.makeMove(true);
                                    }
                                }
                            }
                            if (inputSrv.length == 5) {
                                gamePlayers = new String[3];
                                gamePlayers[0] = inputSrv[1];
                                gamePlayers[1] = inputSrv[2];
                                gamePlayers[2] = inputSrv[3];
                                clientTui.showMessage("The game has started with the following players: " 
                                        + gamePlayers[0]
                                        + " and " + gamePlayers[1] + " and " + gamePlayers[2]);
                                clientTui.showMessage("The game will be played in this order");
       
                                setPropperColor();
                                clientBoard = new Board(3);
                                try {
                                    showBoard();
                                    clientTui.printHelpMenu();
                                } catch (BoardException e) {
                                    System.out.println(e.getMessage());
                                }
                                moveChecker = new MoveCheck(color, clientBoard);
                                if (gamePlayers[0].equals(name)) {
                                    clientTui.showMessage("it is your turn ," + color.toString()
                                            + " go enter your move, /n  remember typing h will print the help menu");
                                    yourTurn = true;
       
                                }
                                if (isAI) {
                                    aiPlayer = new SmartyAI(clientBoard, color, this, moveChecker, name);
                                    if (yourTurn) {
                                        aiPlayer.makeMove(true);
                                    }
                                }
                            }
                            if (inputSrv.length == 6) {
                                gamePlayers = new String[4];
                                gamePlayers[0] = inputSrv[1];
                                gamePlayers[1] = inputSrv[2];
                                gamePlayers[2] = inputSrv[3];
                                gamePlayers[3] = inputSrv[4];
                                clientTui.showMessage("The game has started with the following players: " 
                                        + gamePlayers[0]
                                        + " and " + gamePlayers[1] + " and " + gamePlayers[2] + " and " 
                                        + gamePlayers[3]);
                                clientTui.showMessage("The game will be played in this order");
       
                                setPropperColor();
                                clientBoard = new Board(4);
                                try {
                                    showBoard();
                                    clientTui.printHelpMenu();
                                } catch (BoardException e) {
                                    System.out.println(e.getMessage());
                                }
                                if (gamePlayers[0].equals(name)) {
                                    clientTui.showMessage("it is your turn ," + color.toString()
                                            + " go enter your move, /n  remember typing h will print the help menu");
                                    yourTurn = true;
       
                                }
                                moveChecker = new MoveCheck(color, clientBoard);
                                if (isAI) {
                                    aiPlayer = new SmartyAI(clientBoard, color, this, moveChecker, name);
                                    if (yourTurn) {
                                        aiPlayer.makeMove(true);
                                    }
                                }
       
                            }
                        }
                    }
       
                    break;
       
                case ProtocolMessages.MOVE:
                    ArrayList<Integer> newIndexes = new ArrayList<>();
                    ArrayList<Integer> totalMove = new ArrayList<>();
                    ArrayList<Integer> indexes = new ArrayList<>();
                    yourTurn = false;
                    clientTui.showMessage(
                            "Player " + inputSrv[2] + "has moved " + "\n it is now the turn of player: " + inputSrv[1]);
                    String direction = inputSrv[3];
       
                    for (int i = 0; i < inputSrv.length; i++) {
                        if (inputSrv[i].matches("([0-9]*)")) {
                            indexes.add(Integer.parseInt(inputSrv[i]));
                        }
                    }
       
                    try {
                        newIndexes = clientBoard.protocolToIndex(indexes);
                    } catch (BoardException e1) {
                        System.out.println(e1.getMessage());
       
                    }
       
                    moveEnemyCheck = new MoveCheck(getPlayerMarble(inputSrv[2]), clientBoard);
                    try {
                        totalMove = moveEnemyCheck.moveChecker(newIndexes, direction);
                    } catch (IllegalMoveException e) {
                        clientTui.showMessage("player " + inputSrv[2] + "Tried a move that is not valid "
                                + "according to us");
                        clientTui.showMessage("the error is: " + e.getMessage());
                        clientTui.showMessage("We did not move the marble, discuss with the party");
                        break;
                    }
                    boolean scores = false;
       
                    try {
                        // move the board
                        // get boolean if there is scored
                        scores = clientBoard.move(totalMove, direction);
                    } catch (BoardException e) {
                        System.out.println(e.getMessage());
                    }
       
                    if (scores) {
                        clientBoard.addScore(getPlayerMarble(inputSrv[2]));
                    }
                    try {
                        showBoard();
                    } catch (BoardException e) {
                        System.out.println(e.getMessage());
                    }
                    if (inputSrv[1].equals(name)) {
                        clientTui.showMessage("it is now your turn, enter your move");
                        yourTurn = true;
                        if (isAI) {
                            aiPlayer.makeMove(true);
                        }
                    }
                    break;
       
                case ProtocolMessages.GAME_FINISHED:
                    clientTui.showMessage("The game has finished");
                    if (inputSrv[1].equals(ProtocolMessages.GameResult.DRAW)) {
                        joiningComplete = false;
                        gameStarted = false;
                        gameSize = 0;
                        clientTui.showMessage("There was a draw");
                        gameSize = clientTui.getInt("what queue do you want to join?", 2, 4);
                        joinQueue(gameSize);
                    }
                    if (inputSrv[1].equals(ProtocolMessages.GameResult.WIN)) {
                        clientTui.showMessage("There is a winner");
                        if (gameSize == 2 || gameSize == 3) {
                            clientTui.showMessage("the winner is: " + gamePlayers[Integer.parseInt(inputSrv[2]) - 1]);
                            joiningComplete = false;
                            gameStarted = false;
                            gameSize = clientTui.getInt("what queue do you want to join?", 2, 4);
                            joinQueue(gameSize);
                        }
       
                        if (gameSize == 4) {
                            if (Integer.parseInt(inputSrv[2]) == 1) {
                                clientTui
                                        .showMessage("the winners are: " + gamePlayers[0] + "and player " 
                                        + gamePlayers[2]);
                                joiningComplete = false;
                                gameStarted = false;
                                gameSize = clientTui.getInt("what queue do you want to join?", 2, 4);
                                joinQueue(gameSize);
                            }
                            if (Integer.parseInt(inputSrv[2]) == 2) {
                                clientTui
                                        .showMessage("the winners are: " + gamePlayers[1] 
                                        + "and player " + gamePlayers[3]);
                                joiningComplete = false;
                                gameStarted = false;
                                gameSize = clientTui.getInt("what queue do you want to join?", 2, 4);
                                joinQueue(gameSize);
                            }
       
                        }
                    }
                    if (inputSrv[1].equals(ProtocolMessages.GameResult.EXCEPTION)) {
                        clientTui.showMessage("There was an error, the game has now ended");
                        joiningComplete = false;
                        gameStarted = false;
                        gameSize = 0;
                        joinQueue(clientTui.getInt("what queue do you want to join?"));
                    }
       
                    break;
                case ProtocolMessages.QUEUE_SIZE:
                    clientTui.showMessage(
                            "the queue for 2 players is : " + inputSrv[1] + "\n" + "The queue for 3 players is : "
                                    + inputSrv[2] + "\n" + "the queue for 4 players is : " + inputSrv[3]);
                    break;
                case ProtocolMessages.EXIT:
                    clientTui.showMessage("The server gives the command to exit");
                    closeConnection();
                    clientTui.stopThread();
                    break;
       
                case "b":
                    if (inputSrv.length > 3) {
                        if (inputSrv[1].contentEquals("c")) {
                            // chatting
                            clientTui.showMessage(inputSrv[2] + ": " + inputSrv[3]);
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            clientTui.showMessage("Received and invalid server commando");
        }
    }
    
    /** Javadoc.
     */
    public void setPropperColor() {
        switch (gamePlayers.length) {
            case 2:
                if (gamePlayers[0].equals(name)) {
                    color = Marble.White;
                } else {
                    color = Marble.Black;
                }
                break;
      
            case 3:
                if (gamePlayers[0].equals(name)) {
                    color = Marble.White;
                } else if (gamePlayers[1].equals(name)) {
                    color = Marble.Black;
                } else {
                    color = Marble.Green;
                }
                break;
      
            case 4:
                if (gamePlayers[0].equals(name)) {
                    color = Marble.White;
                } else if (gamePlayers[1].equals(name)) {
                    color = Marble.Black;
                } else if (gamePlayers[2].equals(name)) {
                    color = Marble.Green;
                } else {
                    color = Marble.Red;
                }
                break;
            default:
                break;
        }
    }
    
    /** Javadoc.
     * @param name Javadoc.
     * @return Javadoc.
     */
    public Marble getPlayerMarble(String name) {
        switch (gameSize) {
            case 2:
                if (gamePlayers[0].equals(name)) {
                    return Marble.White;
                } else {
                    return Marble.Black;
                }
     
            case 3:
                if (gamePlayers[0].equals(name)) {
                    return Marble.White;
                } else if (gamePlayers[1].equals(name)) {
                    return Marble.Black;
                } else {
                    return Marble.Green;
                }
     
            case 4:
                if (gamePlayers[0].equals(name)) {
                    return Marble.White;
                } else if (gamePlayers[1].equals(name)) {
                    return Marble.Black;
                } else if (gamePlayers[2].equals(name)) {
                    return Marble.Green;
                } else {
                    return Marble.Red;
                }
            default:
                break;
        }
        return Marble.Death;
    }
    
    /** Javadoc.
     * @throws ServerUnavailableException Javadoc.
     */
    public void readServer() throws ServerUnavailableException {
        while (running) {

            String serverMessage = readLineFromServer();
            if (serverMessage.startsWith("x")) {
                running = false;
            }

            handleServerCommands(serverMessage);
            // needs to keep reading the server messages
        }
    }
    
    /** Javadoc.
     * @throws BoardException Javadoc.
     */
    public void showBoard() throws BoardException {
        clientTui.showMessage(clientBoard.toString());
        switch (gameSize) {
            case 2:
                clientTui.showMessage("The score for: " + gamePlayers[0] + "(" 
                        + getPlayerMarble(gamePlayers[0]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[0])));
                clientTui.showMessage("The score for: " + gamePlayers[1] + "(" 
                        + getPlayerMarble(gamePlayers[1]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[1])));
                clientTui.showMessage("Turn: " + clientBoard.getTurns() + "out of: " + clientBoard.getMaxTurns());
                break;
            case 3:
                clientTui.showMessage("The score for: " + gamePlayers[0] + "(" 
                        + getPlayerMarble(gamePlayers[0]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[0])));
                clientTui.showMessage("The score for: " + gamePlayers[1] + "(" 
                        + getPlayerMarble(gamePlayers[1]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[1])));
                clientTui.showMessage("The score for: " + gamePlayers[2] + "(" 
                        + getPlayerMarble(gamePlayers[2]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[2])));
                clientTui.showMessage("Turn: " + clientBoard.getTurns() + "out of: " + clientBoard.getMaxTurns());
                break;
            case 4:
                clientTui.showMessage("The score for: " + gamePlayers[0] + "(" 
                        + getPlayerMarble(gamePlayers[0]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[0])));
                clientTui.showMessage("The score for: " + gamePlayers[1] + "(" 
                        + getPlayerMarble(gamePlayers[1]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[1])));
                clientTui.showMessage("The score for: " + gamePlayers[2] + "(" 
                        + getPlayerMarble(gamePlayers[2]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[2])));
                clientTui.showMessage("The score for: " + gamePlayers[3] + "(" 
                        + getPlayerMarble(gamePlayers[3]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[3])));
                clientTui.showMessage("Turn: " + clientBoard.getTurns() + "out of: " + clientBoard.getMaxTurns());
                break;
            default:
                break;
        }
    }

    public String getHint() {
        SmartyAI ai = new SmartyAI(clientBoard, color, this, moveChecker, name);
        return ai.getHint(clientBoard, color, moveChecker);
    }
    
    /** Javadoc.
     * @param message Javadoc.
     * @throws ServerUnavailableException Javadoc.
     */
    public void sendChat(String message) throws ServerUnavailableException {
        if (serverSupportChatting) {
            sendMessage("b;c;" + name + ProtocolMessages.DELIMITER + message + ProtocolMessages.EOC);
        } else {
            clientTui.showMessage("The Server does not support chatting");
        }

    }

    @Override
    public void handleHandshake(boolean chat, boolean challenge, boolean leaderboard, String playerName)
            throws ServerUnavailableException {
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
        sendMessage(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + gamesize + ProtocolMessages.EOC);
        while (!joiningComplete) {
            clientTui.showMessage("Waiting for join conformaton");
            String serverMessage = readLineFromServer();
            handleServerCommands(serverMessage);
        }
    }

    @Override
    public void sendMove(String playerName, String direction, ArrayList<Integer> marbleIndices)
            throws ServerUnavailableException {

        if (yourTurn) {
            if (gameStarted) {
                ArrayList<Integer> convertIndexes = null;
                try {
                    convertIndexes = clientBoard.protocolToIndex(marbleIndices);
                } catch (BoardException e1) {

                    System.out.println(e1.getMessage());
                }

                ArrayList<Integer> allMoved = new ArrayList<>();
                try {
                    allMoved = moveChecker.moveChecker(convertIndexes, direction);
                    ArrayList<Integer> protocolAll = new ArrayList<>();
                    protocolAll = clientBoard.indexToProtocol(allMoved);
                    String toSendMarbles = "";
                    for (int i = 0; i < protocolAll.size(); i++) {
                        toSendMarbles = toSendMarbles + ProtocolMessages.DELIMITER;
                        toSendMarbles = toSendMarbles + protocolAll.get(i);
                    }
                    sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + playerName
                            + ProtocolMessages.DELIMITER + direction + toSendMarbles + ProtocolMessages.EOC);
                } catch (IllegalMoveException e) {
                    clientTui.showMessage(e.getMessage() + "please try again");
                } catch (BoardException e) {
                    clientTui.showMessage(e.getMessage());
                }

            } else {
                clientTui.showMessage("The game hasnt started yet");
            }

        } else {
            clientTui.showMessage("It is not your turn, please wait");
        }

    }

    @Override
    public void getCurrentQueueSizes() throws ServerUnavailableException {

        sendMessage(ProtocolMessages.QUEUE_SIZE + ProtocolMessages.EOC);
    }

    @Override
    public void sendExit() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.EXIT + ProtocolMessages.EOC);

    }

}
