module abalone_project {
	exports abalone.exceptions;
	exports abalone;
	exports abalone.server;
	exports abalone.client;
	exports abalone.protocol;
	exports Testing;

	requires java.desktop;
	requires org.junit.jupiter.api;
}