package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import panda.bind.xml.XmlSerializer;
import panda.bind.xml.Xmls;
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
 * serialize XML object to output
 */
@IocBean(singleton=false)
public class XmlView extends BindView {
	private static final Log log = Logs.getLog(XmlView.class);

	@IocInject
	protected Settings settings;

	protected String rootName;
	
	/**
	 * Constructor.
	 */
	public XmlView() {
		setContentType(MimeTypes.TEXT_XML);
	}

	/**
	 * @param prettyPrint the prettyPrint to set
	 */
	@IocInject(value=MvcConstants.VIEW_JSON_PRETTY, required=false)
	public void setPrettyPrint(boolean prettyPrint) {
		super.setPrettyPrint(prettyPrint);
	}

	/**
	 * @return the rootName
	 */
	public String getRootName() {
		return rootName;
	}

	/**
	 * @param rootName the rootName to set
	 */
	public void setRootName(String rootName) {
		this.rootName = rootName;
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
		if (result != null) {
			XmlSerializer xs = Xmls.newXmlSerializer();
			if (Strings.isNotEmpty(rootName)) {
				xs.setRootName(rootName);
			}
			setSerializerOptions(xs);
			xs.setPrettyPrint(settings.getPropertyAsBoolean(SetConstants.MVC_VIEW_XML_PRETTY, prettyPrint));

			if (log.isDebugEnabled()) {
				String xml = xs.serialize(result);
				log.debug(xml);
				writer.write(xml);
			}
			else {
				xs.serialize(result, writer);
			}
		}
	}
}
