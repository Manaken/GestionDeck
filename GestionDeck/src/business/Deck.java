package business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import single.Singleton;
import xml.XMLTools;

public class Deck {

	public static final int CREATURES = 0;
	public static final int EPHEMERES = 1;
	public static final int RITUELS = 2;
	public static final int ENCHANTEMENTS= 3;
	public static final int ARTEFACTS = 4;
	public static final int ARPENTEUR = 5;
	public static final int TERRAINS = 6;
	
	
	/**
	 * Num�ro de deck
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
		cartePrincipale = new Carte();
		description = "";
		couleurs = "";
		liste = new ListeDeck();
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
		File[] fichiersDecks = repDecks.listFiles();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Deck deserialiser(String nomDeckXml) {
		Deck deck = null;

		try {
			deck = (Deck)XMLTools.decodeFromFile(Singleton.getInstance().getProp().getProperty("ressources.xml.decks") + "/" + nomDeckXml);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deck;
	}
	
	/**
	 * Renvoie le nombre de deck
	 * @return
	 */
	public static int getNbDecks() {
		int nbDecks = 0;
		File repDecks = new File(Singleton.getInstance().getProp().getProperty("ressources.xml.decks"));
		File[] fichiersDecks = repDecks.listFiles();
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
		nbTypesCartes[ARPENTEUR] = getListe().getListeArpenteurs().size();
		nbTypesCartes[TERRAINS] = getListe().getListeTerrains().size();
		
		return nbTypesCartes;
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
