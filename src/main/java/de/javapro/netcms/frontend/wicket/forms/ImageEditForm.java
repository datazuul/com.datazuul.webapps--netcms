package de.javapro.netcms.frontend.wicket.forms;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.lang.Bytes;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;

import de.javapro.netcms.frontend.wicket.FileResource;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.pages.EditImagePage;
import de.javapro.netcms.frontend.wicket.pages.ViewCategoryPage;
import de.javapro.webapps.wicket.components.imagepopup.ImagePopupLink;

/**
 * @author ralf
 */
public class ImageEditForm extends Form {
    private Button btnUpload;

    private FileUploadField fileUploadField;

    public ImageEditForm(final String id, final Image image) {
	super(id, new Model(image));

	// set this form to multipart mode (always needed for uploads)
	setMultiPart(true);
	// set maximum size to 500kB
	// TODO make max size configurable
	long maxUploadSize = 1024;
	final String maxUploadSizeStr = WebApplication.get().getInitParameter("maxUploadSize");
	if (maxUploadSizeStr != null) {
	    maxUploadSize = Long.parseLong(maxUploadSizeStr);
	}
	setMaxSize(Bytes.kilobytes(maxUploadSize));

	// text field for title
	final TextField txtTitle = new TextField("title", new PropertyModel(image, "title"));
	add(txtTitle);

	// description
	final TextArea txtDescription = new TextArea("description", new PropertyModel(image, "description"));
	add(txtDescription);

	final boolean showUploadPanel = (image.getPropsOriginal().getBytes() == null);

	if (showUploadPanel) {
	    // upload fields
	    final Fragment fragUpload = new Fragment("pnlFile", "fragUpload");
	    // add one file input field
	    fileUploadField = new FileUploadField("fileInput");
	    fragUpload.add(fileUploadField);

	    // commented because makes onSubmit of buttons not work
	    // ajax progress bar for upload
	    // UploadProgressBar progressBar = new UploadProgressBar("progress",
	    // this);
	    // if (image.getPropsOriginal().getBytes() != null)
	    // {
	    // progressBar.setVisible(false);
	    // }
	    // add(progressBar);

	    // upload button
	    btnUpload = new Button("btnUpload") {
		@Override
		public void onSubmit() {
		    final DomainName dn = ((SignInSession) getSession()).getDomainName();

		    info("btnUpload clicked");
		    final NetCMSRepository repository = NetCMSRepository.getInstance();
		    final Image image = (Image) getForm().getModelObject();
		    final FileUpload upload = fileUploadField.getFileUpload();
		    if (upload != null) {
			// save image
			String mimetype = upload.getContentType();
			if (mimetype.indexOf("/") != -1) {
			    mimetype = mimetype.substring(mimetype.indexOf("/") + 1);
			}
			image.setFormat(mimetype);
			image.getPropsOriginal().setBytes(upload.getBytes());

			// save image
			repository.save(dn, image);

			// then save parent category, because the new image has
			// been added as child
			final List categories = image.getCategories();
			for (final Iterator iterator = categories.iterator(); iterator.hasNext();) {
			    final Category parent = (Category) iterator.next();
			    parent.getImages().add(image);
			    repository.save(dn, parent);
			}
			setResponsePage(new EditImagePage(image));
		    }
		}
	    };
	    btnUpload.setDefaultFormProcessing(false);
	    fragUpload.add(btnUpload);

	    add(fragUpload);
	}

	final DomainName dn = ((SignInSession) getSession()).getDomainName();
	// Fragment image
	if (!showUploadPanel) {
	    final Fragment fragImage = new Fragment("pnlFile", "fragImage");
	    // link to original
	    final ImagePopupLink lnkOriginal = new ImagePopupLink("lnkOriginal", new Model(image));
	    fragImage.add(lnkOriginal);
	    // preview image
	    final org.apache.wicket.markup.html.image.Image preview = new org.apache.wicket.markup.html.image.Image(
		    "imgPreview", new FileResource(NetCMSRepository.getInstance().getPreviewFile(dn, image)));
	    if (image.getTitle() != null) {
		preview.add(new SimpleAttributeModifier("alt", image.getTitle()));
	    }
	    lnkOriginal.add(preview);
	    add(fragImage);
	}

	// cancel button
	final Button btnCancel = new Button("btnCancel") {
	    @Override
	    public void onSubmit() {
		final Image image = (Image) getParent().getDefaultModelObject();
		setResponsePage(new ViewCategoryPage((Category) image.getCategories().get(0)));
	    }
	};
	btnCancel.setDefaultFormProcessing(false);
	add(btnCancel);

	// save button
	final Button btnSave = new Button("btnSave") {
	    @Override
	    public void onSubmit() {
		final DomainName dn = ((SignInSession) getSession()).getDomainName();
		final NetCMSRepository repository = NetCMSRepository.getInstance();

		final Image image = (Image) getParent().getDefaultModelObject();

		Page next = null;
		if (image.isNew()) {
		    final Category category = (Category) getParent().getDefaultModelObject();

		    // first save image to get new id
		    image.getCategories().add(category);
		    repository.save(dn, image);

		    // then save parent category, because the new image has been
		    // added as child
		    category.getImages().add(image);
		    repository.save(dn, category);

		    next = new ViewCategoryPage(category);
		} else {
		    repository.save(dn, image);
		    next = new ViewCategoryPage((Category) image.getCategories().get(0));
		}
		// when saving was successful, index image
		repository.index(dn, image);

		setResponsePage(next);
	    }
	};
	add(btnSave);
    }

    @Override
    protected void onSubmit() {
	super.onSubmit();

    }
}
