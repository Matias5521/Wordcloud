package de.hs_mannheim.informatik.pr2.wordcloud.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hs_mannheim.informatik.pr2.wordcloud.model.Verzeichnis;
import de.hs_mannheim.informatik.pr2.wordcloud.view.Anzeige;

public class Wordcloud {
	
	private static final Logger logger = LogManager.getLogger(Main.class);
	private Anzeige ui;

	private ArrayList<Verzeichnis> files = new ArrayList<>();

	public Wordcloud() {

		ui = new Anzeige(this);

	}

	public void erstelleWordcloud(String files, String sprache, int maxWoerter, int frequenz, String konvertierung,
			String stopwords, boolean alphabetisch, boolean stemming) {

		try {

			String[] f = files.split(", ");

			for (String fi : f) {
				
				//logger.info(fi);
				
				Verzeichnis fil = new Verzeichnis(fi);
				
				this.files.add(fil);
				
				String[] swords = stopwords.split(", ");
				fil.setStopwords(new ArrayList<String>(Arrays.asList(swords)));
				fil.setMaxWoerter(maxWoerter);
				fil.setFrequenz(frequenz);
				fil.setLocation(fi);
				fil.setSprache(sprache);
				fil.setKonvertierung(konvertierung);
				fil.setSortierung(alphabetisch);
				fil.setStemming(stemming);
				
				fil.extrahiereWorte();
				
				logger.info(fil.toString());
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
