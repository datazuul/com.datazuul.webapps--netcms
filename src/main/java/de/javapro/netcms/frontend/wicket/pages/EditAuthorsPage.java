package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import com.datazuul.commons.cms.domain.Author;

import de.javapro.framework.logging.LoggerFacade;

public class EditAuthorsPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(EditAuthorsPage.class);

    public EditAuthorsPage() {
	LOG.enterPage();

	final Form form = new Form("form");

	// add new button
	final Button btnAdd = new Button("btnAdd") {
	    @Override
	    public void onSubmit() {
		setResponsePage(new EditAuthorPage(new Author()));
	    }
	};
	btnAdd.setDefaultFormProcessing(false);
	form.add(btnAdd);
	add(form);
    }

}
