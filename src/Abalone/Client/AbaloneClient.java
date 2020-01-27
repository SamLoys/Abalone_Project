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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

/**
 * The Abalone Client, will be used by the client. 
 * Created on 17-01-2019. 
 * @author Sam Freriks and Ayla van der Wal.
 * @version 1.0
 */ 
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

    /**
    * main method for the client.
    */
    public static void main(String[] args) {
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
        //create a new client and start
        AbaloneClient client = new AbaloneClient();
        client.start();

    }

    /**
     * get the name of the client.
     * @return the name of the client
     */
    public String getName() {
        return name;
    }
    
    /**
     * Construct a AbaloneClient.
     * create a new Tui and set the proper parameters. 
     */
    public AbaloneClient() {
        this.clientTui = new AbaloneClientTui(this);
        handshakeComplete = false;
        joiningComplete = false;
        gameSize = 0;

    }
    
    /**
     * start method of the Abalone client.
     * follows startup sequence
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
        //set the wanted user name 
        name = clientTui.getUserName("Please give your wanted username");
        //ask the client if it want to play as an ai.
        isAI = clientTui.getBool("Are you an AI?");
        clientTui.showMessage("Welcome " + name + " we will now setup the connection..");
        try {
            //create a connection
            createConnection();
            //send handshake
            handleHandshake(clientSupportChatting, clientSupportChallenge, clientSupportLeaderboard, name);
            //ask for queue sizes
            getCurrentQueueSizes();
            //read the queueSize
            String serverMessage = readLineFromServer();
            handleServerCommands(serverMessage);
            //ask the user how many player queue it wants to join 
            gameSize = clientTui.getInt("How many player game would you like to join?", 2, 4);
            //send a join queue message from the given queue
            joinQueue(gameSize);
            //create a new clientTui on a new thread
            //this will handle all the user input
            Thread threadTui = new Thread(clientTui);
            threadTui.start();
            //read the incoming server commands, this will loop in a while loop. 
            readServer();

        } catch (ServerUnavailableException e) {
            //catch exception, if server connection lost.
            clientTui.showMessage(e.getMessage());
            clientTui.showMessage("The connection has been lost");
            //stop the Tui
            clientTui.stopThread();
            //close the connection
            closeConnection();

        } catch (ExitProgram e) {
            clientTui.showMessage("closing... user didnt want to try again");
            closeConnection();
        }
    }
    
    /**
     * create a connection with the server.
     * @throws ExitProgram if the connection was failed and the user dont want to try again
     */
    public void createConnection() throws ExitProgram {
        //empty old connection
        clearConnection();
        while (sock == null) {
            //keep trying to connect
            InetAddress host = null;
            //ask port
            int port = clientTui.getInt("Please enter port number", 0, 65535);
            //ask ip
            host = clientTui.getIp();
            // try to open a Socket to the server
            try {
                //try to connect
                System.out.println("Attempting to connect to " + host + ":" + port + "...");
                //create socket
                sock = new Socket(host, port);
                //open in and output
                networkIN = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                networkOut = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            } catch (IOException e) {
                System.out.println("ERROR: could not create a socket on " + host + " and port " + port + ".");
                // Do you want to try again? (ask user, to be implemented)
                if (!clientTui.getBool("Do you want to try again")) {
                    //throws exception if the client does not want to try again
                    throw new ExitProgram("User indicated to exit.");
                }
            }
        }
    }
    
    /**
     * send a message to the server.
     * @param message message to send to the server
     * @throws ServerUnavailableException if the server is not available 
     */
    public synchronized void sendMessage(String message) throws ServerUnavailableException {
        if (networkOut != null) {
            try {
                //send message to the server
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

    /**
     * read a line from the server.
     * @return the line from the server
     * @throws ServerUnavailableException Exception if the server is not available
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

    /**
     * close the connection and stop the Tui.
     */
    public void closeConnection() {
        System.out.println("Closing the connection...");
        try {
            running = false;
            if (networkIN != null) {
                networkIN.close();
                System.out.println("NetworkIN closed...");
            }
            if (networkOut != null) {
                networkOut.close();
                System.out.println("NetworkOUT closed...");
            }
            if (sock != null) {
                sock.close();
                System.out.println("Socked Closed...");
            }
            if (clientTui != null) {
                clientTui.stopThread();
            }
           
        } catch (IOException e) {
            clientTui.showMessage("Error during closing the connection");
        }
      
        
    }

    /**
     * set the socket and in and output to null.
     */
    public void clearConnection() {
        sock = null;
        networkIN = null;
        networkOut = null;
    }
    
    /**
     * Given the command will do the appropriate method. 
     * @param msg the server message
     * @throws ServerUnavailableException Exception if the server is not available
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
                    if (inputSrv.length < 3) {
                        //hello message from server
                        serverSupportChatting = Integer.parseInt(inputSrv[1]) == 1 ? true : false;
                        serverSupportChallenge = Integer.parseInt(inputSrv[2]) == 1 ? true : false;
                        serverSupportLeaderboard = Integer.parseInt(inputSrv[3]) == 1 ? true : false;
                        //set the name to the name the server assigns to the user
                        this.name = inputSrv[4];
                        clientTui.showMessage("The server has assigned you as the following name: " + name);
                        handshakeComplete = true;
                        break;
                    } else { 
                        clientTui.showMessage("handshake from the server is not complete:");
                        clientTui.showMessage("message: " + inputSrv.toString());
                    } 
                    break; 
                  
                case ProtocolMessages.JOIN:
                    if (inputSrv.length < 3) {
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
                            break;
                        }
                    } else {
                        clientTui.showMessage("joinmessage from the server is not complete:");
                        clientTui.showMessage("message: " + inputSrv.toString());
                        break;
                    }
                    break;
       
                case ProtocolMessages.GAME_START:
                    gameStarted = true;
                    // only gets this message if the server sends it to me
                    for (int i = 0; i < inputSrv.length; i++) {
                        if (inputSrv[i].equals(name)) {
                            if (inputSrv.length == 4) {
                                //s;name;name;* so the length is 4
                                gamePlayers = new String[2];
                                gamePlayers[0] = inputSrv[1];
                                gamePlayers[1] = inputSrv[2];
                                //sets the color of the players
                                setPropperColor();
                                clientTui.showMessage("The game has started with the following players: " 
                                        + gamePlayers[0]
                                        + " and " + gamePlayers[1]);
                                clientTui.showMessage("The game will be played in this order");
                                //create a board
                                clientBoard = new Board(2);
                                //create a movechecker for this player
                                moveChecker = new MoveCheck(color, clientBoard);
                                try {
                                    //print the board
                                    showBoard();
                                    clientTui.printHelpMenu();
                                } catch (BoardException e) {
                                    System.out.println(e.getMessage());
                                }
                                if (gamePlayers[0].equals(name)) {
                                    //check if it is your turn
                                    clientTui.showMessage("it is your turn (" + color.toString()
                                            + ") go enter your move, /n  remember typing h will print the help menu");
                                    yourTurn = true;
                                }
                                if (isAI) {
                                    //create a new AI with the board, color name and moveChecker
                                    aiPlayer = new SmartyAI(clientBoard, color, this, moveChecker, name);
                                    if (yourTurn) {
                                        //if it is your turn make the move and send it
                                        try {
                                            aiPlayer.makeMove(true);
                                        } catch (ServerUnavailableException e) {
                                            clientTui.showMessage(e.getMessage());
                                        }
                                    }
                                }
                            }
                            if (inputSrv.length == 5) {
                                //s;name;name;name;* so the length is 5
                                //almost the some as with 2 players
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
                                gamePlayers[2] = inputSrv[2];
                                gamePlayers[1] = inputSrv[3];
                                gamePlayers[3] = inputSrv[4];
                                //order is changed, protocol will send team 1 team 1 team 2 team 2
                                //but it was , team 1 team 2 team 1 team 2, protocol changed last minute so 
                                //we decided to keep the implementation the same. 
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
                    if (inputSrv.length > 2) {
                        ArrayList<Integer> newIndexes = new ArrayList<>();
                        ArrayList<Integer> totalMove = new ArrayList<>();
                        ArrayList<Integer> indexes = new ArrayList<>();
                        yourTurn = false;
                        clientTui.showMessage(
                                "Player " + inputSrv[2] + "has moved " + "\n it is now the turn "
                                        + "of player: " + inputSrv[1]);
                        String direction = inputSrv[3];
                        
                        //put marbles inside arrayList
                        for (int i = 0; i < inputSrv.length; i++) {
                            if (inputSrv[i].matches("([0-9]*)")) {
                                indexes.add(Integer.parseInt(inputSrv[i]));
                            }
                        }
                        
                        //change to board model indexes
                        try {
                            newIndexes = clientBoard.protocolToIndex(indexes);
                        } catch (BoardException e1) {
                            System.out.println(e1.getMessage());
                        }
                       
                        //make a new movechecker for the given player, and check if the move is valid
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
                        //check if it now your turn
                        if (inputSrv[1].equals(name)) {
                            clientTui.showMessage("it is now your turn, enter your move");
                            yourTurn = true;
                            if (isAI) {
                                aiPlayer.makeMove(true);
                            }
                        }
                        break;
                    } else {
                        clientTui.showMessage("Move not complete");
                        clientTui.showMessage("message " + inputSrv.toString());
                        break;
                    }
                   
       
                case ProtocolMessages.GAME_FINISHED:
                    if (inputSrv.length > 1) {
                        //check if length is correct
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
                                clientTui.showMessage("the winner is: " 
                                    + gamePlayers[Integer.parseInt(inputSrv[2]) - 1]);
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
                    } else {
                        clientTui.showMessage("Finished message was not a good command");
                        clientTui.showMessage("Message: " + inputSrv.toString());
                    }
                    break;
                case ProtocolMessages.QUEUE_SIZE:
                    //print the queue
                    clientTui.showMessage(
                            "the queue for 2 players is : " + inputSrv[1] + "\n" + "The queue for 3 players is : "
                                    + inputSrv[2] + "\n" + "the queue for 4 players is : " + inputSrv[3]);
                    break;
                case ProtocolMessages.EXIT:
                    //server send the exit command
                    clientTui.showMessage("The server gives the command to exit");
                    closeConnection();
                    clientTui.stopThread();
                    break;
       
                case ProtocolMessages.BONUS:
                    //bonus
                    if (inputSrv.length > 3) {
                        if (inputSrv[1].contentEquals(ProtocolMessages.CHAT)) {
                            // chatting
                            clientTui.showMessage(inputSrv[2] + ": " + inputSrv[3]);
                        }
                    }
                    break;
                default:
                    clientTui.showMessage("Received an unknown server commando");
                    clientTui.showMessage("Message: " + inputSrv.toString());
                    break;
                    
                    
            }
        } else {
            clientTui.showMessage("Received and empty server commando");
        }
    }
    
    /**
     * sets the proper color according to the place of the users name.
     * Use of predefined colors in the protocol
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
    
    /**
     * get the color of the marble given the name.
     * @requires the name of the player must be inside the game
     * @param name of the wanted player
     * @return the color of the player
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
    
    /**
     * reads the server while running is true.
     * @throws ServerUnavailableException if the server is not available
     */
    
    public void readServer() throws ServerUnavailableException {
        while (running) {

            String serverMessage = readLineFromServer();
            if (serverMessage.startsWith("x")) {
                running = false; 
            } 
            //handles the command
            handleServerCommands(serverMessage);
            // needs to keep reading the server messages
        }
    }
    
    /**
     * Prints the board to the console.
     * @ensures to print the up to date board
     * @throws BoardException if the board has an error
     */
    public void showBoard() throws BoardException {
        clientTui.showMessage(clientBoard.toString());
        //print scores
        switch (gameSize) {
            case 2:
                //print score player one
                clientTui.showMessage("The score for: " + gamePlayers[0] + "(" 
                        + getPlayerMarble(gamePlayers[0]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[0])));
                //print score player two
                clientTui.showMessage("The score for: " + gamePlayers[1] + "(" 
                        + getPlayerMarble(gamePlayers[1]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[1])));
                //print moves made
                clientTui.showMessage("Turn: " + clientBoard.getTurns() + "out of: " + clientBoard.getMaxTurns());
                break;
            case 3:
                //print score player1 
                clientTui.showMessage("The score for: " + gamePlayers[0] + "(" 
                        + getPlayerMarble(gamePlayers[0]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[0])));
                //print score player 2
                clientTui.showMessage("The score for: " + gamePlayers[1] + "(" 
                        + getPlayerMarble(gamePlayers[1]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[1])));
                //print score player 3
                clientTui.showMessage("The score for: " + gamePlayers[2] + "(" 
                        + getPlayerMarble(gamePlayers[2]).toString()
                        + ")" + "is " + clientBoard.getScore(getPlayerMarble(gamePlayers[2])));
                //print moves made
                clientTui.showMessage("Turn: " + clientBoard.getTurns() + "out of: " + clientBoard.getMaxTurns());
                break;
            case 4:
                //calculate score
                int scoreteam1 = clientBoard.getScore(getPlayerMarble(gamePlayers[0])) 
                    + clientBoard.getScore(getPlayerMarble(gamePlayers[2]));
                int scoreteam2 = clientBoard.getScore(getPlayerMarble(gamePlayers[1])) 
                        + clientBoard.getScore(getPlayerMarble(gamePlayers[3]));
                //print induvidual scores
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
                //print team scores
                clientTui.showMessage("score team 1:" + scoreteam1);
                clientTui.showMessage("score team 2 is: " + scoreteam2); 
                clientTui.showMessage("Turn: " + clientBoard.getTurns() + "out of: " + clientBoard.getMaxTurns());
                break;
            default:
                break;
        }
    }

    /**
     * get a hitn from the AI.
     * @return a String with a valid move
     * @ensures the move to be valid
     */
    public String getHint() {
        //create an ai for the hint
        SmartyAI ai = new SmartyAI(clientBoard, color, this, moveChecker, name);
        return ai.getHint(clientBoard, color, moveChecker);
    }
    
    /**
     * Send a chat message to the server.
     * @param message to send 
     * @throws ServerUnavailableException throws exception if the server is not available
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
        //read line until a handshake is confirmed
        while (!handshakeComplete) {
            String serverMessage = readLineFromServer();
            handleServerCommands(serverMessage);
        }
    }

    @Override
    public void joinQueue(int gamesize) throws ServerUnavailableException {
        sendMessage(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + gamesize + ProtocolMessages.EOC);
        while (!joiningComplete) {
            //read server until join is complete
            clientTui.showMessage("Waiting for join conformaton");
            String serverMessage = readLineFromServer();
            handleServerCommands(serverMessage);
        }
    }

    @Override
    public void sendMove(String playerName, String direction, ArrayList<Integer> marbleIndices)
            throws ServerUnavailableException {

        if (yourTurn) {
            //need to be your turn
            if (gameStarted) {
                //game needs to be started
                ArrayList<Integer> convertIndexes = null;
                //convert protocol indexes to board model indexes
                try {
                    convertIndexes = clientBoard.protocolToIndex(marbleIndices);
                } catch (BoardException e1) {
                    System.out.println(e1.getMessage());
                }

                ArrayList<Integer> allMoved = new ArrayList<>();
                try {
                    //check movechecker before sending
                    allMoved = moveChecker.moveChecker(convertIndexes, direction);
                    ArrayList<Integer> protocolAll = new ArrayList<>();
                    //from board model to protocol
                    protocolAll = clientBoard.indexToProtocol(allMoved);
                    String toSendMarbles = "";
                    //add all marbles to the String 
                    for (int i = 0; i < protocolAll.size(); i++) {
                        toSendMarbles = toSendMarbles + ProtocolMessages.DELIMITER;
                        toSendMarbles = toSendMarbles + protocolAll.get(i);
                    }
                    sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + playerName
                            + ProtocolMessages.DELIMITER + direction + toSendMarbles + ProtocolMessages.EOC);
                } catch (IllegalMoveException e) {
                    //if movechecker does not accept, print why and ask again
                    clientTui.showMessage(e.getMessage() + "please try again");
                } catch (BoardException e) {
                    //print the board exception
                    clientTui.showMessage(e.getMessage());
                } catch (ServerUnavailableException e) {
                    clientTui.showMessage(e.getMessage());
                    closeConnection();
                }

            } else {
                //if game has not started print this
                clientTui.showMessage("The game hasnt started yet");
            }

        } else {
            //if it is not your turn, print, and dont send
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
        closeConnection();

    }

}
