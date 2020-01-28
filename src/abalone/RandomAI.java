package abalone;

import abalone.client.AbaloneClient;
import abalone.exceptions.BoardException;
import abalone.exceptions.IllegalMoveException;
import abalone.exceptions.ServerUnavailableException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The randomAI , the AI.
 * Created on 17-01-2019. 
 * @author Sam Freriks and Ayla van der Wal.
 * @version 1.0
 */
public class RandomAI extends AI {

    ArrayList<String> allDirections = new ArrayList<String>();
    
    /**
     * the constructor of the ai.
     * Creates a new AI with the given board, color, client, checker and name.
     * @param board the board the ai player will play on
     * @param color  the color the ai represents
     * @param client the client the ai is. 
     * @param checker the checker for the move checks. 
     * @param name the name of the ai
     */
    public RandomAI(Board board, Marble color, AbaloneClient client, MoveCheck checker, String name) {
        super(board,color,client,checker,name);
        
        allDirections.add(Directions.east);
        allDirections.add(Directions.west);
        allDirections.add(Directions.northEast);
        allDirections.add(Directions.northWest);
        allDirections.add(Directions.southEast);
        allDirections.add(Directions.southWest);
    }
    
    /**
     * constructs a move and send a command to the client to send it.
     * @throws ServerUnavailableException throws this exception is the server could not be reached.
     */
    public void makeMove(boolean send) throws ServerUnavailableException {

        totalMarbles = new ArrayList<Integer>();
        convertToProtocol = new ArrayList<Integer>();
        ownMarbles = new ArrayList<>();
        direction = null;
        movefound = false;

        for (int i = 16; i < 105; i++) {
            if (board.getMarble(i) == color) {
                ownMarbles.add(i);
            }
        }
        Collections.shuffle(ownMarbles);
        Collections.shuffle(allDirections);
        for (int index : ownMarbles) {
            for (String direc : allDirections) {
                if (!movefound) {
                    try {
                        totalMarbles = checker.moveChecker(index, direc);
                        movefound = true; 
                        direction = direc;
                    } catch (IllegalMoveException e) {
                      //continue to the next move
                    }
                }
            }
            
        }

        try {
            convertToProtocol = board.indexToProtocol(totalMarbles);
        } catch (BoardException e) {
            System.out.println(e.getMessage());
        }
       
        client.sendMove(name, direction, convertToProtocol);
     

    }
}
