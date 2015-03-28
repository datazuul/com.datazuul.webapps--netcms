package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.datazuul.commons.cms.domain.Category;

import de.javapro.netcms.frontend.wicket.pages.EditImagePage;
import de.javapro.netcms.frontend.wicket.pages.EditImagesPage;

public class ActionsImagesPanel extends Panel {

    public ActionsImagesPanel(final String id, final IModel model) {
	super(id, model);

	// edit link for list
	final IPageLink iLnkEdit = new IPageLink() {
	    public Page getPage() {
		final Category category = (Category) getDefaultModelObject();
		return new EditImagesPage(category);
	    }

	    public Class getPageIdentity() {
		return EditImagesPage.class;
	    }
	};
	final PageLink lnkEdit = new PageLink("lnkEdit", iLnkEdit);
	add(lnkEdit);

	// new link
	final IPageLink iLnkNew = new IPageLink() {
	    public Page getPage() {
		final Category category = (Category) getDefaultModelObject();
		return new EditImagePage(category);
	    }

	    public Class getPageIdentity() {
		return EditImagePage.class;
	    }
	};
	final PageLink lnkNew = new PageLink("lnkNew", iLnkNew);
	add(lnkNew);
    }
}
