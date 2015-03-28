package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Author;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.pages.EditAuthorPage;

public class ActionsAuthorPanel extends Panel {

    public ActionsAuthorPanel(final String id, final IModel model) {
	super(id, model);

	// edit link
	final IPageLink iLnkEdit = new IPageLink() {
	    public Page getPage() {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Author author = (Author) getDefaultModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		author = repository.getAuthorById(dn, author.getId());

		return new EditAuthorPage(author);
	    }

	    public Class getPageIdentity() {
		return EditAuthorPage.class;
	    }
	};
	final PageLink lnkEdit = new PageLink("lnkEdit", iLnkEdit);
	add(lnkEdit);
    }
}