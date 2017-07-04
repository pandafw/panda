package panda.ioc.meta;

import panda.lang.Injector;
import panda.lang.Objects;

public class IocValue {

	public static final char TYPE_NULL = '-';
	public static final char TYPE_RAW = '*';

	public static final char TYPE_REF = '#';
	public static final char TYPE_EL = '$';
	public static final char TYPE_JSON = '!';

	private char type;
	private Object value;
	private Injector injector;
	private boolean required = true;

	public IocValue(char type) {
		this.type = type;
	}

	/**
	 * @param type the IocValue type
	 * @param value the value
	 */
	public IocValue(char type, Object value) {
		this.type = type;
		this.value = value;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the injector
	 */
	public Injector getInjector() {
		return injector;
	}

	/**
	 * @param injector the injector to set
	 */
	public void setInjector(Injector injector) {
		this.injector = injector;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("type", type)
				.append("value", value)
				.append("required", required)
				.append("injector", injector)
				.toString();
	}

}