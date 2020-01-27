package Abalone.Client;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Abalone.Directions;
import Abalone.Exceptions.*;
import Abalone.protocol.ProtocolMessages;

public class AbaloneClientTUI implements Runnable {
    AbaloneClient client;
    private PrintWriter consoleOut;
    private BufferedReader consoleIN;
    boolean looping = true;
    
    /** Javadoc.
     * @param client Javadoc.
     */
    public AbaloneClientTUI(AbaloneClient client) {
        this.client = client;
        consoleOut = new PrintWriter(System.out, true);
        consoleIN = new BufferedReader(new InputStreamReader(System.in));
    }

    /** Javadoc.
     */
    public void stopThread() {
        looping = false;
        try {
            consoleIN.close();
            consoleOut.close();
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            System.out.println("The TUI has teminated");
        } catch (IOException e) {
            System.out.println("Error closing consonle IN");
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }
    
    /** Javadoc.
     */
    public void run() {
        boolean looping = true;
        while (looping) {
            String input;
            try {
                input = consoleIN.readLine();
                handleUserInput(input);
            } catch (Exception e) {
                looping = false;
                // client.sendExit();
                System.out.println("Closed");
            }
        }
    }
    
    /** Javadoc.
     * @param input Javadoc.
     * @throws ExitProgram Javadoc.
     * @throws ServerUnavailableException Javadoc.
     */
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
        if (!input.equals("")) {
            String command = input.substring(0, 1);
            String[] userInput = input.split(" ");

            ArrayList<Integer> marbles = new ArrayList<>();
            switch (command) {

                case ProtocolMessages.MOVE:
                    if (userInput.length < 3) {
                        showMessage("Invalid command please try again");
                        printHelpMenu();
                        break;
                    }
                    if (userInput[1].equals(Directions.northEast) || userInput[1].equals(Directions.northWest)
                            || userInput[1].equals(Directions.west) || userInput[1].equals(Directions.east)
                            || userInput[1].equals(Directions.southEast) || userInput[1].equals(Directions.southWest)) {
     
                        for (int i = 0; i < userInput.length; i++) {
                            if (userInput[i].matches("([0-9]*)")) {
                                marbles.add(Integer.parseInt(userInput[i]));
                            }
     
                        }
     
                        client.sendMove(client.getName(), userInput[1], marbles);
                        break;
                    } else {
                        showMessage("Invalid command please try again");
                        printHelpMenu();
                        break;
                    }
     
                case ProtocolMessages.QUEUE_SIZE:
                    client.getCurrentQueueSizes();
                    break;
     
                case "c":
     
                    if (userInput.length > 1) {
                        String fullmessage = "";
                        for (int i = 1; i < userInput.length; i++) {
                            fullmessage = fullmessage + userInput[i];
                            fullmessage = fullmessage + " ";
                        }
                        client.sendChat(fullmessage);
                    }
     
                    break;
                case ProtocolMessages.EXIT:
                    client.sendExit();
                    client.closeConnection();
                    stopThread();
                    break;
                case "h":
                    printHelpMenu();
                    break;
                case "t":
                    String hint = client.getHint();
                    showMessage(hint);
                    break;
                default:
                    showMessage("Invalid command please try again");
     
                    break;
            }
        }
    }
    
    /** Javadoc.
     */
    public void printHelpMenu() {
        String helpmenu = "HELP MENU \n" + "To move a marble type <m><direction><marbles>" + "For example, <m r 2 3> \n"
                + "direction: \n" + "Only include marbles you want to move" + "Type r for right" + "Type l for left \n"
                + "Type ur for upper right" + "Type ul for upper left \n" + "Type lr for lower right"
                + "Type ll for lower left" + "Type h for this help menu \n" + "Type q for the queue list \n"
                + "To get a hint type t";
        showMessage(helpmenu);
    }
    
    /** Ask the user to input a valid IP. If it is not valid, show a message and ask
     * again.
     * @return Javadoc.
     */
    public InetAddress getIp() {
        BufferedReader inputIn = new BufferedReader(new InputStreamReader(System.in));
        String ip = null;
        while (true) {
            System.out.println("please enter a valid IP adress:");
            try {
                ip = inputIn.readLine();
            } catch (IOException e) {

                e.printStackTrace();
            }

            if (ip.matches(
                    "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|"
                    + "1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
                InetAddress addr = null;
                try {
                    addr = InetAddress.getByName(ip);
                } catch (UnknownHostException e) {
                    showMessage("Invalid try again");
                }
                return addr;
            }

        }

    }
    
    /** Javadoc.
     * @param question Javadoc.
     * @return Javadoc.
     */
    public int getInt(String question) {
        showMessage(question);
        showMessage("use an integer to reply");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        try {
            answer = br.readLine();
        } catch (IOException e) {

            e.printStackTrace();
        }
        int answerInt = 0;
        try {
            answerInt = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            showMessage("The given entry is not an integer, please try again");
            return getInt(question);
        }
        return answerInt;
    }
    
    /** Javadoc.
     * @param question Javadoc.
     * @param lowend Javadoc.
     * @param highend Javadoc.
     * @return
     */
    public int getInt(String question, int lowend, int highend) {
        showMessage(question);
        showMessage("use an integer to reply");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        try {
            answer = br.readLine();
        } catch (IOException e) {

            e.printStackTrace();
        }
        int answerInt = 0;
        try {
            answerInt = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            showMessage("The given entry is not an integer, please try again");
            return getInt(question);
        }
        if (lowend <= answerInt && answerInt <= highend) {
            return answerInt;
        } else {
            showMessage("not in range");
            return getInt(question);
        }

    }
    
    /** Javadoc.
     * @param question Javadoc.
     * @return Javadoc.
     */
    public String getString(String question) {
        while (true) {
            showMessage(question);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String answer = "";
            try {
                answer = br.readLine();
            } catch (IOException e) {

                e.printStackTrace();
            }
            if (answer.trim().equals("")) {
                showMessage("The name is empty");
            } else {
                return answer;
            }
        }

    }
    
    /** Javadoc.
     * @param question Javadoc.
     * @return Javadoc.
     */
    public String getUserName(String question) {
        while (true) {
            showMessage(question);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String answer = "";
            try {
                answer = br.readLine();
            } catch (IOException e) {

                e.printStackTrace();
            }
            answer = answer.trim();
            if (answer.matches("(\\w|[ ])+")) {
                return answer;
            } else {
                showMessage("This is not a valid username");
            }
        }

    }
    
    /** Javadoc.
     * @param question Javadoc.
     * @return Javadoc.
     */
    public boolean getBool(String question) {
        showMessage(question);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        while (true) {
            try {
                answer = br.readLine();
            } catch (IOException e) {

                e.printStackTrace();
            }
            if (answer.equals("yes")) {
                return true;
            }
            if (answer.equals("no")) {
                return false;
            }
            showMessage("invalid try again, use yes or no");

        }
    }

    public void showMessage(String message) {
        consoleOut.println(message);
    }

}
