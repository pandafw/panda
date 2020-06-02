package panda.ioc.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bean.PropertyInjector;
import panda.cast.Castors;
import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocParam;
import panda.ioc.val.ParamsValue;
import panda.lang.Objects;
import panda.lang.reflect.FieldInjector;
import panda.lang.reflect.Fields;
import panda.lang.reflect.Injector;

public class IocFieldInjector {

	public static IocFieldInjector create(Class<?> mirror, String fieldName, IocParam ip, ValueProxy vp) {
		IocFieldInjector fi = new IocFieldInjector();
		fi.proxy = vp;
		fi.injector = ip.getInjector();

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

	private ValueProxy proxy;
	private Injector injector;

	private IocFieldInjector() {
	}

	public void inject(IocMaking ing, Object obj) {
		Object ov = proxy.get(ing);
		if (ov == ValueProxy.UNDEFINED) {
			return;
		}

		Castors cs = Castors.i();
		if (proxy instanceof ParamsValue) {
			Type[] types = injector.types(obj);
			
			Object[] ovs = (Object[])ov;
			Object[] cvs = new Object[ovs.length];
			for (int i = 0; i < ovs.length; i++) {
				cvs[i] = cs.cast(ovs[i], types[i]);
			}
			injector.injects(obj, cvs);
		}
		else {
			Object cv = cs.cast(ov, injector.type(obj));
			injector.inject(obj, cv);
		}
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("proxy", proxy)
				.append("injector", injector)
				.toString();
	}
}
