package panda.mvc.validation.validator;

import panda.mvc.ActionContext;

public class CastErrorFieldValidator extends AbstractFieldValidator {

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
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		String fullFieldName = getFullFieldName(getName());

		if (ac.getCastErrors().containsKey(fullFieldName)) {
			Object value = ac.getCastErrors().get(fullFieldName);
			
			if (clearErrors) {
				ac.getParamErrors().getErrors().remove(fullFieldName);
			}

			addFieldError(ac, getName(), value);
			return false;
		}
		
		return true;
	}

}
