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
import panda.bind.json.JsonArray;
import panda.bind.json.JsonObject;
import panda.cast.Castors;
import panda.el.El;
import panda.ioc.annotation.IocInject;
import panda.lang.Asserts;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.MvcException;

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

	@SuppressWarnings("unchecked")
	public void setParameter(String name, Object value) {
		if (bean == null) {
			bean = Beans.i().getBeanHandler(this.getClass());
		}

		if (bean.canWriteBean(name)) {
			Type pt = bean.getBeanType(name);
			Object cv = Castors.scast(value, pt);
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
		return end(writer, body, true);
	}

	/**
	 * Callback for the start tag of this component. Should the body be evaluated again?
	 * <p/>
	 * <b>NOTE:</b> has a parameter to determine to pop the component stack.
	 * 
	 * @param writer the output writer.
	 * @param body the rendered body.
	 * @param popComponentStack should the component stack be popped?
	 * @return true if the body should be evaluated again
	 */
	protected boolean end(Writer writer, String body, boolean popComponentStack) {
		Asserts.notNull(body);

		try {
			if (Strings.isNotEmpty(body)) {
				writer.write(body);
			}
		}
		catch (IOException e) {
			throw new MvcException("IOError while writing the body: " + e.getMessage(), e);
		}
		if (popComponentStack) {
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
		copyParams(params, Objects.NULL);
	}
	
	public void copyParams(Map<String, Object> params, Object arg) {
		if (Collections.isEmpty(params)) {
			return;
		}
		
		for (Entry<String, Object> en : params.entrySet()) {
			String p = en.getKey();
			Object v = en.getValue();
			if (v instanceof String) {
				String s = (String)v;
				if (s.length() > 3) {
					char c0 = s.charAt(0);
					char c1 = s.charAt(1);
					char cx = s.charAt(s.length() - 1);
					if ((c0 == '$' || c0 == '%') && c1 == '{' && cx == '}') {
						v = eval(s.substring(2, s.length() - 1), arg);
					}
					else if (c0 == '#' && c1 == '{' && cx == '}') {
						v = JsonObject.fromJson(s.substring(1));
					}
					else if (c0 == '#' && c1 == '[' && cx == ']') {
						v = JsonArray.fromJson(s.substring(1));
					}
				}
			}
			
			setParameter(p, v);
		}
	}
	
	public Object eval(String expr) {
		return eval(expr, Objects.NULL);
	}
	
	public Object eval(String expr, Object arg) {
		if (Objects.NULL == arg) {
			return El.eval(expr, context);
		}

		try {
			context.push(arg);
			return El.eval(expr, context);
		}
		finally {
			context.pop();
		}
	}

	public String evalString(String expr) {
		return evalString(expr, Objects.NULL);
	}
	
	public String evalString(String expr, Object arg) {
		Object o = eval(expr, arg);
		return o == null ? null : o.toString();
	}
	
	public <T> T newComponent(Class<T> cls) {
		return context.getIoc().get(cls);
	}
}
