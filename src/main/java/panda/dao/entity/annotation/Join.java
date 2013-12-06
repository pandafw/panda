package panda.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A JOIN mapping:
 * <pre>
 *   select * from [this] 
 *     [LEFT OUTER] JOIN [target] ON [keys] = [refs]  
 * </pre>
 * @author yf.frank.wang@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Join {

	/**
	 * join name
	 */
	String name();

	/**
	 * join type: default is 'LEFT OUTER'
	 */
	String type() default "";

	/**
	 * target class
	 */
	Class<?> target();

	/**
	 * query key fields of this entity
	 */
	String[] keys();

	/**
	 * the related keys of the target class.
	 * if not supplied, the primary keys of the target class will be used.
	 */
	String[] refs() default {};

}
