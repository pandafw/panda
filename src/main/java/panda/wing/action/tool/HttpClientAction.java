package panda.wing.action.tool;

import panda.io.Streams;
import panda.lang.Exceptions;
import panda.net.http.HttpClient;
import panda.net.http.HttpResponse;
import panda.wing.mvc.AbstractAction;
import panda.wing.mvc.util.StrutsContextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpClientAction extends AbstractAction {
	private String __url;
	
	/**
	 * @return the __url
	 */
	public String get__url() {
		return __url;
	}

	/**
	 * @param __url the __url to set
	 */
	public void set__url(String __url) {
		this.__url = __url;
	}

	private void transferResponse(HttpResponse hr) throws Exception {
		HttpServletResponse response = StrutsContextUtils.getServletResponse();
		
		response.setStatus(hr.getStatusCode());
		for (Entry<String, Object> en : hr.getHeader().entrySet()) {
			String key = en.getKey();
			Object val = en.getValue();
			if (val instanceof List) {
				for (Object s : (List)val) {
					response.setHeader(key, s.toString());
				}
			}
			else {
				response.setHeader(key, val.toString());
			}
		}
		
		InputStream is = hr.getRawStream();
		try {
			Streams.copy(is, response.getOutputStream());
		}
		finally {
			Streams.safeClose(is);
		}
	}
	
	public String doGet() {
		try {
			HttpResponse hr = HttpClient.get(__url);

			transferResponse(hr);
		}
		catch (Exception e) {
			log.warn("Failed to get " + __url, e);
			sendError(e);
		}
		return NONE;
	}
	
	public String doPost() {
		try {
			HttpServletRequest request = StrutsContextUtils.getServletRequest();
			
			Map<String, Object> params = new HashMap<String, Object>();
			for (Object o : request.getParameterMap().entrySet()) {
				Entry e = (Entry)o;
				if (e.getKey().toString().startsWith("__")) {
					continue;
				}
				params.put(e.getKey().toString(), e.getValue());
			}

			HttpResponse hr = HttpClient.post(__url, params);
			transferResponse(hr);
		}
		catch (Exception e) {
			log.warn("Failed to post " + __url, e);
			sendError(e);
		}
		return NONE;
	}

	public String doError() {
		try {
			__url.charAt(0);
		}
		catch (Exception e) {
			sendError(e);
		}
		return NONE;
	}
	
	private void sendError(Exception e) {
		String stack = Exceptions.getStackTrace(e);
		HttpServletResponse response = StrutsContextUtils.getServletResponse();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try {
			response.getWriter().write(stack);
		}
		catch (IOException e1) {
			//skip
		}
	}
	
	/**
	 * execute
	 * 
	 * @return INPUT
	 * @throws Exception if an error occurs
	 */
	public String execute() throws Exception {
		return doGet();
	}

}
