package panda.mvc.validation;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bind.json.JsonException;
import panda.bind.json.JsonObject;
import panda.bind.json.Jsons;
import panda.cast.CastException;
import panda.cast.Castors;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.validator.BinaryValidator;
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
import panda.mvc.validation.validator.NumberValidator;
import panda.mvc.validation.validator.ProhibitedValidator;
import panda.mvc.validation.validator.RegexValidator;
import panda.mvc.validation.validator.RequiredValidator;
import panda.mvc.validation.validator.StringValidator;
import panda.mvc.validation.validator.Validator;
import panda.mvc.validation.validator.VisitValidator;

@IocBean(type=Validators.class)
public class DefaultValidators implements Validators {
	// -------------------------------------------------------
	@IocInject(required=false)
	private Beans beans = Beans.i();

	@IocInject(required=false)
	private Castors castors = Mvcs.getCastors();
	
	private Map<String, Class<? extends Validator>> map;

	@SuppressWarnings("unchecked")
	public DefaultValidators() {
		map = new HashMap<String, Class<? extends Validator>>();
		map.put(CAST, CastErrorValidator.class);
		map.put(REQUIRED, RequiredValidator.class);
		map.put(EMPTY, EmptyValidator.class);
		map.put(EL, ElValidator.class);
		map.put(REGEX, RegexValidator.class);
		map.put(EMAIL, EmailValidator.class);
		map.put(FILENAME, FilenameValidator.class);
		map.put(CREDITCARDNO, CreditCardNoValidator.class);
		map.put(BINARY, BinaryValidator.class);
		map.put(DATE, DateValidator.class);
		map.put(NUMBER, NumberValidator.class);
		map.put(STRING, StringValidator.class);
		map.put(DECIMAL, DecimalValidator.class);
		map.put(FILE, FileValidator.class);
		map.put(IMAGE, ImageValidator.class);
		map.put(CONSTANT, ConstantValidator.class);
		map.put(PROHIBITED, ProhibitedValidator.class);
		map.put(VISIT, VisitValidator.class);
		
		InputStream is = null;
		try {
			// load default custom settings
			is = ClassLoaders.getResourceAsStream("mvc-validators.json");
			if (is != null) {
				Map<String, String> cm = Jsons.fromJson(is, Charsets.UTF_8, Map.class);
				for (Entry<String, String> en : cm.entrySet()) {
					map.put(en.getKey(), (Class<? extends Validator>)Classes.getClass(en.getValue()));
				}
			}
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			Streams.safeClose(is);
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
			Validator fv = createValidator(ac, VisitValidator.class, Validators.VISIT);
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
		Validator fv = createValidator(ac, v.type(), v.value());

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
				throw new IllegalArgumentException("Failed to set params of Validator " + fv.getClass() + ", params: " + v.params());
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
	
	public Validator createValidator(ActionContext ac, Class<? extends Validator> type, String alias) {
		Validator v = _createValidator(ac, type, alias);
		if (v instanceof VisitValidator) {
			((VisitValidator)v).setValidators(this);
		}
		return v;
	}
	
	private Validator _createValidator(ActionContext ac, Class<? extends Validator> type, String alias) {
		if (type != Validator.class) {
			Validator fv = ac.getIoc().getIfExists(type);
			if (fv != null) {
				return fv;
			}
			try {
				return Classes.newInstance(type);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Failed to create validator(" + type + ")", e);
			}
		}

		if (Strings.isNotEmpty(alias)) {
			Class<? extends Validator> c = map.get(alias);
			if (c != null) {
				return _createValidator(ac, c, null);
			}

			try {
				return (Validator)Classes.newInstance(alias);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Failed to create validator(" + alias + ")", e);
			}
		}

		throw new IllegalArgumentException("Missing type or value of @Validator()");
	}
}
