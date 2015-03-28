package de.javapro.webapps.wicket.components.lightbox;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.Model;

/**
 * @author Uwe Schäfer, (uwe@codesmell.org), Ralf Eichinger
 */
class ExternalImageUrl extends WebComponent {
    private static final long serialVersionUID = 1L;

    public ExternalImageUrl(final String id, final String imageUrl) {
	super(id);
	add(new AttributeModifier("src", true, new Model(imageUrl)));
	setVisible(!((imageUrl == null) || imageUrl.equals("")));
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
	super.onComponentTag(tag);
	checkComponentTag(tag, "img");
    }
}
