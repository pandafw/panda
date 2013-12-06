package panda.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declare primary key。
 * <p>
 * 
 * <pre>
 * &#064;Table("t_abc")
 * public class Abc {
 *   &#064;PK
 *   int a;
 *   
 *   &#064;PK
 *   int b;
 * ...
 * </pre>
 * 
 * <p>
 * 这个注解主要应用在复合主键的情况，如果一个 POJO 是复合主键的话，你需要通过
 * <ul>
 * <li>fetch(Class<?>,Object ...) 来获取一个对象
 * <li>delete(Class<?>,Object ...) 来删除一个对象
 * </ul>
 * 变参给入的顺序，需要按照本注解声明的顺序，否则会发生不可预知的错误。
 * <p>
 * 在 POJO 中，你可以同时声明 '@Id'，'@PK'，但是 '@Id' 更优先
 * 
 * @author yf.frank.wang@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface PK {

	/**
	 * key name
	 */
	String value() default "";
}
