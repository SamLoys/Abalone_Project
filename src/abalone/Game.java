package abalone;

import abalone.exceptions.BoardException;
import abalone.exceptions.ClientUnavailableException;
import abalone.exceptions.IllegalMoveException;
import abalone.protocol.ProtocolMessages;
import abalone.server.AbaloneServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The game class, keeps track of the game and can move marble
 * Created on 17-01-2019. 
 * @author Sam Freriks and Ayla van der Wal.
 * @version 1.0
 */
public class Game {

    private Board board;
    private String[] playerNames;
    private AbaloneServer srv;
    private static final int scoreLimit = 6;
    boolean finished = false; 
    private int gameSize;
    private int moves;
    private int maxMoves;
    HashMap<String, MoveCheck> checkmap;
    HashMap<String, Marble> marbleMap;

    /** 
     * Constructor of the game, make a new game with the number of players, server, names of the players.
     * @param amountplayers the amount of players
     * @param srv the server connected to this game
     * @param player1Name name of the first player
     * @param player2Name name of the second player
     * @ensures the set the parameter accordingly
     */
    public Game(int amountplayers, AbaloneServer srv, String player1Name, String player2Name) {
        board = new Board(amountplayers);
        playerNames = new String[2];
        playerNames[0] = player1Name;
        playerNames[1] = player2Name;
        checkmap = new HashMap<String, MoveCheck>();
        checkmap.put(player1Name, new MoveCheck(Marble.White, board));
        checkmap.put(player2Name, new MoveCheck(Marble.Black, board));
        marbleMap = new HashMap<String, Marble>();
        marbleMap.put(player1Name, Marble.White);
        marbleMap.put(player2Name, Marble.Black); 
        moves = board.getTurns();
        maxMoves = board.getMaxTurns();
        gameSize = 2;
        this.srv = srv; 
        
    }

    /** 
     * Constructor of the game, make a new game with the number of players, server, names of the players.
     * @param amountplayers the amount of players
     * @param srv the server connected to this game
     * @param player1Name name of the first player
     * @param player2Name name of the second player
     * @param player3Name name of the third player
     * @ensures the set the parameter accordingly
     */
    public Game(int amountplayers, AbaloneServer srv, String player1Name, String player2Name, String player3Name) {
        board = new Board(amountplayers);
        playerNames = new String[3];
        playerNames[0] = player1Name;
        playerNames[1] = player2Name;
        playerNames[2] = player3Name;
        checkmap = new HashMap<String, MoveCheck>();
        checkmap.put(player1Name, new MoveCheck(Marble.White, board));
        checkmap.put(player2Name, new MoveCheck(Marble.Black, board));
        checkmap.put(player3Name, new MoveCheck(Marble.Green, board));
        marbleMap = new HashMap<String, Marble>();
        marbleMap.put(player1Name, Marble.White);
        marbleMap.put(player2Name, Marble.Black);
        marbleMap.put(player3Name, Marble.Green);
        moves = board.getTurns();
        maxMoves = board.getMaxTurns();
        gameSize = 3;
        this.srv = srv;
        
    }

    /** 
     * Constructor of the game, make a new game with the number of players, server, names of the players.
     * @param amountplayers the amount of players
     * @param srv the server connected to this game
     * @param player1Name name of the first player
     * @param player2Name name of the second player
     * @param player3Name name of the third player
     * @param player4Name name of the fourth player
     * @ensures the set the parameter accordingly
     */
    public Game(int amountplayers, AbaloneServer srv, String player1Name, String player2Name, String player3Name,
            String player4Name) { 
        board = new Board(amountplayers);  
        playerNames = new String[4];
        playerNames[0] = player1Name;
        playerNames[2] = player2Name;
        playerNames[1] = player3Name;
        playerNames[3] = player4Name;
        checkmap = new HashMap<String, MoveCheck>();
        checkmap.put(player1Name, new MoveCheck(Marble.White, board));
        checkmap.put(player2Name, new MoveCheck(Marble.Black, board));
        checkmap.put(player3Name, new MoveCheck(Marble.Green, board));
        checkmap.put(player4Name, new MoveCheck(Marble.Red, board));
        marbleMap = new HashMap<String, Marble>();
        marbleMap.put(player1Name, Marble.White);
        marbleMap.put(player2Name, Marble.Black);
        marbleMap.put(player3Name, Marble.Green);
        marbleMap.put(player4Name, Marble.Red);
        moves = board.getTurns(); 
        maxMoves = board.getMaxTurns();
        gameSize = 4;
        this.srv = srv;
        
    }
    
    /**
     * calls the reset method on the board object.
     */
    public void reset() {
        board.reset();
        
    }

    /**
     * checks if a player /team has reached the score limit, if it is reached, the game finished message will be send.
     * to all the player. 
     * @throws BoardException exception if the board encounters an error
     * @throws IOException exception if the IO encounters an error
     * @throws ClientUnavailableException Exception if the client is not available
     */
    public void checkScore() throws BoardException, IOException, ClientUnavailableException {
        int scoreteam1;
        int scoreteam2;
        int scoreteam3;
        if (moves < maxMoves && finished == false) {
            //first check if the maximumMoves is reached
            switch (gameSize) {
                //is the game consists of two players
                case 2:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
                    System.out.println("score team 1: " + scoreteam1);
                    System.out.println("score team 2: " + scoreteam2);
                    System.out.print("moves: " + moves);
                    if (scoreteam1 == scoreLimit) {
                        //limit reached
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "1" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished to the players
                        srv.multipleSend(msg, playerNames);
                        //remove the game
                        srv.removeGame(this);
                        
                    } else if (scoreteam2 == scoreLimit) {
                        //score limit reached
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "2" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished to the players
                        srv.multipleSend(msg, playerNames);
                        //remove the game
                        srv.removeGame(this);
                        
                    }
                    break;
                //if the game consist of three players    
                case 3:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
                    scoreteam3 = board.getScore(marbleMap.get(playerNames[2]));
                    if (scoreteam1 == scoreLimit) {
                        //limit reached
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "1" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove the game
                        srv.removeGame(this);
                        // player 1 wins
                        
                    } else if (scoreteam2 == scoreLimit) {
                        //score limit reached
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "2" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove the game
                        srv.removeGame(this);
                        // player 2 wins
                        
                    } else if (scoreteam3 == scoreLimit) {
                        //limit reached
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "3" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove the game
                        srv.removeGame(this);
                        // player 2 wins
                        
                    }
                    break;
                //if the game consist of four players
                case 4:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]))
                        + board.getScore(marbleMap.get(playerNames[2]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]))
                            + board.getScore(marbleMap.get(playerNames[3]));
                    if (scoreteam1 == scoreLimit) {
                        //score limit reached
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "1" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove this game
                        srv.removeGame(this);
                        // team 1 wins
                        
                    } else if (scoreteam2 == scoreLimit) {
                        //limit reached
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "2" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove this game
                        srv.removeGame(this);
                        // player 2 wins
                        
                    }
                    break;
                default:
                    //should never happen, game is always a size of 2 , 3 or 4.
                    break;
                    
            }
            
        }
        // if the moves made is bigger or equal as the max moves, and the finished is not yet true.
        if (moves >= maxMoves && finished == false) {
            switch (gameSize) {
                case 2:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
                    if (scoreteam1 == scoreteam2) {
                        // draw
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        
                    } else if (scoreteam1 > scoreteam2) {
                        //team one won
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        // player 1 wins
                        
                    } else if (scoreteam1 < scoreteam2) {
                        //team 2 wins
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "2" + ProtocolMessages.EOC;
                        finished = true;
                        //send message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        // player 2 wins
                        
                    }
                    break;
                case 3:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
                    scoreteam3 = board.getScore(marbleMap.get(playerNames[2]));
                    if (scoreteam1 == scoreteam2 && scoreteam1 == scoreteam3) {
                        // draw
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
                        finished = true;
                        //send message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        
                    } else if (scoreteam1 > scoreteam2 && scoreteam1 > scoreteam3) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        // player 1 wins
                        
                    } else if (scoreteam2 > scoreteam1 && scoreteam2 > scoreteam3) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove game 
                        srv.removeGame(this);
                        // player 2 wins
                        
                    } else if (scoreteam3 > scoreteam1 && scoreteam3 > scoreteam2) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "3" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        
                    } else {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        
                    }
                    break;
                case 4:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]))
                        + board.getScore(marbleMap.get(playerNames[2]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]))
                        + board.getScore(marbleMap.get(playerNames[3]));
                    if (scoreteam1 == scoreteam2) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        
                    }
                    
                    if (scoreteam1 < scoreteam2) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        
                    }
                    if (scoreteam2 < scoreteam1) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
                        finished = true;
                        //send finished message
                        srv.multipleSend(msg, playerNames);
                        //remove game
                        srv.removeGame(this);
                        
                    }
                    break;
                default:
                    break;
                    
            }
            
        }
        
    }

    /**
     * Will check if the player is on turn and check if the move is valid, will move if both true. 
     * @param name of the players
     * @param direction direction of the wanted move
     * @param indexes of all the marbles moving
     * @throws IOException exception if the IO gets an error.
     * @throws ClientUnavailableException exception if the client is not available
     * @throws BoardException exception is there is an error on the board
     * @throws IllegalMoveException throws exception if the move is not legal
     */
    public synchronized void addMove(String name, String direction, ArrayList<Integer> indexes)
            throws IOException, ClientUnavailableException, BoardException, IllegalMoveException {
        // first check if the player has the right to move
        ArrayList<Integer> newIndexes = new ArrayList<>();
        ArrayList<Integer> totalMove = new ArrayList<>();
        ArrayList<Integer> totalMoveToProtocol = new ArrayList<Integer>();
        //check if it is the turn of the player and the game is not yet finished
        if (!finished) {
            if (name.equals(getNextPlayer())) {
                //convert the protocol indexes to the board model indexes
                newIndexes = board.protocolToIndex(indexes);
            
                //check if the indexes pass the moveCheck, it will return all the marble with the move if valid
                totalMove = checkmap.get(name).moveChecker(newIndexes, direction);
                //throw IllegalMoveException if not correct, needs to be send to client

                boolean scores = false;
               
                //actually move the marbles on the board, move is checked above
                scores = board.move(totalMove, direction);
                //scores will become true if the move pushed a marble off. 
                //throws boardException if out of range, needs to be send to client
                 
                if (scores) {
                    //increase the score in the board if a player has pushed a marble off. 
                    board.addScore(marbleMap.get(name));
                    
                }
                //update the amount of moves by getting the amount of turn from the board.
                moves = board.getTurns();
                //convert the board index model back to protocol indexes
                totalMoveToProtocol = board.indexToProtocol(totalMove);
                //get the next player
                String nextplayer = getNextPlayer();
                //construct a message with the next player
                String message = srv.handlePlayerMove(nextplayer);
                String indexesString = "";
                //convert the ArrayList of indexes to a string in the proper way
                for (int i : totalMoveToProtocol) {
                    indexesString = indexesString + ProtocolMessages.DELIMITER;
                    indexesString = indexesString + i;
                    
                }
                //append the indexes to the constructed message
                message = message + name + ProtocolMessages.DELIMITER + direction 
                        + indexesString + ProtocolMessages.EOC;
                //send the message to all the players inside this game
                srv.multipleSend(message, playerNames);
                //check if the game should end
                checkScore();
                //return that the move is accepted and moved, in this game the clientHandler knows the move
                //did not had an exception
             
            } else {
                throw new IllegalMoveException("Not your turn");
                
            } // game finished, no need for exception   
        }  
    }

    /**
     * return next player, the order made in the constructor is kept.
     * @return the name of the players that is next in turn.
     */
    public String getNextPlayer() {
        //normal order
        int tempmoves = moves;
        while (tempmoves >= playerNames.length) {
            tempmoves = tempmoves - playerNames.length;
            
        }
        return playerNames[tempmoves];
        //it goes like [0]  - [1] - [2] - [3] repeat
    }
    
    /**
     * get all players in this game.
     * @return an array with all the players connected to this game
     */
    public String[] getPlayers() {
        return playerNames;
        
    }
    
}
