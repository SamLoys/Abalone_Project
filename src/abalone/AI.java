package abalone;

import abalone.client.AbaloneClient;
import abalone.exceptions.ServerUnavailableException;
import java.util.ArrayList;



public class AI {

    ArrayList<Integer> totalMarbles = new ArrayList<Integer>();
    ArrayList<Integer> convertToProtocol = new ArrayList<Integer>();
    ArrayList<Integer> ownMarbles = new ArrayList<>();
    String direction = null;
    boolean movefound = false; 
    Board board;
    Marble color;
    AbaloneClient client;
    MoveCheck checker;
    String name;
    
    /**
     * Constructor of all the AI's, sets all the variables.
     * @param board board to play
     * @param color color of the marble
     * @param client clients name
     * @param checker checker for the moves
     * @param name name of the client
     */
    public AI(Board board, Marble color, AbaloneClient client, MoveCheck checker, String name) {
        this.board = board;
        this.color = color;
        this.client = client;
        this.checker = checker;
        this.name = name;
    }
    
    /**
     * move method used in the sub classes.
     * @param send make true is the Ai needs to send to the server automatically
     * @throws ServerUnavailableException if server not available
     */
    public void makeMove(boolean send) throws ServerUnavailableException {
        
    }
    
   
}
