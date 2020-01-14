package Abalone;

import java.util.Arrays;

public class MoveCheck {
	Board board;
	int[] index1 = new int[1];
	int[] index2 = new int[2];
	int[] index3 = new int[3];
	Player player;
	
	public boolean checkMove(Player player, Board board, int index, String direction) {
		this.board = board;
		this.player = player;
		index1[0] = index;
		return isValidMove(index1, direction);
	}
	
	public boolean checkMove(Player player, Board board, int i1, int i2, String direction) {
		this.board = board;
		this.player = player;
		index2[0] = i1;
		index2[1] = i2;
		Arrays.sort(index2);
		return isValidMove(index2, direction);
	} 

	public boolean checkMove(Player player, Board board, int i1, int i2, int i3, String direction) {
		this.board = board;
		this.player = player;
		index3[0] = i1;
		index3[1] = i2;
		index3[2] = i3;
		Arrays.sort(index3);
		return isValidMove(index3, direction);
	}  
	
	
	public boolean isValidMove(int[] index, String direction) {
		if (isOwnMarble(index)) {
			if (index.length == 1) {
				if (board.getMarble(getNeighbourIndex(index[0], direction)) == Marble.Empty) {
					return true;
				}
			} else if (index.length == 2 || index.length == 3) {
				// For 2 or 3 marbles, it must always be checked if they are neighbours
				if (isNeighbours(index)) {
					// Checks if first marble after a summito move for 2 marbles is empty
					if (isSummitoMove(index, direction)) {
						if (isValidSummitoMove(index, direction)) {
							return true;
						} else if (isValidSummitoAttack(index, direction)) {
							return true;
						}
					} else if (isValidNormalMove(index, direction)) {
							return true; 
						} 
					} 
				}
			} 
		return false;
	}
	
	//getDirection --> determine summito --> check with given direction
	public boolean isValidSummitoMove(int[] index, String direction) {
		Marble nextM = board.getMarble(getNeighbourIndex(index[getFirstorLast(index,direction)], direction));
		if (nextM == Marble.Empty) {
				return true;
		}
		return false;
	}
	
	public boolean isSummitoMove(int[] index, String direction) {
		int givenOrientation = getOrientation(direction);
		int indexOrientation = getOrientation(getDirection(index));
		if (givenOrientation == indexOrientation) {
			return true;
		}
		return false;
	}
	
	
	/* Checks whether the summito is a valid summito attack
	 * @requires given that there's at least 2 or 3 marbles
	 * 
	 */
	
	public boolean isValidSummitoAttack(int[] index, String direction) {
		if (isValidSummitoMove(index, direction)) {
			int nextI = getNeighbourIndex(index[getFirstorLast(index, direction)], direction);
			Marble nextM = board.getMarble(nextI);
			int nextnextI = getNeighbourIndex(nextI, direction);
			Marble nextnextM = board.getMarble(nextnextI);
			
			if (isOpponent(nextM)) {
				if (index.length == 2) {
					if (nextnextM == Marble.Empty || nextnextM == Marble.Death) {
						return true;
					}
				} else if (index.length == 3) {
					int nextnextnextI = getNeighbourIndex(nextnextI, direction);
					Marble nextnextnextM = board.getMarble(nextnextnextI);
					
					if (nextnextM == Marble.Death || nextnextM == Marble.Empty) {
						return true;
					} else if (isOpponent(nextnextM)) {
						if (nextnextnextM == Marble.Death || nextnextnextM == Marble.Empty) {
							return true;
						} 
					}
				}	
			} 
		} 	 
		return false;		
		}
	
	public boolean isValidNormalMove(int[] index, String direction) {
		Marble[] marble = new Marble[index.length];
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
	 * Determines whether the first or last index from the array should be used with the given direction
	 * @param index
	 * @param direction
	 * @return
	 */
	public int getFirstorLast(int[] index, String direction) {
		int i;
		if (direction == Directions.west || direction == Directions.northWest || direction == Directions.northEast) {
			i = 0;
		} else {
			if (index.length == 2) {
				i = 1;
			} else {
				i = 2;
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
	 * @return 1 if orrientation is NW or SE 2 if orrientation is NE or SW 3 if
	 *         orrientation is E or W if something goes wrong return 9999
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
	
	// Checks own marbles for 2, 3 or 4 players. To be added!!
	//Controleer van indexes of het eigen of team indexes zijn
	
	public boolean isOwnMarble(int[] index) {
		boolean valid = true;
		for (int i : index) {
			if (!(board.getMarble(i) == player.getMarble())) {
				//class Player to be extended
				valid = false;;
			}
		}
		return valid;
	}
	
	/**
	 * @requires the two indexes to be neighbours
	 * @param i1
	 * @param i2
	 * @return
	 */
	public String getDirection(int[] index) {
		if (getWest(index[0]) == index[1]) {
			return Directions.west;
		} else if (getEast(index[0]) == index[1]) {
			return Directions.east;
		} else if (getNorthWest(index[0]) == index[1]) {
			return Directions.northWest;
		} else if (getNorthEast(index[0]) == index[1]) {
			return Directions.northEast;
		} else if (getSouthWest(index[0]) == index[1]) {
			return Directions.southWest;
		} else if (getSouthEast(index[0]) == index[1]) {
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
	 * Checks if the given indexes are each others neighbour indexes
	 * @param index
	 * @return
	 */
	public boolean isNeighbours(int[] index) {
		int[] neighbours = board.getNeighbours(index[0]);
		for (int i : neighbours) {
			for (int j : index) {
				if (!(i == j)) {
				return false;
			}
			}
		}
		return true;
	}
	
	/**
	 * Checks if 3 indexes given are in line, requires they are sorted and neighbours
	 * @requires to have more than one index
	 * @param index
	 * @return
	 */
//	public boolean isSameDirection(int[] index) {
//		String direction1;
//		String direction2;
	//	direction1 = getDirection(index[0], index[1]);
	//	direction2 = getDirection(index[1], index[2]);
	//	if (direction1 == direction2) {
//			return true;
//		} else {
//			return false; }
	
	
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
			
	
	public boolean isOpponent(Marble marble) {
		Marble ownMarble = player.getMarble();
		if (board.getPlayerCount() == 2) {
			if (ownMarble == Marble.White && marble == Marble.Black) {
				return true;
			} else if (ownMarble == Marble.Black && marble == Marble.White) {
				return true;
			} else {
				return false;
			}
		} else if (board.getPlayerCount() == 3) {
			if (ownMarble == Marble.White && (marble == Marble.Black || marble == Marble.Green)) {
				return true;
			} else if (ownMarble == Marble.Black && (marble == Marble.White || marble == Marble.Green)) {
				return true;
			} else if (ownMarble == Marble.Green && (marble == Marble.Black || marble == Marble.White)) {
				return true;
			} else {
				return false;
			}
		} else if (board.getPlayerCount() == 4) {
			if (ownMarble == Marble.White && (marble == Marble.Red || marble == Marble.Green)) {
				return true;
			} else if (ownMarble == Marble.Black && (marble == Marble.Red || marble == Marble.Green)) {
				return true;
			} else if (ownMarble == Marble.Green && (marble == Marble.Black || marble == Marble.White)) {
				return true;
			} else if (ownMarble == Marble.Red && (marble == Marble.Black || marble == Marble.White)) {
				return true;
			} else {
				return false;
			}
		}
		return false;	
	}
}
	

