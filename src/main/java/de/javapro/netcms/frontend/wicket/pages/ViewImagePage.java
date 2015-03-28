package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.FileResource;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.webapps.wicket.components.imagepopup.ImagePopupLink;

/**
 * @author ralf
 * 
 */
public class ViewImagePage extends AppBasePage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(ViewImagePage.class);

    /**
     * Default constructor
     */
    public ViewImagePage() {
    }

    public ViewImagePage(final Image pImage) {
	LOG.enterPage();

	setDefaultModel(new CompoundPropertyModel(pImage));

	// link to original
	final ImagePopupLink lnkOriginal = new ImagePopupLink("lnkOriginal", new Model(pImage));
	add(lnkOriginal);

	// preview image
	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final org.apache.wicket.markup.html.image.Image preview = new org.apache.wicket.markup.html.image.Image(
		"imgPreview", new FileResource(NetCMSRepository.getInstance().getPreviewFile(dn, pImage)));
	if (pImage.getTitle() != null) {
	    preview.add(new SimpleAttributeModifier("alt", pImage.getTitle()));
	}
	lnkOriginal.add(preview);

	// title
	add(new Label("lblTitle", pImage.getTitle()));

	add(new Label("title"));
	add(new Label("description"));
    }
}
