package de.javapro.netcms.frontend.wicket.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import com.datazuul.commons.cms.domain.Author;
import com.datazuul.commons.cms.domain.Text;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.forms.TextEditForm;

/**
 * @author ralf
 * 
 */
public class EditTextPage extends AuthenticatedWebPage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(EditTextPage.class);

    /**
     * Default constructor
     */
    public EditTextPage() {
    }

    public EditTextPage(final Author pAuthor) {
	final Text text = new Text();
	text.setAuthor(pAuthor);
	buildPage(text);
    }

    public EditTextPage(final Text pText) {
	buildPage(pText);
    }

    /**
     * @param text
     * 
     */
    private void buildPage(final Text text) {
	LOG.enterPage();

	add(new Label("lblTitle", text.getTitle()));
	add(new FeedbackPanel("feedback"));
	add(new TextEditForm("textInputForm", text));
    }
}
