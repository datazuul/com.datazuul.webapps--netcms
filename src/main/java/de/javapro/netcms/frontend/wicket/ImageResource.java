package de.javapro.netcms.frontend.wicket;

import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.util.string.StringValueConversionException;
import org.apache.wicket.util.value.ValueMap;

import com.datazuul.commons.cms.backend.NetCMSRepository;
import com.datazuul.commons.cms.domain.DomainName;
import com.datazuul.commons.cms.domain.Image;

public class ImageResource extends DynamicWebResource {

    @Override
    protected ResourceState getResourceState() {
	long id = -1;
	final ValueMap params = getParameters();
	if (params != null) {
	    if (params.containsKey("id")) {
		try {
		    id = params.getLong("id");
		} catch (final StringValueConversionException e) {
		    // invalid id show error message
		    // TODO
		}
		final String imageSize = params.getString("size");
		final NetCMSRepository repository = NetCMSRepository.getInstance();

		final DomainName dn = ((NetCMSApplication) NetCMSApplication.get()).getDomainName();
		final Image image = repository.getImageById(dn, id);
		final ImageResourceState state = new ImageResourceState(image, imageSize);
		return state;
	    }
	}
	return null;
    }

}
