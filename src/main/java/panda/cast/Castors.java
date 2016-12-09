package panda.cast;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.cast.castor.ArrayCastor;
import panda.cast.castor.ByteArrayCastor;
import panda.cast.castor.CharArrayCastor;
import panda.cast.castor.ClassCastor;
import panda.cast.castor.CollectionCastor;
import panda.cast.castor.DateTypeCastor;
import panda.cast.castor.DirectCastor;
import panda.cast.castor.EmailAddressCastor;
import panda.cast.castor.EnumCastor;
import panda.cast.castor.FileItemCastor;
import panda.cast.castor.IterableCastor;
import panda.cast.castor.IteratorCastor;
import panda.cast.castor.JavaBeanCastor;
import panda.cast.castor.LocaleCastor;
import panda.cast.castor.MapCastor;
import panda.cast.castor.NumberTypeCastor;
import panda.cast.castor.PrimitiveTypeCastor;
import panda.cast.castor.PrimitiveWrapCastor;
import panda.cast.castor.StreamCastor;
import panda.cast.castor.StringTypeCastor;
import panda.cast.castor.TimeZoneCastor;
import panda.lang.Asserts;
import panda.lang.collection.MultiKey;
import panda.lang.reflect.Types;

/**
 * !! thread-safe !! 
 * @author yf.frank.wang@gmail.com
 */
public class Castors {
	private static Castors i = new Castors();

	/**
	 * @return instance
	 */
	public static Castors i() {
		return i;
	}

	/**
	 * @return instance
	 */
	public static Castors getInstance() {
		return i;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setInstance(Castors instance) {
		Castors.i = instance;
	}

	public static <T> T scast(Object value, Type toType) {
		Asserts.notNull(toType);
		return i().cast(value, toType);
	}

	public static <T> T scastTo(Object value, T target) {
		if (value == null) {
			return target;
		}
		
		Asserts.notNull(target);
		
		return i().castTo(value, target);
	}

	// ------------------------------------------------------------------------
	private Beans beans = Beans.i();
	private Map<MultiKey, Castor> castors = new ConcurrentHashMap<MultiKey, Castor>();
	
	/**
	 * Constructor
	 */
	public Castors() {
		register(new PrimitiveTypeCastor.BooleanCastor());
		register(new PrimitiveTypeCastor.ByteCastor());
		register(new PrimitiveTypeCastor.CharacterCastor());
		register(new PrimitiveTypeCastor.DoubleCastor());
		register(new PrimitiveTypeCastor.FloatCastor());
		register(new PrimitiveTypeCastor.IntegerCastor());
		register(new PrimitiveTypeCastor.LongCastor());
		register(new PrimitiveTypeCastor.ShortCastor());

		register(new PrimitiveWrapCastor.BooleanCastor());
		register(new PrimitiveWrapCastor.ByteCastor());
		register(new PrimitiveWrapCastor.CharacterCastor());
		register(new PrimitiveWrapCastor.DoubleCastor());
		register(new PrimitiveWrapCastor.FloatCastor());
		register(new PrimitiveWrapCastor.IntegerCastor());
		register(new PrimitiveWrapCastor.LongCastor());
		register(new PrimitiveWrapCastor.ShortCastor());

		register(new NumberTypeCastor.NumberCastor());
		register(new NumberTypeCastor.BigIntegerCastor());
		register(new NumberTypeCastor.BigDecimalCastor());

		register(new ByteArrayCastor());
		register(new CharArrayCastor());

		DateTypeCastor.DateCastor dc = new DateTypeCastor.DateCastor();
		register(dc);
		register(new DateTypeCastor.CalendarCastor(dc));
		register(new DateTypeCastor.GregorianCalendarCastor(dc));
		register(new DateTypeCastor.SqlDateCastor(dc));
		register(new DateTypeCastor.SqlTimeCastor(dc));
		register(new DateTypeCastor.SqlTimestampCastor(dc));
		
		register(new StringTypeCastor.StringCastor(dc));
		register(new StringTypeCastor.StringBufferCastor(dc));
		register(new StringTypeCastor.StringBuilderCastor(dc));
		
		register(new StreamCastor.InputStreamCastor());
		register(new StreamCastor.ReaderCastor());

		register(new ClassCastor());
		
		register(new IterableCastor());
		register(new IteratorCastor());

		register(new LocaleCastor());
		register(new TimeZoneCastor());
		
		register(new FileItemCastor());
		register(new EmailAddressCastor());
	}
	
	public Beans getBeans() {
		return beans;
	}

	public void setBeans(Beans beans) {
		this.beans = beans;
	}

	/**
	 * Register (add) a castor for a class
	 * 
	 * @param castor - the castor instance
	 */
	public void register(AbstractCastor castor) {
		register(castor.getFromType(), castor.getToType(), castor);
	}

	/**
	 * Register (add) a castor for a class
	 * 
	 * @param toType - the class
	 * @param castor - the castor instance
	 */
	public void register(Type fromType, Type toType, Castor castor) {
		castors.put(new MultiKey(fromType, toType), castor);
	}
	
	/**
	 * Unregister (remove) a castor for a class
	 * 
	 * @param toType - the class
	 */
	public void unregister(Type fromType, Type toType) {
		castors.remove(new MultiKey(fromType, toType));
	}
	
	/**
	 * clear converters
	 */
	public void clear() {
		castors.clear();
	}
	
	public CastContext newCastContext() {
		return new CastContext(this);
	}
	
	/**
	 * getCastor
	 * @param type object type
	 * @return Castor
	 */
	public <T> Castor<Object, T> getCastor(Class<T> type) {
		return getCastor((Type)type);
	}
	
	/**
	 * getCastor
	 * @param toType object type
	 * @return Castor
	 */
	public <T> Castor<Object, T> getCastor(Type toType) {
		return getCastor(Object.class, toType);
	}
	
	/**
	 * getCastor
	 * @param toType object type
	 * @return Castor
	 */
	@SuppressWarnings("unchecked")
	public <S, T> Castor<S, T> getCastor(Type fromType, Type toType) {
		Asserts.notNull(fromType, "The from type is null");
		Asserts.notNull(toType, "The to type is null");

		// same type
		if (fromType.equals(toType)) {
			return DirectCastor.i();
		}

		// type -> type castor
		Castor<S, T> castor = castors.get(new MultiKey(fromType, toType));
		if (castor != null) {
			return castor;
		}
		
		// object -> type castor
		if (!Object.class.equals(fromType)) {
			castor = castors.get(new MultiKey(Object.class, toType));
			if (castor != null) {
				return castor;
			}
		}
		
		// default castor
		if (Object.class.equals(toType)) {
			return DirectCastor.i();
		}
		
		Class<?> toClass = Types.getRawType(toType);
		if (toClass.isEnum()) {
			return (Castor<S, T>)new EnumCastor(toClass);
		}
		
		if (Types.isArrayType(toType)) {
			return new ArrayCastor(fromType, toType);
		}
		
		if (Types.isAssignable(toType, Number.class)) {
			return (Castor<S, T>)new NumberTypeCastor.NumberCastor();
		}
		
		if (Types.isAssignable(toType, Map.class)) {
			return (Castor<S, T>)new MapCastor(toType, this);
		}
		
		if (Types.isAbstractType(toType)) {
			if (toType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType)toType;
				Type rawType = pt.getRawType();
				if (Types.isAssignable(ArrayList.class, rawType)) {
					rawType = ArrayList.class;
				}
				else if (Types.isAssignable(LinkedHashSet.class, rawType)) {
					rawType = LinkedHashSet.class;
				}
				else if (Types.isAssignable(fromType, toType)) {
					return DirectCastor.i();
				}
				else {
					throw new RuntimeException("Failed to find Castor(" + fromType + " -> " + toType + ")");
				}
				
				toType = Types.paramTypeOfOwner(pt.getOwnerType(), rawType, pt.getActualTypeArguments());
				return (Castor<S, T>)getCastor(toType);
			}

			if (Types.isAssignable(fromType, toType)) {
				return DirectCastor.i();
			}

			if (Types.isAssignable(ArrayList.class, toType)) {
				return (Castor<S, T>)new CollectionCastor(fromType, ArrayList.class);
			}
			
			if (Types.isAssignable(LinkedHashSet.class, toType)) {
				return (Castor<S, T>)new CollectionCastor(fromType, LinkedHashSet.class);
			}
			
			throw new RuntimeException("Failed to find Castor(" + fromType + " -> " + toType + ")");
		}

		if (Types.isAssignable(toType, Collection.class)) {
			return (Castor<S, T>)new CollectionCastor(fromType, toType);
		}
		
		return new JavaBeanCastor(toType, beans);
	}

	public <T> BeanHandler<T> getBeanHandler(Type type) {
		return beans.getBeanHandler(type);
	}

	public <T> T cast(Object value, Type toType) {
		Asserts.notNull(toType);
		return cast(value, toType, newCastContext());
	}

	public <T> T cast(Object value, Type toType, CastContext context) {
		Asserts.notNull(toType);
		Castor<Object, T> c = getCastor(value == null ? Object.class : value.getClass(), toType);
		return c.cast(value, context);
	}

	public <T> T castTo(Object value, T target) {
		Asserts.notNull(target);
		return castTo(value, target, null, newCastContext());
	}

	public <T> T castTo(Object value, T target, CastContext context) {
		Asserts.notNull(target);
		return castTo(value, target, null, context);
	}

	public <T> T castTo(Object value, T target, Type toType) {
		Asserts.notNull(target);
		return castTo(value, target, toType, newCastContext());
	}

	public <T> T castTo(Object value, T target, Type toType, CastContext context) {
		Asserts.notNull(target);
		Castor<Object, T> c = getCastor(
			value == null ? Object.class : value.getClass(), 
			toType == null ? target.getClass() : toType);
		return c.castTo(value, target, context);
	}
}
