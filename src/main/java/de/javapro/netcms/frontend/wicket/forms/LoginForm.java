package de.javapro.netcms.frontend.wicket.forms;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import de.javapro.netcms.frontend.wicket.SignInSession;
import de.javapro.netcms.frontend.wicket.models.LoginModel;
import de.javapro.netcms.frontend.wicket.pages.ViewCategoryPage;

public final class LoginForm extends Form {
    private final LoginModel model = new LoginModel();

    public LoginForm(final String id) {
	super(id);

	final FeedbackPanel feedback = new FeedbackPanel("feedback");
	add(feedback);

	add(new TextField("username", new PropertyModel(model, "username")));
	add(new PasswordTextField("password", new PropertyModel(model, "password")));
    }

    /**
     * @see wicket.markup.html.form.Form#onSubmit()
     */
    @Override
    public final void onSubmit() {
	// Get session info
	final SignInSession session = (SignInSession) getSession();

	// Sign the user in
	/*
	 * User user = new User(); user.setPassword("test");
	 * user.setUsername("test"); session.setUser(user); if (true)
	 */
	if (session.authenticate(model)) {

	    if (!continueToOriginalDestination()) {
		// FIXME if admin/author
		// if admin:
		setResponsePage(new ViewCategoryPage(session.getLastViewedCategory()));

		// if author:
		// setResponsePage(new EditAuthorPage(false));
	    }
	} else {
	    // Form method that will notify feedback panel
	    // Try the component based localizer first. If not found try the
	    // application localizer. Else use the default
	    final String errmsg = getLocalizer().getString("loginError", this, "Unable to sign you in");
	    error(errmsg);
	}
    }

}
