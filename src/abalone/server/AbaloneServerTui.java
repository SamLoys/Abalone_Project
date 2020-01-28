package abalone.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class AbaloneServerTui implements Runnable {

    private PrintWriter console;
    private BufferedReader consoleIn;
    private AbaloneServer srv; 

    AbaloneServerTui(AbaloneServer srv) {
        console = new PrintWriter(System.out, true); 
        consoleIn = new BufferedReader(new InputStreamReader(System.in));
        this.srv = srv;
    }

    /**
     * given a question return a integer.
     * @param question the wanted question
     * @return a integer for the answer
     * @ensures to return a valid integer, otherwise asks again
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

    /**
     * returns a string as answer to the given question. 
     * @param question the question
     * @return a string as answer
     * @ensures to return a string as answer
     */
    public String getString(String question) {
        // To be implemented
        showMessage(question);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        try {
            answer = br.readLine();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return answer;
    }

    /**
     * returns a boolean for the given question.
     * @param question the wanted question
     * @return a boolean for the answer
     * @ensures to keep asking until a valid boolean is typed in.
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

    /**
     * prints the message in the console.
     * @param message the message
     */
    public void showMessage(String message) {
        console.println(message);
    }

    @Override
    public void run() {
        boolean looping = true; 
        while (looping) {
            try {
                String message = consoleIn.readLine();
                if (message.equals("x")) {
                    showMessage("Shutting down");
                    srv.shutDown();
                    
                }
            } catch (IOException e) {
                showMessage(e.getMessage());
            }
        }
        
    }
}
