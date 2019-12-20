package Abalone;

public class Board {

	private Marble[][] fields;

	public Board(int players) {
		if (players == 2) {
			initBoard2();
		}

		if (players == 3) {
			initBoard3();
		}

		if (players == 4) {
			initBoard4();
		}
		else {System.out.println("NotValid");}

	}
	
	private void initBoard2() {
		fields = new Marble[11][11]; 
		
	}
	
	private void initBoard3() {
		fields = new Marble[11][11]; 
		
	}
	
	private void initBoard4() {
		fields = new Marble[11][11]; 
		
	}
	
	

}
