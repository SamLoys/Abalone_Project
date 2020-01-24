package Abalone;

import Abalone.Marble;
import Abalone.Exceptions.BoardException;
import Abalone.Exceptions.IllegalMoveException;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
 
	Marble d = Marble.Death;
	Marble w = Marble.White;
	Marble e = Marble.Empty;
	Marble b = Marble.Black;
	Marble g = Marble.Green;
	Marble r = Marble.Red;

	private int scoreBlack;
	private int scoreWhite;
	private int scoreRed;
	private int scoreGreen;

	private int moves = 0;
	private final static int MaxMoves = 96;

	int playerCount = 0;
	// field keeps track of the state of the field.
	private Marble[][] fields;

	public Board(int players) {
		if (players == 2) {
			initBoard2();
			this.playerCount = 2;
			this.scoreBlack = 0;
			this.scoreWhite = 0;

		}

		else if (players == 3) {
			initBoard3();
			this.playerCount = 3;
			this.scoreBlack = 0;
			this.scoreWhite = 0;
			this.scoreRed = 0;

		}

		else if (players == 4) {
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

	/**
	 * Initializes the board for a two player game, white on top and black on bottom
	 */
	private void initBoard2() {
		fields = new Marble[][] { { d, d, d, d, d, d, d, d, d, d, d }, // 0
				{ d, d, d, d, d, b, b, b, b, b, d }, // 1
				{ d, d, d, d, b, b, b, b, b, b, d }, // 2
				{ d, d, d, e, e, b, b, b, e, e, d }, // 3
				{ d, d, e, e, e, e, e, e, e, e, d }, // 4
				{ d, e, e, e, e, e, e, e, e, e, d }, // 5
				{ d, e, e, e, e, e, e, e, e, d, d }, // 6
				{ d, e, e, w, w, w, e, e, d, d, d }, // 7
				{ d, w, w, w, w, w, w, d, d, d, d }, // 8
				{ d, w, w, w, w, w, d, d, d, d, d }, // 9
				{ d, d, d, d, d, d, d, d, d, d, d } };// 10
	}

	/**
	 * Initializes the board for a 3 player game, White on the left, black on the
	 * right, and green at the bottom
	 */

	private void initBoard3() {
		fields = new Marble[][] { { d, d, d, d, d, d, d, d, d, d, d }, // 0
				{ d, d, d, d, d, w, w, e, b, b, d }, // 1
				{ d, d, d, d, w, w, e, e, b, b, d }, // 2
				{ d, d, d, w, w, e, e, e, b, b, d }, // 3
				{ d, d, w, w, e, e, e, e, b, b, d }, // 4
				{ d, w, w, e, e, e, e, e, b, b, d }, // 5
				{ d, w, e, e, e, e, e, e, b, d, d }, // 6
				{ d, e, e, e, e, e, e, e, d, d, d }, // 7
				{ d, g, g, g, g, g, g, d, d, d, d }, // 8
				{ d, g, g, g, g, g, d, d, d, d, d }, // 9
				{ d, d, d, d, d, d, d, d, d, d, d } };// 10

	}

	/**
	 * Initializes the board for a 4 player game, red top left, black top right,
	 * white left bottom, green right bottom
	 */
	private void initBoard4() {
		fields = new Marble[][] { { d, d, d, d, d, d, d, d, d, d, d }, // 0
				{ d, d, d, d, d, r, r, r, r, e, d }, // 1
				{ d, d, d, d, e, r, r, r, e, b, d }, // 2
				{ d, d, d, e, e, r, r, e, b, b, d }, // 3
				{ d, d, e, e, e, e, e, b, b, b, d }, // 4
				{ d, w, w, w, e, e, e, b, b, b, d }, // 5
				{ d, w, w, w, e, e, e, e, e, d, d }, // 6
				{ d, w, w, e, g, g, e, e, d, d, d }, // 7
				{ d, w, e, g, g, g, e, d, d, d, d }, // 8
				{ d, e, g, g, g, g, d, d, d, d, d }, // 9
				{ d, d, d, d, d, d, d, d, d, d, d } };// 10

	}

	/**
	 * Copies the board needs to be implemented
	 */
	public Board deepCopy() {
		Board copy = new Board(playerCount);
		for (int i = 0; i < 120; i++) {
			copy.setMarble(i, this.getMarble(i));
		}

		return copy;
	}

	/**
	 * Given the row and the col, the index of the marble is returned
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public int Getindex(int row, int col) {
		int place = 0;
		place = 11 * row;
		place = place + col;
		return place;
	}

	/**
	 * given the index of the marble. the col is returned
	 * 
	 * @param index
	 * @return
	 */
	public int getCol(int index) {
		while (index >= 11) {
			index = index - 11;
		}
		return index;
	}

	/**
	 * given the index of the marble, the row is returned
	 * 
	 * @param index
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

	public void setMarble(int index, Marble marble) {
		int row = getRow(index);
		int col = getCol(index);
		setMarble(row, col, marble);
	}

	public void setMarble(int row, int col, Marble marble) {
		fields[row][col] = marble;
	}

	/**
	 * Converts the index to our own index system.
	 * 
	 * @param index
	 * @requires to give a value from 0 up and uncluding 60
	 * @return returns the converted index
	 * @throws BoardException 
	 */
	public int protocolToIndex(int index) throws BoardException {
		if (index < 61 && index >= 0) {
			int indexConverter[] = new int[] { 16, 17, 18, 19, 20, 26, 27, 28, 29, 30, 31, 36, 37, 38, 39, 40, 41, 42,
					46, 47, 48, 49, 50, 51, 52, 53, 56, 57, 58, 59, 60, 61, 62, 63, 64, 67, 68, 69, 70, 71, 72, 73, 74,
					78, 79, 80, 81, 82, 83, 84, 89, 90, 91, 92, 93, 94, 100, 101, 102, 103, 104 };

			return indexConverter[index];
		} 
		throw new BoardException("The index is out of range");

	}

	public ArrayList<Integer> protocolToIndex(ArrayList<Integer> indexes) throws BoardException {
		ArrayList<Integer> toIndex = new ArrayList<Integer>();
		for (int index : indexes) {
			toIndex.add(protocolToIndex(index));
		}
		return toIndex;
	}

	/**
	 * given the own coordinate index, returns the index given in the protocol
	 * 
	 * @param index
	 * @return returns 9999 if the index is out of bounds
	 * @throws BoardException 
	 */
	public int indexToProtocol(int index) throws BoardException {
		int indexConverter[] = new int[] { 16, 17, 18, 19, 20, 26, 27, 28, 29, 30, 31, 36, 37, 38, 39, 40, 41, 42, 46,
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

	public ArrayList<Integer> indexToProtocol(ArrayList<Integer> indexes) throws BoardException {
		ArrayList<Integer> toProtocol = new ArrayList<Integer>();
		for (int index : indexes) {
			toProtocol.add(indexToProtocol(index));
		}
		return toProtocol;
	}

	/**
	 * given the index of the marble, the state of the marble is returned The index
	 * is our own index, not the protocol index
	 * 
	 * @param index
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

	/**
	 * given the row and the col, the state of the marble is returned.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Marble getMarble(int row, int col) {
		if (isValidField(row, col)) {
			return fields[row][col];
		}
		return null;
	}

	/**
	 * given two indexes, it checks wheter the second index is the neighbour of the
	 * first index
	 * 
	 * @param index1
	 * @param index2
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

	/**
	 * Given the index from our own index table, it will return an array with all
	 * the neighbouring indexes.
	 * 
	 * @requires that the index given is between 16 and 104
	 * @param index
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
	 * checks if the field exists given the row and the col
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isValidField(int row, int col) {
		if ((row < 11) && (col < 11)) {
			return true;
		}
		return false;
	}

	/*
	 * checks if the given index has a valid field a field is valid if it exists on
	 * the board, this includes death states. the index used is our index index, not
	 * the protocol index
	 */
	public boolean isValidField(int index) {
		int col = getCol(index);
		int row = getRow(index);
		return isValidField(row, col);
	}

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
		
		//should actually never happen
		throw new BoardException("Marble doesnt exist");
		
		
	}



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
	 * resets the board to the default state according to the amount of players
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
		}
	}

	
	/**
	 * returns the representation of the board as a string;
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
					number = indexToProtocol(Getindex(row, col));
				}
				catch(BoardException e) {
					//no need to print anything, this will happen if the number is a death state
					// and when thats happens you dont need the number
				}
				
				if (getMarble(row, col).toString().equals("Death")) {
					s = s + "";
				} else if (getMarble(row, col).toString().equals("Empty")) {
					s = s + "   -" + "(" + number + ")" + "-   ";

				} else if (getMarble(row, col).toString().equals("Red")) {
					s = s + "  " + getMarble(row, col).toString() + "(" + number + ")"
							+ "  ";
				} else {
					s = s + " " + getMarble(row, col).toString() + "(" + number + ")"
							+ " ";
				}
			}
			s = s + "\n\n" + "\n\n";
		}

		return s;
	}

}
