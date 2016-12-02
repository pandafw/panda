package panda.wing.util.pdf;

import java.io.OutputStream;
import java.util.Map;

public abstract class Html2Pdf {
	protected String charset;
	
	protected String url;
	
	protected Map<String, Object> headers;
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public abstract void process(OutputStream os) throws Exception;
}
