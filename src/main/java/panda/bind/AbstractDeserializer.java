package panda.bind;

import java.io.Reader;
import java.lang.reflect.Type;

import panda.io.Streams;
import panda.io.stream.CharSequenceReader;
import panda.lang.reflect.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class AbstractDeserializer extends AbstractBinder implements Deserializer {
	private boolean ignoreReadonlyProperty = false;
	private boolean ignoreMissingProperty = false;

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
	 * @return the ignoreMissingProperty
	 */
	public boolean isIgnoreMissingProperty() {
		return ignoreMissingProperty;
	}

	/**
	 * @param ignoreMissingProperty the ignoreMissingProperty to set
	 */
	public void setIgnoreMissingProperty(boolean ignoreMissingProperty) {
		this.ignoreMissingProperty = ignoreMissingProperty;
	}

	/**
	 * Creates a object from a string, with a specific target class.<br>
	 */
	public <T> T deserialize(CharSequence source, Type type) {
		Reader r = new CharSequenceReader(source);
		try {
			return deserialize(r, type);
		}
		finally {
			Streams.safeClose(r);
		}
	}

	protected Type getArrayElementType(Type type) {
		Type etype = Types.getArrayElementType(type);
		return etype == null ? Object.class : etype;
	}
	
}
