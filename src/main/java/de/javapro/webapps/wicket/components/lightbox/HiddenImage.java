package de.javapro.webapps.wicket.components.lightbox;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Uwe Schäfer, (uwe@codesmell.org), Ralf Eichinger
 */
class HiddenImage extends Panel {
    private static final long serialVersionUID = 1L;

    HiddenImage(final String id, final String groupName, final HiddenLightBoxImage tb) {
	super(id);
	setRenderBodyOnly(true);
	final ExternalLink externalLink = new ExternalLink("link", tb.getUrlToFullSizeImage());
	externalLink.add(new SimpleAttributeModifier("rel", "lightbox[" + groupName + "]"));
	add(externalLink);
    }
}
