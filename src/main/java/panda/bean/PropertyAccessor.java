package panda.bean;

import java.lang.reflect.Member;
import java.lang.reflect.Type;

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
}
