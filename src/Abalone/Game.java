package Abalone;


public class Game {
	
	private Board board; 
	
	
	public Game(int players) {
		board = new Board(players);
		
	}
	
	
	public void start() {
		boolean continueGame = true;
		while (continueGame) {
			reset();
			play();
			continueGame =false;
//			System.out.println("\n> Play another time? (y/n)?");
//			continueGame = TextIO.getBoolean();
		}
	}
	
	public void reset() {
		board.reset();
	};
	
	public void play() {
		update();
		board.move(81, 82, Directions.east);
		update();
	}
	
	public void update(){
		System.out.println(board.toString());
	}
}
