package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.pages.DeleteCategoryPage;
import de.javapro.netcms.frontend.wicket.pages.EditCategoryPage;

public class ActionsCategoryPanel extends Panel {

    public ActionsCategoryPanel(final String id, final Category category) {
	super(id, new Model(category));

	// edit link
	final Link lnkEditCategory = new Link("lnkEdit") {
	    @Override
	    public void onClick() {
		setResponsePage(new EditCategoryPage((Category) getParent().getDefaultModelObject()));
	    }
	};
	add(lnkEditCategory);

	// delete link
	final Link lnkDelete = new Link("lnkDelete") {
	    @Override
	    public void onClick() {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Category category = (Category) getParent().getDefaultModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		category = repository.getCategoryById(dn, category.getId());
		setResponsePage(new DeleteCategoryPage(category));
	    }
	};
	add(lnkDelete);
    }
}
