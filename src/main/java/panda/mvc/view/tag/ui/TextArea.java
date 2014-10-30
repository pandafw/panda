package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc --> Render HTML textarea tag.</p> <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;s:textarea label=&quot;Comments&quot; name=&quot;comments&quot; cols=&quot;30&quot; rows=&quot;8&quot;/&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@IocBean(singleton=false)
public class TextArea extends TextField {
	protected Integer cols;
	protected Integer rows;
	protected String wrap;
	protected String layout;

	/**
	 * @return the layout
	 */
	public String getLayout() {
		return layout;
	}

	/**
	 * @param layout the layout to set
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * @return the cols
	 */
	public Integer getCols() {
		return cols;
	}

	/**
	 * @return the rows
	 */
	public Integer getRows() {
		return rows;
	}

	/**
	 * @return the wrap
	 */
	public String getWrap() {
		return wrap;
	}

	/**
	 * @param cols the cols to set
	 */
	public void setCols(Integer cols) {
		this.cols = cols;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * @param wrap the wrap the set
	 */
	public void setWrap(String wrap) {
		this.wrap = wrap;
	}
}
