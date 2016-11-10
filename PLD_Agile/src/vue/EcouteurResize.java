package vue;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import modele.Plan;

public class EcouteurResize extends ComponentAdapter {
    private Plan plan;
    private VuePlan vuePlan;
    private static double OCCUPATION = 0.95;

    public EcouteurResize(Plan plan, VuePlan vuePlan) {
	super();
	this.plan = plan;
	this.vuePlan = vuePlan;
    }

    public void componentResized(ComponentEvent e) {
	Point basDte = plan.getPointBasDroite();
	double maxPlan = Math.max(basDte.getX(), basDte.getY());
	Point dimVuePlan = new Point(vuePlan.getWidth(), vuePlan.getHeight());
	double minVue = Math.min(dimVuePlan.getX(), dimVuePlan.getY());
	vuePlan.setEchelle((minVue * OCCUPATION) / maxPlan);
	vuePlan.repaint();
    }
}
