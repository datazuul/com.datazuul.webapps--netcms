package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import com.datazuul.commons.cms.domain.Category;

import de.javapro.framework.logging.LoggerFacade;

public class EditArticlesPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(EditArticlesPage.class);

    public EditArticlesPage(final Category category) {
	LOG.enterPage();

	// category name
	add(new Label("lblCategoryName", category.getName()));

	final Form formArticles = new Form("formArticles", new Model(category));

	// add new article button
	final Button btnAddArticle = new Button("btnAddArticle") {
	    @Override
	    public void onSubmit() {
		setResponsePage(new EditArticlePage((Category) getParent().getDefaultModelObject()));
	    }
	};
	btnAddArticle.setDefaultFormProcessing(false);
	formArticles.add(btnAddArticle);

	add(formArticles);

    }

}
