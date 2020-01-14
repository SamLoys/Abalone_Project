package Abalone.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class AbaloneServerTUI {
	
	private PrintWriter console;
	
	AbaloneServerTUI(){
		console = new PrintWriter(System.out, true);
	}

	public int getInt(String question) {
		showMessage(question);
		showMessage("Please use an integer to reply");
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
		}
		catch(NumberFormatException e){ 
			showMessage("The given entry is not an integer, please try again");
			return getInt(question);
		} 
		return answerInt; 
	}
	
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
	
	public boolean getBool(String question) {
		showMessage(question);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String answer = "";
		while(true) {
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
		console.println(message + "/n");
	}
}
