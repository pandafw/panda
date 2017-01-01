package panda.bind;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.cast.Castor;
import panda.cast.Castors;
import panda.lang.Arrays;
import panda.lang.reflect.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class AbstractBinder {
	private Castors castors = Castors.i();

	private boolean ignoreNullProperty = true;

	private Set<String> excludePropertyNames;
	
	private Set<Class<?>> excludePropertyTypes;

	private Map<Type, PropertyFilter> propertyFilters = new HashMap<Type, PropertyFilter>();

	/**
	 * Constructor 
	 */
	public AbstractBinder() {
		getExcludePropertyTypes().addAll(Arrays.asList(Beans.RESERVED_PROPERTY_TYPES));
	}

	public boolean isIgnoreNullProperty() {
		return ignoreNullProperty;
	}

	public void setIgnoreNullProperty(boolean ignoreNullProperty) {
		this.ignoreNullProperty = ignoreNullProperty;
	}

	public Castors getCastors() {
		return castors;
	}

	public void setCastors(Castors castors) {
		this.castors = castors;
	}

	//----------------------------------------------------------
	public Set<String> getExcludePropertyNames() {
		if (excludePropertyNames == null) {
			excludePropertyNames = new HashSet<String>();
		}
		return excludePropertyNames;
	}
	public void addExcludeProperty(String exclude) {
		getExcludePropertyNames().add(exclude);
	}
	public void removeExcludeProperty(String exclude) {
		getExcludePropertyNames().remove(exclude);
	}

	public Set<Class<?>> getExcludePropertyTypes() {
		if (excludePropertyTypes == null) {
			excludePropertyTypes = new HashSet<Class<?>>();
		}
		return excludePropertyTypes;
	}
	public void addExcludeProperty(Class<?> exclude) {
		getExcludePropertyTypes().add(exclude);
	}
	public void removeExcludeProperty(Class<?> exclude) {
		getExcludePropertyTypes().remove(exclude);
	}

	//----------------------------------------------------------
	public Map<Type, PropertyFilter> getPropertyFilters() {
		return propertyFilters;
	}
	@SuppressWarnings("unchecked")
	public <T> PropertyFilter<T> getPropertyFilter(Type type) {
		return propertyFilters.get(type);
	}
	public void registerPropertyFilter(Type type, PropertyFilter propertyFilter) {
		propertyFilters.put(type, propertyFilter);
	}
	public void removePropertyFilter(Type type) {
		propertyFilters.remove(type);
	}
	public void clearPropertyFilters() {
		propertyFilters.clear();
	}

	//----------------------------------------------------------
	protected Beans getBeans() {
		return castors.getBeans();
	}

	protected <T> BeanHandler<T> getBeanHandler(Type type) {
		return getBeans().getBeanHandler(type);
	}
	
	protected <S, T> Castor<S, T> getCastor(Type fromType, Type toType) {
		return castors.getCastor(fromType, toType);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T convertValue(Object value, Type toType) {
		if (toType == null) {
			return null;
		}
		return (T)castors.cast(value, toType);
	}
	
	protected boolean isExcludeProperty(String name) {
		return excludePropertyNames != null && excludePropertyNames.contains(name);
	}
	
	protected boolean isExcludeProperty(Class type) {
		return excludePropertyTypes != null && excludePropertyTypes.contains(type);
	}

	protected boolean isArrayType(Type type) {
		return Types.isArrayType(type) || Types.isAssignable(type, Collection.class);
	}

	protected boolean isImmutableType(Type type) {
		return Types.isImmutableType(type);
	}
}
