package panda.mvc.view.tag.ui;

import panda.io.MimeTypes;
import panda.ioc.annotation.IocBean;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpMethod;


/**
 * <!-- START SNIPPET: javadoc --> Renders an HTML file input element. <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;s:file name=&quot;anUploadFile&quot; accept=&quot;text/*&quot; /&gt;
 * &lt;s:file name=&quot;anohterUploadFIle&quot; accept=&quot;text/html,text/plain&quot; /&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@IocBean(singleton=false)
public class File extends InputUIBean {
	private static final Log LOG = Logs.getLog(File.class);

	protected String accept;
	protected Integer size;
	protected Boolean multiple;

	/**
	 * evaluate parameters
	 */
	public void evaluateParams() {
		super.evaluateParams();

		Form form = (Form)findAncestor(Form.class);
		if (form != null) {
			String encType = form.getEnctype();
			if (!MimeTypes.MULTIPART_FORM_DATA.equals(encType)) {
				// uh oh, this isn't good! Let's warn the developer
				LOG.warn("A file upload UI tag (r.file) being used without a form set to enctype 'multipart/form-data'. This is probably an error!");
			}

			String method = form.getMethod();
			if (!HttpMethod.POST.equalsIgnoreCase(method)) {
				// uh oh, this isn't good! Let's warn the developer
				LOG.warn("A file upload UI tag (r.file) being used without a form set to method 'POST'. This is probably an error!");
			}
		}
	}

	/**
	 * @return the accept
	 */
	public String getAccept() {
		return accept;
	}

	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param accept the accept to set
	 */
	public void setAccept(String accept) {
		this.accept = accept;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * @return the multiple
	 */
	public Boolean getMultiple() {
		return multiple;
	}

	/**
	 * @param multiple the multiple to set
	 */
	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}
}
