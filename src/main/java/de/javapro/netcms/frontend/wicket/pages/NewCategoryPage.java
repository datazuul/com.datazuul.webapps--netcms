package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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
public class NewCategoryPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(NewCategoryPage.class);

    /**
     * Default constructor
     */
    private NewCategoryPage() {
    }

    public NewCategoryPage(final Category pCategory) {
	final Category category = new Category();
	category.setParent(pCategory);
	buildPage(category);
    }

    /**
     * @param category
     * 
     */
    private void buildPage(final Category category) {
	LOG.enterPage();

	// subcategories as links for navigation
	final WebMarkupContainer subcategories = new WebMarkupContainer("subcategories");
	subcategories.setVisible(false);
	add(subcategories);

	add(new Label("lblParentCategoryName", category.getParent().getName()));
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
		    final Category parent = ((Category) getParent().getDefaultModelObject()).getParent();
		    setResponsePage(new EditSubcategoriesPage(parent));
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
	    // first save category to get new id (parent already set in
	    // constructor of page)
	    NetCMSRepository.getInstance().save(dn, category);

	    // then save parent category, because the new category has been
	    // added as child
	    final Category parent = category.getParent();
	    parent.getSubcategories().add(category);
	    NetCMSRepository.getInstance().save(dn, parent);

	    // when saving was successful, index category
	    NetCMSRepository.getInstance().index(dn, category);

	    next = new ViewCategoryPage(category);
	    setResponsePage(next);
	}
    }
}
