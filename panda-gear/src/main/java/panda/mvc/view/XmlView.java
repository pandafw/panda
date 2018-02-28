package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import panda.bind.xml.XmlSerializer;
import panda.bind.xml.Xmls;
import panda.io.MimeTypes;
import panda.ioc.annotation.IocBean;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;

/**
 * serialize XML object to output
 */
@IocBean(singleton=false)
public class XmlView extends AbstractBindView {

	protected static final Log log = Logs.getLog(XmlView.class);

	public static final XmlView DEFAULT = new XmlView();

	private String rootName = "result";
	
	/**
	 * Constructor.
	 */
	public XmlView() {
		setContentType(MimeTypes.TEXT_XML);
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
			setSerializerOptions(xs);

			if (log.isDebugEnabled()) {
				if (xs.isPrettyPrint()) {
					log.debug(xs.serialize(result));
				}
				else {
					xs.setPrettyPrint(true);
					log.debug(xs.serialize(result));
					xs.setPrettyPrint(false);
				}
			}
			xs.serialize(result, writer);
		}
	}
}
