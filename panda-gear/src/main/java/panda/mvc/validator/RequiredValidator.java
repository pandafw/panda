package panda.mvc.validator;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.ioc.annotation.IocBean;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.Validators;
import panda.vfs.FileItem;

@IocBean(singleton=false)
public class RequiredValidator extends AbstractValidator {

	private Collection<String> fields;
	private Map<String, String> refers;
	
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

	/**
	 * @return the refers
	 */
	public Map<String, String> getRefers() {
		return refers;
	}

	/**
	 * @param refers the refers to set
	 */
	public void setRefers(Map<String, String> refers) {
		this.refers = refers;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean validateValue(ActionContext ac, Object value) {
		if (value != null) {
			if (Collections.isNotEmpty(refers)) {
				boolean errs = false;
				BeanHandler bh = Beans.i().getBeanHandler(value.getClass());
				for (Entry<String, String> en : refers.entrySet()) {
					Object v = bh.getBeanValue(value, en.getKey());
					if (!exists(v)) {
						addChildFieldError(ac, en.getKey(), en.getValue());
						errs = true;
					}
				}
				return !errs;
			}

			if (Collections.isNotEmpty(fields)) {
				boolean errs = false;
				BeanHandler bh = Beans.i().getBeanHandler(value.getClass());
				for (String f : fields) {
					Object v = bh.getBeanValue(value, f);
					if (!exists(v)) {
						addChildFieldError(ac, f, f);
						errs = true;
					}
				}
				return !errs;
			}

			return exists(value);
		}
		
		if (Collections.isNotEmpty(refers)) {
			for (Entry<String, String> en : refers.entrySet()) {
				addChildFieldError(ac, en.getKey(), en.getValue());
			}
		}
		else if (Collections.isNotEmpty(fields)) {
			for (String f : fields) {
				addChildFieldError(ac, f, f);
			}
		}
		else {
			addFieldError(ac);
		}
		return false;
	}
	
	private boolean exists(Object v) {
		if (v == null) {
			return false;
		}
		
		if (v instanceof File) {
			File f = (File)v;
			return f.exists();
		}
		
		if (v instanceof FileItem) {
			FileItem f = (FileItem)v;
			return f.isExists();
		}
		
		return true;
	}

	private void addChildFieldError(ActionContext ac, String field, String refer) {
		String pn = Strings.isEmpty(refer) ? field : refer;
		addFieldError(ac, Strings.isEmpty(getName()) ? pn : getName() + "." + pn);
	}
}
