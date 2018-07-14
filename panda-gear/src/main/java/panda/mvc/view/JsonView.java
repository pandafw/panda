package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import panda.bind.json.JsonSerializer;
import panda.bind.json.Jsons;
import panda.io.MimeTypes;
import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;

/**
 * serialize json object to output
 */
@IocBean(singleton=false)
public class JsonView extends BindView {
	private static final Log log = Logs.getLog(JsonView.class);

	protected String jsonp;
	
	public JsonView() {
		setContentType(MimeTypes.TEXT_JAVASCRIPT);
	}

	/**
	 * @return the jsonp
	 */
	public String getJsonp() {
		return jsonp;
	}

	/**
	 * @param jsonp the jsonp to set
	 */
	public void setJsonp(String jsonp) {
		this.jsonp = jsonp;
	}

	/**
	 * write result
	 * @param ac action context
	 * @param writer response writer
	 * @param result result object
	 * @throws IOException
	 */
	@Override
	protected void writeResult(ActionContext ac, PrintWriter writer, Object result) throws IOException {
		if (Strings.isEmpty(jsonp)) {
			jsonp = ac.getRequest().getParameter("jsonp");
		}

		if (Strings.isNotEmpty(jsonp)) {
			writer.write(jsonp);
			writer.write('(');
		}
		if (result != null) {
			JsonSerializer js = Jsons.newJsonSerializer();
			setSerializerOptions(js);

			if (log.isDebugEnabled()) {
				if (js.isPrettyPrint()) {
					log.debug(js.serialize(result));
				}
				else {
					js.setPrettyPrint(true);
					log.debug(js.serialize(result));
					js.setPrettyPrint(false);
				}
			}
			js.serialize(result, writer);
		}
		if (Strings.isNotEmpty(jsonp)) {
			writer.write(");");
		}
	}
}
