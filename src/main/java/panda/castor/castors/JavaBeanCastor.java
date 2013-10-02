package panda.castor.castors;

import java.lang.reflect.Type;

import panda.bean.BeanHandler;
import panda.castor.CastContext;
import panda.castor.Castor;
import panda.castor.Castors;
import panda.lang.CycleDetectStrategy;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> target type
 */
public class JavaBeanCastor<T> extends Castor<Object, T> {
	private Castors castors;
	private BeanHandler<T> beanHandler;
	
	public JavaBeanCastor(Type fromType, Type toType, Castors castors) {
		super(fromType, toType);
		
		this.castors = castors;
		beanHandler = castors.getBeanHandler(toType);
	}

	@Override
	protected T createTarget() {
		return beanHandler.createObject();
	}

	@Override
	protected T castValue(Object value, CastContext context) {
		T bean = createTarget();
		return castValueTo(value, bean, context);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected T castValueTo(Object value, T bean, CastContext context) {
		BeanHandler bh = castors.getBeanHandler(value.getClass());
		String[] pns = bh.getReadPropertyNames(value);
		if (pns.length == 0) {
			throw castError(value, context);
		}
		
		for (String pn : pns) {
			if (!beanHandler.canWriteBean(pn)) {
				continue;
			}

			Object pv = bh.getPropertyValue(value, pn);
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
				Type pt = beanHandler.getBeanType(pn);
				Object bv = beanHandler.getBeanValue(bean, pn);

				if (bv == null) {
					bv = castors.cast(pv, pt, context);
					beanHandler.setBeanValue(bean, pn, bv);
				}
				else {
					Object cv = castors.castTo(pv, bv, pt, context);
					if (cv != bv) {
						beanHandler.setBeanValue(bean, pn, cv);
					}
				}
			}
			catch (Throwable e) {
				throw wrapError(e, context);
			}
			finally {
				context.popup();
			}
		}
		
		return bean; 
	}
}
