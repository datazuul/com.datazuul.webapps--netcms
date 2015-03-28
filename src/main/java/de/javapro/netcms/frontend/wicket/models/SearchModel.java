package de.javapro.netcms.frontend.wicket.models;

import java.io.Serializable;

public class SearchModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String query;

    /**
     * @return the query
     */
    public String getQuery() {
	return query;
    }

    /**
     * @param query
     *            the query to set
     */
    public void setQuery(final String query) {
	this.query = query;
    }
}
