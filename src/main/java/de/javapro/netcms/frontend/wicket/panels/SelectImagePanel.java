package de.javapro.netcms.frontend.wicket.panels;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import wicket.extensions.markup.html.form.wysiwyg.DialogImagePage;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;

import de.javapro.netcms.frontend.wicket.FileResource;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.pages.SelectImagePage;

/**
 * @author ralf
 */
public class SelectImagePanel extends Panel {
    Image selectedImage = new Image();

    HiddenField hiddenTitle = null;

    HiddenField hiddenOriginalUrl = null;

    HiddenField hiddenPreviewUrl = null;

    HiddenField hiddenThumbnailUrl = null;

    org.apache.wicket.markup.html.image.Image imgThumbnailSelected = null;

    Label lblImageAuthorSelected = null;

    Label lblImageDescriptionSelected = null;

    Label lblImageTitleSelected = null;

    /**
     * @param id
     *            component id
     */
    public SelectImagePanel(final String id) {
	super(id);
	addComponents(new Category(1));
    }

    /**
     * @param id
     *            component id
     * @param category
     *            show images of this category
     */
    public SelectImagePanel(final String id, final Category category) {
	super(id);
	addComponents(category);
    }

    private void addComponents(final Category category) {
	setDefaultModel(new Model(category));

	String categoryName = "not found";
	if (category == null) {
	    // invalid id show error message
	    // TODO
	} else {
	    categoryName = category.getName();
	}

	// feedback panel
	final FeedbackPanel pnlFeedback = new FeedbackPanel("pnlFeedback");
	add(pnlFeedback);

	// category title
	/*
	 * Label lblCategoryName = new Label("lblCategoryName", categoryName);
	 * add(lblCategoryName);
	 */

	// category navigator
	final Panel pnlCategoryNavigator = new CategoryNavigatorPanel("pnlCategoryNavigator", category,
		SelectImagePage.class, false);
	add(pnlCategoryNavigator);

	// hidden fields for values of selected image
	hiddenTitle = new HiddenField("hiddenTitle", new Model() {
	    @Override
	    public Serializable getObject() {
		return selectedImage.getTitle();
	    }
	});
	hiddenTitle.setOutputMarkupId(true);
	add(hiddenTitle);

	hiddenOriginalUrl = new HiddenField("hiddenOriginalUrl", new Model() {
	    @Override
	    public Serializable getObject() {
		final ResourceReference imageResource = new ResourceReference("image");
		final String url = getRequestCycle().urlFor(imageResource) + "/id/" + selectedImage.getId()
			+ "/size/original";
		return url;
	    }
	});
	hiddenOriginalUrl.setOutputMarkupId(true);
	add(hiddenOriginalUrl);

	hiddenPreviewUrl = new HiddenField("hiddenPreviewUrl", new Model() {
	    @Override
	    public Serializable getObject() {
		final ResourceReference imageResource = new ResourceReference("image");
		final String url = getRequestCycle().urlFor(imageResource) + "/id/" + selectedImage.getId()
			+ "/size/preview";
		return url;
	    }
	});
	hiddenPreviewUrl.setOutputMarkupId(true);
	add(hiddenPreviewUrl);

	hiddenThumbnailUrl = new HiddenField("hiddenThumbnailUrl", new Model() {
	    @Override
	    public Serializable getObject() {
		final ResourceReference imageResource = new ResourceReference("image");
		final String url = getRequestCycle().urlFor(imageResource) + "/id/" + selectedImage.getId()
			+ "/size/thumbnail";
		return url;
	    }
	});
	hiddenThumbnailUrl.setOutputMarkupId(true);
	add(hiddenThumbnailUrl);

	// show all images of this category
	final List images = category.getImages();
	final ListView imageListView = new ListView("images", images) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Image image = (Image) listItem.getModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		image = repository.getImageById(dn, image.getId());

		// title of image
		listItem.add(new Label("lblImageTitle", image.getTitle()));

		final AjaxFallbackLink lnkImage = new AjaxFallbackLink("lnkSelect", new Model(image)) {
		    @Override
		    public void onClick(final AjaxRequestTarget target) {
			final DomainName dn = ((SignInSession) getSession()).getDomainName();
			Image image = (Image) getModelObject();
			final NetCMSRepository repository = NetCMSRepository.getInstance();
			image = repository.getImageById(dn, image.getId());
			selectedImage = image;
			target.addComponent(hiddenOriginalUrl);
			target.addComponent(hiddenPreviewUrl);
			target.addComponent(hiddenThumbnailUrl);
			target.addComponent(hiddenTitle);
			target.addComponent(imgThumbnailSelected);
			target.addComponent(lblImageAuthorSelected);
			target.addComponent(lblImageDescriptionSelected);
			target.addComponent(lblImageTitleSelected);
		    }
		};
		listItem.add(lnkImage);

		// thumbnail of image
		lnkImage.add(new org.apache.wicket.markup.html.image.Image("imgThumbnail", new FileResource(repository
			.getThumbnailFile(dn, image))));
	    }
	};
	add(imageListView);

	// show all subcategories as links
	final List subcategories = category.getSubcategories();
	final ListView subcategoriesListView = new ListView("subcategories", subcategories) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		Category subcategory = (Category) listItem.getModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		subcategory = repository.getCategoryById(dn, subcategory.getId());
		final Link lnkViewCategory = new Link("lnkViewCategory") {
		    @Override
		    public void onClick() {
			final Category category = (Category) getParent().getDefaultModelObject();
			setResponsePage(new SelectImagePage(category));
		    }
		};
		lnkViewCategory.add(new Label("lblCategoryName", subcategory.getName()));
		listItem.add(lnkViewCategory);
	    }
	};
	add(subcategoriesListView);

	// thumbnail of selected image
	imgThumbnailSelected = new org.apache.wicket.markup.html.image.Image("imgThumbnailSelected",
		DialogImagePage.IMG_NO_IMAGE);
	/*
	 * new Model() { public Object getObject(Component comp) {
	 * NetCMSRepository repository = NetCMSRepository .getInstance();
	 * Resource thumbnailResource = repository
	 * .getThumbnailResource(selectedImage); if (thumbnailResource == null)
	 * { ResourceReference result = DialogImagePage.IMG_NO_IMAGE; return
	 * result; } else { return thumbnailResource; } } });
	 */
	imgThumbnailSelected.setOutputMarkupId(true);
	add(imgThumbnailSelected);

	// title of selected image
	lblImageTitleSelected = new Label("lblImageTitleSelected", new Model() {
	    @Override
	    public Serializable getObject() {
		return selectedImage.getTitle();
	    }
	});
	lblImageTitleSelected.setOutputMarkupId(true);
	add(lblImageTitleSelected);

	// author of selected image
	lblImageAuthorSelected = new Label("lblImageAuthorSelected", new Model() {
	    @Override
	    public Serializable getObject() {
		String result = "";
		if (selectedImage.getAuthor() != null) {
		    result = selectedImage.getAuthor().getFirstname() + " " + selectedImage.getAuthor().getSurname();
		}
		return result;
	    }
	});
	lblImageAuthorSelected.setOutputMarkupId(true);
	add(lblImageAuthorSelected);

	// description of selected image
	lblImageDescriptionSelected = new Label("lblImageDescriptionSelected", new Model() {
	    @Override
	    public Serializable getObject() {
		return selectedImage.getDescription();
	    }
	});
	lblImageDescriptionSelected.setOutputMarkupId(true);
	add(lblImageDescriptionSelected);
    }
}
