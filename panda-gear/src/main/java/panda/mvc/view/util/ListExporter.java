package panda.mvc.view.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.bean.Beans;
import panda.io.stream.ListWriter;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.mvc.view.Component;
import panda.mvc.view.tag.Property;

public class ListExporter extends Component {

	private static final Log log = Logs.getLog(ListExporter.class);
	
	private Map<Object, Property> codetags = new HashMap<Object, Property>();
	private Property property;

	protected Iterable list;
	protected List<ListColumn> columns;
	protected boolean noHeader;

	/**
	 * @return the list
	 */
	public Iterable getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(Iterable list) {
		this.list = list;
	}

	/**
	 * @return the columns
	 */
	public List<ListColumn> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<ListColumn> columns) {
		this.columns = columns;
	}

	/**
	 * @return the noHeader
	 */
	public boolean isNoHeader() {
		return noHeader;
	}

	/**
	 * @param noHeader the noHeader to set
	 */
	public void setNoHeader(boolean noHeader) {
		this.noHeader = noHeader;
	}

	private Object getBeanProperty(Object bean, String name) {
		try {
			return Beans.getBean(bean, name);
		}
		catch (Exception e) {
			return null;
		}
	}

	private Property getCodeProperty(ListColumn c) {
		Property property = codetags.get(c);
		if (property == null) {
			property = newComponent(Property.class);
			property.setFormat(c.format.type);
			property.setCodemap(c.format.codemap);
			property.setEscape(Strings.defaultString(c.format.escape, Escapes.ESCAPE_HTML));
			codetags.put(c.format.codemap, property);
		}
		return property;
	}

	public void export(ListWriter cw) throws IOException {
		if (list == null) {
			log.warn(getClass().getName() + " null list property!");
			return;
		}

		if (!list.iterator().hasNext()) {
			return;
		}
		
		if (!noHeader && Collections.isNotEmpty(columns)) {
			List<Object> line = new ArrayList<Object>();
			for (ListColumn c : columns) {
				if (c.hidden) {
					continue;
				}
	
				line.add(c.header == null ? c.name : c.header);
				if (c.format != null && Strings.isEmpty(c.format.escape)) {
					c.format.escape = Escapes.ESCAPE_NONE;
				}
			}
			cw.writeList(line);
		}
		
		if (list == null) {
			return;
		}

		if (Collections.isEmpty(columns)) {
			writeArrayList(cw);
		}
		else {
			writeObjectList(cw);
		}
	}

	protected void writeArrayList(ListWriter cw) throws IOException {
		for (Object d : list) {
			if (d == null) {
				continue;
			}
			
			if (d instanceof Collection) {
				cw.writeList((Collection)d);
			}
			else if (d instanceof Object[]) {
				cw.writeArray((Object[])d);
			}
			else if (d.getClass().isArray()) {
				cw.writeArray(d);
			}
			else {
				throw new IllegalArgumentException("columns is not defined for non array/list object: " + d.getClass());
			}
		}
	}
	
	protected void writeObjectList(ListWriter cw) throws IOException {
		List<Object> line = new ArrayList<Object>();

		for (Object d : list) {
			line.clear();
			for (ListColumn c : columns) {
				if (c.hidden) {
					continue;
				}
	
				String value = null;
				if (c.format != null) {
					if (Strings.startsWithIgnoreCase(c.format.type, "code")) {
						Object v = getBeanProperty(d, c.name);
						Property p = getCodeProperty(c);
						p.setValue(v);
						value = p.formatValue();
					}
					else if ("eval".equals(c.format.type)) {
						if (Strings.isEmpty(c.format.expr)) {
							throw new IllegalArgumentException("The expression of [" + c.name + "] is empty");
						}
						Object v = Mvcs.evaluate(context, c.format.expr);
						if (v != null) {
							value = castString(v);
						}
					}
					else if ("expr".equals(c.format.type)) {
						if (Strings.isEmpty(c.format.expr)) {
							throw new IllegalArgumentException("The expression of [" + c.name + "] is empty");
						}
						value = findString(c.format.expr, d);
					}
					else if ("tran".equals(c.format.type)) {
						if (Strings.isEmpty(c.format.expr)) {
							throw new IllegalArgumentException("The expression of [" + c.name + "] is empty");
						}
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
							property.setPattern(c.format.pattern);
							property.setEscape(c.format.escape);
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
}
