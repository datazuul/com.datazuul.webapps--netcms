package de.javapro.netcms.frontend.wicket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;

public class ByteArrayResourceStream implements IResourceStream {
    ByteArrayInputStream is = null;

    private Locale locale = null;

    private byte[] content = null;

    private String contentType = null;

    public ByteArrayResourceStream(final byte[] content, final String contentType) {
	this.content = content;
	this.contentType = contentType;
	is = new ByteArrayInputStream(content);
    }

    public void close() throws IOException {
	is.close();
    }

    public String getContentType() {
	return contentType;
    }

    public InputStream getInputStream() throws ResourceStreamNotFoundException {

	return is;
    }

    public Locale getLocale() {
	return locale;
    }

    public long length() {
	return content.length;
    }

    public void setLocale(final Locale locale) {
	this.locale = locale;
    }

    public Time lastModifiedTime() {
	return Time.now();
    }

}
