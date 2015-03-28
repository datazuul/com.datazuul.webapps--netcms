package de.javapro.netcms.frontend.wicket.models;

import java.io.Serializable;

public class LoginModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    /**
     * @return the password
     */
    public String getPassword() {
	return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(final String password) {
	this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
	return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(final String username) {
	this.username = username;
    }
}
