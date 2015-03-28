package de.javapro.netcms.frontend.wicket.forms;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.datazuul.commons.cms.domain.SearchTarget;

import de.javapro.netcms.frontend.wicket.models.SearchModel;
import de.javapro.netcms.frontend.wicket.pages.ViewSearchResultsPage;

public class SearchForm extends Form {
    private final SearchModel model = new SearchModel();
    TextField txtQuery = null;

    public RadioGroup targetGroup = new RadioGroup("searchTargetGroup", new Model());

    private static final List TARGETS;
    static {
	TARGETS = new ArrayList();
	TARGETS.add(new SearchTarget(SearchTarget.AUTHORS_AND_TITLES, "in Autoren u. Titel"));
	TARGETS.add(new SearchTarget(SearchTarget.CATEGORIES_AND_ARTICLES, "in Themenbereichen"));
    }

    public SearchForm(final String id) {
	super(id);

	// text field for search query string
	txtQuery = new TextField("query", new PropertyModel(model, "query"));
	add(txtQuery);

	final ListView targets = new ListView("targets", TARGETS) {
	    @Override
	    protected void populateItem(final ListItem item) {
		item.add(new Radio("radio", item.getModel()));
		item.add(new Label("target", item.getDefaultModelObjectAsString()));
	    };
	}.setReuseItems(true);
	targetGroup.add(targets);
	// set default selection:
	targetGroup.setModelObject(TARGETS.get(1));
	add(targetGroup);
    }

    public SearchForm(final String id, final String query) {
	this(id);
	txtQuery.setModelObject(query);
    }

    public void setQuery(final String query) {
	model.setQuery(query);
    }

    @Override
    public final void onSubmit() {
	final SearchTarget searchTarget = (SearchTarget) targetGroup.getModelObject();
	final Page next = new ViewSearchResultsPage(model.getQuery(), searchTarget);
	setResponsePage(next);
    }
}
