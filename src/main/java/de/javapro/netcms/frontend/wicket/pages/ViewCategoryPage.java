package de.javapro.netcms.frontend.wicket.pages;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.string.StringValueConversionException;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Article;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;
import com.datazuul.commons.cms.domain.User;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.forms.SearchForm;
import de.javapro.netcms.frontend.wicket.panels.ActionsArticlePanel;
import de.javapro.netcms.frontend.wicket.panels.ActionsArticlesPanel;
import de.javapro.netcms.frontend.wicket.panels.ActionsCategoryPanel;
import de.javapro.netcms.frontend.wicket.panels.ActionsImagesPanel;
import de.javapro.netcms.frontend.wicket.panels.ActionsSubcategoriesPanel;
import de.javapro.netcms.frontend.wicket.panels.CategoryNavigatorPanel;
import de.javapro.webapps.wicket.components.lightbox.HiddenLightBoxImage;
import de.javapro.webapps.wicket.components.lightbox.LightBox2Config;
import de.javapro.webapps.wicket.components.lightbox.LightBox2Panel;
import de.javapro.webapps.wicket.components.lightbox.LightBoxImage;

public class ViewCategoryPage extends AppBasePage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(ViewCategoryPage.class);

    public ViewCategoryPage() {
	super();
	buildPage(0);
    }

    public ViewCategoryPage(final PageParameters params) {
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

    public ViewCategoryPage(final long id) {
	buildPage(id);
    }

    public ViewCategoryPage(final Category pCategory) {
	buildPage(pCategory.getId());
    }

    /**
     * @param categoryId
     */
    private void buildPage(long categoryId) {
	LOG.enterPage();
	LOG.logInfo("id='" + categoryId + "'", "buildPage");

	// search form
	final SearchForm searchForm = new SearchForm("searchForm", "Suche");
	searchForm.targetGroup.setVisible(false);
	add(searchForm);

	if (categoryId < 1) {
	    categoryId = 1;
	}
	final NetCMSRepository repository = NetCMSRepository.getInstance();
	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final Category category = repository.getCategoryById(dn, categoryId);

	// category actions
	final ActionsCategoryPanel pnlActionsCategory = new ActionsCategoryPanel("pnlActionsCategory", category);
	final SignInSession session = (SignInSession) getSession();
	final User user = session.getUser();
	if (user == null) {
	    pnlActionsCategory.setVisible(false);
	}
	add(pnlActionsCategory);

	// String categoryName = "not found";
	// if (category == null) {
	// // invalid id show error message
	// // TODO
	// } else {
	// categoryName = category.getName();
	// }
	// Label lblCategoryName = new Label("lblCategoryName", categoryName);
	// add(lblCategoryName);

	final Panel pnlCategoryNavigator = new CategoryNavigatorPanel("pnlCategoryNavigator", category, null, false);
	add(pnlCategoryNavigator);

	// actions articles
	final ActionsArticlesPanel pnlActionsArticles = new ActionsArticlesPanel("pnlActionsArticles", new Model(
		category));
	if (user == null) {
	    pnlActionsArticles.setVisible(false);
	}
	add(pnlActionsArticles);

	// show all articles
	final List articles = category.getArticles();
	final ListView articleListView = new ListView("articles", articles) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Article article = (Article) listItem.getModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		article = repository.getArticleById(dn, article.getId());

		// title
		listItem.add(new Label("lblArticleTitle", article.getTitle()));

		// content
		final Label lblArticleContent = new Label("lblArticleContent", article.getHtmlContent());
		lblArticleContent.setEscapeModelStrings(false);
		listItem.add(lblArticleContent);

		// actions article panel
		final ActionsArticlePanel pnlActionsArticle = new ActionsArticlePanel("pnlActionsArticle",
			listItem.getModel());
		final SignInSession session = (SignInSession) getSession();
		final User user = session.getUser();
		if (user == null) {
		    pnlActionsArticle.setVisible(false);
		}
		listItem.add(pnlActionsArticle);

	    }
	};
	add(articleListView);

	// images section
	final List images = category.getImages();
	final WebMarkupContainer imagesSection = new WebMarkupContainer("imagesSection");
	if ((images.isEmpty() || images.size() == 0) && user == null) {
	    imagesSection.setVisible(false);
	}
	add(imagesSection);

	// actions images
	final ActionsImagesPanel pnlActionsImages = new ActionsImagesPanel("pnlActionsImages", new Model(category));
	if (user == null) {
	    pnlActionsImages.setVisible(false);
	}
	imagesSection.add(pnlActionsImages);

	// show all images
	// ListView imageListView = new ListView("images", images) {
	// public void populateItem(final ListItem listItem) {
	// Image image = (Image) listItem.getDefaultModelObject();
	// NetCMSRepository repository = NetCMSRepository.getInstance();
	// image = repository.getImageById(image.getId());
	//
	// ImagePopupLink lnkOriginal = new ImagePopupLink("lnkOriginal",
	// new Model(image));
	// listItem.add(lnkOriginal);
	//
	// // title of image
	// listItem.add(new Label("lblImageTitle", image.getTitle()));
	//
	// // thumbnail of image
	// org.apache.wicket.markup.html.image.Image thumbnail = new
	// org.apache.wicket.markup.html.image.Image(
	// "imgThumbnail", repository.getThumbnailResource(image));
	// if (image.getTitle() != null) {
	// thumbnail.add(new SimpleAttributeModifier("alt", image
	// .getTitle()));
	// }
	// lnkOriginal.add(thumbnail);
	//
	// // actions image panel
	// ActionsImagePanel pnlActionsImage = new ActionsImagePanel(
	// "pnlActionsImage", (Image) listItem
	// .getDefaultModelObject());
	// SignInSession session = (SignInSession) getSession();
	// User user = session.getUser();
	// if (user == null) {
	// pnlActionsImage.setVisible(false);
	// }
	// listItem.add(pnlActionsImage);
	// }
	// };
	// imagesSection.add(imageListView);

	// image gallery
	final List<LightBoxImage> list = new LinkedList<LightBoxImage>();
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

	imagesSection.add(new LightBox2Panel("pnlGallery", new Model(new LightBox2Config(list, hiddenOnes))));

	// show all subcategories as links
	final List subcategories = category.getSubcategories();
	final ListView subcategoriesListView = new ListView("subcategories", subcategories) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Category subcategory = (Category) listItem.getDefaultModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		subcategory = repository.getCategoryById(dn, subcategory.getId());
		final BookmarkablePageLink lnkViewCategory = new BookmarkablePageLink("lnkViewCategory",
			ViewCategoryPage.class);
		lnkViewCategory.setParameter("id", subcategory.getId());
		lnkViewCategory.add(new Label("lblCategoryName", subcategory.getName()));
		listItem.add(lnkViewCategory);
	    }
	};
	add(subcategoriesListView);

	// actions subcategories
	final ActionsSubcategoriesPanel pnlActionsSubcategories = new ActionsSubcategoriesPanel(
		"pnlActionsSubcategories", new Model(category));
	if (user == null) {
	    pnlActionsSubcategories.setVisible(false);
	}
	add(pnlActionsSubcategories);

	// put this category as "last viewed" one on user session
	session.setLastViewedCategory(category);
    }
}
