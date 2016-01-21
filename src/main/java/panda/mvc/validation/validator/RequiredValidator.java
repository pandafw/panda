package panda.mvc.validation.validator;

import java.util.Collection;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.ioc.annotation.IocBean;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.validation.Validators;

@IocBean(singleton=false)
public class RequiredValidator extends AbstractValidator {

	private Collection<String> fields;
	
	/**
	 * 
	 */
	public RequiredValidator() {
		setMsgId(Validators.MSGID_REQUIRED);
	}

	/**
	 * @return the fields
	 */
	public Collection<String> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Collection<String> fields) {
		this.fields = fields;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean validateValue(ActionContext ac, Object value) {
		if (value != null) {
			if (Collections.isNotEmpty(fields)) {
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
		
		if (Collections.isEmpty(fields)) {
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
