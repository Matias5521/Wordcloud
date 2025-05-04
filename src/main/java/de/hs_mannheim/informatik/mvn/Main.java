package de.hs_mannheim.informatik.mvn;

import java.io.File;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Main {

	public static void main(String[] args) {
		
		String pdfFilePath = "src/main/resources/wc.pdf";
		File pdfFile = new File(pdfFilePath);
		try (PDDocument document = Loader.loadPDF(pdfFile)) {
		PDFTextStripper pdfStripper = new PDFTextStripper();
		String text = pdfStripper.getText(document);
		System.out.println(text);
		} catch (Exception e) {
		e.printStackTrace();
		}
		
	}
	
	

}
