package de.hs_mannheim.informatik.mvn.model;

import java.util.ArrayList;

import de.hs_mannheim.informatik.mvn.view.Anzeige;

public class Wordcloud {
	
	private Anzeige ui;
	
	private ArrayList<File> files = new ArrayList<>();
	
	public Wordcloud() {
		
		ui = new Anzeige(this);
		
	}

}
