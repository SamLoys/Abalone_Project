package Abalone;

import Abalone.Marble;

public class Board {

	Marble d = Marble.Death;
	Marble w = Marble.White;
	Marble e = Marble.Empty;
	Marble b = Marble.Black;
	Marble g = Marble.Green;
	Marble r = Marble.Red;

	int playerCount = 0;
	// field keeps track of the state of the field.
	private Marble[][] fields;

	public Board(int players) {
		if (players == 2) {
			initBoard2();
			this.playerCount = 2;
		}

		if (players == 3) {
			initBoard3();
			this.playerCount = 3;
		}

		if (players == 4) {
			initBoard4();
			this.playerCount = 4;
		} else {
			System.out.println("NotValid");
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

	/**
	 * given the index of the marble, the state of the marble is returned
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
	
	

}
