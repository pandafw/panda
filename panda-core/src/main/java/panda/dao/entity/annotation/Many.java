package panda.dao.entity.annotation;

import java.lang.annotation.ElementType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A one to many mapping:
 * <h4 style=color:red>Can be Many:</h4>
 * <p>
 * <blockquote>
 * '@Many' declared field may be a collection or array.
 * Example: 
 * <pre>
 * &#064;Many(target = Pet.class, fields = { &quot;id&quot; })
 * private List<Pet> pets;
 * </pre>
 * 
 * </blockquote>
 * 
 * @author yf.frank.wang@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Documented
public @interface Many {

	/**
	 * related fields of this class
	 */
	String[] fields();

	/**
	 * target class
	 */
	Class<?> target();

	/**
	 * the related keys of the target class.
	 * if not supplied, the primary keys of the target class will be used.
	 */
	String[] keys() default {};

}
