package de.javapro.netcms.frontend.wicket.forms;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.resource.ByteArrayResource;
import org.apache.wicket.util.lang.Bytes;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Author;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Text;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.pages.EditTextPage;
import de.javapro.netcms.frontend.wicket.pages.ViewAuthorPage;

/**
 * @author ralf
 */
public class TextEditForm extends Form {
    private Button btnUpload;

    private FileUploadField fileUploadField;

    public TextEditForm(final String id, final Text text) {
	super(id, new Model(text));

	// set this form to multipart mode (always needed for uploads)
	setMultiPart(true);
	// set maximum size to 1024kB
	// TODO make max size configurable
	setMaxSize(Bytes.kilobytes(1024));

	// text field for title
	final TextField txtTitle = new TextField("title", new PropertyModel(text, "title"));
	add(txtTitle);

	// description
	final TextArea txtDescription = new TextArea("description", new PropertyModel(text, "description"));
	add(txtDescription);

	final boolean showUploadPanel = (text.getProperties().getBytes() == null);

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
		    info("btnUpload clicked");

		    final DomainName dn = ((SignInSession) getSession()).getDomainName();
		    final NetCMSRepository repository = NetCMSRepository.getInstance();
		    final Text text = (Text) getForm().getModelObject();
		    final FileUpload upload = fileUploadField.getFileUpload();
		    if (upload != null) {
			// set format
			String mimetype = upload.getContentType();
			if (mimetype.indexOf("/") != -1) {
			    mimetype = mimetype.substring(mimetype.indexOf("/") + 1);
			}
			text.setFormat(mimetype);

			// set bytes
			text.getProperties().setBytes(upload.getBytes());

			// save
			repository.save(dn, text);

			// add text to author and save author
			final Author author = text.getAuthor();
			author.getTexts().add(text);
			repository.save(dn, author);
			setResponsePage(new EditTextPage(text));
		    }
		}
	    };
	    btnUpload.setDefaultFormProcessing(false);
	    fragUpload.add(btnUpload);

	    add(fragUpload);
	}

	// Fragment image
	if (!showUploadPanel) {
	    final Fragment fragDownload = new Fragment("pnlFile", "fragDownload");
	    // download link
	    final ResourceLink lnkDownload = new ResourceLink("lnkDownload", new ByteArrayResource("application/"
		    + text.getFormat(), text.getProperties().getBytes()));
	    fragDownload.add(lnkDownload);
	    add(fragDownload);
	}

	// cancel button
	final Button btnCancel = new Button("btnCancel") {
	    @Override
	    public void onSubmit() {
		final Text text = (Text) getParent().getDefaultModelObject();
		setResponsePage(new ViewAuthorPage(text.getAuthor()));
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

		final Text text = (Text) getParent().getDefaultModelObject();

		Page next = null;
		if (text.isNew()) {
		    // first save text to get new id
		    repository.save(dn, text);

		    // add text to author and save author
		    final Author author = (Author) getParent().getDefaultModelObject();
		    author.getTexts().add(text);
		    repository.save(dn, author);

		    next = new ViewAuthorPage(author);
		} else {
		    repository.save(dn, text);
		    next = new ViewAuthorPage(text.getAuthor());
		}
		// when saving was successful, index image
		repository.index(dn, text);

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
