package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import com.datazuul.commons.cms.domain.Category;

import de.javapro.framework.logging.LoggerFacade;

public class EditImagesPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(EditImagesPage.class);

    public EditImagesPage(final Category category) {
	LOG.enterPage();

	// category name
	add(new Label("lblCategoryName", category.getName()));

	final Form formImages = new Form("formImages", new Model(category));

	// add new article button
	final Button btnAdd = new Button("btnAdd") {
	    @Override
	    public void onSubmit() {
		setResponsePage(new EditImagePage((Category) getParent().getDefaultModelObject()));
	    }
	};
	btnAdd.setDefaultFormProcessing(false);
	formImages.add(btnAdd);

	add(formImages);

    }

}
