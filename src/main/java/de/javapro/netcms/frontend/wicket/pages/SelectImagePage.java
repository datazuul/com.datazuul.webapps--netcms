package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.util.string.StringValueConversionException;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.panels.SelectImagePanel;

public class SelectImagePage extends wicket.extensions.markup.html.form.wysiwyg.SelectImagePage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(SelectImagePage.class);

    public SelectImagePage() {
	super();
	buildPage(0);
    }

    public SelectImagePage(final PageParameters params) {
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

    public SelectImagePage(final Category pCategory) {
	buildPage(pCategory.getId());
    }

    /**
     * @param categoryId
     */
    private void buildPage(long categoryId) {
	LOG.enterPage();

	// search form
	// Form searchForm = new SearchForm("searchForm");
	// add(searchForm);

	if (categoryId < 1) {
	    categoryId = 1;
	}
	final NetCMSRepository repository = NetCMSRepository.getInstance();
	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final Category category = repository.getCategoryById(dn, categoryId);

	// LinkTree tree = new LinkTree("tree", createTreeModel());
	// add(tree);

	add(new SelectImagePanel("pnlSelectImage", category));
    }

    @Override
    public int getHeight() {
	return 500;
    }

    @Override
    public int getWidth() {
	return 800;
    }
}
