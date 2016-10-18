package vue;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

public class VueArc extends formeGraphique{
	protected Color couleur;
	protected Point origine;
	protected Point destination;
	
	public VueArc(int origineX, int origineY,int destinationX,int destinationY)
	{
		origine = new Point(origineX, origineY);
		destination = new Point(destinationX,destinationY);
	}
	

		 
}
