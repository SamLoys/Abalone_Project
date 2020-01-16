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

	@BeforeEach
	void setUp() throws Exception {
		board = new Board(2);
		black = new Player(Marble.Black, "Henk");
		white = new Player(Marble.White, "Grietje");
		copy = board.deepCopy();
		movecheck = new MoveCheck(black, copy);
	}

	@Test
	void test2PlayerMoves() {
		//Checks for one marble to empty space
		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(81));
		ArrayList<Integer> list = movecheck.moveChecker(81, Directions.northWest);
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
		
		//Checks for three marbles two opponent summito random order
		copy.setMarble(59, Marble.White);
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

}
