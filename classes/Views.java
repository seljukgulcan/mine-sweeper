/**
 * Collection class for Viewable objects.
 * 
 *  @author Selçuk Gülcan
 *  @version 1
 */

import java.util.ArrayList;

public class Views {

	//PROPERTIES
	ArrayList<Viewable> views;
	
	//CONSTRUCTORS
	public Views() {
		
		views = new ArrayList<Viewable>();
	}
	
	//METHODS
	public void addView( Viewable view) {
		
		views.add( view);
	}
	
	public void removeView( Viewable view) {
		
		views.remove( view);
	}
	
	public void update() {
		
		for( Viewable view : views)
			view.update();
	}
	
	public void start() {
		
		for( Viewable view : views)
			view.start();
	}
}
