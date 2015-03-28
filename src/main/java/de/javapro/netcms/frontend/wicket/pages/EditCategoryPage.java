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
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.SignInSession;

public class EditCategoryPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(EditCategoryPage.class);

    public EditCategoryPage(final PageParameters params) {
	super();
	long categoryId = 1;
	if (params != null) {
	    if (params.containsKey("id")) {
		try {
		    categoryId = params.getLong("id");
		} catch (final StringValueConversionException e) {

		    // invalid id show error message
		    // TODO
		}
	    }
	}

	buildPage(categoryId);
    }

    public EditCategoryPage(final Category pCategory) {
	buildPage(pCategory.getId());
    }

    /**
     * @param categoryId
     */
    private void buildPage(long categoryId) {
	LOG.enterPage();

	if (categoryId < 1) {
	    categoryId = 1;
	}
	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final NetCMSRepository repository = NetCMSRepository.getInstance();
	final Category category = repository.getCategoryById(dn, categoryId);

	// category name
	add(new Label("lblCategoryName", category.getName()));

	// edit category name
	final Form formCategory = new Form("formCategory", new CompoundPropertyModel(category)) {
	    @Override
	    protected void onSubmit() {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		final Category category = (Category) getModelObject();
		Page next = null;
		NetCMSRepository.getInstance().save(dn, category);

		// when saving was successful, index category
		NetCMSRepository.getInstance().index(dn, category);

		next = new ViewCategoryPage(category);
		setResponsePage(next);
	    }
	};

	// text field for name
	final TextField txtCategoryName = new TextField("name");
	formCategory.add(txtCategoryName);

	// cancel button
	final Button btnCancel = new Button("btnCancel") {
	    @Override
	    public void onSubmit() {
		setResponsePage(new ViewCategoryPage((Category) getParent().getDefaultModelObject()));
	    }
	};
	btnCancel.setDefaultFormProcessing(false);
	formCategory.add(btnCancel);

	// save button
	final Button btnSaveCategory = new Button("btnSaveCategory");
	formCategory.add(btnSaveCategory);

	add(formCategory);

    }
}
