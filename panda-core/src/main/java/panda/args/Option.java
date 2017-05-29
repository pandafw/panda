package panda.args;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ResourceBundle;

/**
 * Marks a field/setter that receives a command line switch value.
 * <pre>
 * class Foo {
 * 	&#64;Option(name = 'c')
 * 	public boolean coin;
 * }
 * </pre>
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD, PARAMETER })
public @interface Option {
	/**
	 * short form option, such as <code>-f</code>.
	 */
	char opt() default ' ';

	/**
	 * long form option, such as <code>--long-option-name</code>.
	 */
	String option() default "";

	/**
	 * Sets the display name for the argument value.
	 *
	 * <pre>
	 * -x FOO  : blah blah blah
	 * </pre>
	 *
	 * You can replace the <code>FOO</code> token by using this parameter.
	 * <p>
	 * If unspecified, means this is a FLAG without VALUE.
	 */
	String arg() default "";

	/**
	 * Help string used to display the usage screen.
	 * <p>
	 * This parameter works in two ways. For a simple use, you can just encode the human-readable
	 * help string directly, and that will be used as the message. This is easier, but it doesn't
	 * support localization.
	 * <p>
	 * For more advanced use, this property is set to a key of a {@link ResourceBundle}. The actual
	 * message is obtained by querying a {@link ResourceBundle} instance supplied to
	 * {@link CmdLineParser} by this key. This allows the usage screen to be properly localized.
	 * <p>
	 * If this value is empty, the option will not be displayed in the usage screen.
	 */
	String usage() default "";

	/**
	 * Specify that the option is mandatory. a {@link CmdLineException} will
	 * be thrown if a required option is not present.
	 * <p>
	 * Note that in most of the command line interface design principles, options should be really
	 * optional. So use caution when using this flag.
	 */
	boolean required() default false;

	/**
	 * Specify that the option is hidden from the usage, by default.
	 */
	boolean hidden() default false;

	/**
	 * List of other options that this option depends on.
	 * <h3>Example</h3>
	 *
	 * <pre>
	 * &#64;Option(name = "-a")
	 * int a;
	 * // -b is not required but if it's provided, then a becomes required
	 * &#64;Option(name = "-b", depends = { "-a" })
	 * int b;
	 * </pre>
	 * <p>
	 * A {@link CmdLineException} will be thrown if options required by another one are not present.
	 * </p>
	 */
//TODO:	String[] depends() default {};

	/**
	 * List of other options that this option is incompatible with..
	 * <h3>Example</h3>
	 *
	 * <pre>
	 * &#64;Option(name = "-a")
	 * int a;
	 * // -h and -a cannot be specified together
	 * &#64;Option(name = "-h", forbids = { "-a" })
	 * boolean h;
	 * </pre>
	 * <p>
	 * A {@link CmdLineException} will be thrown if forbidden option combinations are present.
	 * </p>
	 */
//TODO:	String[] forbids() default {};
}
