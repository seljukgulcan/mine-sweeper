import java.util.Scanner;

/**
 * A basic console interface for Minesweeper game.
 * 
 * - - - Controls - - -
 * TODO : Add Controls
 * 
 * @author win7
 * @version 1
 */

public class ViewConsole implements Viewable {
	
	//A - Properties
	Controller  game;
	Scanner		scanner;
	
	//B - Constructors
	public ViewConsole( Controller game) {
		
		this.game = game;
		scanner = new Scanner( System.in);
		start();
	}
	
	//C - Methods

	@Override
	public void update() {
		
		int[][] arr = game.getLastState();
		
		for( int i = 0; i < arr.length; i++) {
			
			for( int j = 0; j < arr[0].length; j++) {
				
				String result = ".";
				if( arr[i][j] == Controller.CLOSED)
					result = "#";
				
				else if( arr[i][j] == Controller.FLAGGED)
					result = "/";
				
				else if( arr[i][j] == Controller.MINED)
					result = "*";
				
				else
					result = "" + arr[i][j];
				
				System.out.print( result + " ");
			}
			
			System.out.println();
		}
		
		if( game.isGameOver()) {
			
			if( game.hasPlayerWon())
				System.out.println( "U won");
			
			else
				System.out.println( "U lost");
			
			System.out.println( "New game ? (1/0)");
			if( scanner.nextInt() == 1)
				game.newGame();
			
			return;
		}
		
		int row, col;
		
		System.out.println( "Write the coordinates (Add -1 to row input if u want to flag/unflag) :");
		row = scanner.nextInt();
		col = scanner.nextInt();
		
		if( row < 0) {
			
			row = Math.abs( row);
			row--;
			col--;
			game.flagOrUnflag( row, col);
		}
		
		else {
			
			row--;
			col--;
			game.play(row, col);
		}
	}

	@Override
	public void start() {

		System.out.println( "Welcome to the Minesweeper.");
	}
}
