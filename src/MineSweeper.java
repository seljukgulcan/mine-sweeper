import java.util.Iterator;
import board.*;

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
	public static final int EASY = 0;
	public static final int MED = 1;
	public static final int HARD = 2;
	private final int OPEN = 1;
	private final int CLOSED = 2;
	private final int FLAGGED = 3;
	private final int MINED = 4;
	private final int CLEAR = 5;
	
	private final int IND_OPEN_CLOSED = 0;
	private final int IND_MINED = 1;
	private final int IND_NUMBER = 2;
	
	//A.2 Properties
	public int		boardRows;
	public int		boardCols;
	public int		noOfMines;
	
	private Board   board;
	private boolean hasWon;
	private boolean isOver;
	private boolean isStarted;
	private int		noOfClearBox; //No of remaining closed clear boxes
	
	//B - Constructors
	public MineSweeper( int difficulty)  {
		
		setDifficulty( difficulty);
		
		board = new Board( boardRows, boardCols);
		isStarted = false;
		hasWon = false;
		isOver = false;
		
		Iterator<Tile> it = board.iterator();
		
		while( it.hasNext()) {
			
			Tile tile = it.next();
			tile.addState( CLOSED);
			tile.addState( CLEAR);
			tile.addState( 0);			
		}
	}
	
	public MineSweeper( int rows, int cols, int mines)  {
		
		setRowColMine( rows, cols, mines);
		board = new Board( boardRows, boardCols);
		isStarted = false;
		hasWon = false;
		isOver = false;
		
		Iterator<Tile> it = board.iterator();
		
		while( it.hasNext()) {
			
			Tile tile = it.next();
			tile.addState( CLOSED);
			tile.addState( CLEAR);
			tile.addState( 0);			
		}
	}
	
	//C - Methods
	
	//C.1 - Get & Set Methods
	public boolean isOver() {
		
		return isOver;
	}
	
	public boolean hasPlayerWon() {
		
		return hasWon;
	}
	
	public boolean isStarted() {
		
		return isStarted;
	}
	
	public void setRowColMine( int row, int col, int mine){
		
		board = new Board( row, col);
		Iterator<Tile> it = board.iterator();
		
		while( it.hasNext()) {
			
			Tile tile = it.next();
			tile.addState( CLOSED);
			tile.addState( CLEAR);
			tile.addState( 0);			
		}
		
		boardRows = row;
		boardCols = col;
		noOfMines = mine;
	}
	
	public void setDifficulty( int difficulty)  {
		
		//TODO: Make this numbers constants.
		if( difficulty == EASY)
			setRowColMine( 10, 10, 12);
		
		else if( difficulty == MED) 
			setRowColMine( 16, 16, 40);
		
		else if( difficulty == HARD)
			setRowColMine( 16, 30, 100);
		
		else {
			
			//TODO : Give Exception
		}
	}
	
	//C.2 - Other Methods
	public void newGame() {
		
		hasWon = false;
		isOver = false;
		isStarted = true;
		
		Iterator<Tile> it = board.iterator();
		while( it.hasNext())
			it.next().setState( IND_OPEN_CLOSED, CLOSED);
		
		setMines( noOfMines);
	}
	
	private void reveal() {
		
		Iterator<Tile> it = board.iterator();
		while( it.hasNext()) {
			
			Tile tile = it.next();
			tile.setState( IND_OPEN_CLOSED, OPEN);
			if( tile.getState( IND_MINED) != MINED) {
				
				int noOfMines = 0;
				
				Iterator<Tile> it2 = board.getNearTiles( tile).iterator();
				while( it2.hasNext())
					if( it2.next().getState(IND_MINED) == MINED)
						noOfMines++;
				
				tile.setState( IND_NUMBER, noOfMines);
			}
		}
	}
	
	public boolean open( int row, int col) {
		
		//Return false if game is over after opening the box.
		if( row < 0 || col < 0 || row >= boardRows || col >= boardCols) {
			
			//TODO: Exception
			return true;
		}
		
		Tile tile = board.getTile( row, col);
		if( tile.getState( IND_OPEN_CLOSED) == OPEN || tile.getState( IND_OPEN_CLOSED) == FLAGGED)
			return true;
		
		if( board.getState( row, col, IND_MINED) == MINED) {
			
			isOver = true;
			reveal();
			return false; //Lost
		}
		
		else {
			
			tile.setState( IND_OPEN_CLOSED, OPEN);
			
			int noOfNearMines = 0;
			
			Iterator<Tile> it = board.getNearTiles( tile).iterator();
			
			while( it.hasNext()) {
				
				Tile nearTile = it.next();
				if( nearTile.getState( IND_MINED) == MINED)
					noOfNearMines++;
			}
			
			tile.setState( IND_NUMBER, noOfNearMines);
			
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
		
		return true;
	}
	
	public void flag( int row, int col) {
		
		if( board.getState( row, col, IND_OPEN_CLOSED) == CLOSED)
			board.setState( row, col, IND_OPEN_CLOSED, FLAGGED);
	}
	
	public void unflag( int row, int col) {
		
		if( board.getState( row, col, IND_OPEN_CLOSED) == FLAGGED)
			board.setState( row, col, IND_OPEN_CLOSED, CLOSED);
	}
	
	public void flagOrUnflag( int row, int col) {
		
		if( board.getState( row, col, IND_OPEN_CLOSED) == CLOSED) {
			
			board.setState( row, col, IND_OPEN_CLOSED, FLAGGED);
			return;
		}
		
		if( board.getState( row, col, IND_OPEN_CLOSED) == FLAGGED)
			board.setState( row, col, IND_OPEN_CLOSED, CLOSED);
	}
	
	private void setMines( int noMine) {
		
		int size = boardRows * boardCols;
		noOfClearBox = size - noMine;
		
		for( int i = 0; i < size; i++)
			board.setState( i / boardCols, i % boardCols, IND_MINED, CLEAR);
		
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
			board.getTile( randomNumbers[i] / boardCols, randomNumbers[i] % boardCols).setState( IND_MINED, MINED);
	}
	
	public int[][] getLastState() {
		
		//TODO: Use constants of Controller class and discuss its elegance.
		
		int[][] arr = new int[boardRows][boardCols];
		
		for( int i = 0; i < boardRows; i++)
			for( int j = 0; j < boardCols; j++) {
				
				Tile tile = board.getTile( i, j);
				if( tile.getState( IND_OPEN_CLOSED) == CLOSED)
					arr[i][j] = -1;
				
				else if( tile.getState( IND_OPEN_CLOSED) == FLAGGED)
					arr[i][j] = -2;
				
				else {
					
					if( tile.getState( IND_MINED) == MINED)
						arr[i][j] = -3;
					
					else
						arr[i][j] = tile.getState( IND_NUMBER);
				}
			}
		
		return arr;
	}
}
