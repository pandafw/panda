package panda.mvc.view.tag;

import java.io.Writer;

import panda.ioc.annotation.IocBean;
import panda.mvc.MvcException;
import panda.servlet.HttpServletSupport;


/**
 * <!-- START SNIPPET: javadoc -->
 *
 * Write http header.
 *
 * <p/>
 *
 * Configurable attributes are :-
 * <ul>
 *    <li>contentType</li>
 *    <li>charset</li>
 *    <li>bom</li>
 *    <li>attachment</li>
 *    <li>fileName</li>
 *    <li>maxAge</li>
 * </ul>
 *
 * <p/>
 *
 * <!-- END SNIPPET: javadoc -->
 *
 * <p/> <b>Examples</b>
 * <pre>
 *  <!-- START SNIPPET: example -->
 *  &lt;r:head maxAge="0" /&gt;
 *  <!-- END SNIPPET: example -->
 * </pre>
 *
 */
@IocBean(singleton=false)
public class Head extends Component {
	protected HttpServletSupport hss = new HttpServletSupport();
	
	/**
	 * Callback for the end tag of this component. Should the body be evaluated again?
	 * <p/>
	 * <b>NOTE:</b> will pop component stack.
	 * 
	 * @param writer the output writer.
	 * @param body the rendered body.
	 * @return true if the body should be evaluated again
	 */
	public boolean end(Writer writer, String body) {
		super.end(writer, body);

		try {
			hss.setRequest(context.getRequest());
			hss.setResponse(context.getResponse());
			hss.writeResponseHeader();
		}
		catch (Exception e) {
			throw new MvcException(e);
		}
		return false;
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxage(int maxAge) {
		hss.setMaxAge(maxAge);
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(int maxAge) {
		hss.setMaxAge(maxAge);
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.hss.setContentType(contentType);
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(Boolean attachment) {
		this.hss.setAttachment(attachment);
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.hss.setFileName(filename);
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.hss.setCharset(charset);
	}

	/**
	 * @param bom the bom to set
	 */
	public void setBom(Boolean bom) {
		this.hss.setBom(bom);
	}
}

