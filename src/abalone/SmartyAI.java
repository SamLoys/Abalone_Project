package abalone;

import abalone.client.AbaloneClient;
import abalone.exceptions.BoardException;
import abalone.exceptions.IllegalMoveException;
import abalone.exceptions.ServerUnavailableException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The AI is focused on going to the center.
 * Created on 17-01-2019. 
 * @invariant board != null
 * @invariant marble != null
 * @invariant client != null
 * @invariant name != null
 * @author Sam Freriks and Ayla van der Wal.
 * @version 1.0
 */
public class SmartyAI extends AI {
    //Arrays with the indices corresponding the the different "rings" on the board
    static final int[] ringFive = { 16, 17, 18, 19, 20, 31, 42, 53, 64, 74, 84, 94, 104, 103, 102, 101, 100, 89, 78, 67,
        56, 46, 36, 26 };
    static final int[] ringFour = { 27, 28, 29, 30, 41, 52, 63, 73, 83, 93, 92, 91, 90, 79, 68, 57, 47, 37 };
    static final int[] ringThree = { 38, 39, 40, 51, 62, 72, 82, 81, 80, 69, 58, 48 };
    static final int[] ringTwo = { 49, 50, 61, 71, 70, 59 };
    static final int[] ringOne = { 60 };


    /**
     * the constructor of the ai.
     * Creates a new AI with the given board, color, client, checker and name.
     * @param board the board the ai player will play on
     * @param color  the color the ai represents
     * @param client the client the ai is. 
     * @param checker the checker for the move checks. 
     * @param name the name of the ai
     * @ensures this.board == board, this.color == color, this.client == client
     * @ensures this.checker == checker, this.name == name
     */
    public SmartyAI(Board board, Marble color, AbaloneClient client, MoveCheck checker, String name) {
        super(board,color,client,checker,name);
       
    }

    /**
     * returns a valid move to do , a hint.
     * @param board the board for the hint
     * @param color the color to use
     * @param checker the checker
     * @return a string that contains a marble of the color and a valid direction
     * @requires board != null, color != null, checker != null
     */
    public String getHint(Board board, Marble color, MoveCheck checker) { 
        try {
            this.board = board;
            this.color = color;
            this.checker = checker;
            makeMove(false);
            //call makemove so the direction and the index is updated
        } catch (ServerUnavailableException e) {
            // send is false so it can not happen
        }
        return "you can select marble number " + convertToProtocol.get(0) + " and move it in direction: "
                + direction.toString();
    }
    
    /**
     * sends a marble that will have a valid move.
     * @return a marble which can make a valid move. 
     * @ensures that a valid move is collected
     */
    public int getHintForAiMarbles() { 
        try {
            makeMove(false);
        } catch (ServerUnavailableException e) {
            // send is false so it can not happen
        }
        return convertToProtocol.get(0);
    }
    
    /**
     * returns the direction the previous hint was meant to go to.
     * @return the direction of the previous hint
     * @ensures the direction for the hint is given
     */
    public String getHintForAiDirection() {
        return direction.toString();
    }

    /**
     * constructs a move and send a command to the client to send it.
     * @param send if the constructed move should be send.
     * @throws ServerUnavailableException throws this exception is the server could not be reached.
     * @ensures is send to client as a move to be made if send == true
     * @requires send == true || send == false
     */
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
        //shuffle all marbles
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
        //added to a list so you can use the suffle method
        Collections.shuffle(list4);
        for (int i = 0; i < list4.size(); i++) {
            ringFour[i] = list4.get(i);
        }

        //first check if you can make the ideal move on the 5th ring
        for (int index : ringFive) {
            if (ownMarbles.contains(index) && movefound == false) {
                direction = board.getDirectionToCenter(index);
                try {
                    totalMarbles = checker.moveChecker(index, direction);
                    movefound = true;
                } catch (IllegalMoveException e) {
                    //continue to the next move
                }
            }
        }
        //check if you can make at least a move 
        for (int index : ringFive) {
            if (ownMarbles.contains(index) && movefound == false) {
                direction = board.getDirectionToCenter(index);
                try {
                    totalMarbles = checker.moveChecker(index, direction);
                    movefound = true;
                } catch (IllegalMoveException e) {
                    //continue to the next move
                }
                if (!movefound) {
                    direction = Directions.east;
                    try {
                        totalMarbles = checker.moveChecker(index, direction);
                        movefound = true;
                    } catch (IllegalMoveException e) {
                        //continue to the next move
                    }

                    if (!movefound) {
                        direction = Directions.northEast;
                        try {
                            totalMarbles = checker.moveChecker(index, direction);
                            movefound = true;
                        } catch (IllegalMoveException e) {
                            //continue to the next move
                        }
                        if (!movefound) {
                            direction = Directions.northWest;
                            try {
                                totalMarbles = checker.moveChecker(index, direction);
                                movefound = true;
                            } catch (IllegalMoveException e) {
                              //continue to the next move
                            }
                            if (!movefound) {
                                direction = Directions.west;
                                try {
                                    totalMarbles = checker.moveChecker(index, direction);
                                    movefound = true;
                                } catch (IllegalMoveException e) {
                                  //continue to the next move
                                }
                                if (!movefound) {
                                    direction = Directions.southWest;
                                    try {
                                        totalMarbles = checker.moveChecker(index, direction);
                                        movefound = true;
                                    } catch (IllegalMoveException e) {
                                      //continue to the next move
                                    }
                                    if (!movefound) {
                                        direction = Directions.southEast;
                                        try {
                                            totalMarbles = checker.moveChecker(index, direction);
                                            movefound = true;
                                        } catch (IllegalMoveException e) {
                                          //continue to the next move
                                        }
                                    }

                                }

                            }

                        }
                    }

                }

            }
        }
        //check if you can the ideal move for the 4th ring
        for (int index : ringFour) {
            if (ownMarbles.contains(index) && movefound == false) {
                direction = board.getDirectionToCenter(index);
                try {
                    totalMarbles = checker.moveChecker(index, direction);
                    movefound = true;
                } catch (IllegalMoveException e) {
                  //continue to the next move
                }
            }
        }
        //check if you can make atleast one move on the fourth ring
        for (int index : ringFour) {
            if (ownMarbles.contains(index) && movefound == false) {
                direction = board.getDirectionToCenter(index);
                try {
                    totalMarbles = checker.moveChecker(index, direction);
                    movefound = true;
                } catch (IllegalMoveException e) {
                  //continue to the next move
                }
                if (!movefound) {
                    direction = Directions.east;
                    try {
                        totalMarbles = checker.moveChecker(index, direction);
                        movefound = true;
                    } catch (IllegalMoveException e) {
                      //continue to the next move
                    }

                    if (!movefound) {
                        direction = Directions.west;
                        try {
                            totalMarbles = checker.moveChecker(index, direction);
                            movefound = true;
                        } catch (IllegalMoveException e) {
                          //continue to the next move
                        }
                        if (!movefound) {
                            direction = Directions.northEast;
                            try {
                                totalMarbles = checker.moveChecker(index, direction);
                                movefound = true;
                            } catch (IllegalMoveException e) {
                              //continue to the next move
                            }
                            if (!movefound) {
                                direction = Directions.northWest;
                                try {
                                    totalMarbles = checker.moveChecker(index, direction);
                                    movefound = true;
                                } catch (IllegalMoveException e) {
                                  //continue to the next move
                                }
                                if (!movefound) {
                                    direction = Directions.southEast;
                                    try {
                                        totalMarbles = checker.moveChecker(index, direction);
                                        movefound = true;
                                    } catch (IllegalMoveException e) {
                                      //continue to the next move
                                    }
                                    if (!movefound) {
                                        direction = Directions.southWest;
                                        try {
                                            totalMarbles = checker.moveChecker(index, direction);
                                            movefound = true;
                                        } catch (IllegalMoveException e) {
                                          //continue to the next move
                                        }
                                    }

                                }

                            }

                        }
                    }

                }

            }
        }
        //check if you can make the ideal move on the 3th ring
        for (int index : ringThree) {
            if (ownMarbles.contains(index) && movefound == false) {
                direction = board.getDirectionToCenter(index);
                try {
                    totalMarbles = checker.moveChecker(index, direction);
                    movefound = true;
                } catch (IllegalMoveException e) {
                  //continue to the next move
                }
            }
        }
        //check if you can make atleast one move on the 3th ring
        for (int index : ringThree) {
            if (ownMarbles.contains(index) && movefound == false) {
                direction = board.getDirectionToCenter(index);
                try {
                    totalMarbles = checker.moveChecker(index, direction);
                    movefound = true;
                } catch (IllegalMoveException e) {
                  //continue to the next move
                }
                if (!movefound) {
                    direction = Directions.east;
                    try {
                        totalMarbles = checker.moveChecker(index, direction);
                        movefound = true;
                    } catch (IllegalMoveException e) {
                      //continue to the next move
                    }

                    if (!movefound) {
                        direction = Directions.west;
                        try {
                            totalMarbles = checker.moveChecker(index, direction);
                            movefound = true;
                        } catch (IllegalMoveException e) {
                          //continue to the next move
                        }
                        if (!movefound) {
                            direction = Directions.northEast;
                            try {
                                totalMarbles = checker.moveChecker(index, direction);
                                movefound = true;
                            } catch (IllegalMoveException e) {
                              //continue to the next move
                            }
                            if (!movefound) {
                                direction = Directions.northWest;
                                try {
                                    totalMarbles = checker.moveChecker(index, direction);
                                    movefound = true;
                                } catch (IllegalMoveException e) {
                                  //continue to the next move
                                }
                                if (!movefound) {
                                    direction = Directions.southEast;
                                    try {
                                        totalMarbles = checker.moveChecker(index, direction);
                                        movefound = true;
                                    } catch (IllegalMoveException e) {
                                      //continue to the next move
                                    }
                                    if (!movefound) {
                                        direction = Directions.southWest;
                                        try {
                                            totalMarbles = checker.moveChecker(index, direction);
                                            movefound = true;
                                        } catch (IllegalMoveException e) {
                                          //continue to the next move
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
                  //continue to the next move

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
                  //continue to the next move

                }
                if (!movefound) {
                    direction = Directions.east;
                    try {
                        totalMarbles = checker.moveChecker(index, direction);
                        movefound = true;
                    } catch (IllegalMoveException e) {
                      //continue to the next move
                    }

                    if (!movefound) {
                        direction = Directions.west;
                        try {
                            totalMarbles = checker.moveChecker(index, direction);
                            movefound = true;
                        } catch (IllegalMoveException e) {
                          //continue to the next move
                        }
                        if (!movefound) {
                            direction = Directions.northEast;
                            try {
                                totalMarbles = checker.moveChecker(index, direction);
                                movefound = true;
                            } catch (IllegalMoveException e) {
                              //continue to the next move
                            }
                            if (!movefound) {
                                direction = Directions.northWest;
                                try {
                                    totalMarbles = checker.moveChecker(index, direction);
                                    movefound = true;
                                } catch (IllegalMoveException e) {
                                  //continue to the next move
                                }
                                if (!movefound) {
                                    direction = Directions.southEast;
                                    try {
                                        totalMarbles = checker.moveChecker(index, direction);
                                        movefound = true;
                                    } catch (IllegalMoveException e) {
                                      //continue to the next move
                                    }
                                    if (!movefound) {
                                        direction = Directions.southWest;
                                        try {
                                            totalMarbles = checker.moveChecker(index, direction);
                                            movefound = true;
                                        } catch (IllegalMoveException e) {
                                          //continue to the next move
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
                  //continue to the next move
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
                  //continue to the next move
                }
                if (!movefound) {
                    direction = Directions.east;
                    try {
                        totalMarbles = checker.moveChecker(index, direction);
                        movefound = true;
                    } catch (IllegalMoveException e) {
                      //continue to the next move
                    }

                    if (!movefound) {
                        direction = Directions.west;
                        try {
                            totalMarbles = checker.moveChecker(index, direction);
                            movefound = true;
                        } catch (IllegalMoveException e) {
                          //continue to the next move
                        }
                        if (!movefound) {
                            direction = Directions.northEast;
                            try {
                                totalMarbles = checker.moveChecker(index, direction);
                                movefound = true;
                            } catch (IllegalMoveException e) {
                              //continue to the next move
                            }
                            if (!movefound) {
                                direction = Directions.northWest;
                                try {
                                    totalMarbles = checker.moveChecker(index, direction);
                                    movefound = true;
                                } catch (IllegalMoveException e) {
                                  //continue to the next move
                                }
                                if (!movefound) {
                                    direction = Directions.southEast;
                                    try {
                                        totalMarbles = checker.moveChecker(index, direction);
                                        movefound = true;
                                    } catch (IllegalMoveException e) {
                                      //continue to the next move
                                    }
                                    if (!movefound) {
                                        direction = Directions.southWest;
                                        try {
                                            totalMarbles = checker.moveChecker(index, direction);
                                            movefound = true;
                                        } catch (IllegalMoveException e) {
                                          //continue to the next move
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        try {
            convertToProtocol = board.indexToProtocol(totalMarbles);
        } catch (BoardException e) {

            System.out.println(e.getMessage());
        }
        //if true send the message
        if (send) {
            client.sendMove(name, direction, convertToProtocol);
        }

    }
}
