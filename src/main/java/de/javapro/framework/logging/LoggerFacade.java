package de.javapro.framework.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.javapro.framework.webapps.session.SessionContext;

/**
 * Needs an initialized SessionContext for logging with session-ID.
 * 
 * @author ralf
 */
public final class LoggerFacade {
	private Logger logger;
	private Class clazz;

	private LoggerFacade(Class clazz) {
		this.clazz = clazz;
		this.logger = Logger.getLogger(clazz.getName());
	}

	public static synchronized LoggerFacade getInstance(Class clazz) {
		return new LoggerFacade(clazz);
	}

	// ===== proxy methods for accessing the original logger-methods =====
	public void config(String key, String value) {
		logger.config(key + " = " + value);
	}

	public void enterPage() {
		logInfo("--> entered", null);
	}

	/**
	 * @param methodName
	 *            method name
	 * @param e
	 *            exception object
	 */
	public void logException(String methodName, Throwable e) {
		logException("", methodName, e);
	}

	/**
	 * @param msg
	 *            message text
	 * @param methodName
	 *            method name
	 * @param e
	 *            exception object
	 */
	public void logException(String msg, String methodName, Throwable e) {
		String sessionId = SessionContext.getSessionId();
		logger.logp(Level.SEVERE, clazz.getName(), methodName, sessionId + " "
				+ msg, e);
	}

	/**
	 * @param msg
	 *            message text
	 * @param methodName
	 *            method name
	 */
	public void logWarning(String msg, String methodName) {
		String sessionId = SessionContext.getSessionId();
		logger.logp(Level.WARNING, clazz.getName(), methodName, sessionId + " "
				+ msg);
	}

	/**
	 * @param msg
	 *            message text
	 * @param methodName
	 *            method name
	 */
	public void logInfo(String msg, String methodName) {
		String sessionId = SessionContext.getSessionId();
		logger.logp(Level.INFO, clazz.getName(), methodName, sessionId + " "
				+ msg);
	}
}
