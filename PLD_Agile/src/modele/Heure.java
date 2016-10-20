package modele;

public class Heure {

	private int heure;
	private int minutes;
	private int secondes;
	
	/**
	 * Constructeur par d�faut.
	 * Cr�e une heure 00:00:00 
	 */
	public Heure() {
		this.heure = 0;
		this.minutes = 0;
		this.secondes = 0;
	}
	
	/**
	 * Constructeur de copie
	 * @param h heure a copier
	 */
	public Heure(Heure h) {
		this.heure = h.heure;
		this.minutes = h.minutes;
		this.secondes = h.secondes;
	}
	/**
	 * Cr�e une heure � partir d'un temps en secondes
	 * heure comprise entre 00:00:00 et 23:59:59
	 * @param secondes
	 */
	public Heure(int secondes) {
		int reste = secondes;
		this.secondes = reste%60;
		reste-= this.secondes;
		reste = reste/60;
		this.minutes = reste%60;
		reste -= this.minutes;
		reste = reste/60;
		reste = reste%24;
		this.heure = reste;
	}
	/**
	 * Cr�e une heure � partir d'une heure format�e
	 * @param heureFormatee heure au format "hh:mm:ss"
	 */
	public Heure(String heureFormatee) throws IndexOutOfBoundsException {
		String[] splits = heureFormatee.split(":");
		this.heure = Integer.parseInt(splits[0]);
		this.minutes = Integer.parseInt(splits[1]);
		this.secondes = Integer.parseInt(splits[2]);
	}
	
	/**
	 * Ajoute une heure � l'heure actuelle
	 * @param h une heure � ajouter
	 * @return cette heure modifi�e
	 */
	public Heure ajouterHeure(Heure h) {
		this.secondes += h.secondes;
		if(secondes >= 60 ){
			this.secondes -= 60;
			this.minutes ++;
		}
		this.minutes += h.minutes;
		if(minutes >= 60 ){
			this.minutes -= 60;
			this.heure ++;
		}
		this.heure += h.heure;
		this.heure = this.heure%24;
		return this;
	}
	
	/**
	 * Ajoutes des secondes � l'heure actuelle
	 * @param secondes les secondes � ajouter
	 * @return cette heure modifi�e
	 */
	public Heure ajouterSecondes(int secondes) {
		this.ajouterHeure(new Heure(secondes));
		return this;
	}
	
	public String toString() {
		return (this.heure >= 10 ? "" : "0") + this.heure 
			+ ":" + (this.minutes >= 10 ? "" : "0") 
			+ this.minutes+":" + (this.secondes >= 10?"" : "0") 
			+ this.secondes;
	}
}
