package panda.ex.slack;

import java.io.IOException;

import panda.bind.json.Jsons;
import panda.io.MimeType;
import panda.lang.Charsets;
import panda.net.http.HttpClient;
import panda.net.http.HttpMethod;
import panda.net.http.HttpRequest;
import panda.net.http.HttpResponse;

public class Slacks {
	public static String sendWebHook(String url, Message msg) throws IOException {
		HttpClient hc = new HttpClient();
		HttpRequest hreq = hc.getRequest();
		hreq.setUrl(url)
			.setContentType(MimeType.APP_JSON)
			.setMethod(HttpMethod.POST);
		hreq.setEncoding(Charsets.UTF_8);
		
		String body = Jsons.toJson(msg, true);
		hreq.setBody(body);
		
		HttpResponse hres = hc.send();
		return hres.getContentText();
	}
}
