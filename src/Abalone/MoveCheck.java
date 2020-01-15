package Abalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MoveCheck {
	Board board;
	ArrayList<Integer> index = new ArrayList<Integer>();
	String typeMove;
	Player player;

	public MoveCheck(Player player, Board board, int i1, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		Collections.sort(index);
	}

	public MoveCheck(Player player, Board board, int i1, int i2, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		index.add(i2);
		Collections.sort(index);
	}

	public MoveCheck(Player player, Board board, int i1, int i2, int i3, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		index.add(i2);
		index.add(i3);
		Collections.sort(index);
		String s = moveChecker(index, direction);
		System.out.println(s);
	}

	public MoveCheck(Player player, Board board, int i1, int i2, int i3, int i4, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		index.add(i2);
		index.add(i3);
		index.add(i4);
		Collections.sort(index);
	}

	public MoveCheck(Player player, Board board, int i1, int i2, int i3, int i4, int i5, String direction) {
		this.board = board;
		this.player = player;
		index.add(i1);
		index.add(i2);
		index.add(i3);
		index.add(i4);
		index.add(i5);
		Collections.sort(index);
	}

	public String moveChecker(ArrayList<Integer> index, String direction) {
		String s = "";
		int ownMarble = 0;
		int teamMarble = 0;
		int oppMarble = 0;
		int emptyMarble = 0;
		int deathMarble = 0;

		// Checks if a summito
		if (isInLine(index)) {
			if (isStraightMove(index, direction)) {
				if (isOwnMarble(index.get(0))) {
					s = s + "Y";
					for (int i : index) {
						while (isOwnTeam(i)) {
							int n = getNeighbourIndex(i, direction);
							if (isOwnTeam(n)) {
								teamMarble++;
								s = s + "T";
							} else {
								break;
							}
						}
						while (isOpponent(i)) {
							int n = getNeighbourIndex(i, direction);
							if (isOpponent(n)) {
								oppMarble++;
								s = s + "O";
								index.add(n);
							} else {
								break;
							}
						}
						if (board.getMarble(i) == Marble.Empty) {
							emptyMarble++;
							s = s + "E";
						} else if (board.getMarble(i) == Marble.Death) {
							deathMarble++;
							s = s + "D";
						}
					}
				} else {
					s = s + "Not own marble first";
				}

				// Checks if a sidestep move is valid
			} else {
				if (hasOwnMarble(index) && index.size() < 4) {
					for (int i : index) {
						if (!(isOwnTeam(i))) {
							s = s + "Contains wrong marble";
						} else {
							teamMarble++;
						}
					}
				}
			}
		} else {
			s = "Not in line";
		}

		return s;
	}

	/**
	 * Checks if the given 2 or 3 indexes make a straight move. If not, it is a side
	 * move.
	 * 
	 * @requires array length to be at least 2
	 * @param index
	 * @param direction
	 * @return
	 */
	public boolean isStraightMove(ArrayList<Integer> index, String direction) {
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
		int nextI = getNeighbourIndex(index.get(index.size() - 1), direction);
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

	// Reverses the list to get the front marble on index 0 for the direction west,
	// northwest or northeast
	public ArrayList<Integer> flipList(ArrayList<Integer> index, String direction) {
		if (direction == Directions.west || direction == Directions.northWest || direction == Directions.northEast) {
			Collections.sort(index, Collections.reverseOrder());
		}
		return index;
	}

	/**
	 * get the orientation of the given direction
	 * 
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
	 * @requires To receive a minimum of 3 marbles Checks if the given 3 indexes are
	 *           in line
	 * @param index
	 * @return
	 */
	public boolean isInLine(ArrayList<Integer> index) {
		if (index.size() == 1) {
			return true;
		} else {
			int neighbourCount = 0;
			int[] neighbours = board.getNeighbours(index.get(0));
			for (int i = 0; i < index.size(); i++) {
				for (int j = 0; j < neighbours.length; j++) {
					if (index.get(j + 1) == neighbours[j]) {
						neighbourCount++;
						neighbours = board.getNeighbours(index.get(j + 1));
					}
				}

			}
			if (neighbourCount < index.size()) {
				return false;
			}
		}

		// Checks if the direction of all the numbers is the same
		for (int i = 0; i < (index.size() - 2); i++) {
			String direction1 = getDirection(index.get(i), index.get(i + 1));
			String direction2 = getDirection(index.get(i + 1), index.get(i + 2));
			if (!(direction1 == direction2)) {
				return false;
			}
		}
		return true;
	}

	// Checks if the side step includes at least one own marble
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

	// Checks if this is an opponent marble
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

	// Checks if this is a team marble for all 2, 3 or 4 player games.
	public boolean isOwnTeam(int i1) {
		Marble ownMarble = player.getMarble();
		boolean valid = true;
		if (board.getPlayerCount() == 2 || board.getPlayerCount() == 3) {
			if (board.getMarble(i1) == ownMarble) {
				return true;
			}
		} else {
			if (ownMarble == Marble.White
					&& (board.getMarble(i1) == Marble.Black || board.getMarble(i1) == ownMarble)) {
				return true;
			} else if (!(ownMarble == Marble.Black
					&& (board.getMarble(i1) == Marble.White || board.getMarble(i1) == ownMarble))) {
				return true;
			} else if (!(ownMarble == Marble.Green
					&& (board.getMarble(i1) == Marble.Red || board.getMarble(i1) == ownMarble))) {
				return true;
			} else if (!(ownMarble == Marble.Red
					&& (board.getMarble(i1) == Marble.Green || board.getMarble(i1) == ownMarble))) {
				return true;
			}
		}
		return false;
	}

	// Checks if the input marble is your own marble
	public boolean isOwnMarble(int i1) {
		if (board.getMarble(i1) == player.getMarble()) {
			return true;
		}
		return false;
	}

	// Completes and finalizes the index list from the client
	public ArrayList<Integer> completeList(ArrayList<Integer> index, String direction) {
		ArrayList<Integer> joinList = new ArrayList<Integer>();
		joinList.addAll(index);
		// If list is 1 and there is no hidden summito, return original list
		if (index.size() == 1 && !(isHiddenSummito(index, direction))) {
			return joinList;
		}
		// If between 1 and 4, this will check if there is a hidden summito and add the
		// indexes
		flipList(index, direction);
		if (isStraightMove(index, direction)) {
			if ((0 < index.size()) && (index.size() < 5)) {
				if (isHiddenSummito(index, direction)) {
					ArrayList<Integer> summitoList = getHiddenSummito(index, direction);
					joinList.addAll(summitoList);
				}
			}
			// If it is a straight move, this will add the index after the marbles to the
			// list
			int last = joinList.get(joinList.size() - 1);
			joinList.add(getNeighbourIndex(last, direction));
		}
		return joinList;
	}

	/**
	 * Checks if there is a hidden summito
	 * 
	 * @param index
	 * @param direction
	 * @return
	 */
	public boolean isHiddenSummito(ArrayList<Integer> index, String direction) {
		int next = getNeighbourIndex(index.get(index.size() - 1), direction);
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
			int n = getNeighbourIndex(index.get(index.size() - 1), direction);
			if (isOwnTeam(n)) {
				ownTeam++;
				index.add(n);
			} else {
				break;
			}
		}
		while (index.size() < 5 && opponent < ownTeam) {
			int n = getNeighbourIndex(index.get(index.size() - 1), direction);
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
