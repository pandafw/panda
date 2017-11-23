package panda.mvc.validation;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bind.json.JsonException;
import panda.bind.json.JsonObject;
import panda.cast.CastException;
import panda.cast.Castors;
import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.ioc.meta.IocValue;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.Mvcs;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.validator.BinaryValidator;
import panda.mvc.validation.validator.CIDRValidator;
import panda.mvc.validation.validator.CastErrorValidator;
import panda.mvc.validation.validator.ConstantValidator;
import panda.mvc.validation.validator.CreditCardNoValidator;
import panda.mvc.validation.validator.DateValidator;
import panda.mvc.validation.validator.DecimalValidator;
import panda.mvc.validation.validator.ElValidator;
import panda.mvc.validation.validator.EmailValidator;
import panda.mvc.validation.validator.EmptyValidator;
import panda.mvc.validation.validator.FileValidator;
import panda.mvc.validation.validator.FilenameValidator;
import panda.mvc.validation.validator.ImageValidator;
import panda.mvc.validation.validator.ImailValidator;
import panda.mvc.validation.validator.NumberValidator;
import panda.mvc.validation.validator.ProhibitedValidator;
import panda.mvc.validation.validator.RegexValidator;
import panda.mvc.validation.validator.RequiredValidator;
import panda.mvc.validation.validator.StringValidator;
import panda.mvc.validation.validator.URLValidator;
import panda.mvc.validation.validator.Validator;
import panda.mvc.validation.validator.VisitValidator;

@IocBean(type=Validators.class, create="initialize")
public class DefaultValidators implements Validators {
	// -------------------------------------------------------
	@IocInject
	private Ioc ioc;
	
	@IocInject(required=false)
	private Beans beans = Beans.i();

	@IocInject(required=false)
	private Castors castors = Mvcs.getCastors();

	@IocInject(value=MvcConstants.MVC_VALIDATORS, required=false)
	private Map<String, String> aliass;

	public DefaultValidators() {
	}

	private void addValidator(String alias, Class<? extends Validator> type) {
		if (!aliass.containsKey(alias)) {
			aliass.put(alias, IocValue.TYPE_REF + type.getName());
		}
	}
	
	public void initialize() {
		if (aliass == null) {
			aliass = new HashMap<String, String>();
		}
		
		addValidator(CAST, CastErrorValidator.class);
		addValidator(REQUIRED, RequiredValidator.class);
		addValidator(EMPTY, EmptyValidator.class);
		addValidator(EL, ElValidator.class);
		addValidator(REGEX, RegexValidator.class);
		addValidator(EMAIL, EmailValidator.class);
		addValidator(IMAIL, ImailValidator.class);
		addValidator(FILENAME, FilenameValidator.class);
		addValidator(CREDITCARDNO, CreditCardNoValidator.class);
		addValidator(BINARY, BinaryValidator.class);
		addValidator(CIDR, CIDRValidator.class);
		addValidator(DATE, DateValidator.class);
		addValidator(NUMBER, NumberValidator.class);
		addValidator(STRING, StringValidator.class);
		addValidator(DECIMAL, DecimalValidator.class);
		addValidator(FILE, FileValidator.class);
		addValidator(IMAGE, ImageValidator.class);
		addValidator(CONSTANT, ConstantValidator.class);
		addValidator(PROHIBITED, ProhibitedValidator.class);
		addValidator(URL, URLValidator.class);
		addValidator(VISIT, VisitValidator.class);

		// check validators
		for (String a : aliass.values()) {
			createValidator(ioc, a);
		}
	}

	@Override
	public boolean validate(ActionContext ac, String name, Object value) {
		return validate(ac, null, name, value);
	}

	@Override
	public boolean validate(ActionContext ac, Validator parent, String name, Object value) {
		return validate(ac, parent, name, value, null);
	}
	
	@Override
	public boolean validate(ActionContext ac, Validator parent, String name, Object value, Validate[] vs) {
		if (Arrays.isEmpty(vs)) {
			Validator fv = createValidator(ac, Validators.VISIT);
			fv.setName(name);
			return fv.validate(ac, value);
		}

		boolean r = true;
		for (Validate v : vs) {
			Validator fv = createValidator(ac, v);
			fv.setName(name);
			fv.setParent(parent);
			if (!fv.validate(ac, value)) {
				if (fv.isShortCircuit()) {
					return false;
				}
				r = false;
			}
		}
		return r;
	}

	/**
	 * create validator
	 * @param ac action context
	 * @param v validator annotation
	 * @return validator
	 */
	@SuppressWarnings("unchecked")
	public Validator createValidator(ActionContext ac, Validate v) {
		Validator fv = createValidator(ac, v.value());

		if (Strings.isNotEmpty(v.refer())) {
			fv.setRefer(v.refer());
		}
		if (Strings.isNotEmpty(v.message())) {
			fv.setMessage(v.message());
		}
		if (Strings.isNotEmpty(v.msgId())) {
			fv.setMsgId(v.msgId());
		}
		fv.setShortCircuit(v.shortCircuit());
		
		if (Strings.isNotEmpty(v.params())) {
			BeanHandler bh = beans.getBeanHandler(fv.getClass());
			JsonObject jo = null;
			try {
				jo = JsonObject.fromJson(v.params());
			}
			catch (JsonException e) {
				throw new IllegalArgumentException("Failed to set params of Validator " + fv.getClass() + ", params: " + v.params(), e);
			}
			
			// translate ${..} expression
			for (Entry<String, Object> en : jo.entrySet()) {
				String pn = en.getKey();
				Object pv = Mvcs.evaluate(ac, en.getValue());
				
				Type pt = bh.getPropertyType(pn);
				if (pt == null) {
					throw new IllegalArgumentException("Failed to find property('" + pn + "') of Validator " + fv.getClass() + ", params: " + v.params());
				}

				try {
					Object cv = castors.cast(pv, pt);
					if (!bh.setPropertyValue(fv, pn, cv)) {
						throw new IllegalArgumentException("Failed to set property('" + pn + "') of Validator " + fv.getClass() + ", params: " + v.params());
					}
				}
				catch (CastException e) {
					throw new IllegalArgumentException("Failed to cast property('" + pn + "') of Validator " + fv.getClass() + ", params: " + v.params(), e);
				}
			}
		}
		
		return fv;
	}
	
	public Validator createValidator(ActionContext ac, String alias) {
		Validator v = createValidator(ac.getIoc(), alias);
		if (v instanceof VisitValidator) {
			((VisitValidator)v).setValidators(this);
		}
		return v;
	}
	
	private Validator createValidator(Ioc ioc, String alias) {
		if (Strings.isEmpty(alias)) {
			throw new IllegalArgumentException("Missing value of @Validator()");
		}
		
		String name = aliass.get(alias);
		if (name == null) {
			name = alias;
		}

		try {
			Validator v;
			if (Strings.startsWithChar(name, IocValue.TYPE_REF)) {
				v = ioc.get(Validator.class, name.substring(1));
			}
			else {
				v = (Validator)Classes.newInstance(name);
			}
			return v;
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Failed to create validator(" + name + ")", e);
		}
	}
}
