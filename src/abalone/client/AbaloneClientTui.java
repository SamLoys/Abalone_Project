package abalone.client;

import abalone.Directions;
import abalone.exceptions.ExitProgram;
import abalone.exceptions.ServerUnavailableException;
import abalone.protocol.ProtocolMessages;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * The Abalone Client Tui, this will read the user console input
 * Created on 17-01-2019. 
 * @author Sam Freriks and Ayla van der Wal.
 * @version 1.0
 */

public class AbaloneClientTui implements Runnable {
    AbaloneClient client;
    private PrintWriter consoleOut;
    private BufferedReader consoleIN;
    boolean looping = true; 
    boolean questionFirst = false;
    
    /**
     * constructor of the Tui, will set the client and create and input and output stream.
     * @param client the Tui needs to be connected to
     */
    public AbaloneClientTui(AbaloneClient client) {
        this.client = client;
        consoleOut = new PrintWriter(System.out, true);
        consoleIN = new BufferedReader(new InputStreamReader(System.in)); 
    }

    /**
     * Stop the Tui thread, needs to be called when the client will stop.
     */
    public void stopThread() {
        
        try {
            consoleIN.close();
            consoleOut.close();
            System.out.println("The TUI has teminated");
        } catch (IOException e) {
            System.out.println("Error closing in or out");
        }

    }
    
    /**
     * keeps reading the client Tui. 
     */
    public void run() {
        String input;
        try {
            
            while ((input = consoleIN.readLine()) != null) {
                handleUserInput(input);
            }
            
        } catch (ServerUnavailableException e) {
            System.out.println(e.getMessage());
            looping = false;
            stopThread();
            
        } catch (IOException e) {
            System.out.println("IO exception, stopping TUI");
            System.out.println(e.getMessage());
            looping = false;
            stopThread();
            
        } catch (ExitProgram e) {
            System.out.println("Exit program, stopping TUI");
            looping = false;
            stopThread();
        }
        
    }
    
    /**
     * handles the command of the user.
     * @param input command of the user
     * @throws ExitProgram exception if the 
     * @throws ServerUnavailableException if the server cannot be reached
     */
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
        //questionFirst is a variable which will become true at the moment a question is asked to the user given
        //one of the methods below, because they use the same console input, you dont want to 
        //execute one of these commands
        //so it wil skip this and go to the question
        if (!questionFirst) {
            if (!input.equals("")) {
                String command = input.substring(0, 1);
                String[] userInput = input.split(" ");
            
                ArrayList<Integer> marbles = new ArrayList<>();
                switch (command) {
                    //move 
                    case ProtocolMessages.MOVE:
                        if (userInput.length < 3) {
                            //check if the move is valid
                            showMessage("please try again");
                            printHelpMenu();
                            break;
                        }
                        if (userInput[1].equals(Directions.northEast) || userInput[1].equals(Directions.northWest)
                                || userInput[1].equals(Directions.west) || userInput[1].equals(Directions.east)
                                || userInput[1].equals(Directions.southEast) 
                                || userInput[1].equals(Directions.southWest)) {
             
                            for (int i = 0; i < userInput.length; i++) {
                                if (userInput[i].matches("([0-9]*)")) {
                                    marbles.add(Integer.parseInt(userInput[i]));
                                }
            
                            }
                            //send the move to the server
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
            
                    case ProtocolMessages.CHAT:
                        //send chat
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
                    case ProtocolMessages.HELP:
                        printHelpMenu();
                        break;
                    case ProtocolMessages.TIP:
                        String hint = client.getHint();
                        showMessage(hint);
                        break;
                    default:
                        showMessage("Invalid command please try again");
                        break;
                }
            }
        } else {
            showMessage("QuestionFirst");
        }
    } 
    
    /**
     * prints the helpmenu. 
     */
    public void printHelpMenu() {
        String helpmenu = "\n ------ \n " + "HELP MENU \n" + "To move a marble type <m><direction><marbles>" 
                + "For example, <m r 2 3> \n"
                + "direction: \n" + "Only include marbles you want to move" + "Type r for right" + "Type l for left \n"
                + "Type -> ur for upper right \n" + "Type -> ul for upper left \n" + "Type -> lr for lower right \n"
                + "Type -> ll for lower left \n" + "Type -> h for this help menu \n" + "Type -> q for the queue list \n"
                + "Type -> t to a hint type t \n -----";
        showMessage(helpmenu);
    }
    
    /** 
     * Ask the user to input a valid IP. If it is not valid, show a message and ask
     * again.
     * @return A valid ip adress
     * @ensures to keep asking until a valid ip is filled in
     */
    public InetAddress getIp() {
        questionFirst = true;
        String ip = null;
        while (true) {
            System.out.println("please enter a valid IP adress:");
            try { 
                ip = consoleIN.readLine();
            } catch (IOException e) {
                showMessage(e.getMessage());
            }
            if (ip != null) {
                if (ip.matches(
                        "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|"
                        + "1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
                    //should match this REGEX otherwise it is not an ip. 
                    InetAddress addr = null;
                    try {
                        addr = InetAddress.getByName(ip);
                    } catch (UnknownHostException e) {
                        showMessage("Invalid try again");
                    }
                    questionFirst = false;
                    return addr;
                 
                } else {
                    showMessage("empty try again");
                }
           
            }

        }

    }
    
    /**
     * given a question return a integer.
     * @param question the wanted question
     * @return a integer for the answer
     * @ensures to return a valid integer, otherwise asks again
     */
    public int getInt(String question) {
        questionFirst = true;
        while (true) {
            showMessage(question);
            showMessage("use an integer to reply");
            
            String answer = "";
            try {
                answer = consoleIN.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int answerInt = 0;
            try {
                answerInt = Integer.parseInt(answer);
                questionFirst = false;
                return answerInt;
            } catch (NumberFormatException e) {
                showMessage("The given entry is not an integer, please try again");
                
            }
           
        }
        
    }
    
    /**
     * given a question return a integer.
     * @param question the wanted question
     * @param lowend the low end of the range
     * @param highend the high end of the range
     * @return a integer for the answer
     * @throws ExitProgram  exception is the IO stoped working
     * @ensures to return a valid integer, otherwise asks again
     */
    public int getInt(String question, int lowend, int highend) throws ExitProgram {
        questionFirst = true;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            showMessage(question);
            showMessage("use an integer to reply");
            
            String answer = "";
            try {
                answer = in.readLine();
            } catch (IOException e) {
                //stoped
                throw new ExitProgram("IO has stoped working");
            }
            int answerInt = 0; 
            try {
                answerInt = Integer.parseInt(answer);
                if (lowend <= answerInt && answerInt <= highend) {
                    questionFirst = false;
                    return answerInt;
                } else {
                    showMessage("not in range");
                   
                }     
               
            } catch (NumberFormatException e) {
                showMessage("The given entry is not an integer, please try again");
            }
            
        }

    }
    
    /**
     * returns a string as answer to the given question. 
     * @param question the question
     * @return a string as answer
     * @ensures to return a string as answer
     */
    public String getString(String question) {
        questionFirst = true;
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
                questionFirst = false;
                return answer;
            }
        }

    }
    
    /**
     * asks the user name the clients want.
     * @param question to ask
     * @return a valid username
     * @ensures to keep asking 
     */
    public String getUserName(String question) {
        questionFirst = true;
        while (true) {
            showMessage(question);
         
            String answer = "";
            try {
                answer = consoleIN.readLine();
            } catch (IOException e) {

                e.printStackTrace();
            }
            answer = answer.trim();
            if (answer.matches("(\\w|[ ])+")) {
                questionFirst = false;
                return answer;
            } else {
                showMessage("This is not a valid username");
            }
        }

    }
    
    /**
     * returns a boolean for the given question.
     * @param question the wanted question
     * @return a boolean for the answer
     * @ensures to keep asking until a valid boolean is typed in.
     */
    public boolean getBool(String question) {
        questionFirst = true;
        showMessage(question);
        String answer = "";
        while (true) {
            try {
                answer = consoleIN.readLine(); 
            } catch (IOException e) {

                e.printStackTrace();
            }
            if (answer.equals("yes")) {
                questionFirst = false;
                return true;
            }
            if (answer.equals("no")) {
                questionFirst = false;
                return false;
            }
            showMessage("invalid try again, use yes or no");
        }
    }

    /**
     * prints the message in the console. 
     * @param message the message
     */
    public void showMessage(String message) {
        consoleOut.println(message);
    }

}
