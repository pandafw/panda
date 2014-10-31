package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import panda.bean.Beans;
import panda.el.El;
import panda.io.stream.CsvWriter;
import panda.ioc.annotation.IocBean;
import panda.lang.Asserts;
import panda.lang.Exceptions;
import panda.lang.Iterators;
import panda.log.Log;
import panda.log.Logs;

/**
 * Render CSV.
 * 
 * Example:
 * 
 * <pre>
 * &lt;s:csv list=&quot;list&quot; columns=&quot;columns&quot; columnKeys=&quot;columnKyes&quot; seperator=&quot;,&quot; quotechar='&quot;' escapechar='&quot;'/&gt;
 * 
 * </pre>
 * 
 */
@IocBean(singleton=false)
public class Csv extends Component {

	private static final Log log = Logs.getLog(Csv.class);

	protected Iterable list;
	protected List<ListColumn> columns;

	protected Character separator;
	protected Character quotechar;
	protected Character escapechar;

	private Object getBeanProperty(Object bean, String name) {
		try {
			return Beans.getBean(bean, name);
		}
		catch (Exception e) {
			return null;
		}
	}

	private Map<String, Map> codemaps = new HashMap<String, Map>();
	
	private String getCodeText(Object cm, Object v) {
		if (cm instanceof String) {
			Map m = codemaps.get(cm);
			if (m == null) {
				m = (Map)findValue((String)cm);
				codemaps.put((String)cm, m);
			}
			cm = m;
		}

		if (cm instanceof Map) {
			v = ((Map)cm).get(v);
			return v == null ? null : v.toString(); 
		}
		
		return null;
	}

	/**
	 * @see panda.mvc.view.tag.Component#end(java.io.Writer, java.lang.String)
	 */
	public boolean end(Writer writer, String body) {
		@SuppressWarnings("resource")
		CsvWriter cw = new CsvWriter(writer);
		if (separator != null) {
			cw.setSeparator(separator);
		}
		if (quotechar != null) {
			cw.setQuotechar(quotechar);
		}
		if (escapechar != null) {
			cw.setEscapechar(escapechar);
		}
		
		try {
			ArrayList<Object> line = new ArrayList<Object>();
			for (ListColumn c : columns) {
				if (!c.hidden) {
					line.add(c.header == null ? c.name : c.header);
				}
			}
			cw.writeNext(line);

			if (list != null) {
				for (Object d : list) {
					line.clear();
					for (ListColumn c : columns) {
						if (c.hidden) {
							continue;
						}

						String value = null;
						if ("code".equals(c.format.type)) {
							Object v = getBeanProperty(d, c.name);
							Iterator iv = Iterators.asIterator(v);
							if (iv != null) {
								StringBuilder sb = new StringBuilder();
								while (iv.hasNext()) {
									sb.append(getCodeText(c.format.codemap, iv.next()));
									if (iv.hasNext()) {
										sb.append(' ');
									}
								}
								value = sb.toString();
							}
						}
						else if ("expression".equals(c.format.type)) {
							Asserts.notEmpty(c.format.expression, "The expression of [" + c.name + "] is empty");
							Object v = El.eval(c.format.expression, d);
							if (v != null) {
								Property p = context.getIoc().get(Property.class);
								p.setValue(v);
								p.setEscape(null);
								value = p.formatValue();
							}
						}
						else {
							Object v = El.eval(c.name, d);
							if (v != null) {
								Property p = context.getIoc().get(Property.class);
								p.setValue(v);
								p.setEscape(null);
								value = p.formatValue();
							}
						}
						line.add(value);
					}
					cw.writeNext(line);
				}
			}
			cw.flush();
		}
		catch (Exception e) {
			log.error("csv", e);
			try {
				writer.write(Exceptions.getStackTrace(e));
			}
			catch (IOException e2) {
			}
		}
		finally {
		}

		return super.end(writer, "");
	}

	/**
	 * @param list the list to set
	 */
	public void setList(Iterable list) {
		this.list = list;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<ListColumn> columns) {
		this.columns = columns;
	}

	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(Character separator) {
		this.separator = separator;
	}

	/**
	 * @param quotechar the quotechar to set
	 */
	public void setQuotechar(Character quotechar) {
		this.quotechar = quotechar;
	}

	/**
	 * @param escapechar the escapechar to set
	 */
	public void setEscapechar(Character escapechar) {
		this.escapechar = escapechar;
	}

}
