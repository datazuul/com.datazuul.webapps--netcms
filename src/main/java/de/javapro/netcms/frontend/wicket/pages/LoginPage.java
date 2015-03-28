package de.javapro.netcms.frontend.wicket.pages;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.netcms.frontend.wicket.forms.LoginForm;

/**
 * @author Ralf Eichinger, 06.02.2007
 */
public class LoginPage extends AppBasePage {
	private static LoggerFacade LOG = LoggerFacade.getInstance(LoginPage.class);

	public LoginPage() {
		LOG.enterPage();

		add(new LoginForm("form"));
	}
}
