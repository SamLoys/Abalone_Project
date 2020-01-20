package Abalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoveCheck {
	Board board;
	ArrayList<Integer> index;

	Marble color;

//	public static void main(String[] args) {
//		Board board = new Board(4);
//		Board copy = board.deepCopy();
//		player = new Player(Marble.Green, "Henk");
//
//  	copy.setMarble(82, Marble.White);
//		MoveCheck mv = new MoveCheck(player, copy);
//		ArrayList<Integer> test = mv.moveChecker(93, 81, 69, Directions.northEast);
//		System.out.println(test);
//	}

	public MoveCheck(Marble color, Board board) {
		this.board = board;

		this.color = color;
	}
	
	public Marble getColor() {
		return color; 
	}

	/*
	 * @requires that the indexes are already converted to own indexes insteads of
	 * protocol indexes
	 */
	public ArrayList<Integer> moveChecker(int i1, String direction) {
		index = new ArrayList<Integer>();
		index.add(i1);
		if (isOnBoard(index)) {
			Collections.sort(index);
			ArrayList<Integer> newList = completeList(index, direction);
			ArrayList<Integer> returnList = returnMoves(newList, direction);
			return returnList;
		} else {
			ArrayList<Integer> empty = new ArrayList<Integer>();
			return empty;
		}
	}

	public ArrayList<Integer> moveChecker(int i1, int i2, String direction) {
		index = new ArrayList<Integer>();
		index.add(i1);
		index.add(i2);
		if (isOnBoard(index)) {
			Collections.sort(index);
			ArrayList<Integer> newList = completeList(index, direction);
			ArrayList<Integer> returnList = returnMoves(newList, direction);
			return returnList;
		} else {
			ArrayList<Integer> empty = new ArrayList<Integer>();
			return empty;
		}
	}

	public ArrayList<Integer> moveChecker(int i1, int i2, int i3, String direction) {
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
			ArrayList<Integer> empty = new ArrayList<Integer>();
			return empty;
		}
	}

	public ArrayList<Integer> moveChecker(int i1, int i2, int i3, int i4, String direction) {
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
			ArrayList<Integer> empty = new ArrayList<Integer>();
			return empty;
		}
	}

	public ArrayList<Integer> moveChecker(int i1, int i2, int i3, int i4, int i5, String direction) {
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
			ArrayList<Integer> empty = new ArrayList<Integer>();
			return empty;
		}
	}

	public ArrayList<Integer> moveChecker(ArrayList<Integer> index, String direction) {
		switch (index.size()) {
		case 1:
			return moveChecker(index.get(0), direction);

		case 2:
			return moveChecker(index.get(0), index.get(1), direction);
		case 3:

			return moveChecker(index.get(0), index.get(1), index.get(2), direction);
		case 4:
			return moveChecker(index.get(0), index.get(1), index.get(3), index.get(4), direction);

		case 5:
			return moveChecker(index.get(0),index.get(1), index.get(2), index.get(3), index.get(4), direction);
		default: 
			System.out.println("ït happend"); 
			return null; 
			// this should not happen
			
		}
	}

	public ArrayList<Integer> returnMoves(ArrayList<Integer> index, String direction) {
		ArrayList<Integer> all = new ArrayList<Integer>();

		// Checks if a summito
		if (isInLine(index)) {
			if (isStraightMove(index, direction)) {
				if (isOwnMarble(index.get(0))) {
					switch ((index.size() - 1)) {
					case 1:
						if (board.getMarble(index.get(1)) == Marble.Empty) {
							all.add(index.get(0));
						}
						break;
					case 2:
						if (isOwnTeam(index.get(1))) {
							if (board.getMarble(index.get(2)) == Marble.Empty) {
								all.add(index.get(0));
								all.add(index.get(1));

							}
						}
						break;
					case 3:
						if (isOwnTeam(index.get(1))) {
							if (isOwnTeam(index.get(2))) {
								if (board.getMarble(index.get(3)) == Marble.Empty) {
									all.add(index.get(0));
									all.add(index.get(1));
									all.add(index.get(2));
								}
							} else if (isOpponent(index.get(2))) {
								if (board.getMarble(index.get(3)) == Marble.Empty
										|| board.getMarble(index.get(3)) == Marble.Death) {
									all.add(index.get(0));
									all.add(index.get(1));
									all.add(index.get(2));
								}
							}
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
									}
								}
							}

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
										}
									}
								}
							}

						}
						break;
					}
				}
			} else {
				if ((hasOwnMarble(index)) && (index.size() < 4) && (isValidSideStep(index, direction))) {
					int marbles = 0;
					for (int i = 0; i < index.size(); i++) {
						if (!(isOwnTeam(index.get(i)))) {
							break;
						} else {
							marbles++;
						}
					}
					if (index.size() == marbles) {
						for (int j = 0; j < index.size(); j++) {
							all.add(index.get(j));
						}
					}
				}
			}
		}
		return all;

	}

	// Checks if the given values are on the board
	public boolean isOnBoard(ArrayList<Integer> index) {
		for (int i : index) {
			if (!(15 < i && i < 105)) {
				return false;
			}
		}
		return true;
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
	 * @requires Stuk!! Moet nog gemaakt
	 * @param index
	 * @return
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

	// Checks if the side step includes at least one own marble
	public boolean hasOwnMarble(ArrayList<Integer> index) {
		for (int i : index) {
			if (board.getMarble(i) == color) {
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

	// Checks if this is a team marble for all 2, 3 or 4 player games.
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

	// Checks if the input marble is your own marble
	public boolean isOwnMarble(int i1) {
		if (board.getMarble(i1) == color) {
			return true;
		}
		return false;
	}

	// Completes and finalizes the index list from the client
	public ArrayList<Integer> completeList(ArrayList<Integer> index, String direction) {
		ArrayList<Integer> newIndex = new ArrayList<Integer>();
		newIndex.addAll(index);
		Collections.sort(newIndex);
		// If between 1 and 4, this will check if there is a hidden summito and add the
		// indexes
		newIndex = flipList(index, direction);
		if (index.size() == 1 || isStraightMove(index, direction)) {
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

	public ArrayList<Integer> getHiddenSummito(ArrayList<Integer> index, String direction) {
		int ownTeam = 0;
		int opponent = 0;

		while (index.size() < 3 && ownTeam <= 3) {
			int n = getNeighbourIndex(index.get(index.size() - 1), direction);
			if (isOwnTeam(n)) {
				ownTeam++;
				index.add(n);
			} else {
				break;
			}
		}
		int n;
		while (index.size() <= 5 && opponent <= 2) {
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

//	public String stringMove(ArrayList<Integer> index, String direction) {
//	String s = "";
//	int ownMarble = 0;
//	int teamMarble = 0;
//	int oppMarble = 0;
//	int emptyMarble = 0;
//	int deathMarble = 0;
//
//	if (isStraightMove(index, direction) && index.size() == 2) {
//		if (isOwnMarble(index.get(0))) {
//			s = s + "Y";
//			if (board.getMarble(index.get(1)) == Marble.Empty) {
//				return s = s + "E";
//			}
//		}
//	}
//
//	// Checks if a summito
//	if (isInLine(index)) {
//		if (isStraightMove(index, direction)) {
//			if (isOwnMarble(index.get(0))) {
//				for (int j = 0; (j < index.size() - 1); j++) {
//					if (board.getMarble(index.get(j)) == Marble.Empty
//							|| board.getMarble(index.get(j)) == Marble.Death) {
//						s = s + "False input, empty or death spaces";
//					}
//				}
//				s = s + "Y";
//				int i = 0;
//				while ((i < index.size() - 1) && (isOwnTeam(index.get(i)))) {
//					int n = getNeighbourIndex(index.get(i), direction);
//					if (isOwnTeam(n)) {
//						teamMarble++;
//						s = s + "T";
//						if (teamMarble > 3) {
//							return s = s + "Too many marbles moved";
//						}
//					} else {
//						break;
//					}
//					i++;
//				}
//				while (isOpponent(index.get(i))) {
//					int n = getNeighbourIndex(index.get(i), direction);
//					if (isOpponent(n) || oppMarble < teamMarble) {
//						oppMarble++;
//						s = s + "O";
//						index.add(n);
//					} else {
//						break;
//					}
//					i++;
//				}
//				if (oppMarble >= ownMarble) {
//					return s = s + "Too many opponents";
//				}
//				if (board.getMarble(index.get(index.size() - 1)) == Marble.Empty) {
//					emptyMarble++;
//					s = s + "E";
//				} else if (board.getMarble(index.get(index.size() - 1)) == Marble.Death) {
//					deathMarble++;
//					s = s + "D";
//				}
//
//			} else {
//				s = s + "Not own marble first";
//			}
//
//			// Checks if a sidestep move is valid
//		} else {
//			if (hasOwnMarble(index) && index.size() < 4) {
//				for (int i : index) {
//					if (!(isOwnTeam(i))) {
//						s = s + "Contains wrong marble";
//					} else {
//						if (isValidSideStep(index, direction)) {
//							teamMarble++;
//							s = s + "T";
//						} else {
//							return s = s + "Not valid sidestep";
//						}
//					}
//				}
//			} else {
//				return s = s + "Not own marble in sidestep or sidestep too long";
//			}
//			return s;
//		}
//	} else {
//		return s = s + "Not in line";
//	}
//	return s;
//}
}
