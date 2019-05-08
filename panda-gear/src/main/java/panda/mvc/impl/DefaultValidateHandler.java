package panda.mvc.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import panda.bean.BeanHandler;
import panda.cast.CastException;
import panda.ioc.annotation.IocBean;
import panda.lang.Classes;
import panda.lang.Objects;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.ValidateHandler;
import panda.mvc.Validator;
import panda.mvc.annotation.validate.ValidateBy;
import panda.mvc.validator.VisitValidator;

@IocBean(type=ValidateHandler.class)
public class DefaultValidateHandler implements ValidateHandler {
	public DefaultValidateHandler() {
	}

	@Override
	public boolean validate(ActionContext ac, String name, Object value) {
		Validator fv = createValidator(ac, VisitValidator.class);
		fv.setName(name);
		return fv.validate(ac, value);
	}

	@Override
	public boolean validate(ActionContext ac, Validator parent, String name, Object value, Annotation[] vas) {
		boolean r = true;
		for (Annotation va : vas) {
			Class<?> at = va.annotationType();
			ValidateBy vb = at.getAnnotation(ValidateBy.class);
			if (vb == null) {
				continue;
			}

			Validator fv = createValidator(ac, va, vb.value());
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
	 * @param an validator annotation
	 * @param vb Validator Class
	 * @return validator
	 */
	@SuppressWarnings("unchecked")
	protected Validator createValidator(ActionContext ac, Annotation an, Class<? extends Validator> vb) {
		Validator fv = createValidator(ac, vb);

		// set parameters
		BeanHandler bh = Mvcs.getBeans().getBeanHandler(fv.getClass());

		Method[] ms = an.annotationType().getDeclaredMethods();
		for (Method m : ms) {
			String pn = m.getName();
			Object v;
			try {
				v = m.invoke(an);
			}
			catch (Exception e) {
				throw new RuntimeException("Failed to invoke annotation method " + pn + "() of " + an);
			}

			// ignore value
			if (Objects.isEmpty(v)) {
				continue;
			}
			
			// translate ${..} expression
			Object pv = v;
			if (!"message".equals(pn)) {
				pv = Mvcs.evaluate(ac, v);
			}
			
			Type pt = bh.getPropertyType(pn);
			if (pt == null) {
				throw new IllegalArgumentException("Failed to find property('" + pn + "') of Validator " + fv.getClass());
			}

			try {
				Object cv = Mvcs.getCastors().cast(pv, pt);
				if (!bh.setPropertyValue(fv, pn, cv)) {
					throw new IllegalArgumentException("Failed to set property('" + pn + "') of Validator " + fv.getClass());
				}
			}
			catch (CastException e) {
				throw new IllegalArgumentException("Failed to cast property('" + pn + "') of Validator " + fv.getClass(), e);
			}
		}
		
		return fv;
	}
	
	/**
	 * create validator
	 * @param ac action context
	 * @param vc validator class
	 * @return validator
	 */
	protected Validator createValidator(ActionContext ac, Class<? extends Validator> vc) {
		try {
			Validator v = ac.getIoc().getIfExists(vc);
			if (v == null) {
				v = (Validator)Classes.newInstance(vc);
			}
			return v;
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to create validator(" + vc + ")", e);
		}
	}
}
