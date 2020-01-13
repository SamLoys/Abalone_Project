package Abalone;

import Abalone.Marble;

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

	/**
	 * Initializes the board for a two player game, white on top and black on bottom
	 */
	private void initBoard2() {
		fields = new Marble[][] { { d, d, d, d, d, d, d, d, d, d, d }, // 0
				{ d, d, d, d, d, w, w, w, w, w, d }, // 1
				{ d, d, d, d, w, w, w, w, w, w, d }, // 2
				{ d, d, d, e, e, w, w, w, e, e, d }, // 3
				{ d, d, e, e, e, e, e, e, e, e, d }, // 4
				{ d, e, e, e, e, e, e, e, e, e, d }, // 5
				{ d, e, e, e, e, e, e, e, e, d, d }, // 6
				{ d, e, e, b, b, b, e, e, d, d, d }, // 7
				{ d, b, b, b, b, b, b, d, d, d, d }, // 8
				{ d, b, b, b, b, b, d, d, d, d, d }, // 9
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
	public void deepCopy() {

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
	 */
	public int protocolToIndex(int index) {
		if (index < 61 && index >= 0) {
			int indexConverter[] = new int[] { 16, 17, 18, 19, 20, 26, 27, 28, 29, 30, 31, 36, 37, 38, 39, 40, 41, 42,
					46, 47, 48, 49, 50, 51, 52, 53, 56, 57, 58, 59, 60, 61, 62, 63, 64, 67, 68, 69, 70, 71, 72, 73, 74,
					78, 79, 80, 81, 82, 83, 84, 89, 90, 91, 92, 93, 94, 100, 101, 102, 103, 104 };

			return indexConverter[index];
		} else
			return 9999;

	}

	/**
	 * given the own coordinate index, returns the index given in the protocol
	 * 
	 * @param index
	 * @return returns 9999 if the index is out of bounds
	 */
	public int indexToProtocol(int index) {
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
		return 9999;
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
		return index - 1;
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

	/**
	 * moving the marble in the given direction,(this is not an attack move)
	 * 
	 * @requires that the field is already checked and the move is valid
	 * @param index
	 * @param direction
	 */
	public void move(int index, String direction) {
		Marble placeholder = getMarble(index);
		switch (direction) {
		case Directions.east:
			setMarble(index, Marble.Empty);
			setMarble(getEast(index), placeholder);
			break;
		case Directions.northEast:
			setMarble(index, Marble.Empty);
			setMarble(getNorthEast(index), placeholder);
			break;

		case Directions.northWest:
			setMarble(index, Marble.Empty);
			setMarble(getNorthWest(index), placeholder);
			break;

		case Directions.southEast:
			setMarble(index, Marble.Empty);
			setMarble(getSouthEast(index), placeholder);
			break;

		case Directions.southWest:
			setMarble(index, Marble.Empty);
			setMarble(getSouthWest(index), placeholder);

			break;

		case Directions.west:
			setMarble(index, Marble.Empty);
			setMarble(getWest(index), placeholder);
			break;
		}

	}

	/**
	 * given the direction and the indexes moves the marble (this is not an attack
	 * move)
	 * 
	 * @requires that the field is already checked and the move is valid
	 * @param index
	 * @param index2
	 * @param direction
	 */

	public void move(int index, int index2, String direction) {
		Marble placeholder = getMarble(index);
		switch (direction) {
		case Directions.east:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(getEast(index), placeholder);
			setMarble(getEast(index2), placeholder);
			break;
		case Directions.west:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(getWest(index), placeholder);
			setMarble(getWest(index2), placeholder);
			break;
		case Directions.northEast:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(getNorthEast(index), placeholder);
			setMarble(getNorthEast(index2), placeholder);
			break;
		case Directions.northWest:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(getNorthWest(index), placeholder);
			setMarble(getNorthWest(index2), placeholder);
			break;
		case Directions.southEast:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(getSouthEast(index), placeholder);
			setMarble(getSouthEast(index2), placeholder);
			break;
		case Directions.southWest:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(getSouthWest(index), placeholder);
			setMarble(getSouthWest(index2), placeholder);
			break;

		}
	}

	/**
	 * given the direction and the indexes moves the marble (this is not an attack
	 * move)
	 * 
	 * @requires that the field is already checked and the move is valid
	 * @param index
	 * @param index2
	 * @param index3
	 * @param direction
	 */
	public void move(int index, int index2, int index3, String direction) {
		Marble placeholder = getMarble(index);
		switch (direction) {
		case Directions.east:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(index3, Marble.Empty);
			setMarble(getEast(index), placeholder);
			setMarble(getEast(index2), placeholder);
			setMarble(getEast(index3), placeholder);
			break;
		case Directions.west:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(index3, Marble.Empty);
			setMarble(getWest(index), placeholder);
			setMarble(getWest(index2), placeholder);
			setMarble(getWest(index3), placeholder);
			break;
		case Directions.northEast:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(index3, Marble.Empty);
			setMarble(getNorthEast(index), placeholder);
			setMarble(getNorthEast(index2), placeholder);
			setMarble(getNorthEast(index3), placeholder);
			break;
		case Directions.northWest:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(index3, Marble.Empty);
			setMarble(getNorthWest(index), placeholder);
			setMarble(getNorthWest(index2), placeholder);
			setMarble(getNorthWest(index3), placeholder);
			break;
		case Directions.southEast:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(index3, Marble.Empty);
			setMarble(getSouthEast(index), placeholder);
			setMarble(getSouthEast(index2), placeholder);
			setMarble(getSouthEast(index3), placeholder);
			break;
		case Directions.southWest:
			setMarble(index, Marble.Empty);
			setMarble(index2, Marble.Empty);
			setMarble(index3, Marble.Empty);
			setMarble(getSouthWest(index), placeholder);
			setMarble(getSouthWest(index2), placeholder);
			setMarble(getSouthWest(index3), placeholder);
			break;

		}
	}

	/**
	 * checks if the move is a summito
	 * 
	 * @param index
	 * @param index2
	 * @param direction
	 * @return true if the move if technically a summito
	 */
	public boolean isSummito(int index, int index2, String direction) {
		int orientation = getOrientation(index, index2);

		if (compareOrientationDirection(orientation, direction)) {
			return true;
		}
		return false;
	}

	/**
	 * checks if the move is technically a summito
	 * 
	 * @param index
	 * @param index2
	 * @param index3
	 * @param direction
	 * @return true if the move is technically a summito
	 */
	public boolean isSummito(int index, int index2, int index3, String direction) {
		int orientation = getOrientation(index, index2, index3);
		if (compareOrientationDirection(orientation, direction)) {
			return true;
		} else
			return false;
	}

	public void attackMove(int index, int index2, String direction) {
		Marble placeholderAttacker = getMarble(index);

		int [] indexes =new int[2];
		indexes[0] = index;
		indexes[1] = index2 ;
		Arrays.sort(indexes); 
		
		switch (direction) {
		case Directions.east:
			
			if (getMarble(getEast(getEast(indexes[1]))) == Marble.Death){
				move(index, index2, direction);
				addScore(placeholderAttacker); 
			} 
			else {
				move(getEast(indexes[1]),direction);
				move(index, index2, direction);
			}
			break;

		case Directions.west:
			if (getMarble(getWest(getWest(indexes[0]))) == Marble.Death) {
				move(index, index2, direction);
				addScore(placeholderAttacker);
			}
			else {
				move(getEast(indexes[0]),direction);
				move(index, index2, direction); 
			}
			break;

		case Directions.northEast:
			if (getMarble(getNorthEast(getNorthEast(indexes[0]))) == Marble.Death) {
				move(index, index2, direction);
				addScore(placeholderAttacker);
			}
			else {
				move(getNorthEast(indexes[0]),direction);
				move(index, index2, direction); 
			}
			break;

		case Directions.northWest:
			if (getMarble(getNorthWest(getNorthWest(indexes[0]))) == Marble.Death) {
				move(index, index2, direction);
				addScore(placeholderAttacker);
			}
			else {
				move(getNorthWest(indexes[0]),direction);
				move(index, index2, direction);  
			}
			break;

		case Directions.southEast:
			if (getMarble(getSouthEast(getSouthEast(indexes[1]))) == Marble.Death) {
				move(index, index2, direction);
				addScore(placeholderAttacker);
			}
			else { 
				move(getSouthEast(indexes[1]),direction);
				move(index, index2, direction);  
			}
			break;
		case Directions.southWest:
			if (getMarble(getSouthWest(getSouthWest(indexes[1]))) == Marble.Death) {
				move(index, index2, direction);
				addScore(placeholderAttacker);
			} 
			else { 
				move(getSouthWest(indexes[1]),direction);
				move(index, index2, direction);  
			}
			break;

		}

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

	/**
	 * checks if the orientation is inline with the direction
	 * 
	 * @param orientation
	 * @param direction
	 * @return true if the direction is inline with the orientation
	 */
	public boolean compareOrientationDirection(int orientation, String direction) {
		if (orientation == 1) {
			if (direction.equals(Directions.northWest) || direction.equals(Directions.southEast)) {
				return true;
			}
		}
		if (orientation == 2) {
			if (direction.equals(Directions.northEast) || direction.equals(Directions.southWest)) {
				return true;
			}
		}
		if (orientation == 3) {
			if (direction.equals(Directions.east) || direction.equals(Directions.west)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get the orrientation the the given indexes,
	 * 
	 * @requires that it is already checked that they are neighbours.
	 * @param index
	 * @param index2
	 * @return 1 if orrientation is NW or SE 2 if orrientation is NE or SW 3 if
	 *         orrientation is E or W if something goes wrong return 9999
	 */
	public int getOrientation(int index, int index2) {
		int[] indexes = new int[2];
		indexes[0] = index;
		indexes[1] = index2;
		Arrays.sort(indexes);

		if (getEast(indexes[0]) == indexes[1] || getWest(indexes[0]) == indexes[1]) {
			return 3;
		}
		if (getNorthEast(indexes[0]) == indexes[1] || getSouthWest(indexes[0]) == indexes[1]) {
			return 2;
		}
		if (getNorthWest(indexes[0]) == indexes[1] || getSouthEast(indexes[0]) == indexes[1]) {
			return 1;
		}
		return 9999;
	}

	/**
	 * get the orrientation the the given indexes,
	 * 
	 * @requires that the given indexes are already on one line
	 * @param index
	 * @param index2
	 * @param index3
	 * @return 1 if orrientation is NW or SE 2 if orrientation is NE or SW 3 if
	 *         orrientation is E or W if something goes wrong return 9999
	 */
	public int getOrientation(int index, int index2, int index3) {
		int[] indexes = new int[3];
		indexes[0] = index;
		indexes[1] = index2;
		indexes[2] = index3;
		Arrays.sort(indexes);
		return getOrientation(indexes[0], indexes[1]);
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
	 * loops through all the scores of the players, if one score is higher as 6, the
	 * function will return true
	 * 
	 * @return
	 */
	public boolean GameOver() {
		if (scoreBlack < 6|| scoreGreen< 6|| scoreRed< 6 || scoreWhite< 6) {
			return true;
		}
		return false;
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

				if (getMarble(row, col).toString().equals("Death")) {
					s = s + "";
				} else if (getMarble(row, col).toString().equals("Empty")) {
					s = s + "   -" + "(" + indexToProtocol(Getindex(row, col)) + ")" + "-   ";

				} else if (getMarble(row, col).toString().equals("Red")) {
					s = s + "  " + getMarble(row, col).toString() + "(" + indexToProtocol(Getindex(row, col)) + ")"
							+ "  ";
				} else {
					s = s + " " + getMarble(row, col).toString() + "(" + indexToProtocol(Getindex(row, col)) + ")"
							+ " ";
				}
			}
			s = s + "\n\n" + "\n\n";
		}

		return s;
	}

}
