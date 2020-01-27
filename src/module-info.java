module abalone_project {
    exports abalone.protocol;
    exports abalone.server;
    exports abalone.audiopack;
    exports testing;
    exports abalone.client;
    exports abalone;
    exports abalone.exceptions;

    requires java.desktop;
    requires org.junit.jupiter.api;
}