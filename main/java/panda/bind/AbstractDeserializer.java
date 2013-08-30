package panda.bind;

import java.io.StringReader;
import java.lang.reflect.Type;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class AbstractDeserializer extends AbstractBinder implements Deserializer {
	private boolean ignoreReadonlyProperty = false;

	/**
	 * Constructor
	 */
	public AbstractDeserializer() {
	}

	/**
	 * @return the ignoreReadonlyProperty
	 */
	public boolean isIgnoreReadonlyProperty() {
		return ignoreReadonlyProperty;
	}

	/**
	 * @param ignoreReadonlyProperty the ignoreReadonlyProperty to set
	 */
	public void setIgnoreReadonlyProperty(boolean ignoreReadonlyProperty) {
		this.ignoreReadonlyProperty = ignoreReadonlyProperty;
	}

	/**
	 * Creates a object from a string, with a specific target class.<br>
	 */
	public <T> T deserialize(String source, Type type) {
		return deserialize(new StringReader(source), type);
	}
}
