package abalone;

import abalone.client.AbaloneClient;
import abalone.exceptions.BoardException;
import abalone.exceptions.IllegalMoveException;
import abalone.exceptions.ServerUnavailableException;
import java.util.ArrayList;
import java.util.Collections;

public class AttackAI extends AI {

    ArrayList<String> allDirections = new ArrayList<String>();
    SmartyAI smarty; 
    
    /**
     * constructor of the attacker AI.
     * @param board board which he will play
     * @param color color of the player
     * @param client client 
     * @param checker the move checker
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
        
        for (int i = 16; i < 105; i++) {
            if (board.getMarble(i) == color) {
                ownMarbles.add(i);
            }
        }
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
        
        //iff a move is found send it 
        try {
            convertToProtocol = board.indexToProtocol(totalMarbles);
        } catch (BoardException e) {
            System.out.println(e.getMessage());
        }
        
        if (movefound) {
            client.sendMove(name, direction, convertToProtocol);
        }
        
        //if no move was found with its own strategy, "ask" smartAI for a hint   
        if (!movefound) {
            convertToProtocol = new ArrayList<Integer>();
            convertToProtocol.add(smarty.getHintForAiMarbles());
            direction = smarty.getHintForAiDirection();
            client.sendMove(name, direction, convertToProtocol);
        }
       
        

    }
    
    

}
