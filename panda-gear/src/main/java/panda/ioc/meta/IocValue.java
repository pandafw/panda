package panda.ioc.meta;

import java.lang.reflect.Type;

import panda.lang.Objects;

public class IocValue {

	public static final char KIND_NULL = '-';
	public static final char KIND_RAW = '*';
	public static final char KIND_INNER = 'i';

	public static final char KIND_REF = '#';
	public static final char KIND_EL = '$';
	public static final char KIND_JSON = '!';

	private char kind;
	private Type type;
	private Object value;
	private boolean required = true;

	public IocValue(char kind) {
		this.kind = kind;
	}

	public IocValue(char kind, Type type, Object value) {
		this.kind = kind;
		this.type = type;
		this.value = value;
	}

	public IocValue(char kind, Type type, Object value, boolean required) {
		this.kind = kind;
		this.type = type;
		this.value = value;
		this.required = required;
	}

	public char getKind() {
		return kind;
	}

	public void setKind(char kind) {
		this.kind = kind;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
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
				.append("kind", kind)
				.append("type", type)
				.append("value", value)
				.append("required", required)
				.toString();
	}

}
