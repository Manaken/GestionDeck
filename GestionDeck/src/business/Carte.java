package business;

import java.util.ArrayList;

import org.jdom.Element;

import single.Singleton;
import xml.XMLManager;

/**
 * Repr�sentation d'une carte magic
 * @author Ben
 *
 */
public class Carte {

	/**
	 * Nom de la carte
	 */
	private String name;

	/**
	 * Description de la carte
	 */
	private String text;

	/**
	 * Type de la carte
	 */
	private String type;

	/**
	 * Chemin de l'image
	 */
	private ImagesCartes set;

	/**
	 * Force et Endurance de la cr�ature
	 */
	private String pt;

	/**
	 * Co�t de la carte
	 */
	private String manacost;

	/**
	 * Couleur de la carte
	 */
	private String color;



	/**
	 * Constructeur utilisant tout les champs de la classe
	 * @param nomCarte
	 * @param description
	 * @param cout
	 * @param type
	 */
	public Carte() {
		super();
		this.name = "";
		this.text = "";
		this.type = "";

	}

	public Carte(Carte carte) {
		this.name = carte.name.replace('/', '-');
		this.text = carte.text;
		this.type = carte.type;
		this.set = carte.set;
		this.pt = carte.pt;
		this.manacost = carte.manacost;
		this.color = carte.color;
	}

	public Carte(String name, String text, String type, ImagesCartes set,
			String pt, String manacost, String color) {
		super();
		this.name = name.replace('/', '-');
		this.text = text;
		this.type = type;
		this.set = set;
		this.pt = pt;
		this.manacost = manacost;
		this.color = color;
	}

	public Carte(Element elt) {
		this.name = elt.getChild("name").getText().replace('/', '-');
		if (elt.getChild("color") != null) {
			this.color = elt.getChild("color").getText();
		}
		this.manacost = elt.getChild("manacost").getText();
		if (elt.getChild("pt") != null) { 
			this.pt = elt.getChild("pt").getText();
		}
		this.set = new ImagesCartes(elt);
		this.text = elt.getChild("text").getText();
		this.type = elt.getChild("type").getText();
	}

	public static Carte rechercheCarte(String name){
		Carte carte = null;

		XMLManager xmlManager = new XMLManager();

		carte = xmlManager.getCarte(name);

		return carte;

	}

	/**
	 * Renvoie une liste de carte commençant par la chaine passée en paramètre
	 * @param nomCarte Début du nom de la carte
	 * @param i La taille de la liste souhaitée
	 * @return
	 */
	public static ArrayList<String> getListeCarteSuggeree(String nomCarte, int i) {

		ArrayList<String> listeCarte = new ArrayList<>();
		XMLManager xmlManager = new XMLManager();
		listeCarte = xmlManager.getListeCarteSuggeree(nomCarte, i);

		return listeCarte;
	}

	public String toString() {
		return name;
	}

	/**
	 * Indique si la carte en paramètre est égale à this
	 * @param carte
	 * @return
	 */
	public boolean equals(Carte carte) {
		return carte.getName().equals(this.getName());
	}

	/**
	 * Renvoie le coût de la carte en int
	 * @return
	 */
	public int coutCarte() {
		int coutCarte = 0;

		String regexNormal = "[X]*[0-9]*[W]*[U]*[R]*[B]*[G]*";
		if (manacost.matches(regexNormal)) {
			for (int i=0; i<manacost.length(); i++) {
				Character caractere = manacost.charAt(i);
				if (caractere != 'X') {
					if (Character.isDigit(caractere)) {
						coutCarte += Character.getNumericValue(caractere);
					} else {
						coutCarte ++;
					}
				}
			}
		} else {
			for (int i=0; i<manacost.length(); i++) {
				Character caractere = manacost.charAt(i);
				if (caractere != 'X') {
					if (Character.isDigit(caractere)) {
						coutCarte += Character.getNumericValue(caractere);
					} else if(caractere == '('){
						coutCarte ++;
						i+=4;
					} else {
						coutCarte++;
					}
				}
			}
		}
		return coutCarte;
	}

	/*********************************\
	 ****** Getters and Setters ******
	\*********************************/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ImagesCartes getSet() {
		return set;
	}

	public void setSet(ImagesCartes set) {
		this.set = set;
	}

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public String getManacost() {
		return manacost;
	}

	public void setManacost(String manacost) {
		this.manacost = manacost;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public static void main(String[] args) {
		ArrayList<String>liste = (ArrayList<String>) Singleton.getInstance().getNomsCartes();
		for (String nomCarte :liste) {
			Carte carte = Carte.rechercheCarte(nomCarte);
			Singleton.getInstance().getImage(carte, 0);
		}
	}
}