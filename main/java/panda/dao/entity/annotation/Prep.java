package panda.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对象插入前的自动执行
 * <p>
 * 本注解声明在一个 POJO 的数据库字段上（带有 '@Column' 注解的字段）<br>
 * 当插入一个对象之前，通过一个 SQL 从数据库中获取值，并赋予该字段。
 * <p>
 * <b style=color:red>注意：</b> 你的字段只能允许是字符型的，或者是整数型的。否则会报错。
 * 
 * @see panda.dao.entity.annotation.SQL
 * 
 * @author yf.frank.wang@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Prep {

	SQL[] value() default {};

}
