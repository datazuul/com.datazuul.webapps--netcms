package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.datazuul.commons.cms.domain.Category;

import de.javapro.netcms.frontend.wicket.pages.EditArticlePage;
import de.javapro.netcms.frontend.wicket.pages.EditArticlesPage;

public class ActionsArticlesPanel extends Panel {

    public ActionsArticlesPanel(final String id, final IModel model) {
	super(id, model);

	// edit link for list
	final IPageLink iLnkEditArticles = new IPageLink() {
	    public Page getPage() {
		final Category category = (Category) getDefaultModelObject();
		return new EditArticlesPage(category);
	    }

	    public Class getPageIdentity() {
		return EditArticlesPage.class;
	    }
	};
	final PageLink lnkEditArticles = new PageLink("lnkEditArticles", iLnkEditArticles);
	add(lnkEditArticles);

	// new article link
	final IPageLink iLnkNewArticle = new IPageLink() {
	    public Page getPage() {
		final Category category = (Category) getDefaultModelObject();
		return new EditArticlePage(category);
	    }

	    public Class getPageIdentity() {
		return EditArticlePage.class;
	    }
	};
	final PageLink lnkNewArticle = new PageLink("lnkNewArticle", iLnkNewArticle);
	add(lnkNewArticle);
    }
}
