package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import panda.bind.json.JsonSerializer;
import panda.io.MimeType;
import panda.log.Log;
import panda.log.Logs;

/**
 * serialize json object to output
 */
public class JsonView extends AbstractBindView {
	private static final Log log = Logs.getLog(JsonView.class);

	public static final JsonView DEFAULT = new JsonView("");
	
	public JsonView(String location) {
		super(location);
		setContentType(MimeType.TEXT_JAVASCRIPT);
	}

	/**
	 * write result
	 * @param writer response writer
	 * @param result result object
	 * @throws IOException
	 */
	@Override
	protected void writeResult(PrintWriter writer, Object result) throws IOException {
		if (result != null) {
			JsonSerializer js = new JsonSerializer();
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
	}
}
