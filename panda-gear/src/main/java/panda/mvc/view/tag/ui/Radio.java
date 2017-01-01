package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc --> Render a radio button input field.</p> <!-- END SNIPPET: javadoc
 * -->
 * <p/>
 * <b>Examples</b>
 * <p/>
 * <!-- START SNIPPET: exdescription --> In this example, a radio control is displayed with a list
 * of genders. The gender list is built from attribute id=genders. The framework calls getGenders()
 * which will return a Map. For examples using listKey and listValue attributes, see the section
 * select tag. The default selected one will be determined (in this case) by the getMale() method in
 * the action class which should retun a value similar to the key of the getGenters() map if that
 * particular gender is to be selected.
 * <p/>
 * <!-- END SNIPPET: exdescription -->
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;s:action name=&quot;GenderMap&quot; var=&quot;genders&quot;/&gt;
 * &lt;s:radio label=&quot;Gender&quot; name=&quot;male&quot; list=&quot;#genders.genders&quot;/&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@IocBean(singleton=false)
public class Radio extends ListUIBean {
	protected String headerKey;
	protected String headerValue;

	/**
	 * @return the headerKey
	 */
	public String getHeaderKey() {
		return headerKey;
	}

	/**
	 * @return the headerValue
	 */
	public String getHeaderValue() {
		return headerValue;
	}

	/**
	 * @param headerKey the headerKey to set
	 */
	public void setHeaderKey(String headerKey) {
		this.headerKey = headerKey;
	}

	/**
	 * @param headerValue the headerValue to set
	 */
	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}

}
