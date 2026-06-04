package com.baloise.library.common;

import java.io.Serializable;

/**
 * Bean Klasse fuer Addresse (Kundenadresse)
 * 
 * @author Matthias
 */
public final class Adresse implements Serializable {

	private static final long serialVersionUID = 2375134275131779804L;
	
	private long id;
	private String strasse;
	private String ort;
	private String plz;

	/**
	 * C'tor zum Erstellen einer neuen Adresse mit den Pflichtfeldern
	 * 
	 * @param strasse
	 * @param ort
	 */
	public Adresse(String strasse, String ort) {
		this.strasse = strasse;
		this.ort = ort;
	}
	
	/**
	 * C'tor zum Erstellen einer neuen Adresse
	 * 
	 * @param strasse
	 * @param ort
	 * @param plz
	 */
	public Adresse(String strasse, String ort, String plz) {
		this.strasse = strasse;
		this.ort = ort;
		this.plz = plz;
	}
	
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public String getStrasse() { return strasse; }

	public String getOrt() { return ort; }

	public String getPlz() { return plz; }
	public void setPlz(String plz) { this.plz = plz; }
}
