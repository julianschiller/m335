package com.baloise.library.common;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Bean Klasse fuer Ausleihe
 *
 * @author Matthias
 * @author Julian Schiller
 */
public final class Ausleihe implements Serializable {

	private static final long serialVersionUID = 2425672164760554627L;

	private long id;
	private Date leihdatum;
	private Short leihdauer;
	private Kunde kunde;
	private Medium medium;

	/**
	 * C'tor zum Erzeugen einer neuen Ausleihe mit den Pflichtfeldern gesetzt.
	 *
	 * @param kundenNummer
	 * @param inventarNummer
	 */
	public Ausleihe(long kundenNummer, long inventarNummer) {
		this.kunde = new Kunde(kundenNummer);
		this.medium = new Medium(inventarNummer);
	}

	public void setId(long id) { this.id = id; }
	public long getId() { return id; }

	public void setLeihdatum(Date leihdatum) { this.leihdatum = leihdatum; }

	/**
	 * Gibt das Leihdatum als lokale Zeichenkette aus.
	 *
	 * @return das Leihdatum
	 */
	public String getLeihdatumLokalisiert() {
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
		format.setTimeZone(TimeZone.getDefault());
		return format.format(leihdatum);
	}

	public Short getLeihdauer() { return leihdauer; }

	/**
	 * Setzt die Leihdauer in Tagen. Wenn nicht gesetzt, wird beim Persistieren 14
	 * Tage eingesetzt.
	 *
	 * @param leihdauer
	 */
	public void setLeihdauer(Short leihdauer) {
		this.leihdauer = leihdauer;
	}

	/**
	 * Gibt das Faelligkeitsdatum als lokale Zeichenkette aus. Diese
	 * Eigenschaft ist transient und wird bei jedem Aufruf neu berechnet.
	 *
	 * @return das Faelligkeitsdatum
	 */
	public String getFaelligkeitsdatumLokalisiert() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(leihdatum);
		if (leihdauer != null) cal.add(Calendar.DAY_OF_YEAR, leihdauer);
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
		format.setTimeZone(TimeZone.getDefault());
		return format.format(cal.getTime());
	}

	public Kunde getKunde() { return kunde; }
	public Medium getMedium() { return medium; }

	/**
	 * Gibt die Ausleihe als Zeichenkette aus. {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Ausleihe [id=" + id + ", leihdatum=" + getLeihdatumLokalisiert() + ", faellig="
				+ getFaelligkeitsdatumLokalisiert() + ", kunde=" + kunde + ", medium=" + medium + "]";
	}
}
