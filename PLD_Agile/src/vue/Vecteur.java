package vue;

public class Vecteur {
	/* une classe Vecteur, qui permet de réaliser les opérations basiques d'un vecteur 2D
	 * 
	 */
	protected double x;
	protected double y;
	
	public Vecteur(double x,double y){
		this.x=x;
		this.y=y;
	}
	
	/*
	 * constructeur de copie
	 * 
	 * @param v le vecteur à copier
	 */
	public Vecteur(Vecteur v){
		this.x= v.x;
		this.y = v.y;
	}
	
	/*Addition de deux vecteurs
	 * 
	 * @param a ajoute a this le vecteur a et renvoie le résultat
	 */
	public Vecteur add(Vecteur a){
		return new Vecteur(this.x + a.x, this.y + a.y);
	}
	
	/*operation de muliplication du vecteur par un réel
	 * 
	 * @param e le reel qui est multiplié au vecteur
	 */
	public Vecteur multiply(double e){
		return new Vecteur((this.x * e),( this.y * e));
	}
	
	/*renvoie la norme du vecteur
	 * 
	 */
	public double norme(){
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	/*Permet une représentation sous forme de tete du vecteur
	 * 
	 */
	@Override
	public String toString(){
		return "{\n"+"x :"+this.x+"\ny :"+this.y+"\n}\n";
	}
}
