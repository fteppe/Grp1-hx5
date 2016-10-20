package vue;

public class Vecteur {
	protected double x;
	protected double y;
	
	public Vecteur(double x,double y){
		this.x=x;
		this.y=y;
	}
	
	public Vecteur(Vecteur v){
		this.x= v.x;
		this.y = v.y;
	}
	
	public Vecteur add(Vecteur a){
		return new Vecteur(this.x + a.x, this.y + a.y);
	}
	
	public Vecteur multiply(double e){
		return new Vecteur((this.x * e),( this.y * e));
	}
	
	public double norme(){
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	@Override
	public String toString(){
		return "{\n"+"x :"+this.x+"\ny :"+this.y+"\n}\n";
	}
}
