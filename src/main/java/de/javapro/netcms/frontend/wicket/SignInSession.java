package de.javapro.netcms.frontend.wicket;

import java.util.Properties;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;

import com.datazuul.commons.cms.domain.Category;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.User;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.models.LoginModel;

/**
 * Session class for signin example. Holds and authenticates users.
 * 
 * @author Jonathan Locke
 */
public final class SignInSession extends WebSession {
    private static LoggerFacade LOG = LoggerFacade.getInstance(SignInSession.class);

    /** user representation */
    private User user;

    private Category lastViewedCategory;

    private final DomainName domainName;

    private String theme;

    private String siteTitle;

    /**
     * Constructor
     * 
     * @param application
     *            The application
     * @param request
     * @param domainName
     */
    protected SignInSession(final WebApplication application, final Request request, final DomainName domainName) {
	super(application, request);
	this.domainName = domainName;
    }

    /**
     * Checks the given username and password, returning a User object if if the
     * username and password identify a valid user.
     * 
     * @param username
     *            The username
     * @param password
     *            The password
     * @return True if the user was authenticated
     */
    public final boolean authenticate(final LoginModel model) {
	if (user == null && model != null) {
	    // Trivial password "db"
	    /*
	     * if ("wicket".equalsIgnoreCase(username) &&
	     * "wicket".equalsIgnoreCase(password)) { user = username; }
	     */
	    final String username = model.getUsername();
	    final String password = model.getPassword();

	    LOG.logInfo("trying username = " + username, "authenticate");

	    // FIXME: remove static user login
	    if (username.equals("admin") && password.equals("admin123")) {
		// if (username.equals(password)) {
		// login successful, save session objects:
		this.user = new User();
		this.user.setPassword(password);
		this.user.setUsername(username);
		LOG.logInfo("Login successful: username = " + username, "authenticate");
	    } else {
		LOG.logInfo("Login not successful: password incorrect", "authenticate");
		return false;
	    }
	}

	return this.user != null;
    }

    /**
     * @return True if user is signed in
     */
    public boolean isSignedIn() {
	return user != null;
    }

    /**
     * @return User
     */
    public User getUser() {
	return user;
    }

    /**
     * @param user
     *            New user
     */
    public void setUser(final User user) {
	this.user = user;
    }

    /**
     * @return last viewed category
     */
    public Category getLastViewedCategory() {
	return lastViewedCategory;
    }

    /**
     * @param lastViewedCategory
     *            last viewed category
     */
    public void setLastViewedCategory(final Category lastViewedCategory) {
	this.lastViewedCategory = lastViewedCategory;
    }

    public DomainName getDomainName() {
	return domainName;
    }

    public String getTheme() {
	String result = this.theme;
	if (result == null) {
	    final Properties themes = ((NetCMSApplication) NetCMSApplication.get()).getThemesProperties();
	    result = themes.getProperty(domainName.getFullyQualifiedDomainName());
	    if (result == null) {
		result = themes.getProperty("*");
	    }
	    this.theme = result;
	}
	return result;
    }

    public String getSiteTitle() {
	String result = this.siteTitle;
	if (result == null) {
	    final Properties sites = ((NetCMSApplication) NetCMSApplication.get()).getSitesProperties();
	    result = sites.getProperty(domainName.getFullyQualifiedDomainName() + "-title");
	    if (result == null) {
		result = sites.getProperty("*-title", "");
	    }
	    this.siteTitle = result;
	}
	return result;
    }
}
