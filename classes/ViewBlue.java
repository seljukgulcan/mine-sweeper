import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A GUI for minesweeper game.
 * TODO: Extra comments
 * 
 * @author Selçuk Gülcan
 */
public class ViewBlue extends JPanel implements Viewable {
	
	//A - Properties
	private Controller controller;
	private JLabel[][] board;
	private int	rows;
	private int cols;
	public static final ImageIcon closed = new ImageIcon( "images/closed_tile.jpg");
	public static final ImageIcon mined = new ImageIcon( "images/mine.jpg");
	public static final ImageIcon flagged = new ImageIcon( "images/flagged.jpg");
	public static final Color[] colors = { new Color( 0, 0, 0), new Color( 0, 0, 240),
										   new Color( 40, 0, 200), new Color( 80, 0, 160),
										   new Color( 120, 0, 120), new Color( 160, 0, 80),
										   new Color( 200, 0, 40), new Color( 240, 0, 0)};
	
	JMenuItem easy, med, hard;
	JFrame frame;
	
	//B - Constructors
	public ViewBlue( Controller controller) {
		
		super();
		setBackground( new Color( 187, 221, 255));
		this.controller = controller;
		rows = controller.getRows();
		cols = controller.getCols();
		board = new JLabel[ rows][ cols];
		for( int i = 0; i < rows; i++)
			for( int j = 0; j < cols; j++) {
				board[i][j] = new JLabel();
				board[i][j].setIcon( closed);
				board[i][j].setHorizontalAlignment( SwingConstants.CENTER );
			}
		
		setPreferredSize( new Dimension( rows * 20, cols * 20)); 
		setLayout( new GridLayout( rows, cols));
		for( int i = 0; i < rows; i++)
			for( int j = 0; j < cols; j++)
				add( board[i][j]);
		
		addMouseListener( new GameListener());
		start();
	}
	
	//C - Methods

	@Override
	public void update() {
		
		int[][] arr = controller.getLastState();
		
		if( arr.length != rows || arr[0].length != cols) {
			
			rows = arr.length;
			cols = arr[0].length;
			
			frame.setPreferredSize( new Dimension( rows * 20, cols * 20));
			board = new JLabel[ rows][ cols];
			for( int i = 0; i < rows; i++)
				for( int j = 0; j < cols; j++) {
					board[i][j] = new JLabel();
					board[i][j].setIcon( closed);
					board[i][j].setHorizontalAlignment( SwingConstants.CENTER );
				}
			
			setPreferredSize( new Dimension( rows * 20, cols * 20)); 
			setLayout( new GridLayout( rows, cols));
			for( int i = 0; i < rows; i++)
				for( int j = 0; j < cols; j++)
					add( board[i][j]);
		}
		
		for( int i = 0; i < arr.length; i++)
			for( int j = 0; j < arr[0].length; j++) {
				
				board[i][j].setText( null);
				
				if( arr[i][j] == Controller.CLOSED)
					board[i][j].setIcon( closed);
				
				else if( arr[i][j] == Controller.FLAGGED)
					board[i][j].setIcon( flagged);
				
				else if( arr[i][j] == Controller.MINED)
					board[i][j].setIcon( mined);
				
				else {
					
					board[i][j].setIcon( null);
					if( arr[i][j] != 0)
						board[i][j].setText( "" + arr[i][j]);
					
					board[i][j].setForeground( colors[ arr[i][j]]);
				}
			}
		
	}

	@Override
	public void start() {
		
		JMenuBar bar = new JMenuBar();
		JMenu newGame = new JMenu( "New Game");
		JMenu about = new JMenu( "About");
		easy = new JMenuItem( "Easy");
		easy.addActionListener( new MenuListener());
		med = new JMenuItem( "Medium");
		med.addActionListener( new MenuListener());
		hard = new JMenuItem( "Hard");
		hard.addActionListener( new MenuListener());
		JMenuItem game = new JMenuItem( "Game");
		newGame.add( easy);
		newGame.add( med);
		newGame.add( hard);
		about.add( game);
		bar.add( newGame);
		bar.add( about);
		
		frame = new JFrame( "Minesweeper");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add( this);
		frame.setJMenuBar( bar);
		frame.pack();
		frame.setVisible( true);
	}
	
	public class GameListener implements MouseListener {

		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent event) {
			
			if(SwingUtilities.isLeftMouseButton( event))
				controller.play( event.getPoint().y / 20,  event.getPoint().x / 20);
			
			else
				controller.flagOrUnflag( event.getPoint().y / 20,  event.getPoint().x / 20);
		}
		
		
	}
	
	public class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			
			if( event.getSource() == easy) {
			
				controller.setDifficulty( Controller.EASY);
				controller.newGame();
			}
			
			else if( event.getSource() == med) {
				
				controller.setDifficulty( Controller.MED);
				controller.newGame();
			}
			
			else if( event.getSource() == hard) {
				
				controller.setDifficulty( Controller.HARD);
				controller.newGame();
			}
		}
	}
}
