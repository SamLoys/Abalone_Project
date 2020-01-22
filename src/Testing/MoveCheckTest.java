package Testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Abalone.Board;
import Abalone.Directions;
import Abalone.Marble;
import Abalone.MoveCheck;
import Abalone.Player;
import Abalone.Exceptions.IllegalMoveException;

class MoveCheckTest {
	MoveCheck movecheck;
	Board board;
	Board copy;
	Player black;
	Player white; 
	Player green; 
	Player red;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test2PlayerStraightMoves() {
		board = new Board(2);
		copy = board.deepCopy();
		
		movecheck = new MoveCheck(Marble.Black, copy);
		//Checks for one marble to empty space
		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(81));
		ArrayList<Integer> list = new ArrayList<>();
		try {
			list = movecheck.moveChecker(81, Directions.northWest);
		} catch (IllegalMoveException e1) {
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
		
		//Checks for valid marble 91 to empty space
		copy.reset();
		test.add(91);
		test.add(80);
		try {
			list = movecheck.moveChecker(91, Directions.northWest);
		} catch (IllegalMoveException e) {
		}
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		
		//Checks for three valid marbles to empty space
		test.add(103);
		test.add(92);
		test.add(81);
		try {
			list = movecheck.moveChecker(103, 92, 81, Directions.northWest);
		} catch (IllegalMoveException e) {
		}
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for three valid marbles to empty space in random order
		test.add(103);
		test.add(92);
		test.add(81);
		try {
			list = movecheck.moveChecker(92, 103, 81, Directions.northWest);
		} catch (IllegalMoveException e) {
		}
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for three valid marbles one opponent summito
		copy.setMarble(70, Marble.White);
		test.add(103);
		test.add(92);
		test.add(81);
		test.add(70);
		try {
			list = movecheck.moveChecker(103, 92, 81, 70, Directions.northWest);
		} catch (IllegalMoveException e) {
		}
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for invalid four own marbles one opponent summito
		copy.setMarble(70, Marble.Black);
		copy.setMarble(59, Marble.White);
		try {
			list = movecheck.moveChecker(103, Directions.northWest);
		} catch (IllegalMoveException e) {
			assertEquals(e.getMessage(), "push is blocked");
		}
		test.clear();
		list.clear();
		
		//Checks for three marbles two opponent summito random order
		copy.setMarble(59, Marble.White);
		copy.setMarble(70, Marble.White);
		test.add(103);
		test.add(92);
		test.add(81);
		test.add(70);
		test.add(59);
		try {
			list = movecheck.moveChecker(70, 103, 59, 81, 92, Directions.northWest);
		} catch (IllegalMoveException e) {
		}
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for three marbles two hidden opponent summito
		test.add(103);
		test.add(92);
		test.add(81);
		test.add(70);
		test.add(59);
		try {
			list = movecheck.moveChecker(103, Directions.northWest);
		} catch (IllegalMoveException e) {
		}
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		
		//Checks for wrong move for invalid summito
		copy.setMarble(59, Marble.White);
		copy.setMarble(70, Marble.White);
		copy.setMarble(81, Marble.White);
		try {
			list = movecheck.moveChecker(103, Directions.northWest);
		} catch (IllegalMoveException e) {
			assertEquals(e.getMessage(), "push is blocked");
		}
		test.clear();
		list.clear();
		
		//Checks for wrong move for empty spaces in input
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
    	board = new Board(3);
    	Board copy = board.deepCopy();
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
		
		//Checks for one marble to empty space
		test.add(89);
		try {
			list = movecheck.moveChecker(89, Directions.northEast);
		} catch (IllegalMoveException e) {
		}
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		
		//Tests for hidden 3-2 summito with two different opponents
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
		}
		assertEquals(test, list);

    }

	@Test
    void test4PlayerStraightMoves() {
    	board = new Board(4);
    	Board copy = board.deepCopy();
    	movecheck = new MoveCheck(Marble.Red, copy);
    	
		//Checks for three marbles not in line
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
		
		//Checks if 3-2 summito is valid with marbles from team and two opponents
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
		
		//Checks if 3-2 summito is invalid if first marble is not own marble
		copy.setMarble(16, Marble.Green);
		try {
			list = movecheck.moveChecker(16, Directions.southEast);
		} catch (IllegalMoveException e) {
			assertEquals(e.getMessage(), "is not a valid straight move, the first marble is not your own");
		}
		
    }


     @Test
     void test2PlayerSideMoves() {
    		board = new Board(2);
    		copy = board.deepCopy();
    		
    		movecheck = new MoveCheck(Marble.Black, copy);
    		
    		//Checks for valid two marbles to empty space
    		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(82, 81));
    		ArrayList<Integer> list = new ArrayList<>();
			try {
				list = movecheck.moveChecker(81, 82, Directions.northWest);
			} catch (IllegalMoveException e) {
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
    		
    		
    		//Checks for three marbles to occupied space
    		board.setMarble(71, Marble.White);
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
 		board = new Board(3);
 		copy = board.deepCopy();
 		
 		movecheck = new MoveCheck(Marble.White, copy);
 		//Checks for valid move three marbles to empty space
 		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(47, 57, 67));
 		ArrayList<Integer> list = new ArrayList<>();
		try {
			list = movecheck.moveChecker(67, 57, 47, Directions.southEast);
		} catch (IllegalMoveException e) {
		}
 		assertEquals(test, list);
 		test.clear();
 		list.clear();
 		
 		//Checks for invalid move four marbles to empty space
 		try {
			list = movecheck.moveChecker(47, 37, 57, 67, Directions.southEast);
		} catch (IllegalMoveException e) {
	 		assertEquals(e.getMessage(), "is not a valid sidestep");
		}
 		test.clear();
 		list.clear();
 		
		//Checks for invalid move three marbles to one occupied space
 		copy.setMarble(58, Marble.Green);
 		try {
			list = movecheck.moveChecker(47, 57, 67, Directions.southEast);
		} catch (IllegalMoveException e) {
	 		assertEquals(e.getMessage(), "is not a valid sidestep");
		}
 		copy.setMarble(58, Marble.Empty);
 		test.clear();
 		list.clear();
 		
		//Checks for invalid move three marbles of which middle opponent
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
  		board = new Board(4);
  		copy = board.deepCopy();
  		
  		movecheck = new MoveCheck(Marble.Green, copy);
  		
  		//Checks for invalid move three marbles including one opponent to empty space
  		copy.setMarble(82, Marble.Empty);
  		ArrayList<Integer> test = new ArrayList<Integer>();
  		ArrayList<Integer> list= new ArrayList<>();
		try {
			list = movecheck.moveChecker(93, 81, 69, Directions.northEast);
		} catch (IllegalMoveException e) {
	  		assertEquals(e.getMessage(), "Not all marbles are in your team");
		}

  		test.clear();
  		list.clear();
  		
//  		//Checks for invalid move four marbles to empty space
//  		list = movecheck.moveChecker(47, 37, 57, 67, Directions.southEast);
//  		assertEquals(test, list);
//  		test.clear();
//  		list.clear();
     }

}

