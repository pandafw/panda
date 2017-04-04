package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import panda.bind.xml.XmlSerializer;
import panda.bind.xml.Xmls;
import panda.io.MimeType;
import panda.log.Log;
import panda.log.Logs;

/**
 * serialize XML object to output
 */
public class XmlView extends AbstractBindView {

	protected static final Log log = Logs.getLog(XmlView.class);

	public static final XmlView DEFAULT = new XmlView("");

	private String rootName = "result";
	
	/**
	 * Constructor.
	 */
	public XmlView(String location) {
		super(location);
		setContentType(MimeType.TEXT_XML);
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
	 * @param writer response writer
	 * @param result result object
	 * @throws IOException
	 */
	@Override
	protected void writeResult(PrintWriter writer, Object result) throws IOException {
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
