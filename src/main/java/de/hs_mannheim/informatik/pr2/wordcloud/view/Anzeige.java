package de.hs_mannheim.informatik.pr2.wordcloud.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hs_mannheim.informatik.pr2.wordcloud.controller.Main;
import de.hs_mannheim.informatik.pr2.wordcloud.controller.Wordcloud;

public class Anzeige extends JFrame {
	
	private static final Logger logger = LogManager.getLogger(Main.class);

	private JPanel jp1, jp2, jp3, jp4, jp5, jp6, jp7, jp8, jp9;
	private JTextField jtf1, jtf2, jtf3, jtf4;
	private JComboBox<String> jcb1, jcb2;
	private JCheckBox jb1, jb2;
	private JButton jbt1;
	
	private Wordcloud wc;

	public Anzeige(Wordcloud wordcld) {
		// TODO Auto-generated constructor stub
		
		this.wc = wordcld;

		this.setTitle("Wordcloud");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setLayout(new GridLayout(9, 1));

		// File importieren
		jp1 = new JPanel();
		jp1.setLayout(new BoxLayout(jp1, BoxLayout.X_AXIS));
		jp1.add(new JLabel("Filepfad mit Dateiname und Endung: "));
		jp1.add(Box.createHorizontalGlue());
		jtf1 = new JTextField("C:\\Users\\matia\\git\\Wordcloud\\src\\main\\resources\\wc.pptx", 24);
		jtf1.setMaximumSize(new Dimension(100, 24));
		jp1.add(jtf1);

		this.add(jp1);

		// zu filternde Sprache auswählen
		jp2 = new JPanel();
		jp2.setLayout(new BoxLayout(jp2, BoxLayout.X_AXIS));
		jp2.add(new JLabel("<html>Sprache nach der gefiltert werden soll:<br>(Nur '.txt', '.pdf' oder '.docx' Files möglich.)</html>"));
		jp2.add(Box.createHorizontalGlue());
		String[] moeglichkeiten = { "Englisch", "Deutsch" };
		jcb1 = new JComboBox<String>(moeglichkeiten);
		jcb1.setMaximumSize(new Dimension(100, 24));
		jp2.add(jcb1);
		this.add(jp2);

		// maximale Anzahl an Wörtern in der Cloud setzen
		jp3 = new JPanel();
		jp3.setLayout(new BoxLayout(jp3, BoxLayout.X_AXIS));
		jp3.add(new JLabel("Maximale Anzahl an Wörtern, die gefiltert werden sollen:"));
		jp3.add(Box.createHorizontalGlue());
		jtf2 = new JTextField("0", 6);
		jtf2.setMaximumSize(new Dimension(100, 24));
		jp3.add(jtf2);

		this.add(jp3);

		// Mindestmaß an Vorkommen eines Wortes
		jp4 = new JPanel();
		jp4.setLayout(new BoxLayout(jp4, BoxLayout.X_AXIS));
		jp4.add(new JLabel("Minimale Frequenz eines Wortes:"));
		jp4.add(Box.createHorizontalGlue());
		jtf3 = new JTextField("0", 6);
		jtf3.setMaximumSize(new Dimension(100, 24));
		jp4.add(jtf3);

		this.add(jp4);

		// Zu lowercase oder uppercase konvertieren
		jp5 = new JPanel();
		jp5.setLayout(new BoxLayout(jp5, BoxLayout.X_AXIS));
		jp5.add(new JLabel("Konvertierung des Textes: "));
		jp5.add(Box.createHorizontalGlue());
		String[] moeglichkeiten2 = { "Upper", "Lower"};
		jcb2 = new JComboBox<String>(moeglichkeiten2);
		jcb2.setMaximumSize(new Dimension(100, 24));
		jp5.add(jcb2);
		this.add(jp5);

		// Stopwords setzen
		jp6 = new JPanel();
		jp6.setLayout(new BoxLayout(jp6, BoxLayout.X_AXIS));
		jp6.add(new JLabel("Wörter die vermieden werden sollen:"));
		jp6.add(Box.createHorizontalGlue());
		jtf4 = new JTextField("Wort1, Wort2, Wort3, ...", 24);
		jtf4.setMaximumSize(new Dimension(100, 24));
		jp6.add(jtf4);

		this.add(jp6);

		// aphabetische Sortierung
		jp7 = new JPanel();
		jp7.setLayout(new BoxLayout(jp7, BoxLayout.X_AXIS));
		jp7.add(new JLabel("Alphabetische Sortierung:"));
		jp7.add(Box.createHorizontalGlue());getWarningString();
		jb1 = new JCheckBox();
		jb1.setMaximumSize(new Dimension(100, 24));
		jp7.add(jb1);
		
		this.add(jp7);
		
		// Stemming erlauben
		jp8 = new JPanel();
		jp8.setLayout(new BoxLayout(jp8, BoxLayout.X_AXIS));
		jp8.add(new JLabel("Stemming erlauben"));
		jp8.add(Box.createHorizontalGlue());getWarningString();
		jb2 = new JCheckBox();
		jb2.setMaximumSize(new Dimension(100, 24));
		jp8.add(jb2);

		this.add(jp8);

		// Eingabe absenden
		jp9 = new JPanel();
		jbt1 = new JButton("Eingabe bestätigen");
		jbt1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				logger.info(jtf1.getText()+" "+String.valueOf(jcb1.getSelectedItem())+" "+Integer.parseInt(jtf2.getText())+" "+Integer.parseInt(jtf3.getText())+" "+String.valueOf(jcb2.getSelectedItem())+" "+jtf4.getText()+" "+jb1.isSelected()+" "+jb2.isSelected());
				wc.erstelleWordcloud(jtf1.getText(), String.valueOf(jcb1.getSelectedItem()), Integer.parseInt(jtf2.getText()), Integer.parseInt(jtf3.getText()) , String.valueOf(jcb2.getSelectedItem()), jtf4.getText().toLowerCase(), jb1.isSelected(), jb2.isSelected());
			}
			
		});
		
		jp9.add(jbt1);
		this.add(jp9);

		this.setVisible(true);
	}

}
