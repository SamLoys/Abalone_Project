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

public class SystemTest3Player implements Runnable {
    
    AbaloneClient client; 
    AbaloneServer server; 
    String serverorclient; 
    
    /**
     * construct the client and server.
     */
    
    public static void main(String[] args) {
        
        //create threads
        Thread t1 = new Thread(new SystemTest3Player("server"));
        t1.start();
        Thread t2 = new Thread(new SystemTest3Player("client"));
        t2.start(); 
        Thread t3 = new Thread(new SystemTest3Player("client"));
        t3.start(); 
        Thread t4 = new Thread(new SystemTest3Player("client"));
        t4.start(); 
        
    }
    
    /**
     * create a server or a client.
     * @param serverorclient string with server or client
     */
    public SystemTest3Player(String serverorclient) {
        //set to a server or client
        this.serverorclient = serverorclient;
    }

    @Override
    public void run() {
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
                    //setup client
                    InetAddress ip = InetAddress.getLocalHost();
                    client = new AbaloneClient("Henk", ip, 8888, true, 3);
                    client.handleHandshake(false, false, false, "Henk");
                    //send join
                    client.joinQueue(3);
                    //read response
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
