package Abalone.Client;

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
	private PrintWriter consoleOUT;
	private BufferedReader consoleIN;

	public AbaloneClientTUI(AbaloneClient client) {
		this.client = client;
		consoleOUT = new PrintWriter(System.out, true);
		consoleIN = new BufferedReader(new InputStreamReader(System.in));
	}

	public void run() {
		boolean looping = true;
		while (looping) {
			String input;
			try {
				input = consoleIN.readLine();
				handleUserInput(input);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				looping = false;
				// client.sendExit();
				e.printStackTrace();
//			} catch (IOException e) {
//				throw new ServerUnavailableException("The server is unavailble");
//			}
			}
		}
	}

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

		case ProtocolMessages.EXIT:

			break;
		case "h":
			printHelpMenu();
			break;

		default:
			showMessage("Invalid command please try again");

			break;
		}
		}
	}

	public void printHelpMenu() {
		String helpmenu = "HELP MENU \n " + "To move a marble type <m><direction><marbles>" + "For example, <m r 2 3>"
				+ "direction: " + "Type r for right" + "Type l for left" + "Type ur for upper right"
				+ "Type ul for upper left" + "Type lr for lower right" + "Type ll for lower left"
				+ "Type H for this help menu" + "Type Q for the queue list";
		showMessage(helpmenu);
	}

	public InetAddress getIp() {
		/**
		 * Ask the user to input a valid IP. If it is not valid, show a message and ask
		 * again.
		 * 
		 * @return a valid IP
		 */

		BufferedReader inputIn = new BufferedReader(new InputStreamReader(System.in));
		String ip = null;
		while (true) {
			System.out.println("please enter a valid IP adress:");
			try {
				ip = inputIn.readLine();
			} catch (IOException e) {

				e.printStackTrace();
			}
			boolean valid = false;
			try {
				if (ip == null || ip.isEmpty()) {
					valid = false;
				}

				String[] parts = ip.split("\\.");
				if (parts.length != 4) {
					valid = false;
				}

				for (String s : parts) {
					int i = Integer.parseInt(s);
					if ((i < 0) || (i > 255)) {
						valid = false;
					}
				}
				if (ip.endsWith(".")) {
					valid = false;
				}

				valid = true;
			} catch (NumberFormatException nfe) {
				valid = false;
			}

			if (valid) {
				InetAddress addr = null;
				try {
					addr = InetAddress.getByName(ip);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return addr;
			}
			System.out.println("INVALID");
		}

	}

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
		consoleOUT.println(message);
	}

}
