package panda.tool.codegen.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import panda.lang.Arrays;
import panda.lang.Strings;

/**
 * <p>
 * Java class for InputUI complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;InputUI&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;field&quot; type=&quot;{panda.tool.codegen}InputField&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;param&quot; type=&quot;{panda.tool.codegen}Param&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;header&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; maxOccurs=&quot;1&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;footer&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; maxOccurs=&quot;1&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;generate&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;focus&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;theme&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;formId&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;template&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;extend&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "InputUI")
public class InputUI implements Comparable<InputUI> {

	@XmlElement(name = "field")
	private List<InputField> fieldList;
	@XmlElement(name = "param")
	private List<Param> paramList;
	@XmlElement
	private String header;
	@XmlElement
	private String footer;

	@XmlAttribute
	private Boolean generate;
	@XmlAttribute
	private Boolean focus = true;
	@XmlAttribute
	private String theme;
	@XmlAttribute
	private String formId;
	@XmlAttribute
	private String template;
	@XmlAttribute
	private String extend;
	@XmlAttribute(required = true)
	private String name;

	/**
	 * Constructor
	 */
	public InputUI() {
	}

	/**
	 * Constructor - copy properties from source
	 * 
	 * @param iui source input ui
	 */
	public InputUI(InputUI iui) {
		this.generate = iui.generate;
		this.focus = iui.focus;
		this.theme = iui.theme;
		this.formId = iui.formId;
		this.template = iui.template;
		this.extend = iui.extend;
		this.name = iui.name;

		fieldList = new ArrayList<InputField>();
		for (InputField ifd : iui.getFieldList()) {
			fieldList.add(new InputField(ifd));
		}

		paramList = new ArrayList<Param>();
		for (Param p : iui.getParamList()) {
			paramList.add(new Param(p));
		}
		
		this.header = iui.header;
		this.footer = iui.footer;
	}

	/**
	 * extend inputui
	 * 
	 * @param src source inputui
	 * @param parent extend inputui
	 * @return inputui
	 */
	public static InputUI extend(InputUI src, InputUI parent) {
//		System.out.println("extend input " + src.getName() + " from " + parent.getName());
		InputUI me = new InputUI(parent);

		if (src.generate != null) {
			me.generate = src.generate;
		}
		if (src.focus != null) {
			me.focus = src.focus;
		}
		if (src.theme != null) {
			me.theme = src.theme;
		}
		if (src.formId != null) {
			me.formId = src.formId;
		}
		if (src.template != null) {
			me.template = src.template;
		}
		if (src.name != null) {
			me.name = src.name;
		}
		if (src.header != null) {
			me.header = src.header;
		}
		if (src.footer != null) {
			me.footer = src.footer;
		}

		List<InputField> mifList = me.getFieldList();
		List<InputField> sifList = src.getFieldList();
		for (InputField sif : sifList) {
			boolean add = false;
			for (int i = 0; i < mifList.size(); i++) {
				InputField mif = mifList.get(i);

				if (mif.getName().equals(sif.getName())) {
					mifList.set(i, InputField.extend(sif, mif));
					add = true;
					break;
				}
			}
			if (!add) {
				mifList.add(new InputField(sif));
			}
		}

		List<Param> mpList = me.getParamList();
		List<Param> spList = src.getParamList();
		for (Param sp : spList) {
			boolean add = false;
			for (int i = 0; i < mpList.size(); i++) {
				Param mp = mpList.get(i);
				if (mp.getName().equals(sp.getName())) {
					mp.setValue(sp.getValue());
					add = true;
					break;
				}
			}
			if (!add) {
				mpList.add(new Param(sp));
			}
		}

		return me;
	}

	/**
	 * @return the ordered field list which InputField.display is not false
	 */
	public Set<InputField> getDisplayFieldList() {
		Set<InputField> set = new TreeSet<InputField>();
		List<InputField> list = getFieldList();
		for (int i = 0; i < list.size(); i++) {
			InputField f = list.get(i);

			if (f.getOrder() == null) {
				f.setOrder((i + 1) * 100);
			}

			if (!Boolean.FALSE.equals(f.getDisplay())) {
				set.add(f);
			}
		}
		return set;
	}

	public InputField getFieldByName(String name) {
		for (InputField f : getFieldList()) {
			if (f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}
	
	/**
	 * @return the fieldList
	 */
	public List<InputField> getFieldList() {
		if (fieldList == null) {
			fieldList = new ArrayList<InputField>();
		}
		return this.fieldList;
	}

	/**
	 * @return the paramList
	 */
	public List<Param> getParamList() {
		if (paramList == null) {
			paramList = new ArrayList<Param>();
		}
		return this.paramList;
	}

	private Map<String, String> params;

	/**
	 * @return the params map
	 */
	public Map<String, String> getParams() {
		if (params == null) {
			params = new HashMap<String, String>();
			for (Param p : getParamList()) {
				params.put(p.getName(), p.getValue());
			}
		}
		return params;
	}
	
	/**
	 * Gets the value of the templates property.
	 * 
	 * @return templates
	 */
	public List<String> getTemplates() {
		return Arrays.asList(Strings.split(template));
	}

	/**
	 * @return the generate
	 */
	public Boolean getGenerate() {
		return generate;
	}

	/**
	 * @param generate the generate to set
	 */
	public void setGenerate(Boolean generate) {
		this.generate = generate;
	}

	/**
	 * @return the focus
	 */
	public Boolean getFocus() {
		return focus;
	}

	/**
	 * @param focus the focus to set
	 */
	public void setFocus(Boolean focus) {
		this.focus = focus;
	}

	/**
	 * @return the theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * @return the formId
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * @param formId the formId to set
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the extend
	 */
	public String getExtend() {
		return extend;
	}

	/**
	 * @param extend the extend to set
	 */
	public void setExtend(String extend) {
		this.extend = extend;
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
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return tpublic
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @param o compare target
	 * @return -1/0/1
	 */
	public int compareTo(InputUI o) {
		if (this == o) {
			return 0;
		}
		int i = this.name.compareTo(o.name);
		return i == 0 ? this.hashCode() - o.hashCode() : i;
	}

}
