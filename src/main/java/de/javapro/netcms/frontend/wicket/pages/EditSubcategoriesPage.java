package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import com.datazuul.commons.cms.domain.Category;

import de.javapro.framework.logging.LoggerFacade;

public class EditSubcategoriesPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(EditSubcategoriesPage.class);

    public EditSubcategoriesPage(final Category category) {
	LOG.enterPage();

	// category name
	add(new Label("lblCategoryName", category.getName()));

	final Form formSubcategories = new Form("formSubcategories", new Model(category));

	// add new subcategory button
	final Button btnAddSubcategory = new Button("btnAddSubcategory") {
	    @Override
	    public void onSubmit() {
		setResponsePage(new NewCategoryPage((Category) getParent().getDefaultModelObject()));
	    }
	};
	btnAddSubcategory.setDefaultFormProcessing(false);
	formSubcategories.add(btnAddSubcategory);

	add(formSubcategories);
    }
}
