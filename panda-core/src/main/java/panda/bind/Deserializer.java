package panda.bind;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * 
 *
 */
public interface Deserializer {
	/**
	 * Creates a object from a string, with a specific target class.<br>
	 */
	<T> T deserialize(CharSequence source, Type type);
	
	/**
	 * Creates a object from a reader, with a specific target class.<br>
	 */
	<T> T deserialize(Reader source, Type type);
}
