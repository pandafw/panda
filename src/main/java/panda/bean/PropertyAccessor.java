package panda.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class PropertyAccessor {
	protected Type type;
	protected Field field;
	protected Method getter;
	protected Method setter;


	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * @return the getter
	 */
	public Method getGetter() {
		return getter;
	}
	
	/**
	 * @param getter the getter to set
	 */
	public void setGetter(Method getter) {
		this.getter = getter;
	}
	
	/**
	 * @return the setter
	 */
	public Method getSetter() {
		return setter;
	}
	
	/**
	 * @param setter the setter to set
	 */
	public void setSetter(Method setter) {
		this.setter = setter;
	}

}
