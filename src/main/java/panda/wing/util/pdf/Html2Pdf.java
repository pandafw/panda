package panda.wing.util.pdf;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

public interface Html2Pdf {
	void process(OutputStream os, HttpServletRequest request, String url, String charset) throws Exception;
}
