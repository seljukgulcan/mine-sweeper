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
import javax.swing.JOptionPane;
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
	
	private static final long serialVersionUID = 6553403741026826421L;
	//A - Properties
	private Controller controller;
	private JLabel[][] board;
	private int	rows;
	private int cols;
	public final ImageIcon closed = new ImageIcon( getClass().getResource( "/images/closed_tile.jpg"));
	public final ImageIcon mined = new ImageIcon( getClass().getResource( "/images/mine.jpg"));
	public final ImageIcon flagged = new ImageIcon( getClass().getResource( "/images/flagged.jpg"));
	public static final Color[] colors = { new Color( 0, 0, 0), new Color( 0, 0, 240),
										   new Color( 40, 0, 200), new Color( 80, 0, 160),
										   new Color( 120, 0, 120), new Color( 160, 0, 80),
										   new Color( 200, 0, 40), new Color( 240, 0, 0)};
	
	JMenuItem easy, med, hard, game;
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
		
		setPreferredSize( new Dimension( cols * 20, rows * 20)); 
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
			
			removeAll();
			setPreferredSize( new Dimension( cols * 20, rows * 20));
			frame.pack();
			setLayout( new GridLayout( rows, cols));
			board = new JLabel[ rows][ cols];
			for( int i = 0; i < rows; i++)
				for( int j = 0; j < cols; j++) {
					board[i][j] = new JLabel();
					board[i][j].setIcon( closed);
					board[i][j].setHorizontalAlignment( SwingConstants.CENTER );
				}
			
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
		
		if( controller.isGameOver()) {
			
			if( controller.hasPlayerWon())
				JOptionPane.showMessageDialog(frame, "The game is over, you won");
			
			else
				JOptionPane.showMessageDialog(frame, "The game is over, you lost");
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
		game = new JMenuItem( "Game");
		game.addActionListener( new MenuListener());
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
		frame.setResizable( false);
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
			
			else if( event.getSource() == game) {
				
				JOptionPane.showMessageDialog(frame,
					    "For more information, visit Git page :\nhttps://github.com/Shathra/mine-sweeper.",
					    "About the Game",
					    JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}
