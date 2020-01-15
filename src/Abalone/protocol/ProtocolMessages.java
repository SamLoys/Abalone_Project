package Abalone.protocol;

public class ProtocolMessages {

	/**
	 * Delimiter used to separate arguments sent over the network.
	 */
	public static final String DELIMITER = ";";

	/**
	 * Used to signal the end of a command.
	 */
	public static final String EOC = ";*";

	/**
	 * Message types used by both the clients and the server.
	 */
	public static final String HELLO = "h";
	public static final String JOIN = "j";
	public static final String MOVE = "m";
	public static final String GAME_START = "s";
	public static final String GAME_FINISHED = "f";
	public static final String QUEUE_SIZE = "q";
	public static final String EXIT = "x";


	/**
	 * Used when indicating the direction of a move.
	 */
	public static class Directions {
		public static final String northEast = "ur";
		public static final String northWest = "ul";
		public static final String east = "r";
		public static final String west = "l";
		public static final String southEast = "lr";
		public static final String southWest = "ll";
	}

	/**
	 * Used by the server to indicate the reason for the game ending.
	 */
	public static class GameResult {
		public static final String WIN = "w";
		public static final String DRAW = "d";
		public static final String EXCEPTION = "e";
	}
}
