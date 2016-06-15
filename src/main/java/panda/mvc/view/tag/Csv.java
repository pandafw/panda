package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import panda.bean.Beans;
import panda.io.stream.CsvWriter;
import panda.ioc.annotation.IocBean;
import panda.lang.Asserts;
import panda.lang.Collections;
import panda.lang.Iterators;
import panda.lang.Strings;
import panda.mvc.MvcException;
import panda.mvc.Mvcs;

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
public class Csv extends Component {

	protected Iterable list;
	protected List<ListColumn> columns;

	protected Character separator;
	protected Character quotechar;
	protected Character escapechar;

	private Property property;
	
	private Object getBeanProperty(Object bean, String name) {
		try {
			return Beans.getBean(bean, name);
		}
		catch (Exception e) {
			return null;
		}
	}

	private Map<String, Map> codemaps = new HashMap<String, Map>();
	
	private String getCodeText(Object cm, Object k) {
		if (k == null) {
			return Strings.EMPTY;
		}
		
		if (cm instanceof String) {
			Map m = codemaps.get(cm);
			if (m == null) {
				m = (Map)findValue((String)cm);
				codemaps.put((String)cm, m);
			}
			cm = m;
		}

		if (cm instanceof Map) {
			return Mvcs.getCodeText((Map)cm, k);
		}
		
		return Strings.EMPTY;
	}

	/**
	 * @see panda.mvc.view.tag.Component#end(java.io.Writer, java.lang.String)
	 */
	public boolean end(Writer writer, String body) {
		if (Collections.isEmpty(columns)) {
			throw new IllegalArgumentException("columns of Csv tag is not defined");
		}
		
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
			cw.writeList(line);

			if (list != null) {
				for (Object d : list) {
					line.clear();
					for (ListColumn c : columns) {
						if (c.hidden) {
							continue;
						}

						String value = null;
						if (c.format != null) {
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
							else if ("eval".equals(c.format.type)) {
								Asserts.notEmpty(c.format.expr, "The expression of [" + c.name + "] is empty");
								Object v = Mvcs.evaluate(context, c.format.expr);
								if (v != null) {
									value = castString(v);
								}
							}
							else if ("expr".equals(c.format.type)) {
								Asserts.notEmpty(c.format.expr, "The expression of [" + c.name + "] is empty");
								value = findString(c.format.expr, d);
							}
							else if ("tran".equals(c.format.type)) {
								Asserts.notEmpty(c.format.expr, "The expression of [" + c.name + "] is empty");
								value = Mvcs.translate(context, c.format.expr);
							}
							else {
								Object v = getBeanProperty(d, c.name);
								if (v != null) {
									if (property == null) {
										property = newComponent(Property.class);
									}
									property.setValue(v);
									property.setFormat(c.format.type);
									value =  property.formatValue();
								}
							}
						}
						else {
							Object v = getBeanProperty(d, c.name);
							if (v != null) {
								value = castString(v);
							}
						}
						line.add(value);
					}
					cw.writeList(line);
				}
			}
			cw.flush();
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
