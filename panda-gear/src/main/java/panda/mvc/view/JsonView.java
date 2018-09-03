package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import panda.bind.json.JsonSerializer;
import panda.bind.json.Jsons;
import panda.io.MimeTypes;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.SetConstants;

/**
 * serialize json object to output
 */
@IocBean(singleton=false)
public class JsonView extends BindView {
	private static final Log log = Logs.getLog(JsonView.class);

	@IocInject
	protected Settings settings;
	
	protected String jsonp;
	
	public JsonView() {
		setContentType(MimeTypes.APP_JSON);
	}

	/**
	 * @param prettyPrint the prettyPrint to set
	 */
	@IocInject(value=MvcConstants.JSON_VIEW_PRETTY_PRINT, required=false)
	public void setPrettyPrint(boolean prettyPrint) {
		super.setPrettyPrint(prettyPrint);
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

	@Override
	public void render(ActionContext ac) {
		if (Strings.isEmpty(jsonp)) {
			jsonp = ac.getRequest().getParameter("jsonp");
		}

		if (Strings.isNotEmpty(jsonp)) {
			setContentType(MimeTypes.TEXT_JAVASCRIPT);
		}
		
		super.render(ac);
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
		if (Strings.isNotEmpty(jsonp)) {
			writer.write(jsonp);
			writer.write('(');
		}
		if (result != null) {
			JsonSerializer js = Jsons.newJsonSerializer();

			setSerializerOptions(js);
			js.setPrettyPrint(settings.getPropertyAsBoolean(SetConstants.XML_VIEW_PRETTY_PRINT, prettyPrint));

			if (log.isDebugEnabled()) {
				String json = js.serialize(result);
				log.debug(json);
				writer.write(json);
			}
			else {
				js.serialize(result, writer);
			}
		}
		if (Strings.isNotEmpty(jsonp)) {
			writer.write(");");
		}
	}
}
