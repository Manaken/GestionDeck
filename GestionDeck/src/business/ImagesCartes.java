package business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

public class ImagesCartes {

	public ImagesCartes() {
		super();
		// TODO Auto-generated constructor stub
	}

	private ArrayList<ImageCarte> lienImages;
	
	public ImagesCartes(Element elt) {
		lienImages = new ArrayList<ImageCarte>();
		@SuppressWarnings("unchecked")
		List<Element> liste = (List<Element>) elt.getChildren();
		
		Iterator<Element> itr = liste.iterator();
		while(itr.hasNext()) {
			Element courant = (Element) itr.next();
			if (courant.getName().equals("set")) {
				lienImages.add(new ImageCarte(courant.getAttributeValue("picURL"), courant.getText()));
			}
		}
	}

	public ArrayList<ImageCarte> getLienImages() {
		return lienImages;
	}

	public void setLienImages(ArrayList<ImageCarte> lienImages) {
		this.lienImages = lienImages;
	}
	
	
}
