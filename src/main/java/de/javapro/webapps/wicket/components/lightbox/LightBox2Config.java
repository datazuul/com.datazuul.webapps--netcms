package de.javapro.webapps.wicket.components.lightbox;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * LightBoxConfig essentially contains two Lists of Images to be displayed. The
 * List of hidden images is optional.
 * 
 * @author Uwe Schäfer, (uwe@codesmell.org), Ralf Eichinger
 * 
 */
public class LightBox2Config implements Serializable {

    private final List<LightBoxImage> images;
    private final List<HiddenLightBoxImage> hidden;

    @SuppressWarnings("unchecked")
    public LightBox2Config(final List<LightBoxImage> images) {
	this(images, Collections.EMPTY_LIST);
    }

    /**
     * @param images2
     * @param hidden
     */
    public LightBox2Config(final List<LightBoxImage> images, final List<HiddenLightBoxImage> hidden) {
	this.images = images;
	this.hidden = hidden;
    }

    public List<LightBoxImage> getImages() {
	return images;
    }

    public List<HiddenLightBoxImage> getHidden() {
	return hidden;
    }
}
