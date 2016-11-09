package vue;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import modele.Plan;

public class EcouteurResize extends ComponentAdapter{
	private Plan plan;
	private VuePlan vuePlan;
	public void componentResized(ComponentEvent e){
		vuePlan.update();
	}
}
