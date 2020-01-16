package Testing;

import static org.junit.jupiter.api.Assertions.*;

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
	Board practice;
	Player black;
	Player white;

	@BeforeEach
	void setUp() throws Exception {
		board = new Board(2);
		black = new Player(Marble.Black);
		white = new Player(Marble.White);
		movecheck = new MoveCheck(black, board);
		practice = board.deepCopy();
	}

	@Test
	void test() {
		String s = movecheck.moveChecker(1, Directions.northEast);
		assertEquals(s, "Index not on board");
	}

}
