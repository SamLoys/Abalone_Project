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

	private int[] score = new int[4];

	public Board(int players) {
		if (players == 2) {
			initBoard2();
			this.playerCount = 2;
		}

		else if (players == 3) {
			initBoard3();
			this.playerCount = 3;
		}

		else if (players == 4) {
			initBoard4();
			this.playerCount = 4;
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
		for (int i = 0; i < score.length; i++) {
			if (score[i] > 6) {
				return true;
			}
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
					s = s + "   -" + "(" +indexToProtocol(Getindex(row, col)) +")" + "-   ";

				} else if (getMarble(row, col).toString().equals("Red")) {
					s = s + "  " + getMarble(row, col).toString() + "(" + indexToProtocol(Getindex(row, col)) +")" + "  ";
				} else {
					s = s + " " + getMarble(row, col).toString()+ "(" +indexToProtocol(Getindex(row, col)) +")" + " ";
				}
			}
			s = s + "\n\n" +"\n\n";
		}

		return s;
	}

}
