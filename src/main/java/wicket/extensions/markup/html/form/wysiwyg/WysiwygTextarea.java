package wicket.extensions.markup.html.form.wysiwyg;

import org.apache.wicket.PageMap;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.JavascriptUtils;

/**
 * @author Ralf Eichinger (pixotec)
 */
public class WysiwygTextarea extends Panel {
	private WysiwygTextareaSettings settings;

	private static final CompressedResourceReference EDITOR_JS = new CompressedResourceReference(
			WysiwygTextarea.class, "res/wysiwygTextarea.js");

	private static final CompressedResourceReference DIALOG_TABLE_HTML = new CompressedResourceReference(
			WysiwygTextarea.class, "res/dialogTable.htm");

	private static final ResourceReference IMG_BTN_BOLD_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnBold_up.gif");

	private static final ResourceReference IMG_BTN_HTML_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnHTML_up.gif");

	private static final ResourceReference IMG_BTN_IMAGE_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnImage_up.gif");

	private static final ResourceReference IMG_BTN_INDENT_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnIndent_up.gif");

	private static final ResourceReference IMG_BTN_ITALIC_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnItalic_up.gif");

	private static final ResourceReference IMG_BTN_JUSTIFY_CENTER_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnJustifyCenter_up.gif");

	private static final ResourceReference IMG_BTN_JUSTIFY_LEFT_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnJustifyLeft_up.gif");

	private static final ResourceReference IMG_BTN_JUSTIFY_RIGHT_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnJustifyRight_up.gif");

	private static final ResourceReference IMG_BTN_ORDERED_LIST_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnOrderedList_up.gif");

	private static final ResourceReference IMG_BTN_OUTDENT_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnOutdent_up.gif");

	private static final ResourceReference IMG_BTN_REDO_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnRedo_up.gif");

	private static final ResourceReference IMG_BTN_SUBSCRIPT_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnSubscript_up.gif");

	private static final ResourceReference IMG_BTN_SUPERSCRIPT_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnSuperscript_up.gif");

	private static final ResourceReference IMG_BTN_TABLE_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnTable_up.gif");

	private static final ResourceReference IMG_BTN_UNDERLINE_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnUnderline_up.gif");

	private static final ResourceReference IMG_BTN_UNDO_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnUndo_up.gif");

	private static final ResourceReference IMG_BTN_UNORDERED_LIST_UP = new ResourceReference(
			WysiwygTextarea.class, "res/btnUnorderedList_up.gif");

	private HiddenField hiddenHtmlField = null;

	public WysiwygTextarea(String id, IModel model,
			WysiwygTextareaSettings settings) {
		super(id, model);

		this.settings = settings;

		hiddenHtmlField = new HiddenField("html", model);
		add(hiddenHtmlField);

		add(new Image("btnBold", IMG_BTN_BOLD_UP));
		add(new Image("btnHTML", IMG_BTN_HTML_UP));
		add(new Image("btnIndent", IMG_BTN_INDENT_UP));
		add(new Image("btnItalic", IMG_BTN_ITALIC_UP));
		add(new Image("btnJustifyCenter", IMG_BTN_JUSTIFY_CENTER_UP));
		add(new Image("btnJustifyLeft", IMG_BTN_JUSTIFY_LEFT_UP));
		add(new Image("btnJustifyRight", IMG_BTN_JUSTIFY_RIGHT_UP));
		add(new Image("btnOrderedList", IMG_BTN_ORDERED_LIST_UP));
		add(new Image("btnOutdent", IMG_BTN_OUTDENT_UP));
		add(new Image("btnRedo", IMG_BTN_REDO_UP));
		add(new Image("btnSubscript", IMG_BTN_SUBSCRIPT_UP));
		add(new Image("btnSuperscript", IMG_BTN_SUPERSCRIPT_UP));
		add(new Image("btnTable", IMG_BTN_TABLE_UP));
		add(new Image("btnUnderline", IMG_BTN_UNDERLINE_UP));
		add(new Image("btnUndo", IMG_BTN_UNDO_UP));
		add(new Image("btnUnorderedList", IMG_BTN_UNORDERED_LIST_UP));
		PopupSettings popupSettings = new PopupSettings(PageMap
				.forName("popup"), PopupSettings.RESIZABLE);
		popupSettings.setWidth(700);
		popupSettings.setHeight(550);
		PageLink lnkDialogImage = new PageLink("lnkDialogImage",
				new DialogImagePage(settings));
		lnkDialogImage.setPopupSettings(popupSettings);
		lnkDialogImage.add(new Image("btnImage", IMG_BTN_IMAGE_UP));
		add(lnkDialogImage);
	}

	protected void onBeforeRender() {
		super.onBeforeRender();

		StringBuffer js = new StringBuffer();
		js.append(JavascriptUtils.SCRIPT_OPEN_TAG);

		// var width
		js.append("var width=" + getWidth() + ";\n");

		// var height
		js.append("var height=" + getHeight() + ";\n");

		// var dataFieldId
		String dataFieldId = hiddenHtmlField.getInputName();
		js.append("var dataFieldId='" + dataFieldId + "';\n");

		// var dialogTableUrl
		CharSequence charSequence = RequestCycle.get()
				.urlFor(DIALOG_TABLE_HTML);
		js.append("var dialogTableUrl='" + charSequence.toString() + "';\n");

		js.append(JavascriptUtils.SCRIPT_CLOSE_TAG);
		add(new StringHeaderContributor(js.toString()));

		// change id of hiddenHtmlField to dataFieldId to be accessed with in
		// javascript
		hiddenHtmlField.add(new SimpleAttributeModifier("id", dataFieldId));

		// add onSubmit to Form
		getParent()
				.add(
						new SimpleAttributeModifier("onSubmit",
								"return submitForm();"));

		// add wysiwygTextarea.js-link to header
		getPage().add(HeaderContributor.forJavaScript(EDITOR_JS));
	}

	private int getHeight() {
		if (settings == null || settings.getHeight() == -1) {
			return 400;
		}
		return settings.getHeight();
	}

	private int getWidth() {
		if (settings == null || settings.getWidth() == -1) {
			return 800;
		}
		return settings.getWidth();
	}
}
