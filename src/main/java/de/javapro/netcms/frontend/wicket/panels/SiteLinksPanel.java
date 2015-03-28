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
import de.javapro.netcms.frontend.wicket.pages.ViewCategoryPage;

public class SiteLinksPanel extends Panel {

    public SiteLinksPanel(final String id) {
	super(id);

	// actions panel
	final ActionsSitePagesPanel pnlActions = new ActionsSitePagesPanel("pnlActions");
	final SignInSession session = (SignInSession) getSession();
	final User user = session.getUser();
	if (user == null) {
	    pnlActions.setVisible(false);
	}
	add(pnlActions);

	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final NetCMSRepository repository = NetCMSRepository.getInstance();
	final List categories = repository.getSiteCategories(dn);
	add(new CategoryNavigatorListView("linkList", categories));
    }

    protected class CategoryNavigatorListView extends ListView {
	public CategoryNavigatorListView(final String id, final List categories) {
	    super(id, categories);
	}

	@Override
	protected void populateItem(final ListItem item) {
	    final DomainName dn = ((SignInSession) getSession()).getDomainName();
	    Category category = (Category) item.getModelObject();
	    final NetCMSRepository repository = NetCMSRepository.getInstance();
	    category = repository.getCategoryById(dn, category.getId());

	    // link to category
	    final BookmarkablePageLink lnkViewCategory = new BookmarkablePageLink("lnkViewCategory",
		    ViewCategoryPage.class);
	    lnkViewCategory.setParameter("id", category.getId());
	    lnkViewCategory.add(new Label("lblCategoryName", category.getName()));
	    item.add(lnkViewCategory);
	}

    }
}
