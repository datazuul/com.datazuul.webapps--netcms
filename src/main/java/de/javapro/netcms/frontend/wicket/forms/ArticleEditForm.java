package de.javapro.netcms.frontend.wicket.forms;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import wicket.extensions.markup.html.form.wysiwyg.WysiwygTextarea;
import wicket.extensions.markup.html.form.wysiwyg.WysiwygTextareaSettings;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Article;
import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.pages.SelectImagePage;
import de.javapro.netcms.frontend.wicket.pages.ViewCategoryPage;

/**
 * @author ralf
 */
public class ArticleEditForm extends Form {
    public ArticleEditForm(final String id, final Article article) {
	super(id, new CompoundPropertyModel(article));

	// text field for title
	final TextField txtArticleTitle = new TextField("title");
	add(txtArticleTitle);

	final WysiwygTextareaSettings settings = new WysiwygTextareaSettings();
	settings.setWidth(650);
	settings.setHeight(500);

	final Category category = article.getCategory();
	final SelectImagePage selectImagePage = new SelectImagePage(category);
	settings.setSelectImagePage(selectImagePage);

	final WysiwygTextarea wysiwygTextarea = new WysiwygTextarea("htmlContent", new PropertyModel(article,
		"htmlContent"), settings);
	add(wysiwygTextarea);

	// cancel button
	final Button btnCancel = new Button("btnCancel") {
	    @Override
	    public void onSubmit() {
		final Article article = (Article) getParent().getDefaultModelObject();
		setResponsePage(new ViewCategoryPage(article.getCategory()));
	    }
	};
	btnCancel.setDefaultFormProcessing(false);
	add(btnCancel);
    }

    @Override
    public final void onSubmit() {
	final Article article = (Article) getModelObject();

	final DomainName dn = ((SignInSession) getSession()).getDomainName();

	Page next = null;
	if (article.isNew()) {
	    // first save article to get new id
	    NetCMSRepository.getInstance().save(dn, article);

	    // then save parent category, because the new article has been added
	    // as child
	    final Category parent = article.getCategory();
	    parent.getArticles().add(article);
	    NetCMSRepository.getInstance().save(dn, parent);
	    next = new ViewCategoryPage(parent);
	} else {
	    NetCMSRepository.getInstance().save(dn, article);
	    next = new ViewCategoryPage(article.getCategory());
	}
	// when saving was successful, index article
	NetCMSRepository.getInstance().index(dn, article);

	setResponsePage(next);
    }
}
