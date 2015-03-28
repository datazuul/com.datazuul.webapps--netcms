package de.javapro.netcms.frontend.wicket.panels;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.User;

import de.javapro.netcms.frontend.wicket.SignInSession;

public class RootCategoriesNavigatorPanel extends Panel {
    protected Class responsePage = null;

    public RootCategoriesNavigatorPanel(final String id, final Class responsePage, final boolean linkCurrentCategory) {
	super(id);

	// actions panel
	final ActionsRootCategoriesPanel pnlActions = new ActionsRootCategoriesPanel("pnlActions");
	final SignInSession session = (SignInSession) getSession();
	final User user = session.getUser();
	if (user == null) {
	    pnlActions.setVisible(false);
	}
	add(pnlActions);

	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final NetCMSRepository repository = NetCMSRepository.getInstance();
	final List rootCategories = repository.getRootCategories(dn);
	add(new CategoryNavigatorListView("rootCategoriesList", rootCategories, linkCurrentCategory));

	this.responsePage = responsePage;
    }

    protected class CategoryNavigatorListView extends ListView {
	private boolean linkCurrentCategory = false;

	public CategoryNavigatorListView(final String id, final List categories, final boolean linkCurrentCategory) {
	    super(id, categories);
	    this.linkCurrentCategory = linkCurrentCategory;
	}

	@Override
	protected void populateItem(final ListItem item) {
	    final DomainName dn = ((SignInSession) getSession()).getDomainName();
	    Category subcategory = (Category) item.getModelObject();
	    final NetCMSRepository repository = NetCMSRepository.getInstance();
	    subcategory = repository.getCategoryById(dn, subcategory.getId());

	    // link to subcategory
	    final BookmarkablePageLink lnkViewCategory = new BookmarkablePageLink("lnkViewCategory", responsePage);
	    lnkViewCategory.setParameter("id", subcategory.getId());
	    lnkViewCategory.add(new Label("lblCategoryName", subcategory.getName()));
	    item.add(lnkViewCategory);
	}

    }
}
