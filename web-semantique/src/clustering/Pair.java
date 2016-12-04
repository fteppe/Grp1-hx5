package clustering;

import java.security.MessageDigest;

public class Pair {
	private  String x;
	private  String y;
	private MessageDigest digest;
	
	public Pair(String x, String y){
		this.x = x;
		this.y = y;
	}
	
	private String getX(){
		return x;
	}
	
	private String getY(){
		return y;
	}
	
	@Override
	public int hashCode(){
		return x.hashCode() ^ y.hashCode();
	}
	
	/**
	 * cette pair est volontairement inversible. L'orde des valeurs n'a pas d'importance.
	 */
	@Override
	public boolean equals(Object o){
		if (!(o instanceof Pair)) return false;
		Pair pair = (Pair) o;
		if((y == pair.getY() && pair.getX() == x) ||(y == pair.getX() && x == pair.getY()))
		{
			return true;
		}
		return false;
	}
}
