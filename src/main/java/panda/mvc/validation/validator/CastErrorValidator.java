package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;

@IocBean(singleton=false)
public class CastErrorValidator extends AbstractValidator {

	private boolean clearErrors = true;

	/**
	 * @return the clearErrors
	 */
	public boolean isClearErrors() {
		return clearErrors;
	}

	/**
	 * @param clearErrors the clearErrors to set
	 */
	public void setClearErrors(boolean clearErrors) {
		this.clearErrors = clearErrors;
	}

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		String fullFieldName = getFullFieldName(getName());

		if (ac.getCastErrors().containsKey(fullFieldName)) {
			setValue(ac.getCastErrors().get(fullFieldName));
			
			if (clearErrors) {
				ac.getParamAware().getErrors().remove(fullFieldName);
			}

			addFieldError(ac);
			return false;
		}
		
		return true;
	}

}
