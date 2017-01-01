package panda.doc.markdown;

/**
 * An interface for emitting span elements. Currently only used for special links.
 */
public interface SpanEmitter {
	/**
	 * Emits a span element.
	 * 
	 * @param out The StringBuilder to append to.
	 * @param content The span's content.
	 */
	public void emitSpan(StringBuilder out, String content);
}
