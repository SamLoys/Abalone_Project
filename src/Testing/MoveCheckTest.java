package Testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Abalone.Board;
import Abalone.Directions;

import Abalone.Exceptions.IllegalMoveException;

import Abalone.Marble;
import Abalone.MoveCheck;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A JUnit test for testing if the player or the client has send a valid move. Returns the list with all indexes
 * that have to be moved if valid, if not valid specific exceptions are thrown.
 * @author Ayla en Sam
 * @version 1.0
 */
class MoveCheckTest {
    MoveCheck movecheck;
    Board copy;
    
    
    @BeforeEach
    void setUp() throws Exception {
    }
    
    @Test
    void test2PlayerStraightMoves() {
        //Tests the straight moves for a 2 player game
        copy = new Board(2).deepCopy();
        movecheck = new MoveCheck(Marble.White, copy);
        //Checks for one marble to empty space
        ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(81));
        ArrayList<Integer> list = new ArrayList<>();
        try {
            list = movecheck.moveChecker(81, Directions.northWest);
        } catch (IllegalMoveException e1) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
 
        //Checks for one wrong marble to empty space
        try {
            list = movecheck.moveChecker(38, Directions.southEast);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "is not a valid straight move, the first marble is not your own");
        }
        test.clear();
        list.clear();
        
        //Checks for valid marble 91 to empty space, returns 91 and 80 as valid marbles
        copy.reset();
        test.add(91);
        test.add(80);
        try {
            list = movecheck.moveChecker(91, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        
        //Checks for three valid marbles to empty space, returns 103, 92 and 81
        test.add(103);
        test.add(92);
        test.add(81);
        try {
            list = movecheck.moveChecker(103, 92, 81, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        //Checks for three valid marbles to empty space in random order, returns 103, 92, 81
        test.add(103);
        test.add(92);
        test.add(81);
        try {
            list = movecheck.moveChecker(92, 103, 81, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        //Checks for three valid marbles one opponent summito, returns 103, 92, 81, 70
        copy.setMarble(70, Marble.Black);
        test.add(103);
        test.add(92);
        test.add(81);
        test.add(70);
        try {
            list = movecheck.moveChecker(103, 92, 81, 70, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        //Checks for invalid four own marbles one opponent summito, returns exception
        copy.setMarble(70, Marble.White);
        copy.setMarble(59, Marble.Black);
        try {
            list = movecheck.moveChecker(103, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "push is blocked");
        }
        test.clear();
        list.clear();
        
        //Checks for three marbles two opponent summito random order, returns 103, 92, 81, 70, 59
        copy.setMarble(59, Marble.Black);
        copy.setMarble(70, Marble.Black);
        test.add(103);
        test.add(92);
        test.add(81);
        test.add(70);
        test.add(59);
        try {
            list = movecheck.moveChecker(70, 103, 59, 81, 92, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        //Checks for three marbles two hidden opponent summito, returns 103, 92, 81, 70, 59
        test.add(103);
        test.add(92);
        test.add(81);
        test.add(70);
        test.add(59);
        try {
            list = movecheck.moveChecker(103, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        
        //Checks for wrong move for invalid summito, returns exception
        copy.setMarble(59, Marble.Black);
        copy.setMarble(70, Marble.Black);
        copy.setMarble(81, Marble.Black);
        try {
            list = movecheck.moveChecker(103, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "push is blocked");
        }
        test.clear();
        list.clear();
        
        //Checks for wrong move for empty spaces in input, returns exception
        copy.setMarble(81, Marble.Empty);
        System.out.println(copy.toString());
        try {
            list = movecheck.moveChecker(103, 92, 81, 70, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "four four input, Third marble is not own team");
        }
        test.clear();
        list.clear();
    }

    @Test
      void test3PlayerStraightMoves() {
        //tests straight move in 3 player game
        copy = new Board(3).deepCopy();
        movecheck = new MoveCheck(Marble.Green, copy);
        
        //Checks for one marble to occupied space
        ArrayList<Integer> test = new ArrayList<Integer>();
        ArrayList<Integer> list = new ArrayList<>();
        try {
            list = movecheck.moveChecker(103, Directions.east);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "direction is not empty");
        }
        
        test.clear();
        list.clear();
        
        //Checks for one marble to empty space, returns 89
        test.add(89);
        try {
            list = movecheck.moveChecker(89, Directions.northEast);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        
        //Tests for hidden 3-2 summito with two different opponents, returns 100, 90, 80, 70, 60
        copy.setMarble(80, Marble.Green);
        copy.setMarble(70, Marble.White);
        copy.setMarble(60, Marble.Black);
        
        test.add(100);
        test.add(90);
        test.add(80);
        test.add(70);
        test.add(60);
        try {
            list = movecheck.moveChecker(100, Directions.northEast);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
         
    }

    @Test
    void test4PlayerStraightMoves() {
        //Tests for straight move in 4 player game
        copy = new Board(4).deepCopy();
        movecheck = new MoveCheck(Marble.Red, copy);
        
        //Checks for three marbles not in line, returns exception
        ArrayList<Integer> test = new ArrayList<Integer>();
        copy.setMarble(48, Marble.Red);
        ArrayList<Integer> list = new ArrayList<>();
        try {
            list = movecheck.moveChecker(48, 28, 18, Directions.southWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "is not inline");
        }
        test.clear();
        list.clear();
        
        //Checks if 3-2 summito is valid with marbles from team and two opponents, returns 16, 27, 38, 49, 60
        copy.setMarble(48, Marble.Empty);
        copy.setMarble(49, Marble.Black);
        copy.setMarble(60, Marble.White);
        copy.setMarble(38, Marble.Green);
        
        test.add(16);
        test.add(27);
        test.add(38);
        test.add(49);
        test.add(60);
        try {
            list = movecheck.moveChecker(16, Directions.southEast);
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        //Checks if 3-2 summito is invalid if first marble is not own marble, returns exception
        copy.setMarble(16, Marble.Green);
        try {
            list = movecheck.moveChecker(16, Directions.southEast);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "is not a valid straight move, the first marble is not your own");
        }
   
    }


    @Test
     void test2PlayerSideMoves() {
        //Tests for side move in 2 player game
        copy = new Board(2).deepCopy();
        movecheck = new MoveCheck(Marble.White, copy);
        
        //Checks for valid two marbles to empty space, returns 82, 81
        ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(82, 81));
        ArrayList<Integer> list = new ArrayList<>();
        try {
            list = movecheck.moveChecker(81, 82, Directions.northWest);
        } catch (IllegalMoveException e) {
                  //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        //Checks for one wrong marble to empty space, returns exception
        try {
            list = movecheck.moveChecker(38, Directions.southEast);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "is not a valid straight move, the first marble is not your own");
        }
        test.clear();
        list.clear();
        
        //Checks for three marbles to occupied space, returns exception
        copy.setMarble(71, Marble.Black);
        try {
            list = movecheck.moveChecker(80, 82, 81, Directions.southEast);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "is not a valid sidestep");
        }
        test.clear();
        list.clear();
        
    }
     
    @Test
     void test3PlayerSideMoves() {
        //tests for side moves in 3 player game
        copy = new Board(3).deepCopy();
        movecheck = new MoveCheck(Marble.White, copy);
        
        //Checks for valid move three marbles to empty space, returns 47, 57, 67
        ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(47, 57, 67));
        ArrayList<Integer> list = new ArrayList<>();
        try {
            list = movecheck.moveChecker(67, 57, 47, Directions.southEast);
        } catch (IllegalMoveException e) {
                //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        
        //Checks for invalid move four marbles to empty space, returns exception
        try {
            list = movecheck.moveChecker(47, 37, 57, 67, Directions.southEast);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "is not a valid sidestep");
        }
        test.clear();
        list.clear();
        
        //Checks for invalid move three marbles to one occupied space, returns exception
        copy.setMarble(58, Marble.Green);
        try {
            list = movecheck.moveChecker(47, 57, 67, Directions.southEast);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "is not a valid sidestep");
        }
        copy.setMarble(58, Marble.Empty);
        test.clear();
        list.clear();
        
        //Checks for invalid move three marbles of which middle opponent, returns exception
        copy.setMarble(57, Marble.Black);
        try {
            list = movecheck.moveChecker(47, 57, 67, Directions.southEast);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Not all marbles are in your team");
        }
        test.clear();
        list.clear();
        
    }
     
    @Test
     void test4PlayersSideMoves() {
        //Tests side moves for a 4 player game
        copy = new Board(4).deepCopy();
        
        movecheck = new MoveCheck(Marble.Green, copy);
        
        //Checks for invalid move three marbles including one opponent to empty space, returns exception
        copy.setMarble(82, Marble.Empty);
        ArrayList<Integer> test = new ArrayList<Integer>();
        ArrayList<Integer> list = new ArrayList<>();
        try {
            list = movecheck.moveChecker(93, 81, 69, Directions.northEast);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Not all marbles are in your team");
        }
        
        test.clear();
        list.clear();
        
    }
          
}

