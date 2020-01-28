package testing;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import abalone.Board;
import abalone.Marble;
import abalone.exceptions.BoardException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A JUnit test for testing if the board indexes are correctly implemented and connected to data
 * like neighbouring fields and rows and columns.
 * @author Ayla en Sam
 * @version 1.0
 */
class BoardTest {
    private Board board2; 
    private Board board3;
    private Board board4;
    
    @BeforeEach  
    public void setup() {
        board2 = new Board(2);  
        board3 = new Board(3);
        board4 = new Board(4);
    }
    
    @Test
    public void testdeepCopy() {
        //test the deep copy function
        Board copy = board2.deepCopy();
        
        for (int i = 0; i < 121; i++) {
            assertEquals(board2.getMarble(i), copy.getMarble(i));
        }
    } 
    
    @Test 
    public void testInitialized() {

        //test if the board assigns the correct colors
        assertEquals(Marble.Black, board2.getMarble(17));
        assertEquals(Marble.Black, board2.getMarble(18));
        assertEquals(Marble.Black, board2.getMarble(39));
        assertEquals(Marble.White, board2.getMarble(80));
        assertEquals(Marble.White, board2.getMarble(91));
        assertEquals(Marble.White, board2.getMarble(102));
        
        assertEquals(Marble.Black, board3.getMarble(20));
        assertEquals(Marble.Black, board3.getMarble(31));
        assertEquals(Marble.White, board3.getMarble(46));
        assertEquals(Marble.White, board3.getMarble(36));
        assertEquals(Marble.Green, board3.getMarble(100));
        assertEquals(Marble.Green, board3.getMarble(101));
        
        assertEquals(Marble.Red, board4.getMarble(16));
        assertEquals(Marble.Red, board4.getMarble(17));
        assertEquals(Marble.Black, board4.getMarble(53));
        assertEquals(Marble.Black, board4.getMarble(64));
        assertEquals(Marble.White, board4.getMarble(56));
        assertEquals(Marble.White, board4.getMarble(67));
        assertEquals(Marble.Green, board4.getMarble(103));
        assertEquals(Marble.Green, board4.getMarble(104));
        
        assertEquals(2, board2.getPlayerCount());
        assertEquals(3, board3.getPlayerCount());
        assertEquals(4, board4.getPlayerCount());  
    }
    
    @Test
    public void testCenter() {
        assertEquals("r", board2.getDirectionToCenter(56)); 
        
        assertEquals("l", board2.getDirectionToCenter(63));
        
        assertEquals("lr", board2.getDirectionToCenter(37));
        assertEquals("ll", board2.getDirectionToCenter(30));
        assertEquals("ul", board2.getDirectionToCenter(83));
        assertEquals("ur", board2.getDirectionToCenter(80));
    }
    
    @Test
    public void testGetindex() {
        //test if the correct index is returned.
        assertEquals(16, board2.getindex(1, 5));
        assertEquals(60, board2.getindex(5, 5));
        assertEquals(67, board2.getindex(6, 1));
        assertEquals(94, board2.getindex(8, 6));
    }
    
    @Test 
    public void testSetMarble() {
        board2.setMarble(100, Marble.Black);
        assertEquals(board2.getMarble(100), Marble.Black);
        board2.setMarble(50, Marble.White);
        assertEquals(board2.getMarble(50), Marble.White);
        board2.setMarble(10, Marble.Red);
        assertEquals(board2.getMarble(10), Marble.Red);
    }
    
    @Test 
    public void testProtocolToIndex() {
        //test the protocol to own model converter
        try {
            assertEquals(89, board2.protocolToIndex(50));
            assertEquals(104, board2.protocolToIndex(60));
            assertEquals(29, board2.protocolToIndex(8));
            assertEquals(60, board2.protocolToIndex(30));
            assertEquals(37, board2.protocolToIndex(12));
            assertEquals(17, board2.protocolToIndex(1));
        } catch (BoardException e) {
           
            e.printStackTrace();
        }
        
        try {
            assertEquals(89, board2.protocolToIndex(65));
        } catch (BoardException e) {
            assertEquals("The index is out of range", e.getMessage());
           
        }
        
        try {
            ArrayList<Integer> test = new ArrayList<Integer>();
            test.add(12);
            test.add(30);
            ArrayList<Integer> converted = new ArrayList<Integer>();
            converted.add(37);
            converted.add(60);
            assertEquals(converted, board2.protocolToIndex(test));
        } catch (BoardException e) {
            //
        }
        
    }
    
    @Test
    public void testindexToProtocol() {
        try {
            assertEquals(30, board2.indexToProtocol(60));
            assertEquals(0, board2.indexToProtocol(16));
            assertEquals(0, board2.indexToProtocol(12));
        } catch (BoardException e) {
            assertEquals("The index is out of range", e.getMessage());
        }
    }  
    
    
    
    @Test
    public void testneighbours() {
        //tests if the two entered indexes are each others neighbour, return true or false
        assertTrue(board2.isNeighbour(16, 17));
        assertTrue(board2.isNeighbour(60, 50));
        assertTrue(board2.isNeighbour(60, 49));
        assertTrue(board2.isNeighbour(80, 90));
        assertTrue(board2.isNeighbour(92,103));
        assertFalse(board2.isNeighbour(28, 16));
        assertFalse(board2.isNeighbour(50, 38)); 
    }
    
    
    @Test 
    public void testValidField() {
        //test if the field is valid
        for (int i = 0; i < 121; i++) {
            assertTrue(board2.isValidField(i));
        }
        
        for (int i = -100; i < 0; i++) {
            assertFalse(board2.isValidField(i));
        }
        
        for (int i = 123; i < 200; i++) {
            assertFalse(board2.isValidField(i));
        }
    }
    
    @Test
    public void testMove() {
        //move a marble and test if the marble has moved
        ArrayList<Integer> marbles = new ArrayList<Integer>();
        marbles.add(38);
        try {
            board2.move(marbles, "ll");
            assertEquals(Marble.Black, board2.getMarble(48));
            assertEquals(Marble.Empty, board2.getMarble(38));
        } catch (BoardException e) {
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void testScore() {
        board2.addScore(Marble.Black);
        board2.addScore(Marble.Black);
        try {
            assertEquals(2, board2.getScore(Marble.Black));
            assertEquals(2, board2.getScore(Marble.Death));
        } catch (BoardException e) {
            assertEquals("Marble doesnt exist", e.getMessage());
        } 
        
    }
    
    @Test 
    public void testGetNeighbors() {
        //test to see if all the neighbors return
        assertArrayEquals(new int[] {29,31,19,20,40,41}, board2.getNeighbours(30));
        assertArrayEquals(new int[] {49,51,39,40,60,61}, board2.getNeighbours(50));
    }
    
    @Test  
    public void testcolrow() {
        //tests if the entered index returns the right row and column, return true or false
        assertEquals(6, board2.getRow(67));
        assertEquals(9, board2.getRow(101));
        assertEquals(7, board2.getCol(40));
        assertEquals(0, board2.getCol(0));
    }
    
    @Test void testMoves() {
        assertEquals(0, board2.getTurns());
        assertEquals(96, board2.getMaxTurns());
    } 
}
