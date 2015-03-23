package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.MvcException;
import panda.mvc.Mvcs;

/**
 * Base class to extend for UI components.
 * <p/>
 * This class is a good extension point when building reuseable UI components.
 */
public class Component {

	@IocInject
	protected ActionContext context;

	protected Map<String, Object> params;
	
	protected BeanHandler bean;
	
	/**
	 * Constructor.
	 */
	public Component() {
	}
	
	private void addParameter(String name, Object value) {
		if (params == null) {
			params = new LinkedHashMap<String, Object>();
		}
		params.put(name, value);
	}

	/**
	 * @return the params
	 */
	public Map<String, Object> getParameters() {
		return params;
	}

	public Object getParameter(String name) {
		if (Collections.isEmpty(params)) {
			return null;
		}
		return params.get(name);
	}

	@SuppressWarnings("unchecked")
	public void setParameter(String name, Object value) {
		if (bean == null) {
			bean = Beans.i().getBeanHandler(this.getClass());
		}

		if (bean.canWriteBean(name)) {
			Type pt = bean.getBeanType(name);
			Object cv = Mvcs.getCastors().cast(value, pt);
			bean.setBeanValue(this, name, cv);
			return;
		}

		addParameter(name, value);
	}

	/**
	 * Callback for the start tag of this component. Should the body be evaluated?
	 * 
	 * @param writer the output writer.
	 * @return true if the body should be evaluated
	 */
	public boolean start(Writer writer) {
		context.push(this);
		return true;
	}

	/**
	 * Callback for the end tag of this component. Should the body be evaluated again?
	 * <p/>
	 * <b>NOTE:</b> will pop component stack.
	 * 
	 * @param writer the output writer.
	 * @param body the rendered body.
	 * @return true if the body should be evaluated again
	 */
	public boolean end(Writer writer, String body) {
		try {
			if (Strings.isNotEmpty(body)) {
				writer.write(body);
			}
		}
		catch (IOException e) {
			throw new MvcException("IOError while writing the body: " + e.getMessage(), e);
		}
		finally {
			popComponentStack();
		}
		return false;
	}

	/**
	 * Pops the component stack.
	 */
	protected void popComponentStack() {
		context.pop();
	}

	/**
	 * Finds the nearest ancestor of this component stack.
	 * 
	 * @param clazz the class to look for, or if assignable from.
	 * @return the component if found, <tt>null</tt> if not.
	 */
	@SuppressWarnings("unchecked")
	protected Component findAncestor(Class clazz) {
		List<Object> cs = context.getTops();
		int p = cs.indexOf(this);
		if (p >= 0) {
			int start = cs.size() - p - 1;

			for (int i = start; i >= 0; i--) {
				Component c = (Component)cs.get(i);
				if (clazz.isAssignableFrom(c.getClass()) && c != this) {
					return c;
				}
			}
		}

		return null;
	}

	/**
	 * Overwrite to set if body should be used.
	 * 
	 * @return always false for this component.
	 */
	public boolean usesBody() {
		return false;
	}

	public void copyParams(Map<String, Object> params) {
		if (Collections.isEmpty(params)) {
			return;
		}
		
		for (Entry<String, Object> en : params.entrySet()) {
			String p = en.getKey();
			Object v = Mvcs.evaluate(en.getValue(), context);
			setParameter(p, v);
		}
	}
	
	public void copyParams(Map<String, Object> params, Object arg) {
		if (Collections.isEmpty(params)) {
			return;
		}
		
		for (Entry<String, Object> en : params.entrySet()) {
			String p = en.getKey();
			Object v = Mvcs.evaluate(en.getValue(), context, arg);
			setParameter(p, v);
		}
	}
	
	public Object findValue(String expr) {
		return Mvcs.findValue(expr, context);
	}
	
	public Object findValue(String expr, Object arg) {
		return Mvcs.findValue(expr, context, arg);
	}

	public String findString(String expr) {
		Object o = findValue(expr);
		return o == null ? null : o.toString();
	}
	
	public String findString(String expr, Object arg) {
		Object o = findValue(expr, arg);
		return o == null ? null : o.toString();
	}
	
	public <T> T newComponent(Class<T> cls) {
		return context.getIoc().get(cls);
	}
}
