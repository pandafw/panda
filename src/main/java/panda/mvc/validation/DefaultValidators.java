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
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.adaptor.DefaultParamAdaptor;
import panda.mvc.annotation.param.Param;
import panda.mvc.validation.annotation.Validation;
import panda.mvc.validation.annotation.Validator;
import panda.mvc.validation.validator.BinaryFieldValidator;
import panda.mvc.validation.validator.CastErrorFieldValidator;
import panda.mvc.validation.validator.ConstantFieldValidator;
import panda.mvc.validation.validator.CreditCardNumberFieldValidator;
import panda.mvc.validation.validator.DateFieldValidator;
import panda.mvc.validation.validator.DecimalFieldValidator;
import panda.mvc.validation.validator.ElFieldValidator;
import panda.mvc.validation.validator.EmailFieldValidator;
import panda.mvc.validation.validator.EmptyFieldValidator;
import panda.mvc.validation.validator.FieldValidator;
import panda.mvc.validation.validator.FileFieldValidator;
import panda.mvc.validation.validator.FilenameFieldValidator;
import panda.mvc.validation.validator.ImageFieldValidator;
import panda.mvc.validation.validator.NumberFieldValidator;
import panda.mvc.validation.validator.ProhibitedFieldValidator;
import panda.mvc.validation.validator.RegexFieldValidator;
import panda.mvc.validation.validator.RequiredFieldValidator;
import panda.mvc.validation.validator.StringFieldValidator;
import panda.mvc.validation.validator.VisitorFieldValidator;

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
	@IocInject
	private Castors castors = Castors.i();
	
	private Map<String, Class<? extends FieldValidator>> map;

	@SuppressWarnings("unchecked")
	public DefaultValidators() {
		map = new HashMap<String, Class<? extends FieldValidator>>();
		map.put(CAST, CastErrorFieldValidator.class);
		map.put(REQUIRED, RequiredFieldValidator.class);
		map.put(EMPTY, EmptyFieldValidator.class);
		map.put(EL, ElFieldValidator.class);
		map.put(REGEX, RegexFieldValidator.class);
		map.put(EMAIL, EmailFieldValidator.class);
		map.put(FILENAME, FilenameFieldValidator.class);
		map.put(CREDITCARDNO, CreditCardNumberFieldValidator.class);
		map.put(BINARY, BinaryFieldValidator.class);
		map.put(DATE, DateFieldValidator.class);
		map.put(NUMBER, NumberFieldValidator.class);
		map.put(STRING, StringFieldValidator.class);
		map.put(DECIMAL, DecimalFieldValidator.class);
		map.put(FILE, FileFieldValidator.class);
		map.put(IMAGE, ImageFieldValidator.class);
		map.put(CONSTANT, ConstantFieldValidator.class);
		map.put(PROHIBITED, ProhibitedFieldValidator.class);
		map.put(VISITOR, VisitorFieldValidator.class);
		
		InputStream is = null;
		try {
			// load default custom settings
			is = ClassLoaders.getResourceAsStream("mvc-validators.json");
			if (is != null) {
				Map<String, String> cm = Jsons.fromJson(is, Charsets.UTF_8, Map.class);
				for (Entry<String, String> en : cm.entrySet()) {
					map.put(en.getKey(), (Class<? extends FieldValidator>)Classes.getClass(en.getValue()));
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

	public void validate(ActionContext ac) {
		if (ac.getParams() == null) {
			return;
		}

		Method method = ac.getMethod();
		Validation ma = method.getAnnotation(Validation.class);
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
			Validation valid = null;

			Annotation[] pas = pass[i];
			for (Annotation pa : pas) {
				if (pa instanceof Param) {
					param = (Param)pa;
				}
				if (pa instanceof Validation) {
					valid = (Validation)pa;
				}
			}

			if (valid == null) {
				continue;
			}

			Object obj = ac.getArgs()[i];
			String name = DefaultParamAdaptor.indexedName(i, param);

			for (Validator v : valid.value()) {
				FieldValidator fv = createValidator(ac, v);

				fv.setName(name);
				fv.setMessage(v.message());
				fv.setMsgId(v.msgId());
				fv.setShortCircuit(v.shortCircuit());
				
				if (Strings.isNotEmpty(v.params())) {
					JsonObject jo = JsonObject.fromJson(v.params());
					CastContext cctx = castors.getCastContext();
					castors.castTo(jo, fv, cctx);
					if (cctx.hasError()) {
						throw new IllegalArgumentException("Failed to set params(\"" + v.params() + " to " + fv.getClass() + "\nErrors: " + Jsons.toJson(cctx.getErrors(), true));
					}
				}
				
				if (!fv.validate(ac, obj)) {
					return;
				}
			}
		}
	}

	protected FieldValidator createValidator(ActionContext ac, Validator v) {
		if (v.type() != FieldValidator.class) {
			FieldValidator fv = ac.getIoc().getIfExists(v.type());
			if (fv == null) {
				Classes.born(v.type());
			}
			
			return fv;
		}

		String alias = v.value();
		if (Strings.isNotEmpty(alias)) {
			Class<? extends FieldValidator> c = map.get(alias);
			if (c != null) {
				return Classes.born(c);
			}

			return (FieldValidator)Classes.born(alias);
		}

		throw new IllegalArgumentException("Missing type or value of @Validator()");
	}

	public boolean validate(ActionContext ac, String name, Object object) {
		return true;
	}
}
