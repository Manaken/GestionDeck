package single;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import properties.GestionProperties;
import business.Carte;


public class Singleton {
	/** L'instance statique */
	private static Singleton instance;

	private HashMap<String, Carte> cartes;

	private List<String> nomsCartes;

	private Properties prop;


	/** 
	 * Récupère l'instance unique de la class Singleton.<p>
	 * Remarque : le constructeur est rendu inaccessible
	 */
	public static Singleton getInstance() {
		if (null == instance) { // Premier appel
			instance = new Singleton();
		}
		return instance;
	}

	/** Constructeur redéfini comme étant privé pour interdire
	 * son appel et forcer à passer par la méthode
	 */
	@SuppressWarnings("unchecked")
	private Singleton() {
		try {			
			prop = GestionProperties.load("GestionDeck.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Document document = new Document();
		SAXBuilder sxb = new SAXBuilder();
		try {
			String ficCartes = prop.getProperty("ressources.xml.cartes");
			InputStream input =
					Singleton.class.getResourceAsStream(ficCartes);

			document = sxb.build(input);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Element racine = document.getRootElement();
		Element racineCartes = (Element) racine.getChild("cards");
		List<Element> cartesXml = (List<Element>) (racineCartes.getChildren());
		cartes = new HashMap<>();
		nomsCartes = new ArrayList<>();
		for (Element elt : cartesXml) {
			Carte carte = new Carte(elt);
			cartes.put(carte.getName().replace('/', '-'), carte);
			nomsCartes.add(carte.getName());
		}


	}

	/**
	 * Renvoie la liste des cartes sous forme d'un dictionnaire de Cartes
	 * @return
	 */
	public HashMap<String, Carte> getCartes() {
		return cartes;
	}

	public List<String> getNomsCartes() {
		return nomsCartes;
	}

	public ImageIcon getImage(Carte carte, int edition) {


		ImageIcon image = null;
		String dossierImage = prop.getProperty("ressources.dossier.images.cartes");
		if (existImage(carte.getName())) {
			try {
				BufferedImage buff = ImageIO.read(new File(dossierImage + carte.getName() + ".jpg"));
				image = new ImageIcon(buff);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String lien = carte.getSet().getLienImages().get(edition).getLienUrl();

			URL url = null;
			try {
				url = new URL(lien);
				image = new ImageIcon(url);
				if (image.getIconHeight() != -1) {
					try {

						File imageFichier = new File(dossierImage + carte.getName() + ".jpg");

						Image imageTempo = image.getImage();  
						RenderedImage rendered = null;  
						if (imageTempo instanceof RenderedImage)  
						{  
							rendered = (RenderedImage)imageTempo;  
						}  
						else  
						{  
							BufferedImage buffered = new BufferedImage(  
									image.getIconWidth(),  
									image.getIconHeight(),  
									BufferedImage.TYPE_INT_RGB  
									);  
							Graphics2D g = buffered.createGraphics();  
							g.drawImage(imageTempo, 0, 0, null);  
							g.dispose();  
							rendered = buffered;  
						}
						ImageIO.write(rendered,"jpg",imageFichier);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return image;
	}

	public Properties getProp() {
		return prop;
	}
	/**
	 * Vérifie si l'image est déjà sauvegardée
	 * @param name
	 * @return  true si la carte existe
	 * 			false sinon
	 */
	public boolean existImage(String name) {
		boolean exist = false;
		File repImages = new File(prop.getProperty("ressources.dossier.images.cartes"));
		if (!repImages.isDirectory()) {
			repImages.mkdirs();
		}
		File[] fichiersImages = repImages.listFiles();
		for (File ficImage : fichiersImages) {
			if (ficImage.getName().equalsIgnoreCase(name + ".jpg")) {
				exist = true;
				break;
			}
		}

		return exist;
	}

}
