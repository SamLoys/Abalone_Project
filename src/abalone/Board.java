package abalone;

import abalone.Marble;
import abalone.exceptions.BoardException;
import java.util.ArrayList;
/**
 * the board class of the Abalone project. Contains the board and can set moves. 
 * Created on 17-01-2019. 
 * @author Sam Freriks and Ayla van der Wal.
 * @version 1.0
 */

public class Board {
    //Initializes all the different marbles
    Marble death = Marble.Death;
    Marble white = Marble.White;
    Marble empty = Marble.Empty;
    Marble black = Marble.Black; 
    Marble green = Marble.Green;
    Marble red = Marble.Red;  
    //integers to keep the scores
    private int scoreBlack;
    private int scoreWhite;
    private int scoreRed;
    private int scoreGreen;
    //amount of moves
    private int moves = 0;
    private static final int MaxMoves = 96;
    int playerCount = 0;
    // field keeps track of the state of the field.
    private Marble[][] fields;

    /**
     * The constructor of the board.
     * Given the number of players the board will be constructed in the proper way. 
     * @param players the amount of players
     * @requires the number of players to be 2, 3 or 4
     */
    public Board(int players) {
        if (players == 2) {
            //A 2 player board will be created 
            initBoard2();
            this.playerCount = 2;
            this.scoreBlack = 0;
            this.scoreWhite = 0;
        } else if (players == 3) {
            // a three player board will be created. 
            initBoard3();
            this.playerCount = 3;
            this.scoreBlack = 0;
            this.scoreWhite = 0;
            this.scoreRed = 0;
        } else if (players == 4) {
            // a four player board will be created. 
            initBoard4();
            this.playerCount = 4;
            this.scoreBlack = 0;
            this.scoreWhite = 0;
            this.scoreRed = 0;
            this.scoreGreen = 0;
        } else {
            //there is no board available for this amount of players
            System.out.println("NotValid the board cannot be initialized");
        }

    }

    /**
     * returns the amount of moves made.
     * @return the moves as in Integer. 
     * @ensures moves>= 0 
     */
    public int getTurns() {
        return moves;
    }

    /**
     * returns the amount of maximum moves possible.
     * @return the predefined maximum moves
     */
    public int getMaxTurns() {
        return MaxMoves;
    }

    /**
     * get the number of players that are assigned to the board. 
     * @return number of players
     * @ensures  2 <= playerCount <= 4 
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /** Initializes the board for a two player game, black on top and white on bottom.
     * @ensures to set the fields in a proper way. 
     */
    private void initBoard2() {
        fields = new Marble[][] { { death, death, death, death, death, death, death, death, death, death, death }, // 0
            { death, death, death, death, death, black, black, black, black, black, death }, // 1
            { death, death, death, death, black, black, black, black, black, black, death }, // 2
            { death, death, death, empty, empty, black, black, black, empty, empty, death }, // 3
            { death, death, empty, empty, empty, empty, empty, empty, empty, empty, death }, // 4
            { death, empty, empty, empty, empty, empty, empty, empty, empty, empty, death }, // 5
            { death, empty, empty, empty, empty, empty, empty, empty, empty, death, death }, // 6
            { death, empty, empty, white, white, white, empty, empty, death, death, death }, // 7
            { death, white, white, white, white, white, white, death, death, death, death }, // 8
            { death, white, white, white, white, white, death, death, death, death, death }, // 9
            { death, death, death, death, death, death, death, death, death, death, death } };// 10
    }

    /** Initializes the board for a 3 player game, White on the left, black on the
     * right, and green at the bottom.
     * @ensures to set the fields in a proper way. 
     */

    private void initBoard3() {
        fields = new Marble[][] { { death, death, death, death, death, death, death, death, death, death, death }, // 0
            { death, death, death, death, death, white, white, empty, black, black, death }, // 1
            { death, death, death, death, white, white, empty, empty, black, black, death }, // 2
            { death, death, death, white, white, empty, empty, empty, black, black, death }, // 3
            { death, death, white, white, empty, empty, empty, empty, black, black, death }, // 4
            { death, white, white, empty, empty, empty, empty, empty, black, black, death }, // 5
            { death, white, empty, empty, empty, empty, empty, empty, black, death, death }, // 6
            { death, empty, empty, empty, empty, empty, empty, empty, death, death, death }, // 7
            { death, green, green, green, green, green, green, death, death, death, death }, // 8
            { death, green, green, green, green, green, death, death, death, death, death }, // 9
            { death, death, death, death, death, death, death, death, death, death, death } };// 10

    }

    /**Initializes the board for a 4 player game, red top left, black top right,
     * white left bottom, green right bottom.
     * @ensures to set the field in a proper way. 
     */
    private void initBoard4() {
        fields = new Marble[][] { { death, death, death, death, death, death, death, death, death, death, death }, // 0
            { death, death, death, death, death, red, red, red, red, empty, death }, // 1
            { death, death, death, death, empty, red, red, red, empty, black, death }, // 2
            { death, death, death, empty, empty, red, red, empty, black, black, death }, // 3
            { death, death, empty, empty, empty, empty, empty, black, black, black, death }, // 4
            { death, white, white, white, empty, empty, empty, black, black, black, death }, // 5
            { death, white, white, white, empty, empty, empty, empty, empty, death, death }, // 6
            { death, white, white, empty, green, green, empty, empty, death, death, death }, // 7
            { death, white, empty, green, green, green, empty, death, death, death, death }, // 8
            { death, empty, green, green, green, green, death, death, death, death, death }, // 9
            { death, death, death, death, death, death, death, death, death, death, death } };// 10

    }

    /** 
     * copies the board. 
     * @returns a new board with the same fields as this board. 
     */
    public Board deepCopy() {
        Board copy = new Board(playerCount);
        for (int i = 0; i < 120; i++) {
            copy.setMarble(i, this.getMarble(i));
        }

        return copy;
    }

    /** Given the row and the column, the board model index of the marble is returned.
     * @param row of the board
     * @param col of the board
     * @return the index according to the board model. 
     */
    public int getindex(int row, int col) {
        int place = 0;
        place = 11 * row;
        place = place + col;
        return place;
    }

    /**
     * given the index of the marble according to the board model. the column is returned.
     * @param index index of the board according to the board model 
     * @return the number of the column. 
     */
    public int getCol(int index) {
        while (index >= 11) {
            index = index - 11;
        }
        return index;
    }

    /** given the index of the marble according to the board model, the row is returned.
     * @param index of the board
     * @return the number of the row
     */
    public int getRow(int index) {
        int row = 0;
        while (index >= 11) {
            index = index - 11;
            row++;
        }
        return row;
    }

    /** Places the given marble on the given index.
     * @requires index > 15 && index < 105
     * @param index the index according the board model 
     * @param marble the color you want to set the index
     */
    public void setMarble(int index, Marble marble) {
        int row = getRow(index);
        int col = getCol(index);
        setMarble(row, col, marble);
    }

    /**
     * sets the marble according the row and column and the marble. 
     * @param row the wanted row
     * @param col the wanted column
     * @param marble the wanted marble. 
     * @requires the row and column to be valid according to the board model. 
     */
    public void setMarble(int row, int col, Marble marble) {
        fields[row][col] = marble;
    }

    /** Converts the protocol index to our own index board model index.
     * @param index of the protocol index
     * @requires to give a value from 0 up and including 60
     * @return returns the converted index 
     * @throws BoardException if index < 0 || index > 60
     */
    public int protocolToIndex(int index) throws BoardException {
        if (index < 61 && index >= 0) {
            int[] indexConverter = new int[] { 16, 17, 18, 19, 20, 26, 27, 28, 29, 30, 31, 36, 37, 38, 39, 40, 41, 42,
                46, 47, 48, 49, 50, 51, 52, 53, 56, 57, 58, 59, 60, 61, 62, 63, 64, 67, 68, 69, 70, 71, 72, 73, 74,
                78, 79, 80, 81, 82, 83, 84, 89, 90, 91, 92, 93, 94, 100, 101, 102, 103, 104 };

            return indexConverter[index];
        }
        throw new BoardException("The index is out of range");

    }

    /** Returns an ArrayList with the numbers from protocol indexes converted to the board model indexes.
     * @param indexes according to the protocol. 
     * @return the ArrayList of indexes of the board model
     * @throws BoardException when index is out of range
     */
    public ArrayList<Integer> protocolToIndex(ArrayList<Integer> indexes) throws BoardException {
        ArrayList<Integer> toIndex = new ArrayList<Integer>();
        for (int index : indexes) {
            toIndex.add(protocolToIndex(index));
        }
        return toIndex; 
    }

    /** given board model index, returns the index according the protocol.
     * @param index of the board model index
     * @throws BoardException if index is not a valid board model index
     * @requires the index to be a valid board model index.
     */
    public int indexToProtocol(int index) throws BoardException {
        int[] indexConverter = new int[] { 16, 17, 18, 19, 20, 26, 27, 28, 29, 30, 31, 36, 37, 38, 39, 40, 41, 42, 46,
            47, 48, 49, 50, 51, 52, 53, 56, 57, 58, 59, 60, 61, 62, 63, 64, 67, 68, 69, 70, 71, 72, 73, 74, 78, 79,
            80, 81, 82, 83, 84, 89, 90, 91, 92, 93, 94, 100, 101, 102, 103, 104 };

        int looping = 0;
        for (int i = 0; i < indexConverter.length; i++) {
            if (indexConverter[i] == index) {
                return looping;
            }
            looping++;
        }
        throw new BoardException("The index is out of range");
    }

    /** given board model index, returns the index given in the protocol.
     * @param indexes of the protocol
     * @return the indexes in the protocol that correspond with the indexes of the game
     * @throws BoardException when index is out of range
     * @requires the indexes to be a valid board model index
     */
    public ArrayList<Integer> indexToProtocol(ArrayList<Integer> indexes) throws BoardException {
        ArrayList<Integer> toProtocol = new ArrayList<Integer>();
        for (int index : indexes) {
            toProtocol.add(indexToProtocol(index));
        }
        return toProtocol;
    }

    /** 
     * given the index of the marble, the state of the marble is returned.
     * The index needed is the board model index system
     * @param index of the wanted marble
     * @return the marble currently on this spot
     * @requires the index to be a valid board model index
     */
    public Marble getMarble(int index) {
        int row = getRow(index);
        int col = getCol(index);
        if (isValidField(row, col)) {
            return fields[row][col];
        }
        return null;
    }

    /** 
     * given the row and the column, the state of the marble is returned.
     * @param row on the board
     * @param col on the board
     * @return the marble currently on this spot if row or column not on the board, the 
     * @requires the column and row to be on the board
     */
    public Marble getMarble(int row, int col) {
        if (isValidField(row, col)) {
            return fields[row][col];
        }
        return null;
    }

    /** 
     * given two indexes, it checks whether the second index is the neighbor of the first index.
     * @param index1 the first index
     * @param index2 the index to be checked
     * @return true is the indexes are neighbors, else false 
     */
    public boolean isNeighbour(int index1, int index2) {
        int[] neighbours = getNeighbours(index1);
        for (int i = 0; i < neighbours.length; i++) {
            if (index2 == neighbours[i]) {
                return true;
            }
        }
        return false;
    }

    /** 
     * Given the index from our own index table, it will return an array with all the neighboring indexes.
     * @requires that the index given is a valid board model index
     * @param index of the game
     * @return an array with all the indexes of the neighbors of the parameter. 
     */
    public int[] getNeighbours(int index) {
        int[] neighbours = new int[6];
        neighbours[0] = index - 1;
        neighbours[1] = index + 1;
        neighbours[2] = index - 11;
        neighbours[3] = index - 10;
        neighbours[4] = index + 10;
        neighbours[5] = index + 11;
        return neighbours;
    }

    /**
     * Given the index, returns the index of the marble in direction east.
     * @param index of the marble according to the board model. 
     * @return the index of the neighbor.
     */
    public int getEast(int index) {
        return index + 1;
    }

    /**
     * Given the index, returns the index of the marble in direction west.
     * @param index of the marble according to the board model. 
     * @return the index of the neighbor.
     */
    public int getWest(int index) {
        index = index - 1;
        return index;
    }

    /**
     * Given the index, returns the index of the marble in direction NorthEast.
     * @param index of the marble according to the board model. 
     * @return the index of the neighbor.
     */
    public int getNorthEast(int index) {
        return index - 10;
    }

    /**
     * Given the index, returns the index of the marble in direction NorthWest.
     * @param index of the marble according to the board model. 
     * @return the index of the neighbor.
     */
    public int getNorthWest(int index) {
        return index - 11;
    }

    /**
     * Given the index, returns the index of the marble in direction SouthEast.
     * @param index of the marble according to the board model. 
     * @return the index of the neighbor.
     */
    public int getSouthEast(int index) {
        return index + 11;
    }

    /**
     * Given the index, returns the index of the marble in direction SouthWest.
     * @param index of the marble according to the board model. 
     * @return the index of the neighbor.
     */
    public int getSouthWest(int index) {
        return index + 10;
    }

    /** Given the index and direction, returns the neighbor index in that direction.
     * @param index of the board model index
     * @param direction the direction you want
     * @throws BoardException when the direction does not exist
     * @requires the direction to be a valid direction and the index to be a valid board model index
     */
    public int getNeighbour(int index, String direction) throws BoardException {
        switch (direction) {
            case Directions.east:
                return getEast(index);
            case Directions.west:
                return getWest(index);
            case Directions.northEast:
                return getNorthEast(index);
            case Directions.northWest:
                return getNorthWest(index);
            case Directions.southEast:
                return getSouthEast(index);
            case Directions.southWest:
                return getSouthWest(index);
            default:
                throw new BoardException("The given direction does not excist");

        }

    }

    /** 
     * Checks if the field exists given the row and the column.
     * @param row on the board
     * @param col on the board
     * @return true if 0 >=row < 11 && 0>=col < 11
     */
    public boolean isValidField(int row, int col) {
        if ((row >= 0 && row < 11) && (col >= 0 && col < 11)) {
            return true;   
        }
        return false;
    }

    /** 
     * checks if the given index has a valid field a field is valid if it exists on
     * the board, this includes death states. the index used is our index index, not
     * the protocol index.
     * @param index on the board according to the board model index
     */
    public boolean isValidField(int index) { 
        int col = getCol(index);
        int row = getRow(index);
        return isValidField(row, col); 
    }

    /** 
     * moves all indexes in the list to the given direction. 
     * @param indexes of the game
     * @param direction the wanted direction
     * @throws BoardException when it is off the board 
     * @returns true if a marble is pushed of the board, false if not
     * @requires the index to be between 1 and 6 length.
     */
    public boolean move(ArrayList<Integer> indexes, String direction) throws BoardException {
        boolean scored = false;
        ArrayList<Marble> placeholders = new ArrayList<Marble>();
        for (int index : indexes) {
            placeholders.add(getMarble(index));
            setMarble(index, Marble.Empty);
        }
        for (int i = 0; i < indexes.size(); i++) {
            if (getMarble(getNeighbour(indexes.get(i), direction)) == Marble.Death) {
                scored = true;
            } else {
                setMarble(getNeighbour(indexes.get(i), direction), placeholders.get(i));
            }
        }
        moves++;
        return scored;
    }

    /** 
     * adds a +1 to the score of the given marble.
     * @param marble that gets a score
     * @requires a valid marble color
     */
    public void addScore(Marble marble) {
        if (marble == Marble.Black) {
            scoreBlack++;
        }
        if (marble == Marble.White) {
            scoreWhite++;
        }
        if (marble == Marble.Red) {
            scoreRed++;
        }
        if (marble == Marble.Green) {
            scoreGreen++;
        }
    } 

    /** 
     * Get the score of the given marble.
     * @param marble of which the score is needed
     * @return the score of the given marble
     * @throws BoardException if the marble doesn't exist
     * @requires the Marble to be a valid color
     */
    public int getScore(Marble marble) throws BoardException {
        if (marble == Marble.Black) {
            return scoreBlack;
        }
        if (marble == Marble.White) {
            return scoreWhite;
        }
        if (marble == Marble.Red) {
            return scoreRed;
        }
        if (marble == Marble.Green) {
            return scoreGreen;
        }

        // should actually never happen
        throw new BoardException("Marble doesnt exist");

    }

    /** Calculates the direction of the given marble to the center.
     * @param index on the board according to the board model index
     * @return the direction towards the center
     * @requires the index to be a valid board model index
     * @ensures to return the direction to the center
     */
    public String getDirectionToCenter(int index) {
        int row = getRow(index);
        int col = getCol(index);

        if (row == 5) {
            if (col < 5) {
                return Directions.east;
            } else {
                return Directions.west;
            }
        }
        if (row < 5) {
            if (col <= 5) {
                return Directions.southEast;
            } else {
                return Directions.southWest;
            }

        } else {
            if (col < 5) {
                return Directions.northEast;
            } else {
                return Directions.northWest;
            }
        }
    }

    /**
     *  resets the board to the default state according to the amount of players.
     *  @ensrues to reset the board to the default setting
     */
    public void reset() {
        switch (playerCount) {
            case 2:
                initBoard2();
                break;
            case 3:
                initBoard3();
                break;
            case 4:
                initBoard4();
                break;
            default: 
                break;
        }
    }

    /** 
     * returns the representation of the board as a string.
     * @return A string with the board representation of the up to date board
     */
    public String toString() {
        String s = "";
        for (int row = 0; row < 11; row++) {
            if (row == 1) {
                s = s + "                        ";
            }
            if (row == 2) {
                s = s + "                 ";
            }
            if (row == 3) {
                s = s + "           ";
            }
            if (row == 4) {
                s = s + "     ";
            }
            if (row == 5) {
                s = s + "";
            }
            if (row == 6) {
                s = s + "     ";
            }
            if (row == 7) {
                s = s + "             ";
            }
            if (row == 8) {
                s = s + "                   ";
            }
            if (row == 9) {
                s = s + "                         ";
            }
            for (int col = 0; col < 11; col++) {

                int number = 0;
                try {
                    number = indexToProtocol(getindex(row, col));
                } catch (BoardException e) {
                    // no need to print anything, this will happen if the number is a death state
                    // and when thats happens you dont need the number
                }

                if (getMarble(row, col).toString().equals("Death")) {
                    s = s + "";
                } else if (getMarble(row, col).toString().equals("Empty")) {
                    s = s + "   -" + "(" + number + ")" + "-   ";

                } else if (getMarble(row, col).toString().equals("Red")) {
                    s = s + "  " + getMarble(row, col).toString() + "(" + number + ")" + "  ";
                } else {
                    s = s + " " + getMarble(row, col).toString() + "(" + number + ")" + " ";
                }
            }
            s = s + "\n\n" + "\n\n";
        }

        return s;
    }

}
