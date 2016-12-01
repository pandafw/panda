package panda.wing.util.pdf;

import java.io.OutputStream;
import java.util.Map;

public interface Html2Pdf {
	void process(OutputStream os, String url, String charset, Map<String, Object> headers) throws Exception;
}
