package panda.ioc.meta;

import panda.lang.Injector;
import panda.lang.Objects;

/**
 * 描述了对象的一个值，这个值可以是构造函数的参数，也可以是一个字段的值。
 * <p>
 * 它由两个属性，一个是值的类型，另外一个是 value。
 * <h4>赋值约定</h4><br>
 * <ul>
 * <li>如果 type 是 "null"，则值会被认为是 null
 * <li>如果 value 是 字符串，数字，布尔，那么 type 必须为 "normal"或 null
 * <li>如果 value 是 数组， Collection 或 Map，那么类型也必须是 "normal"或 null，Ioc 容器的实现类会深层递归集合的每个元素。集合内的每个元素的值也可以是
 * IocValue，规则符合本约定
 * <li>如果 value 是 IocObject，则表示这个值是一个内部匿名对象，type 必须为 "inner" 或者 null
 * <li>如果 value 是字符串，表示另外一个对象的名称，type 必须是 "ref"
 * <li>如果 value 是字符串，表示一个环境变量(通过System.getenv(String))，type 必须是 "env"
 * <li>如果 value 是字符串，表示一个系统变量(通过System.getProperties().get(String))，type 必须是 "sys"
 * <li>如果 value 是字符串，表示一个文件路径，type 必须是 "file"
 * <li>如果 value 是字符串，表示一个 EL 调用，type 必须是 "el"，具体值的语法，请参看 ElValue 类的 JDoc，当然 Ioc 容器来解析执行它，不需要
 * IocLoader 操心 说明
 * <li>你的 ValueProxyMaker 可以扩展这个约定
 * </ul>
 * 
 */
public class IocValue {

	public static final char TYPE_NULL = '0';
	public static final char TYPE_NORMAL = '1';

	public static final char TYPE_REF = 'R';
	public static final char TYPE_EL = 'E';
	public static final char TYPE_JSON = 'J';

	private char type;
	private Object value;
	private Injector injector;
	private boolean required = true;

	public IocValue(char type) {
		this.type = type;
	}

	/**
	 * @param type
	 * @param value
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
