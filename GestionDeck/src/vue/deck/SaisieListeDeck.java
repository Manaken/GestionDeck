package vue.deck;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import single.Singleton;
import business.Carte;
import business.Deck;
import business.ListeDeck;
import ch.rakudave.suggest.JSuggestField;
import java.awt.Color;

public class SaisieListeDeck extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JSuggestField txtNomcarte;
	private ListeDeck listeDeck;
	private JTextField nbCartes;
	private JTree arbreCartes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Deck deck = new Deck(Deck.deserialiser("GBW Rock.xml"));
					SaisieListeDeck frame = new SaisieListeDeck(deck);
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
		setBounds(500, 50, 726, 802);
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

					modifierArbreCarte(listeDeck);
					@SuppressWarnings("unchecked")
					Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)arbreCartes.getModel().getRoot()).preorderEnumeration();
					while (e.hasMoreElements()) {
						//Expand the current node.
						SaisieListeDeck.this.arbreCartes.expandPath(new TreePath((e.nextElement()).getPath()));
					}

					SaisieListeDeck.this.contentPane.repaint();
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
				}

				modifierArbreCarte(listeDeck);
				@SuppressWarnings("unchecked")
				Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)arbreCartes.getModel().getRoot()).preorderEnumeration();
				while (e.hasMoreElements()) {
					//Expand the current node.
					SaisieListeDeck.this.arbreCartes.expandPath(new TreePath((e.nextElement()).getPath()));
				}
				SaisieListeDeck.this.contentPane.repaint();

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
		if (deck.getListe().isEmpty()) {
			listeDeck = new ListeDeck();
		} else {
			listeDeck = deck.getListe();
		}

		arbreCartes = new JTree();
		arbreCartes.setBounds(410, 11, 247, 732);
		contentPane.add(arbreCartes);
		arbreCartes.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		arbreCartes.setForeground(UIManager.getColor("Button.focus"));
		arbreCartes.setBackground(Color.WHITE);
		alimenterArbreCarte(deck);
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)arbreCartes.getModel().getRoot()).preorderEnumeration();
		while (e.hasMoreElements()) {
			//Expand the current node.
			this.arbreCartes.expandPath(new TreePath((e.nextElement()).getPath()));
		}

		arbreCartes.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
				DefaultMutableTreeNode noeud = (DefaultMutableTreeNode) 
						arbreCartes.getLastSelectedPathComponent();
				if (noeud == null)
					return;
				if (noeud.isLeaf()) {
					String nomCarte = noeud.toString();
					boolean trouve = false;
					for (int i =0; i<5 && !trouve; i++) {
						String carac = ""+nomCarte.charAt(i);
						if (carac.matches("[A-Za-z]")) {
							trouve = true;
							nomCarte = nomCarte.substring(i);
						}
					}
					Carte carte = Carte.rechercheCarte(nomCarte);
					txtNomcarte.setText(carte.getName());
					ImageIcon image = Singleton.getInstance().getImage(carte, 0);
					lblImgCartePrinc.setIcon(image);
				}
			}
		});
	}

	/**
	 * Permet d'initialiser l'arbre représentant la liste des cartes du deck
	 * @param arbreCartes
	 * @param deck
	 */
	private void alimenterArbreCarte(Deck deck) {
		DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Nombre de cartes : " + deck.getListe().nbCarte());

		DefaultMutableTreeNode noeud = new DefaultMutableTreeNode();
		for (String carte : deck.getListe().getListe()) {
			if (carte.charAt(0) != ' ') {
				noeud = new DefaultMutableTreeNode(carte);
				racine.add(noeud);
				arbreCartes.setModel(new DefaultTreeModel(racine));

			} else {
				DefaultMutableTreeNode carteLeaf = new DefaultMutableTreeNode(carte);
				noeud.add(carteLeaf);
			}
		}
	}

	/**
	 * Permet de mettre l'arbre à jour
	 * @param liste
	 */
	private void modifierArbreCarte(ListeDeck liste) {

		TreeModel modele = arbreCartes.getModel();

		DefaultMutableTreeNode racine = (DefaultMutableTreeNode) modele.getRoot();
		racine.setUserObject("Nombre de cartes : " + liste.nbCarte());

		DefaultMutableTreeNode noeud = new DefaultMutableTreeNode();
		int indexNoeud = 0;
		int indexLeaf = 0;
		for (String carte : liste.getListe()) {
			if (carte.charAt(0) != ' ') {
				noeud = (DefaultMutableTreeNode) racine.getChildAt(indexNoeud);
				noeud.setUserObject(carte);
				indexNoeud++;
				indexLeaf = 0;

			} else {
				DefaultMutableTreeNode carteLeaf = (DefaultMutableTreeNode) noeud.getChildAt(indexLeaf);
				carteLeaf.setUserObject(carte);
				indexLeaf++;

			}
		}
	}
}
