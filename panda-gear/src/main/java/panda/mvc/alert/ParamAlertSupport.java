package panda.mvc.alert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.lang.Collections;

@IocBean(type=ParamAlert.class, scope=Scope.REQUEST)
public class ParamAlertSupport implements ParamAlert {

	private Map<String, List<String>> errors;

	/**
	 * @see panda.mvc.alert.ParamAlert#setErrors(java.util.Map)
	 */
	public void setErrors(Map<String, List<String>> errors) {
		this.errors = errors;
	}

	/**
	 * @see panda.mvc.alert.ParamAlert#getErrors()
	 */
	public Map<String, List<String>> getErrors() {
		if (errors == null) {
			errors = new LinkedHashMap<String, List<String>>();
		}
		return errors;
	}

	/**
	 * @see panda.mvc.alert.ParamAlert#addError(java.lang.String,
	 *      java.lang.String)
	 */
	public void addError(String name, String error) {
		final Map<String, List<String>> errors = getErrors();
		List<String> perrors = errors.get(name);

		if (perrors == null) {
			perrors = new ArrayList<String>();
			errors.put(name, perrors);
		}

		perrors.add(error);
	}

	/**
	 * @see panda.mvc.alert.ParamAlert#hasErrors()
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