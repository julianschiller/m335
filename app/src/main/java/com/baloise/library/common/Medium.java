package com.baloise.library.common;

import java.io.Serializable;

/**
 * Bean Klasse fuer Medium
 * 
 * @author Matthias
 * @author Julian Schiller
 */
public final class Medium implements Serializable {

	private static final long serialVersionUID = 1881561894623524513L;
	
	private long id;
	private String titel;
	private String autor;
	private String genre;
	private Short fsk;
	private Long ean;
	private String standort;

	/**
	 * C'tor zum Erstellen eines neuen Mediums nur mit seiner Inventarnummer gesetzt.
	 * 
	 * @param id
	 */
	Medium(long id) {
		this.id = id;
	}
	
	/**
	 * C'tor zum Erstellen eines neuen Mediums mit den Pflichtfeldern gesetzt.
	 * 
	 * @param titel
	 * @param autor
	 */
	public Medium(String titel, String autor) {
		this.titel = titel;
		this.autor = autor;
	}

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	
	public String getTitel() { return titel; }
	
	public String getAutor() { return autor; }

	public String getGenre() { return genre; }
	public void setGenre(String genre) { this.genre = genre; }

	public Short getFsk() { return fsk; }
	public void setFsk(Short fsk) { this.fsk = fsk; }

	public Long getEan() { return ean; }
	public void setEan(Long ean) { this.ean = ean; }

	public String getStandort() { return standort; }
	public void setStandort(String standort) { this.standort = standort; }

	/**
	 * Gibt das Medium als Zeichenkette aus. {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Medium [id=" + id + ", titel=" + titel + ", autor=" + autor + ", genre=" + genre + "]";
	}
}
