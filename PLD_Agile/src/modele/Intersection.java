package modele;

import java.awt.Point;

/**
 * Cette classe permet la creation d'intersections representant les points
 * remarquables sur le plan fourni.
 *
 */
public class Intersection extends ObjetGraphique {
	private int id;
	private int longitude;
	private int latitude;

	/**
	 * Cree une Intersection de coordonnees (longitude, latitude)
	 * 
	 * @param id
	 *            L'identifiant de l'Intersection
	 * @param longitude
	 *            Longitude de l'Intersection
	 * @param latitude
	 *            Latitude de l'Intersection
	 */
	public Intersection(int id, int longitude, int latitude) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	/**
	 * Indique si l'Intersection contient le point p +/- la tolerance
	 * 
	 * @param p
	 *            Le point de coordonnées recherchées
	 * @param tolerance
	 *            L'intervalle de tolérance des coordonnées
	 * @return True si le point est assez proche de l'Intersection, false sinon
	 */
	@Override
	protected boolean contient(Point p, int tolerance) {
		int x = p.x;
		int y = p.y;
		if (!(x - tolerance <= longitude && longitude < x + tolerance))
			return false;
		if (!(y - tolerance <= latitude && latitude <= y + tolerance))
			return false;
		return true;
	}

	/**
	 * @return ID de l'Intersection
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return Longitude de l'Intersection
	 */
	public int getLongitude() {
		return this.longitude;
	}

	/**
	 * @return Latitude de l'Intersection
	 */
	public int getLatitude() {
		return this.latitude;
	}

	/**
	 * Compare deux Intersections. Renvoit true si les objets manipules
	 * possedent le meme identifiant et les memes coordonnees, et false sinon.
	 */
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
