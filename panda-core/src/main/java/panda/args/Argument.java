package panda.args;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Argument of the command line. This works mostly like {@link Option} except the following
 * differences.
 * <ol>
 * <li>Arguments have an index about their relative position on the command line.
 * </ol>
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD, PARAMETER })
public @interface Argument {
	/**
	 * Position of the argument.
	 * <p>
	 * If you define multiple single value properties to bind to arguments, they should have
	 * {@code index=0, index=1, index=2}, ... and so on.
	 * <p>
	 * Multiple value properties bound to arguments must be always the last entry.
	 */
	int index() default -1;

	/**
	 * Name of the argument.
	 */
	String name();

	/**
	 * See {@link Option#usage()}.
	 */
	String usage() default "";

	/**
	 * See {@link Option#required()}.
	 */
	boolean required() default false;

	/**
	 * See {@link Option#hidden()}.
	 */
	boolean hidden() default false;

}
