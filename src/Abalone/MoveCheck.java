package Abalone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Abalone.Exceptions.IllegalMoveException;

public class MoveCheck {
    static Board board;
    ArrayList<Integer> index;
    Marble color;

    /** Javadoc.
     * @param color Javadoc.
     * @param board Javadoc.
     */
    public MoveCheck(Marble color, Board board) {
        this.board = board;
        this.color = color;
        
    }

    /** Javadoc.
     * @return Javadoc.
     */
    public Marble getColor() {
        return color;
        
    }
    
    /** Javadoc.
     * @param i1 Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
     * @throws IllegalMoveException Javadoc.
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
    
    /** Javadoc.
     * @param i1 Javadoc.
     * @param i2 Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
     * @throws IllegalMoveException Javadoc.
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
    
    /** Javadoc.
     * @param i1 Javadoc.
     * @param i2 Javadoc.
     * @param i3 Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
     * @throws IllegalMoveException Javadoc.
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
    
    /** Javadoc.
     * @param i1 Javadoc.
     * @param i2 Javadoc.
     * @param i3 Javadoc.
     * @param i4 Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
     * @throws IllegalMoveException Javadoc.
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
    
    /** Javadoc.
     * @param i1 Javadoc.
     * @param i2 Javadoc.
     * @param i3 Javadoc.
     * @param i4 Javadoc.
     * @param i5 Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
     * @throws IllegalMoveException Javadoc.
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
    
    /** Javadoc.
     * @param index Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
     * @throws IllegalMoveException Javadoc.
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
    
    /** Javadoc.
     * @param index Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
     * @throws IllegalMoveException Javadoc.
     */
    public ArrayList<Integer> returnMoves(ArrayList<Integer> index, String direction) throws IllegalMoveException {
        ArrayList<Integer> all = new ArrayList<Integer>();
        // Checks if a summito
        if (isInLine(index)) {
            if (isStraightMove(index, direction)) {
                if (isOwnMarble(index.get(0))) {
                    switch ((index.size() - 1)) {
                        case 1:
                            if (board.getMarble(index.get(1)) == Marble.Empty) {
                                all.add(index.get(0));
                                
                            } else {
                                throw new IllegalMoveException("is not valid single move");
                                
                            }
                            break;
                        case 2:
                            if (isOwnTeam(index.get(1))) {
                                if (board.getMarble(index.get(2)) == Marble.Empty) {
                                    all.add(index.get(0));
                                    all.add(index.get(1));
                                    
                                } else {
                                    throw new IllegalMoveException("direction is not empty");
                                    
                                }
                                
                            } else {
                                throw new IllegalMoveException("for two input, second marble is not own team");
                                
                            }
                            break;
                        case 3:
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
                                    throw new IllegalMoveException("input is death or empty");
                                    
                                }
                                
                            } else {
                                throw new IllegalMoveException("for three input, second marble is not own team");
                                
                            }
                            break;
                        case 4:
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
                                                throw new IllegalMoveException("for 5 input, the push is blocked");
                                                
                                            }
                                            
                                        } else {
                                            throw new IllegalMoveException("for 5 input, fifth marble is not opponent");
                                            
                                        }
                                        
                                    } else {
                                        throw new IllegalMoveException("for 5 input, fourth marble is not opponent");
                                        
                                    }
                                    
                                } else {
                                    throw new IllegalMoveException("for 5 input, third marble is not own team");
                                    
                                }
                                
                            } else {
                                throw new IllegalMoveException("for 5 input, second marble is not own team");
                                
                            }
                            break;
                        default: 
                            break;
                    }
                    
                } else {
                    throw new IllegalMoveException("is not a valid straight move, the first marble is not your own");
                    
                }
                
            } else {
                if ((hasOwnMarble(index)) && (index.size() < 4) && (isValidSideStep(index, direction))) {
                    int marbles = 0;
                    for (int i = 0; i < index.size(); i++) {
                        if (!(isOwnTeam(index.get(i)))) {
                            throw new IllegalMoveException("Not all marbles are in your team");
                            
                        } else {
                            marbles++;
                            
                        }
                        
                    }
                    if (index.size() == marbles) {
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


    /** Javadoc.
     * @param index Javadoc.
     * @return Javadoc.
     */
    public boolean isOnBoard(ArrayList<Integer> index) {
        for (int i : index) {
            if (!(15 < i && i < 105)) {
                return false;
                
            }
            
        }
        return true;
        
    }

    /** Checks if the given 2 or 3 indexes make a straight move. If not, it is a side
     * move.
     * @param index Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
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
     * @param index Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
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
     * @param direction Javadoc.
     * @return Javadoc.
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
    
    /** Javadoc.
     * @param index Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
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
    
    /** Javadoc.
     * @param i1 Javadoc.
     * @param i2 Javadoc.
     * @return Javadoc.
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
    
    
    /** Javadoc.
     * @param i Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
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
    
    
    /** Javadoc.
     * @param index Javadoc.
     * @return Javadoc.
     */
    public boolean isInLine(ArrayList<Integer> index) {
        boolean valid = false;
        if (index.size() == 1) {
            return true;
            
        } else if (index.size() == 2) {
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
                    
                } else {
                    return true;
                    
                }
                
            }
            
        }
        return valid;
        
    }
    
    /** Javadoc. 
     * @param index Javadoc.
     * @return Javadoc.
     */
    public boolean hasOwnMarble(ArrayList<Integer> index) {
        for (int i : index) {
            if (board.getMarble(i) == color) {
                return true;
                
            }
            
        }
        return false;
        
    }
    
    /** Javadoc. 
     * @param index Javadoc.
     * @return Javadoc.
     */
    public int getEast(int index) {
        return index + 1;
        
    }
    
    /** Javadoc. 
     * @param index Javadoc.
     * @return Javadoc.
     */
    public int getWest(int index) {
        index = index - 1;
        return index;
        
    }
    
    /** Javadoc. 
     * @param index Javadoc.
     * @return Javadoc.
     */
    public int getNorthEast(int index) {
        return index - 10;
        
    }
    
    /** Javadoc. 
     * @param index Javadoc.
     * @return Javadoc.
     */
    public int getNorthWest(int index) {
        return index - 11;
        
    }
    
    /** Javadoc. 
     * @param index Javadoc.
     * @return Javadoc.
     */
    public int getSouthEast(int index) {
        return index + 11;
        
    }
    
    /** Javadoc. 
     * @param index Javadoc.
     * @return Javadoc.
     */
    public int getSouthWest(int index) {
        return index + 10;
        
    }
    
    /** Javadoc. 
     * @return Javadoc.
     */
    public boolean isOpponent(int i1) {
        Marble ownMarble = color;
        Marble checkMarble = board.getMarble(i1);
        if (board.getPlayerCount() == 2 || board.getPlayerCount() == 3) {
            if (ownMarble == checkMarble || checkMarble == Marble.Death || checkMarble == Marble.Empty) {
                return false;
                
            }
            
        } else {
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
    
    /** Javadoc. 
     * @param i1 Javadoc.
     * @return Javadoc.
     */
    public boolean isOwnTeam(int i1) {
        Marble ownMarble = color;
        if (board.getPlayerCount() == 2 || board.getPlayerCount() == 3) {
            if (board.getMarble(i1) == ownMarble) {
                return true;
                
            }
            
        } else {
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
    
    /** Javadoc. 
     * @param i1 Javadoc.
     * @return Javadoc.
     */
    public boolean isOwnMarble(int i1) {
        if (board.getMarble(i1) == color) {
            return true;
            
        }
        return false;
        
    }
    
    /** Javadoc. 
     * @param index Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
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
    
    
    /** Javadoc.
     * @param index Javadoc.
     * @param direction Javadoc.
     * @return Javadoc.
     */
    public ArrayList<Integer> getHiddenSummito(ArrayList<Integer> index, String direction) {
        int ownTeam = 0;
        int opponent = 0;
        for (int i = 0; i < index.size(); i++) {
            if (isOwnTeam(index.get(i))) {
                ownTeam++;
            }
            if (isOpponent(i)) {
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
