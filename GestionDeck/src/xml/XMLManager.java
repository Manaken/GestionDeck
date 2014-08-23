/**
 * 
 */
package xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import single.Singleton;

import business.Carte;
import business.Deck;

/**
 * @author Ben
 *
 */
public class XMLManager {

	/**
	 * Renvoie la carte dont le nom est passé en paramètre
	 * @param name
	 * @return
	 */
	public Carte getCarte(String name) {
		Carte carte = new Carte();
		List<String> listeCartes = (List<String>)Singleton.getInstance().getNomsCartes();
		
		Iterator<String> iterator = listeCartes.iterator();
		while (iterator.hasNext()) {
			String courant = (String) iterator.next();
			if (courant.equalsIgnoreCase(name)) {
				carte = (Carte)Singleton.getInstance().getCartes().get(courant);
				return carte;
			}
		}
		
		return null;
	}
	
	/**
	 * Renvoit le deck dont le nom est passé en paramètre
	 * @param nomDeck
	 * @return
	 */
	public Deck getDeck(String nomDeck) {
		Deck deck = new Deck();
		
		return deck;
	}

	/**
	 * Renvoie une liste de nom de carte dont le nom commence par la chaîne passée en paramètre
	 * La liste contient au maximum i noms de carte
	 * @param nomCarte Une chaine contenant le début du nom de cartes.
	 * @param i Le nombre maximum de nom de carte renvoyé
	 * @return
	 */
	public ArrayList<String> getListeCarteSuggeree(String nomCarte, int i) {
		ArrayList<String> listeNomCarte = new ArrayList<>();
		
		List<String> listeCartes = (List<String>)Singleton.getInstance().getNomsCartes();
		for (int j=0, nbCartes = 0; j<listeCartes.size() && nbCartes <= i; j++) {
			if (listeCartes.get(j).toLowerCase().startsWith(nomCarte.toLowerCase())) {
				listeNomCarte.add(listeCartes.get(j));
				nbCartes++;
			}
		}
		
		return listeNomCarte;
	}
	
	public static void main(String [] args) {
		XMLManager xmlManager = new XMLManager();
		
		Carte carte = xmlManager.getCarte("Scavenging Ooze");
		if (carte != null) {
			System.out.println(carte.getName());
		} else {
			System.out.println("Pas trouv�");
		}
		
		
		
		
	}

	
}
