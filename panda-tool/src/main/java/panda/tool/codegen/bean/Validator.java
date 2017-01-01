package panda.tool.codegen.bean;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Collections;

/**
 * <p>
 * Java class for Validator complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Validator&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;param&quot; type=&quot;{panda.tool.codegen}Param&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;type&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;refer&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;msgId&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;message&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Validator", propOrder = { "paramList" })
public class Validator {

	@XmlElement(name = "param")
	private List<Param> paramList;

	@XmlAttribute(required = true)
	private String type;

	@XmlAttribute
	private String refer;

	@XmlAttribute
	private String message;

	@XmlAttribute
	private String msgId;

	/**
	 * Constructor
	 */
	public Validator() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param validator source validator
	 */
	public Validator(Validator validator) {
		this.type = validator.type;
		this.refer = validator.refer;
		this.message = validator.message;
		this.msgId = validator.msgId;

		paramList = new LinkedList<Param>();
		for (Param p : validator.getParamList()) {
			paramList.add(new Param(p));
		}
	}

	/**
	 * @return the paramList
	 */
	public List<Param> getParamList() {
		if (paramList == null) {
			paramList = new LinkedList<Param>();
		}
		return this.paramList;
	}

	public boolean isHasParams() {
		return Collections.isNotEmpty(paramList);
	}
	
	public String getParams() {
		if (paramList == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		for (int i = 0; i < paramList.size(); i++) {
			Param p = paramList.get(i);
			sb.append("'").append(p.getName()).append("': ").append(p.getValue());
			if (i < paramList.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append(" }");
		return sb.toString();
	}

	/**
	 * @return the refer
	 */
	public String getRefer() {
		return refer;
	}

	/**
	 * @param refer the refer to set
	 */
	public void setRefer(String refer) {
		this.refer = refer;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId == null ? type : msgId;
	}

	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
