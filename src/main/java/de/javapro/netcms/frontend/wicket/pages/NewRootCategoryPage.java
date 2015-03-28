package de.javapro.netcms.frontend.wicket.pages;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.SignInSession;

/**
 * @author ralf
 * 
 */
public class NewRootCategoryPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(NewRootCategoryPage.class);

    public NewRootCategoryPage() {
	buildPage();
    }

    /**
     * @param category
     * 
     */
    private void buildPage() {
	LOG.enterPage();

	// subcategories as links for navigation
	final WebMarkupContainer subcategories = new WebMarkupContainer("subcategories");
	subcategories.setVisible(false);
	add(subcategories);

	final Category category = new Category();
	add(new CategoryInputForm("categoryInputForm", category));
    }

    public class CategoryInputForm extends Form {
	public CategoryInputForm(final String id, final Category category) {
	    super(id, new CompoundPropertyModel(category));

	    // text field for name
	    final TextField txtCategoryName = new TextField("name");
	    txtCategoryName.setRequired(true);
	    add(txtCategoryName);

	    // cancel button
	    final Button btnCancel = new Button("btnCancel") {
		@Override
		public void onSubmit() {
		    setResponsePage(HomePage.class);
		}
	    };
	    btnCancel.setDefaultFormProcessing(false);
	    add(btnCancel);
	}

	@Override
	public final void onSubmit() {
	    final DomainName dn = ((SignInSession) getSession()).getDomainName();
	    final Category category = (Category) getModelObject();
	    Page next = null;

	    final NetCMSRepository repository = NetCMSRepository.getInstance();

	    // first save category to get new id
	    repository.save(dn, category);

	    // then save as root category
	    final List<Category> rootCategories = repository.getRootCategories(dn);
	    rootCategories.add(category);
	    repository.saveRootCategories(dn, rootCategories);

	    // when saving was successful, index category
	    repository.index(dn, category);

	    next = new ViewCategoryPage(category);
	    setResponsePage(next);
	}
    }
}
