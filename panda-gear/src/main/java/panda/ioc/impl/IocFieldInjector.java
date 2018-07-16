package panda.ioc.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bean.PropertyInjector;
import panda.cast.Castors;
import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocValue;
import panda.lang.Injector;
import panda.lang.reflect.FieldInjector;
import panda.lang.reflect.Fields;

public class IocFieldInjector {

	public static IocFieldInjector create(Class<?> mirror, String fieldName, IocValue iv, ValueProxy vp) {
		IocFieldInjector fi = new IocFieldInjector();
		fi.proxy = vp;
		fi.injector = iv.getInjector();

		if (fi.injector == null) {
			BeanHandler bh = Beans.i().getBeanHandler(mirror);
			if (mirror.isArray() || Collection.class.isAssignableFrom(mirror) || bh.canWriteProperty(fieldName)) {
				fi.injector = new PropertyInjector(fieldName, bh);
			}
			else {
				// private member ?
				Field field = Fields.getField(mirror, fieldName, true);
				if (field == null) {
					throw new RuntimeException("Unknown field [" + fieldName + "] of " + mirror);
				}
				
				if (Modifier.isFinal(field.getModifiers())) {
					throw new RuntimeException("Can't inject final field [" + fieldName + "] of " + mirror);
				}
				
				fi.injector = new FieldInjector(field);
			}
		}
		
		return fi;
	}

	private Injector injector;
	private ValueProxy proxy;

	private IocFieldInjector() {
	}

	public void inject(IocMaking ing, Object obj) {
		Object value = proxy.get(ing);
		if (value == ValueProxy.UNDEFINED) {
			return;
		}
		
		value = Castors.i().cast(value, injector.type(obj));
		injector.inject(obj, value);
	}
}
