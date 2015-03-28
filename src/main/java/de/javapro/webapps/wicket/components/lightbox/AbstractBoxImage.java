package de.javapro.webapps.wicket.components.lightbox;

import java.io.Serializable;

/**
 * @author Uwe Schäfer, (uwe@codesmell.org), Ralf Eichinger
 */
abstract class AbstractBoxImage implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String urlToFullSizeImage;

    AbstractBoxImage(final String urlToFullSizeImage) {
	this.urlToFullSizeImage = urlToFullSizeImage;
    }

    String getUrlToFullSizeImage() {
	return urlToFullSizeImage;
    }
}
