package panda.mvc.validation;

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
@IocBean(type=ParamValidationAware.class, scope=Scope.REQUEST)
public class ParamValidationAwareSupport implements ParamValidationAware {

	private Map<String, List<String>> errors;

	/**
	 * @see panda.mvc.validation.ParamValidationAware#setErrors(java.util.Map)
	 */
	public void setErrors(Map<String, List<String>> errors) {
		this.errors = errors;
	}

	/**
	 * @see panda.mvc.validation.ParamValidationAware#getErrors()
	 */
	public Map<String, List<String>> getErrors() {
		if (errors == null) {
			errors = new LinkedHashMap<String, List<String>>();
		}
		return errors;
	}

	/**
	 * @see panda.mvc.validation.ParamValidationAware#addError(java.lang.String,
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
	 * @see panda.mvc.validation.ParamValidationAware#hasErrors()
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
