package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.datazuul.commons.cms.domain.Category;

import de.javapro.netcms.frontend.wicket.pages.EditSubcategoriesPage;
import de.javapro.netcms.frontend.wicket.pages.NewCategoryPage;

public class ActionsSubcategoriesPanel extends Panel {

    public ActionsSubcategoriesPanel(final String id, final IModel model) {
	super(id, model);

	// edit link for list
	final IPageLink iLnkEditSubcategories = new IPageLink() {
	    public Page getPage() {
		final Category category = (Category) getDefaultModelObject();
		return new EditSubcategoriesPage(category);
	    }

	    public Class getPageIdentity() {
		return EditSubcategoriesPage.class;
	    }
	};
	final PageLink lnkEditSubcategories = new PageLink("lnkEditSubcategories", iLnkEditSubcategories);
	add(lnkEditSubcategories);

	// new subcategory link
	final IPageLink iLnkNewSubcategory = new IPageLink() {
	    public Page getPage() {
		final Category category = (Category) getDefaultModelObject();
		return new NewCategoryPage(category);
	    }

	    public Class getPageIdentity() {
		return NewCategoryPage.class;
	    }
	};
	final PageLink lnkNewSubcategory = new PageLink("lnkNewSubcategory", iLnkNewSubcategory);
	add(lnkNewSubcategory);
    }

}
