package de.hs_mannheim.informatik.pr2.wordcloud.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import de.hs_mannheim.informatik.pr2.wordcloud.controller.Main;

public class Verzeichnis {

	private static final Logger logger = LogManager.getLogger(Main.class);

	private HashMap<String, Integer> woerter = new HashMap<>();
	private String location, konvertierung, sprache;
	private ArrayList<String> stopwords;
	private int maxWoerter, frequenz;
	private boolean sortierung, stemming;

	public Verzeichnis(String location) {
		this.location = location;
	}

	public String toString() {
		String s = "Location: " + location + ", Konvertierung: " + konvertierung + ", Sprache: " + sprache
				+ ", Maximale Woerter: " + maxWoerter + ", Frequenz: " + frequenz + ", Sortierung Alphabetisch: "
				+ sortierung + ", Stemming: " + stemming;
		s += ", Stopwoerter\n";
		for (String w : stopwords) {
			s += w + ", ";
		}
		s += "\nWoerter:\n";
		for (String d : woerter.keySet()) {
			s += "'" + d.toString() + "' kommt " + woerter.get(d) + " mal vor,\n";
		}

		return s + "\nGesamte Worte: " + woerter.size();
	}

	public void extrahiereWorte() {

		String[] endung = location.split("\\.");

		if (endung[1].equalsIgnoreCase("pdf")) {

			extrahiereWorteAusPdf();

		} else if (endung[1].equals("docx")) {

			extrahiereWorteAusDocx();

		} else if (endung[1].equals("txt")) {

			extrahiereWorteAusTxt();

		} else if (endung[1].equals("pptx")) {

			extrahiereWorteAusPptx();
		}
	}

	private void extrahiereWorteAusPptx() {

	}

	private void extrahiereWorteAusTxt() {

		try {
			Scanner s = new Scanner(new File(location));
			String text = "";

			while (s.hasNext()) {

				text = text + " " + s.next();
			}

			bearbeiteText(text);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void extrahiereWorteAusDocx() {

	}

	private void extrahiereWorteAusPdf() {

		String text;
		File pdfFile = new File(location);

		try (PDDocument document = Loader.loadPDF(pdfFile)) {

			PDFTextStripper pdfStripper = new PDFTextStripper();
			text = pdfStripper.getText(document);

			bearbeiteText(text);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void bearbeiteText(String text) {

		// Stopwords uebergabebereit machen
		CharArraySet stopSet;

		// zuzsaetzlicher Filter...
		for (int i = 0; i < 100; i++) {
			stopwords.add(String.valueOf(i));
		}

		stopSet = new CharArraySet(stopwords, false);

		if (sprache.equalsIgnoreCase("deutsch")) {

			if (stemming) {
				try (Analyzer analyzer = new GermanAnalyzer(stopSet)) {

					analysiereText(analyzer, text);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try (Analyzer analyzer = createGermanAnalyzerOhneStemming(stopSet)) {

					analysiereText(analyzer, text);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else { // dann englisch
			if (stemming) {
				try (Analyzer analyzer = new EnglishAnalyzer(stopSet)) {

					analysiereText(analyzer, text);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try (Analyzer analyzer = createEnglishAnalyzerOhneStemming(stopSet)) {

					analysiereText(analyzer, text);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private Analyzer createGermanAnalyzerOhneStemming(CharArraySet stopSet) {
		return new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String fieldName) {
				final var source = new StandardTokenizer();
				TokenStream result = new LowerCaseFilter(source);
				result = new StopFilter(result, stopSet);
				return new TokenStreamComponents(source, result);
			}
		};
	}

	private Analyzer createEnglishAnalyzerOhneStemming(CharArraySet stopSet) {
		return new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String fieldName) {
				final var source = new StandardTokenizer();
				TokenStream result = new LowerCaseFilter(source);
				result = new StopFilter(result, stopSet);
				return new TokenStreamComponents(source, result);
			}
		};
	}

	private void analysiereText(Analyzer analyzer, String text) throws IOException {

		TokenStream tokenStream = analyzer.tokenStream(null, text);
		CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		tokenStream.reset();

		List<String> tokens = new ArrayList<>();

		while (tokenStream.incrementToken()) {

			String wort = termAttribute.toString();

			if (wort.length() >= 2) {
				tokens.add(wort);
			}

		}

		// weitere Filterungen durchnehmen
		List<String> tokensNeu = tokens.stream().flatMap(s -> Arrays.stream(s.split("[ ,?!&]+")))
				.collect(Collectors.toList());

		for (String wort : tokensNeu) {

			if(konvertierung.equals("Upper")) {
				woerter.put(wort.toUpperCase(), woerter.getOrDefault(wort.toUpperCase(), 0) + 1);
			}else if(konvertierung.equals("Lower")) {
				woerter.put(wort.toLowerCase(), woerter.getOrDefault(wort.toLowerCase(), 0) + 1);
			}else {
				woerter.put(wort, woerter.getOrDefault(wort, 0) + 1);
			}
		}

	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setKonvertierung(String konvertierung) {
		logger.info("Konvertierung: " + konvertierung);
		this.konvertierung = konvertierung;
	}

	public void setMaxWoerter(int maxWoerter) {
		this.maxWoerter = maxWoerter;
	}

	public void setFrequenz(int frequenz) {
		this.frequenz = frequenz;
	}

	public void setSprache(String sprache) {
		this.sprache = sprache;
	}

	public void setSortierung(boolean sortierung) {
		this.sortierung = sortierung;
	}

	public void setStopwords(ArrayList<String> stopwords) {
		this.stopwords = stopwords;
	}

	public void setStemming(boolean stemming) {
		this.stemming = stemming;
	}

}
