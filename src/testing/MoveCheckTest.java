package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import abalone.Board;
import abalone.Directions;
import abalone.Marble;
import abalone.MoveCheck;
import abalone.exceptions.IllegalMoveException;
import java.util.ArrayList;
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
    ArrayList<Integer> test;
    ArrayList<Integer> list;
    ArrayList<Integer> index;
    
    
    @BeforeEach
    void setUp() throws Exception {
        test = new ArrayList<Integer>();
        list = new ArrayList<>();
        index = new ArrayList<>();
    }
    
    @Test
    void testGetColor() {
        copy = new Board(2).deepCopy();
        movecheck = new MoveCheck(Marble.White, copy);
        assertEquals(Marble.White, movecheck.getColor());
        
    }
    
    @Test
    void testNotOnBoardException() {
        copy = new Board(3).deepCopy();
        movecheck = new MoveCheck(Marble.Green, copy);
        //Checks for one invalid marbles not on board
        index.add(15);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Is not on board");
        }
        index.clear();
        
        //Checks for two invalid marbles not on board
        index.add(15);
        index.add(105);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Is not on board");
        }
        index.clear();
        
        //Checks for three invalid marbles not on board
        index.add(-20);
        index.add(0);
        index.add(1000);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Is not on board");
        }
        index.clear();
        
        //Checks for four invalid marbles not on board
        index.add(-20);
        index.add(0);
        index.add(1000);
        index.add(40);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Is not on board");
        }
        index.clear();
        
        //Checks for five invalid marbles not on board
        index.add(-20);
        index.add(0);
        index.add(1000);
        index.add(40);
        index.add(60);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Is not on board");
        }
        index.clear();
        
        //Checks for zero invalid marbles not on board
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Too little or too many indexes");
        }
        index.clear();
        
        //Checks for six invalid marbles not on board
        index.add(-20);
        index.add(0);
        index.add(1000);
        index.add(40);
        index.add(60);
        index.add(100);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Too little or too many indexes");
        }
        
        
    }
    
    
    @Test
    void test2PlayerStraightMoves() {
        //Tests the straight moves for a 2 player game
        copy = new Board(2).deepCopy();
        movecheck = new MoveCheck(Marble.White, copy);
        //Checks for one marble to empty space
        try {
            list = movecheck.moveChecker(81, Directions.northWest);
        } catch (IllegalMoveException e1) {
            //Not needed to print
        }
        test.add(81);
        assertEquals(test, list);
 
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
        index.add(91);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        index.clear();
        
        //Checks for three valid marbles to empty space, returns 103, 92 and 81
        test.add(103);
        test.add(92);
        test.add(81);
        index.add(103);
        index.add(92);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        index.clear();
        
        
        //Checks for three valid marbles to empty space, returns 103, 92 and 81
        test.add(103);
        test.add(92);
        test.add(81);
        index.add(103);
        index.add(92);
        index.add(81);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        index.clear();
        
        //Checks for three valid marbles to empty space in random order, returns 103, 92, 81
        test.add(103);
        test.add(92);
        test.add(81);
        index.add(92);
        index.add(103);
        index.add(81);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        index.clear();
        
        //Checks for three valid marbles one opponent summito, returns 103, 92, 81, 70
        copy.setMarble(70, Marble.Black);
        test.add(103);
        test.add(92);
        test.add(81);
        test.add(70);
        index.add(92);
        index.add(103);
        index.add(81);
        index.add(70);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        index.clear();
        
        //Checks for invalid four own marbles one opponent summito, returns exception
        copy.setMarble(70, Marble.White);
        copy.setMarble(59, Marble.Black);
        index.add(103);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "push is blocked");
        }
        test.clear();
        list.clear();
        index.clear();
        
        //Checks for three marbles two opponent summito random order, returns 103, 92, 81, 70, 59
        copy.setMarble(59, Marble.Black);
        copy.setMarble(70, Marble.Black);
        test.add(103);
        test.add(92);
        test.add(81);
        test.add(70);
        test.add(59);
        index.add(70);
        index.add(103);
        index.add(81);
        index.add(59);
        index.add(92);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            //Not needed to print
        }
        assertEquals(test, list);
        test.clear();
        list.clear();
        index.clear();
        
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
        copy.reset();
        
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
        copy.reset();
        
        //Checks for valid 2-1 summito, with empty space
        copy.setMarble(81, Marble.Black);
        try {
            list = movecheck.moveChecker(92, 81, 103, Directions.northWest);
        } catch (IllegalMoveException e) {
            //not needed to print
        }
        test.add(103);
        test.add(92);
        test.add(81);
        assertEquals(test, list);
        test.clear();
        list.clear();
        copy.reset();
        
        //Checks for valid 3-1 summito, with death space in end
        copy.setMarble(81, Marble.White);
        copy.setMarble(70, Marble.Black);
        copy.setMarble(59, Marble.Death);
        try {
            list = movecheck.moveChecker(92, 81, 103, 70, Directions.northWest);
        } catch (IllegalMoveException e) {
            //not needed to print
        }
        test.add(103);
        test.add(92);
        test.add(81);
        test.add(70);
        assertEquals(test, list);
        test.clear();
        list.clear();
        copy.reset();
        
        //Checks for valid 3-2 summito, with death space in end
        copy.setMarble(81, Marble.White);
        copy.setMarble(70, Marble.Black);
        copy.setMarble(59, Marble.Black);
        copy.setMarble(48, Marble.Death);
        try {
            list = movecheck.moveChecker(92, 81, 103, 70, 59, Directions.northWest);
        } catch (IllegalMoveException e) {
            //not needed to print
        }
        test.add(103);
        test.add(92);
        test.add(81);
        test.add(70);
        test.add(59);
        assertEquals(test, list);
        test.clear();
        list.clear();
    }

    @Test
      void test3PlayerStraightMoves() {
        //tests straight move in 3 player game
        copy = new Board(3).deepCopy();
        movecheck = new MoveCheck(Marble.Green, copy);
        
        //Checks for one marble to occupied space
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
        copy.setMarble(48, Marble.Red);
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
        try {
            list = movecheck.moveChecker(81, 82, Directions.northWest);
        } catch (IllegalMoveException e) {
                  //Not needed to print
        }
        test.add(82);
        test.add(81);
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
        try {
            list = movecheck.moveChecker(67, 57, 47, Directions.southEast);
        } catch (IllegalMoveException e) {
                //Not needed to print
        }
        test.add(47);
        test.add(57);
        test.add(67);
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
        try {
            list = movecheck.moveChecker(93, 81, 69, Directions.northEast);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "Not all marbles are in your team");
        }
        
        test.clear();
        list.clear();
        
    }
    
    @Test
    void testAllExceptions() {
        copy = new Board(2).deepCopy();
        movecheck = new MoveCheck(Marble.White, copy);
        
        //Tests IllegalMoveException "is not valid single move"
        copy.setMarble(82, Marble.Black);
        index.add(81);
        try {
            list = movecheck.moveChecker(index, Directions.east);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "is not valid single move");
        }
        index.clear();
        
        
        //Tests IllegalMoveException "second marble is not own team"
        copy.setMarble(82, Marble.Black);
        index.add(81);
        index.add(82);
        try {
            list = movecheck.moveChecker(index, Directions.east);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "second marble is not own team");
        }
        index.clear();
        
        
        //Tests IllegalMoveException "third input is death or empty"
        copy.setMarble(81, Marble.Empty);
        index.add(103);
        index.add(92);
        index.add(81);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "third input is death or empty");
        }
        index.clear();
        copy.reset();
        
        //Tests IllegalMoveException "for three input, second is not own team"
        copy.setMarble(81, Marble.Empty);
        copy.setMarble(92, Marble.Black);
        index.add(103);
        index.add(92);
        index.add(81);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "for three input, second marble is not own team");
        }
        index.clear();
        copy.reset();
        
        //Tests IllegalMoveException "for three input, second is not own team"
        copy.setMarble(81, Marble.White);
        copy.setMarble(70, Marble.White);
        index.add(103);
        index.add(92);
        index.add(81);
        index.add(70);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "for four input, fourth marble is not opponenet");
        }
        index.clear();
        copy.reset();
        
        //Tests IllegalMoveException "for three input, second is not own team"
        copy.setMarble(81, Marble.White);
        copy.setMarble(70, Marble.White);
        copy.setMarble(92, Marble.Death);
        index.add(103);
        index.add(92);
        index.add(81);
        index.add(70);
        try {
            list = movecheck.moveChecker(index, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "for four input, second marble is not own team");
        }
        index.clear();
        copy.reset();
        
        //Tests for IllegalMoveException 4 input, "the push is blocked"
        copy.setMarble(81, Marble.White);
        copy.setMarble(70, Marble.Black);
        copy.setMarble(59, Marble.White);
        try {
            list = movecheck.moveChecker(92, 81, 103, 70, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "the push is blocked");
        }
        test.clear();
        list.clear();
        copy.reset();
        
        //Tests for IllegalMoveException 5 input, "the push is blocked"
        copy.setMarble(48, Marble.White);
        copy.setMarble(70, Marble.Black);
        copy.setMarble(59, Marble.Black);
        try {
            list = movecheck.moveChecker(92, 81, 103, 70, 59, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "the push is blocked");
        }
        test.clear();
        list.clear();
        copy.reset();
        
        //Tests for IllegalMoveException 5 input, "fifth marble is not opponent"
        copy.setMarble(48, Marble.White);
        copy.setMarble(70, Marble.Black);
        copy.setMarble(59, Marble.White);
        try {
            list = movecheck.moveChecker(92, 81, 103, 70, 59, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "fifth marble is not opponent");
        }
        test.clear();
        list.clear();
        copy.reset();
        
        //Tests for IllegalMoveException 5 input, "fourth marble is not opponent"
        copy.setMarble(48, Marble.White);
        copy.setMarble(70, Marble.White);
        copy.setMarble(59, Marble.White);
        try {
            list = movecheck.moveChecker(92, 81, 103, 70, 59, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "fourth marble is not opponent");
        }
        test.clear();
        list.clear();
        copy.reset();
        
        //Tests for IllegalMoveException 5 input, "third marble is not own team"
        copy.setMarble(48, Marble.White);
        copy.setMarble(70, Marble.White);
        copy.setMarble(59, Marble.White);
        copy.setMarble(81, Marble.Black);
        try {
            list = movecheck.moveChecker(92, 81, 103, 70, 59, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "third marble is not own team");
        }
        test.clear();
        list.clear();
        copy.reset();
        
        //Tests for IllegalMoveException 5 input, "second marble is not own team"
        copy.setMarble(48, Marble.White);
        copy.setMarble(70, Marble.White);
        copy.setMarble(59, Marble.White);
        copy.setMarble(81, Marble.Black);
        copy.setMarble(92, Marble.Black);
        try {
            list = movecheck.moveChecker(92, 81, 103, 70, 59, Directions.northWest);
        } catch (IllegalMoveException e) {
            assertEquals(e.getMessage(), "second marble is not own team");
        }
        test.clear();
        list.clear();
        copy.reset();
        
        
        
    }
          
}

