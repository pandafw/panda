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
	 * @see panda.mvc.alert.ParamAlert#getErrors()
	 */
	@Override
	public Map<String, List<String>> getErrors() {
		if (errors == null) {
			errors = new LinkedHashMap<String, List<String>>();
		}
		return errors;
	}

	/**
	 * @see panda.mvc.alert.ParamAlert#getErrors(java.lang.String)
	 */
	@Override
	public List<String> getErrors(String name) {
		final Map<String, List<String>> errors = getErrors();
		List<String> perrors = errors.get(name);

		if (perrors == null) {
			perrors = new ArrayList<String>();
			errors.put(name, perrors);
		}
		return perrors;
	}

	/**
	 * @see panda.mvc.alert.ParamAlert#hasErrors()
	 */
	@Override
	public boolean hasErrors() {
		return Collections.isNotEmpty(errors);
	}

	/**
	 * @see panda.mvc.alert.ParamAlert#hasErrors(java.lang.String)
	 */
	@Override
	public boolean hasErrors(String name) {
		if (Collections.isEmpty(errors)) {
			return false;
		}
		return Collections.isNotEmpty(errors.get(name));
	}

	/**
	 * @see panda.mvc.alert.ParamAlert#addError(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void addError(String name, String error) {
		getErrors(name).add(error);
	}

	/**
	 * remove field errors Map.
	 */
	@Override
	public void removeErrors(String name) {
		if (errors != null) {
			errors.remove(name);
		}
	}

	/**
	 * Remove all field errors.
	 */
	@Override
	public void removeErrors() {
		Collections.clear(errors);
	}
}
