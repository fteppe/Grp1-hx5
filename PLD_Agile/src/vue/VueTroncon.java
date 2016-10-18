package vue;

import java.awt.Color;
import java.awt.Graphics;

public class VueTroncon extends VueArc{
	
	public VueTroncon(int origineX, int origineY,int destinationX,int destinationY){
		super( origineX,  origineY, destinationX, destinationY);
		couleur = Color.blue;
	}

	public void dessiner(Graphics g){
		g.setColor(couleur);
		g.drawLine(origine.x, origine.y, destination.x, destination.y);
	}
}
