package panda.util;

import java.io.Serializable;

import panda.lang.Objects;

/**
 * code<->text bean object
 * @author yf.frank.wang@gmail.com
 */
public class CodeText implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	private String code;
	private String text;

	/**
	 * constructor
	 */
	public CodeText() {
	}

	/**
	 * constructor
	 * @param code code
	 * @param text text
	 */
	public CodeText(String code, String text) {
		super();
		this.code = code;
		this.text = text;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("code", code)
				.append("text", text)
				.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodes(code, text);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		CodeText rhs = (CodeText) obj;
		return Objects.equalsBuilder()
				.append(code, rhs.code)
				.append(text, rhs.text)
				.isEquals();
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public CodeText clone() {
		CodeText clone = new CodeText();

		clone.code = this.code;
		clone.text = this.text;
		
		return clone;
	}

}
