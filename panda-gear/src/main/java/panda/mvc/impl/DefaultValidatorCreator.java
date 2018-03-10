package panda.mvc.impl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.BeanHandler;
import panda.bind.json.JsonException;
import panda.bind.json.JsonObject;
import panda.cast.CastException;
import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.ioc.meta.IocValue;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.Mvcs;
import panda.mvc.Validator;
import panda.mvc.ValidatorCreator;
import panda.mvc.annotation.Validate;
import panda.mvc.validator.BinaryValidator;
import panda.mvc.validator.CIDRValidator;
import panda.mvc.validator.CastErrorValidator;
import panda.mvc.validator.ConstantValidator;
import panda.mvc.validator.CreditCardNoValidator;
import panda.mvc.validator.DateValidator;
import panda.mvc.validator.DecimalValidator;
import panda.mvc.validator.ElValidator;
import panda.mvc.validator.EmailValidator;
import panda.mvc.validator.EmptyValidator;
import panda.mvc.validator.FileValidator;
import panda.mvc.validator.FilenameValidator;
import panda.mvc.validator.ImageValidator;
import panda.mvc.validator.ImailValidator;
import panda.mvc.validator.NumberValidator;
import panda.mvc.validator.ProhibitedValidator;
import panda.mvc.validator.RegexValidator;
import panda.mvc.validator.RequiredValidator;
import panda.mvc.validator.StringValidator;
import panda.mvc.validator.URLValidator;
import panda.mvc.validator.Validators;
import panda.mvc.validator.VisitValidator;

@IocBean(type=ValidatorCreator.class, create="initialize")
public class DefaultValidatorCreator implements ValidatorCreator {
	// -------------------------------------------------------
	@IocInject
	private Ioc ioc;
	
	@IocInject(value=MvcConstants.MVC_VALIDATORS, required=false)
	private Map<String, String> aliass;

	public DefaultValidatorCreator() {
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
		
		addValidator(Validators.CAST, CastErrorValidator.class);
		addValidator(Validators.REQUIRED, RequiredValidator.class);
		addValidator(Validators.EMPTY, EmptyValidator.class);
		addValidator(Validators.EL, ElValidator.class);
		addValidator(Validators.REGEX, RegexValidator.class);
		addValidator(Validators.EMAIL, EmailValidator.class);
		addValidator(Validators.IMAIL, ImailValidator.class);
		addValidator(Validators.FILENAME, FilenameValidator.class);
		addValidator(Validators.CREDITCARDNO, CreditCardNoValidator.class);
		addValidator(Validators.BINARY, BinaryValidator.class);
		addValidator(Validators.CIDR, CIDRValidator.class);
		addValidator(Validators.DATE, DateValidator.class);
		addValidator(Validators.NUMBER, NumberValidator.class);
		addValidator(Validators.STRING, StringValidator.class);
		addValidator(Validators.DECIMAL, DecimalValidator.class);
		addValidator(Validators.FILE, FileValidator.class);
		addValidator(Validators.IMAGE, ImageValidator.class);
		addValidator(Validators.CONSTANT, ConstantValidator.class);
		addValidator(Validators.PROHIBITED, ProhibitedValidator.class);
		addValidator(Validators.URL, URLValidator.class);
		addValidator(Validators.VISIT, VisitValidator.class);

		// check validators
		for (String a : aliass.values()) {
			createValidator(ioc, a);
		}
	}

	/**
	 * create validator
	 * @param ac action context
	 * @param v validator annotation
	 * @return validator
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Validator create(ActionContext ac, Validate v) {
		Validator fv = create(ac, v.value());

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
			JsonObject jo = null;
			try {
				jo = JsonObject.fromJson(v.params());
			}
			catch (JsonException e) {
				throw new IllegalArgumentException("Failed to set params of Validator " + fv.getClass() + ", params: " + v.params(), e);
			}
			
			// set parameters
			BeanHandler bh = Mvcs.getBeans().getBeanHandler(fv.getClass());
			for (Entry<String, Object> en : jo.entrySet()) {
				String pn = en.getKey();

				// translate ${..} expression
				Object pv = Mvcs.evaluate(ac, en.getValue());
				
				Type pt = bh.getPropertyType(pn);
				if (pt == null) {
					throw new IllegalArgumentException("Failed to find property('" + pn + "') of Validator " + fv.getClass() + ", params: " + v.params());
				}

				try {
					Object cv = Mvcs.getCastors().cast(pv, pt);
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
	
	/**
	 * create validator
	 * @param ac action context
	 * @param name validator name
	 * @return validator
	 */
	@Override
	public Validator create(ActionContext ac, String name) {
		return createValidator(ac.getIoc(), name);
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
