package de.javapro.netcms.frontend.wicket.pages;

import java.util.List;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.resource.ByteArrayResource;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Article;
import com.datazuul.commons.cms.domain.Author;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;
import com.datazuul.commons.cms.domain.Text;
import com.datazuul.commons.cms.domain.User;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.FileResource;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.panels.ActionsImagePanel;
import de.javapro.netcms.frontend.wicket.panels.ActionsTextsPanel;
import de.javapro.webapps.wicket.components.imagepopup.ImagePopupLink;

/**
 * @author ralf
 * 
 */
public class ViewAuthorPage extends AppBasePage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(ViewAuthorPage.class);

    /**
     * Default constructor
     */
    public ViewAuthorPage() {
    }

    public ViewAuthorPage(final Author author) {
	LOG.enterPage();
	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	setDefaultModel(new CompoundPropertyModel(author));

	// title of page
	// name = surname, firstname(s)
	String name = author.getSurname();
	if (author.getFirstname() != null) {
	    name += ", " + author.getFirstname();
	}
	add(new Label("lblTitle", name));

	Image imgPortrait = author.getImage();
	if (imgPortrait != null) {
	    final Fragment fragImage = new Fragment("pnlImage", "fragImage");
	    imgPortrait = NetCMSRepository.getInstance().getImageById(dn, imgPortrait.getId());
	    // link to original portrait image
	    final ImagePopupLink lnkPortraitOriginal = new ImagePopupLink("lnkPortraitOriginal", new Model(imgPortrait));
	    fragImage.add(lnkPortraitOriginal);

	    // preview image
	    final org.apache.wicket.markup.html.image.Image preview = new org.apache.wicket.markup.html.image.Image(
		    "imgPreview", new FileResource(NetCMSRepository.getInstance().getPreviewFile(dn, imgPortrait)));
	    if (imgPortrait.getTitle() != null) {
		preview.add(new SimpleAttributeModifier("alt", imgPortrait.getTitle()));
	    }
	    lnkPortraitOriginal.add(preview);

	    add(fragImage);
	} else {
	    final Fragment fragNoImage = new Fragment("pnlImage", "fragNoImage");
	    add(fragNoImage);
	}

	// surname
	add(new Label("lblSurname", author.getSurname()));

	// firstname(s)
	add(new Label("lblFirstname", author.getFirstname()));

	// day of birth
	add(new Label("lblDayOfBirth", author.getDayOfBirth()));

	// day of death
	add(new Label("lblDayOfDeath", author.getDayOfDeath()));

	// list of authors articles
	final List articles = author.getArticles();
	final ListView articleListView = new ListView("articleList", articles) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Article article = (Article) listItem.getModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		article = repository.getArticleById(dn, article.getId());
		Category category = article.getCategory();
		category = repository.getCategoryById(dn, category.getId());

		// link view and title
		final Link lnkView = new Link("lnkView") {
		    @Override
		    public void onClick() {
			final Article article = (Article) getParent().getDefaultModelObject();
			setResponsePage(new ViewCategoryPage(article.getCategory()));
		    }
		};
		lnkView.add(new Label("lblTitle", article.getTitle()));
		listItem.add(lnkView);

		// category of article
		listItem.add(new Label("lblCategoryTitle", category.getName()));
	    }
	};
	add(articleListView);

	// list of authors texts
	// list actionsText
	final ActionsTextsPanel pnlActionsTexts = new ActionsTextsPanel("pnlActionsTexts", new Model(author));
	final SignInSession session = (SignInSession) getSession();
	final User user = session.getUser();
	if (user == null) {
	    pnlActionsTexts.setVisible(false);
	}
	add(pnlActionsTexts);

	final List texts = author.getTexts();
	final ListView textListView = new ListView("textList", texts) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Text text = (Text) listItem.getModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		text = repository.getTextById(dn, text.getId());

		// link view and title
		final ResourceLink lnkView = new ResourceLink("lnkView", new ByteArrayResource("application/"
			+ text.getFormat(), text.getProperties().getBytes()));
		listItem.add(lnkView);

		// title
		listItem.add(new Label("lblTitle", text.getTitle()));

		// description
		listItem.add(new Label("lblDescription", text.getDescription()));
	    }
	};
	add(textListView);

	// list of authors images
	// list actions
	/*
	 * ActionsImagesPanel pnlActionsImages = new ActionsImagesPanel(
	 * "pnlActionsImages", (IModel) new Model(category)); if (user == null)
	 * { pnlActionsImages.setVisible(false); } add(pnlActionsImages);
	 */

	// list
	final List images = author.getImages();
	final ListView imageListView = new ListView("imageList", images) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Image image = (Image) listItem.getDefaultModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		image = repository.getImageById(dn, image.getId());

		final ImagePopupLink lnkOriginal = new ImagePopupLink("lnkOriginal", new Model(image));
		listItem.add(lnkOriginal);

		// title of image
		listItem.add(new Label("lblImageTitle", image.getTitle()));

		// thumbnail of image
		final org.apache.wicket.markup.html.image.Image thumbnail = new org.apache.wicket.markup.html.image.Image(
			"imgThumbnail", new FileResource(repository.getThumbnailFile(dn, image)));
		if (image.getTitle() != null) {
		    thumbnail.add(new SimpleAttributeModifier("alt", image.getTitle()));
		}
		lnkOriginal.add(thumbnail);

		// actions image panel
		final ActionsImagePanel pnlActionsImage = new ActionsImagePanel("pnlActionsImage",
			(Image) listItem.getDefaultModelObject());
		final SignInSession session = (SignInSession) getSession();
		final User user = session.getUser();
		if (user == null) {
		    pnlActionsImage.setVisible(false);
		}
		listItem.add(pnlActionsImage);
	    }
	};
	add(imageListView);

    }
}
