package vue.deck;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import single.Singleton;
import business.Carte;
import business.Deck;
import javax.swing.JTabbedPane;

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
	private JLabel lblImgCartePrinc;
	private JTextField txtCoutMoyen;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionDeck frame = new GestionDeck(Deck.deserialiser("GBW Rock.xml"));
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
		setBounds(300, 50, 935, 863);
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

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(2, 0, 917, 825);
		contentPane.add(tabbedPane);

		JPanel panel = new JPanel();
		JPanel panelDetail = new JPanel();
		tabbedPane.addTab("Généralités", null, panel, null);
		tabbedPane.addTab("Détails", null, panelDetail, null);
		panel.setLayout(null);
		panelDetail.setLayout(null);

		lblImgCartePrinc = new JLabel();
		lblImgCartePrinc.setBounds(10, 329, 223, 310);
		panel.add(lblImgCartePrinc);

		String listeDeck = this.deck.getListe().toStringName();
		Carte carte = deck.getCartePrincipale();
		if (carte != null) {
			ImageIcon image = Singleton.getInstance().getImage(carte, 0);
			lblImgCartePrinc.setIcon(image);
		}

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
		btnSaisirLaListe.setBounds(10, 221, 109, 23);
		panel.add(btnSaisirLaListe);

		JButton btnSauvegarder = new JButton("Sauvegarder");
		btnSauvegarder.setBounds(162, 221, 118, 23);
		panel.add(btnSauvegarder);

		JButton btnRevenirLa = new JButton("Revenir à la liste");
		btnRevenirLa.setBounds(323, 221, 150, 23);
		panel.add(btnRevenirLa);
		JButton btnSaisirPartir = new JButton("Saisir à partir d'un fichier");
		btnSaisirPartir.setBounds(10, 270, 189, 23);
		panel.add(btnSaisirPartir);

		JButton btnEditerLaListe = new JButton("Editer la liste");
		btnEditerLaListe.setBounds(247, 270, 131, 23);
		panel.add(btnEditerLaListe);

		JLabel lblNomDeLa = new JLabel("Carte principale ");
		lblNomDeLa.setBounds(90, 304, 109, 14);
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

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(619, 29, 272, 702);
		panel.add(scrollPane);
		txtListeDeck = new JEditorPane("text/html", listeDeck);
		scrollPane.setViewportView(txtListeDeck);
		txtListeDeck.setAutoscrolls(true);

		JLabel lblListeDesCartes = new JLabel("Liste des cartes du deck");
		lblListeDesCartes.setBounds(692, 11, 158, 14);
		panel.add(lblListeDesCartes);
		selectFichier = new JFileChooser(dossierFichierImport);
		selectFichier.setBounds(200, 300, 352, -229);
		panel.add(selectFichier);
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
				deck.setCartePrincipale(carte);
				if (carte != null) {
					carte.coutCarte();
					lblImgCartePrinc.setIcon(null);
					lblImgCartePrinc.setText("");
					ImageIcon image = Singleton.getInstance().getImage(carte, 0);
					lblImgCartePrinc.setIcon(image);
				} else {
					String pasImage = "Aucune carte ne correspond à ce nom";
					lblImgCartePrinc.setText(pasImage);


				}

			}
		});
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
		btnRevenirLa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChoixDeck frame = new ChoixDeck();
				frame.setVisible(true);
				GestionDeck.this.dispose();
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
