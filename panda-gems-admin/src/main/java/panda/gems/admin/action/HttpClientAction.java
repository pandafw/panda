package panda.gems.admin.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.app.action.BaseAction;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpClient;
import panda.net.http.HttpResponse;

public class HttpClientAction extends BaseAction {
	
	private static final Log log = Logs.getLog(HttpClientAction.class);
	
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
		HttpServletResponse response = getResponse();
		
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
	
	public void doGet() {
		try {
			HttpResponse hr = HttpClient.get(__url);

			transferResponse(hr);
		}
		catch (Exception e) {
			log.warn("Failed to get " + __url, e);
			sendError(e);
		}
	}
	
	public void doPost() {
		try {
			HttpServletRequest request = getRequest();
			
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
	}

	private void sendError(Exception e) {
		String stack = Exceptions.getStackTrace(e);
		
		HttpServletResponse response = getResponse();
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
	 * @throws Exception if an error occurs
	 */
	public void execute() throws Exception {
		doGet();
	}

}
