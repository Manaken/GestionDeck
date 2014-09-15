package vue.deck;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import single.Singleton;
import vue.technique.Validation;
import business.Carte;
import business.Deck;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

public class GestionDeck extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nomDeck;
	private JFileChooser selectFichier;

	private Deck deck;
	private JLabel lblImgCartePrinc;
	private JTextField txtCoutMoyen;
	private JTree arbreCartes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Deck deck = new Deck(Deck.deserialiser("GBW Rock.xml"));
					GestionDeck frame = new GestionDeck(deck);
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
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(300, 20, 935, 879);
		setTitle(this.deck.getNomDeck());
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		JMenuItem mntmEditerLaListe = new JMenuItem("Editer la liste");
		mntmEditerLaListe.addActionListener(new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
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
	
					e.printStackTrace();
				} catch (IOException e) {
	
					e.printStackTrace();
				}
				
				Validation frame = new Validation("Fichier correctement exporté", "Le fichier a bien été exporté. \n" + file.getAbsolutePath());
				frame.setVisible(true);
				
				
			}
		});
		mnFichier.add(mntmEditerLaListe);
		
		JMenuItem mntmRevenirLa = new JMenuItem("Revenir à la liste des decks");
		mntmRevenirLa.addActionListener(new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ChoixDeck frame = new ChoixDeck();
				frame.setVisible(true);
				GestionDeck.this.dispose();
				
			}
		}
		);
		mnFichier.add(mntmRevenirLa);
		
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionDeck.this.dispose();
			}
		});
		mnFichier.add(mntmQuitter);
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

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(2, 0, 917, 851);
		contentPane.add(tabbedPane);

		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Button.background"));
		JPanel panelDetail = new JPanel();
		tabbedPane.addTab("Généralités", null, panel, null);
		tabbedPane.addTab("Détails", null, panelDetail, null);
		panel.setLayout(null);
		panelDetail.setLayout(null);


		int [] nbTypesCartes = deck.getnbTypesCartes();

		DefaultPieDataset pieDataset = new DefaultPieDataset();
		if (nbTypesCartes[Deck.CREATURES] > 0) {
			pieDataset.setValue("Créatures", nbTypesCartes[Deck.CREATURES]);
		}
		if (nbTypesCartes[Deck.EPHEMERES] > 0) {
			pieDataset.setValue("Ephémères", nbTypesCartes[Deck.EPHEMERES]);
		}
		if (nbTypesCartes[Deck.RITUELS] > 0) {
			pieDataset.setValue("Rituels", nbTypesCartes[Deck.RITUELS]);
		}
		if (nbTypesCartes[Deck.ENCHANTEMENTS] > 0) {
			pieDataset.setValue("Enchantements", nbTypesCartes[Deck.ENCHANTEMENTS]);
		}
		if (nbTypesCartes[Deck.ARTEFACTS] > 0) {
			pieDataset.setValue("Artefacts", nbTypesCartes[Deck.ARTEFACTS]);
		}
		if (nbTypesCartes[Deck.PLANESWALKERS] > 0) {
			pieDataset.setValue("Planeswalkers", nbTypesCartes[Deck.PLANESWALKERS]);
		}
		if (nbTypesCartes[Deck.TERRAINS] > 0) {
			pieDataset.setValue("Terrains", nbTypesCartes[Deck.TERRAINS]);
		}

		JFreeChart pieChart = ChartFactory.createPieChart3D("",
				pieDataset, false, false, true);

		DefaultCategoryDataset catDataset = new DefaultCategoryDataset();
		catDataset.setValue(deck.getListeCarteCMC(0, false).size(),"Nb cartes", "0");
		catDataset.setValue(deck.getListeCarteCMC(1, false).size(),"Nb cartes", "1");
		catDataset.setValue(deck.getListeCarteCMC(2, false).size(),"Nb cartes", "2");
		catDataset.setValue(deck.getListeCarteCMC(3, false).size(),"Nb cartes", "3");
		catDataset.setValue(deck.getListeCarteCMC(4, false).size(),"Nb cartes", "4");
		catDataset.setValue(deck.getListeCarteCMC(5, false).size(),"Nb cartes", "5");
		catDataset.setValue(deck.getListeCarteCMC(6, true).size(),"Nb cartes", "6+");
		JFreeChart histoChart = ChartFactory.createBarChart("", "",
				"", catDataset, PlotOrientation.VERTICAL, true, true, false);
		
		lblImgCartePrinc = new JLabel();
		lblImgCartePrinc.setBounds(10, 268, 223, 310);
		panel.add(lblImgCartePrinc);

		Carte carte = deck.getCartePrincipale();
		if (carte != null) {
			ImageIcon image = Singleton.getInstance().getImage(carte, 0);
			lblImgCartePrinc.setIcon(image);
		}

		JLabel lblNomDuDeck = new JLabel("Nom du deck");
		lblNomDuDeck.setBounds(10, 11, 218, 26);
		panel.add(lblNomDuDeck);

		nomDeck = new JTextField(this.deck.getNomDeck());
		nomDeck.setBounds(235, 10, 353, 28);
		panel.add(nomDeck);
		nomDeck.setColumns(10);

		JLabel lblDescriptionDuDeck = new JLabel("Description du deck");
		lblDescriptionDuDeck.setBounds(10, 64, 124, 14);
		panel.add(lblDescriptionDuDeck);

		final JTextArea descrDeck = new JTextArea(this.deck.getDescription() != null ? this.deck.getDescription() : "");
		descrDeck.setBounds(10, 89, 428, 98);
		panel.add(descrDeck);

		JButton btnSaisirLaListe = new JButton("Saisir la liste");
		btnSaisirLaListe.setBounds(390, 209, 109, 23);
		panel.add(btnSaisirLaListe);

		JButton btnSauvegarder = new JButton("Sauvegarder");
		btnSauvegarder.setBounds(226, 209, 118, 23);
		panel.add(btnSauvegarder);
		JButton btnSaisirPartir = new JButton("Saisir à partir d'un fichier");
		btnSaisirPartir.setBounds(10, 209, 189, 23);
		panel.add(btnSaisirPartir);

		JLabel lblNomDeLa = new JLabel("Carte principale ");
		lblNomDeLa.setBounds(90, 243, 109, 14);
		panel.add(lblNomDeLa);

		ChartPanel cPanel = new ChartPanel(pieChart);
		cPanel.setBounds(337, 45, 265, 200);
		panelDetail.add(cPanel);
		cPanel.setMouseZoomable(true);
		cPanel.setDisplayToolTips(false);

		txtCoutMoyen = new JTextField();
		txtCoutMoyen.setBounds(96, 11, 86, 20);
		panelDetail.add(txtCoutMoyen);
		txtCoutMoyen.setEditable(false);
		txtCoutMoyen.setText(""+deck.getCMCMoyen());

		JLabel lblCotMoyen = new JLabel("Coût moyen");
		lblCotMoyen.setBounds(0, 11, 76, 14);
		panelDetail.add(lblCotMoyen);

		ChartPanel cPanel2 = new ChartPanel(histoChart);
		cPanel2.setBounds(0, 45, 265, 200);
		panelDetail.add(cPanel2);
		panelDetail.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblCotMoyen, txtCoutMoyen, cPanel2, cPanel}));

		JLabel lblListeDesCartes = new JLabel("Liste des cartes du deck");
		lblListeDesCartes.setBounds(692, 11, 158, 14);
		panel.add(lblListeDesCartes);
		selectFichier = new JFileChooser(dossierFichierImport);
		selectFichier.setBounds(200, 300, 352, -229);
		panel.add(selectFichier);
		arbreCartes = new JTree();
		arbreCartes.setBounds(655, 36, 247, 750);
		panel.add(arbreCartes);
		arbreCartes.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		arbreCartes.setForeground(UIManager.getColor("Button.focus"));
		arbreCartes.setBackground(Color.WHITE);
		panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblNomDuDeck, nomDeck, lblListeDesCartes, lblDescriptionDuDeck, descrDeck, btnSaisirPartir, btnSauvegarder, btnSaisirLaListe, lblNomDeLa, lblImgCartePrinc, selectFichier, arbreCartes}));
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
        			ImageIcon image = Singleton.getInstance().getImage(carte, 0);
        			lblImgCartePrinc.setIcon(image);
        		}
        	}
        });
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
						if (ficDeck.isFile()) {
							Deck deckTempo = Deck.deserialiser(ficDeck.getName());
							if (deckTempo.getNumDeck() > nbDecks) {
								nbDecks = deckTempo.getNumDeck();
							}
						}
					}
					if (GestionDeck.this.deck.getNumDeck() == 0) {
						GestionDeck.this.deck.setNumDeck(nbDecks+1);
					}
					GestionDeck.this.deck.serialiser();
				}
			}
		});
		btnSaisirLaListe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SaisieListeDeck fenetreFille  = new SaisieListeDeck(GestionDeck.this.deck);
				fenetreFille.setVisible(true);
				GestionDeck.this.dispose();
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
