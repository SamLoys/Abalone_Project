package Abalone.protocol;

import Abalone.Exceptions.ServerUnavailableException;
import java.util.ArrayList;



public interface ClientProtocol {
    /**
     * When a client first connects to the server, it sends a hello message, which.
     * includes some information about itself and the player:
     * HELLO + DELIMITER + supports chat + DELIMITER + supports challenge  +
     * DELIMITER + supports leaderboard + DELIMITER + player name + EOC
     * 
     */
    void handleHandshake(boolean chat, boolean challenge, boolean leaderboard, String playerName)
            throws ServerUnavailableException;

    /**
     * After the handshake, clients should join one of three queues, either for 2, 3.
     * or 4-player games:
     * JOIN + DELIMITER + <queue/> + EOC
     * 
     * @param gamesize What kind of game to join, either a 2, 3 or 4-player game
     */
    void joinQueue(int gamesize) throws ServerUnavailableException;

    /**
     * When the client has come up with a move, it sends it to the server:
     * MOVE + DELIMITER + player name + DELIMITER + direction + DELIMITER +
     * index of field (multiple times) + EOC
     * All marbles involved in the move should be included, each separated by
     * DELIMITER.
     * 
     * @param playerName    The name of the player that made this move
     * @param direction     The direction that the line of marbles should move to.
     * @param marbleIndices All indices of the marbles involved in this move. The
     *                      order of the indices does not matter.
     */
    void sendMove(String playerName, String direction, ArrayList<Integer> marbleIndices)
            throws ServerUnavailableException;

    /**
     * The client can always query the server for the current queue sizes, no matter.
     * its current state:
     * QUEUE_SIZE + EOC
     * 
     */
    void getCurrentQueueSizes() throws ServerUnavailableException;

    /**
     * When a client wants to exit, it should inform the server:.
     * EXIT + EOC
     */
    void sendExit() throws ServerUnavailableException;
}
