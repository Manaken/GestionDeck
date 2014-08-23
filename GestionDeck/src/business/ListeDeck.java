package business;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ListeDeck {

	private ArrayList<String> liste;

	private String [] tabCarteLigne;
	private ArrayList<Carte> listeArtefacts;
	private ArrayList<Carte> listeCreatures;
	private ArrayList<Carte> listeEnchantements;
	private ArrayList<Carte> listeEphemeres;
	private ArrayList<Carte> listeRituels;
	private ArrayList<Carte> listeArpenteurs;
	private ArrayList<Carte> listeTerrains;

	public ListeDeck() {
		liste = new ArrayList<>();
		tabCarteLigne = new String[300];
		listeArtefacts = new ArrayList<>();
		listeCreatures = new ArrayList<>();
		listeEnchantements = new ArrayList<>();
		listeEphemeres = new ArrayList<>();
		listeRituels = new ArrayList<>();
		listeArpenteurs = new ArrayList<>();
		listeTerrains = new ArrayList<>();
	}


	/**
	 * Construction d'une liste de cartes d'un deck à partir du format apprentice Magic Ville
	 * @param listeLignes
	 */
	public ListeDeck(ArrayList<String> listeLignes) {
		this();
		for (String carteStr : listeLignes) {
			StringBuilder nbCarte = new StringBuilder("");
			boolean nbOk = false;
			int i;
			for(i=0; i<carteStr.length() && !nbOk; i++) {
				if (carteStr.charAt(i) >= '0' && carteStr.charAt(i) <= '9') {
					nbCarte.append(carteStr.charAt(i));
				}
				if (!nbCarte.toString().equals("") && !(carteStr.charAt(i+1) >= '0' && carteStr.charAt(i+1) <= '9')) {
					nbOk = true;
				}
			}

			Carte carte = Carte.rechercheCarte(carteStr.substring(i+1));
			if (carte != null) {
				for (int j=0; j<Integer.parseInt(nbCarte.toString()); j++) {
					this.ajouterCarte(carte);
				}
			}
		}
	}


	/**
	 * 
	 * @param carte
	 */
	public void ajouterCarte(Carte carte) {


		String typeCarte = getTypePrincipal(carte);
		sortAllNom();
		if (typeCarte.equalsIgnoreCase("Artifact")) {
			listeArtefacts.add(carte);

		} else if (typeCarte.equalsIgnoreCase("Creature")) {
			listeCreatures.add(carte);

		} else if (typeCarte.equalsIgnoreCase("Enchantment")) {
			listeEnchantements.add(carte);

		} else if (typeCarte.equalsIgnoreCase("Sorcery")) {
			listeRituels.add(carte);

		} else if (typeCarte.equalsIgnoreCase("Land")) {
			listeTerrains.add(carte);

		} else if (typeCarte.equalsIgnoreCase("Planeswalker")) {
			listeArpenteurs.add(carte);

		} else if (typeCarte.equalsIgnoreCase("Instant")) {
			listeEphemeres.add(carte);

		}		

		sortAllNom();
	}

	/**
	 * 
	 * @param carte
	 */
	public void retirerCarte(Carte carte) {
		String typeCarte = getTypePrincipal(carte);

		if (typeCarte.equals("Artifact")) {
			removeCarte(listeArtefacts, carte);

		} else if (typeCarte.equals("Creature")) {
			removeCarte(listeCreatures, carte);

		} else if (typeCarte.equals("Enchantment")) {
			removeCarte(listeEnchantements, carte);

		} else if (typeCarte.equals("Sorcery")) {
			removeCarte(listeRituels, carte);

		} else if (typeCarte.equals("Land")) {
			removeCarte(listeTerrains, carte);

		} else if (typeCarte.equals("Planeswalker")) {
			removeCarte(listeArpenteurs, carte);

		} else if (typeCarte.equals("Instant")) {
			removeCarte(listeEphemeres, carte);

		}	
		sortAllNom();
	}
	
	private void removeCarte(ArrayList<Carte> liste, Carte carte) {
		
		boolean present = false;
		for (int i=0; i<liste.size() && !present; i++) {
			Carte carteListe = liste.get(i);
			present = carte.equals(carteListe);
			if (present) {
				liste.remove(i);
			}
		}
		
	}

	/**
	 * Renvoit la liste du deck sous forme d'une chaine de caractère
	 */
	public String toStringName() {

		StringBuilder deckCre = new StringBuilder();
		StringBuilder deckEph = new StringBuilder();
		StringBuilder deckRit = new StringBuilder();
		StringBuilder deckEnc = new StringBuilder();
		StringBuilder deckArt = new StringBuilder();
		StringBuilder deckArp = new StringBuilder();
		StringBuilder deckTer = new StringBuilder();

		// Réinit de la liste pour la mettre à jour
		liste = new ArrayList<>();
		String enTete = "Nombre de cartes : " + nbCarte() + "<br/>";
		deckCre = listeToString(listeCreatures, "Cr&eacute;atures");
		deckEph = listeToString(listeEphemeres, "Eph&eacute;m&egrave;res");
		deckRit = listeToString(listeRituels, "Rituels");
		deckEnc = listeToString(listeEnchantements, "Enchantements");
		deckArt = listeToString(listeArtefacts, "Artefacts");
		deckArp = listeToString(listeArpenteurs, "Arpenteurs");
		deckTer = listeToString(listeTerrains, "Terrains");


		return enTete+deckCre.append(deckEph).append(deckRit).append(deckEnc).append(deckArt).append(deckArp).append(deckTer).toString();
	}

	/**
	 * Renvoie un StringBuilder contenant 
	 * @param liste
	 * @param titre
	 * @param sb
	 * @return
	 */
	private StringBuilder listeToString(ArrayList<Carte>liste, String titre) {
		StringBuilder sb = new StringBuilder();
		if (!liste.isEmpty()) {
			sb.append("<b>" + titre + " ("+ liste.size() + ")</b><br/>");
			
			// Tableau utilisé lors du clic sur une zone de texte comportant une liste de carte d'un deck
			this.liste.add(titre);

			// Nombre de fois où la carte est présente dans la liste
			int nbCarte = 1;
			for(int i = 0; i<liste.size(); i++) {
				Carte carte = liste.get(i);
				Carte cartePlus1 = new Carte();
				if (i+1 < liste.size()) {
					cartePlus1 = liste.get(i+1);
				}
				if (cartePlus1.getName().equals(carte.getName())) {
					nbCarte++;
				} else {
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;"+ nbCarte + "&nbsp;" + carte.getName() + "<br/>");
					this.liste.add(carte.getName());
					nbCarte = 1;
				}
			}

			sb.append("<br/>");
			this.liste.add("");
		}

		return sb;
	}

	/**
	 * Renvoie le type principale de la carte
	 * @param carte
	 * @return
	 */
	private String getTypePrincipal(Carte carte) {
		String typeCarte = "";
		StringBuilder type = new StringBuilder(carte.getType());
		if (type.toString().contains("-")) {
			type.delete(type.indexOf("-"), type.length());
		}
		StringTokenizer tokens = new StringTokenizer(type.toString(), " ");
		while(tokens.hasMoreTokens()) {
			typeCarte = tokens.nextToken();
		}

		return typeCarte;
	}

	/**
	 * 
	 */
	private	void sortAllNom() {
		sortListeNom(listeArpenteurs);
		sortListeNom(listeCreatures);
		sortListeNom(listeEphemeres);
		sortListeNom(listeRituels);
		sortListeNom(listeEnchantements);
		sortListeNom(listeArtefacts);
		sortListeNom(listeTerrains);

	}

	/**
	 * Tri de la liste de carte passée en paramètre
	 * @param listeATrier
	 */
	private void sortListeNom(ArrayList<Carte> listeATrier) {

		// Double parcours de la liste
		// On parcourt la liste autant de fois qu'il y a d'élements dedans
		for (int i=0; i<listeATrier.size();i++) {
			// Parcours de la liste jusqu'à l'avant dernier élément
			for (int j=0; j<listeATrier.size()-i-1;j++) {
				Carte carteJ = listeATrier.get(j);
				Carte carteJPlus1 = new Carte();
				if (j+1 < listeATrier.size()) {
					carteJPlus1 = listeATrier.get(j+1);
				}
				String nomCarte = carteJ.getName();
				// Si le nom de la carte est situé plus loin dans l'ordre alphabétique que celle d'après, 
				// ces deux cartes sont permutées dans la liste
				if (nomCarte.compareTo(carteJPlus1.getName()) > 0) {
					Carte carteTempo = carteJ;
					listeATrier.set(j, carteJPlus1);
					listeATrier.set(j+1, carteTempo);
				}
			}
		}
	}

	public boolean isEmpty() {

		if (liste.isEmpty()) {
			return true;
		}

		return false;
	}
	
	public int nbCarte() {
		return listeCreatures.size() + listeArpenteurs.size() + listeArtefacts.size() + listeEnchantements.size() + listeEphemeres.size() + listeRituels.size() + listeTerrains.size();
	}


	/*********************************\
	 ****** Getters and Setters ******
	\*********************************/

	/**
	 * @return the liste
	 */
	public ArrayList<String> getListe() {
		return liste;
	}


	/**
	 * @param liste the liste to set
	 */
	public void setListe(ArrayList<String> liste) {
		this.liste = liste;
	}
	/**
	 * @return the listeArtefacts
	 */
	public ArrayList<Carte> getListeArtefacts() {
		return listeArtefacts;
	}


	/**
	 * @param listeArtefacts the listeArtefacts to set
	 */
	public void setListeArtefacts(ArrayList<Carte> listeArtefacts) {
		this.listeArtefacts = listeArtefacts;
	}


	/**
	 * @return the listeCreatures
	 */
	public ArrayList<Carte> getListeCreatures() {
		return listeCreatures;
	}


	/**
	 * @param listeCreatures the listeCreatures to set
	 */
	public void setListeCreatures(ArrayList<Carte> listeCreatures) {
		this.listeCreatures = listeCreatures;
	}


	/**
	 * @return the listeEnchantements
	 */
	public ArrayList<Carte> getListeEnchantements() {
		return listeEnchantements;
	}


	/**
	 * @param listeEnchantements the listeEnchantements to set
	 */
	public void setListeEnchantements(ArrayList<Carte> listeEnchantements) {
		this.listeEnchantements = listeEnchantements;
	}


	/**
	 * @return the listeEphemeres
	 */
	public ArrayList<Carte> getListeEphemeres() {
		return listeEphemeres;
	}


	/**
	 * @param listeEphemeres the listeEphemeres to set
	 */
	public void setListeEphemeres(ArrayList<Carte> listeEphemeres) {
		this.listeEphemeres = listeEphemeres;
	}


	/**
	 * @return the listeRituels
	 */
	public ArrayList<Carte> getListeRituels() {
		return listeRituels;
	}


	/**
	 * @param listeRituels the listeRituels to set
	 */
	public void setListeRituels(ArrayList<Carte> listeRituels) {
		this.listeRituels = listeRituels;
	}


	/**
	 * @return the listeArpenteurs
	 */
	public ArrayList<Carte> getListeArpenteurs() {
		return listeArpenteurs;
	}


	/**
	 * @param listeArpenteurs the listeArpenteurs to set
	 */
	public void setListeArpenteurs(ArrayList<Carte> listeArpenteurs) {
		this.listeArpenteurs = listeArpenteurs;
	}


	/**
	 * @return the listeTerrains
	 */
	public ArrayList<Carte> getListeTerrains() {
		return listeTerrains;
	}


	/**
	 * @param listeTerrains the listeTerrains to set
	 */
	public void setListeTerrains(ArrayList<Carte> listeTerrains) {
		this.listeTerrains = listeTerrains;
	}


	/**
	 * @return the tabCarteLigne
	 */
	public String[] getTabCarteLigne() {
		return tabCarteLigne;
	}


	/**
	 * @param tabCarteLigne the tabCarteLigne to set
	 */
	public void setTabCarteLigne(String[] tabCarteLigne) {
		this.tabCarteLigne = tabCarteLigne;
	}
}
