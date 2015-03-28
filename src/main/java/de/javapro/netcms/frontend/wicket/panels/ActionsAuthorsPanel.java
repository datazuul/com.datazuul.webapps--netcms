package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.datazuul.commons.cms.domain.Author;

import de.javapro.netcms.frontend.wicket.pages.EditAuthorPage;
import de.javapro.netcms.frontend.wicket.pages.EditAuthorsPage;

public class ActionsAuthorsPanel extends Panel {
    public ActionsAuthorsPanel(final String id) {
	super(id);

	// edit link for list
	final IPageLink iLnkEditList = new IPageLink() {
	    public Page getPage() {
		return new EditAuthorsPage();
	    }

	    public Class getPageIdentity() {
		return EditAuthorsPage.class;
	    }
	};
	final PageLink lnkEditList = new PageLink("lnkEdit", iLnkEditList);
	add(lnkEditList);

	// new list entry link
	final IPageLink iLnkNewItem = new IPageLink() {
	    public Page getPage() {
		return new EditAuthorPage(new Author());
	    }

	    public Class getPageIdentity() {
		return EditAuthorPage.class;
	    }
	};
	final PageLink lnkNewItem = new PageLink("lnkNew", iLnkNewItem);
	add(lnkNewItem);
    }
}