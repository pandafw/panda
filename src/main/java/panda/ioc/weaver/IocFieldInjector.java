package panda.ioc.weaver;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bean.PropertyInjector;
import panda.castor.Castors;
import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.lang.Injector;
import panda.lang.reflect.FieldInjector;
import panda.lang.reflect.Fields;
import panda.lang.reflect.Types;

public class IocFieldInjector {

	public static IocFieldInjector create(Class<?> mirror, String fieldName, ValueProxy vp) {
		IocFieldInjector fi = new IocFieldInjector();
		fi.proxy = vp;

		BeanHandler bh = Beans.i().getBeanHandler(mirror);
		if (bh.canWriteProperty(fieldName)) {
			fi.type = bh.getPropertyType(fieldName);
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
			
			fi.type = field.getGenericType();
			fi.injector = new FieldInjector(field);
		}

		
		return fi;
	}

	private Type type;
	private Injector injector;
	private ValueProxy proxy;

	private IocFieldInjector() {
	}

	public void inject(IocMaking ing, Object obj) {
		Object value = proxy.get(ing);
		if (value == ValueProxy.UNDEFINED) {
			return;
		}
		
		if (value == null || !Types.isAssignable(value.getClass(), type, false)) {
			value = Castors.i().cast(value, type);
		}
		injector.inject(obj, value);
	}
}
