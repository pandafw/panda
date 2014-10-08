package panda.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import panda.lang.Exceptions;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class PropertyAccessor {
	protected Type type;
	protected Member getter;
	protected Member setter;


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
	 * @return the getter
	 */
	public Member getGetter() {
		return getter;
	}
	
	/**
	 * @return the setter
	 */
	public Member getSetter() {
		return setter;
	}

	/**
	 * get property value of the specified object
	 */
	public Object getValue(Object obj) {
		try {
			if (getter instanceof Field) {
				return ((Field)getter).get(obj);
			}
			else if (getter instanceof Method) {
				return ((Method)getter).invoke(obj);
			}
			return null;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
	/**
	 * set property value of the specified object
	 */
	public boolean setValue(Object obj, Object val) {
		try {
			if (setter instanceof Field) {
				((Field)setter).set(obj, val);
				return true;
			}
			else if (setter instanceof Method) {
				((Method)setter).invoke(obj, val);
				return true;
			}
			
			return false;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
