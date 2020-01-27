package Abalone;

import Abalone.Exceptions.BoardException;
import Abalone.Exceptions.IllegalMoveException;
import Abalone.Marble;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    Marble death = Marble.Death;
    Marble white = Marble.White;
    Marble empty = Marble.Empty;
    Marble black = Marble.Black;
    Marble green = Marble.Green;
    Marble red = Marble.Red; 

    private int scoreBlack;
    private int scoreWhite;
    private int scoreRed;
    private int scoreGreen;
    private int moves = 0;
    private static final int MaxMoves = 96;
    int playerCount = 0;
    // field keeps track of the state of the field.
    private Marble[][] fields;

    /**
     * Little summary.
     * 
     * @param players the amount of players
     */
    public Board(int players) {
        if (players == 2) {
            initBoard2();
            this.playerCount = 2;
            this.scoreBlack = 0;
            this.scoreWhite = 0;
        } else if (players == 3) {
            initBoard3();
            this.playerCount = 3;
            this.scoreBlack = 0;
            this.scoreWhite = 0;
            this.scoreRed = 0;
        } else if (players == 4) {
            initBoard4();
            this.playerCount = 4;
            this.scoreBlack = 0;
            this.scoreWhite = 0;
            this.scoreRed = 0;
            this.scoreGreen = 0;
        } else {
            System.out.println("NotValid the board cannot be initialized");
        }

    }

    public int getTurns() {
        return moves;
    }

    public int getMaxTurns() {
        return MaxMoves;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    /** Initializes the board for a two player game, white on top and black on bottom.
     * 
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
     * 
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
     * 
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

    /** Copies the board needs to be implemented.
     * 
     */
    public Board deepCopy() {
        Board copy = new Board(playerCount);
        for (int i = 0; i < 120; i++) {
            copy.setMarble(i, this.getMarble(i));
        }

        return copy;
    }

    /** Given the row and the col, the index of the marble is returned.
     * @param row of the board
     * @param col of the board
     * @return
     */
    public int getindex(int row, int col) {
        int place = 0;
        place = 11 * row;
        place = place + col;
        return place;
    }

    /**
     * given the index of the marble. the col is returned.
     * @param index index of the board
     * @return
     */
    public int getCol(int index) {
        while (index >= 11) {
            index = index - 11;
        }
        return index;
    }

    /** given the index of the marble, the row is returned.
     * @param index of the board
     * @return
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
     * @param index > 15 && index < 105
     * @param marble != null
     */
    public void setMarble(int index, Marble marble) {
        int row = getRow(index);
        int col = getCol(index);
        setMarble(row, col, marble);
    }

    public void setMarble(int row, int col, Marble marble) {
        fields[row][col] = marble;
    }

    /** Converts the index to our own index system.
     * @param index >= 0 && index < 61
     * @requires to give a value from 0 up and uncluding 60
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

    /** Returns an arraylist with the numbers from protocol to index.
     * @param indexes of the protocol
     * @return the arraylist of indexes of the game
     * @throws BoardException when index is out of range
     */
    public ArrayList<Integer> protocolToIndex(ArrayList<Integer> indexes) throws BoardException {
        ArrayList<Integer> toIndex = new ArrayList<Integer>();
        for (int index : indexes) {
            toIndex.add(protocolToIndex(index));
        }
        return toIndex;
    }

    /** given the own coordinate index, returns the index given in the protocol.
     * @param index > 15 && index < 105
     * @throws BoardException if index is out of ranges
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

    /** given the own coordinate index, returns the index given in the protocol.
     * @param indexes of the protocol
     * @return the indexes in the protocol that correspond with the indexes of the game
     * @throws BoardException when index is out of range
     */
    public ArrayList<Integer> indexToProtocol(ArrayList<Integer> indexes) throws BoardException {
        ArrayList<Integer> toProtocol = new ArrayList<Integer>();
        for (int index : indexes) {
            toProtocol.add(indexToProtocol(index));
        }
        return toProtocol;
    }

    /** given the index of the marble, the state of the marble is returned The index.
     * is our own index, not the protocol index
     * @param index > 15 && index < 105
     * @return
     */
    public Marble getMarble(int index) {
        int row = getRow(index);
        int col = getCol(index);
        if (isValidField(row, col)) {
            return fields[row][col];
        }
        return null;
    }

    /** given the row and the col, the state of the marble is returned.
     * @param row on the board
     * @param col on the board
     * @return
     */
    public Marble getMarble(int row, int col) {
        if (isValidField(row, col)) {
            return fields[row][col];
        }
        return null;
    }

    /** given two indexes, it checks whether the second index is the neighbour of the
     * first index.
     * @param index1 the first index
     * @param index2 the index to be checked
     * @return true is the indexes are neighbours, else false
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

    /** Given the index from our own index table, it will return an array with all
     * the neighbouring indexes.
     * @requires that the index given is between 16 and 104
     * @param index of the game
     * @return
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

    public int getEast(int index) {
        return index + 1;
    }

    public int getWest(int index) {
        index = index - 1;
        return index;
    }

    public int getNorthEast(int index) {
        return index - 10;
    }

    public int getNorthWest(int index) {
        return index - 11;
    }

    public int getSouthEast(int index) {
        return index + 11;
    }

    public int getSouthWest(int index) {
        return index + 10;
    }

    /** Given the index and direction, returns the neighbour index in that direction.
     * @param index on the board
     * @param direction != null
     * @throws BoardException when the direction does not exist
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

    /** Checks if the field exists given the row and the col.
     * @param row on the board
     * @param col on the board
     * @return true if row < 11 && col < 11
     */
    public boolean isValidField(int row, int col) {
        if ((row < 11) && (col < 11)) {
            return true;
        }
        return false;
    }

    /** checks if the given index has a valid field a field is valid if it exists on
     * the board, this includes death states. the index used is our index index, not
     * the protocol index.
     * @param index on the board
     */
    public boolean isValidField(int index) {
        int col = getCol(index);
        int row = getRow(index);
        return isValidField(row, col);
    }

    /** moves all indexes in the list to the given direction. true when the last neighbour == Death
     * @param indexes of the game
     * @param direction != null
     * @throws BoardException when it is off the board 
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

    /** adds a +1 to the score of the given marble.
     * @param marble that gets a score
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

    /** Get the score of the given marble.
     * @param marble of which the score is needed
     * @return the score of the given marble
     * @throws BoardException if the marble doesn't exist
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

    /** Calculates the direction of the given marble to the centre.
     * @param index on the board
     * @return the direction towards the centre
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

    /** resets the board to the default state according to the amount of players.
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

    /** returns the representation of the board as a string.
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
