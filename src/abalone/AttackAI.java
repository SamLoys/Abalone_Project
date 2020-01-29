package abalone;

import abalone.client.AbaloneClient;
import abalone.exceptions.BoardException;
import abalone.exceptions.IllegalMoveException;
import abalone.exceptions.ServerUnavailableException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The AI that is focused on pushing opponent and defending.
 * @author Sam Freriks and Ayla van der Wal
 * @version 1.0
 *
 */
public class AttackAI extends AI {

    ArrayList<String> allDirections = new ArrayList<String>();
    SmartyAI smarty; 
    
    /**
     * constructor of the attacker AI.
     * @param board board which the AI will play
     * @param color color of the player
     * @param client client link to the AI
     * @param checker The move checker the AI should use
     * @param name name of the AI
     */
    public AttackAI(Board board, Marble color, AbaloneClient client, MoveCheck checker, String name) {
        super(board, color, client, checker, name);
        allDirections.add(Directions.east);
        allDirections.add(Directions.west);
        allDirections.add(Directions.northEast);
        allDirections.add(Directions.northWest);
        allDirections.add(Directions.southEast);
        allDirections.add(Directions.southWest);
        smarty = new SmartyAI(board, color, client, checker, name); 
        //create new SmartyAI for assistance
    }
    
    /**
     * Construct an attack move and send it. 
     */
    public void makeMove(boolean send) throws ServerUnavailableException {
        totalMarbles = new ArrayList<Integer>();
        convertToProtocol = new ArrayList<Integer>();
        ownMarbles = new ArrayList<>();
        direction = null;
        movefound = false;
        //take all the marbles the AI owns
        for (int i = 16; i < 105; i++) {
            if (board.getMarble(i) == color) {
                ownMarbles.add(i);
            }
        }
        //shuffle the Marbles to make the Ai less predictable
        Collections.shuffle(ownMarbles);
        Collections.shuffle(allDirections);
        
        //first check if the AI can move a total of 5 marbles
        for (int index : ownMarbles) {
            for (String direc : allDirections) {
                if (!movefound) {
                    try {
                        totalMarbles = checker.moveChecker(index, direc);
                        if (totalMarbles.size() == 5) {
                            movefound = true; 
                            direction = direc;
                        }
                    } catch (IllegalMoveException e) {
                      //continue to the next move
                    }
                }
            }  
        }
        
        //second check if the AI can move a total of 4 Marbles
        for (int index : ownMarbles) {
            for (String direc : allDirections) {
                if (!movefound) {
                    try {
                        //the checker always returns all the marbles that are going to move
                        totalMarbles = checker.moveChecker(index, direc);
                        if (totalMarbles.size() == 4) {
                            movefound = true; 
                            direction = direc;
                        }
                    } catch (IllegalMoveException e) {
                      //continue to the next move
                    }
                }
            }  
        }
        
        
        //convert the found marbles back to protocol marbles
        try {
            convertToProtocol = board.indexToProtocol(totalMarbles);
        } catch (BoardException e) {
            System.out.println(e.getMessage());
        }
        //iff a move is found send it 
        if (movefound) {
            
            client.sendMove(name, direction, convertToProtocol);
        }
        
        //if no move was found with its own strategy, "ask" smartAI for a hint   
        if (!movefound) {
            convertToProtocol = new ArrayList<Integer>();
            //SmartyAi already sends the protocol ready indices
            convertToProtocol.add(smarty.getHintForAiMarbles());
            direction = smarty.getHintForAiDirection();
            client.sendMove(name, direction, convertToProtocol);
            //send the move
        }
    }
}
