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
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import single.Singleton;
import business.Carte;
import business.Deck;
import business.ListeDeck;
import ch.rakudave.suggest.JSuggestField;

public class SaisieListeDeck extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JSuggestField txtNomcarte;
	final JEditorPane txtListeDeck;
	private ListeDeck listeDeck;
	private JTextField nbCartes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SaisieListeDeck frame = new SaisieListeDeck(Deck.deserialiser("GBW Rock.xml"));
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
	public SaisieListeDeck(final Deck deck) {


		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(500, 50, 697, 802);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		final JLabel lblImgCartePrinc = new JLabel();
		lblImgCartePrinc.setBounds(34, 181, 223, 310);
		contentPane.add(lblImgCartePrinc);
		



		txtNomcarte = new JSuggestField(this);
		txtNomcarte.setBounds(171, 21, 161, 20);
		txtNomcarte.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				lblImgCartePrinc.setIcon(null);
				lblImgCartePrinc.setText("");
				if (!txtNomcarte.getText().isEmpty()) {
					Carte carte = Carte.rechercheCarte(txtNomcarte.getText());
					if (carte != null) {
						ImageIcon image = Singleton.getInstance().getImage(Carte.rechercheCarte(txtNomcarte.getText()), 0);
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
		txtNomcarte.addKeyListener(new KeyListener() {
			
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
				if (txtNomcarte.getText().length() > 1) {
					// Une liste de noms de cartes est recherchée en fonction des lettres saisies
					ArrayList<String> listeNomCartes = Carte.getListeCarteSuggeree(txtNomcarte.getText()+arg0.getKeyChar(), 5);
					
					Vector<String> vecteurNomsCartes = new Vector<>();
					for (String nomCarte : listeNomCartes) {
						vecteurNomsCartes.add(nomCarte);
					}
					txtNomcarte.setSuggestData(vecteurNomsCartes);
				}
				
			}
		});
		contentPane.add(txtNomcarte);
		txtNomcarte.setColumns(10);


		JLabel lblNomCarte = new JLabel("Nom de la carte à ajouter");
		lblNomCarte.setBounds(10, 24, 151, 14);
		contentPane.add(lblNomCarte);

		

		JButton btnAjouterCarte = new JButton("Ajouter carte");
		btnAjouterCarte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Carte carte = Carte.rechercheCarte(txtNomcarte.getText());
				if (carte != null) {
					int nombreCartes = 1;
					if (!nbCartes.getText().isEmpty()) {
						nombreCartes = Integer.parseInt(nbCartes.getText());
					}
					for (int i=0; i<nombreCartes; i++) {
						listeDeck.ajouterCarte(carte);
					}
					txtListeDeck.setText(listeDeck.toStringName());
				}
			}

		});
		btnAjouterCarte.setBounds(174, 113, 115, 23);
		contentPane.add(btnAjouterCarte);

		JButton btnRetirerCarte = new JButton("Retirer carte");
		btnRetirerCarte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Carte carte = Carte.rechercheCarte(txtNomcarte.getText());
				if (carte != null) {
					int nombreCartes = 1;
					if (!nbCartes.getText().isEmpty()) {
						nombreCartes = Integer.parseInt(nbCartes.getText());
					}
					for (int i=0; i<nombreCartes; i++) {
						listeDeck.retirerCarte(carte);
					}
					txtListeDeck.setText(listeDeck.toStringName());
				}

			}
		});
		btnRetirerCarte.setBounds(174, 147, 115, 23);
		contentPane.add(btnRetirerCarte);
		
		JButton btnSauvegarderDeck = new JButton("Sauvegarder deck");
		btnSauvegarderDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deck.setListe(listeDeck);
				deck.serialiser();
			}
		});
		btnSauvegarderDeck.setBounds(56, 502, 151, 23);
		contentPane.add(btnSauvegarderDeck);
		
		JButton btnRevenirSurLa = new JButton("Revenir sur la gestion de deck");
		btnRevenirSurLa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				GestionDeck frame = new GestionDeck(deck);
				frame.setVisible(true);
				SaisieListeDeck.this.dispose();
			}
		});
		btnRevenirSurLa.setBounds(34, 536, 188, 23);
		contentPane.add(btnRevenirSurLa);
		
		JLabel lblNombreDeCartes = new JLabel("Nombre de cartes à ajouter");
		lblNombreDeCartes.setBounds(10, 49, 161, 14);
		contentPane.add(lblNombreDeCartes);
		
		nbCartes = new JTextField();
		nbCartes.setBounds(171, 46, 58, 20);
		contentPane.add(nbCartes);
		nbCartes.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane.setBounds(380, 23, 270, 700);
		contentPane.add(scrollPane);
		
		txtListeDeck = new JEditorPane();
		scrollPane.setViewportView(txtListeDeck);
		txtListeDeck.setContentType("text/html");
		txtListeDeck.setEditable(false);
		
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
				String nomCarte = SaisieListeDeck.this.listeDeck.getListe().get((e.getY()-(txtListeDeck.getFont().getSize()+7))/(txtListeDeck.getFont().getSize()+7));
				Carte carte = Carte.rechercheCarte(nomCarte);
				if (carte != null) {
					SaisieListeDeck.this.txtNomcarte.setText(nomCarte);
					lblImgCartePrinc.setIcon(null);
					lblImgCartePrinc.setText("");
					if (!txtNomcarte.getText().isEmpty()) {
						Carte carteTempo = Carte.rechercheCarte(txtNomcarte.getText());
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
		if (deck.getListe().isEmpty()) {
			listeDeck = new ListeDeck();
		} else {
			listeDeck = deck.getListe();
			txtListeDeck.setText(listeDeck.toStringName());
		}
	}
}
