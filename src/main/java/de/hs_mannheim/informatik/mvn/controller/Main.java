package de.hs_mannheim.informatik.mvn.controller;

import de.hs_mannheim.informatik.mvn.model.Wordcloud;

public class Main {

	public static void main(String[] args) {
		
		Wordcloud wc = new Wordcloud();

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
