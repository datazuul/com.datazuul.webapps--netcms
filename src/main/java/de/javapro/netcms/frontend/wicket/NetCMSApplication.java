package de.javapro.netcms.frontend.wicket;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Component;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadWebRequest;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.util.file.Folder;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.backend.PersistenceManager;
import com.datazuul.commons.cms.backend.filesystem.FilePathFactory;
import com.datazuul.commons.cms.backend.filesystem.FilePersistenceManager;
import com.datazuul.commons.cms.backend.search.lucene.DefaultLuceneDocumentFactory;
import com.datazuul.commons.cms.backend.search.lucene.LuceneIndexNetCMS;
import com.datazuul.commons.cms.backend.search.lucene.LuceneIndexer;
import com.datazuul.commons.cms.backend.search.lucene.LuceneSearcher;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.framework.util.PropertiesLoader;

import de.javapro.framework.logging.LoggerFacade;
import de.javapro.framework.logging.PixotecFormatter;
import de.javapro.netcms.frontend.wicket.pages.AuthenticatedWebPage;
import de.javapro.netcms.frontend.wicket.pages.HomePage;
import de.javapro.netcms.frontend.wicket.pages.LoginPage;
import de.javapro.netcms.frontend.wicket.pages.ViewCategoryPage;

/**
 * @author Ralf Eichinger, 06.02.2007
 */
public class NetCMSApplication extends WebApplication {
    private static LoggerFacade LOG = null;

    private final ThreadLocal<DomainName> domainName = new ThreadLocal<DomainName>();

    private Folder uploadFolder = null;

    private Properties sitesProperties;

    private Properties themesProperties;

    public NetCMSApplication() {
    }

    /**
     * @see wicket.examples.WicketExampleApplication#init()
     */
    @Override
    protected void init() {
	// init application modes
	// String runtimeMode = getInitParameter(Application.CONFIGURATION);
	// configure(runtimeMode);
	System.out.println("configuration type = " + getConfigurationType());
	// Application.DEVELOPMENT or Application.DEPLOYMENT

	// we have popup windows for image insertion...
	getPageSettings().setAutomaticMultiWindowSupport(true);

	// init Logging
	initLogging();

	// init upload folder
	uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "cms-uploads");
	// Ensure folder exists
	uploadFolder.mkdirs();

	// read themes.properties
	initThemesProperties();

	// read sites.properties
	initSitesProperties();

	// mount pages
	mountBookmarkablePage("/home", HomePage.class);

	// http://localhost:18080/kms/app/category/id/3
	mountBookmarkablePage("/category", ViewCategoryPage.class);

	// http://localhost:18080/kms/app/image/id/7/size/thumbnail
	getSharedResources().add("image", new ImageResource());
	mountSharedResource("/image", new ResourceReference("image").getSharedResourceKey());

	// init Authorization-Strategy
	getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy() {
	    public boolean isActionAuthorized(final Component component, final Action action) {
		return true;
	    }

	    public boolean isInstantiationAuthorized(final Class componentClass) {
		if (AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
		    // Is user signed in?
		    if (((SignInSession) Session.get()).isSignedIn()) {
			// okay to proceed
			return true;
		    }

		    // Force sign in
		    throw new RestartResponseAtInterceptPageException(LoginPage.class);
		}
		return true;
	    }
	});
    }

    private void initThemesProperties() {
	try {
	    // Load the themes.properties file
	    themesProperties = PropertiesLoader.loadParams("themes");
	} catch (final Exception ex) {
	    System.out.println(" Fatal Error : Couldn't Read themes.properties file: " + ex.toString());
	}
    }

    private void initSitesProperties() {
	try {
	    // Load the sites.properties file
	    sitesProperties = PropertiesLoader.loadParams("sites");
	} catch (final Exception ex) {
	    System.out.println(" Fatal Error : Couldn't Read sites.properties file: " + ex.toString());
	}
    }

    private void initLogging() {
	// ignoring global configuration, making own:
	// configuration for pixoprivate and all descendant packages
	final Logger logger = Logger.getLogger("de.pixotec.webapps.kms");

	// set logfile(s)
	// ==============
	FileHandler fh = null;
	String logDirPath = getInitParameter("logDir");
	if (logDirPath == null) {
	    logDirPath = "/var/log/netcms";
	}
	try {
	    final File logDir = new File(logDirPath);
	    if (!logDir.exists()) {
		logDir.mkdirs();
	    }
	    // pattern - the pattern for naming the output file
	    // limit - the maximum number of bytes to write to any one file
	    // count - the number of files to use
	    final String pattern = logDirPath + "/netcms-%g.log";
	    final int limit = 10485760; // 1024 * 1024 * 10 = 10 MB
	    final int count = 10;
	    fh = new FileHandler(pattern, limit, count);
	} catch (final SecurityException e) {
	    e.printStackTrace();
	} catch (final IOException e) {
	    e.printStackTrace();
	}

	// set format
	// ==========
	// fh.setFormatter(new SimpleFormatter());
	fh.setFormatter(new PixotecFormatter());

	logger.addHandler(fh);

	// set logging level
	// =================
	// Level.ALL, Level.FINE, Level.FINER, Level.FINEST, Level.INFO,
	// Level.OFF, Level.SEVERE, Level.WARNING, Level.CONFIG
	final String logLevel = getInitParameter("logLevel");
	if (logLevel != null) {
	    logger.setLevel(Level.parse(logLevel));
	} else {
	    logger.setLevel(Level.ALL);
	}

	// now start logging:
	// ==================
	LOG = LoggerFacade.getInstance(NetCMSApplication.class);
	LOG.config("logDir", logDirPath);
	LOG.config("logLevel", logger.getLevel().getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.
     * wicket.Request, org.apache.wicket.Response)
     */
    @Override
    public Session newSession(final Request request, final Response response) {
	final DomainName dn = NetCMSApplication.this.domainName.get();

	// init PersistenceManager
	final PersistenceManager pm = FilePersistenceManager.getInstance(dn);
	pm.setUseCache(false);

	// init Repository
	final NetCMSRepository netCMSRepository = NetCMSRepository.getInstance();
	netCMSRepository.setPersistenceManager(pm);
	netCMSRepository.init(getDomainName());
	final NetCMSRepository repository = NetCMSRepository.getInstance();

	// // init Index/Search
	final LuceneIndexNetCMS index = new LuceneIndexNetCMS(FilePathFactory.getRootPathRepository(dn) + "index");

	// // init Indexer
	final LuceneIndexer indexer = new LuceneIndexer();
	indexer.setLuceneDocumentFactory(new DefaultLuceneDocumentFactory());
	indexer.setIndex(index);
	NetCMSRepository.getInstance().setIndexer(dn, indexer);

	// // init Searcher
	final LuceneSearcher searcher = new LuceneSearcher();
	searcher.setLuceneDocumentFactory(new DefaultLuceneDocumentFactory());
	searcher.setIndex(index);
	NetCMSRepository.getInstance().setSearcher(dn, searcher);

	// // do re-index if index is somehow empty but content is present
	NetCMSRepository.getInstance().reIndex(dn, false);

	return new SignInSession(NetCMSApplication.this, request, dn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class getHomePage() {
	return HomePage.class;
    }

    @Override
    public RequestCycle newRequestCycle(final Request request, final Response response) {
	return new WebRequestCycle(this, (WebRequest) request, response) {
	    @Override
	    protected void onBeginRequest() {
		final HttpServletRequest httpServletRequest = ((WebRequest) request).getHttpServletRequest();
		final DomainName dn = new DomainName(httpServletRequest.getServerName());
		NetCMSApplication.this.domainName.set(dn);
	    }

	    @Override
	    protected void onEndRequest() {
		// close session
	    }
	};
    }

    public DomainName getDomainName() {
	return this.domainName.get();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.protocol.http.WebApplication#newWebRequest(javax.servlet
     * .http.HttpServletRequest)
     */
    @Override
    protected WebRequest newWebRequest(final HttpServletRequest servletRequest) {
	return new UploadWebRequest(servletRequest);
    }

    /**
     * @return the folder for uploads
     */
    public Folder getUploadFolder() {
	return uploadFolder;
    }

    public Properties getSitesProperties() {
	return sitesProperties;
    }

    public Properties getThemesProperties() {
	return themesProperties;
    }
}
