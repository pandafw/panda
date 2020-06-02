package panda.ioc.meta;

import panda.lang.Objects;
import panda.lang.reflect.Injector;

public class IocParam {
	private IocValue[] values;
	private Injector injector;

	public IocParam() {
	}

	public IocParam(IocValue iv) {
		setValue(iv);
	}

	public IocParam(IocValue[] ivs) {
		setValues(ivs);
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
	 * @return the values
	 */
	public IocValue[] getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(IocValue[] values) {
		this.values = values;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(IocValue value) {
		this.values = new IocValue[] { value };
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("values", values)
				.append("injector", injector)
				.toString();
	}

}
