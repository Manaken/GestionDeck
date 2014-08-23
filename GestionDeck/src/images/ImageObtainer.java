/*
 * ImageObtainer.java                       21 mars 2011
 * Functional class providing methods for pictures extracting.
 * 
 * 
 * Copyright 2011-2012 Julien ROUTABOUL
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package images;

import java.net.URL;

import javax.swing.ImageIcon;

/*
 *          VERSION HISTORY
 *            
 *  1.0 - 21/03/2011 - Julien ROUTABOUL : 
 *      Initial Release.
 *       
 */

/**
 * Functional class providing methods for pictures extracting. Those
 * pictures have to be in the same package as this class' one.
 * 
 * @author Julien ROUTABOUL
 * @version 1.0, 21/03/2011
 * @since 1.0
 */
public class ImageObtainer {

    
    /**
     * Get the URL of the picture pointed by the given name.
     * @param name Name of the picture to get.
     * @return The URL of the picture with the specified name
     */
    public static URL getURL(String name) {      
        return ImageObtainer.class.getResource(name);
    }
    
    /**
     * Get the object representation of the picture pointed 
     * by the given name.
     * @param name Name of the picture to get.
     * @return The object representation of the picture with 
     *         the specified name
     */
    public static ImageIcon getImageIcon(String name) {
        return new ImageIcon(ImageObtainer.class.getResource(name));
    }
    
}
