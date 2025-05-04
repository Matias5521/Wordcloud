package de.hs_mannheim.informatik.mvn.model;

import java.util.HashMap;

public class Wordcloud {
	
	private HashMap<Word, Integer> woerter = new HashMap<>();
	
	public Wordcloud() {
		
	}

	public HashMap<Word, Integer> getWoerter() {
		return woerter;
	}

	public void setWoerter(HashMap<Word, Integer> woerter) {
		this.woerter = woerter;
	}
	
	

}
