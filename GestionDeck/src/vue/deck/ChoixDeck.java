package vue.deck;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import single.Singleton;
import business.Carte;
import business.Deck;
import javax.swing.JScrollPane;

public class ChoixDeck extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	final JList<String> list;
	JLabel lblImgCartePrinc;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChoixDeck frame = new ChoixDeck();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChoixDeck() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 711, 470);
		setTitle("Accueil");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		DefaultListModel<String> listeDeck = new DefaultListModel<String>();

		lblImgCartePrinc = new JLabel();
		lblImgCartePrinc.setBounds(312, 94, 223, 310);
		contentPane.add(lblImgCartePrinc);

		String dossDecks = Singleton.getInstance().getProp().getProperty("ressources.xml.decks");
		String dossierDeckExport = Singleton.getInstance().getProp().getProperty("ressources.dossier.deck.export");
		File repDecksExport = new File(dossierDeckExport);
		if (!repDecksExport.isDirectory()) {
			repDecksExport.mkdirs();
		}
		File repDecks = new File(dossDecks);
		if (!repDecks.isDirectory()) {
			repDecks.mkdirs();
		}
		File[] fichiersDecks = repDecks.listFiles();
		for(File deckFile : fichiersDecks) {
			if (deckFile.isFile()) {
				Deck deck = Deck.deserialiser(deckFile.getName());
				listeDeck.addElement(deck.getNomDeck());
			}
		}

		JButton btnChoixDuDeck = new JButton("Choisir le deck");
		btnChoixDuDeck.setBounds(57, 54, 122, 23);
		btnChoixDuDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Deck deck = new Deck();
				if (list.getSelectedValue() != null ) {
					deck = Deck.deserialiser(list.getSelectedValue() + ".xml");
				}
				GestionDeck frame = new GestionDeck(deck);
				frame.setVisible(true);
				dispose();
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnChoixDuDeck);

		JButton btnChoixAlatoire = new JButton("Choix aléatoire");
		btnChoixAlatoire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Deck deck = Deck.rechercherDeckRandom();
				DefaultListModel<String> listeDeck = new DefaultListModel<String>();
				listeDeck.addElement(deck.getNomDeck());
				ChoixDeck.this.list.setModel(listeDeck);
				list.setSelectedIndex(0);
				lblImgCartePrinc.setIcon(null);
				lblImgCartePrinc.setText("");
				if (deck.getCartePrincipale() != null && !deck.getCartePrincipale().equals("")) {
					Carte carte = deck.getCartePrincipale();
					if (carte != null) {
						ImageIcon image = Singleton.getInstance().getImage(carte, 0);
						lblImgCartePrinc.setIcon(image);
					} else {
						String pasImage = "Aucune carte ne correspond à ce nom";
						lblImgCartePrinc.setText(pasImage);

					}
				}

			}
		});
		btnChoixAlatoire.setBounds(378, 54, 129, 23);
		contentPane.add(btnChoixAlatoire);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 93, 218, 213);
		contentPane.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(null);
		
		list = new JList<String>(listeDeck);
		list.setBounds(0, 0, 218, 213);
		panel.add(list);
		
		JButton btnNouveauDeck = new JButton("Nouveau deck");
		btnNouveauDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Deck deck = new Deck();
				GestionDeck frame = new GestionDeck(deck);
				frame.setVisible(true);
				dispose();
				
			}
		});
		btnNouveauDeck.setBounds(201, 54, 149, 23);
		contentPane.add(btnNouveauDeck);


	}
}
