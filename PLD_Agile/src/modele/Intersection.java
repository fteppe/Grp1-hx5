package modele;

public class Intersection {
    private int id;
    private int longitude;
    private int latitude;
    
    /**
     * Cree une intersection de coordonnees (longitude, latitude)
     * @param id L'identifiant de l'intersection
     * @param longitude Longitude de l'intersection
     * @param latitude Latitude de l'intersection
     */
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Intersection other = (Intersection) obj;
		if (id != other.id)
			return false;
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
			return false;
		return true;
	}
    
    
}
