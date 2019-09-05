package panda.cgen.mvc.bean;

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
 *         &lt;element name=&quot;field&quot; type=&quot;{panda.cgen.mvc}InputField&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;param&quot; type=&quot;{panda.cgen.mvc}Param&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;header&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; maxOccurs=&quot;1&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;footer&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; maxOccurs=&quot;1&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;generate&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; /&gt;
 *       &lt;attribute name=&quot;headinc&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;footinc&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;focus&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
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
	private String focusme;
	@XmlAttribute
	private String headinc;
	@XmlAttribute
	private String footinc;
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
		this.focusme = iui.focusme;
		this.theme = iui.theme;
		this.headinc = iui.headinc;
		this.footinc = iui.footinc;
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
		if (src.focusme != null) {
			me.focusme = src.focusme;
		}
		if (src.headinc != null) {
			me.headinc = src.headinc;
		}
		if (src.footinc != null) {
			me.footinc = src.footinc;
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
	 * @return the ordered field list which InputField.generate is not false
	 */
	public Set<InputField> getOrderedFieldList() {
		Set<InputField> set = new TreeSet<InputField>();
		List<InputField> list = getFieldList();
		for (int i = 0; i < list.size(); i++) {
			InputField f = list.get(i);

			if (f.getOrder() == null) {
				f.setOrder((i + 1) * 100);
			}

			if (!Boolean.FALSE.equals(f.getGenerate())) {
				set.add(f);
			}
		}
		return set;
	}
	
	/**
	 * <#list ifs as f>${entity.simpleName}.${f.uname}<#if f_has_next>, </#if></#list>
	 * @param en Entity
	 * @return fields string
	 */
	public String getDisplayFields(Entity en) {
		String s = "";
		Set<InputField> ifs = getOrderedFieldList();
		for (InputField f : ifs) {
			if (Boolean.FALSE.equals(f.getDisplay())) {
				continue;
			}
			if (!s.isEmpty()) {
				s += ", ";
			}
			s += en.getSimpleName() + "." + f.getUname();
		}
		return s;
	}
	
	public List<InputField> getRequiredValidateFieldList() {
		List<InputField> set = new ArrayList<InputField>();
		for (InputField f : getOrderedFieldList()) {
			if (Boolean.TRUE.equals(f.getRequired()) && Boolean.TRUE.equals(f.getRequiredvalidate())) {
				set.add(f);
			}
		}
		return set;
	}
	
	public String getRequiredValidateFields() {
		boolean noRefer = true;
		for (InputField f : getRequiredValidateFieldList()) {
			String r = Strings.defaultString(f.getRequiredrefer());
			if (Strings.isNotEmpty(r)) {
				noRefer = false;
			}
		}

		if (noRefer) {
			StringBuilder sb = new StringBuilder();
			sb.append("fields={ ");
			for (InputField f : getRequiredValidateFieldList()) {
				sb.append('"').append(f.getName()).append("\", ");
			}
			sb.setLength(sb.length() - 2);
			sb.append(" }");
			return sb.toString();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("refers=\"{ ");
		for (InputField f : getRequiredValidateFieldList()) {
			sb.append('\'').append(f.getName()).append("': '").append(Strings.defaultString(f.getRequiredrefer())).append("', ");
		}
		sb.setLength(sb.length() - 2);
		sb.append(" }\"");
		return sb.toString();
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
	 * @return the focusme
	 */
	public String getFocusme() {
		return focusme;
	}

	/**
	 * @param focusme the focusme to set
	 */
	public void setFocusme(String focusme) {
		this.focusme = focusme;
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
	 * @return the headinc
	 */
	public String getHeadinc() {
		return headinc;
	}

	/**
	 * @param headinc the headinc to set
	 */
	public void setHeadinc(String headinc) {
		this.headinc = headinc;
	}

	/**
	 * @return the footinc
	 */
	public String getFootinc() {
		return footinc;
	}

	/**
	 * @param footinc the footinc to set
	 */
	public void setFootinc(String footinc) {
		this.footinc = footinc;
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

	public String[] getExtends() {
		return Strings.split(extend, ", ");
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extend == null) ? 0 : extend.hashCode());
		result = prime * result + ((fieldList == null) ? 0 : fieldList.hashCode());
		result = prime * result + ((focusme == null) ? 0 : focusme.hashCode());
		result = prime * result + ((footer == null) ? 0 : footer.hashCode());
		result = prime * result + ((footinc == null) ? 0 : footinc.hashCode());
		result = prime * result + ((formId == null) ? 0 : formId.hashCode());
		result = prime * result + ((generate == null) ? 0 : generate.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + ((headinc == null) ? 0 : headinc.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((paramList == null) ? 0 : paramList.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		result = prime * result + ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((theme == null) ? 0 : theme.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InputUI other = (InputUI)obj;
		if (extend == null) {
			if (other.extend != null)
				return false;
		}
		else if (!extend.equals(other.extend))
			return false;
		if (fieldList == null) {
			if (other.fieldList != null)
				return false;
		}
		else if (!fieldList.equals(other.fieldList))
			return false;
		if (focusme == null) {
			if (other.focusme != null)
				return false;
		}
		else if (!focusme.equals(other.focusme))
			return false;
		if (footer == null) {
			if (other.footer != null)
				return false;
		}
		else if (!footer.equals(other.footer))
			return false;
		if (footinc == null) {
			if (other.footinc != null)
				return false;
		}
		else if (!footinc.equals(other.footinc))
			return false;
		if (formId == null) {
			if (other.formId != null)
				return false;
		}
		else if (!formId.equals(other.formId))
			return false;
		if (generate == null) {
			if (other.generate != null)
				return false;
		}
		else if (!generate.equals(other.generate))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		}
		else if (!header.equals(other.header))
			return false;
		if (headinc == null) {
			if (other.headinc != null)
				return false;
		}
		else if (!headinc.equals(other.headinc))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (paramList == null) {
			if (other.paramList != null)
				return false;
		}
		else if (!paramList.equals(other.paramList))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		}
		else if (!params.equals(other.params))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		}
		else if (!template.equals(other.template))
			return false;
		if (theme == null) {
			if (other.theme != null)
				return false;
		}
		else if (!theme.equals(other.theme))
			return false;
		return true;
	}


}
