package panda.el.parse;

/**
 * a char queue for reader
 */
public class CharQueue {
	private CharSequence string;
	private int cursor;

	public CharQueue(CharSequence string) {
		this.string = string;
	}

	/**
	 * read current (offset: 0) character but not move to next
	 * @return the peeked character
	 */
	public char peek() {
		return peek(0);
	}

	/**
	 * read character at the specified offset but not move to next
	 * 
	 * @param offset offset
	 * @return the peeked character
	 */
	public char peek(int offset) {
		int idx = cursor + offset;
		return idx >= string.length() ? 0 : string.charAt(idx);
	}

	/**
	 * read and move to next
	 * @return the polled character
	 */
	public char poll() {
		char x = peek();
		cursor++;
		return x;
	}

	/**
	 * @return true if the queue is empty
	 */
	public boolean isEmpty() {
		return cursor >= string.length();
	}
}
