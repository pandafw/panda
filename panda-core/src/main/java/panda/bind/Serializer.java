package panda.bind;


/**
 * 
 *
 */
public interface Serializer {
	String serialize(Object src);

	void serialize(Object src, Appendable writer);
}
