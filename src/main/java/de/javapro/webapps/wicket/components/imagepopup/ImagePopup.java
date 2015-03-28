package de.javapro.webapps.wicket.components.imagepopup;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.image.Image;

/**
 * Displays an image using the provided image resource.
 * 
 * @author Eelco Hillenius
 */
public final class ImagePopup extends WebPage {
    /**
     * Construct.
     * 
     * @param imageResource
     *            the image resource to display
     */
    public ImagePopup(final WebResource imageResource) {
	super();
	add(new Image("image", imageResource));
    }
}