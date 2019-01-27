package panda.mvc.validator;

import java.util.ArrayList;
import java.util.List;

import panda.ioc.annotation.IocBean;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.net.CIDR;


@IocBean(singleton=false)
public class CIDRValidator extends AbstractStringValidator {
	private boolean split;
	private String error;
	
	public CIDRValidator() {
		setMsgId(Validators.MSGID_CIDR);
	}


	/**
	 * @return the split
	 */
	public boolean isSplit() {
		return split;
	}


	/**
	 * @param split the split to set
	 */
	public void setSplit(boolean split) {
		this.split = split;
	}


	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}


	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}


	@Override
	protected boolean validateString(String value) {
		if (split) {
			List<String> es = new ArrayList<String>();
			String[] ss = Strings.split(value);
			for (String s : ss) {
				if (!CIDR.isCIDR(s)) {
					es.add(s);
				}
			}
			
			if (Collections.isNotEmpty(es)) {
				error = Strings.join(es, ", ");
				return false;
			}
		}
		else {
			if (!CIDR.isCIDR(value)) {
				error = value;
				return false;
			}
		}
		return true;
	}
}
