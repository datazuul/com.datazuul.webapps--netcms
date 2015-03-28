package de.javapro.netcms.frontend.wicket.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.FileResource;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.webapps.wicket.components.imagepopup.ImagePopupLink;

/**
 * @author ralf
 * 
 */
public class DeleteImagePage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(DeleteImagePage.class);

    /**
     * Default constructor
     */
    public DeleteImagePage() {
    }

    public DeleteImagePage(final Image pImage) {
	buildPage(pImage);
    }

    /**
     * @param image
     * 
     */
    private void buildPage(final Image image) {
	LOG.enterPage();

	final Fragment fragImage = new Fragment("pnlFile", "fragImage");
	add(fragImage);

	// link to original
	final ImagePopupLink lnkOriginal = new ImagePopupLink("lnkOriginal", new Model(image));
	fragImage.add(lnkOriginal);
	// preview image
	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	final org.apache.wicket.markup.html.image.Image preview = new org.apache.wicket.markup.html.image.Image(
		"imgPreview", new FileResource(NetCMSRepository.getInstance().getPreviewFile(dn, image)));
	if (image.getTitle() != null) {
	    preview.add(new SimpleAttributeModifier("alt", image.getTitle()));
	}
	lnkOriginal.add(preview);
	add(fragImage);

	add(new Label("lblTitle", image.getTitle()));
	add(new FeedbackPanel("feedback"));

	final Form form = new Form("imageInputForm", new Model(image));
	add(form);

	// cancel button
	final Button btnCancel = new Button("btnCancel") {
	    @Override
	    public void onSubmit() {
		final Image image = (Image) getParent().getDefaultModelObject();
		setResponsePage(new ViewCategoryPage((Category) image.getCategories().get(0)));
	    }
	};
	btnCancel.setDefaultFormProcessing(false);
	form.add(btnCancel);

	// delete button
	final Button btnDelete = new Button("btnDelete") {
	    @Override
	    public void onSubmit() {
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		final DomainName dn = ((SignInSession) getSession()).getDomainName();

		final Image image = (Image) getParent().getDefaultModelObject();

		final long categoryId = ((Category) image.getCategories().get(0)).getId();

		repository.deleteImage(dn, image);
		repository.unIndex(dn, image);

		final List categories = image.getCategories();
		for (final Iterator iterator = categories.iterator(); iterator.hasNext();) {
		    Category category = (Category) iterator.next();
		    category = repository.getCategoryById(dn, category.getId());

		    final List newImages = new ArrayList<Category>();
		    final List images = category.getImages();
		    for (final Iterator iterator2 = images.iterator(); iterator2.hasNext();) {
			final Image img = (Image) iterator2.next();
			if (image.getId() != img.getId()) {
			    newImages.add(img);
			}
		    }
		    category.setImages(newImages);
		    repository.save(dn, category);
		}

		final Page next = new ViewCategoryPage(categoryId);
		setResponsePage(next);
	    }
	};
	form.add(btnDelete);
    }
}
