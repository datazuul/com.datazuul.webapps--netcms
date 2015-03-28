package de.javapro.framework.webapps.session;

import javax.servlet.http.HttpSession;

public final class SessionContext {
	private static ThreadLocal SESSION = new ThreadLocal();

	private SessionContext() {

	}

	public static void initialize(HttpSession session) {
		SESSION.set(session);
	}

	private static HttpSession getSession() {
		return (HttpSession) SESSION.get();
	}

	public static String getSessionId() {
		if (getSession() != null) {
			return getSession().getId();
		}
		return "---";
	}
}
