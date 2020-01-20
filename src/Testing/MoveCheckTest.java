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
		black = new Player(Marble.Black, "Henk");
		white = new Player(Marble.White, "Grietje");
	}

	@Test
	void test2PlayerStraightMoves() {
		board = new Board(2);
		copy = board.deepCopy();
		
		movecheck = new MoveCheck(black, copy);
		//Checks for one marble to empty space
		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(81));
		ArrayList<Integer> list = movecheck.moveChecker(81, Directions.northWest);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for one wrong marble to empty space
		list = movecheck.moveChecker(38, Directions.southEast);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for three marbles to empty space
		test.add(103);
		test.add(92);
		test.add(81);
		list = movecheck.moveChecker(103, 92, 81, Directions.northWest);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for three marbles to empty space in random order
		test.add(103);
		test.add(92);
		test.add(81);
		list = movecheck.moveChecker(92, 103, 81, Directions.northWest);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for three marbles one opponent summito
		copy.setMarble(70, Marble.White);
		test.add(103);
		test.add(92);
		test.add(81);
		test.add(70);
		list = movecheck.moveChecker(103, 92, 81, 70, Directions.northWest);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for invalid four own marbles one opponent summito
		copy.setMarble(70, Marble.Black);
		copy.setMarble(59, Marble.White);
		list = movecheck.moveChecker(103, Directions.northWest);
		assertEquals(test, list);
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
		list = movecheck.moveChecker(70, 103, 59, 81, 92, Directions.northWest);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for three marbles two hidden opponent summito
		test.add(103);
		test.add(92);
		test.add(81);
		test.add(70);
		test.add(59);
		list = movecheck.moveChecker(103, Directions.northWest);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		
		//Checks for empty list for invalid summito
		copy.setMarble(81, Marble.White);
		list = movecheck.moveChecker(103, Directions.northWest);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for empty list for empty spaces in input
		copy.setMarble(81, Marble.Empty);
		System.out.println(copy.toString());
		list = movecheck.moveChecker(103, 92, 81, 70, Directions.northWest);
		assertEquals(test, list);
		test.clear();
		list.clear();
	}

	@Test
    void test3PlayerStraightMoves() {
    	board = new Board(3);
    	Board copy = board.deepCopy();
    	green = new Player(Marble.Green, "Gert");
    	movecheck = new MoveCheck(green, copy);
    	
		//Checks for one marble to empty space
		ArrayList<Integer> test = new ArrayList<Integer>();
		ArrayList<Integer> list = movecheck.moveChecker(103, Directions.east);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks for one marble to empty space
		test.add(89);
		list = movecheck.moveChecker(89, Directions.northEast);
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
		list = movecheck.moveChecker(100, Directions.northEast);
		assertEquals(test, list);

    }

	@Test
    void test4PlayerStraightMoves() {
    	board = new Board(4);
    	Board copy = board.deepCopy();
    	green = new Player(Marble.Green, "Gert");
    	red = new Player(Marble.Red, "Annie");
    	movecheck = new MoveCheck(red, copy);
    	
		//Checks for three marbles not in line
		ArrayList<Integer> test = new ArrayList<Integer>();
		copy.setMarble(48, Marble.Red);
		ArrayList<Integer> list = movecheck.moveChecker(48, 28, 18, Directions.southWest);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		copy.setMarble(49, Marble.Black);
		copy.setMarble(60, Marble.White);
		copy.setMarble(38, Marble.Green);
		
		test.add(16);
		test.add(27);
		test.add(38);
		test.add(49);
		test.add(60);
		
		//Checks if 3-2 summito is valid with marbles from team and two opponents
		list = movecheck.moveChecker(16, Directions.southEast);
		assertEquals(test, list);
		test.clear();
		list.clear();
		
		//Checks if 3-2 summito is invalid if first marble is not own marble
		copy.setMarble(16, Marble.Green);
		list = movecheck.moveChecker(16, Directions.southEast);
		assertEquals(test, list);
		
    }


     @Test
     void test2PlayerSideMoves() {
    		board = new Board(2);
    		copy = board.deepCopy();
    		
    		movecheck = new MoveCheck(black, copy);
    		//Checks for one marble to empty space
    		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(82, 81));
    		ArrayList<Integer> list = movecheck.moveChecker(81, 82, Directions.northWest);
    		assertEquals(test, list);
    		test.clear();
    		list.clear();
    		
    		//Checks for one wrong marble to empty space
    		list = movecheck.moveChecker(38, Directions.southEast);
    		assertEquals(test, list);
    		test.clear();
    		list.clear();
     }
     
     @Test
     void test3PlayerSideMoves() {
 		board = new Board(3);
 		copy = board.deepCopy();
 		
 		movecheck = new MoveCheck(white, copy);
 		//Checks for valid move three marbles to empty space
 		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(47, 57, 67));
 		ArrayList<Integer> list = movecheck.moveChecker(67, 57, 47, Directions.southEast);
 		assertEquals(test, list);
 		test.clear();
 		list.clear();
 		
 		//Checks for invalid move four marbles to empty space
 		list = movecheck.moveChecker(47, 37, 57, 67, Directions.southEast);
 		assertEquals(test, list);
 		test.clear();
 		list.clear();
 		
		//Checks for invalid move three marbles to one occupied space
 		copy.setMarble(58, Marble.Green);
 		list = movecheck.moveChecker(47, 57, 67, Directions.southEast);
 		assertEquals(test, list);
 		copy.setMarble(58, Marble.Empty);
 		test.clear();
 		list.clear();
 		
		//Checks for invalid move three marbles of which middle opponent
 		copy.setMarble(57, Marble.Black);
 		list = movecheck.moveChecker(47, 57, 67, Directions.southEast);
 		assertEquals(test, list);
 		test.clear();
 		list.clear();
     }
     
     @Test
     void test4PlayersSideMoves() {
  		board = new Board(4);
  		copy = board.deepCopy();
  		
  		green = new Player(Marble.Green, "Henk");
  		movecheck = new MoveCheck(green, copy);
  		//Checks for invalid move three marbles with one opponent to empty space
  		//Gets a null pointer exception for some reason????
  		copy.setMarble(82, Marble.Empty);
  		ArrayList<Integer> test = new ArrayList<Integer>();
  		ArrayList<Integer> list = movecheck.moveChecker(93, 81, 69, Directions.northEast);
  		assertEquals(test, list);
  		test.clear();
  		list.clear();
  		
//  		//Checks for invalid move four marbles to empty space
//  		list = movecheck.moveChecker(47, 37, 57, 67, Directions.southEast);
//  		assertEquals(test, list);
//  		test.clear();
//  		list.clear();
     }

}

