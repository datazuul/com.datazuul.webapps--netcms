package de.javapro.webapps.wicket.components.wysiwygtextarea;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.request.WebClientInfo;

public class WysiwygTextarea extends Panel {
	boolean isEditable = false;

	boolean isIE;

	boolean isGecko;

	boolean isSafari;

	boolean isKonqueror;

	boolean isMozillaFirefox;

	public WysiwygTextarea(String id, IModel model) {
		super(id);

		initiateEditor();

		String html = "<b>hello world</b>";
		int width = 600;
		int height = 300;
		add(displayEditor(id, model, html, width, height));
	}

	private Panel displayEditor(String id, IModel model, String html,
			int width, int height) {
		Panel result = null;
		// // Javascript function displayEditor will create the textarea.
		//
		// function displayEditor(editor, html, width, height) {
		// if(isEditable){
		// document.writeln('<iframe id="' + editor + '" name="' + editor + '"
		// width="' + width + 'px" height="' + height + 'px"></iframe>');
		// // create a hidden field that will hold everything that is typed in
		// the textarea
		// document.writeln('<input type="hidden" id="hidden' + editor + '"
		// name="hidden' + editor + '" value="">');
		// // assign html (textarea value) to hiddeneditor
		// document.getElementById('hidden' + editor).value = html;
		// // call function designer
		// designer(editor, html);
		// } else {
		// document.writeln('<textarea name="' + editor + '" id="' + editor + '"
		// cols="39" rows="10">' + html + '</textarea>');
		// }
		// }
		if (isEditable) {
			result = new IFramePanel(id, model, isIE, html, width, height);
		} else {

		}
		return result;
	}

	private void initiateEditor() {
		// in javascript:
		// // check what browser is in use
		// var browser = navigator.userAgent.toLowerCase();
		// isIE = ((browser .indexOf( "msie" ) != -1) && (browser .indexOf(
		// "opera" ) == -1) && (browser .indexOf( "webtv" ) == -1));
		// isGecko = (browser .indexOf( "gecko" ) != -1);
		// isSafari = (browser .indexOf( "safari" ) != -1);
		// isKonqueror = (browser.indexOf( "konqueror" ) != -1);
		//        
		// //enable designMode if the browser is not safari or konqueror.
		// if (document.getElementById && document.designMode && !isSafari &&
		// !isKonqueror) {
		// isEditable= true;
		// }

		// check what browser is in use
		ClientProperties client = ((WebClientInfo) getSession().getClientInfo())
				.getProperties();
		isMozillaFirefox = client.isBrowserMozillaFirefox();
		isIE = client.isBrowserInternetExplorer();
		isSafari = client.isBrowserSafari();
		isKonqueror = client.isBrowserKonqueror();

		// enable designMode if the browser is not safari or konqueror.
		if (!isSafari && !isKonqueror) {
			isEditable = true;
		}
	}
}
