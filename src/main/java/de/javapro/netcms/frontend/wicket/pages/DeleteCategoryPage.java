package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import com.datazuul.commons.cms.business.CategoryService;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.SignInSession;

/**
 * @author ralf
 * 
 */
public class DeleteCategoryPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(DeleteCategoryPage.class);

    /**
     * Default constructor
     */
    public DeleteCategoryPage() {
    }

    public DeleteCategoryPage(final Category pCategory) {
	buildPage(pCategory);
    }

    /**
     * @param category
     * 
     */
    private void buildPage(final Category category) {
	LOG.enterPage();

	add(new Label("lblTitle", category.getName()));
	add(new FeedbackPanel("feedback"));

	final Form form = new Form("form", new Model(category));
	add(form);

	// no button
	final Button btnNo = new Button("btnNo") {
	    @Override
	    public void onSubmit() {
		final Category category = (Category) getParent().getDefaultModelObject();
		setResponsePage(new ViewCategoryPage(category));
	    }
	};
	btnNo.setDefaultFormProcessing(false);
	form.add(btnNo);

	// yes button
	final Button btnYes = new Button("btnYes") {
	    @Override
	    public void onSubmit() {
		final Category category = (Category) getParent().getDefaultModelObject();

		final CategoryService cs = new CategoryService();
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		cs.delete(dn, category, true);

		final Page next = new ViewCategoryPage(1);
		setResponsePage(next);
	    }
	};
	form.add(btnYes);
    }
}
