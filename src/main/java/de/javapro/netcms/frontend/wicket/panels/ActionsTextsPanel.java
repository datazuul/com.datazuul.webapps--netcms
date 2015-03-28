package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.datazuul.commons.cms.domain.Author;

import de.javapro.netcms.frontend.wicket.pages.EditTextPage;

public class ActionsTextsPanel extends Panel {

    public ActionsTextsPanel(final String id, final IModel model) {
	super(id, model);

	// edit link for list
	/*
	 * IPageLink iLnkEdit = new IPageLink() { public Page getPage() { Author
	 * author = (Author) getModelObject(); return new EditTextsPage(author);
	 * }
	 * 
	 * public Class getPageIdentity() { return EditTextsPage.class; } };
	 * PageLink lnkEdit = new PageLink("lnkEdit", iLnkEdit); add(lnkEdit);
	 */

	// new link
	final IPageLink iLnkNew = new IPageLink() {
	    public Page getPage() {
		final Author author = (Author) getDefaultModelObject();
		return new EditTextPage(author);
	    }

	    public Class getPageIdentity() {
		return EditTextPage.class;
	    }
	};
	final PageLink lnkNew = new PageLink("lnkNew", iLnkNew);
	add(lnkNew);
    }
}