package panda.mvc.view.tag.ui;

import panda.filepool.FileItem;
import panda.io.FileNames;
import panda.ioc.annotation.IocBean;


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
public class Uploader extends InputUIBean {
	protected String accept;
	protected Integer size;
	
	protected String uploadLink;
	protected String uploadName;
	protected String dnloadLink;

	@Override
	protected void evaluateParams() {
		super.evaluateParams();
		
		if (value != null && !(value instanceof FileItem)) {
			throw new IllegalArgumentException("The value of Uploader is not a FileItem object.");
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
	 * @return the uploadLink
	 */
	public String getUploadLink() {
		return uploadLink;
	}

	/**
	 * @param uploadLink the uploadLink to set
	 */
	public void setUploadLink(String uploadLink) {
		this.uploadLink = uploadLink;
	}

	/**
	 * @return the uploadName
	 */
	public String getUploadName() {
		return uploadName;
	}

	/**
	 * @param uploadName the uploadName to set
	 */
	public void setUploadName(String uploadName) {
		this.uploadName = uploadName;
	}

	/**
	 * @return the dnloadLink
	 */
	public String getDnloadLink() {
		return dnloadLink;
	}

	/**
	 * @param dnloadLink the dnloadLink to set
	 */
	public void setDnloadLink(String dnloadLink) {
		this.dnloadLink = dnloadLink;
	}

	//----------------------------------------------
	public FileItem getFileItem() {
		return (FileItem)value;
	}

	public Long getFileId() {
		return value == null ? null : getFileItem().getId();
	}

	public String getFileName() {
		return value == null ? null : getFileItem().getName();
	}

	public Integer getFileSize() {
		return value == null ? null : getFileItem().getSize();
	}

	public String getFileContentType() {
		return value == null ? null : FileNames.getContentTypeFor(getFileItem().getName());
	}

	public boolean isFileExits() {
		return value == null ? false : getFileItem().isExists();
	}
}
