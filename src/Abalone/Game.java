package Abalone;

import Abalone.Exceptions.BoardException;
import Abalone.Exceptions.ClientUnavailableException;
import Abalone.Exceptions.IllegalMoveException;
import Abalone.Server.AbaloneServer;
import Abalone.protocol.ProtocolMessages;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

    /** Javadoc. what is going on.
     * @param amountplayers Javadoc.
     * @param srv Javadoc.
     * @param player1Name Javadoc.
     * @param player2Name Javadoc.
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

    /** Javadoc.
     * @param players Javadoc.
     * @param srv Javadoc.
     * @param player1Name Javadoc.
     * @param player2Name Javadoc.
     * @param player3Name Javadoc.
     */
    public Game(int players, AbaloneServer srv, String player1Name, String player2Name, String player3Name) {
        board = new Board(players);
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

    /** Javadoc.
     * @param players Javadoc.
     * @param srv Javadoc.
     * @param player1Name Javadoc.
     * @param player2Name Javadoc.
     * @param player3Name Javadoc.
     * @param player4Name Javadoc.
     */
    public Game(int players, AbaloneServer srv, String player1Name, String player2Name, String player3Name,
            String player4Name) {
        board = new Board(players);
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
    
    /** Javadoc.
     */
    public void reset() {
        board.reset();
        
    }

    /** Javadoc.
     * @throws BoardException Javadoc.
     * @throws IOException Javadoc.
     * @throws ClientUnavailableException Javadoc.
     */
    public void checkScore() throws BoardException, IOException, ClientUnavailableException {
        int scoreteam1;
        int scoreteam2;
        int scoreteam3;
        if (moves < maxMoves && finished == false) {
            switch (gameSize) {
                case 2:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
                    System.out.println("score team 1: " + scoreteam1);
                    System.out.println("score team 2: " + scoreteam2);
                    System.out.print("moves: " + moves);
                    if (scoreteam1 == scoreLimit) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" 
                                + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        
                    } else if (scoreteam2 == scoreLimit) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "2" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        
                    }
                    break;
                case 3:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
                    scoreteam3 = board.getScore(marbleMap.get(playerNames[2]));
                    if (scoreteam1 == scoreLimit) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "1" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        // player 1 wins
                        
                    } else if (scoreteam2 == scoreLimit) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "2" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        // player 2 wins
                        
                    } else if (scoreteam3 == scoreLimit) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "2" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        // player 2 wins
                        
                    }
                    break;
                case 4:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]))
                        + board.getScore(marbleMap.get(playerNames[2]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]))
                            + board.getScore(marbleMap.get(playerNames[3]));
                    if (scoreteam1 == scoreLimit) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "1" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        // player 1 wins
                        
                    } else if (scoreteam2 == scoreLimit) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "2" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        // player 2 wins
                        
                    }
                    break;
                default:
                    break;
                    
            }
            
        }
        
        if (moves >= maxMoves) {
            switch (gameSize) {
                case 2:
                    scoreteam1 = board.getScore(marbleMap.get(playerNames[0]));
                    scoreteam2 = board.getScore(marbleMap.get(playerNames[1]));
                    if (scoreteam1 == scoreteam2) {
                        // draw
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        
                    } else if (scoreteam1 > scoreteam2) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        // player 1 wins
                        
                    } else if (scoreteam1 < scoreteam2) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                                + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER 
                                + "2" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
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
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        
                    } else if (scoreteam1 > scoreteam2 && scoreteam1 > scoreteam3) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        // player 1 wins
                        
                    } else if (scoreteam2 > scoreteam1 && scoreteam2 > scoreteam3) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        // player 2 wins
                        
                    } else if (scoreteam3 > scoreteam1 && scoreteam3 > scoreteam2) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "3" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        
                    } else {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.DRAW + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
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
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        
                    }
                    
                    if (scoreteam1 < scoreteam2) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "1" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        
                    }
                    if (scoreteam2 < scoreteam1) {
                        String msg = ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER
                            + ProtocolMessages.GameResult.WIN + ProtocolMessages.DELIMITER + "2" + ProtocolMessages.EOC;
                        finished = true;
                        srv.multipleSend(msg, playerNames);
                        srv.removeGame(this);
                        
                    }
                    break;
                default:
                    break;
                    
            }
            
        }
        
    }

    /** Javadoc.
     * @param name Javadoc.
     * @param direction Javadoc.
     * @param indexes Javadoc.
     * @return Javadoc.
     * @throws IOException Javadoc.
     * @throws ClientUnavailableException Javadoc.
     * @throws BoardException Javadoc.
     */
    public synchronized String addMove(String name, String direction, ArrayList<Integer> indexes)
            throws IOException, ClientUnavailableException, BoardException {
        // first check if the player has the right to move
        ArrayList<Integer> newIndexes = new ArrayList<>();
        ArrayList<Integer> totalMove = new ArrayList<>();
        ArrayList<Integer> totalMoveToProtocol = new ArrayList<Integer>();
        if (name.equals(getNextPlayer()) && finished == false) {
            newIndexes = board.protocolToIndex(indexes);
            try {
                totalMove = checkmap.get(name).moveChecker(newIndexes, direction);
                //this is the return 
            } catch (IllegalMoveException e) {
                return e.getMessage();
                
            }
            boolean scores = false;
            try {
                scores = board.move(totalMove, direction);
        
            } catch (BoardException e) {
                return e.getMessage();
                
            }
            if (scores) {
                board.addScore(marbleMap.get(name));
                
            }
            moves = board.getTurns();
            totalMoveToProtocol = board.indexToProtocol(totalMove);
            String nextplayer = getNextPlayer();
            String message = srv.handlePlayerMove(nextplayer);
            String indexesString = "";
            for (int i : totalMoveToProtocol) {
                indexesString = indexesString + ProtocolMessages.DELIMITER;
                indexesString = indexesString + i;
                
            }
            message = message + name + ProtocolMessages.DELIMITER + direction + indexesString + ProtocolMessages.EOC;
            srv.multipleSend(message, playerNames);
            checkScore();
            return "good";
        } else {
            return "not your turn";
            
        }
        
    }

    /** Javadoc.
     * @return Javadoc.
     */
    public String getNextPlayer() {
        //normal order
        int tempmoves = moves;
        while (tempmoves >= playerNames.length) {
            tempmoves = tempmoves - playerNames.length;
            
        }
        return playerNames[tempmoves];
        //different order with 4 players, 1 - 3 - 2 -4 , because 1 and 2 are in a team and 2 and 3 are in a team
        
    }
    
    /** Javadoc.
     * * @return Javadoc.
     */
    public String[] getPlayers() {
        return playerNames;
        
    }
    
}
