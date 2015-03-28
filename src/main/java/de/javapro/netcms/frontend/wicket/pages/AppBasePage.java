package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.Page;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PageLink;

import com.datazuul.commons.cms.domain.User;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.panels.RootCategoriesNavigatorPanel;
import de.javapro.netcms.frontend.wicket.panels.SiteLinksPanel;

public class AppBasePage extends WebPage {
    private static final ResourceReference CSS_EDIT_MODE = new ResourceReference(AppBasePage.class, "editMode.css");

    private static final ResourceReference CSS_VIEW_MODE = new ResourceReference(AppBasePage.class, "viewMode.css");

    public AppBasePage() {
	super();

	final SignInSession session = (SignInSession) getSession();

	// theme
	final String theme = session.getTheme();
	add(CSSPackageResource.getHeaderContribution("styles/" + theme + "/css/gallery.css"));
	add(CSSPackageResource.getHeaderContribution("styles/" + theme + "/css/style.css"));

	// edit/view mode specific css
	final User user = session.getUser();
	if (user != null) {
	    add(CSSPackageResource.getHeaderContribution(CSS_EDIT_MODE));
	} else {
	    add(CSSPackageResource.getHeaderContribution(CSS_VIEW_MODE));
	}

	// Login-Link
	final IPageLink iLnkLogin = new IPageLink() {
	    public Page getPage() {
		return new LoginPage();
	    }

	    public Class getPageIdentity() {
		return LoginPage.class;
	    }
	};
	final PageLink lnkLogin = new PageLink("lnkLogin", iLnkLogin);
	if (user != null) {
	    lnkLogin.setVisible(false);
	}
	add(lnkLogin);

	// Logout-Link
	final Link lnkLogout = new Link("lnkLogout") {
	    @Override
	    public void onClick() {
		final SignInSession session = (SignInSession) getSession();
		session.invalidate();
		final Class next = getApplication().getHomePage();
		setResponsePage(next);
	    }
	};
	if (user == null) {
	    lnkLogout.setVisible(false);
	}
	add(lnkLogout);

	// Link to homepage
	final BookmarkablePageLink lnkHome = new BookmarkablePageLink("lnkHome", HomePage.class);
	add(lnkHome);

	// top level navigation (root categories)
	final RootCategoriesNavigatorPanel pnlRootCategories = new RootCategoriesNavigatorPanel("pnlRootCategories",
		ViewCategoryPage.class, true);
	add(pnlRootCategories);

	// label logo
	final String siteTitle = session.getSiteTitle();
	final Label lblLogo = new Label("lblLogo", siteTitle);
	add(lblLogo);

	// site links navigation
	final SiteLinksPanel pnlSiteLinks = new SiteLinksPanel("pnlSiteLinks");
	add(pnlSiteLinks);

	// Link to authors
	// IPageLink iLnkAuthorsPage = new IPageLink() {
	// public Page getPage() {
	// return new AuthorsPage();
	// }
	//
	// public Class getPageIdentity() {
	// return AuthorsPage.class;
	// }
	// };
	// PageLink lnkAuthorsPage = new PageLink("lnkAuthorsPage",
	// iLnkAuthorsPage);
	// add(lnkAuthorsPage);
    }
}
