package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;

import de.javapro.netcms.frontend.wicket.pages.NewSiteCategoryPage;

public class ActionsSitePagesPanel extends Panel {

    public ActionsSitePagesPanel(final String id) {
	super(id);

	// // edit link for list
	// IPageLink iLnkEdit = new IPageLink() {
	// public Page getPage() {
	// Category category = (Category) getDefaultModelObject();
	// return new EditRootCategoriesPage(category);
	// }
	//
	// public Class getPageIdentity() {
	// return EditRootCategoriesPage.class;
	// }
	// };
	// PageLink lnkEdit = new PageLink("lnkEdit", iLnkEdit);
	// add(lnkEdit);

	// new link
	final IPageLink iLnkNew = new IPageLink() {
	    public Page getPage() {
		return new NewSiteCategoryPage();
	    }

	    public Class getPageIdentity() {
		return NewSiteCategoryPage.class;
	    }
	};
	final PageLink lnkNew = new PageLink("lnkNew", iLnkNew);
	add(lnkNew);
    }

}
