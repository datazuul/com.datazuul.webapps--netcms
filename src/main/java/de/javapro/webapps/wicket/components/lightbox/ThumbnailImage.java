package de.javapro.webapps.wicket.components.lightbox;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;
import com.datazuul.commons.cms.domain.User;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.panels.ActionsImagePanel;

/**
 * @author Uwe Schäfer, (uwe@codesmell.org), Ralf Eichinger
 */
class ThumbnailImage extends Panel {
    ThumbnailImage(final String id, final String groupName, final LightBoxImage tb) {
	super(id);

	// actions image panel
	final long imageId = tb.getId();

	// FIXME: make independent of netCMS!
	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final NetCMSRepository repository = NetCMSRepository.getInstance();
	final Image img = repository.getImageById(dn, imageId);
	final ActionsImagePanel pnlActionsImage = new ActionsImagePanel("pnlActionsImage", img);
	final SignInSession session = (SignInSession) getSession();
	final User user = session.getUser();
	if (user == null) {
	    pnlActionsImage.setVisible(false);
	}
	add(pnlActionsImage);

	final String thumb = tb.getUrlToThumbNail();
	final String full = tb.getUrlToFullSizeImage();
	final String caption = tb.getCaption();
	setRenderBodyOnly(true);
	final ExternalLink externalLink = new ExternalLink("link", full);
	externalLink.add(new SimpleAttributeModifier("rel", "lightbox[" + groupName + "]"));
	if (caption != null) {
	    externalLink.add(new SimpleAttributeModifier("alt", caption));
	    externalLink.add(new SimpleAttributeModifier("title", caption));
	}
	add(externalLink);

	final ExternalImageUrl externalImageUrl = new ExternalImageUrl("image", thumb);
	externalLink.add(externalImageUrl);

	final Label lblImageTitle = new Label("lblImageTitle", caption);
	if (caption == null) {
	    lblImageTitle.setVisible(false);
	}
	add(lblImageTitle);
    }

    private static final long serialVersionUID = 1L;

}
