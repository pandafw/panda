package panda.castor.castors;

import java.lang.reflect.Type;

import panda.bean.BeanHandler;
import panda.castor.AbstractCastor;
import panda.castor.CastContext;
import panda.castor.Castors;
import panda.lang.CycleDetectStrategy;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> target type
 */
public class JavaBeanCastor<T> extends AbstractCastor<Object, T> {
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
	@SuppressWarnings("unchecked")
	public T convertValue(Object value, CastContext context) {
		T bean = createTarget();
		
		BeanHandler bh = castors.getBeanHandler(value.getClass());
		
		for (String pn : beanHandler.getWritePropertyNames()) {
			if (bh.canReadProperty(pn)) {
				Object pv = bh.getPropertyValue(value, pn);
				if (pv != null && context.isCycled(pv)) {
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

				Type pt = beanHandler.getPropertyType(pn);
				context.push(pn, value);
				try {
					pv = castors.cast(pv, pt, context);
					beanHandler.setPropertyValue(bean, pn, pv);
				}
				catch (Throwable e) {
					throw wrapError(context, e);
				}
				finally {
					context.popup();
				}
			}
		}
		return bean; 
	}
}
