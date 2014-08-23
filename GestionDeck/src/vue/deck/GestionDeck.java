package vue.deck;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import single.Singleton;
import business.Carte;
import business.Deck;
import ch.rakudave.suggest.JSuggestField;

public class GestionDeck extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nomDeck;
	private JFileChooser selectFichier;
	private JEditorPane txtListeDeck;


	private Deck deck;
	private JSuggestField txtCartePrinc;
	private JLabel lblImgCartePrinc;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionDeck frame = new GestionDeck(new Deck());
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
	public GestionDeck(final Deck deck) {

		this.deck = deck;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(300, 50, 935, 802);
		setTitle(this.deck.getNomDeck());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String chemin = Singleton.getInstance().getProp().getProperty("ressources.dossier.deck.import");
		File dossierFichierImport = new File(chemin);
		if (!dossierFichierImport.isDirectory()) {
			dossierFichierImport.setWritable(true);
			dossierFichierImport.mkdirs();
		}
		selectFichier = new JFileChooser(dossierFichierImport);
		selectFichier.setBounds(250, 300, 353, -160);
		contentPane.add(selectFichier);
		
		nomDeck = new JTextField(this.deck.getNomDeck());
		nomDeck.setBounds(196, 11, 353, 28);
		contentPane.add(nomDeck);		
		nomDeck.setColumns(10);

		JLabel lblNomDuDeck = new JLabel("Nom du deck");
		lblNomDuDeck.setBounds(44, 14, 124, 23);
		contentPane.add(lblNomDuDeck);

		JLabel lblListeDesCartes = new JLabel("Liste des cartes du deck");
		lblListeDesCartes.setBounds(703, 11, 158, 14);
		contentPane.add(lblListeDesCartes);

		final JTextArea descrDeck = new JTextArea(this.deck.getDescription() != null ? this.deck.getDescription() : "");
		descrDeck.setBounds(10, 101, 428, 98);
		contentPane.add(descrDeck);

		JLabel lblDescriptionDuDeck = new JLabel("Description du deck");
		lblDescriptionDuDeck.setBounds(10, 62, 124, 14);
		contentPane.add(lblDescriptionDuDeck);

		

		JButton btnSaisirLaListe = new JButton("Saisir la liste");
		btnSaisirLaListe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SaisieListeDeck fenetreFille  = new SaisieListeDeck(GestionDeck.this.deck);
				fenetreFille.setVisible(true);
				GestionDeck.this.dispose();
			}
		});
		btnSaisirLaListe.setBounds(10, 220, 109, 23);
		contentPane.add(btnSaisirLaListe);


		String listeDeck = this.deck.getListe().toStringName();
		txtListeDeck = new JEditorPane("text/html", listeDeck);
		txtListeDeck.setAutoscrolls(true);
		txtListeDeck.setBounds(618, 36, 272, 702);
		contentPane.add(txtListeDeck);
		txtListeDeck.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				String nomCarte = GestionDeck.this.deck.getListe().getListe().get((e.getY()-(txtListeDeck.getFont().getSize()+7))/(txtListeDeck.getFont().getSize()+7));
				Carte carte = Carte.rechercheCarte(nomCarte);
				if (carte != null) {
					GestionDeck.this.txtCartePrinc.setText(nomCarte);
					lblImgCartePrinc.setIcon(null);
					lblImgCartePrinc.setText("");
					if (!txtCartePrinc.getText().isEmpty()) {
						Carte carteTempo = Carte.rechercheCarte(txtCartePrinc.getText());
						if (carteTempo != null) {
							ImageIcon image = Singleton.getInstance().getImage(carteTempo, 0);
							lblImgCartePrinc.setIcon(image);
						} else {
							String pasImage = "Aucune carte ne correspond à ce nom";
							lblImgCartePrinc.setText(pasImage);

						}
					} else {
						String pasImage = "Tape le nom d'une carte";
						lblImgCartePrinc.setText(pasImage);
					}
				}
				
			}
		});
		JButton btnSaisirPartir = new JButton("Saisir à partir d'un fichier");
		btnSaisirPartir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GestionDeck.this.selectFichier.showOpenDialog(null);
				Deck deck = new Deck();
				if (selectFichier.getSelectedFile() != null) {
					deck = new Deck(selectFichier.getSelectedFile());
					GestionDeck.this.deck = deck;
					GestionDeck frame = new GestionDeck(deck);
					frame.setVisible(true);
					GestionDeck.this.dispose();
				}
				
			}

		});
		btnSaisirPartir.setBounds(10, 267, 189, 23);
		contentPane.add(btnSaisirPartir);
		
		JButton btnRevenirLa = new JButton("Revenir à la liste");
		btnRevenirLa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChoixDeck frame = new ChoixDeck();
				frame.setVisible(true);
				GestionDeck.this.dispose();
			}
		});
		btnRevenirLa.setBounds(322, 220, 150, 23);
		contentPane.add(btnRevenirLa);
		
		JLabel lblNomDeLa = new JLabel("Nom de la carte principale ");
		lblNomDeLa.setBounds(10, 314, 169, 14);
		contentPane.add(lblNomDeLa);
		
		txtCartePrinc = new JSuggestField(this);
		if (deck.getCartePrincipale() != null) {
			txtCartePrinc.setText(deck.getCartePrincipale().getName());
		}
		txtCartePrinc.setBounds(145, 311, 233, 23);
		contentPane.add(txtCartePrinc);
		txtCartePrinc.setColumns(10);
		
		lblImgCartePrinc = new JLabel();
		Carte carte = deck.getCartePrincipale();
		if (carte != null) {
			ImageIcon image = Singleton.getInstance().getImage(carte, 0);
			lblImgCartePrinc.setIcon(image);
		}
		lblImgCartePrinc.setBounds(50, 358, 223, 310);
		contentPane.add(lblImgCartePrinc);
		
		txtCartePrinc.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// Si le nom de la carte fait au moins 3 caratères de long 
				// (txtNomcarte.getText() + arg0.getChar()
				if (txtCartePrinc.getText().length() > 1) {
					// Une liste de noms de cartes est recherchée en fonction des lettres saisies
					ArrayList<String> listeNomCartes = Carte.getListeCarteSuggeree(txtCartePrinc.getText()+arg0.getKeyChar(), 5);
					
					Vector<String> vecteurNomsCartes = new Vector<>();
					for (String nomCarte : listeNomCartes) {
						vecteurNomsCartes.add(nomCarte);
					}
					txtCartePrinc.setSuggestData(vecteurNomsCartes);
				}
				
			}
		});
		txtCartePrinc.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				lblImgCartePrinc.setIcon(null);
				lblImgCartePrinc.setText("");
				if (!txtCartePrinc.getText().isEmpty()) {
					Carte carte = Carte.rechercheCarte(txtCartePrinc.getText());
					if (carte != null) {
						ImageIcon image = Singleton.getInstance().getImage(carte, 0);
						lblImgCartePrinc.setIcon(image);
					} else {
						String pasImage = "Aucune carte ne correspond à ce nom";
						lblImgCartePrinc.setText(pasImage);

					}
				} else {
					String pasImage = "Tape le nom d'une carte";
					lblImgCartePrinc.setText(pasImage);
				}
			}
		});

		JButton btnSauvegarder = new JButton("Sauvegarder");
		btnSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!nomDeck.getText().isEmpty()) {
					GestionDeck.this.deck.setCouleurs("10101");
					if (!descrDeck.getText().isEmpty()) {
						GestionDeck.this.deck.setDescription(descrDeck.getText());
					}
					GestionDeck.this.deck.setNomDeck(nomDeck.getText());

					File repDecks = new File(Singleton.getInstance().getProp().getProperty("ressources.xml.decks"));
					File[] fichiersDecks = repDecks.listFiles();
					int nbDecks = 0;
					for (File ficDeck : fichiersDecks) {
						Deck deckTempo = Deck.deserialiser(ficDeck.getName());
						if (deckTempo.getNumDeck() > nbDecks) {
							nbDecks = deckTempo.getNumDeck();
						}
					}
					if (GestionDeck.this.deck.getNumDeck() == 0) {
						GestionDeck.this.deck.setNumDeck(nbDecks+1);
					}
					if (!txtCartePrinc.getText().isEmpty()) {
						Carte carte = Carte.rechercheCarte(txtCartePrinc.getText());
						GestionDeck.this.deck.setCartePrincipale(carte);
					}
					GestionDeck.this.deck.serialiser();
				}
			}
		});
		btnSauvegarder.setBounds(162, 220, 118, 23);
		contentPane.add(btnSauvegarder);
		
		JButton btnEditerLaListe = new JButton("Editer la liste");
		btnEditerLaListe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String dossierDeckExport = Singleton.getInstance().getProp().getProperty("ressources.dossier.deck.export");
				File file = new File(dossierDeckExport + GestionDeck.this.deck.getNomDeck() + ".html");
				
				try {
					file.createNewFile();
					FileOutputStream out = new FileOutputStream(file);
					String listeCarte = GestionDeck.this.deck.getListe().toStringName();
					out.write(listeCarte.getBytes());
					out.flush();
					out.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		btnEditerLaListe.setBounds(248, 267, 131, 23);
		contentPane.add(btnEditerLaListe);
		
		
	}

	/**
	 * @return the deck
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * @param deck the deck to set
	 */
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
}
