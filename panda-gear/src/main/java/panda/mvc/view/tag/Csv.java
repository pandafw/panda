package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.mvc.MvcException;
import panda.mvc.view.util.CsvExporter;
import panda.mvc.view.util.ListColumn;

/**
 * Render CSV.
 * 
 * Example:
 * 
 * <pre>
 * &lt;s:csv list=&quot;list&quot; columns=&quot;columns&quot; seperator=&quot;,&quot; quotechar='&quot;' escapechar='&quot;'/&gt;
 * 
 * </pre>
 * 
 */
@IocBean(singleton=false)
public class Csv extends TagBean {

	@IocInject
	private CsvExporter exp;
	
	/**
	 * @see panda.mvc.view.tag.TagBean#end(java.io.Writer, java.lang.String)
	 */
	public boolean end(Writer writer, String body) {
		if (Collections.isEmpty(exp.getColumns())) {
			throw new IllegalArgumentException("columns of Csv tag is not defined");
		}
		
		try {
			exp.export(writer);
		}
		catch (IOException e) {
			throw new MvcException("Failed to write out Csv tag", e);
		}

		return super.end(writer, "");
	}

	/**
	 * @param list the list to set
	 */
	public void setList(Iterable list) {
		exp.setList(list);
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<ListColumn> columns) {
		exp.setColumns(columns);
	}

	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(Character separator) {
		exp.setSeparator(separator);
	}

	/**
	 * @param quotechar the quotechar to set
	 */
	public void setQuotechar(Character quotechar) {
		exp.setQuotechar(quotechar);
	}

	/**
	 * @param escapechar the escapechar to set
	 */
	public void setEscapechar(Character escapechar) {
		exp.setEscapechar(escapechar);
	}

}
