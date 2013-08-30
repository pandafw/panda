package panda.dao.entity.annotation;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public @interface Index {

	boolean unique() default true;

	String name();

	String[] fields();

}
