package panda.mvc.validation;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.bind.json.JsonObject;
import panda.bind.json.Jsons;
import panda.castor.CastContext;
import panda.castor.Castors;
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
import panda.mvc.adaptor.DefaultParamAdaptor;
import panda.mvc.annotation.param.Param;
import panda.mvc.validation.annotation.Validates;
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
import panda.mvc.validation.validator.Validator;
import panda.mvc.validation.validator.FileValidator;
import panda.mvc.validation.validator.FilenameFieldValidator;
import panda.mvc.validation.validator.ImageValidator;
import panda.mvc.validation.validator.NumberValidator;
import panda.mvc.validation.validator.ProhibitedValidator;
import panda.mvc.validation.validator.RegexValidator;
import panda.mvc.validation.validator.RequiredValidator;
import panda.mvc.validation.validator.StringValidator;
import panda.mvc.validation.validator.VisitorValidator;

@IocBean(type=Validators.class)
public class DefaultValidators implements Validators {
	// -------------------------------------------------------
	// alias
	//
	public static final String CAST = "cast";
	public static final String REQUIRED = "required";
	public static final String EMPTY = "empty";

	public static final String EL = "el";
	public static final String REGEX = "regex";
	public static final String EMAIL = "email";
	public static final String FILENAME = "filename";
	public static final String CREDITCARDNO = "creditcardno";

	public static final String BINARY = "binary";
	public static final String DATE = "date";
	public static final String NUMBER = "number";
	public static final String STRING = "string";
	public static final String DECIMAL = "decimal";

	public static final String FILE = "file";
	public static final String IMAGE = "image";

	public static final String CONSTANT = "constant";
	public static final String PROHIBITED = "prohibited";

	public static final String VISITOR = "visitor";

	// -------------------------------------------------------
	@IocInject(required=false)
	private Castors castors = Castors.i();
	
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
		map.put(FILENAME, FilenameFieldValidator.class);
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
		map.put(VISITOR, VisitorValidator.class);
		
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

	public boolean validate(ActionContext ac) {
		validateParams(ac);
		return !(ac.getActionAware().hasErrors() || ac.getParamAware().hasErrors());
	}
	
	protected void validateParams(ActionContext ac) {
		if (Arrays.isEmpty(ac.getArgs())) {
			return;
		}

		Method method = ac.getMethod();
		Validates ma = method.getAnnotation(Validates.class);
		if (ma != null) {
			// TODO: plain method validate
		}

		Annotation[][] pass = method.getParameterAnnotations();
		if (pass.length == 1 && !DefaultParamAdaptor.hasParam(pass[0])) {
			// TODO
			return;
		}

		for (int i = 0; i < pass.length; i++) {
			Param param = null;
			Validates valid = null;

			Annotation[] pas = pass[i];
			for (Annotation pa : pas) {
				if (pa instanceof Param) {
					param = (Param)pa;
				}
				if (pa instanceof Validates) {
					valid = (Validates)pa;
				}
			}

			if (valid == null) {
				continue;
			}

			Object obj = ac.getArgs()[i];
			String name = DefaultParamAdaptor.indexedName(i, param);

			if (Arrays.isEmpty(valid.value())) {
				Validator fv = createValidator(ac, VisitorValidator.class, "visitor");
				fv.setName(name);
				if (!fv.validate(ac, obj)) {
					if (fv.isShortCircuit()) {
						break;
					}
				}
			}
			else {
				validate(ac, name, obj, valid);
			}
		}
	}

	protected boolean validate(ActionContext ac, String name, Object value, Validates av) {
		for (Validate v : av.value()) {
			Validator fv = createValidator(ac, v);
			fv.setName(name);
			
			if (!fv.validate(ac, value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * create validator
	 * @param ac action context
	 * @param v validator annotation
	 * @return validator
	 */
	public Validator createValidator(ActionContext ac, Validate v) {
		Validator fv = createValidator(ac, v.type(), v.value());

		fv.setMessage(v.message());
		fv.setMsgId(v.msgId());
		fv.setShortCircuit(v.shortCircuit());
		
		if (Strings.isNotEmpty(v.params())) {
			JsonObject jo = JsonObject.fromJson(v.params());
			CastContext cctx = castors.getCastContext();
			castors.castTo(jo, fv, cctx);
			if (cctx.hasError()) {
				throw new IllegalArgumentException("Failed to set params(\"" + v.params() + "\") to " + fv.getClass() + "\nErrors: " + Jsons.toJson(cctx.getErrors(), true));
			}
		}
		
		return fv;
	}
	
	public Validator createValidator(ActionContext ac, Class<? extends Validator> type, String alias) {
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
				try {
					return Classes.newInstance(c);
				}
				catch (Exception e) {
					throw new IllegalArgumentException("Failed to create validator(" + c + ")", e);
				}
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
