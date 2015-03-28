package de.javapro.netcms.frontend.wicket;

import org.apache.wicket.markup.html.DynamicWebResource.ResourceState;

import com.datazuul.commons.cms.domain.Image;

public class ImageResourceState extends ResourceState {

    private final Image image;

    private final String propsId;

    public ImageResourceState(final Image image, final String propsId) {
	this.image = image;
	this.propsId = propsId;
    }

    @Override
    public String getContentType() {
	return "image/" + image.getFormat();
    }

    @Override
    public byte[] getData() {
	if (image.getPropsThumbnail().getId().equals(propsId)) {
	    return image.getPropsThumbnail().getBytes();
	} else if (image.getPropsPreview().getId().equals(propsId)) {
	    return image.getPropsPreview().getBytes();
	} else {
	    return image.getPropsOriginal().getBytes();
	}

    }

}
