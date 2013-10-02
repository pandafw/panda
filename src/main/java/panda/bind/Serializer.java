package panda.bind;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public interface Serializer {
	String serialize(Object src);

	void serialize(Object src, Appendable writer);
}
