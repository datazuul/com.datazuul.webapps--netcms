package de.javapro.netcms.frontend.wicket.panels;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.pages.HomePage;
import de.javapro.netcms.frontend.wicket.pages.ViewCategoryPage;

public class CategoryNavigatorPanel extends Panel {
    protected Class responsePage = null;

    public CategoryNavigatorPanel(final String id, Category pCategory, final Class responsePage,
	    final boolean linkCurrentCategory) {
	super(id);
	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final ArrayList categories = new ArrayList();
	final NetCMSRepository repository = NetCMSRepository.getInstance();
	pCategory = repository.getCategoryById(dn, pCategory.getId());
	categories.add(pCategory);
	Category parent = pCategory.getParent();
	while (parent != null) {
	    parent = repository.getCategoryById(dn, parent.getId());
	    categories.add(parent);
	    parent = parent.getParent();
	}
	// categories.add(parent);
	Collections.reverse(categories);
	add(new CategoryNavigatorListView("categoriesList", categories, linkCurrentCategory));

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
	    final Category category = (Category) item.getModelObject();
	    final int index = item.getIndex();
	    final int max = getModelObject().size();
	    item.add(new CategoryNavigatorComponent("category", category, index, max, responsePage, linkCurrentCategory));
	}

    }

    private static final class CategoryNavigatorComponent extends Panel {
	public CategoryNavigatorComponent(final String id, final Category pCategory, final int index, final int max,
		final Class responsePage, final boolean linkCurrentCategory) {
	    super(id);
	    add(new Label("sep", (index > 0) ? "&raquo;" : "").setEscapeModelStrings(false).setRenderBodyOnly(true));

	    Link lnkCategory = null;
	    if (responsePage == null) {
		if (pCategory.getParent() == null) {
		    lnkCategory = new BookmarkablePageLink("lnkCategory", HomePage.class);
		} else {
		    lnkCategory = new BookmarkablePageLink("lnkCategory", ViewCategoryPage.class).setParameter("id",
			    pCategory.getId());
		}
	    } else {

		lnkCategory = new Link("lnkCategory") {
		    @Override
		    public void onClick() {
			final Class[] argClasses = new Class[] { Category.class };
			try {
			    final Constructor constr = responsePage.getConstructor(argClasses);
			    final Category category = (Category) getDefaultModelObject();
			    final Object page = constr.newInstance(new Object[] { category });
			    setResponsePage((WebPage) page);
			} catch (final SecurityException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			} catch (final NoSuchMethodException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			} catch (final IllegalArgumentException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			} catch (final InstantiationException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			} catch (final IllegalAccessException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			} catch (final InvocationTargetException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		    }
		};
	    }
	    lnkCategory.add(new Label("label", pCategory.getName()));
	    if (index == (max - 1) && linkCurrentCategory == false) {
		lnkCategory.setEnabled(false);
	    }
	    add(lnkCategory);
	}
    }
}
