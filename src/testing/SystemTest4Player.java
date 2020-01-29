package testing;

import abalone.client.AbaloneClient;
import abalone.exceptions.ExitProgram;
import abalone.exceptions.ServerUnavailableException;
import abalone.server.AbaloneServer;
import java.io.IOException;
import java.net.InetAddress;


/**
 * This is the system test of the client and server.
 * This code will create multiple clients and a single server.
 * The clients will connect to the server, the clients have the AI turned on.
 * When all the clients are connected the game will be played.
 * After running this test the game will be ended with a draw.
 * @author Sam Freriks and Ayla van der Wal
 * @version 1.0
 */

public class SystemTest4Player implements Runnable {
    
    AbaloneClient client; 
    AbaloneServer server; 
    String serverorclient; 
    
    /**
     * construct the client and server.
     */
    
    public static void main(String[] args) {
        //create threads
        Thread t1 = new Thread(new SystemTest4Player("server"));
        t1.start();
        Thread t2 = new Thread(new SystemTest4Player("client"));
        t2.start(); 
        Thread t3 = new Thread(new SystemTest4Player("client"));
        t3.start(); 
        Thread t4 = new Thread(new SystemTest4Player("client"));
        t4.start();  
        Thread t5 = new Thread(new SystemTest4Player("client"));
        t5.start(); 
        
    }
    
    /**
     * create a server or a client.
     * @param serverorclient string with server or client
     */
    public SystemTest4Player(String serverorclient) {
        this.serverorclient = serverorclient;
        
    }

    @Override
    public void run() {
        //make server or client
        switch (serverorclient) {
            case "server":
                try {
                    server = new AbaloneServer(8888);
                } catch (IOException e) {
                
                    e.printStackTrace();
                }
                break;
            case "client":
                try {
                    //create client
                    InetAddress ip = InetAddress.getLocalHost();
                    client = new AbaloneClient("Henk", ip, 8888, false, 4);
                    client.handleHandshake(false, false, false, "Henk");
                    //join the queue
                    client.joinQueue(4);
                    //read the server
                    client.readServer();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (ServerUnavailableException e) {
                    System.out.println(e.getMessage());
                }  catch (ExitProgram e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                break;
        } 
    }
    
}
