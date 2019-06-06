package panda.tube.sendgrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Personalization {
	public List<Email> to;
	public List<Email> cc;
	public List<Email> bcc;
	public String subject;
	public Map<String, String> headers;
	public Map<String, String> substitutions;
	public Map<String, String> custom_args;
	public Map<String, Object> dynamic_template_data;
	public Long send_at;

	public void addTo(Email email) {
		if (to == null) {
			to = new ArrayList<Email>();
		}
		to.add(email);
	}

	public void addCc(Email email) {
		if (cc == null) {
			cc = new ArrayList<Email>();
		}
		cc.add(email);
	}

	public void addBcc(Email email) {
		if (bcc == null) {
			bcc = new ArrayList<Email>();
		}
		bcc.add(email);
	}

	public void addHeader(String key, String value) {
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		headers.put(key, value);
	}

	public void addSubstitution(String key, String value) {
		if (substitutions == null) {
			substitutions = new HashMap<String, String>();
		}
		substitutions.put(key, value);
	}

	public void addCustomArg(String key, String value) {
		if (custom_args == null) {
			custom_args = new HashMap<String, String>();
		}
		custom_args.put(key, value);
	}

	public void addDynamicTemplateData(String key, Object value) {
		if (dynamic_template_data == null) {
			dynamic_template_data = new HashMap<String, Object>();
		}
		dynamic_template_data.put(key, value);
	}

}
