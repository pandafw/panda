package panda.tube.sendgrid;

import java.io.IOException;

import panda.bind.json.Jsons;
import panda.io.MimeTypes;
import panda.net.Scheme;
import panda.net.http.HttpClient;
import panda.net.http.HttpHeader;
import panda.net.http.HttpRequest;
import panda.net.http.HttpResponse;
import panda.net.http.HttpStatus;

public class SendGrid {
	private String scheme = Scheme.HTTPS;
	private String host = "api.sendgrid.com";
	private String version = "v3";
	private String apiKey;
	private boolean compress;

	public SendGrid() {
	}

	public SendGrid(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	/**
	 * @return the compress
	 */
	public boolean isCompress() {
		return compress;
	}

	/**
	 * @param compress the compress to set
	 */
	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	private String getUrl(String api) {
		return scheme + "://" + host + '/' + version + api;
	}

	public void mailSend(Mail mail) throws SendGridException {
		HttpClient hc = new HttpClient();

		HttpRequest hreq = hc.getRequest();
		
		hreq.setUrl(getUrl("/mail/send"));
		hreq.setAuthentication("Bearer " + apiKey);
		hreq.setUserAgent(HttpRequest.DEFAULT_USERAGENT);
		hreq.setContentType(MimeTypes.APP_JSON);
		if (compress) {
			hreq.setContentEncoding(HttpHeader.CONTENT_ENCODING_GZIP);
		}
		hreq.setBody(Jsons.toJson(mail, true));
		
		try {
			HttpResponse hr = hc.doPost();
			if (hr.getStatusCode() == HttpStatus.SC_ACCEPTED) {
				return;
			}
			
			throw new SendGridException(hr.getStatusCode(), "Failed to send mail: " + hr.getStatusLine(), hr.getContentText());
		}
		catch (SendGridException e) {
			throw e;
		}
		catch (IOException e) {
			throw new SendGridException("Failed to send mail: " + e.getMessage(), e);
		}
	}
}
