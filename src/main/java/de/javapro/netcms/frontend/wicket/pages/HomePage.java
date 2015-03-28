package de.javapro.netcms.frontend.wicket.pages;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Article;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.NetCMSApplication;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.forms.SearchForm;
import de.javapro.netcms.frontend.wicket.panels.CategoryNavigatorPanel;
import de.javapro.webapps.wicket.components.lightbox.HiddenLightBoxImage;
import de.javapro.webapps.wicket.components.lightbox.LightBox2Config;
import de.javapro.webapps.wicket.components.lightbox.LightBox2Panel;
import de.javapro.webapps.wicket.components.lightbox.LightBoxImage;

public class HomePage extends AppBasePage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(HomePage.class);

    public HomePage() {
	super();
	buildPage();
    }

    private void buildPage() {
	LOG.enterPage();

	final NetCMSRepository repository = NetCMSRepository.getInstance();
	// DomainName dn = ((SignInSession) getSession()).getDomainName();
	final DomainName dn = ((NetCMSApplication) getApplication()).getDomainName();

	// search form
	final SearchForm searchForm = new SearchForm("searchForm", "Suche");
	searchForm.targetGroup.setVisible(false);
	add(searchForm);

	// The homepage is the category with id = 1
	final Category category = repository.getCategoryById(dn, 1);

	String categoryName = "not found";
	if (category == null) {
	    // invalid id show error message
	    // TODO
	} else {
	    categoryName = category.getName();
	}

	// <div id="categoryNavigator">
	// <span wicket:id="pnlCategoryNavigator"/>
	// </div>

	// Panel pnlCategoryNavigator = new CategoryNavigatorPanel(
	// "pnlCategoryNavigator", category, null, false);
	// add(pnlCategoryNavigator);

	// show all articles
	// <span wicket:id="articles">
	// <div class="article editable">
	// <span wicket:id="pnlActionsArticle"/>
	// <h1 class="first"><span
	// wicket:id="lblArticleTitle">[Artikel-Titel]</span></h1>
	// <p class="content"><span
	// wicket:id="lblArticleContent">[Artikel-Inhalt]</span></p>
	// </div>
	// </span>

	// List articles = category.getArticles();
	// ListView articleListView = new ListView("articles", articles) {
	// public void populateItem(final ListItem listItem) {
	// Article article = (Article) listItem.getModelObject();
	// NetCMSRepository repository = NetCMSRepository.getInstance();
	// article = repository.getArticleById(article.getId());
	//
	// // title
	// listItem.add(new Label("lblArticleTitle", article.getTitle()));
	//
	// // content
	// String html = article.getHtmlContent();
	// Label lblArticleContent = new Label("lblArticleContent", html);
	// lblArticleContent.setEscapeModelStrings(false);
	// listItem.add(lblArticleContent);
	//
	// // actions article
	// ActionsArticlePanel pnlActionsArticle = new ActionsArticlePanel(
	// "pnlActionsArticle", listItem.getModel());
	// SignInSession session = (SignInSession) getSession();
	// User user = session.getUser();
	// if (user == null) {
	// pnlActionsArticle.setVisible(false);
	// }
	// listItem.add(pnlActionsArticle);
	// }
	// };
	// add(articleListView);

	// show latest 5 images
	final List<LightBoxImage> list = new LinkedList<LightBoxImage>();
	final List images = NetCMSRepository.getInstance().getLatest(dn, 5, Image.class);
	for (final Iterator iterator = images.iterator(); iterator.hasNext();) {
	    Image img = (Image) iterator.next();
	    img = repository.getImageById(dn, img.getId());

	    // thumbnail
	    final String webappContext = WebApplication.get().getServletContext().getContextPath();
	    final LightBoxImage lbi = new LightBoxImage(webappContext + "/image/id/" + img.getId(), webappContext
		    + "/image/size/thumbnail/id/" + img.getId(), img.getTitle(), img.getId());
	    list.add(lbi);
	}
	final List<HiddenLightBoxImage> hiddenOnes = new LinkedList<HiddenLightBoxImage>();
	add(new LightBox2Panel("pnlGallery", new Model(new LightBox2Config(list, hiddenOnes))));

	// show latest 5 articles
	final List resultArticles = NetCMSRepository.getInstance().getLatest(dn, 5, Article.class);
	final ListView articleHitListView = new ListView("articleHits", resultArticles) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Article article = (Article) listItem.getModelObject();
		article = NetCMSRepository.getInstance().getArticleById(dn, article.getId());
		final Category parent = article.getCategory();

		// link to parent category
		final Link lnkViewCategory = new Link("lnkViewCategory") {
		    @Override
		    public void onClick() {
			final DomainName dn = ((SignInSession) getSession()).getDomainName();
			final Article hit = (Article) getParent().getDefaultModelObject();
			final Article article = NetCMSRepository.getInstance().getArticleById(dn, hit.getId());
			final Category parent = article.getCategory();
			setResponsePage(new ViewCategoryPage(parent));
		    }
		};
		listItem.add(lnkViewCategory);

		listItem.add(new Label("lblHitTitle", article.getTitle()));

		// highlightedText
		String html = article.getHtmlContent();
		if (html.length() > 250) {
		    html = html.substring(0, 250) + " ... ";
		}
		final Label lblHighlightedText = new Label("lblHighlightedText", html);
		lblHighlightedText.setEscapeModelStrings(false);
		listItem.add(lblHighlightedText);

		final Panel pnlCategoryNavigator = new CategoryNavigatorPanel("pnlCategoryNavigator", parent, null,
			true);
		listItem.add(pnlCategoryNavigator);
	    }
	};
	add(articleHitListView);

	// show all subcategories as links
	final List subcategories = category.getSubcategories();
	final ListView subcategoriesListView = new ListView("subcategories", subcategories) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Category subcategory = (Category) listItem.getModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		subcategory = repository.getCategoryById(dn, subcategory.getId());

		// link to subcategory
		final BookmarkablePageLink lnkViewCategory = new BookmarkablePageLink("lnkViewCategory",
			ViewCategoryPage.class);
		lnkViewCategory.setParameter("id", subcategory.getId());

		// Link lnkViewCategory = new Link("lnkViewCategory") {
		// public void onClick() {
		// Category subCategory = (Category) getParent()
		// .getDefaultModelObject();
		// NetCMSRepository repository = NetCMSRepository
		// .getInstance();
		// subCategory = repository.getCategoryById(subCategory
		// .getId());
		// SignInSession session = (SignInSession) getSession();
		// User user = session.getUser();
		// if (subCategory.getParent() == null) {
		// // FIXME: user = null/author
		// if (user == null) {
		// setResponsePage(new HomePage());
		// } else {
		// setResponsePage(new EditCategoryPage(
		// subCategory));
		// }
		// } else {
		// // FIXME: user = null/author
		// // if (user == null)
		// // {
		// // setResponsePage(new ViewCategoryPage(
		// // subCategory));
		// // } else
		// // {
		// // setResponsePage(new EditCategoryPage(
		// // subCategory, false));
		// // }
		// setResponsePage(new ViewCategoryPage(subCategory));
		// }
		// }
		// };
		lnkViewCategory.add(new Label("lblCategoryName", subcategory.getName()));
		listItem.add(lnkViewCategory);

		// // list of sub-subcategories
		// List subSubCategories = subcategory.getSubcategories();
		// if (subSubCategories != null) {
		// ListView subSubCategoriesListView = new ListView(
		// "subSubCategories", subSubCategories) {
		// public void populateItem(final ListItem listItem) {
		// Category subSubCategory = (Category) listItem
		// .getModelObject();
		// NetCMSRepository repository = NetCMSRepository
		// .getInstance();
		// subSubCategory = repository
		// .getCategoryById(subSubCategory.getId());
		//
		// // link to sub-subcategory
		// Link lnkViewSubCategory = new Link(
		// "lnkViewSubCategory") {
		// public void onClick() {
		// Category subSubCategory = (Category) getParent()
		// .getDefaultModelObject();
		// NetCMSRepository repository = NetCMSRepository
		// .getInstance();
		// subSubCategory = repository
		// .getCategoryById(subSubCategory
		// .getId());
		// SignInSession session = (SignInSession) getSession();
		// User user = session.getUser();
		// if (subSubCategory.getParent() == null) {
		// // FIXME: user = null/author
		// if (user == null) {
		// setResponsePage(new HomePage());
		// } else {
		// setResponsePage(new EditCategoryPage(
		// subSubCategory));
		// }
		// } else {
		// // FIXME: user = null/author
		// // if (user == null)
		// // {
		// // setResponsePage(new ViewCategoryPage(
		// // subSubCategory));
		// // } else
		// // {
		// // setResponsePage(new EditCategoryPage(
		// // subSubCategory, false));
		// // }
		// setResponsePage(new ViewCategoryPage(
		// subSubCategory));
		// }
		// }
		// };
		// String label = subSubCategory.getName();
		// // if (listItem.i)
		// lnkViewSubCategory.add(new Label(
		// "lblSubCategoryName", label));
		// listItem.add(lnkViewSubCategory);
		//
		// int index = listItem.getIndex();
		// int max = ((List) getModelObject()).size();
		// Label separator = new Label("separator", ", ");
		// if (index == (max - 1)) {
		// separator.setVisible(false);
		// }
		// listItem.add(separator);
		// }
		// };
		// listItem.add(subSubCategoriesListView);
		// }
	    }
	};
	add(subcategoriesListView);

	// put this category as "last viewed" one on user session
	final SignInSession session = (SignInSession) getSession();
	session.setLastViewedCategory(category);
    }
}
