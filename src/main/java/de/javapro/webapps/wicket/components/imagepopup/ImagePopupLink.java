package de.javapro.webapps.wicket.components.imagepopup;

import org.apache.wicket.Page;
import org.apache.wicket.PageMap;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.resource.IResourceStream;

import com.datazuul.commons.cms.domain.Image;

import de.javapro.netcms.frontend.wicket.ByteArrayResourceStream;

/**
 * Link that displays a image in a popup window that resizes to fit.
 * 
 * @author Ralf Eichinger
 */
public final class ImagePopupLink extends Link {
    /**
     * Construct.
     * 
     * @param id
     *            component id
     * @param cdModel
     */
    public ImagePopupLink(final String id, final IModel model) {
	super(id, model);

	// custom popup settings that uses our automatic resize script
	final PopupSettings popupSettings = new PopupSettings(PageMap.forName("imagepopup"));
	popupSettings.setHeight(20);
	popupSettings.setWidth(20);
	setPopupSettings(popupSettings);
    }

    /**
     * @see wicket.markup.html.link.Link#onClick()
     */
    @Override
    public void onClick() {
	final WebResource imgResource = new WebResource() {
	    @Override
	    public IResourceStream getResourceStream() {
		final Image image = (Image) getModelObject();
		final String contentType = "image/" + image.getFormat();
		final ByteArrayResourceStream resourceStream = new ByteArrayResourceStream(image.getPropsOriginal()
			.getBytes(), contentType);
		return resourceStream;
	    }
	};
	getRequestCycle().setResponsePage(new ImagePopup(imgResource));
    }

    /**
     * @see wicket.markup.html.link.Link#linksTo(wicket.Page)
     */
    @Override
    protected boolean linksTo(final Page page) {
	// this is kind of ugly, but as isEnabled is marked final, this
	// is our best option; otherwise we would have to override render
	final Image image = (Image) getModelObject();
	return image.getPropsOriginal().getBytes() == null;
    }
}