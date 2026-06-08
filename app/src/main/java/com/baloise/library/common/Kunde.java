package com.baloise.library.common;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Bean Klasse fuer Kunde
 * 
 * @author Matthias
 * @author Julian Schiller
 */
public final class Kunde implements Serializable {

	private static final long serialVersionUID = 2177739324279157094L;
	
	private long id;
	private String vorname;
	private String familienname;
	private Date geburtsdatum;
	private Adresse adresse;
	private String email;

	/**
	 * C'tor zum Erstellen eines neuen Kunden nur mit seiner Kundennummer gesetzt.
	 * 
	 * @param id
	 */
	Kunde(long id) {
		this.id = id;
	}

	/**
	 * C'tor zum Erstellen eines neuen Kunden mit den Pflichtfeldern gesetzt.
	 * 
	 * @param vorname
	 * @param familienname
	 * @param geburtsdatum
	 * @param adresse
	 * @param email
	 */
	public Kunde(String vorname, String familienname, Date geburtsdatum, Adresse adresse, String email) {
		this.vorname = vorname;
		this.familienname = familienname;
		this.geburtsdatum = geburtsdatum;
		this.adresse = adresse;
		this.email = email;
	}
	
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public String getVorname() { return vorname; }

	public String getFamilienname() { return familienname; }
	public void setFamilienname(String familienname) { this.familienname = familienname; }

	/**
	 * Gibt das Geburtsdatum als lokale Zeichenkette aus.
	 * 
	 * @return das Faelligkeitsdatum
	 */
	public String getGeburtsdatumLokalisiert() {
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
		format.setTimeZone(TimeZone.getDefault());
		return format.format(geburtsdatum);
	}

	public Adresse getAdresse() { return adresse; }
	public void setAdresse(Adresse adresse) { this.adresse = adresse; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	/**
	 * Gibt den Kunden als Zeichenkette aus. {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Kunde [id=" + id + ", name=" + vorname + " " + familienname + ", geburtsdatum="
				+ getGeburtsdatumLokalisiert() + "]";
	}
}
