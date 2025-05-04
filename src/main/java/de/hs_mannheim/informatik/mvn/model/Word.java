package de.hs_mannheim.informatik.mvn.model;

public class Word {
	
	private String wort;
	private int anzahl;
	
	public Word(String wort) {
		this.wort = wort;
	}
	
	public String toString() {
		return wort+" kommt "+anzahl+" mal vor.";
	}

	public String getWort() {
		return wort;
	}

	public void setWort(String wort) {
		this.wort = wort;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}
	
	

}
