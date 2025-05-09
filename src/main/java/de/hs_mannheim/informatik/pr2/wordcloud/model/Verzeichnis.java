package de.hs_mannheim.informatik.pr2.wordcloud.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
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
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

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

		} else if (endung[1].equals("txt")) {

			extrahiereWorteAusTxt();

		} else if (endung[1].equals("docx")) {

			extrahiereWorteAusDocx();

		} else if (endung[1].equals("pptx")) {

			extrahiereWorteAusPptx();
		}

		schreibeWorteInCloud();
	}

	private void schreibeWorteInCloud() {

		// Alle Worte rausfiltern, die die minimal Frequenz nicht erfuellen
		woerter.entrySet().removeIf(e -> e.getValue() < frequenz);

		// So viele Worte löschen, bis maximale Anzahl an Worten erlaubt
		Random r = new Random();
		List<String> keys = new ArrayList<>(woerter.keySet());
		while (woerter.size() > maxWoerter) {
			int randIndex = r.nextInt(keys.size());
			String key = keys.get(randIndex);
			woerter.remove(key);
			keys.remove(randIndex);
		}

		// Alphabetische Sortierung beachten
		TreeMap<String, Integer> woerterAlphabetisch = new TreeMap<>();

		if (sortierung) {
			for (String wort : woerter.keySet()) {
				woerterAlphabetisch.put(wort, woerter.get(wort));
			}

			befuelleHtml(woerterAlphabetisch);
			return;
		}

		befuelleHtml(woerter);

	}

	private void befuelleHtml(Map<String, Integer> worte) {

		String html1 = """
			<html>
				<head>
				    <title>PR2 Wordcloud</title>
				</head>

				<body>

				    <style type="text/css">
				        #htmltagcloud{

				                font-size: 100%;
				                width: auto;
				                font-family:'lucida grande','trebuchet ms',arial,helvetica,sans-serif;
				                background-color:#fff;
				                margin:1em 1em 0 1em;
				                border:2px dotted #ddd;
				                padding:2em;

				        }
				        #htmltagcloud{line-height:2.4em;word-spacing:normal;letter-spacing:normal;text-transform:none;text-align:justify;text-indent:0}
				        #htmltagcloud a:link{text-decoration:none}
				        #htmltagcloud a:visited{text-decoration:none}
				        #htmltagcloud a:hover{color:white;background-color:#05f}
				        #htmltagcloud a:active{color:white;background-color:#03d}
				        .wrd{padding:0;position:relative}
				        .wrd a{text-decoration:none}
				        .tagcloud0{font-size:1.0em;color:#ACC1F3;z-index:10}.tagcloud0 a{color:#ACC1F3}
				        .tagcloud1{font-size:1.4em;color:#ACC1F3;z-index:9}.tagcloud1 a{color:#ACC1F3}
				        .tagcloud2{font-size:1.8em;color:#86A0DC;z-index:7}.tagcloud2 a{color:#86A0DC}
				        .tagcloud3{font-size:2.2em;color:#86A0DC;z-index:7;padding:5px}.tagcloud3 a{color:#86A0DC}
				        .tagcloud4{font-size:2.6em;color:#607EC5;z-index:6}.tagcloud4 a{color:#607EC5}
				        .tagcloud5{font-size:3.0em;color:#607EC5;z-index:5}.tagcloud5 a{color:#607EC5}
				        .tagcloud6{font-size:3.3em;color:#4C6DB9;z-index:4}.tagcloud6 a{color:#4C6DB9}
				        .tagcloud7{font-size:3.6em;color:#395CAE;z-index:3}.tagcloud7 a{color:#395CAE}
				        .tagcloud8{font-size:3.9em;color:#264CA2;z-index:2}.tagcloud8 a{color:#264CA2}
				        .tagcloud9{font-size:4.2em;color:#133B97;z-index:1}.tagcloud9 a{color:#133B97}
				        .tagcloud10{font-size:4.5em;color:#002A8B;z-index:0}.tagcloud10 a{color:#002A8B}
				        .freq{font-size:10pt !important;color:#bbb}
				        #credit{text-align:center;color:#333;margin-bottom:0.6em;font:0.7em 'lucida grande',trebuchet,'trebuchet ms',verdana,arial,helvetica,sans-serif}
				        #credit a:link{color:#777;text-decoration:none}
				        #credit a:visited{color:#777;text-decoration:none}
				        #credit a:hover{color:white;background-color:#05f}
				        #credit a:active{text-decoration:underline}
				    </style>

				    <div id="htmltagcloud">

							""";

		int id = 0;
		int lvl = 1;

		for (String wort : worte.keySet()) {
			lvl = ermittleGroesse(worte, wort);
			html1 += "          <span id=\"" + id + "\" class=\"wrd tagcloud" + lvl + "\">" + "<a href=\"\">" + wort
					+ "</a></span>\n";

			id++;
		}

		html1 += """

						</div>

					</body>

				</html>\n""";

		// ins File schreiben
		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\matia\\git\\Wordcloud\\src\\main\\resources\\anzeige.html"));

			logger.info(html1);
			writer.write(html1);

		} catch (IOException e) {
			logger.error("Fehler beim Beschreiben des Html Files.");
			e.printStackTrace();
		}

	}

	private int ermittleGroesse(Map<String, Integer> worte, String wort) {

		int max = Collections.max(worte.values());
		int r = (int) Math.round((double) (worte.get(wort) - 1) * max / (max - 1));
		// logger.info(r);
		return r;
	}

	private void extrahiereWorteAusPptx() {
		try (FileInputStream fis = new FileInputStream(location); XMLSlideShow ppt = new XMLSlideShow(fis)) {

			StringBuilder text = new StringBuilder();

			for (XSLFSlide slide : ppt.getSlides()) {
				for (XSLFShape shape : slide.getShapes()) {
					if (shape instanceof XSLFTextShape textShape) {
						text.append(textShape.getText()).append(" ");
					}
				}
			}

			bearbeiteText(text.toString());

		} catch (IOException e) {
			logger.error("Fehler beim Lesen der PPTX-Datei", e);
		}
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

		try {
			try (FileInputStream fis = new FileInputStream(location);
					XWPFDocument doc = new XWPFDocument(fis);
					XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {

				String text = extractor.getText();
				bearbeiteText(text);

			} catch (IOException e) {
				logger.error("Fehler beim Lesen der DOCX-Datei", e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void extrahiereWorteAusPdf() {

		String text;
		File pdfFile = new File(location);

		try (PDDocument document = Loader.loadPDF(pdfFile)) {

			PDFTextStripper pdfStripper = new PDFTextStripper();
			text = pdfStripper.getText(document);

			bearbeiteText(text);

		} catch (Exception e) {
			logger.error("Fehler beim Einlesen aus der Datei.");
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
					logger.error("Fehler beim Bearbeiten des Textes.");
				}
			} else {
				try (Analyzer analyzer = createGermanAnalyzerOhneStemming(stopSet)) {

					analysiereText(analyzer, text);

				} catch (IOException e) {
					e.printStackTrace();
					logger.error("Fehler beim Bearbeiten des Textes.");
				}
			}

		} else { // dann englisch
			if (stemming) {
				try (Analyzer analyzer = new EnglishAnalyzer(stopSet)) {

					analysiereText(analyzer, text);

				} catch (IOException e) {
					e.printStackTrace();
					logger.error("Fehler beim Bearbeiten des Textes.");
				}
			} else {
				try (Analyzer analyzer = createEnglishAnalyzerOhneStemming(stopSet)) {

					analysiereText(analyzer, text);

				} catch (IOException e) {
					e.printStackTrace();
					logger.error("Fehler beim Bearbeiten des Textes.");
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

			if (konvertierung.equals("Upper")) {
				woerter.put(wort.toUpperCase(), woerter.getOrDefault(wort.toUpperCase(), 0) + 1);
			} else if (konvertierung.equals("Lower")) {
				woerter.put(wort.toLowerCase(), woerter.getOrDefault(wort.toLowerCase(), 0) + 1);
			} else {
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
