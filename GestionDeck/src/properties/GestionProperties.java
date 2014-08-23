package properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GestionProperties {
	
	public static Properties load(String filename) throws IOException, FileNotFoundException{
		Properties properties = new Properties();
		InputStream input =
				GestionProperties.class.getResourceAsStream(filename);
		try{
			properties.load(input);
			return properties;
		}
		finally{
			input.close();
		}
	}
}
