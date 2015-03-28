package wicket.extensions.markup.html.form.wysiwyg;

import org.apache.wicket.Page;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Fragment;

public class DialogImagePage extends WebPage {
	private WysiwygTextareaSettings settings;

	public static final ResourceReference IMG_NO_IMAGE = new ResourceReference(
			DialogImagePage.class, "res/noimage.jpg");

	private static final ResourceReference IMG_TEST_IMAGE = new ResourceReference(
			DialogImagePage.class, "res/temple.jpg");

	public DialogImagePage(WysiwygTextareaSettings settings) {
		super();
		this.settings = settings;

		addComponents();
	}

	private void addComponents() {
		add(new Image("imgTest", IMG_TEST_IMAGE));

		PageLink selectImagePageLink;
		if (getSelectImagePage() == null) {
			Fragment fragWithoutImageDialog = new Fragment("pnlImageUrl",
					"fragWithoutImageDialog");
			add(fragWithoutImageDialog);
		} else {
			Fragment fragWithImageDialog = new Fragment("pnlImageUrl",
					"fragWithImageDialog");
			SelectImagePage imgPage = settings.getSelectImagePage();
			selectImagePageLink = new PageLink("lnkSelectImage", imgPage);
			PopupSettings popupSettings = new PopupSettings(
					PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS);
			popupSettings.setHeight(imgPage.getHeight());
			popupSettings.setWidth(imgPage.getWidth());
			selectImagePageLink.setPopupSettings(popupSettings);
			fragWithImageDialog.add(selectImagePageLink);

			fragWithImageDialog.add(new Image("imgSelected", IMG_NO_IMAGE));
			add(fragWithImageDialog);
		}
	}

	private Page getSelectImagePage() {
		if (settings != null) {
			return settings.getSelectImagePage();
		}
		return null;
	}
}
