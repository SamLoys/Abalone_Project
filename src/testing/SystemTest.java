package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import abalone.client.AbaloneClient;
import abalone.exceptions.ServerUnavailableException;
import abalone.server.AbaloneServer;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;




public class SystemTest implements Runnable{
    
    AbaloneClient client; 
    AbaloneServer server; 
    String serverorclient; 
    
    /**
     * construct the client and server.
     */
    
    public static void main(String[] args) {
        Thread t1 = new Thread(new SystemTest("server"));
        t1.start();
        Thread t2 = new Thread(new SystemTest("client"));
        t2.start();
        
    }
    
    /**
     * create a server or a client.
     * @param serverorclient string with server or client
     */
    public SystemTest(String serverorclient) {
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
                client = new AbaloneClient("Henk", "192.168.10.103", 8888, true);
                client.handleHandshake(false, false, false, "Henk");
               
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerUnavailableException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  
            break;
        default:
            break;
    } 
    }
    
}
