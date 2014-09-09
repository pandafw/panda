package panda.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.mvc.UrlMapping;
import panda.mvc.impl.UrlMappingImpl;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface UrlMappingBy {

	Class<? extends UrlMapping> value() default UrlMappingImpl.class;

	String[] args() default {};

}
