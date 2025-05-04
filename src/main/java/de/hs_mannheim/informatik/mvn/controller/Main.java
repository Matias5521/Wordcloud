package de.hs_mannheim.informatik.mvn.controller;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import de.hs_mannheim.informatik.mvn.view.Anzeige;

public class Main {

	public static void main(String[] args) {
		
		Anzeige ui = new Anzeige();

//		// Beispiel PDFBox
//		String pdfFilePath = "src/main/resources/wc.pdf";
//		File pdfFile = new File(pdfFilePath);
//		try (PDDocument document = Loader.loadPDF(pdfFile)) {
//			PDFTextStripper pdfStripper = new PDFTextStripper();
//			String text = pdfStripper.getText(document);
//			System.out.println(text);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		// Beispiel Lucene
//		String text = "Der schnelle braune Fuchs springt über den lahmen Hund.";
//
//		try (GermanAnalyzer analyzer = new GermanAnalyzer()) {
//			TokenStream tokenStream = analyzer.tokenStream(null, text);
//			CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
//			tokenStream.reset();
//			while (tokenStream.incrementToken()) {
//				System.out.println(termAttribute.toString());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

}
