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

public class SystemTest2Player implements Runnable {
    
    AbaloneClient client; 
    AbaloneServer server; 
    String serverorclient; 
    
    /**
     * construct the client and server.
     */
    
    public static void main(String[] args) {
        //create new threads
        Thread t1 = new Thread(new SystemTest2Player("server"));
        t1.start();
        Thread t2 = new Thread(new SystemTest2Player("client"));
        t2.start(); 
        Thread t3 = new Thread(new SystemTest2Player("client"));
        t3.start(); 
        
    }
    
    /**
     * create a server or a client.
     * @param serverorclient string with server or client
     */
    public SystemTest2Player(String serverorclient) {
        this.serverorclient = serverorclient;
        //set it to server or client
    }

    @Override
    public void run() {
        //run a server or a client
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
                    //set the client
                    InetAddress ip = InetAddress.getLocalHost();
                    client = new AbaloneClient("Henk", ip, 8888, true, 2);
                    //do the handshake
                    client.handleHandshake(false, false, false, "Henk");
                    //join a queue
                    client.joinQueue(2);
                    //read the response of the server
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
