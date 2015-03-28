package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.Image;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.forms.ImageEditForm;

/**
 * @author ralf
 * 
 */
public class EditImagePage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(EditImagePage.class);

    /**
     * Default constructor
     */
    public EditImagePage() {
    }

    public EditImagePage(final Category pCategory) {
	final Image image = new Image();
	image.getCategories().add(pCategory);
	buildPage(image);
    }

    public EditImagePage(final Image pImage) {
	buildPage(pImage);
    }

    /**
     * @param image
     * 
     */
    private void buildPage(final Image image) {
	LOG.enterPage();

	add(new Label("lblTitle", image.getTitle()));
	add(new FeedbackPanel("feedback"));
	add(new ImageEditForm("imageInputForm", image));
    }
}
