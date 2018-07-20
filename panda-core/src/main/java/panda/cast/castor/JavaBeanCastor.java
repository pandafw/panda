package panda.cast.castor;

import java.lang.reflect.Type;
import java.util.Map;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.cast.CastContext;
import panda.lang.CycleDetectStrategy;

/**
 * 
 *
 * @param <T> target type
 */
public class JavaBeanCastor<T> extends AnyJsonCastor<T> {
	/**
	 * target bean handler
	 */
	private BeanHandler<T> tbh;
	
	public JavaBeanCastor(Type toType, Beans beans) {
		super(toType);
		
		tbh = beans.getBeanHandler(toType);
	}

	@Override
	protected T createTarget() {
		return tbh.createObject();
	}

	@Override
	protected T castValue(Object value, CastContext context) {
		T bean = createTarget();
		return castValueTo(value, bean, context);
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected T castValueTo(Object value, T bean, CastContext context) {
		if (!(value instanceof Map)) {
			return castError(value, context);
		}

		BeanHandler sbh = context.getCastors().getBeanHandler(value.getClass());
		String[] pns = sbh.getReadPropertyNames(value);
		if (pns.length == 0) {
			return bean;
		}
		
		for (String pn : pns) {
			Object pv = sbh.getPropertyValue(value, pn);

			if (!tbh.canWriteBean(pn)) {
				// add readonly bean error
				context.addError(context.toName(), pv);
				continue;
			}

			if (context.isCycled(pv)) {
				switch (context.getCycleDetectStrategy()) {
				case CycleDetectStrategy.CYCLE_DETECT_NOPROP:
					continue;
				case CycleDetectStrategy.CYCLE_DETECT_LENIENT:
					pv = null;
					break;
				default:
					throw cycleError(context, pn, value);
				}
			}

			context.push(pn, pv);
			try {
				Type pt = tbh.getBeanType(bean, pn);
				Object bv = context.getCastors().cast(pv, pt, context);
				if (!tbh.setBeanValue(bean, pn, bv)) {
					context.addError(context.toName(), pv);
				}
			}
			catch (Throwable e) {
				castError(pv, context, e);
			}
			finally {
				context.popup();
			}
		}
		
		return bean; 
	}
}
