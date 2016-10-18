package modele;

public class Sommet implements Comparable<Sommet> {

    private int id;
    private int position;
    private double cout;
    private Etat etat;
    
    public Sommet(int id, int position, double cout, Etat etat){
	this.id = id;
	this.position = position;
	this.cout = cout;
	this.etat = etat;
    }
    
    @Override
    public int compareTo(Sommet autre) {
	// TODO Auto-generated method stub
	return Double.compare(this.cout, autre.cout);
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public double getCout() {
        return cout;
    }
    
    public Etat getEtat() {
        return etat;
    }
    
    public void setEtat(Etat nouvelEtat) {
	this.etat = nouvelEtat;
    }
    
    public void setCout(double nouveauCout) {
	this.cout = nouveauCout;
    }
    

}
