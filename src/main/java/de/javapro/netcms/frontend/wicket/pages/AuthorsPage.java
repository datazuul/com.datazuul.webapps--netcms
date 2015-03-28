package de.javapro.netcms.frontend.wicket.pages;

import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.Author;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.User;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.forms.SearchForm;
import de.javapro.netcms.frontend.wicket.panels.ActionsAuthorPanel;
import de.javapro.netcms.frontend.wicket.panels.ActionsAuthorsPanel;

public class AuthorsPage extends AppBasePage {
    private static LoggerFacade LOG = LoggerFacade.getInstance(AuthorsPage.class);

    public AuthorsPage() {
	super();
	buildPage(null);
    }

    public AuthorsPage(final PageParameters params) {
	super();
	String sublist = null;
	if (params != null) {
	    if (params.containsKey("sublist")) {
		sublist = params.getString("sublist");
	    }
	}
	buildPage(sublist);
    }

    public AuthorsPage(final String sublist) {
	buildPage(sublist);
    }

    /**
     * Show list of all authors. If sublist is given show only authors, whose
     * surname begins with sublists value.
     * 
     * @param sublist
     *            start string for authors surname or null if all authors
     */
    private void buildPage(final String sublist) {
	LOG.enterPage();
	LOG.logInfo("sublist='" + sublist + "'", "buildPage");

	// search form
	final Form searchForm = new SearchForm("searchForm");
	add(searchForm);

	// edit list
	final ActionsAuthorsPanel pnlActionsAuthorList = new ActionsAuthorsPanel("pnlActionsAuthorList");
	final SignInSession session = (SignInSession) getSession();
	final User user = session.getUser();
	if (user == null) {
	    pnlActionsAuthorList.setVisible(false);
	}
	add(pnlActionsAuthorList);

	final DomainName dn = session.getDomainName();

	// list
	final NetCMSRepository repository = NetCMSRepository.getInstance();
	final List authors = repository.getAuthors(dn);
	final ListView authorListView = new ListView("authorList", authors) {
	    @Override
	    public void populateItem(final ListItem listItem) {
		Author author = (Author) listItem.getModelObject();
		final NetCMSRepository repository = NetCMSRepository.getInstance();
		author = repository.getAuthorById(((SignInSession) getSession()).getDomainName(), author.getId());

		// name = surname, firstname(s)
		String name = author.getSurname();
		if (author.getFirstname() != null) {
		    name += ", " + author.getFirstname();
		}
		final Link lnkView = new Link("lnkView") {
		    @Override
		    public void onClick() {
			final Author author = (Author) getParent().getDefaultModelObject();
			setResponsePage(new ViewAuthorPage(author));
		    }
		};
		lnkView.add(new Label("lblName", name));
		listItem.add(lnkView);

		// day of birth
		listItem.add(new Label("lblDayOfBirth", author.getDayOfBirth()));

		// day of death
		listItem.add(new Label("lblDayOfDeath", author.getDayOfDeath()));

		// actions panel
		final ActionsAuthorPanel pnlActionsAuthor = new ActionsAuthorPanel("pnlActionsAuthor",
			listItem.getModel());
		final SignInSession session = (SignInSession) getSession();
		final User user = session.getUser();
		if (user == null) {
		    pnlActionsAuthor.setVisible(false);
		}
		listItem.add(pnlActionsAuthor);
	    }
	};
	add(authorListView);
    }
}
