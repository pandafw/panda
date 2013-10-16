package panda.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.lang.Strings;

/**
 * 声明一个 Java 字段是否有对应的数据库字段
 * <p>
 * 如果数据库字段名同 JAVA 字段名不一样，通过这个注解可以进行特殊标注:
 * 
 * <pre>
 * &#064;Column(&quot;数据库字段名&quot;)
 * </pre>
 * 
 * <b style=color:red>需要说明的是：</b>
 * <ul>
 * <li>
 * 如果你的 POJO <u><b>没有任何一个字段</b></u> 声明了这个注解，那么所有的 Java 字段都会被认为是数据库字段。
 * <li>声明了 '@Id' or '@PK' 的字段没必要声明这个注解。
 * </ul>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Column {
	/**
	 * column name
	 */
	String value() default Strings.EMPTY;

	/**
	 * jdbc type
	 * @see panda.dao.sql.JdbcTypes
	 */
	String type() default Strings.EMPTY;

	int size() default 0;

	int scale() default 0;

	boolean notNull() default false;

	boolean unsigned() default false;

	/**
	 * real database column type
	 */
	String dbType() default Strings.EMPTY;
	
	/**
	 * database default value
	 */
	String defaults() default Strings.EMPTY;
}
