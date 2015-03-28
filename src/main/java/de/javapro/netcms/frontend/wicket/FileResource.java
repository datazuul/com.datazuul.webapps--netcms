package de.javapro.netcms.frontend.wicket;

import org.apache.wicket.Resource;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

public class FileResource extends Resource {

    private final File file;

    public FileResource(final java.io.File file) {
	this.file = new File(file);
    }

    @Override
    public IResourceStream getResourceStream() {
	return new FileResourceStream(file);
    }

}
