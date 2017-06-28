package panda.mvc.view.tag.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.Mvcs;
import panda.mvc.util.MvcURLBuilder;
import panda.vfs.FileItem;


@IocBean(singleton=false)
public class Uploader extends InputUIBean {
	protected String accept;
	protected boolean multiple;
	protected Integer size;
	
	@IocInject
	protected MvcURLBuilder uploader;
	
	@IocInject
	protected MvcURLBuilder dnloader;
	
	@IocInject
	protected MvcURLBuilder defaulter;

	protected String uploadLink;
	protected String uploadName;
	protected String uploadData;
	protected String dnloadLink;
	protected String dnloadName;
	protected String dnloadData;
	protected String defaultLink;
	protected String defaultText;
	protected Map<String, Object> defaultParams;
	protected boolean defaultEnable;

	@Override
	protected void evaluateParams() {
		super.evaluateParams();

		// check value type
		if (value != null && !(value instanceof FileItem)) {
			value = null;
		}

		if (uploadLink == null) {
			uploadLink = uploader.build();
		}
		
		if (dnloadLink == null) {
			dnloadLink = dnloader.build();
		}
		
		if (defaultLink == null) {
			if (defaultParams == null) {
				defaultLink = defaulter.build();
			}
			else {
				for (Entry<String, Object> en : defaultParams.entrySet()) {
					Object v = Mvcs.evaluate(context, en.getValue());
					en.setValue(v);
				}
				defaulter.setParams(defaultParams);
				defaultLink = defaulter.build();
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
	 * @return the multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * @param multiple the multiple to set
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
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
	 * @param uploadAction the upload action to set
	 */
	public void setUploadAction(String uploadAction) {
		uploader.setAction(uploadAction);
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
	 * @return the uploadData
	 */
	public String getUploadData() {
		return uploadData;
	}

	/**
	 * @param uploadData the uploadData to set
	 */
	public void setUploadData(String uploadData) {
		this.uploadData = uploadData;
	}

	/**
	 * @param dnloadAction the download action to set
	 */
	public void setDnloadAction(String dnloadAction) {
		dnloader.setAction(dnloadAction);
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

	/**
	 * @return the dnloadName
	 */
	public String getDnloadName() {
		return dnloadName;
	}

	/**
	 * @param dnloadName the dnloadName to set
	 */
	public void setDnloadName(String dnloadName) {
		this.dnloadName = dnloadName;
	}

	/**
	 * @return the dnloadData
	 */
	public String getDnloadData() {
		return dnloadData;
	}

	/**
	 * @param dnloadData the dnloadData to set
	 */
	public void setDnloadData(String dnloadData) {
		this.dnloadData = dnloadData;
	}

	/**
	 * @param defaultAction the default action to set
	 */
	public void setDefaultAction(String defaultAction) {
		defaulter.setAction(defaultAction);
	}

	/**
	 * @return the defaultLink
	 */
	public String getDefaultLink() {
		return defaultLink;
	}

	/**
	 * @param defaultLink the defaultLink to set
	 */
	public void setDefaultLink(String defaultLink) {
		this.defaultLink = defaultLink;
	}

	/**
	 * @return the defaultParams
	 */
	public Map<String, Object> getDefaultParams() {
		return defaultParams;
	}

	/**
	 * @param defaultParams the defaultParams to set
	 */
	public void setDefaultParams(Map<String, Object> defaultParams) {
		this.defaultParams = defaultParams;
	}

	/**
	 * @return the defaultText
	 */
	public String getDefaultText() {
		return defaultText;
	}

	/**
	 * @param defaultText the defaultText to set
	 */
	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	/**
	 * @return the defaultEnable
	 */
	public boolean isDefaultEnable() {
		return defaultEnable;
	}

	/**
	 * @param defaultEnable the defaultEnable to set
	 */
	public void setDefaultEnable(boolean defaultEnable) {
		this.defaultEnable = defaultEnable;
	}

	//----------------------------------------------
	@SuppressWarnings("unchecked")
	public Collection<FileItem> getFileItems() {
		if (value == null) {
			return null;
		}
		if (value instanceof Collection) {
			return (Collection<FileItem>)value;
		}
		if (value instanceof FileItem[]) {
			return Arrays.asList((FileItem[])value);
		}
		if (value instanceof FileItem) {
			return Arrays.asList((FileItem)value);
		}
		throw new IllegalArgumentException("The uploader value is not a Collection/Array/FileItem object: " + value.getClass());
	}
}
