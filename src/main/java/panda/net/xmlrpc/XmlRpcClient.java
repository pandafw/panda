package panda.net.xmlrpc;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import panda.bind.xmlrpc.XmlRpcDocument;
import panda.bind.xmlrpc.XmlRpcs;
import panda.cast.Castors;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpClient;
import panda.net.http.HttpContentType;
import panda.net.http.HttpHeader;
import panda.net.http.HttpRequest;
import panda.net.http.HttpResponse;


public class XmlRpcClient {
	private static final Log log = Logs.getLog(XmlRpcClient.class);
	
	private Castors castors;
	
	private HttpClient http;
	
	private String url;

	/**
	 * @param url
	 */
	public XmlRpcClient(String url) {
		this.url = url;
		castors = Castors.i();
		http = new HttpClient();
	}

	public <T> T call(String method, Type resultType, Object... params) throws XmlRpcFaultException, IOException {
		XmlRpcDocument<Object> xreq = new XmlRpcDocument<Object>();
		
		xreq.setMethodName(method);

		List<Object> pls = Arrays.toList(params);
		while (pls.size() > 0) {
			if (pls.get(pls.size() - 1) != null) {
				break;
			}
			pls.remove(pls.size() - 1);
		}
		xreq.setParams(pls);
		String xbody = XmlRpcs.toXml(xreq, true, log.isDebugEnabled());

		HttpRequest hreq = HttpRequest.post(url);
		hreq.getHeader().setDefaultAgentPC();
		hreq.getHeader().add(HttpHeader.CONTENT_TYPE, HttpContentType.TEXT_XML);
		hreq.setBody(xbody);
		
		http.setRequest(hreq);
		
		if (log.isDebugEnabled()) {
			log.debug("XML-RPC Call> " + url + " [" + method + "]: " + Streams.LINE_SEPARATOR + xbody);
		}

		StopWatch sw = new StopWatch();
		HttpResponse hres = http.send();
		
		XmlRpcDocument<Object> xres;
		if (hres.isOK()) {
			if (log.isDebugEnabled()) {
				log.debug("XML-RPC Call> " + url + " [" + method + "]: " + hres.getStatusLine() 
					+ Streams.LINE_SEPARATOR
					+ hres.getContentText());
				xres = XmlRpcs.fromXml(hres.getReader(), XmlRpcDocument.class);
				log.debug("XML-RPC Call> " + url + " [" + method + "]: " + hres.getStatusLine() + " (" + sw + ")");
			}
			else if (log.isInfoEnabled()) {
				xres = XmlRpcs.fromXml(hres.getReader(), XmlRpcDocument.class);
				log.info("XML-RPC Call> " + url + " [" + method + "]: " + hres.getStatusLine() + " (" + sw + ")");
			}
			else {
				xres = XmlRpcs.fromXml(hres.getReader(), XmlRpcDocument.class);
			}
			
			if (xres.getFault() != null) {
				throw new XmlRpcFaultException(xres.getFault());
			}
			
			T ores = castors.cast(xres.getParams(), resultType);
			return ores;
		}

		log.warn("XML-RPC Call Failed> " + url + " [" + method + "]: " + hres.getStatusLine() 
			+ Streams.LINE_SEPARATOR
			+ hres.getContentText());

		throw new XmlRpcFaultException(hres.getStatusCode(), hres.getStatusReason());
	}
}
