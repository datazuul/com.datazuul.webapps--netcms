package de.javapro.netcms.frontend.wicket.panels;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Article;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.pages.EditArticlePage;

public class ActionsArticlePanel extends Panel {

    public ActionsArticlePanel(final String id, final IModel model) {
	super(id, model);

	// edit link
	final IPageLink iLnkEditArticle = new IPageLink() {
	    public Page getPage() {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Article article = (Article) getDefaultModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		article = repository.getArticleById(dn, article.getId());

		return new EditArticlePage(article);
	    }

	    public Class getPageIdentity() {
		return EditArticlePage.class;
	    }
	};
	final PageLink lnkEditArticle = new PageLink("lnkEditArticle", iLnkEditArticle);
	add(lnkEditArticle);
    }
}