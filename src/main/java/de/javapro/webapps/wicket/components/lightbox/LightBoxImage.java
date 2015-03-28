package de.javapro.webapps.wicket.components.lightbox;

/**
 * Encapsulates the urls to preview and fullsize image as well an providing an
 * optional caption
 * 
 * @author Uwe Schäfer, (uwe@codesmell.org), Ralf Eichinger
 */
public class LightBoxImage extends AbstractBoxImage {
    private static final long serialVersionUID = 1L;
    private final String urlToThumbNail;
    private final String caption;
    private final long id;

    /**
     * @param urlToFullSizeImage
     *            the url to the Image to be included in the Lightbox slideshow
     * @param urlToThumbNail
     *            the url to the thumbnail-image be listed
     */
    public LightBoxImage(final String urlToFullSizeImage, final String urlToThumbNail, final long id) {
	this(urlToFullSizeImage, urlToThumbNail, null, id);
    }

    /**
     * @param urlToFullSizeImage
     *            the url to the Image to be included in the Lightbox slideshow
     * @param urlToThumbNail
     *            the url to the thumbnail-image be listed
     * @param caption
     *            an optional caption for display (can be null)
     */
    public LightBoxImage(final String urlToFullSizeImage, final String urlToThumbNail, final String caption,
	    final long id) {
	super(urlToFullSizeImage);
	this.urlToThumbNail = urlToThumbNail;
	this.caption = caption;
	this.id = id;
    }

    String getUrlToThumbNail() {
	return urlToThumbNail;
    }

    String getCaption() {
	return caption;
    }

    public long getId() {
	return this.id;
    }
}
