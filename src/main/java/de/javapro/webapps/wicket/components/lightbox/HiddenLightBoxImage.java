package de.javapro.webapps.wicket.components.lightbox;

/**
 * Encapsulates an url to an image to be included in lightbox-slideshow.
 * 
 * @author Uwe Schäfer, (uwe@codesmell.org), Ralf Eichinger
 * 
 */
public class HiddenLightBoxImage extends AbstractBoxImage {
    private static final long serialVersionUID = 1L;

    /**
     * @param urlToFullSizeImage
     *            the url to the image to be included in the lightbox slideshow
     */
    public HiddenLightBoxImage(final String urlToFullSizeImage) {
	super(urlToFullSizeImage);
    }
}
