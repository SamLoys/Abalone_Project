package Testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Abalone.Board; 

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
    private Board board; 
    
    @BeforeEach  
    public void setup() {
        board = new Board(2);
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
    public void testcolrow() {
        assertEquals(6, board.getRow(67));
        assertEquals(9, board.getRow(101));
        assertEquals(7, board.getCol(40));
        assertEquals(0, board.getCol(0));
    }
}
