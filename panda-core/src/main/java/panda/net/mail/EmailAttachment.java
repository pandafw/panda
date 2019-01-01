package panda.net.mail;

import java.io.File;

import panda.lang.Objects;
import panda.lang.Strings;

public class EmailAttachment {
	private String cid;
	private String name;
	private Object data;
	
	public EmailAttachment(File file) throws EmailException {
		this(null, file.getName(), file);
	}

	public EmailAttachment(String name, Object data) throws EmailException {
		this(null, name, data);
	}

	public EmailAttachment(String cid, String name, Object data) throws EmailException {
		if (Strings.isEmpty(name)) {
			throw new EmailException("Empty name of email attachment");
		}
		if (data == null) {
			throw new EmailException("Null data of email attachment");
		}
		this.cid = cid;
		this.name = name;
		this.data = data;
	}

	/**
	 * @return true if cid is not empty
	 */
	public boolean isInline() {
		return Strings.isNotEmpty(cid);
	}
	
	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}

	/**
	 * @param cid the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("cid", cid)
				.append("name", name)
				.append("data", data == null ? data : data.getClass())
				.toString();
	}
}
