package de.javapro.netcms.frontend.wicket.pages;

import java.util.List;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.backend.SearchResult;
import com.datazuul.commons.cms.domain.Article;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;
import com.datazuul.commons.cms.domain.SearchTarget;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.FileResource;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.forms.SearchForm;
import de.javapro.netcms.frontend.wicket.panels.CategoryNavigatorPanel;

public class ViewSearchResultsPage extends AppBasePage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(ViewSearchResultsPage.class);

    /**
     * @param query
     *            list of id's of the found articles
     * @param searchTarget
     *            index to search in
     */
    public ViewSearchResultsPage(final String query, final SearchTarget searchTarget) {
	LOG.enterPage();

	// search form
	final SearchForm searchForm = new SearchForm("searchForm", query);
	searchForm.targetGroup.setVisible(false);
	add(searchForm);

	List resultArticles = null;
	List resultCategories = null;
	List resultImages = null;

	final DomainName dn = ((SignInSession) getSession()).getDomainName();

	if (searchTarget.getId() == SearchTarget.CATEGORIES_AND_ARTICLES) {
	    resultArticles = NetCMSRepository.getInstance().search(dn, query, new Article());
	    resultCategories = NetCMSRepository.getInstance().search(dn, query, new Category());
	    resultImages = NetCMSRepository.getInstance().search(dn, query, new Image());

	    // result = Collections.unmodifiableList(new LazyLoaderList(result,
	    // NetCMSRepository.getInstance().getPm(), Article.class));

	    // show all category-hits as links to the category
	    final ListView categoryHitListView = new ListView("categoryHits", resultCategories) {
		@Override
		public void populateItem(final ListItem listItem) {
		    final DomainName dn = ((SignInSession) getSession()).getDomainName();
		    final SearchResult hit = (SearchResult) listItem.getModelObject();
		    final Category category = NetCMSRepository.getInstance().getCategoryById(dn,
			    hit.getHandle().longValue());

		    // link to category
		    final Link lnkViewCategory = new Link("lnkViewCategory") {
			@Override
			public void onClick() {
			    final DomainName dn = ((SignInSession) getSession()).getDomainName();
			    final SearchResult hit = (SearchResult) getParent().getDefaultModelObject();
			    final Category category = NetCMSRepository.getInstance().getCategoryById(dn,
				    hit.getHandle().longValue());
			    setResponsePage(new ViewCategoryPage(category));
			}
		    };
		    final Label lblCategoryName = new Label("lblCategoryName", hit.getHighlightedText());
		    lblCategoryName.setEscapeModelStrings(false);
		    lnkViewCategory.add(lblCategoryName);

		    listItem.add(lnkViewCategory);
		}
	    };
	    add(categoryHitListView);

	    // show all article-hits as links to the parent category
	    final ListView articleHitListView = new ListView("articleHits", resultArticles) {
		@Override
		public void populateItem(final ListItem listItem) {
		    final DomainName dn = ((SignInSession) getSession()).getDomainName();
		    final SearchResult hit = (SearchResult) listItem.getModelObject();
		    final Article article = NetCMSRepository.getInstance().getArticleById(dn,
			    hit.getHandle().longValue());
		    final Category parent = article.getCategory();

		    // link to parent category
		    final Link lnkViewCategory = new Link("lnkViewCategory") {
			@Override
			public void onClick() {
			    final DomainName dn = ((SignInSession) getSession()).getDomainName();
			    final SearchResult hit = (SearchResult) getParent().getDefaultModelObject();
			    final Article article = NetCMSRepository.getInstance().getArticleById(dn,
				    hit.getHandle().longValue());
			    final Category parent = article.getCategory();
			    setResponsePage(new ViewCategoryPage(parent));
			}
		    };
		    lnkViewCategory.add(new Label("lblHitTitle", article.getTitle()));
		    listItem.add(lnkViewCategory);

		    // highlightedText
		    final Label lblHighlightedText = new Label("lblHighlightedText", hit.getHighlightedText());
		    lblHighlightedText.setEscapeModelStrings(false);
		    listItem.add(lblHighlightedText);

		    final Panel pnlCategoryNavigator = new CategoryNavigatorPanel("pnlCategoryNavigator", parent, null,
			    true);
		    listItem.add(pnlCategoryNavigator);
		}
	    };
	    add(articleHitListView);

	    // show all image-hits as links to the image details
	    final ListView imageHitListView = new ListView("imageHits", resultImages) {
		@Override
		public void populateItem(final ListItem listItem) {
		    final DomainName dn = ((SignInSession) getSession()).getDomainName();
		    final SearchResult hit = (SearchResult) listItem.getModelObject();
		    final Image image = NetCMSRepository.getInstance().getImageById(dn, hit.getHandle().longValue());

		    // link to image detail
		    final Link lnkViewImage = new Link("lnkViewImage") {
			@Override
			public void onClick() {
			    final DomainName dn = ((SignInSession) getSession()).getDomainName();
			    final SearchResult hit = (SearchResult) getParent().getDefaultModelObject();
			    final Image image = NetCMSRepository.getInstance().getImageById(dn,
				    hit.getHandle().longValue());
			    setResponsePage(new ViewImagePage(image));
			}
		    };

		    // thumbnail of image
		    final org.apache.wicket.markup.html.image.Image thumbnail = new org.apache.wicket.markup.html.image.Image(
			    "imgThumbnail",
			    new FileResource(NetCMSRepository.getInstance().getThumbnailFile(dn, image)));
		    if (image.getTitle() != null) {
			thumbnail.add(new SimpleAttributeModifier("alt", image.getTitle()));
		    }
		    lnkViewImage.add(thumbnail);
		    listItem.add(lnkViewImage);

		    // title
		    final Label lblTitle = new Label("lblTitle", image.getTitle());
		    listItem.add(lblTitle);
		}
	    };
	    add(imageHitListView);
	}
    }
}
