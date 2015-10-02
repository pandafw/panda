package panda.mvc.validation.validator;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.ioc.annotation.IocBean;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.validation.Validators;

@IocBean(singleton=false)
public class RequiredValidator extends AbstractValidator {

	private String[] fields;
	
	/**
	 * 
	 */
	public RequiredValidator() {
		setMsgId(Validators.MSGID_REQUIRED);
	}

	/**
	 * @return the fields
	 */
	public String getFields() {
		return Strings.join(fields, ' ');
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(String fields) {
		this.fields = Strings.split(fields);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean validateValue(ActionContext ac, Object value) {
		if (value != null) {
			if (Arrays.isNotEmpty(fields)) {
				boolean errs = false;
				BeanHandler bh = Beans.i().getBeanHandler(value.getClass());
				for (String pn : fields) {
					Object v = bh.getBeanValue(value, pn);
					if (v == null) {
						addFieldError(ac, Strings.isEmpty(getName()) ? pn : getName() + "." + pn);
						errs = true;
					}
				}
				if (errs) {
					return false;
				}
			}
			return true;
		}
		
		if (Arrays.isEmpty(fields)) {
			addFieldError(ac);
		}
		else {
			for (String pn : fields) {
				addFieldError(ac, Strings.isEmpty(getName()) ? pn : getName() + "." + pn);
			}
		}
		return false;
	}
}
