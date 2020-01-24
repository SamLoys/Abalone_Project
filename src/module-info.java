module abalone_project {
	exports Abalone.Exceptions;
	exports Abalone;
	exports Abalone.Server;
	exports Abalone.Client;
	exports Abalone.protocol;
	exports Testing;

	requires java.desktop;
	requires org.junit.jupiter.api;
}