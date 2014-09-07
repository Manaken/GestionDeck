package business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import single.Singleton;
import xml.XMLTools;

public class Deck {

	/**
	 * Types de cartes
	 */
	public static final int CREATURES = 0;
	public static final int EPHEMERES = 1;
	public static final int RITUELS = 2;
	public static final int ENCHANTEMENTS= 3;
	public static final int ARTEFACTS = 4;
	public static final int PLANESWALKERS = 5;
	public static final int TERRAINS = 6;

	/** 
	 * Couleurs des cartes
	 */
	public static final int BLANC = 0;
	public static final int BLEU = 1;
	public static final int NOIR = 2;
	public static final int ROUGE = 3;
	public static final int VERT = 4;
	public static final int MULTI = 5;
	public static final int INCOLORE = 6;

	/**
	 * Numéro de deck
	 */
	private int numDeck;

	/**
	 * Nom du deck
	 */
	private String nomDeck;

	/**
	 * Carte principale du deck
	 */
	private Carte cartePrincipale;

	/**
	 * Description du deck
	 */
	private String description;

	/**
	 * Couleurs du deck
	 */
	private String couleurs;

	/**
	 * Liste des cartes du deck
	 */
	private ListeDeck liste;

	// Constructeurs
	public Deck() {
		super();
		nomDeck = "";
		cartePrincipale = null;
		description = "";
		couleurs = "";
		liste = new ListeDeck();
	}

	public Deck(Deck deck) {
		nomDeck = deck.getNomDeck();
		numDeck = deck.getNumDeck();
		cartePrincipale = deck.getCartePrincipale();
		description = deck.getDescription();
		couleurs = deck.getCouleurs();
		liste = deck.getListe();

		liste.setListe(new ArrayList<String>());
		liste.initListeCartes(liste.getListeCreatures(), "Créatures");
		liste.initListeCartes(liste.getListeEphemeres(), "Ephémères");
		liste.initListeCartes(liste.getListeRituels(), "Rituels");
		liste.initListeCartes(liste.getListeEnchantements(), "Enchantements");
		liste.initListeCartes(liste.getListeArtefacts(), "Artefacts");
		liste.initListeCartes(liste.getListePlaneswalkers(), "Planeswalkers");
		liste.initListeCartes(liste.getListeTerrains(), "Terrains");
	}
	/**
	 * Constructeur d'un deck suivant un fichier plat
	 * @param file
	 */
	public Deck(File file) {
		FileInputStream in = null;
		StringBuilder sb = new StringBuilder();
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (in != null) {

			try {
				byte[] buffer = new byte[512];
				while (in.read(buffer) > 0 ) {
					String donneesFichier = new String(buffer);
					sb.append(donneesFichier);
					buffer = new byte[512];
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		StringTokenizer tokens = new StringTokenizer(sb.toString(), "\n");
		ArrayList<String> listeLignes = new ArrayList<>();
		while(tokens.hasMoreTokens()) {
			String ligne = tokens.nextToken();
			if (ligne.endsWith("\r")) {
				ligne = ligne.substring(0, ligne.length()-1);
			}
			Pattern pattern = Pattern.compile("\\s*[0-9]+\\s+[A-Za-z]*.*");
			Matcher matcher = pattern.matcher(ligne);
			if (matcher.matches()) {
				listeLignes.add(ligne);
			}

		}

		liste = new ListeDeck(listeLignes);		

	}

	/**
	 * Renvoie un deck au hasard
	 * @return
	 */
	public static Deck rechercherDeckRandom() {

		int numDeck ;
		int nbDeck = getNbDecks();

		numDeck = (int)((Math.random())*nbDeck);
		File repDecks = new File(Singleton.getInstance().getProp().getProperty("ressources.xml.decks"));
		FilenameFilter textFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".xml")) {
					return true;
				} else {
					return false;
				}
			}
		};
		File[] fichiersDecks = repDecks.listFiles(textFilter);
		
		
		Deck deck = Deck.deserialiser(fichiersDecks[numDeck].getName());

		return deck;
	}

	public void serialiser() {
		try {
			String dossierDecks = Singleton.getInstance().getProp().getProperty("ressources.xml.decks");
			String nomDeck = dossierDecks + "/" + this.nomDeck + ".xml";
			File file = new File(nomDeck);
			if (file != null) {
				file.delete();
			}
			XMLTools.encodeToFile(this, nomDeck);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Deck deserialiser(String nomDeckXml) {
		Deck deck = null;

		try {
			deck = (Deck)XMLTools.decodeFromFile(Singleton.getInstance().getProp().getProperty("ressources.xml.decks") + "/" + nomDeckXml);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		deck.getListe().setListe(new ArrayList<String>());
		deck.getListe().initListeCartes(deck.getListe().getListeCreatures(), "Cr&eacute;atures");
		deck.getListe().initListeCartes(deck.getListe().getListeEphemeres(), "Eph&eacute;m&egrave;res");
		deck.getListe().initListeCartes(deck.getListe().getListeRituels(), "Rituels");
		deck.getListe().initListeCartes(deck.getListe().getListeEnchantements(), "Enchantements");
		deck.getListe().initListeCartes(deck.getListe().getListeArtefacts(), "Artefacts");
		deck.getListe().initListeCartes(deck.getListe().getListePlaneswalkers(), "Planeswalkers");
		deck.getListe().initListeCartes(deck.getListe().getListeTerrains(), "Terrains");

		return deck;
	}

	/**
	 * Renvoie le nombre de deck
	 * @return
	 */
	public static int getNbDecks() {
		int nbDecks = 0;
		File repDecks = new File(Singleton.getInstance().getProp().getProperty("ressources.xml.decks"));
		FilenameFilter textFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".xml")) {
					return true;
				} else {
					return false;
				}
			}
		};
		File[] fichiersDecks = repDecks.listFiles(textFilter);
		for (int i=0; i<fichiersDecks.length; i++) {
			nbDecks++;
		}
		return nbDecks;
	}

	/**
	 * Renvoie le nombre de chaque type de cartes du deck
	 * @return
	 */
	public int[] getnbTypesCartes() {
		int[] nbTypesCartes = new int[7];
		nbTypesCartes[CREATURES] = getListe().getListeCreatures().size();
		nbTypesCartes[EPHEMERES] = getListe().getListeEphemeres().size();
		nbTypesCartes[RITUELS] = getListe().getListeRituels().size();
		nbTypesCartes[ENCHANTEMENTS] = getListe().getListeEnchantements().size();
		nbTypesCartes[ARTEFACTS] = getListe().getListeArtefacts().size();
		nbTypesCartes[PLANESWALKERS] = getListe().getListePlaneswalkers().size();
		nbTypesCartes[TERRAINS] = getListe().getListeTerrains().size();

		return nbTypesCartes;
	}

	/**
	 * Donne le coût moyen en mana du deck
	 * @return
	 */
	public double getCMCMoyen() {
		double cmcMoyen = 0.0;

		ArrayList<Carte> listeCartes = new ArrayList<>();
		listeCartes.addAll(getListe().getListeCreatures());
		listeCartes.addAll(getListe().getListeEphemeres());
		listeCartes.addAll(getListe().getListeRituels());
		listeCartes.addAll(getListe().getListeEnchantements());
		listeCartes.addAll(getListe().getListeArtefacts());
		listeCartes.addAll(getListe().getListePlaneswalkers());

		for (Carte carte : listeCartes) {
			cmcMoyen += carte.coutCarte();
		}

		return (Math.round(cmcMoyen*100.0/listeCartes.size()))/100.0;
	}

	/**
	 * Renvoie la liste des cartes du deck dont le cout est celui indiqué par le paramètre en entrée
	 * @param cmcVoulu
	 * @param etPlus si etPlus == true et si le cout de la carte est supérieur au cout voulu, la carte est intégrée à la liste.
	 * @return
	 */
	public ArrayList<Carte> getListeCarteCMC(int cmcVoulu, boolean etPlus) {
		ArrayList<Carte> listeCartes = new ArrayList<>();
		ArrayList<Carte> listeCartesComplet = new ArrayList<>();
		listeCartesComplet.addAll(getListe().getListeCreatures());
		listeCartesComplet.addAll(getListe().getListeEphemeres());
		listeCartesComplet.addAll(getListe().getListeRituels());
		listeCartesComplet.addAll(getListe().getListeEnchantements());
		listeCartesComplet.addAll(getListe().getListeArtefacts());
		listeCartesComplet.addAll(getListe().getListePlaneswalkers());


		for (Carte carte : listeCartesComplet) {
			if (carte.coutCarte() == cmcVoulu) {
				listeCartes.add(carte);
			}
			if(carte.coutCarte() > cmcVoulu && etPlus) {
				listeCartes.add(carte);
			}
		}
		return listeCartes;
	}

	/**
	 * Renvoie la liste des cartes 
	 * @return
	 */
	public int[] getListeCarteCouleurs() {
		int [] nbCouleursCartes = new int [7];

		//#TODO 
		// Alimenter le tableau avec les couleurs des cartes
		nbCouleursCartes[BLANC] = getListe().getListeCreatures().size();
		nbCouleursCartes[BLEU] = getListe().getListeEphemeres().size();
		nbCouleursCartes[NOIR] = getListe().getListeRituels().size();
		nbCouleursCartes[ROUGE] = getListe().getListeEnchantements().size();
		nbCouleursCartes[VERT] = getListe().getListeArtefacts().size();
		nbCouleursCartes[MULTI] = getListe().getListePlaneswalkers().size();
		nbCouleursCartes[INCOLORE] = getListe().getListeTerrains().size();
		
		return nbCouleursCartes;
	}

	/*********************************\
	 ****** Getters and Setters ******
	\*********************************/
	public String getNomDeck() {
		return nomDeck;
	}

	public void setNomDeck(String nomDeck) {
		this.nomDeck = nomDeck;
	}

	public Carte getCartePrincipale() {
		return cartePrincipale;
	}

	public void setCartePrincipale(Carte cartePrincipale) {
		this.cartePrincipale = cartePrincipale;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCouleurs() {
		return couleurs;
	}

	public void setCouleurs(String couleurs) {
		this.couleurs = couleurs;
	}
	public int getNumDeck() {
		return numDeck;
	}
	public void setNumDeck(int numDeck) {
		this.numDeck = numDeck;
	}

	public ListeDeck getListe() {
		return liste;
	}

	public void setListe(ListeDeck liste) {
		this.liste = liste;
	}

	public static void main(String [] args) {

		for (int i=0; i<20; i++) {
			System.out.println(""+(int)((Math.random())*2+1));
		}

	}


}
