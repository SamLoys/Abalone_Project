package Testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Abalone.*; 

 class BoardTest {

		private Board board; 
		
		@BeforeEach  
		public void setup() {
			board = new Board(2);
		}
		
		@Test
		public void testSummito() {
			assertTrue( board.isSummito(80, 81, Directions.west) ); 
			assertTrue(board.isSummito(80, 81, Directions.east));
			assertFalse(board.isSummito(80, 81, Directions.northEast));
			assertFalse(board.isSummito(80, 81, Directions.southEast));
			assertFalse(board.isSummito(80, 81, Directions.northWest));
			assertFalse(board.isSummito(80, 81, Directions.southWest));
		}
		
		@Test
		public void testneighbours() {
			assertTrue(board.isNeighbour(16, 17));
			assertTrue(board.isNeighbour(60, 50));
			assertTrue(board.isNeighbour(60, 49));
			assertTrue(board.isNeighbour(80, 90));
			assertTrue(board.isNeighbour(92,103));
			assertFalse(board.isNeighbour(28, 16));
			assertFalse(board.isNeighbour(50, 38));
			
		}
		
		@Test
		public void testorientation() {
			assertEquals(3, board.getOrientation(37, 36)); 
			assertEquals(3, board.getOrientation(37, 36, 38)); 
			assertEquals(1, board.getOrientation(48, 70, 59)); 
			assertEquals(9999, board.getOrientation(79, 60, 84)); 
		}
		
		@Test  
		public void testcolrow() {
			assertEquals(6, board.getRow(67));
			assertEquals(9, board.getRow(101));
			assertEquals(7, board.getCol(40));
			assertEquals(0, board.getCol(0));
		}
		
		@Test
		public void attackMoveTest() {
			board.attackMove(39, 40, Directions.west);
			assertEquals(Marble.White, board.getMarble(37));
			board.attackMove(17, 28, Directions.southEast);
			assertEquals(Marble.White, board.getMarble(39));
			board.attackMove(29, 30, Directions.east);
			System.out.println(board.toString());
			assertEquals(1, board.getScore(Marble.White));
			
		}
		

	}

	


