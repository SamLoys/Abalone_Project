package Abalone.Exceptions;

public class ClientUnavailableException extends Exception {

    private static final long serialVersionUID = 3194494346431589825L;

    public ClientUnavailableException(String msg) {
        super(msg);
    }

}
