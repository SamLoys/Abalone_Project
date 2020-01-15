package Abalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MoveCheck {
	Board board;
	ArrayList<Integer> index = new ArrayList<Integer>();
	String typeMove;
	Player player;

	public boolean checkMove(Player player, Board board, int i1, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		Collections.sort(index);
		return true;
	}

	public boolean checkMove(Player player, Board board, int i1, int i2, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		index.add(i2);
		Collections.sort(index);
		return true;
	}

	public boolean checkMove(Player player, Board board, int i1, int i2, int i3, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		index.add(i2);
		index.add(i3);
		Collections.sort(index);
		return true;
	}
	
	public boolean checkMove(Player player, Board board, int i1, int i2, int i3, int i4, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		index.add(i2);
		index.add(i3);
		index.add(i4);
		Collections.sort(index);
		return true;
	}
	
	public boolean checkMove(Player player, Board board, int i1, int i2, int i3, int i4, int i5, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		index.add(i2);
		index.add(i3);
		index.add(i4);
		index.add(i5);
		Collections.sort(index);
		return true;
	}
	

	
	public boolean isOwnValidMove(ArrayList<Integer> index, String direction) {
		if (isOwnMarble(index.get(getFirstorLastFront(index, direction)))) {
			if (index.size() == 1) {
				
			} else if ((index.size() == 2 && isNeighbour(index.get(0), index.get(1)))
					|| (index.size() == 3 && isInLine(index))) {
				// For 2 or 3 marbles, it must always be checked if they are neighbours
				// Checks if first marble after a summito move for 2 marbles is empty
				if (isSummitoMove(index, direction)) {
					if (isValidSummitoMove(index, direction)) {
						return true;
					} else if (isValidSummitoAttack(index, direction)) {
						return true;
					}
				} else if (isValidSideMove(index, direction)) {
					return true;
				}
			}
		}
		return false;
	}

	// getDirection --> determine summito --> check with given direction
	public boolean isValidSummitoMove(ArrayList<Integer> index, String direction) {
		Marble nextM = board.getMarble(getNeighbourIndex(index.get(getFirstorLastBack(index, direction)), direction));
		if (nextM == Marble.Empty) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the given 2 or 3 indexes make a summito move
	 * @requires array length to be at least 2
	 * @param index
	 * @param direction
	 * @return
	 */
	public boolean isSummitoMove(ArrayList<Integer> index, String direction) {
		int givenOrientation = getOrientation(direction);
		int indexOrientation = getOrientation(getDirection(index.get(0), index.get(1)));
		if (givenOrientation == indexOrientation) {
			return true;
		}
		return false;
	}

	/*
	 * Checks whether the summito is a valid summito attack
	 * 
	 * @requires given that there's at least 2 or 3 marbles
	 * 
	 */

	public boolean isValidSummitoAttack(ArrayList<Integer> index, String direction) {
		if (isValidSummitoMove(index, direction)) {
			int nextI = getNeighbourIndex(index.get(getFirstorLastBack(index, direction)), direction);
			Marble nextM = board.getMarble(nextI);
			int nextnextI = getNeighbourIndex(nextI, direction);
			Marble nextnextM = board.getMarble(nextnextI);

			if (isOpponent(nextI)) {
				if (index.size() == 2) {
					if (nextnextM == Marble.Empty || nextnextM == Marble.Death) {
						return true;
					}
				} else if (index.size() == 3) {
					int nextnextnextI = getNeighbourIndex(nextnextI, direction);
					Marble nextnextnextM = board.getMarble(nextnextnextI);

					if (nextnextM == Marble.Death || nextnextM == Marble.Empty) {
						return true;
					} else if (isOpponent(nextnextI)) {
						if (nextnextnextM == Marble.Death || nextnextnextM == Marble.Empty) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isValidSideMove(ArrayList<Integer> index, String direction) {
		Marble[] marble = new Marble[index.size()];
		int number = 0;
		for (int i : index) {
			marble[number] = board.getMarble(getNeighbourIndex(i, direction));
			i++;
		}
		for (Marble m : marble) {
			if (!(m == Marble.Empty)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines whether the first or last index from the array should be used to find the index to be pushed
	 * @param index
	 * @param direction
	 * @return
	 */
	public int getFirstorLastBack(ArrayList<Integer> index, String direction) {
		int i;
		if (direction == Directions.west || direction == Directions.northWest || direction == Directions.northEast) {
			i = 0;
		} else {
			if (index.size() == 2) {
				i = 1;
			} else if (index.size() == 3) {
				i = 2;
			} else if (index.size() == 4) {
				i = 3;
			} else {
				i = 4;
			}
		}
		return i;
	}
	
	public int getFirstorLastFront(ArrayList<Integer> index, String direction) {
		int i;
		if (direction == Directions.east || direction == Directions.southWest || direction == Directions.southEast) {
			i = 0;
		} else {
			if (index.size() == 2) {
				i = 1;
			} else if (index.size() == 3){
				i = 2;
			} else if (index.size() == 4) {
				i = 3;
			} else {
				i = 4;
			}
		}
		return i;
	}

	/**
	 * get the orientation the the given indexes,
	 * 
	 * @requires that it is already checked that they are neighbours.
	 * @param index
	 * @param index2
	 * @return 1 if orientation is NW or SE 2 if orientation is NE or SW 3 if
	 *         orientation is E or W if something goes wrong return 9999
	 */
	public int getOrientation(String direction) {
		if (direction == Directions.west || direction == Directions.east) {
			return 3;
		}
		if (direction == Directions.northEast || direction == Directions.southWest) {
			return 2;
		}
		if (direction == Directions.southEast || direction == Directions.northWest) {
			return 1;
		}
		return 9999;
	}



	/**
	 * @requires the two indexes to be neighbours
	 * @param i1
	 * @param i2
	 * @return
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
		}
		return 9999;

	}

	/**
	 * given two indexes, it checks whether the second index is the neighbour of the
	 * first index
	 * 
	 * @param index1
	 * @param index2
	 * @return true is the indexes are neighbours, else false
	 */
	public boolean isNeighbour(int i1, int i2) {
		int[] neighbours = board.getNeighbours(i1);
		for (int i = 0; i < neighbours.length; i++) {
			if (i2 == neighbours[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @requires To receive a minimum of 3 marbles
	 * Checks if the given 3 indexes are each others neighbour indexes
	 * @param index
	 * @return
	 */
	public boolean isInLine(ArrayList<Integer> index) {
		boolean valid = true;
		for (int i = 0; i < index.size(); i++) {
			String direction1 = getDirection(index.get(i), index.get(i + 1));
			String direction2 = getDirection(index.get(i + 1), index.get(i + 2));
			if (!(direction1 == direction2)) {
				valid = false;
			}
		}
		return valid;
	}
	
	
	public boolean hasOwnMarble(ArrayList<Integer> index) {
		for (int i : index) {
			if (board.getMarble(i) == player.getMarble()) {
				return true;
			}
		}
		return false;
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
	
	public boolean isOpponent(int i1) {
		Marble ownMarble = player.getMarble();
		Marble checkMarble = board.getMarble(i1);
		if (board.getPlayerCount() == 2 || board.getPlayerCount() == 3) {
			if (!(ownMarble == checkMarble)) {
				return true;
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
		return false;
	}	

	// Checks own marbles for 2, 3 or 4 players. To be added!!
	// Controleer van indexes of het eigen of team indexes zijn	
	public boolean isOwnTeam(int i1) {
		Marble ownMarble = player.getMarble();
		boolean valid = true;
		if (board.getPlayerCount() == 2 || board.getPlayerCount() == 3) {
				if (board.getMarble(i1) == ownMarble) {
					return true;
				}
			} else {
				if (ownMarble == Marble.White && (board.getMarble(i1) == Marble.Black || board.getMarble(i1) == ownMarble)) {
					return true;
				} else if (!(ownMarble == Marble.Black && (board.getMarble(i1) == Marble.White || board.getMarble(i1) == ownMarble))) {
					return true;
				} else if (!(ownMarble == Marble.Green && (board.getMarble(i1) == Marble.Red || board.getMarble(i1) == ownMarble))) {
					return true;
				} else if (!(ownMarble == Marble.Red && (board.getMarble(i1) == Marble.Green || board.getMarble(i1) == ownMarble))) {
					return true;
				} 
			}
		return false;
		} 
	
	
	public boolean isOwnMarble(int i1) {
		if (board.getMarble(i1) == player.getMarble()) {
			return true;
		}
		return false;
		
	}
	
	public ArrayList<Integer> fillSummito(ArrayList<Integer> index, String direction) {
		ArrayList<Integer> summitoList = new ArrayList<Integer>();
		if ((0 < index.size()) && (index.size() < 5)) {
			if (isHiddenSummito(index, direction)) {
				summitoList = getHiddenSummito(index, direction);
			}
		}
		ArrayList<Integer> joinList = new ArrayList<Integer>();
		joinList.addAll(index);
		joinList.addAll(summitoList);

		return joinList;
	}
	/**
	 * Checks for list with 1 to 4 indexes if there is a hidden summito
	 * @param index
	 * @param direction
	 * @return
	 */
	public boolean isHiddenSummito(ArrayList<Integer> index, String direction) {
		int next = getNeighbourIndex(getFirstorLastBack(index, direction), direction);
		if (index.size() < 3) {
			if (isOwnTeam(next)) {
				return true;
			}
		} else if (isOpponent(next)) {
			return true;
		}
		return false;
	}
	
	
	public ArrayList<Integer> getHiddenSummito(ArrayList<Integer> index, String direction) {
		int ownTeam = 0;
		int opponent = 0;
		for (int i : index) {
			if (isOwnTeam(i)) {
				ownTeam++;
			} else if (isOpponent(i)) {
				opponent++;
			}
		}
		while (index.size() < 3 && ownTeam <= 3) {
			int n = getNeighbourIndex(getFirstorLastBack(index, direction), direction);
			if (isOwnTeam(n)) {
				ownTeam++;
				index.add(n);
			} else {
				break;
			}
		}
		while (index.size() < 5 && opponent < ownTeam) {
			int n = getNeighbourIndex(getFirstorLastBack(index, direction), direction);
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
