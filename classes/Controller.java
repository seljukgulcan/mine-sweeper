/**
 * Controller class is the connection between user interface and model classes.
 * 
 * @author win7
 * @version 1
 */

public class Controller {

	//A - Properties
	public static final int CLOSED = -1;
	public static final int FLAGGED = -2;
	public static final int MINED = -3;
	
	public static final int EASY = 0;
	public static final int MED = 1;
	public static final int HARD = 2;
	
	private MineSweeper		game;
	private Views			views;
	
	private int[][]			lastState;
	
	//B - Constructors
	public Controller() {
		
		game = new MineSweeper( MineSweeper.EASY); //By default, game's mode is easy.
		game.newGame();
		views = new Views();
		views.addView( new ViewBlue( this));
		//views.addView( new ViewConsole( this));
		
		setLastState();
		views.update();
	}
	
	//C - Methods
	
	public void newGame() {
		
		game.newGame();
		setLastState();
		views.update();
	}
	
	public void setDifficulty( int difficulty) {
		
		game.setDifficulty( difficulty);
	}
	
	public int getRows() {
		
		return game.boardRows;
	}
	
	public int getCols() {
		
		return game.boardCols;
	}
	
	public void customizeDifficulty() {
		
		//TODO: Complete the method
	}
	
	public boolean isGameOver() {
		
		return game.isOver();
	}
	
	public boolean hasPlayerWon() {
		
		return game.hasPlayerWon();
	}
	
	public void play( int row, int col) {
		
		game.open( row, col);
		
		setLastState();
		views.update();
	}
	
	public void flagOrUnflag( int row, int col) {
		
		game.flagOrUnflag( row, col);
		setLastState();
		views.update();
	}
	
	//C.1
	
	public void setLastState() {
		
		lastState = game.getLastState();
	}
	
	public int[][] getLastState() {
		
		return lastState;
	}
	
	//C.2 - Main Method
	public static void main( String[] args) {
		
		Controller main = new Controller();
	}
}
