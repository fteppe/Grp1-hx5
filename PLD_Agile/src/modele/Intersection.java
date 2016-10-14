package modele;

import java.util.Observable;

public class Intersection extends Observable {
    private int id;
    private int longitude;
    private int latitude;
    
    public Intersection(int id, int longitude, int latitude){
	this.id = id;
	this.longitude = longitude;
	this.latitude = latitude;
    }
    
    public int getId(){
	return this.id;
    }
    
    public int getLongitude(){
	return this.longitude;
    }
    
    public int getLatitude(){
	return this.latitude;
    }
}
