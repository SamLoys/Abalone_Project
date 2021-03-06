package abalone;

import abalone.exceptions.IllegalMoveException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A class that checks whether the given indexes are a valid move on the board, based on 
 * the current marble and board status. Returns an ArrayList with all indexes that should be moved if valid,
 * if not valid the class returns specific IllegalMoveExceptions.
 * @invariant index != null
 * @invariant 0 < index && index <= 6
 * @invariant color != null
 * @invariant direction != null
 * @author Ayla en Sam
 * @version 1.0
 */
public class MoveCheck {
    Board board;
    ArrayList<Integer> index;
    Marble color;

    /** Constructor receives values for the marble color and the current board status.
     * @param color the current player trying to make a move, 
     * @param board the current board state
     * @requires color != null
     * @requires board != null
     * @ensures getColor() == color
     */
    public MoveCheck(Marble color, Board board) {
        this.board = board;
        this.color = color;    
    }

    /** Returns the color of the current player using the movecheck.
     * @return color of the current player
     */
    public Marble getColor() {
        return color;
        
    }
    
    /** adds the given indexs to an arraylist, sorts them and finalizes the list by adding hidden summito marbles 
     * when a player enters less than the total amount of marbles to be moved. Finally the full list is compared 
     * to valid moves and sends back all the marbles that should be moved. 
     * @param i1 the first index as an input
     * @param direction the direction as an input
     * @return ArrayList with valid moves
     * @requires i1 != null
     * @requires direction is valid direction
     * @ensures 0 < index && index <= 6
     * @throws IllegalMoveException if index is not on the board
     */
    public ArrayList<Integer> moveChecker(int i1, String direction) throws IllegalMoveException {
        index = new ArrayList<Integer>();
        index.add(i1);
        if (isOnBoard(index)) {
            Collections.sort(index);
            ArrayList<Integer> newList = completeList(index, direction);
            ArrayList<Integer> returnList = returnMoves(newList, direction);
            return returnList;
            
        } else {
            throw new IllegalMoveException("Is not on board");           
        }
       
    }
    
    /** adds the given indices to an ArrayList, sorts them and finalizes the list by adding hidden summito marbles 
     * when a player enters less than the total amount of marbles to be moved. Finally the full list is compared 
     * to valid moves and sends back all the marbles that should be moved. 
     * @param i1 the first index as an input
     * @param i2 the second index as an input
     * @param direction the direction as an input
     * @requires i1, i2 != null
     * @requires direction is a valid direction
     * @ensures 0 < index && index <= 6
     * @return ArrayList with valid moves
     * @throws IllegalMoveException if the index is not on the board
     */
    public ArrayList<Integer> moveChecker(int i1, int i2, String direction) throws IllegalMoveException {
        index = new ArrayList<Integer>();
        index.add(i1);
        index.add(i2);
        if (isOnBoard(index)) {
            Collections.sort(index);
            ArrayList<Integer> newList = completeList(index, direction);
            ArrayList<Integer> returnList = returnMoves(newList, direction);
            return returnList;
            
        } else {
            throw new IllegalMoveException("Is not on board");
            
        }
        
    }
    
    /** adds the given indices to an ArrayList, sorts them and finalizes the list by adding hidden summito marbles 
     * when a player enters less than the total amount of marbles to be moved. Finally the full list is compared 
     * to valid moves and sends back all the marbles that should be moved. 
     * @param i1 the first index as input
     * @param i2 the second index as input
     * @param i3 the third index as input
     * @param direction the direction as input
     * @requires i1, i2, i3 != null
     * @requires valid direction
     * @ensures 0 < index && index <= 6
     * @return ArrayList with valid moves
     * @throws IllegalMoveException if index is not on board
     */
    public ArrayList<Integer> moveChecker(int i1, int i2, int i3, String direction) throws IllegalMoveException {
        index = new ArrayList<Integer>();
        index.add(i1);
        index.add(i2);
        index.add(i3);
        if (isOnBoard(index)) {
            Collections.sort(index);
            ArrayList<Integer> newList = completeList(index, direction);
            ArrayList<Integer> returnList = returnMoves(newList, direction);
            return returnList;
            
        } else {
            throw new IllegalMoveException("Is not on board");
            
        }
        
    }
    
    /** adds the given indices to an ArrayLit, sorts them and finalizes the list by adding hidden summito marbles 
     * when a player enters less than the total amount of marbles to be moved. Finally the full list is compared 
     * to valid moves and sends back all the marbles that should be moved. 
     * @param i1 the first index as input
     * @param i2 the second index as input
     * @param i3 the third index as input
     * @param i4 the fourth index as input
     * @requires i1, i2, i3, i4 != null
     * @requires direction is valid direction
     * @ensures 0 < index && index <= 6
     * @param direction the direction as input
     * @return an ArrayList with valid moves
     * @throws IllegalMoveException if index is not on board
     */
    public ArrayList<Integer> moveChecker(int i1, int i2, int i3, int i4, String direction)
            throws IllegalMoveException {
        index = new ArrayList<Integer>();
        index.add(i1);
        index.add(i2);
        index.add(i3);
        index.add(i4);
        if (isOnBoard(index)) {
            Collections.sort(index);
            ArrayList<Integer> newList = completeList(index, direction);
            ArrayList<Integer> returnList = returnMoves(newList, direction);
            return returnList;
            
        } else {
            throw new IllegalMoveException("Is not on board");
            
        }
        
    }
    
    /** adds the given indexs to an arraylist, sorts them and finalizes the list by adding hidden summito marbles 
     * when a player enters less than the total amount of marbles to be moved. Finally the full list is compared 
     * to valid moves and sends back all the marbles that should be moved. 
     * @param i1 the first index as input
     * @param i2 the second index as input
     * @param i3 the third index as input
     * @param i4 the fourth index as input
     * @param i5 the fifth index as input
     * @requires i1, i2, i3, i4, i5 != null
     * @requires direction is a valid direction
     * @ensures 0 < index && index <= 6
     * @param direction the direction as input
     * @return an ArrayList with valid moves
     * @throws IllegalMoveException if the index is not on board
     */
    public ArrayList<Integer> moveChecker(int i1, int i2, int i3, int i4, int i5, String direction)
            throws IllegalMoveException {
        index = new ArrayList<Integer>();
        index.add(i1);
        index.add(i2);
        index.add(i3);
        index.add(i4);
        index.add(i5);
        if (isOnBoard(index)) {
            Collections.sort(index);
            ArrayList<Integer> newList = completeList(index, direction);
            ArrayList<Integer> returnList = returnMoves(newList, direction);
            return returnList;
            
        } else {
            throw new IllegalMoveException("Is not on board");
            
        }
        
    }
    
    
    /** adds the given indexs to an arraylist, sorts them and finalizes the list by adding hidden summito marbles 
     * when a player enters less than the total amount of marbles to be moved. Finally the full list is compared 
     * to valid moves and sends back all the marbles that should be moved. 
     * @param index an ArrayList with the indexes as given input
     * @param direction the direction as a valid input
     * @requires index.get(i) != null
     * @requires direction is a valid direction
     * @return an ArrayList with valid moves
     * @throws IllegalMoveException if ArrayList.size() < 1 || ArrayList.size() > 5
     */
    public ArrayList<Integer> moveChecker(ArrayList<Integer> index, String direction) throws IllegalMoveException {
        switch (index.size()) {
            case 1:
                return moveChecker(index.get(0), direction);
            case 2:
                return moveChecker(index.get(0), index.get(1), direction);
            case 3:
                return moveChecker(index.get(0), index.get(1), index.get(2), direction);
            case 4:
                return moveChecker(index.get(0), index.get(1), index.get(2), index.get(3), direction);
            case 5:
                return moveChecker(index.get(0), index.get(1), index.get(2), index.get(3), index.get(4), direction);
            default:
                throw new IllegalMoveException("Too little or too many indexes");
                // this should not happen
                
        }
        
    }
    
    /** checks if the indexes in the complete input list are a valid move.
     * @param index the complete list of indexes
     * @param direction the given direction
     * @return an ArrayList with all valid indexes to be moved.
     * @throws IllegalMoveException when the move is not valid
     */
    public ArrayList<Integer> returnMoves(ArrayList<Integer> index, String direction) throws IllegalMoveException {
        ArrayList<Integer> all = new ArrayList<Integer>();
        if (isInLine(index)) {
            // Checks if given indexes are all in line
            if (isStraightMove(index, direction)) {
                // Checks if in line indexes are a straight move
                if (isOwnMarble(index.get(0))) {
                    // Checks if the first index of the move is own marble
                    switch ((index.size() - 1)) {
                        case 1:
                            // for move of size 1, checks if the last marble is empty, else exception
                            if (board.getMarble(index.get(1)) == Marble.Empty) {
                                all.add(index.get(0));   
                            } else {
                                throw new IllegalMoveException("is not valid single move");
                                
                            }
                            break;
                        case 2:
                            // for move of size 2, checks if second marble is own team and 
                            // the last marble is empty, else exception
                            if (isOwnTeam(index.get(1))) {
                                if (board.getMarble(index.get(2)) == Marble.Empty) {
                                    all.add(index.get(0));
                                    all.add(index.get(1));                               
                                } else {
                                    throw new IllegalMoveException("direction is not empty");              
                                }
                            } else {
                                throw new IllegalMoveException("second marble is not own team");
                                
                            }
                            break;
                        case 3:
                            // for move of size 3, checks if (second marble is own team &&
                            // third marble is own team && the last marble is empty) || (second marble is own team &&
                            // third marble is opponent && (the last marble is empty || death), else exception
                            if (isOwnTeam(index.get(1))) {
                                if (isOwnTeam(index.get(2))) {
                                    if (board.getMarble(index.get(3)) == Marble.Empty) {
                                        all.add(index.get(0));
                                        all.add(index.get(1));
                                        all.add(index.get(2));                   
                                    } else {
                                        throw new IllegalMoveException("push is blocked");              
                                    }        
                                } else if (isOpponent(index.get(2))) {
                                    if (board.getMarble(index.get(3)) == Marble.Empty
                                            || board.getMarble(index.get(3)) == Marble.Death) {
                                        all.add(index.get(0));
                                        all.add(index.get(1));
                                        all.add(index.get(2));
                                        
                                    } else {
                                        throw new IllegalMoveException("push is blocked");
                                        
                                    }
                                    
                                } else {
                                    throw new IllegalMoveException("third input is death or empty");
                                    
                                }
                                
                            } else {
                                throw new IllegalMoveException("for three input, second marble is not own team");
                                
                            }
                            break;
                        case 4:
                            // for move of size 4, checks if second marble is own team &&
                            // third marble is own team && the fourth marble is opponent && 
                            // (last marble is death || empty), else exception
                            if (isOwnTeam(index.get(1))) {
                                if (isOwnTeam(index.get(2))) {
                                    if (isOpponent(index.get(3))) {
                                        if (board.getMarble(index.get(4)) == Marble.Empty
                                                || board.getMarble(index.get(4)) == Marble.Death) {
                                            all.add(index.get(0));
                                            all.add(index.get(1));
                                            all.add(index.get(2));
                                            all.add(index.get(3));
                                            
                                        } else {
                                            throw new IllegalMoveException("the push is blocked");
                                            
                                        }
                                        
                                    } else {
                                        throw new IllegalMoveException("for four input, "
                                                + "fourth marble is not opponenet");
                                        
                                    }
                                    
                                } else {
                                    throw new IllegalMoveException("four four input, Third marble is not own team");
                                    
                                }
                                
                            } else {
                                throw new IllegalMoveException("for four input, second marble is not own team");
                                
                            }
                            break;
                        case 5:
                            // for move of size 5, checks if second marble is own team &&
                            // third marble is own team && the fourth marble is opponent && 
                            // fifth marble is opponent &&
                            // (last marble is death || empty), else exception
                            if (isOwnTeam(index.get(1))) {
                                if (isOwnTeam(index.get(2))) {
                                    if (isOpponent(index.get(3))) {
                                        if (isOpponent(index.get(4))) {
                                            if (board.getMarble(index.get(5)) == Marble.Empty
                                                    || board.getMarble(index.get(5)) == Marble.Death) {
                                                all.add(index.get(0));
                                                all.add(index.get(1));
                                                all.add(index.get(2));
                                                all.add(index.get(3));
                                                all.add(index.get(4));
                                                
                                            } else {
                                                throw new IllegalMoveException("the push is blocked");
                                                
                                            }
                                            
                                        } else {
                                            throw new IllegalMoveException("fifth marble is not opponent");
                                            
                                        }
                                        
                                    } else {
                                        throw new IllegalMoveException("fourth marble is not opponent");
                                        
                                    }
                                    
                                } else {
                                    throw new IllegalMoveException("third marble is not own team");
                                    
                                }
                                
                            } else {
                                throw new IllegalMoveException("second marble is not own team");
                                
                            }
                            break;
                        default: 
                            break;
                    }
                    
                } else {
                    throw new IllegalMoveException("is not a valid straight move, the first marble is not your own");
                    
                }
                
            } else {
                // if not straight move, input will be checked for valid side move
                if ((hasOwnMarble(index)) && (index.size() < 4) && (isValidSideStep(index, direction))) {
                    //checks if index.size() < 4, has at least one own marble and if indexes for direction
                    // are empty
                    int marbles = 0;
                    for (int i = 0; i < index.size(); i++) {
                        //counts the amount of marbles that are in own team
                        if (!(isOwnTeam(index.get(i)))) {
                            throw new IllegalMoveException("Not all marbles are in your team");
                            
                        } else {
                            marbles++;
                            
                        }
                        
                    }
                    if (index.size() == marbles) {
                        // if amount of marbles in own team == index.size(),
                        // add indexes to list for valid move, else exception
                        for (int j = 0; j < index.size(); j++) {
                            all.add(index.get(j));             
                        }
                        
                    }
                    
                } else {
                    throw new IllegalMoveException("is not a valid sidestep");
                    
                }
                
            }
            
        }
        if (all.isEmpty()) {
            throw new IllegalMoveException("is not inline");  
        }
        return all;
        
    }


    /** Checks if all indexes in the ArrayList are between 16 and 104.
     * @param index the indexes as an input
     * @return true if all indexes on the board, else false
     */
    public boolean isOnBoard(ArrayList<Integer> index) {
        for (int i : index) {
            if (!(15 < i && i < 105)) {
                return false;
                
            }
            
        }
        return true;
        
    }

    /** Checks if the given 2 or 3 indexes are in the same orientation as the given direction.
     * @param index the given indexes
     * @param direction the direction as an input
     * @return true if the marbles are in the same direction as given direction, else false
     */
    public boolean isStraightMove(ArrayList<Integer> index, String direction) {
        int givenOrientation = getOrientation(direction);
        int indexOrientation = getOrientation(getDirection(index.get(0), index.get(1)));
        if (givenOrientation == indexOrientation) {
            return true;
            
        }
        return false;
        
    }

    /** Reverses the list to get the front marble on index 0 for the direction west, northwest or northeast. 
     * @param index the indexes as an input
     * @param direction the direction as an input
     * @return an ArrayList with reversed order for Directions.west, Directions.northWest or Directions.northEast
     */
    public ArrayList<Integer> flipList(ArrayList<Integer> index, String direction) {
        if (direction.equals(Directions.west) || direction.equals(Directions.northWest)
                || direction.equals(Directions.northEast)) {
            Collections.sort(index, Collections.reverseOrder());
            
        }
        return index;
        
    }

    /** 1 if orientation is NW or SE 2 if orientation is NE or SW 3 if
     * orientation is E or W if something goes wrong return 9999.
     * @param direction the direction as input
     * @return the orientation for a given direction
     */
    public int getOrientation(String direction) {
        if (direction.equals(Directions.west) || direction.equals(Directions.east)) {
            return 3;
            
        }
        if (direction.equals(Directions.northEast) || direction.equals(Directions.southWest)) {
            return 2;
            
        }
        if (direction.equals(Directions.southEast) || direction.equals(Directions.northWest)) {
            return 1;
            
        }
        return 9999;
        
    }
    
    /** Checks if all indexes in the given direction are empty, given that != isStraightMove().
     * @param index the indexes as input
     * @param direction the direction as input
     * @requires direction is valid direction
     * @return true if one index in given direction != empty, else true
     */
    public boolean isValidSideStep(ArrayList<Integer> index, String direction) {
        Marble[] marble = new Marble[index.size()];
        int number = 0;
        for (int i : index) {
            marble[number] = board.getMarble(getNeighbourIndex(i, direction));
            number++;
            
        }
        for (Marble m : marble) {
            if (!(m == Marble.Empty)) {
                return false;
                
            }
          
        }
        return true;
        
    }
    
    /** Gives the direction for two indexes as a parameter.
     * @param i1 the first marble as an input
     * @param i2 the second marble as an input
     * @return the direction for the given input, empty String if there is no direction
     */
    public String getDirection(int i1, int i2) {
        if (getWest(i1) == i2) {
            return Directions.west;
        } else if (getEast(i1) == i2) {
            return Directions.east;
        } else if (getNorthWest(i1) == i2) {
            return Directions.northWest;
        } else if (getNorthEast(i1) == i2) {
            return Directions.northEast;
        } else if (getSouthWest(i1) == i2) {
            return Directions.southWest;
        } else if (getSouthEast(i1) == i2) {
            return Directions.southEast;
        } else {
            return "";
            
        }
        
    }
    
    
    /** Returns the neighbour index of the index in the given direction.
     * @param i the index as an input
     * @param direction a valid direction
     * @requires direction is a valid direction
     * @return returns the index of the neighbour index in the given direction, 9999 if direction not valid
     */
    public int getNeighbourIndex(int i, String direction) {
        switch (direction) {
            case Directions.west:
                i = i - 1;
                return i;
            case Directions.east:
                i = i + 1;
                return i;
            case Directions.northWest:
                i = i - 11;
                return i;
            case Directions.northEast:
                i = i - 10;
                return i;
            case Directions.southWest:
                i = i + 10;
                return i;
            case Directions.southEast:
                i = i + 11;
                return i;
            default: 
                break;
                
        }
        return 9999;
        
    }
    
    
    /** checks if the given indexes are all in the same direction.
     * @param index the indexes as input
     * @return true if all indexes are in line, else false
     */
    public boolean isInLine(ArrayList<Integer> index) {
        boolean valid = true;
        if (index.size() == 1) {
            // If index.size() == 1, always return true
            return true;
            
        } else if (index.size() == 2) {
            // If index.size() == 2, if second index is a neighbor of first, return true, else false
            int[] neighbours = board.getNeighbours(index.get(0));
            for (int i = 0; i < neighbours.length; i++) {
                if (index.get(1) == neighbours[i]) {
                    valid = true;
                }
                
            }
            
        } else {
            // Checks if the direction of all the numbers is the same
            for (int i = 0; i < (index.size() - 2); i++) {
                String direction1 = getDirection(index.get(i), index.get(i + 1));
                String direction2 = getDirection(index.get(i + 1), index.get(i + 2));
                if (!(direction1 == direction2)) { 
                    return false; 
                } 
               
            }
            
        }
        return valid;
        
    }
    
    /** checks if the ArrayList has at least one own marble.
     * @param index the index as an input
     * @return true if at least one marble is the same as the current player
     */
    public boolean hasOwnMarble(ArrayList<Integer> index) {
        for (int i : index) {
            if (board.getMarble(i) == color) {
                return true;
                
            }
            
        }
        return false;
        
    }
    
    /** Gets the index east to the given index.
     * @param index the index as an input
     * @return index + 1
     */
    public int getEast(int index) {
        return index + 1;
        
    }
    
    /** Gets the index west to the given index.
     * @param index the index as an input
     * @return index - 1
     */
    public int getWest(int index) {
        index = index - 1;
        return index;
        
    }
    
    /** Gets the index northeast to the given index.
     * @param index the index as an input
     * @return index - 10
     */
    public int getNorthEast(int index) {
        return index - 10;
        
    }
    
    /** Gets the index northwest to the given index.
     * @param index the index as an input
     * @return index - 11
     */
    public int getNorthWest(int index) {
        return index - 11;
        
    }
    

    /** Gets the index southeast to the given index.
     * @param index the index as an input
     * @return index + 11
     */
    public int getSouthEast(int index) {
        return index + 11;
        
    }
    

    /** Gets the index southwest to the given index.
     * @param index the index as an input
     * @return index + 10
     */
    public int getSouthWest(int index) {
        return index + 10;
        
    }
    

    /** checks if the given index is occupied by an opponent of the current player.
     * @param i1 the index as an input
     * @return false if marble is not an opponent
     */
    public boolean isOpponent(int i1) {
        Marble ownMarble = color;
        Marble checkMarble = board.getMarble(i1);
        if (board.getPlayerCount() == 2 || board.getPlayerCount() == 3) {
            // if playercount == 2 || playercount == 3, return false if marble is current players' marble
            // or death or empty
            if (ownMarble == checkMarble || checkMarble == Marble.Death || checkMarble == Marble.Empty) {
                return false;
                
            }
            
        } else {
            // if playercount == 4, return true if the marble is of the opposite team of the current player
            if (ownMarble == Marble.White && (checkMarble == Marble.Red || checkMarble == Marble.Green)) {
                return true;
                
            } else if (ownMarble == Marble.Black && (checkMarble == Marble.Red || checkMarble == Marble.Green)) {
                return true;
                
            } else if (ownMarble == Marble.Green && (checkMarble == Marble.Black || checkMarble == Marble.White)) {
                return true;
                
            } else if (ownMarble == Marble.Red && (checkMarble == Marble.Black || checkMarble == Marble.White)) {
                return true;
                
            } else {
                return false;
                
            }
            
        }
        return true;
        
    }
    
    /** Checks if the given index is occupied by marble of the current players' team.
     * @param i1 the index as input
     * @return true if given index has the same team as the current players' marble
     */
    public boolean isOwnTeam(int i1) {
        Marble ownMarble = color;
        if (board.getPlayerCount() == 2 || board.getPlayerCount() == 3) {
            // if playercount == 2 or 3, return true if marble is own marble
            if (board.getMarble(i1) == ownMarble) {
                return true;
                
            }
            
        } else {
            // if playercount == 4, return true if the marble is in the same team as the current player
            if (ownMarble == Marble.White
                    && (board.getMarble(i1) == Marble.Black || board.getMarble(i1) == ownMarble)) {
                return true;
                
            } else if ((ownMarble == Marble.Black)
                    && (board.getMarble(i1) == Marble.White || board.getMarble(i1) == ownMarble)) {
                return true;
                
            } else if ((ownMarble == Marble.Green
                    && (board.getMarble(i1) == Marble.Red || board.getMarble(i1) == ownMarble))) {
                return true;
                
            } else if ((ownMarble == Marble.Red
                    && (board.getMarble(i1) == Marble.Green || board.getMarble(i1) == ownMarble))) {
                return true;
                
            }
            
        }
        return false;
        
    }
    
    /** checks if the given index is the same as the current players' marble.
     * @param i1 the index as an input
     * @return true if the marble on the given index == color, else false
     */
    public boolean isOwnMarble(int i1) {
        if (board.getMarble(i1) == color) {
            return true;
            
        }
        return false;
        
    }
    
    /** Fills the given list with a possibly hidden summito.
     * @param index the indexes as an input
     * @param direction the direction as an input
     * @return a complete list with hidden summito
     */
    public ArrayList<Integer> completeList(ArrayList<Integer> index, String direction) {
        ArrayList<Integer> newIndex = new ArrayList<Integer>();
        newIndex.addAll(index);
        Collections.sort(newIndex);
        // If between 1 and 4, this will check if there is a hidden summito and add the
        // indexes
        newIndex = flipList(index, direction);
        if (index.size() == 1 || (isStraightMove(index, direction) && isOwnMarble(index.get(0)))) {
            if (isInLine(index)) {
                ArrayList<Integer> summitoList = getHiddenSummito(newIndex, direction);
                // If it is a straight move, this will add the index after the marbles to the
                // list
                int last = summitoList.get(summitoList.size() - 1);
                summitoList.add(getNeighbourIndex(last, direction));
                return summitoList;
            }
        }
        return newIndex;
    }
    
    
    /** Adds indexes to the list that qualify for a hidden summito.
     * @param index the given index as input
     * @param direction the direction as input
     * @return the list with indexes that qualify as a hidden summito
     */
    public ArrayList<Integer> getHiddenSummito(ArrayList<Integer> index, String direction) {
        int ownTeam = 0;
        int opponent = 0;
        for (int i = 0; i < index.size(); i++) {
            if (isOwnTeam(index.get(i))) {
                ownTeam++;
            } else if (isOpponent(index.get(i))) {
                opponent++;
            } else {
                break;
            }
        }

        while (index.size() < 3 && ownTeam < 3) {
            int n = getNeighbourIndex(index.get(index.size() - 1), direction);
            if (isOwnTeam(n)) {
                ownTeam++;
                index.add(n);
            } else {
                break;
            }
        }
        int n;
        while (index.size() < 5 && (ownTeam - opponent > 1)) {
            n = getNeighbourIndex(index.get(index.size() - 1), direction);
            if (isOpponent(n)) {
                opponent++;
                index.add(n);
            } else {
                break;
            }
        }
        return index;
        
    }
    
}
