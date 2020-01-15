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

	@BeforeEach
	void setUp() throws Exception {
		Board board = new Board(2);
		Player black = new Player(Marble.Black);
		Player white = new Player(Marble.White);
	}

	@Test
	void test() {
		Board board = new Board(2);
		Player black = new Player(Marble.Black);
		Player white = new Player(Marble.White);
		MoveCheck movecheck = new MoveCheck(black, board, 80, 91, 102, Directions.northWest);
	}

}
