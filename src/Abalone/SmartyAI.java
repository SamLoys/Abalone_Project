package Abalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Abalone.Client.AbaloneClient;
import Abalone.Exceptions.IllegalMoveException;
import Abalone.Exceptions.ServerUnavailableException;

public class SmartyAI {

	static final int[] ringFive = { 16, 17, 18, 19, 20, 31, 42, 53, 64, 74, 84, 94, 104, 103, 102, 101, 100, 89, 78, 67,
			56, 46, 36, 26 };
	static final int[] ringFour = { 27, 28, 29, 30, 41, 52, 63, 73, 83, 93, 92, 91, 90, 79, 68, 57, 47, 37 };
	static final int[] ringThree = { 38, 39, 40, 51, 62, 72, 82, 81, 80, 69, 58, 48 };
	static final int[] ringTwo = { 49, 50, 61, 71, 70, 59 };
	static final int[] ringOne = { 60 };
	ArrayList<Integer> totalMarbles = new ArrayList<Integer>();
	ArrayList<Integer> convertToProtocol = new ArrayList<Integer>();
	ArrayList<Integer> ownMarbles = new ArrayList<>();
	String direction = null;
	boolean movefound = false;

	Board board;
	Marble color;
	AbaloneClient client;
	MoveCheck checker;
	String name;

	public SmartyAI(Board board, Marble color, AbaloneClient client, MoveCheck checker, String name) {
		this.board = board;
		this.color = color;
		this.client = client;
		this.checker = checker;
		this.name = name;
	}
	
	public String getHint(Board board, Marble color, MoveCheck checker) {
		try {
			makeMove(false);
		} catch (ServerUnavailableException e) {
			//send is false so it cant happen
		}
		return "you can select marble number " + convertToProtocol.get(0)+ "and move it in direction" + direction ; 
	}

	public void makeMove(boolean send) throws ServerUnavailableException {

		totalMarbles = new ArrayList<Integer>();
		convertToProtocol = new ArrayList<Integer>();
		ownMarbles = new ArrayList<>();
		direction = null; 
		movefound = false;

		for (int i = 16; i < 105; i++) {
			if (board.getMarble(i) == color) {
				ownMarbles.add(i);
			}
		}
		Collections.shuffle(ownMarbles);

		ArrayList<Integer> list5 = new ArrayList<Integer>();
		for (int index : ringFive) {
			list5.add(index);
		}
		Collections.shuffle(list5);
		for (int i = 0; i < list5.size(); i++) {
			ringFive[i] = list5.get(i);
		}

		ArrayList<Integer> list4 = new ArrayList<Integer>();
		for (int index : ringFour) {
			list4.add(index);
		}
		Collections.shuffle(list4);
		for (int i = 0; i < list4.size(); i++) {
			ringFour[i] = list4.get(i);
		}
		
		for (int index : ringFive) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
			}
		}
		for (int index : ringFive) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
				if (!movefound) {
					direction = Directions.east;
					try {
						totalMarbles = checker.moveChecker(index, direction);
						movefound = true;
					} catch (IllegalMoveException e) {
					}

					if (!movefound) {
						direction = Directions.northEast;
						try {
							totalMarbles = checker.moveChecker(index, direction);
							movefound = true;
						} catch (IllegalMoveException e) {
						}
						if (!movefound) {
							direction = Directions.northWest;
							try {
								totalMarbles = checker.moveChecker(index, direction);
								movefound = true;
							} catch (IllegalMoveException e) {
							}
							if (!movefound) {
								direction = Directions.west;
								try {
									totalMarbles = checker.moveChecker(index, direction);
									movefound = true;
								} catch (IllegalMoveException e) {
								}
								if (!movefound) {
									direction = Directions.southWest;
									try {
										totalMarbles = checker.moveChecker(index, direction);
										movefound = true;
									} catch (IllegalMoveException e) {
									}
									if (!movefound) {
										direction = Directions.southEast;
										try {
											totalMarbles = checker.moveChecker(index, direction);
											movefound = true;
										} catch (IllegalMoveException e) {
										}
									}

								}

							}

						}
					}

				}

			}
		}
		
		for (int index : ringFour) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
			}
		}
		for (int index : ringFour) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
				if (!movefound) {
					direction = Directions.east;
					try {
						totalMarbles = checker.moveChecker(index, direction);
						movefound = true;
					} catch (IllegalMoveException e) {
					}

					if (!movefound) {
						direction = Directions.west;
						try {
							totalMarbles = checker.moveChecker(index, direction);
							movefound = true;
						} catch (IllegalMoveException e) {
						}
						if (!movefound) {
							direction = Directions.northEast;
							try {
								totalMarbles = checker.moveChecker(index, direction);
								movefound = true;
							} catch (IllegalMoveException e) {
							}
							if (!movefound) {
								direction = Directions.northWest;
								try {
									totalMarbles = checker.moveChecker(index, direction);
									movefound = true;
								} catch (IllegalMoveException e) {
								}
								if (!movefound) {
									direction = Directions.southEast;
									try {
										totalMarbles = checker.moveChecker(index, direction);
										movefound = true;
									} catch (IllegalMoveException e) {
									}
									if (!movefound) {
										direction = Directions.southWest;
										try {
											totalMarbles = checker.moveChecker(index, direction);
											movefound = true;
										} catch (IllegalMoveException e) {
										}
									}

								}

							}

						}
					}

				}

			}
		}
		
		for (int index : ringThree) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
			}
		}
		for (int index : ringThree) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
				if (!movefound) {
					direction = Directions.east;
					try {
						totalMarbles = checker.moveChecker(index, direction);
						movefound = true;
					} catch (IllegalMoveException e) {
					}

					if (!movefound) {
						direction = Directions.west;
						try {
							totalMarbles = checker.moveChecker(index, direction);
							movefound = true;
						} catch (IllegalMoveException e) {
						}
						if (!movefound) {
							direction = Directions.northEast;
							try {
								totalMarbles = checker.moveChecker(index, direction);
								movefound = true;
							} catch (IllegalMoveException e) {
							}
							if (!movefound) {
								direction = Directions.northWest;
								try {
									totalMarbles = checker.moveChecker(index, direction);
									movefound = true;
								} catch (IllegalMoveException e) {
								}
								if (!movefound) {
									direction = Directions.southEast;
									try {
										totalMarbles = checker.moveChecker(index, direction);
										movefound = true;
									} catch (IllegalMoveException e) {
									}
									if (!movefound) {
										direction = Directions.southWest;
										try {
											totalMarbles = checker.moveChecker(index, direction);
											movefound = true;
										} catch (IllegalMoveException e) {
										}
									}

								}

							}

						}
					}

				}

			}
		}
		for (int index : ringTwo) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
			}
		}
		for (int index : ringTwo) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
				if (!movefound) {
					direction = Directions.east;
					try {
						totalMarbles = checker.moveChecker(index, direction);
						movefound = true;
					} catch (IllegalMoveException e) {
					}

					if (!movefound) {
						direction = Directions.west;
						try {
							totalMarbles = checker.moveChecker(index, direction);
							movefound = true;
						} catch (IllegalMoveException e) {
						}
						if (!movefound) {
							direction = Directions.northEast;
							try {
								totalMarbles = checker.moveChecker(index, direction);
								movefound = true;
							} catch (IllegalMoveException e) {
							}
							if (!movefound) {
								direction = Directions.northWest;
								try {
									totalMarbles = checker.moveChecker(index, direction);
									movefound = true;
								} catch (IllegalMoveException e) {
								}
								if (!movefound) {
									direction = Directions.southEast;
									try {
										totalMarbles = checker.moveChecker(index, direction);
										movefound = true;
									} catch (IllegalMoveException e) {
									}
									if (!movefound) {
										direction = Directions.southWest;
										try {
											totalMarbles = checker.moveChecker(index, direction);
											movefound = true;
										} catch (IllegalMoveException e) {
										}
									}

								}

							}

						}
					}

				}

			}
		}
		for (int index : ringOne) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
			}
		}
		for (int index : ringOne) {
			if (ownMarbles.contains(index) && movefound == false) {
				direction = board.getDirectionToCenter(index);
				try {
					totalMarbles = checker.moveChecker(index, direction);
					movefound = true;
				} catch (IllegalMoveException e) {

				}
				if (!movefound) {
					direction = Directions.east;
					try {
						totalMarbles = checker.moveChecker(index, direction);
						movefound = true;
					} catch (IllegalMoveException e) {
					}

					if (!movefound) {
						direction = Directions.west;
						try {
							totalMarbles = checker.moveChecker(index, direction);
							movefound = true;
						} catch (IllegalMoveException e) {
						}
						if (!movefound) {
							direction = Directions.northEast;
							try {
								totalMarbles = checker.moveChecker(index, direction);
								movefound = true;
							} catch (IllegalMoveException e) {
							}
							if (!movefound) {
								direction = Directions.northWest;
								try {
									totalMarbles = checker.moveChecker(index, direction);
									movefound = true;
								} catch (IllegalMoveException e) {
								}
								if (!movefound) {
									direction = Directions.southEast;
									try {
										totalMarbles = checker.moveChecker(index, direction);
										movefound = true;
									} catch (IllegalMoveException e) {
									}
									if (!movefound) {
										direction = Directions.southWest;
										try {
											totalMarbles = checker.moveChecker(index, direction);
											movefound = true;
										} catch (IllegalMoveException e) {
										}
									}

								}

							}

						}
					}

				}

			}
		}

		
		convertToProtocol = board.indexToProtocol(totalMarbles);
		if (send) {
			client.sendMove(name, direction, convertToProtocol);
		}
		
	}
}
