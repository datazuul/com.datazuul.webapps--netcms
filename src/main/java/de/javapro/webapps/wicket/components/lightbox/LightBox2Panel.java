package de.javapro.webapps.wicket.components.lightbox;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.JavaScriptReference;
import org.apache.wicket.markup.html.resources.TextTemplateResourceReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;

/**
 * Wicket component integrating ligthbox2 javascript.<br />
 * <br />
 * Example:<br/>
 * <br/>
 * <code>
 * List& list = new LinkedList();<br />
 * list.add(new LightboxImage("/images/pic1_fullsize.png","/images/pic1_thumbnail.png"));<br />
 * list.add(new LightboxImage("/images/pic2_fullsize.png","/images/pic2_thumbnail.png"));<br />
 * list.add(new LightboxImage("/images/pic3_fullsize.png","/images/pic3_thumbnail.png"<br />
 *                            ,"Imagefooter-text of Pic3"));<br />
 * <br />                          
 * // this second list is optional <br />
 * List& hiddenOnes = new LinkedList();<br />
 * hiddenOnes.add(new HiddenLightBoxImage("/images/pic4_fullsize.png"));<br />
 * <br />
 * add(new LightBox2Panel("myComponentId", list, hiddenOnes));<br />
 * 
 * </code>
 * 
 * @author Uwe Schäfer, (uwe@codesmell.org), Ralf Eichinger
 * 
 */
public class LightBox2Panel extends Panel {
    private static final long serialVersionUID = 1L;
    private static final AtomicBoolean classAliasConfigured = new AtomicBoolean(false);
    private static final String CONTEXT_NAME = "LightBox2Panel";

    // private static final Model variableModel = new Model(new MicroMap(
    // "baseUrl", "/resources/" + LightBox2Panel.CONTEXT_NAME ));

    /**
     * @deprecated use public LightBox2Panel(final String id, final IModel
     *             model) instead
     * @see public LightBox2Panel(final String id, final IModel model)
     * @param id
     *            Component id as common in Wicket components
     * @param images
     *            a list of images to show.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public LightBox2Panel(final String id, final List<LightBoxImage> images) {
	this(id, images, Collections.EMPTY_LIST);
    }

    /**
     * @deprecated use public LightBox2Panel(final String id, final IModel
     *             model) instead
     * @see public LightBox2Panel(final String id, final IModel model)
     * @param id
     *            Component id as common in Wicket components
     * @param images
     *            a list of images to show.
     * @param hiddenImages
     *            a list of images not to be shown initially in the list of
     *            thumbnails, but nevertheless be included in the lightbox
     *            slideshow
     */
    @Deprecated
    public LightBox2Panel(final String id, final List<LightBoxImage> images,
	    final List<HiddenLightBoxImage> hiddenImages) {
	this(id, new Model(new LightBox2Config(images, hiddenImages)));

    }

    /**
     * Creates a Lightbox-Script according to the given LightBoxConfig-Model.
     * 
     * @param id
     *            The Component id as in wicket:id
     * @param model
     *            the Model referencing a LightBox2Config instance (oh, we need
     *            generics here...)
     */
    public LightBox2Panel(final String id, final IModel model) {
	super(id, model);
	if (!LightBox2Panel.classAliasConfigured.getAndSet(true)) {
	    getApplication().getSharedResources().putClassAlias(this.getClass(), LightBox2Panel.CONTEXT_NAME);
	}

	setOutputMarkupId(true);
	addHeaderContributions();
	addThumbnails();
	addHiddenImages();

	add(new AbstractBehavior() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void renderHead(final IHeaderResponse response) {
		final WebRequest request = (WebRequest) RequestCycle.get().getRequest();
		if (request.isAjax()) {
		    response.renderOnLoadJavascript("initLightbox()");
		}
		// else
		// {
		// response.renderOnLoadJavascript("myLightbox = new Lightbox()");
		// }
	    }
	});
    }

    private void addHiddenImages() {
	add(new ListView("hiddenImages", new PropertyModel(getDefaultModel(), "hidden")) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem item) {
		final HiddenLightBoxImage modelObject = (HiddenLightBoxImage) item.getModelObject();
		item.add(new HiddenImage("hiddenImage", LightBox2Panel.this.getId(), modelObject));
		item.setRenderBodyOnly(true);
	    }
	});
    }

    private void addThumbnails() {
	add(new ListView("thumbnails", new PropertyModel(getDefaultModel(), "images")) {
	    private static final long serialVersionUID = 1L;

	    @Override
	    protected void populateItem(final ListItem item) {
		final LightBoxImage modelObject = (LightBoxImage) item.getModelObject();

		item.add(new ThumbnailImage("thumbnailImage", LightBox2Panel.this.getId(), modelObject));
		item.setRenderBodyOnly(true);
	    }
	});
    }

    /**
     * 
     */
    private void addHeaderContributions() {
	// static resources
	// add(HeaderContributor.forJavaScript(this.getClass(),
	// "js/prototype.js"));
	// add(HeaderContributor.forJavaScript(this.getClass(),
	// "js/scriptaculous.js?load=effects"));
	// add(HeaderContributor.forJavaScript(this.getClass(),
	// "js/lightbox.js"));
	// add(JavascriptPackageResource.getHeaderContribution(this.getClass(),
	// "js/allinone.js"));

	// add(JavascriptPackageResource.getHeaderContribution(this.getClass(),
	// "js/prototype.js"));
	add(new JavaScriptReference("prototype", this.getClass(), "js/prototype.js"));

	// add(new JavaScriptReference("scriptaculous", this.getClass(),
	// "js/scriptaculous.js"));
	add(JavascriptPackageResource
		.getHeaderContribution(this.getClass(), "js/scriptaculous.js?load=effects,builder"));

	// add(JavascriptPackageResource.getHeaderContribution(this.getClass(),
	// "js/lightbox.js"));
	// add(new JavaScriptReference("lightbox", this.getClass(),
	// "js/lightbox.js"));
	add(new JavaScriptReference("lightbox", new TextTemplateResourceReference(this.getClass(), "js/lightbox.js",
		"text/javascript", new LoadableDetachableModel<Map<String, Object>>() {
		    private static final long serialVersionUID = 1L;

		    @Override
		    public Map<String, Object> load() {
			// vars
			final String webappContext = WebApplication.get().getServletContext().getContextPath();

			final Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("webappContext", webappContext);
			return vars;
		    }
		})));

	add(CSSPackageResource.getHeaderContribution(this.getClass(), "css/lightbox.css"));
    }
}
