package panda.mvc.aware;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.lang.Collections;

/**
 * Provides a default implementation of ParamValidationAware.
 */
@IocBean(type=ParamAware.class, scope=Scope.REQUEST)
public class ParamAwareSupport implements ParamAware {

	private Map<String, List<String>> errors;

	/**
	 * @see panda.mvc.aware.ParamAware#setErrors(java.util.Map)
	 */
	public void setErrors(Map<String, List<String>> errors) {
		this.errors = errors;
	}

	/**
	 * @see panda.mvc.aware.ParamAware#getErrors()
	 */
	public Map<String, List<String>> getErrors() {
		if (errors == null) {
			errors = new LinkedHashMap<String, List<String>>();
		}
		return errors;
	}

	/**
	 * @see panda.mvc.aware.ParamAware#addError(java.lang.String,
	 *      java.lang.String)
	 */
	public void addError(String name, String error) {
		final Map<String, List<String>> errors = getErrors();
		List<String> thisErrors = errors.get(name);

		if (thisErrors == null) {
			thisErrors = new ArrayList<String>();
			errors.put(name, thisErrors);
		}

		thisErrors.add(error);
	}

	/**
	 * @see panda.mvc.aware.ParamAware#hasErrors()
	 */
	public boolean hasErrors() {
		return Collections.isNotEmpty(errors);
	}

	/**
	 * Clears field errors Map.
	 * <p/>
	 * Will clear the Map that contains field errors.
	 */
	public void clearErrors() {
		Collections.clear(errors);
	}
}
