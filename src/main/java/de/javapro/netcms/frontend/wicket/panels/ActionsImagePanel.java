package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.pages.DeleteImagePage;
import de.javapro.netcms.frontend.wicket.pages.EditImagePage;

public class ActionsImagePanel extends Panel {

    public ActionsImagePanel(final String id, final Image image) {
	super(id, new Model(image));

	// edit link
	final Link lnkEdit = new Link("lnkEdit") {
	    @Override
	    public void onClick() {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Image image = (Image) getParent().getDefaultModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		image = repository.getImageById(dn, image.getId());
		setResponsePage(new EditImagePage(image));
	    }
	};
	add(lnkEdit);

	// delete link
	final Link lnkDelete = new Link("lnkDelete") {
	    @Override
	    public void onClick() {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Image image = (Image) getParent().getDefaultModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		image = repository.getImageById(dn, image.getId());
		setResponsePage(new DeleteImagePage(image));
	    }
	};
	add(lnkDelete);
    }
}