import java.util.Iterator;
import java.util.Scanner;

/**
 * A basic Mine Sweeper game using Board class.
 * Since it is designed to test Board class, many features of the game is fixed.
 * ( number of mines, size of the board)
 * 
 * @author win7
 * @version 1
 */

//NOTE: No range checking is done.
//TODO: Add range checking and exceptions.
public class MineSweeper {

	//A - Properties & Constants
	//A.1 Constants
	private final int BOARD_ROWS = 10;
	private final int BOARD_COLS = 10;
	private final int NO_OF_MINE = 12;
	private final int OPEN = 1;
	private final int CLOSED = 2;
	private final int FLAGGED = 3;
	private final int MINED = 4;
	private final int CLEAR = 5;
	
	private final int IND_OPEN_CLOSED = 0;
	private final int IND_MINED = 1;
	private final int IND_NUMBER = 2;
	
	//A.2 Properties
	private Board   board;
	private boolean hasWon;
	private boolean isOver;
	private boolean isStarted;
	private int		noOfClearBox; //No of remaining closed clear boxes
	
	//B - Constructors
	public MineSweeper() {
		
		board = new Board( BOARD_ROWS, BOARD_COLS);
		isStarted = false;
		hasWon = false;
		isOver = false;
	}
	
	//C - Methods
	
	//C.1 - Get Methods
	public boolean isOver() {
		
		return isOver;
	}
	
	public boolean hasPlayerWon() {
		
		return hasWon;
	}
	
	public boolean isStarted() {
		
		return isStarted;
	}
	
	//C.2 - Other Methods
	public void newGame() {
		
		isStarted = true;
		hasWon = false;
		isOver = false;
		
		Iterator<Tile> it = board.iterator();
		while( it.hasNext())
			it.next().addState( CLOSED);
		
		setMines( NO_OF_MINE);
	}
	
	public boolean open( int row, int col) {
		
		//Return false if game is over after opening the box.
		
		Tile tile = board.getTile( row, col);
		if( tile.getState( IND_OPEN_CLOSED) == OPEN || tile.getState( IND_OPEN_CLOSED) == FLAGGED) {
			
			System.out.println( "Cannot open the box already opened or flagged"); //Need to change with exception
			return true;
		}
		
		if( board.getState( row, col, IND_MINED) == MINED) {
			
			isOver = true; //Lost
			return false;
		}
		
		else {
			
			tile.setState( IND_OPEN_CLOSED, OPEN);
			
			int noOfNearMines = 0;
			
			//Code Block Here
			Iterator<Tile> it = board.getNearTiles( tile).iterator();
			
			while( it.hasNext()) {
				
				Tile nearTile = it.next();
				if( nearTile.getState( IND_MINED) == MINED)
					noOfNearMines++;
			}
			
			tile.addState( noOfNearMines);
			
			noOfClearBox--;
			if( noOfClearBox == 0) {
				
				isOver = true;
				hasWon = true;
				return true;
			}
			
			if( noOfNearMines == 0) {
				
				Iterator<Tile> it2 = board.getNearTiles( tile).iterator();
				
				while( it2.hasNext()) {
					
					Tile tileToOpen = it2.next();
					if( tileToOpen.getState( IND_OPEN_CLOSED) == CLOSED)
						open( tileToOpen.row, tileToOpen.col);
				}
			}
		}
		
		return false;
	}
	
	public void flag( int row, int col) {
		
		if( board.getState( row, col, IND_OPEN_CLOSED) == CLOSED)
			board.setState( row, col, IND_OPEN_CLOSED, FLAGGED);
	}
	
	private void setMines( int noMine) {
		
		int size = BOARD_ROWS * BOARD_COLS;
		noOfClearBox = size - noMine;
		
		for( int i = 0; i < size; i++)
			board.addState( i / BOARD_ROWS, i % BOARD_COLS, CLEAR);
		
		int[] randomNumbers = new int[ size];
		for( int i = 0; i < size; i++)
			randomNumbers[i] = i;
		
		int rand;
		int temp;
		for( int i = size - 1; i > 0; i--) {
			
			rand = (int)( Math.random() * ( i + 1));
			temp = randomNumbers[i];
			randomNumbers[i] = randomNumbers[rand];
			randomNumbers[rand] = temp;
		}
		
		for( int i = 0; i < noMine; i++)
			board.getTile( randomNumbers[i] / BOARD_ROWS, randomNumbers[i] % BOARD_COLS).setState( IND_MINED, MINED);
	}
	
	//C.3 - Test Methods
	public void display() {
		
		for( int i = 0; i < BOARD_ROWS; i++) {
			
			for( int j = 0; j < BOARD_COLS; j++) {
				
				String result = ".";
				if( board.getState( i, j, IND_OPEN_CLOSED) == CLOSED)
					result = "#";
				
				else if( board.getState( i, j, IND_OPEN_CLOSED) == FLAGGED)
					result = "/";
				
				else {
					
					if( board.getState( i, j, IND_MINED) == MINED)
						result = "*";
					
					else
						result = "" + board.getState( i, j, IND_NUMBER);
				}
				System.out.print( result + " ");
			}
			
			System.out.println();
		}
	}
	
	public static void main( String[] args) {
		
		Scanner scanner = new Scanner( System.in);
		int row, col;
		
		System.out.println( "Welcome to the game\n\n");
		
		MineSweeper game = new MineSweeper();
		game.newGame();
		
		while( !game.isOver()) {
			
			game.display();
			System.out.println();
			row = scanner.nextInt();
			col = scanner.nextInt();
			game.open( row, col);
		}
		
		game.display();
		if( game.hasPlayerWon())
			System.out.println( "You won");
		
		else
			System.out.println( "You lost");
		
		scanner.close();
	}
}
