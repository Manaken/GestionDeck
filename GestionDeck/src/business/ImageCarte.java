package business;

public class ImageCarte {

	
	private String edition;
	
	private String lienUrl;

	
	public ImageCarte () {
		
	}
	public ImageCarte(String lienUrl, String edition) {
		this.edition = edition;
		this.lienUrl = lienUrl;
	}
	
	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getLienUrl() {
		return lienUrl;
	}

	public void setLienUrl(String lienUrl) {
		this.lienUrl = lienUrl;
	}
}
