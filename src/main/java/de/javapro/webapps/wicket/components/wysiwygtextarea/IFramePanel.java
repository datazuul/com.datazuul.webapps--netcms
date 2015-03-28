package de.javapro.webapps.wicket.components.wysiwygtextarea;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Response;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.JavaScriptReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.JavascriptUtils;

public class IFramePanel extends Panel {
	boolean isIE;

	public IFramePanel(String id, IModel model, boolean isIE, String html,
			int width, int height) {
		super(id);

		this.isIE = isIE;

		// add a reference to the panel's javascript
		add(new JavaScriptReference("editorMain", WysiwygTextarea.class,
				"WysiwygTextarea.js"));
		add(new Image("imgBold", "images/bold.gif"));
		add(new Image("imgUnderline", "images/underline.gif"));
		add(new Image("imgItalic", "images/italic.gif"));
		add(new Image("imgJustifyLeft", "images/j_left.gif"));
		add(new Image("imgJustifyCenter", "images/j_center.gif"));
		add(new Image("imgJustifyRight", "images/j_right.gif"));
		add(new Image("imgIndent", "images/indent.gif"));
		add(new Image("imgOutdent", "images/outdent.gif"));
		add(new Image("imgUndo", "images/undo.gif"));
		add(new Image("imgRedo", "images/redo.gif"));

		// set id of iframe-tag
		WebMarkupContainer iframe = new WebMarkupContainer("iframe");
		iframe.add(new AttributeModifier("id", true, new Model() {
			public Serializable getObject() {
				return getId();
			}
		}));
		iframe.add(new WysiwygBehaviour(html));
		add(iframe);

		WebMarkupContainer htmlContent = new WebMarkupContainer("hiddeneditor");
		htmlContent.setDefaultModel(model);
		add(htmlContent);
	}

	private class WysiwygBehaviour extends AbstractBehavior {
		private String html;

		public WysiwygBehaviour(String html) {
			this.html = html;
		}

		public void onRendered(Component component) {
			Response response = component.getResponse();
			final String id = component.getParent().getId();

			response.write(JavascriptUtils.SCRIPT_OPEN_TAG);

			// turn on design mode
			// ===================
			response.write("var mainContent=\"<html id='" + id
					+ "'><head></head><body>" + html + "</body></html>\";\n");
			// assign the frame(textarea) to the edit variable using that frames
			// id
			response.write("var edit = document.getElementById(\"" + id
					+ "\").contentWindow.document;\n");
			// write the content to the textarea
			response.write("edit.write(mainContent);\n");
			// enable the designMode
			response.write("edit.designMode=\"On\";\n");
			// enable the designMode for Mozilla
			if (!isIE) {
				response.write("document.getElementById(\"" + id
						+ "\").contentDocument.designMode=\"on\";\n");
			}

			// function for calling wysiwyg commands
			// =====================================
			response.write("\n\n");
			response.write("function doEditorCommand(command, option) {\n");
			response.write("  editorCommand(\'" + id
					+ "\', command, option);\n");
			response.write("}\n");
			response.write(JavascriptUtils.SCRIPT_CLOSE_TAG);
		}
	}
}
