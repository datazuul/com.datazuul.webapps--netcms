package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.string.StringValueConversionException;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Author;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.SignInSession;

public class EditAuthorPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(EditAuthorPage.class);

    public EditAuthorPage() {
    }

    public EditAuthorPage(final PageParameters params) {
	super();
	long id = 1;
	if (params != null) {
	    if (params.containsKey("id")) {
		try {
		    id = params.getLong("id");
		} catch (final StringValueConversionException e) {
		    // TODO invalid id show error message
		}
	    }
	}
	buildPage(id);
    }

    public EditAuthorPage(final Author pAuthor) {
	buildPage(pAuthor.getId());
    }

    private void buildPage(final long id) {
	LOG.enterPage();
	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final NetCMSRepository repository = NetCMSRepository.getInstance();
	Author author = repository.getAuthorById(dn, id);

	// title of page
	// name = surname, firstname(s)
	String name = "";
	if (author != null) {
	    name = author.getSurname();
	    if (author.getFirstname() != null) {
		name += ", " + author.getFirstname();
	    }
	} else {
	    author = new Author();
	}
	add(new Label("lblTitle", name));

	final Form form = new Form("form", new CompoundPropertyModel(author)) {
	    @Override
	    protected void onSubmit() {
		final Author author = (Author) getModelObject();
		Page next = null;
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		NetCMSRepository.getInstance().save(dn, author);

		// when saving was successful, index category
		NetCMSRepository.getInstance().index(dn, author);

		next = new ViewAuthorPage(author);
		setResponsePage(next);
	    }
	};

	final TextField txtSurname = new TextField("surname");
	form.add(txtSurname);

	final TextField txtFirstname = new TextField("firstname");
	form.add(txtFirstname);

	final TextField txtDayOfBirth = new TextField("dayOfBirth");
	form.add(txtDayOfBirth);

	final TextField txtDayOfDeath = new TextField("dayOfDeath");
	form.add(txtDayOfDeath);

	// cancel button
	final Button btnCancel = new Button("btnCancel") {
	    @Override
	    public void onSubmit() {
		setResponsePage(new AuthorsPage());
	    }
	};
	btnCancel.setDefaultFormProcessing(false);
	form.add(btnCancel);

	// save button
	final Button btnSave = new Button("btnSave");
	form.add(btnSave);

	add(form);
    }
}
