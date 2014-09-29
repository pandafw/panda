package panda.castor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.castor.castors.ArrayCastor;
import panda.castor.castors.ClassCastor;
import panda.castor.castors.CollectionCastor;
import panda.castor.castors.DateTypeCastor;
import panda.castor.castors.EnumCastor;
import panda.castor.castors.JavaBeanCastor;
import panda.castor.castors.MapCastor;
import panda.castor.castors.NumberTypeCastor;
import panda.castor.castors.PrimitiveTypeCastor;
import panda.castor.castors.PrimitiveWrapCastor;
import panda.castor.castors.StreamCastor;
import panda.castor.castors.StringTypeCastor;
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
		Castor<Object, T> c = i().getCastor(value == null ? Object.class : value.getClass(), toType);
		return c.cast(value, new CastContext());
	}

	@SuppressWarnings("unchecked")
	public static <T> T scastTo(Object value, T target) {
		if (value == null) {
			return target;
		}
		
		Asserts.notNull(target);
		
		Castor<Object, T> c = (Castor<Object, T>)i().getCastor(value.getClass(), target.getClass());
		return c.castTo(value, target, new CastContext());
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
	public void register(Castor castor) {
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
			return new Castor(fromType, toType);
		}
		
		Class<?> toClass = Types.getRawType(toType);
		if (toClass.isEnum()) {
			return (Castor<S, T>)new EnumCastor(toClass);
		}
		
		if (Types.isArrayType(toType)) {
			return new ArrayCastor(fromType, toType, this);
		}
		
		if (Types.isAssignable(toType, Number.class)) {
			return (Castor<S, T>)new NumberTypeCastor.NumberCastor();
		}
		
		if (Types.isAbstractType(toType)) {
			if (toType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType)toType;
				Type rawType = pt.getRawType();
				if (Types.isAssignable(rawType, List.class)) {
					rawType = ArrayList.class;
				}
				else if (Types.isAssignable(rawType, Map.class)) {
					rawType = LinkedHashMap.class;
				}
				else if (Types.isAssignable(rawType, Set.class)) {
					rawType = LinkedHashSet.class;
				}
				else {
					return new Castor(fromType, toType);
				}
				
				toType = Types.paramTypeOfOwner(pt.getOwnerType(), rawType, pt.getActualTypeArguments());
				return (Castor<S, T>)getCastor(toType);
			}

			if (Types.isAssignable(toType, List.class)) {
				return (Castor<S, T>)new CollectionCastor(fromType, ArrayList.class, this);
			}
			
			if (Types.isAssignable(toType, Map.class)) {
				return (Castor<S, T>)new MapCastor(fromType, LinkedHashMap.class, this);
			}
			
			if (Types.isAssignable(toType, Set.class)) {
				return (Castor<S, T>)new CollectionCastor(fromType, LinkedHashSet.class, this);
			}
			
			if (Types.isAssignable(fromType, toType, false)) {
				return new Castor(fromType, toType);
			}
			throw new RuntimeException("Failed to find Castor(" + fromType + " -> " + toType + ")");
		}

		if (Types.isAssignable(toType, Map.class)) {
			return (Castor<S, T>)new MapCastor(fromType, toType, this);
		}
		
		if (Types.isAssignable(toType, Collection.class)) {
			return (Castor<S, T>)new CollectionCastor(fromType, toType, this);
		}
		
		return new JavaBeanCastor(fromType, toType, this);
	}

	public <T> BeanHandler<T> getBeanHandler(Type type) {
		return beans.getBeanHandler(type);
	}

	public <T> T cast(Object value, Type toType) {
		Asserts.notNull(toType);
		return cast(value, toType, new CastContext());
	}

	public <T> T cast(Object value, Type toType, CastContext context) {
		Asserts.notNull(toType);
		Castor<Object, T> c = getCastor(value == null ? Object.class : value.getClass(), toType);
		return c.cast(value, context);
	}

	public <T> T castTo(Object value, T target) {
		Asserts.notNull(target);
		return castTo(value, target, null, new CastContext());
	}

	public <T> T castTo(Object value, T target, CastContext context) {
		Asserts.notNull(target);
		return castTo(value, target, null, context);
	}

	public <T> T castTo(Object value, T target, Type toType) {
		Asserts.notNull(target);
		return castTo(value, target, toType, new CastContext());
	}

	public <T> T castTo(Object value, T target, Type toType, CastContext context) {
		Asserts.notNull(target);
		Castor<Object, T> c = getCastor(
			value == null ? Object.class : value.getClass(), 
			toType == null ? target.getClass() : toType);
		return c.castTo(value, target, context);
	}
}
