package abalone.protocol;

/**
 * Functions that every server should support. These methods are used to
 * construct the messages that are sent to the clients. Some are called directly
 * by the server, some in response to a message by the client
 * 
 * @Author Ruben Ollesch
 */

public interface ServerProtocol {
    /**
     * Answers the hello message from a connected client. A name for the player
     * should be chosen (the given one if available). While the server now knows
     * which functionality the client supports the same is not true the other way
     * around, so this information has to be included in the returned message:
     * HELLO + DELIMITER + chat support + DELIMITER + challenge support +
     * DELIMITER + leaderboard support + DELIMITER + actual player name + EOC
     * 
     * @param playerName  The preferred name of the client
     * @param chat        If the client supports chat functionality
     * @param challenge   If the client supports the challenge
     * @param leaderboard If the client supports the leaderboard
     * @return The message that is sent back to the client
     */
    String handleHello(String playerName, boolean chat, boolean challenge, boolean leaderboard);

    /**
     * Places the given player inside the specified queue and returns a message
     * indicating the queue and its size that the player is now part of.
     * JOIN + DELIMITER + queue + DELIMITER + queue size + EOC
     * 
     * @param playerName The player that wants to join a queue
     * @param gamesize   The queue that the player wants to join
     * @return The message that is sent back to the client
     */
    String handleJoin(String playerName, int gamesize);

    /**
     * After a queue has enough players, a game is started and all that are part of
     * it are notified:
     * GAME_START + DELIMITER + player name 1 + ... + DELIMITER + player name n
     * + EOC
     * 
     * @param playerNames The names of all players of the game that has just started
     * @return The message that the clients are notified with
     */
    String handleGameStart(String[] playerNames);

    /**
     * Once the client whose turn it is sends its move, all clients are notified of
     * it:
     * MOVE + next player's name + DELIMITER + move's player name + DELIMITER +
     * marble 1 + ... + DELIMITER + marble n
     * @param playerName The name of the player that should now send back a move
     * @return The message that is sent back to the client
     */
    String handlePlayerMove(String playerName);

    /**
     * Returns a message that includes the current size of each queue. It is called
     * whenever a client requests it.
     * QUEUE_SIZE + DELIMITER + queue2 size + DELIMITER + queue3 size+
     * DELIMITER +queue4 size + EOC
     * 
     * @return The message that is sent back to the client
     */
    String handleQueueSizeQuery();
}
