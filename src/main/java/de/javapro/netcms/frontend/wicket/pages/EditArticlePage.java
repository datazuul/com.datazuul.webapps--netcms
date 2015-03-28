package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import com.datazuul.commons.cms.domain.Article;
import com.datazuul.commons.cms.domain.Category;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.forms.ArticleEditForm;

/**
 * @author ralf
 * 
 */
public class EditArticlePage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(EditArticlePage.class);

    /**
     * Default constructor
     */
    public EditArticlePage() {
    }

    public EditArticlePage(final Category pCategory) {
	final Article article = new Article();
	article.setCategory(pCategory);
	buildPage(article);
    }

    public EditArticlePage(final Article pArticle) {
	buildPage(pArticle);
    }

    /**
     * @param article
     * 
     */
    private void buildPage(final Article article) {
	LOG.enterPage();

	// subcategories as links for navigation
	final WebMarkupContainer subcategories = new WebMarkupContainer("subcategories");
	subcategories.setVisible(false);
	add(subcategories);

	add(new Label("lblArticleTitle", article.getTitle()));
	add(new ArticleEditForm("articleInputForm", article));
    }
}
